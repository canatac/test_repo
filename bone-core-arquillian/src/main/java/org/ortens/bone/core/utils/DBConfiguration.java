package org.ortens.bone.core.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class DBConfiguration {
	private static Logger logger = Logger.getLogger(DBConfiguration.class.getName());
	Properties projectProps;
	String fileName;
	
	private String dbDriver;
	private String dbUrl;
	private String dbHost;
	private String dbPort;
	private String dbName;
	private String dbUser;
	private String dbPwd;
	
	
	public Properties getProjectProps() {
		return projectProps;
	}

	public DBConfiguration(String fileName){
		this.fileName = fileName;
		try {
			this.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setDbDriver();
		this.setDbHost();
		this.setDbName();
		this.setDbPort();
		this.setDbPwd();
		this.setDbUrl();
		this.setDbUser();
	}
	
	public void load() throws IOException{
		projectProps = new Properties();
		this.projectProps.load(new BufferedInputStream(this.getClass().getResourceAsStream(this.fileName)));
	}
	public String getDbDriver() {
		return this.dbDriver;
	}

	public String getDbUrl() {
		return this.dbUrl;
	}

	public String getDbHost() {
		return this.dbHost;
	}

	public String getDbPort() {
		return this.dbPort;
	}

	public String getDbName() {
		return this.dbName;
	}

	public String getDbUser() {
		return this.dbUser;
	}

	public String getDbPwd() {
		return this.dbPwd;
	}

	public void setDbDriver() {
		this.dbDriver = this.projectProps.getProperty("DBDRIVER").toString();
	}

	public void setDbUrl() {
		this.dbUrl = this.projectProps.getProperty("DBURL").toString();
	}

	public void setDbHost() {
		this.dbHost = this.projectProps.getProperty("MYSQL_HOST").toString();
	}

	public void setDbPort() {
		this.dbPort = this.projectProps.getProperty("MYSQL_PORT").toString();
	}

	public void setDbName() {
		this.dbName = this.projectProps.getProperty("MYSQL_DB").toString();
	}

	public void setDbUser() {
		this.dbUser = this.projectProps.getProperty("MYSQL_USER").toString();
	}

	public void setDbPwd() {
		this.dbPwd = this.projectProps.getProperty("MYSQL_PASSWORD").toString();
	}
}
