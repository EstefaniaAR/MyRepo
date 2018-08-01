package com.ef;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MyConnection 
{
	public Connection getConnection() throws Exception
	{
		Connection con =null;

		Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		con=DriverManager.getConnection("jdbc:mysql://localhost:3306/log_schema?serverTimezone=UTC&useSSL=false"
					,"root","system");  
		return con;
	}

}
