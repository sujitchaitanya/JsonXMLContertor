/**
 * 
 */
package com.risksense;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.risksense.converters.ConverterFactory;

/**
 * @author sujit
 *
 */
@Component
public class JsonXMLConvertorRunner implements ApplicationRunner {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final String JSON_ARG_NAME = "jsonFile";
	private static final String XML_ARG_NAME = "xmlFile";
	
	/* (non-Javadoc)
	 * @see org.springframework.boot.CommandLineRunner#run(java.lang.String[])
	 */
	public void run(String... args) throws Exception {
		logger.info("Running with " + Arrays.deepToString(args));
		File json = new File(args[0]);
		File xml = new File(args[1]);
		ConverterFactory.createXMLJSONConverter().convertJSONtoXML(json, xml);
	}

	@Override
	public void run(ApplicationArguments applicationArguments) throws Exception {
		logger.info("Running with Arguments :: {}", Arrays.deepToString(applicationArguments.getSourceArgs()));
		
		boolean validJsonFile = false, validXMLFile = false;
		List<String> jsonAgruments = applicationArguments.getOptionValues(JSON_ARG_NAME);
		List<String> xmlArguments  =applicationArguments.getOptionValues(XML_ARG_NAME);
		String jsonFilePath = null, xmlFilePath = null;
		
		if(jsonAgruments == null || jsonAgruments.isEmpty()) {
			logger.error("Please provide json file with argument " + JSON_ARG_NAME);
		} else if(jsonAgruments.size() > 1) {
			logger.error("Please provide single argument for json file with argument " + JSON_ARG_NAME);
		} else {
			jsonFilePath = jsonAgruments.get(0);
			if(Files.isReadable(Paths.get(jsonFilePath))) {
				validJsonFile = true;
			} else {
				logger.info("Please provide vlaid json file");
			}
		}
		
		if(xmlArguments == null || xmlArguments.isEmpty()) {
			logger.error("Please provide xml file with argument " + XML_ARG_NAME);
		} else if(xmlArguments.size() > 1) {
			logger.error("Please provide single argument for xml file with argument " + XML_ARG_NAME);
		} else {
			xmlFilePath = xmlArguments.get(0);
			if(Files.isWritable(Paths.get(xmlFilePath))) {
				validXMLFile = true;
			} else {
				logger.info("Please provide vlaid json file");
			}
		}
		
		if(validJsonFile && validXMLFile) {
			File json = new File(jsonFilePath);
			File xml = new File(xmlFilePath);
			ConverterFactory.createXMLJSONConverter().convertJSONtoXML(json, xml);
		}
	}
}
