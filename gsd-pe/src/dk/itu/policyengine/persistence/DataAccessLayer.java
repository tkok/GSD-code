package dk.itu.policyengine.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.Calendar;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import dk.itu.nicl.gsd.log.Log;
import dk.itu.policyengine.domain.FloatValue;
import dk.itu.policyengine.domain.GsonFactory;
import dk.itu.policyengine.domain.Policy;
import dk.itu.policyengine.domain.PolicyEntities;
import dk.itu.policyengine.domain.PolicyEntity;
import dk.itu.policyengine.domain.Value;

public class DataAccessLayer {
	private final static Logger logger = Logger.getLogger(DataAccessLayer.class);
	
	
	private static final String serverUrl = "jdbc:mysql://mysql2.gigahost.dk:3306/";
	private static final String dbName = "webaholic_gsd";
	private static final String driver = "com.mysql.jdbc.Driver";
	private static final String userName = "webaholic";
	private static final String password = "Gh2kZuCwlpU5ZfpHQN4i";
	
	/*
	private static final String serverUrl = "jdbc:mysql://localhost:8889/gsd";
	private static final String dbName = "";
	private static final String driver = "com.mysql.jdbc.Driver";
	private static final String userName = "root";
	private static final String password = "root";
	*/
	
	private static Connection connection = null;
	private static PreparedStatement preparedStatement = null;
	
	private DataAccessLayer() {
	}

	private static Connection CreateConn() {
		try {
			Class.forName(driver).newInstance();
			connection = DriverManager.getConnection(serverUrl + dbName, userName, password);
			logger.debug("Creating DB connection to " + serverUrl);
		} catch (Exception e) {
			logger.error("Error creating database connection.", e);
		}
		
		return connection;
	}
	
	private static void CloseConn() {
		try {
			logger.debug("Close DB Connection");
			
			connection.close();
		} catch (Exception e) {
			logger.error("Error closing DB connection.", e);
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
				
				logger.debug(uuid+val);
				ht.put(uuid, new FloatValue(val));
			}
			logger.debug("ht size" + ht.size());
			return ht;
		} catch (Exception e) {
			logger.error(e);
		} finally {
			CloseConn();
		}
		return null;
	}

	public static void deleteAll() {
		connection = CreateConn();
		try {
			preparedStatement = connection.prepareStatement("DELETE FROM policy");
			
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			logger.error(e);
		} finally {
			CloseConn();
		}
	}
	
	private static PolicyEntity insertPolicyEntity(PolicyEntity policyEntity) {
		connection = CreateConn();
		
		try {
			preparedStatement = connection.prepareStatement("INSERT INTO policy (fromTime, toTime, active, policy, name, description) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			preparedStatement.setTime(1, policyEntity.getFromTime());
			preparedStatement.setTime(2, policyEntity.getToTime());
			preparedStatement.setBoolean(3, policyEntity.isActive());
			preparedStatement.setString(4, policyEntity.getPolicy().getJSON());
			preparedStatement.setString(5, policyEntity.getName());
			preparedStatement.setString(6, policyEntity.getDescription());
			
			preparedStatement.executeUpdate();
			logger.debug("Policy " + policyEntity.getName() + " was inserted into the database.");
			
			ResultSet rs = preparedStatement.getGeneratedKeys();
			if (rs.next()) {
				long id = rs.getLong(1);
				policyEntity.setId(id);
			}
			
		} catch (Exception e) {
			logger.error(e);
		} finally {
			CloseConn();
		}
		
		return policyEntity;
	}
	
	private static void updatePolicyEntity(PolicyEntity policyEntity) {
		connection = CreateConn();

		try {
			preparedStatement = connection.prepareStatement("UPDATE policy SET fromTime = ?, toTime = ?, active = ?, policy = ?, name = ?, description = ? WHERE id = ?");
			
			preparedStatement.setTime(1, policyEntity.getFromTime());
			preparedStatement.setTime(2, policyEntity.getToTime());
			preparedStatement.setBoolean(3, policyEntity.isActive());
			preparedStatement.setString(4, policyEntity.getPolicy().getJSON());
			preparedStatement.setString(5, policyEntity.getName());
			preparedStatement.setString(6, policyEntity.getDescription());
			preparedStatement.setLong(7, policyEntity.getId()); 

			preparedStatement.executeUpdate();
			logger.debug("Policy " + policyEntity.getName() + " was updated in the database.");
		} catch (Exception e) {
			logger.error(e);
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

		PolicyEntities policyEntities = new PolicyEntities();

		try {
			Time t = new Time(Calendar.getInstance().getTimeInMillis());
			preparedStatement = connection.prepareStatement("SELECT * FROM policy WHERE fromTime <= ? AND toTime >= ? AND active = TRUE");
			preparedStatement.setTime(1, t);
			preparedStatement.setTime(2, t);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				PolicyEntity policyEntity = new PolicyEntity();

				policyEntity.setId(rs.getLong("id"));
				policyEntity.setFromTime(rs.getTime("fromTime"));
				policyEntity.setToTime(rs.getTime("toTime"));
				policyEntity.setActive(true);
				policyEntity.setName(rs.getString("name"));
				policyEntity.setDescription(rs.getString("description"));
				String json = rs.getString("policy");

				Gson gson = GsonFactory.getInstance();

				Policy policy = null;
				policy = gson.fromJson(json, Policy.class);

				policyEntity.setPolicy(policy);

				policyEntities.add(policyEntity);
			}
		} catch (Exception e) {
			logger.error(e);
		} finally {
			CloseConn();
		}

		return policyEntities;
	}
	
	public static PolicyEntities getAllPolicies() {
		connection = CreateConn();

		PolicyEntities policyEntities = new PolicyEntities();

		try {
			preparedStatement = connection.prepareStatement("SELECT * FROM policy");

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				PolicyEntity policyEntity = new PolicyEntity();

				policyEntity.setId(rs.getLong("id"));
				policyEntity.setFromTime(rs.getTime("fromTime"));
				policyEntity.setToTime(rs.getTime("toTime"));
				policyEntity.setActive(rs.getBoolean("active"));
				policyEntity.setName(rs.getString("name"));
				policyEntity.setDescription(rs.getString("description"));
				String json = rs.getString("policy");

				Gson gson = GsonFactory.getInstance();

				Policy policy = null;
				policy = gson.fromJson(json, Policy.class);

				policyEntity.setPolicy(policy);

				policyEntities.add(policyEntity);
			}
		} catch (Exception e) {
			logger.error(e);
		} finally {
			CloseConn();
		}

		return policyEntities;
	}
}