package dk.itu.kben.gsd;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

public class IfStamenet_toJSONByGSON_Test {
	
	private static String ROOM1_TEMPERATURE = "ROOM1.TEMPERATURE";
	private static String ROOM2_TEMPERATURE = "ROOM2.TEMPERATURE";
	private static String WING1_HEATER 		= "WING1.HEATER";
	
	@Before
	public void setupDatabase() {
		BuildingDAO.setValue(ROOM1_TEMPERATURE, new IntValue(21));
		BuildingDAO.setValue(ROOM2_TEMPERATURE, new IntValue(21));
		BuildingDAO.setValue(WING1_HEATER, new BooleanValue(true));
	}

	@Test
	public void prettyPrintToJSON() {
		Expression expression1 = new Expression(ROOM1_TEMPERATURE, Operator.EQUALS, new IntValue(21));
		Expression expression2 = new Expression(ROOM2_TEMPERATURE, Operator.EQUALS, new IntValue(21));
		Statement thenStatement = new SetStatement(WING1_HEATER, new BooleanValue(false));
		
		IfStatement ifStatement = new IfStatement();
		ifStatement.addExpression(expression1);
		ifStatement.addExpression(expression2);
		ifStatement.addThenStatement(thenStatement);
		
		Gson gson = new Gson();
		
		String json = gson.toJson(ifStatement);
		Assert.assertEquals("{\"conditionalExpressions\":[{\"prefixOperator\":\"AND\",\"aValue\":{\"theValue\":21},\"operator\":\"EQUALS\",\"sensorId\":\"ROOM1.TEMPERATURE\"},{\"prefixOperator\":\"AND\",\"aValue\":{\"theValue\":21},\"operator\":\"EQUALS\",\"sensorId\":\"ROOM2.TEMPERATURE\"}],\"thenStatements\":[{\"aValue\":{\"theValue\":false},\"sensorID\":\"WING1.HEATER\"}]}", json);
	}
}