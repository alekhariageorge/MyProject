package com.base.repository.definition;

import com.base.repository.model.RepositoryItemDescriptor;
import com.utils.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class RepositoryDefinitionLoader {

    public RepositoryDefinition load(InputStream inputStream) {
        try {
            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(inputStream);

            document.getDocumentElement().normalize();

            Element repositoryElement = document.getDocumentElement();
            String repositoryName = resolveRepositoryName(repositoryElement);
            Map<String, RepositoryItemDescriptor> itemDescriptors = new LinkedHashMap<>();

            NodeList descriptorNodes = document.getElementsByTagName("item-descriptor");
            for (int i = 0; i < descriptorNodes.getLength(); i++) {
                Element descriptorElement = (Element) descriptorNodes.item(i);
                String descriptorName = descriptorElement.getAttribute("name");
                Map<String, Class<?>> propertyDefinitions = loadPropertyDefinitions(descriptorElement);

                itemDescriptors.put(
                        descriptorName,
                        new RepositoryItemDescriptor(descriptorName, propertyDefinitions)
                );
            }

            return new RepositoryDefinition(repositoryName, itemDescriptors);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load repository XML", e);
        }
    }

    private Map<String, Class<?>> loadPropertyDefinitions(Element descriptorElement) {
        Map<String, Class<?>> propertyDefinitions = new LinkedHashMap<>();
        NodeList propertyNodes = descriptorElement.getElementsByTagName("property");

        for (int i = 0; i < propertyNodes.getLength(); i++) {
            Element propertyElement = (Element) propertyNodes.item(i);
            String propertyName = propertyElement.getAttribute("name");
            String dataType = propertyElement.getAttribute("data-type");

            propertyDefinitions.put(propertyName, Utils.mapXmlTypeToJavaType(dataType));
        }

        return propertyDefinitions;
    }

    private String resolveRepositoryName(Element repositoryElement) {
        String repositoryName = repositoryElement.getAttribute("name");
        if (!repositoryName.isBlank()) {
            return repositoryName;
        }

        NodeList nameNodes = repositoryElement.getElementsByTagName("name");
        if (nameNodes.getLength() > 0) {
            return nameNodes.item(0).getTextContent().trim();
        }

        return repositoryElement.getTagName();
    }
}
