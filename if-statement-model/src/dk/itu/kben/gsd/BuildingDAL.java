package dk.itu.kben.gsd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;

import com.google.gson.Gson;

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
	
	public static void store(PolicyEntity policyEntity) {
		connection = CreateConn();
		
		try {
			if (policyEntity.getId() != -1) {
				//TODO Update statement
			}
			else {
				preparedStatement = connection.prepareStatement("INSERT INTO policy (fromTime, toTime, active, policy) VALUES (?, ?, ?, ?)");
				
				preparedStatement.setTime(1, policyEntity.fromTime);
				preparedStatement.setTime(2, policyEntity.toTime);
				preparedStatement.setBoolean(3, policyEntity.active);
				preparedStatement.setString(4,  policyEntity.getPolicy().getJSON());
				
				preparedStatement.execute();
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			CloseConn();
			System.out.println("Close DB connection \n");
		}
	}
	
	public static ArrayList<PolicyEntity> getActivePolicies() {
		connection = CreateConn();
		
		ArrayList<PolicyEntity> policies = new ArrayList<PolicyEntity>();
		
		try {
			Calendar calendar = new GregorianCalendar();
			//int milli = calendar.get(Calendar.MILLISECOND);
			int seconds = calendar.get(Calendar.SECOND);
			int minutes = calendar.get(Calendar.MINUTE);
			int hours = calendar.get(Calendar.HOUR_OF_DAY);
			
			System.out.println("Hour: " + hours + ", Minutes: " + minutes + ", Seconds: " + seconds);
			
			long milliseconds = ((hours * 60 * 60) + (minutes * 60) + seconds) * 1000;
			
			Time time = new Time(milliseconds);
			
			System.out.println("Milliseconds: " + milliseconds); 
			
			preparedStatement = connection.prepareStatement("SELECT * FROM policy WHERE fromTime <= ? AND toTime >= ? AND active = TRUE");
			preparedStatement.setTime(1, time);
			preparedStatement.setTime(2, time);
			
			ResultSet rs = preparedStatement.executeQuery();
			
			while (rs.next()) {
				PolicyEntity policyEntity = new PolicyEntity();
				
				policyEntity.setFromTime(rs.getTime("fromTime"));
				policyEntity.setToTime(rs.getTime("toTime"));
				policyEntity.setActive(true);
				String json = rs.getString("policy");
				
				Gson gson = GsonFactory.getInstance();
				
				Policy policy = null;
				policy = gson.fromJson(json, Policy.class);
				
				String s = gson.toJson(policy);
				System.out.println(s);
				
				policyEntity.setPolicy(policy);
				
				policies.add(policyEntity);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			CloseConn();
			System.out.println("Close DB connection \n");
		}
		
		return policies;
	}
}