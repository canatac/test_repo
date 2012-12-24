package org.ortens.bone.core.service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

import org.ortens.bone.core.utils.MySqlConnect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Persister {

	private Logger logger = LoggerFactory.getLogger(Persister.class);

	public Logger getLogger() {
		return logger;
	}
	
	public void persist(Map<String, String> enreg) throws SQLException, ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-yy");
		PreparedStatement statement = null;
		MySqlConnect mysqlDb = new MySqlConnect();
		java.sql.Connection connection = mysqlDb.getConnection();

		String sqlInstruction = "INSERT INTO activity (employee_name,employee_surname,client_name,month,total) VALUES (?,?,?,?,?)";
		
		
		statement = connection.prepareStatement(sqlInstruction);
		statement.setString(1, enreg.get("NAME"));
		statement.setString(2, enreg.get("SURNAME"));
		statement.setString(3, enreg.get("CLIENT_NAME"));
		
		java.util.Date date = dateFormat.parse(enreg.get("MONTH"));
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());  

		
		statement.setDate(4, sqlDate);

		NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
		Number number = format.parse(enreg.get("TOTAL"));
		getLogger().info("TOTAL 2: " + number.doubleValue());
		statement.setDouble(5, number.doubleValue());

		getLogger().info("persisting...");
		getLogger().info(statement.toString());
		statement.executeUpdate();
		getLogger().info("...done");
		
		connection.close();
	}
}
