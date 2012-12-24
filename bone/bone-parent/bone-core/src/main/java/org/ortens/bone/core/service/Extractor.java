package org.ortens.bone.core.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;

public class Extractor {
	private Logger logger = LoggerFactory.getLogger(Extractor.class);

	public Logger getLogger() {
		return logger;
	}
	

	public Map<String, String> extractData(CSVReader reader)
			throws IOException, ParseException {
		String[] nextLine;
		int i = 0;
		Map<String, String> enreg = new HashMap<String, String>();
		String propertiesFile = "./field.properties";
		Properties projectProps = new Properties();
		
		projectProps.load(new BufferedInputStream(this.getClass()
				.getClassLoader().getResourceAsStream(propertiesFile)));	
		
		while ((nextLine = reader.readNext()) != null) {
			// nextLine[] is an array of values from the line
			i++;
			getLogger().debug("=======" + i + "=========");
			getLogger().debug(nextLine[4]);
			getLogger().debug("=======" + i + "=========");
			if (i == 3) {
				getLogger().debug("TOTAL : " + nextLine[Integer.parseInt(projectProps.get("TOTAL").toString())]);
				enreg.put("TOTAL", nextLine[Integer.parseInt(projectProps.get("TOTAL").toString())]);
			}
			if (i == 5) {
				getLogger().debug("MONTH : " + nextLine[Integer.parseInt(projectProps.get("MONTH").toString())]);
				enreg.put("MONTH", nextLine[Integer.parseInt(projectProps.get("MONTH").toString())]);
			}
			if (i == 6) {
				getLogger().debug("CLIENT_NAME : " + nextLine[Integer.parseInt(projectProps.get("CLIENT_NAME").toString())]);
				enreg.put("CLIENT_NAME", nextLine[Integer.parseInt(projectProps.get("CLIENT_NAME").toString())]);
			}
			if (i == 7) {
				getLogger().debug("SURNAME : " + nextLine[2]);
				enreg.put("SURNAME", nextLine[2]);
			}
			if (i == 8) {
				getLogger().debug("NAME : " + nextLine[2]);
				enreg.put("NAME", nextLine[2]);
			}
		}
		return enreg;
	}

}
