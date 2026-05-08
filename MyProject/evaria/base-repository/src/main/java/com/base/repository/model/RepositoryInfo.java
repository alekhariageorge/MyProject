package com.base.repository.model;

import com.utils.Utils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RepositoryInfo {

    private static final char BYTE_ORDER_MARK = '\uFEFF';

    private static final String DEFAULT_FEED_PATH = "repository/add-items";

    private static final String[] FEED_EXTENSIONS = {".xml", ".csv", ".xls", ".xlsx"};

    private String name;

    private String definitionFilePath;

    private Map<String, RepositoryItemDescriptor> itemDescriptors = new LinkedHashMap<>();

    private Map<String, Map<String, RepositoryItem>> repositoryItems = new LinkedHashMap<>();

    public RepositoryInfo(final String repositoryXMLPath) {
        this.name = this.getClass().toString();
        this.definitionFilePath = repositoryXMLPath;
        this.loadRepositoryXml(repositoryXMLPath);
        this.addItems(DEFAULT_FEED_PATH);
    }

    private void loadRepositoryXml(String xmlPath) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(xmlPath);

            if (inputStream == null) {
                throw new RuntimeException("Repository XML not found: " + xmlPath);
            }

            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(inputStream);

            document.getDocumentElement().normalize();

            Element repositoryElement = document.getDocumentElement();
            this.name = repositoryElement.getAttribute("name");

            NodeList descriptorNodes = document.getElementsByTagName("item-descriptor");

            for (int i = 0; i < descriptorNodes.getLength(); i++) {
                Element descriptorElement = (Element) descriptorNodes.item(i);
                String descriptorName = descriptorElement.getAttribute("name");
                Map<String, Class<?>> propertyDefinitions = new HashMap<>();

                NodeList propertyNodes = descriptorElement.getElementsByTagName("property");

                for (int j = 0; j < propertyNodes.getLength(); j++) {
                    Element propertyElement = (Element) propertyNodes.item(j);
                    String propertyName = propertyElement.getAttribute("name");
                    String dataType = propertyElement.getAttribute("data-type");
                    Class<?> javaType = Utils.mapXmlTypeToJavaType(dataType);

                    propertyDefinitions.put(propertyName, javaType);
                }

                RepositoryItemDescriptor descriptor = new RepositoryItemDescriptor(
                        descriptorName,
                        propertyDefinitions
                );

                this.itemDescriptors.put(descriptorName, descriptor);
                this.repositoryItems.put(descriptorName, new LinkedHashMap<>());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load repository XML", e);
        }
    }

    public void addItems(String feedPath) {
        try {
            byte[] feedBytes = loadFeedBytes(feedPath);
            FeedFormat feedFormat = detectFeedFormat(feedBytes);

            switch (feedFormat) {
                case XML -> addItemsFromXml(new ByteArrayInputStream(feedBytes));
                case CSV -> addItemsFromCsv(new ByteArrayInputStream(feedBytes));
                case WORKBOOK -> addItemsFromWorkbook(new ByteArrayInputStream(feedBytes));
                case SPREADSHEET_XML -> addItemsFromSpreadsheetXml(new ByteArrayInputStream(feedBytes));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load repository feed: " + feedPath, e);
        }
    }

    public void importItems(String feedPath) {
        addItems(feedPath);
    }

    private byte[] loadFeedBytes(String feedPath) throws Exception {
        InputStream inputStream = openFeedResource(feedPath);
        if (inputStream == null) {
            throw new RuntimeException("Repository feed not found: " + feedPath);
        }
        return inputStream.readAllBytes();
    }

    private InputStream openFeedResource(String feedPath) {
        if (!hasFileExtension(feedPath)) {
            for (String extension : FEED_EXTENSIONS) {
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream(feedPath + extension);
                if (inputStream != null) {
                    return inputStream;
                }
            }
        }

        return getClass().getClassLoader().getResourceAsStream(feedPath);
    }

    private boolean hasFileExtension(String feedPath) {
        int lastSlashIndex = Math.max(feedPath.lastIndexOf('/'), feedPath.lastIndexOf('\\'));
        int lastDotIndex = feedPath.lastIndexOf('.');
        return lastDotIndex > lastSlashIndex;
    }

    private FeedFormat detectFeedFormat(byte[] feedBytes) {
        if (isWorkbook(feedBytes)) {
            return FeedFormat.WORKBOOK;
        }
        if (isSpreadsheetXml(feedBytes)) {
            return FeedFormat.SPREADSHEET_XML;
        }
        if (isXml(feedBytes)) {
            return FeedFormat.XML;
        }
        return FeedFormat.CSV;
    }

    private boolean isXml(byte[] feedBytes) {
        int startIndex = hasUtf8ByteOrderMark(feedBytes) ? 3 : 0;

        for (int i = startIndex; i < feedBytes.length; i++) {
            char character = (char) feedBytes[i];
            if (!Character.isWhitespace(character)) {
                return character == '<';
            }
        }
        return false;
    }

    private boolean hasUtf8ByteOrderMark(byte[] feedBytes) {
        return feedBytes.length >= 3
                && (feedBytes[0] & 0xFF) == 0xEF
                && (feedBytes[1] & 0xFF) == 0xBB
                && (feedBytes[2] & 0xFF) == 0xBF;
    }

    private boolean isSpreadsheetXml(byte[] feedBytes) {
        String prefix = new String(feedBytes, 0, Math.min(feedBytes.length, 512), StandardCharsets.UTF_8);
        return prefix.contains("<Workbook")
                && prefix.contains("urn:schemas-microsoft-com:office:spreadsheet");
    }

    private boolean isWorkbook(byte[] feedBytes) {
        boolean zipBasedWorkbook = feedBytes.length >= 2
                && feedBytes[0] == 'P'
                && feedBytes[1] == 'K';
        boolean oleWorkbook = feedBytes.length >= 8
                && (feedBytes[0] & 0xFF) == 0xD0
                && (feedBytes[1] & 0xFF) == 0xCF
                && (feedBytes[2] & 0xFF) == 0x11
                && (feedBytes[3] & 0xFF) == 0xE0
                && (feedBytes[4] & 0xFF) == 0xA1
                && (feedBytes[5] & 0xFF) == 0xB1
                && (feedBytes[6] & 0xFF) == 0x1A
                && (feedBytes[7] & 0xFF) == 0xE1;
        return zipBasedWorkbook || oleWorkbook;
    }

    private void addItemsFromXml(InputStream inputStream) {
        try {
            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(inputStream);

            document.getDocumentElement().normalize();

            NodeList addItemNodes = document.getElementsByTagName("add-item");
            for (int i = 0; i < addItemNodes.getLength(); i++) {
                Element addItemElement = (Element) addItemNodes.item(i);
                String descriptorName = addItemElement.getAttribute("item-descriptor");
                String repositoryId = addItemElement.getAttribute("id");
                Map<String, Object> propertyValues = new HashMap<>();

                NodeList propertyNodes = addItemElement.getElementsByTagName("set-property");

                for (int j = 0; j < propertyNodes.getLength(); j++) {
                    Element propertyElement = (Element) propertyNodes.item(j);
                    String propertyName = propertyElement.getAttribute("name");
                    String propertyValue = propertyElement.getTextContent().trim();

                    propertyValues.put(propertyName, propertyValue);
                }
                addItem(repositoryId, descriptorName, propertyValues);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load add-items XML", e);
        }
    }

    private void addItemsFromCsv(InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            List<String> headers = null;
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                List<String> values = parseCsvLine(line);
                if (headers == null) {
                    headers = normalizeHeaders(values);
                    continue;
                }

                if (!isBlankRow(values)) {
                    addItemFromFeedRow(headers, values);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load add-items CSV", e);
        }
    }

    private void addItemsFromWorkbook(InputStream inputStream) {
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            DataFormatter formatter = new DataFormatter();
            Sheet sheet = workbook.getSheetAt(0);
            List<String> headers = null;

            for (Row row : sheet) {
                List<String> values = readWorkbookRow(row, formatter);
                if (isBlankRow(values)) {
                    continue;
                }

                if (headers == null) {
                    headers = normalizeHeaders(values);
                    continue;
                }

                addItemFromFeedRow(headers, values);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load add-items workbook", e);
        }
    }

    private void addItemsFromSpreadsheetXml(InputStream inputStream) {
        try {
            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(inputStream);

            document.getDocumentElement().normalize();

            NodeList rowNodes = document.getElementsByTagName("Row");
            List<String> headers = null;

            for (int i = 0; i < rowNodes.getLength(); i++) {
                Element rowElement = (Element) rowNodes.item(i);
                List<String> values = readSpreadsheetXmlRow(rowElement);
                if (isBlankRow(values)) {
                    continue;
                }

                if (headers == null) {
                    headers = normalizeHeaders(values);
                    continue;
                }

                addItemFromFeedRow(headers, values);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load add-items spreadsheet XML", e);
        }
    }

    private void addItemFromFeedRow(List<String> headers, List<String> values) {
        Map<String, String> rowValues = toRowValueMap(headers, values);
        String descriptorName = requireColumnValue(rowValues, "item-descriptor", "itemDescriptor");
        String repositoryId = requireColumnValue(rowValues, "id", "repositoryId");
        RepositoryItemDescriptor descriptor = requireDescriptor(descriptorName);
        Map<String, Object> propertyValues = new HashMap<>();

        for (Map.Entry<String, String> entry : rowValues.entrySet()) {
            String propertyName = entry.getKey();
            String propertyValue = entry.getValue();

            if (isMetadataColumn(propertyName) || propertyValue.isBlank()) {
                continue;
            }

            if (!descriptor.getPropertyDefinitions().containsKey(propertyName)) {
                throw new RuntimeException(
                        "Unknown property '" + propertyName + "' for item descriptor '" + descriptorName + "'"
                );
            }

            propertyValues.put(propertyName, propertyValue);
        }

        addItem(repositoryId, descriptorName, propertyValues);
    }

    private Map<String, String> toRowValueMap(List<String> headers, List<String> values) {
        Map<String, String> rowValues = new LinkedHashMap<>();

        for (int i = 0; i < headers.size(); i++) {
            String header = headers.get(i);
            if (header.isBlank()) {
                continue;
            }

            String value = i < values.size() ? values.get(i).trim() : "";
            rowValues.put(header, value);
        }

        return rowValues;
    }

    private String requireColumnValue(Map<String, String> rowValues, String... columnNames) {
        for (String columnName : columnNames) {
            String value = rowValues.get(columnName);
            if (value != null && !value.isBlank()) {
                return value;
            }
        }

        throw new RuntimeException("Feed row is missing required column: " + String.join(" or ", columnNames));
    }

    private RepositoryItemDescriptor requireDescriptor(String descriptorName) {
        RepositoryItemDescriptor descriptor = this.itemDescriptors.get(descriptorName);
        if (descriptor == null) {
            throw new RuntimeException("Unknown item descriptor: " + descriptorName);
        }
        return descriptor;
    }

    private boolean isMetadataColumn(String columnName) {
        return "item-descriptor".equals(columnName)
                || "itemDescriptor".equals(columnName)
                || "id".equals(columnName)
                || "repositoryId".equals(columnName);
    }

    private List<String> normalizeHeaders(List<String> headers) {
        List<String> normalizedHeaders = new ArrayList<>();
        for (int i = 0; i < headers.size(); i++) {
            String header = headers.get(i).trim();
            if (i == 0 && !header.isEmpty() && header.charAt(0) == BYTE_ORDER_MARK) {
                header = header.substring(1);
            }
            normalizedHeaders.add(header);
        }
        return normalizedHeaders;
    }

    private List<String> readWorkbookRow(Row row, DataFormatter formatter) {
        List<String> values = new ArrayList<>();
        short lastCellNumber = row.getLastCellNum();

        if (lastCellNumber < 0) {
            return values;
        }

        for (int i = 0; i < lastCellNumber; i++) {
            Cell cell = row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            values.add(cell == null ? "" : formatter.formatCellValue(cell).trim());
        }

        return values;
    }

    private List<String> readSpreadsheetXmlRow(Element rowElement) {
        List<String> values = new ArrayList<>();
        NodeList cellNodes = rowElement.getElementsByTagName("Cell");

        for (int i = 0; i < cellNodes.getLength(); i++) {
            Element cellElement = (Element) cellNodes.item(i);
            String indexValue = cellElement.getAttribute("ss:Index");
            if (!indexValue.isBlank()) {
                int targetIndex = Integer.parseInt(indexValue) - 1;
                while (values.size() < targetIndex) {
                    values.add("");
                }
            }

            NodeList dataNodes = cellElement.getElementsByTagName("Data");
            String cellValue = dataNodes.getLength() == 0 ? "" : dataNodes.item(0).getTextContent().trim();
            values.add(cellValue);
        }

        return values;
    }

    private List<String> parseCsvLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder value = new StringBuilder();
        boolean quoted = false;

        for (int i = 0; i < line.length(); i++) {
            char character = line.charAt(i);
            if (character == '"') {
                boolean escapedQuote = quoted && i + 1 < line.length() && line.charAt(i + 1) == '"';
                if (escapedQuote) {
                    value.append('"');
                    i++;
                } else {
                    quoted = !quoted;
                }
            } else if (character == ',' && !quoted) {
                values.add(value.toString().trim());
                value.setLength(0);
            } else {
                value.append(character);
            }
        }

        if (quoted) {
            throw new RuntimeException("CSV row has an unclosed quoted value");
        }

        values.add(value.toString().trim());
        return values;
    }

    private boolean isBlankRow(List<String> values) {
        for (String value : values) {
            if (!value.isBlank()) {
                return false;
            }
        }
        return true;
    }

    private enum FeedFormat {
        XML,
        CSV,
        WORKBOOK,
        SPREADSHEET_XML
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, RepositoryItemDescriptor> getItemDescriptors() {
        return itemDescriptors;
    }

    public void setItemDescriptors(Map<String, RepositoryItemDescriptor> itemDescriptors) {
        this.itemDescriptors = itemDescriptors;
    }

    public String getDefinitionFilePath() {
        return definitionFilePath;
    }

    public void setDefinitionFilePath(String definitionFilePath) {
        this.definitionFilePath = definitionFilePath;
    }

    public RepositoryItem getItemById(String itemDescriptorName, String repositoryId) {
        return getRepositoryItems().get(itemDescriptorName).get(repositoryId);
    }

    public void addItem(String repositoryId, final String descriptorName, Map<String, Object> propertyValues) {
        RepositoryItemDescriptor itemDescriptor = this.getItemDescriptors().get(descriptorName);
        RepositoryItem item = new RepositoryItem(repositoryId, this, itemDescriptor, propertyValues);
        Map<String, RepositoryItem> items = this.getRepositoryItems().get(descriptorName);
        items.put(repositoryId, item);
    }

    public void removeItem(String itemDescriptorName, String repositoryId) {
        this.getRepositoryItems().get(itemDescriptorName).remove(repositoryId);
    }

    public Map<String, Map<String, RepositoryItem>> getRepositoryItems() {
        return repositoryItems;
    }

    public void setRepositoryItems(Map<String, Map<String, RepositoryItem>> repositoryItems) {
        this.repositoryItems = repositoryItems;
    }
}
