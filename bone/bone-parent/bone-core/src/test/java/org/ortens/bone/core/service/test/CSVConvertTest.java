package org.ortens.bone.core.service.test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.ortens.bone.core.service.ToCSV;
import org.ortens.bone.core.utils.Launcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CSVConvertTest {

	private Logger logger = LoggerFactory.getLogger(CSVConvertTest.class);

	public Logger getLogger() {
		return logger;
	}

	// @Test
	public void testConvertOneSheetToCSV() {
		getLogger().info("==> testConvertOneSheetToCSV()");
		String[] args = {
				"C:\\Documents and Settings\\can.atac-ext\\Mes documents\\ORTENS\\OCTOBRE2012\\CRA ORTENS.xls",
				"C:\\Documents and Settings\\can.atac-ext\\Mes documents\\ORTENS\\OCTOBRE2012\\CRA\\",
				";", "0", "CRA 10 12" };

		File source = new File(
				"C:\\Documents and Settings\\can.atac-ext\\Mes documents\\ORTENS\\OCTOBRE2012\\");

		// Check that the source file/folder exists.
		if (!source.exists()) {
			throw new IllegalArgumentException("The source for the Excel "
					+ "file(s) cannot be found.");
		}

		ToCSV.main(args);
	}

	// @Test
	public void testConvertAnotherSheetToCSV() {
		getLogger().info("\n==> testConvertAnotherSheetToCSV()");
		String[] args = {
				"C:\\Documents and Settings\\can.atac-ext\\Mes documents\\ORTENS\\OCTOBRE2012\\CRA ORTENS.xls",
				"C:\\Documents and Settings\\can.atac-ext\\Mes documents\\ORTENS\\OCTOBRE2012\\FRAIS\\",
				";", "0", "FRAIS MISSION 10 12" };
		ToCSV.main(args);
	}

	// @Test
	public void testConvertMultipleFilesToCSV() {
		getLogger().info("==> testConvertMultipleFilesToCSV()");

		List<String> dirs = Arrays
				.asList("C:\\Documents and Settings\\can.atac-ext\\Mes documents\\ORTENS\\OCTOBRE2012\\",
						"C:\\Documents and Settings\\can.atac-ext\\Mes documents\\ORTENS\\NOVEMBRE2012\\");
		for (String dir : dirs) {
			getLogger().info("\t Converting file : " + dir);
			String[] args = { dir, dir.concat("\\CRA\\"), ";", "0", "CRA" };
			ToCSV.main(args);
		}
	}

	//
	// @Test
	public void testGetListing_absolutePath() {
		getLogger().info("==> testGetListing_absolutePath");

		Path dirListing = Paths
				.get("C:\\Documents and Settings\\can.atac-ext\\Mes documents\\GitHub\\test_repo\\bone\\bone-parent\\bone-core\\src\\test\\resources\\scan.listing");
		Charset charset = Charset.forName("UTF-8");
		BufferedReader reader;
		try {
			reader = Files.newBufferedReader(dirListing, charset);
			String line = null;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}
	}

	//
	// @Test
	public void testGetListing_relativePath() {
		getLogger().info("==> testGetListing_relativePath");
		Path dirListing = FileUtils.toFile(
				this.getClass().getClassLoader().getResource("scan.listing"))
				.toPath();
		Charset charset = Charset.forName("UTF-8");
		BufferedReader reader;
		try {
			reader = Files.newBufferedReader(dirListing, charset);
			String line = null;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}
	}

	// @Test
	public void testGetListingXLSToConvert_FRAIS() {
		getLogger().info("==> testGetListingXLSToConvert_FRAIS");
		Path dirListing = FileUtils.toFile(
				this.getClass().getClassLoader().getResource("scan.listing"))
				.toPath();
		Charset charset = Charset.forName("UTF-8");
		BufferedReader reader;
		try {
			reader = Files.newBufferedReader(dirListing, charset);
			String line = null;

			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				getLogger().info("\t Converting file : " + line);
				String[] args = { line, line.concat("\\FRAIS\\"), ";", "0",
						"FRAIS" };
				ToCSV.main(args);
			}

		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}
	}

	// @Test
	public void testGetListingXLSToConvert_CRA() {
		getLogger().info("==> testGetListingXLSToConvert_CRA");
		Path dirListing = FileUtils.toFile(
				this.getClass().getClassLoader().getResource("scan.listing"))
				.toPath();
		Charset charset = Charset.forName("UTF-8");
		BufferedReader reader;
		try {
			reader = Files.newBufferedReader(dirListing, charset);
			String line = null;

			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				getLogger().info("\t Converting file : " + line);
				String[] args = { line, line.concat("\\CRA\\"), ";", "0", "CRA" };
				ToCSV.main(args);
			}

		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}
	}

	// @Test
	public void testRegexp() {
		getLogger().info("==> testRegexp");
		Pattern p = Pattern.compile("a*b");
		Matcher m = p.matcher("aaaaab");
		boolean b = m.matches();
		getLogger().info(b + "<=== matches ?");

		Matcher m1 = p.matcher("baaaab");
		boolean b1 = m1.matches();
		getLogger().info(b1 + "<=== matches ?");

		String s = "FRAIS 54 54 ";
		String pattern = "FRAIS";
		boolean b2 = s.matches(".*" + pattern + ".*");
		getLogger().info(b2 + "<=== FRAIS matches ?");

		String s3 = " CRA 54 54 ";
		String pattern3 = "CRA";
		boolean b3 = s3.matches(".*" + pattern3 + ".*");
		getLogger().info(b3 + "<=== CRA matches ?");

	}

	// @Test
	public void testGetRowByTemplate() {
		getLogger().info("==>testGetRowByTemplate");
		// create and load properties
		String propertiesFile = "./template_cra.properties";
		Properties projectProps = new Properties();
		try {
			projectProps.load(new BufferedInputStream(this.getClass()
					.getClassLoader().getResourceAsStream(propertiesFile)));
		} catch (IOException e) {
			assert (false);
		}
	}

	// @Test
	public void testConvertXLSByTemplate() {
		getLogger().info("==>testConvertXLSByTemplate");
		// create and load properties
		String propertiesFile = "./template_cra.properties";
		Properties projectProps = new Properties();
		try {
			projectProps.load(new BufferedInputStream(this.getClass()
					.getClassLoader().getResourceAsStream(propertiesFile)));
		} catch (IOException e) {
			assert (false);
		}
		Path dirListing = FileUtils.toFile(
				this.getClass().getClassLoader().getResource("scan.listing"))
				.toPath();
		Charset charset = Charset.forName("UTF-8");
		BufferedReader reader;
		try {
			reader = Files.newBufferedReader(dirListing, charset);
			String line = null;

			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				getLogger().info("\t Converting file : " + line);
				String[] args = { line, line.concat("\\CRA\\"), ";", "0", "CRA" };
				int[] rows = {

						Integer.parseInt(projectProps.get("DAY_OF_MONTH")
								.toString()),
						Integer.parseInt(projectProps.get("DAY_OF_WEEK")
								.toString()),
						Integer.parseInt(projectProps.get("PRODUCTION_AM")
								.toString()),
						Integer.parseInt(projectProps.get("PRODUCTION_PM")
								.toString()),
						Integer.parseInt(projectProps.get("DATE")
								.toString()),
						Integer.parseInt(projectProps.get("CLIENT_NAME")
								.toString()),
						Integer.parseInt(projectProps.get("EMPLOYEE_NAME")
								.toString()),
						Integer.parseInt(projectProps.get("EMPLOYEE_SURNAME")
								.toString()) };

				ToCSV.config(rows);

				ToCSV.main(args);
			}

		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}
	}


	// @Test
	public void testGetDate() {
		getLogger().info("==>testGetDate");

		BufferedReader reader;
		// DOESN'T WORK !! Charset charset = Charset.forName("UTF-8");

		try {
			File file = new File(
					"C:\\Documents and Settings\\can.atac-ext\\Mes documents\\ORTENS\\JUILLET2011\\CRA\\CRA ORTENS.csv");
			Path path = file.toPath();
			String line = null;
			reader = Files.newBufferedReader(path, Charset.defaultCharset());
			int i = 0;
			if (file.exists()) {
				while ((line = reader.readLine()) != null) {
					i++;
					String[] dataArray = line.split(";");

					System.out.println("====== " + i + " ==========");
					System.out.println("longueur : " + dataArray.length);
					System.out.println(dataArray[4]);
					for (String item : dataArray) {
						System.out.println(item);
					}
					System.out.println("================");

				}
			}

		} catch (MalformedURLException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}



	/**
	 * 1 - Lit les répertoires dans 'scan.listing'
	 * 2 - Concatène 'CRA' : nom du sous-répertoire contenant les .csv à lire
	 * 3 - Liste les fichiers et les filtre en fonction de leur extension 'csv'
	 * 4 - Extrait les données
	 * 5 - Persiste les données en base
	 */
	@Test
	public void testOpenCSV() throws IOException, SQLException, ParseException {
		getLogger().info("==>testOpenCSV");
		new Launcher().run();
	}
	
	//@Test
	public void testFormatDate() throws ParseException{
		String input = "juil.-11";
	    SimpleDateFormat format = new SimpleDateFormat("MMM-yy");
	    Date date = format.parse(input);
	    System.out.println(date); 
	}
}