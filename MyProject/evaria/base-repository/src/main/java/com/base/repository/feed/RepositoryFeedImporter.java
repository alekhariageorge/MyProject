package com.base.repository.feed;

import com.base.repository.model.RepositoryItemDescriptor;
import com.base.repository.value.RepositoryValueConverter;
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

public class RepositoryFeedImporter {

    private static final char BYTE_ORDER_MARK = '\uFEFF';

    private final RepositoryValueConverter valueConverter;

    public RepositoryFeedImporter(RepositoryValueConverter valueConverter) {
        this.valueConverter = valueConverter;
    }

    public List<FeedItem> importFeed(FeedResource feedResource,
                                     Map<String, RepositoryItemDescriptor> itemDescriptors) {
        try {
            byte[] feedBytes = feedResource.getBytes();
            FeedFormat feedFormat = detectFeedFormat(feedBytes);

            return switch (feedFormat) {
                case XML -> importXml(new ByteArrayInputStream(feedBytes));
                case CSV -> importCsv(new ByteArrayInputStream(feedBytes), itemDescriptors);
                case WORKBOOK -> importWorkbook(new ByteArrayInputStream(feedBytes), itemDescriptors);
                case SPREADSHEET_XML -> importSpreadsheetXml(new ByteArrayInputStream(feedBytes), itemDescriptors);
            };
        } catch (Exception e) {
            throw new RuntimeException("Failed to import repository feed: " + feedResource.getResolvedPath(), e);
        }
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

    private boolean isSpreadsheetXml(byte[] feedBytes) {
        String prefix = new String(feedBytes, 0, Math.min(feedBytes.length, 512), StandardCharsets.UTF_8);
        return prefix.contains("<Workbook")
                && prefix.contains("urn:schemas-microsoft-com:office:spreadsheet");
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

    private List<FeedItem> importXml(InputStream inputStream) {
        try {
            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(inputStream);

            document.getDocumentElement().normalize();

            List<FeedItem> feedItems = new ArrayList<>();
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

                feedItems.add(new FeedItem(descriptorName, repositoryId, propertyValues));
            }

            return feedItems;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load add-items XML", e);
        }
    }

    private List<FeedItem> importCsv(InputStream inputStream,
                                     Map<String, RepositoryItemDescriptor> itemDescriptors) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            List<FeedItem> feedItems = new ArrayList<>();
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
                    feedItems.add(toFeedItem(headers, values, itemDescriptors));
                }
            }

            return feedItems;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load add-items CSV", e);
        }
    }

    private List<FeedItem> importWorkbook(InputStream inputStream,
                                          Map<String, RepositoryItemDescriptor> itemDescriptors) {
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            DataFormatter formatter = new DataFormatter();
            Sheet sheet = workbook.getSheetAt(0);
            List<FeedItem> feedItems = new ArrayList<>();
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

                feedItems.add(toFeedItem(headers, values, itemDescriptors));
            }

            return feedItems;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load add-items workbook", e);
        }
    }

    private List<FeedItem> importSpreadsheetXml(InputStream inputStream,
                                                Map<String, RepositoryItemDescriptor> itemDescriptors) {
        try {
            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(inputStream);

            document.getDocumentElement().normalize();

            List<FeedItem> feedItems = new ArrayList<>();
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

                feedItems.add(toFeedItem(headers, values, itemDescriptors));
            }

            return feedItems;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load add-items spreadsheet XML", e);
        }
    }

    private FeedItem toFeedItem(List<String> headers,
                                List<String> values,
                                Map<String, RepositoryItemDescriptor> itemDescriptors) {
        Map<String, String> rowValues = toRowValueMap(headers, values);
        String descriptorName = requireColumnValue(rowValues, "item-descriptor", "itemDescriptor");
        String repositoryId = requireColumnValue(rowValues, "id", "repositoryId");
        RepositoryItemDescriptor descriptor = requireDescriptor(descriptorName, itemDescriptors);
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

            Class<?> targetType = descriptor.getPropertyDefinitions().get(propertyName);
            propertyValues.put(propertyName, valueConverter.convert(propertyValue, targetType));
        }

        return new FeedItem(descriptorName, repositoryId, propertyValues);
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

    private RepositoryItemDescriptor requireDescriptor(String descriptorName,
                                                       Map<String, RepositoryItemDescriptor> itemDescriptors) {
        RepositoryItemDescriptor descriptor = itemDescriptors.get(descriptorName);
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
}
