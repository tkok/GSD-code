package dk.itu.kben.gsd;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class IfStatement_IntValue_LT_Test {
	
	private static String ROOM1_TEMPERATURE = "ROOM1.TEMPERATURE";
	private static String ROOM1_HEATER 		= "ROOM1.HEATER";
	
	@Before
	public void setupDatabase() {
		BuildingDAO.setValue(ROOM1_TEMPERATURE, new IntValue(11));
		BuildingDAO.setValue(ROOM1_HEATER, new BooleanValue(false));
	}

	@Test
	public void simpleLT_Execute() {
		Expression expression = new Expression(ROOM1_TEMPERATURE, Operator.LESS_THAN, new IntValue(12));
		Statement thenStatement = new SetStatement(ROOM1_HEATER, new BooleanValue(true));
		
		IfStatement ifStatement = new IfStatement();
		ifStatement.addExpression(expression);
		ifStatement.addThenStatement(thenStatement);
		
		Assert.assertFalse(((BooleanValue) BuildingDAO.getValue(ROOM1_HEATER)).getValue());
		ifStatement.execute();
		Assert.assertTrue(((BooleanValue) BuildingDAO.getValue(ROOM1_HEATER)).getValue());
	}
	
	@Test
	public void simpleLT_NoExecuteBecauseEqual() {
		Expression expression = new Expression(ROOM1_TEMPERATURE, Operator.LESS_THAN, new IntValue(11));
		Statement thenStatement = new SetStatement(ROOM1_HEATER, new BooleanValue(true));
		
		IfStatement ifStatement = new IfStatement();
		ifStatement.addExpression(expression);
		ifStatement.addThenStatement(thenStatement);
		
		Assert.assertFalse(((BooleanValue) BuildingDAO.getValue(ROOM1_HEATER)).getValue());
		ifStatement.execute();
		Assert.assertFalse(((BooleanValue) BuildingDAO.getValue(ROOM1_HEATER)).getValue());
	}	

	@Test
	public void simpleLT_NoExecuteBecauseLT() {
		Expression expression = new Expression(ROOM1_TEMPERATURE, Operator.LESS_THAN, new IntValue(10));
		Statement thenStatement = new SetStatement(ROOM1_HEATER, new BooleanValue(true));
		
		IfStatement ifStatement = new IfStatement();
		ifStatement.addExpression(expression);
		ifStatement.addThenStatement(thenStatement);
		
		Assert.assertFalse(((BooleanValue) BuildingDAO.getValue(ROOM1_HEATER)).getValue());
		ifStatement.execute();
		Assert.assertFalse(((BooleanValue) BuildingDAO.getValue(ROOM1_HEATER)).getValue());
	}
}