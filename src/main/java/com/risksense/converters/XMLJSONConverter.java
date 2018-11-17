/**s
 * 
 */
package com.risksense.converters;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class to convert the provided json value to xml value
 * @author sujit
 *
 */
@Component("xmlJsonConverter")
public class XMLJSONConverter implements XMLJSONConverterI {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/* (non-Javadoc)
	 * @see com.risksense.converters.XMLJSONConverterI#convertJSONtoXML(java.io.File, java.io.File)
	 */
	@Override
	public void convertJSONtoXML(File json, File xml) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(json);
		logger.debug("Json Content :: {} ", objectMapper.writerWithDefaultPrettyPrinter().withDefaultPrettyPrinter().writeValueAsString(jsonNode));
		
		String xmlString = null;
		try {
			xmlString = prepareObject(null, jsonNode);
		} catch (ConvertorException e) {
			logger.error("Unable to convert json node to xml, please check json object", e);
			return;
		}
		
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(xml);
			fileWriter.write(xmlString);
		} finally {
			if(fileWriter != null) {
				fileWriter.close();
			}
		}
	}
	
	private String prepareObject(String fieldName, JsonNode jsonNode) throws ConvertorException {
		String innerBody = jsonNode.asText();
		Iterator<String> fieldNames = jsonNode.fieldNames();
		
		while(fieldNames.hasNext()){
			String name = fieldNames.next();
			innerBody += prepareObject(name, jsonNode.get(name));
	     }
		
		String nameAttr = fieldName == null ? "" : " name=\"" + fieldName + "\"";
		if(jsonNode.isNull()) {
			return "<null" + nameAttr + "/>";
		}
		
		if(jsonNode.isBoolean()) {
			return "<boolean" + nameAttr + ">" + innerBody + "</boolean>";
		}
		
		if(jsonNode.isNumber()) {
			return "<number" + nameAttr + ">" + innerBody + "</number>";
		}
		
		if(jsonNode.isTextual()) {
			return "<string" + nameAttr + ">" + innerBody + "</string>";
		}
		
		if(jsonNode.isObject()) {
			return "<object" + nameAttr + ">" + innerBody + "</object>";
		}
		
		if(jsonNode.isArray()) {
			for (final JsonNode objNode : jsonNode) {
				innerBody += prepareObject(null, objNode);
		    }
			return "<array" + nameAttr + ">" + innerBody + "</array>";
		}
		
		logger.error("Invalid content for json that is not handled");
		throw new ConvertorException("Invalid content for json that is not handled");
	}
	
}
