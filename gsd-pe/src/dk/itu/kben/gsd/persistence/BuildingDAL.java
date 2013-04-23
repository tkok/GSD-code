package dk.itu.kben.gsd.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;

import com.google.gson.Gson;

import dk.itu.kben.gsd.domain.FloatValue;
import dk.itu.kben.gsd.domain.GsonFactory;
import dk.itu.kben.gsd.domain.Policy;
import dk.itu.kben.gsd.domain.PolicyEntities;
import dk.itu.kben.gsd.domain.PolicyEntity;
import dk.itu.kben.gsd.domain.Value;
import dk.itu.nicl.gsd.log.Log;

public class BuildingDAL {

	// TODO: Have config-stuff like this in a config-file (i.e. web.config)?
	/*
	private static final String serverUrl = "jdbc:mysql://mysql2.gigahost.dk:3306/";
	private static final String dbName = "webaholic_gsd";
	private static final String driver = "com.mysql.jdbc.Driver";
	private static final String userName = "webaholic";
	private static final String password = "Gh2kZuCwlpU5ZfpHQN4i";
	*/
	private static final String serverUrl = "jdbc:mysql://localhost:8889/gsd";
	private static final String dbName = "";
	private static final String driver = "com.mysql.jdbc.Driver";
	private static final String userName = "root";
	private static final String password = "root";
	
	private static Connection connection = null;
	private static PreparedStatement preparedStatement = null;

	private BuildingDAL() {
	}

	private static Connection CreateConn() {
		try {
			Class.forName(driver).newInstance();
			connection = DriverManager.getConnection(serverUrl + dbName, userName, password);
			Log.log("Creating DB connection");
			System.out.println("Creating DB connection \n");
		} catch (SQLException e) {
			e.printStackTrace();

		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return connection;
	}

	private static void CloseConn() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();

		} catch (Exception e) {
			e.toString();
		}
	}

	protected static Hashtable<String, Value> GetHashtableWithStringValueFromDB(String tableFromName) {
		connection = CreateConn();
		Hashtable<String, Value> ht = new Hashtable<String, Value>();
		try {
			preparedStatement = connection.prepareStatement("SELECT * FROM " + tableFromName);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String uuid = resultSet.getString("uuid");
				float val = resultSet.getInt("val");
				Log.log(uuid + val);
				System.out.println(uuid + val + "\n");
				ht.put(uuid, new FloatValue(val));
			}
			Log.log("ht size: " + ht.size());
			System.out.println("ht size: " + ht.size() + "\n");
			return ht;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			CloseConn();
			Log.log("Close DB connection \n");
			System.out.println("Close DB connection \n");
		}
		return null;
	}

	public static void deleteAll() {
		connection = CreateConn();
		try {
			preparedStatement = connection.prepareStatement("DELETE FROM policy");
			
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			CloseConn();
			Log.log("Close DB connection");
			System.out.println("Close DB connection \n");
		}
	}
	
	private static PolicyEntity insertPolicyEntity(PolicyEntity policyEntity) {
		System.out.println("Inserting policy.");
		connection = CreateConn();
		
		try {
			preparedStatement = connection.prepareStatement("INSERT INTO policy (fromTime, toTime, active, policy) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			preparedStatement.setTime(1, policyEntity.getFromTime());
			preparedStatement.setTime(2, policyEntity.getToTime());
			preparedStatement.setBoolean(3, policyEntity.isActive());
			preparedStatement.setString(4, policyEntity.getPolicy().getJSON());
			
			preparedStatement.executeUpdate();
			ResultSet rs = preparedStatement.getGeneratedKeys();
			if (rs.next()) {
				long id = rs.getLong(1);
				policyEntity.setId(id);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			CloseConn();
		}
		
		return policyEntity;
	}
	
	private static void updatePolicyEntity(PolicyEntity policyEntity) {
		System.out.println("Updating policy.");
		connection = CreateConn();

		try {
			preparedStatement = connection.prepareStatement("UPDATE policy SET fromTime = ? AND toTime = ? AND active = ? AND policy = ? WHERE ID = ?");
			
			preparedStatement.setTime(1, policyEntity.getFromTime());
			preparedStatement.setTime(2, policyEntity.getToTime());
			preparedStatement.setBoolean(3, policyEntity.isActive());
			preparedStatement.setString(4, policyEntity.getPolicy().getJSON());
			preparedStatement.setLong(5, policyEntity.getId()); 

			preparedStatement.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			CloseConn();
		}
	}
	
	public static PolicyEntity persist(PolicyEntity policyEntity) {
		PolicyEntity result = null;
		
		if (policyEntity.getId() > -1) {
			// Update
			updatePolicyEntity(policyEntity);
		}
		else {
			// Insert
			result = insertPolicyEntity(policyEntity);
		}
		
		return result;
	}

	public static PolicyEntities getActivePolicies() {
		connection = CreateConn();

		// ArrayList<PolicyEntity> policies = new ArrayList<PolicyEntity>();
		PolicyEntities policyEntities = new PolicyEntities();

		try {
			Calendar calendar = new GregorianCalendar();
			int seconds = calendar.get(Calendar.SECOND);
			int minutes = calendar.get(Calendar.MINUTE);
			int hours = calendar.get(Calendar.HOUR_OF_DAY);
			
			Log.log("Hour: " + hours + ", Minutes: " + minutes + ", Seconds: " + seconds);
			System.out.println("Hour: " + hours + ", Minutes: " + minutes + ", Seconds: " + seconds);

			long milliseconds = ((hours * 60 * 60) + (minutes * 60) + seconds) * 1000;

			Time time = new Time(milliseconds);
			
			Log.log("Milliseconds: " + milliseconds);
			System.out.println("Milliseconds: " + milliseconds);

			preparedStatement = connection.prepareStatement("SELECT * FROM policy WHERE fromTime <= ? AND toTime >= ? AND active = TRUE");
			preparedStatement.setTime(1, time);
			preparedStatement.setTime(2, time);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				PolicyEntity policyEntity = new PolicyEntity();

				policyEntity.setId(rs.getLong("id"));
				policyEntity.setFromTime(rs.getTime("fromTime"));
				policyEntity.setToTime(rs.getTime("toTime"));
				policyEntity.setActive(true);
				String json = rs.getString("policy");

				Gson gson = GsonFactory.getInstance();

				Policy policy = null;
				policy = gson.fromJson(json, Policy.class);

				/*
				 * String s = gson.toJson(policy); System.out.println(s);
				 */

				policyEntity.setPolicy(policy);

				policyEntities.add(policyEntity);
				// policies.add(policyEntity);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			CloseConn();
			Log.log("Close DB connection");
			System.out.println("Close DB connection \n");
		}

		return policyEntities;
	}
}