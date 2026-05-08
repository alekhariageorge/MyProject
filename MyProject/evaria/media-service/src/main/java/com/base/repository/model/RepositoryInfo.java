package com.base.repository.model;
import com.utils.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

 public class RepositoryInfo {

    private String name;

    private String definitionFilePath;

    private Map<String,RepositoryItemDescriptor> itemDescriptors = new LinkedHashMap<>();

    private Map<String, Map<String, RepositoryItem>> repositoryItems = new LinkedHashMap<>();

    public RepositoryInfo(final String repositoryXMLPath){
        this.name = this.getClass().toString();
        this.definitionFilePath = repositoryXMLPath;
        this.loadRepositoryXml(repositoryXMLPath);
    }

    private void loadRepositoryXml(String xmlPath) {

        try {
            InputStream inputStream =
                    getClass().getClassLoader()
                            .getResourceAsStream(xmlPath);

            if (inputStream == null) {
                throw new RuntimeException(
                        "Repository XML not found: " + xmlPath
                );
            }

            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(inputStream);

            document.getDocumentElement().normalize();

            Element repositoryElement =
                    document.getDocumentElement();

            this.name = repositoryElement.getAttribute("name");

            NodeList descriptorNodes =
                    document.getElementsByTagName("item-descriptor");

            for (int i = 0; i < descriptorNodes.getLength(); i++) {

                Element descriptorElement =
                        (Element) descriptorNodes.item(i);

                String descriptorName =
                        descriptorElement.getAttribute("name");

                Map<String, Class<?>> propertyDefinitions =
                        new HashMap<String, Class<?>>();

                NodeList propertyNodes =
                        descriptorElement.getElementsByTagName("property");

                for (int j = 0; j < propertyNodes.getLength(); j++) {

                    Element propertyElement =
                            (Element) propertyNodes.item(j);

                    String propertyName =
                            propertyElement.getAttribute("name");
                    String dataType =
                            propertyElement.getAttribute("data-type");

                    Class<?> javaType =
                            Utils.mapXmlTypeToJavaType(dataType);

                    propertyDefinitions.put(
                            propertyName,
                            javaType
                    );
                }

                RepositoryItemDescriptor descriptor  = new RepositoryItemDescriptor(
                        descriptorName,
                        propertyDefinitions
                );

                this.itemDescriptors.put(descriptorName, descriptor);
                this.repositoryItems.put(descriptorName, new LinkedHashMap<>());
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to load repository XML",
                    e
            );
        }
    }

    public void addItems(String xmlPath) {

        try {

            InputStream inputStream =
                    getClass().getClassLoader()
                            .getResourceAsStream(xmlPath);

            if (inputStream == null) {

                throw new RuntimeException(
                        "Add items XML not found: " + xmlPath
                );
            }

            Document document =
                    DocumentBuilderFactory
                            .newInstance()
                            .newDocumentBuilder()
                            .parse(inputStream);

            document.getDocumentElement().normalize();

            NodeList addItemNodes =
                    document.getElementsByTagName("add-item");
            for (int i = 0; i < addItemNodes.getLength(); i++) {

                Element addItemElement =
                        (Element) addItemNodes.item(i);

                String descriptorName =
                        addItemElement.getAttribute(
                                "item-descriptor"
                        );

                String repositoryId =
                        addItemElement.getAttribute("id");

                Map<String, Object> propertyValues =
                        new HashMap<>();

                NodeList propertyNodes =
                        addItemElement.getElementsByTagName(
                                "set-property"
                        );

                for (int j = 0; j < propertyNodes.getLength(); j++) {

                    Element propertyElement =
                            (Element) propertyNodes.item(j);

                    String propertyName =
                            propertyElement.getAttribute("name");

                    String propertyValue =
                            propertyElement
                                    .getTextContent()
                                    .trim();

                    propertyValues.put(
                            propertyName,
                            propertyValue
                    );
                }
                addItem(repositoryId, descriptorName, propertyValues );
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to load add-items XML",
                    e
            );
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String,RepositoryItemDescriptor> getItemDescriptors() {
        return itemDescriptors;
    }

    public void setItemDescriptors(Map<String,RepositoryItemDescriptor> itemDescriptors) {
        this.itemDescriptors = itemDescriptors;
    }

    public String getDefinitionFilePath() {
        return definitionFilePath;
    }

    public void setDefinitionFilePath(String definitionFilePath) {
        this.definitionFilePath = definitionFilePath;
    }

    public RepositoryItem getItemById(
            String itemDescriptorName,
            String repositoryId
    ){
        return getRepositoryItems().get(itemDescriptorName).get(repositoryId);
    }


    public void addItem(String repositoryId, final String descriptorName, Map<String, Object> propertyValues){
        RepositoryItemDescriptor itemDescriptor = this.getItemDescriptors().get(descriptorName);
        RepositoryItem item = new RepositoryItem(repositoryId, this, itemDescriptor, propertyValues);
        Map<String, RepositoryItem> items = this.getRepositoryItems().get(descriptorName);
        items.put(repositoryId, item);
    }

    public void removeItem(
            String itemDescriptorName,
            String repositoryId
    ){
        this.getRepositoryItems().get(itemDescriptorName).remove(repositoryId);
    }

     public Map<String, Map<String, RepositoryItem>> getRepositoryItems() {
         return repositoryItems;
     }

     public void setRepositoryItems(Map<String, Map<String, RepositoryItem>> repositoryItems) {
         this.repositoryItems = repositoryItems;
     }
}