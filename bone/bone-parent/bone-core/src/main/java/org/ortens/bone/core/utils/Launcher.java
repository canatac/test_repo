package org.ortens.bone.core.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.ortens.bone.core.service.Extractor;
import org.ortens.bone.core.service.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;

public class Launcher {
	private Logger logger = LoggerFactory.getLogger(Launcher.class);

	public Logger getLogger() {
		return logger;
	}

	
	/**
	 * @param args
	 * @throws ParseException 
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException, ParseException {
		new Launcher().run();
	}
	public void run() throws SQLException, ParseException{
		getLogger().debug("==>testOpenCSV");
		// boucle sur la lecture des CSV
		Path dirListing = FileUtils.toFile(
				this.getClass().getClassLoader().getResource("scan.listing"))
				.toPath();
		Charset charset = Charset.forName("UTF-8");
		BufferedReader reader;
		CSVReader csvReader = null;
		Map<String, String> enreg = null;
		Persister persister = new Persister();
		Extractor extractor = new Extractor();
		
		File[] filesList = null;
		try {
			reader = Files.newBufferedReader(dirListing, charset);
			String line = null;
			
			while ((line = reader.readLine()) != null) {
				line = line.concat("CRA\\");
				File source = new File(line);

				filesList = source.listFiles(new CsvFilenameFilter());
				for (File file : filesList) {
					if (file.exists()) {
						getLogger().debug(
								"\t Converting file : "
										+ file.getPath().toString());
						csvReader = new CSVReader(new FileReader(file.getPath()
								.toString()), ';');
						enreg = extractor.extractData(csvReader);
						// persist
						persister.persist(enreg);
					}
				}
			}

		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}

	}
	/**
	 * An instance of this class can be used to control the files returned be a
	 * call to the listFiles() method when made on an instance of the File class
	 * and that object refers to a folder/directory
	 */
	class CsvFilenameFilter implements FilenameFilter {

		public boolean accept(File file, String name) {
			return name.endsWith(".csv");
		}
	}	
}