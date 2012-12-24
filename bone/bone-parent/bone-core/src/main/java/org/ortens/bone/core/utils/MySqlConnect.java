package org.ortens.bone.core.utils;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.sql.Connection;


public class MySqlConnect {

	public static Logger logger = Logger.getLogger(MySqlConnect.class.getName());
	
	DBConfiguration config = null;
	
	public MySqlConnect(){
		config = new DBConfiguration("../DBProperties");
	}
	
	public Connection getConnection(){
		try {
			Class.forName(this.config.getDbDriver());		
			java.sql.Connection connection = DriverManager.getConnection(
					this.config.getDbUrl()				+
					this.config.getDbHost()				+
					":"									+
					this.config.getDbPort()				+
					"/"									+
					this.config.getDbName()				+
					"?user="							+
					this.config.getDbUser()				+
					"&password="						+
					this.config.getDbPwd()
			);
			
			
			if (!connection.getMetaData().supportsBatchUpdates()) {
		        throw new SQLException("Batch updates not supported");
		    }
			return connection;
			
		}
		catch (Exception e) {
			logger.log(Level.SEVERE,"Erreur grave de connexion à la base MySql");
		}
		return null;
	}
}
