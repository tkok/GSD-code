package dk.itu.kben.gsd.sanitychecks;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import dk.itu.policyengine.domain.Expression;
import dk.itu.policyengine.domain.FloatValue;
import dk.itu.policyengine.domain.IfStatement;
import dk.itu.policyengine.domain.Operator;
import dk.itu.policyengine.domain.SetStatement;
import dk.itu.policyengine.domain.Statement;
import dk.itu.policyengine.persistence.SensorValueCache;

public class IfStamenet_toJSONByGSON_Test {
	
	private static String ROOM1_TEMPERATURE = "ROOM1.TEMPERATURE";
	private static String ROOM2_TEMPERATURE = "ROOM2.TEMPERATURE";
	private static String WING1_HEATER 		= "WING1.HEATER";
	
	@Before
	public void setupDatabase() {
		SensorValueCache.setValue(ROOM1_TEMPERATURE, new FloatValue(21));
		SensorValueCache.setValue(ROOM2_TEMPERATURE, new FloatValue(21));
	}

	@Test
	public void prettyPrintToJSON() {
		Expression expression1 = new Expression(ROOM1_TEMPERATURE, Operator.EQUALS, new FloatValue(21));
		Expression expression2 = new Expression(ROOM2_TEMPERATURE, Operator.EQUALS, new FloatValue(21));
		Statement thenStatement = new SetStatement(WING1_HEATER, new FloatValue(1));
		
		IfStatement ifStatement = new IfStatement();
		ifStatement.addExpression(expression1);
		ifStatement.addExpression(expression2);
		ifStatement.addThenStatement(thenStatement);
		
		Gson gson = new Gson();
		
		String json = gson.toJson(ifStatement);
		Assert.assertEquals(json, "{\"conditionalExpressions\":[{\"prefixOperator\":\"AND\",\"aValue\":{\"floatValue\":21.0},\"operator\":\"EQUALS\",\"sensorId\":\"ROOM1.TEMPERATURE\"},{\"prefixOperator\":\"AND\",\"aValue\":{\"floatValue\":21.0},\"operator\":\"EQUALS\",\"sensorId\":\"ROOM2.TEMPERATURE\"}],\"thenStatements\":[{\"aValue\":{\"floatValue\":1.0},\"sensorID\":\"WING1.HEATER\"}],\"elseStatements\":[]}");
	}
}