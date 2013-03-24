package dk.itu.kben.gsd;

import java.sql.*;
import java.util.Hashtable;

public class BuildingDAL {
	
	//TODO: Have config-stuff like this in a config-file (i.e. web.config)?
	private static final String serverUrl = "jdbc:mysql://mysql2.gigahost.dk:3306/";
	private static final String dbName = "webaholic_gsd";
	private static final String driver = "com.mysql.jdbc.Driver";
	private static final String userName = "webaholic"; 
	private static final String password = "Gh2kZuCwlpU5ZfpHQN4i";
	private static Connection connection = null;
	private static PreparedStatement preparedStatement = null;
	
	//Private constructor to mimic a static class
	private BuildingDAL()
	{
		//Empty constructor for your pleasure
	}
	
	private static Connection CreateConn()
	{
			// Database connection
	  	  	try 
	  	  	{
	  	  		Class.forName(driver).newInstance();
	  	  		connection = DriverManager.getConnection(serverUrl+dbName,userName,password);
	  	  		System.out.println("Creating DB connection \n");
			}
	  	  	catch (SQLException e) 
	  	  	{
	  	  		e.printStackTrace();
	  	  		
			}
	  	  	catch (IllegalAccessException e)
	  	  	{
	  	  		e.printStackTrace();
			}
	  	  	catch (InstantiationException e) 
	  	  	{
	  	  		e.printStackTrace();
			}
	  	  	catch (ClassNotFoundException e) 
	  	  	{
	  	  		e.printStackTrace();
			}
			return connection;
	}
	
	private static void CloseConn()
	{
		try
		{
			connection.close();
		}
  	  	catch (SQLException e) 
  	  	{
  	  		e.printStackTrace();
  	  		
		}
  	  	catch (Exception e) 
  	  	{
  	  		e.toString();
		}
	}
	
	protected static Hashtable<String, Value> GetHashtableWithStringValueFromDB(String tableFromName)
	{
		connection = CreateConn();
		Hashtable<String, Value> ht = new Hashtable<String, Value>();
		try 
		{
			preparedStatement = connection.prepareStatement("SELECT * FROM " + tableFromName);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) 
			{
				String uuid = resultSet.getString("uuid");
				int val = resultSet.getInt("val");
				System.out.println(uuid + val + "\n");
				ht.put(uuid, new IntValue(val));
			}
			System.out.println("ht size: " + ht.size() + "\n");
			return ht;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			//Close connection
			CloseConn();
			System.out.println("Close DB connection \n");
		}
		return null;
	}
}
