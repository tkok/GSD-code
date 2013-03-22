package dk.itu.kben.gsd;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class IfStatement_IntValue_Equal_Multiple_Test {
	
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
	public void doubleEqual_Execute() {
		Expression expression1 = new Expression(ROOM1_TEMPERATURE, Operator.EQUALS, new IntValue(21));
		Expression expression2 = new Expression(ROOM2_TEMPERATURE, Operator.EQUALS, new IntValue(21));
		Statement thenStatement = new SetStatement(WING1_HEATER, new BooleanValue(false));
		
		IfStatement ifStatement = new IfStatement();
		ifStatement.addExpression(expression1);
		ifStatement.addExpression(expression2);
		ifStatement.addThenStatement(thenStatement);
		
		Assert.assertTrue(((BooleanValue) BuildingDAO.getValue(WING1_HEATER)).getValue());
		ifStatement.execute();
		Assert.assertFalse(((BooleanValue) BuildingDAO.getValue(WING1_HEATER)).getValue());
	}
	
	@Test
	public void simpleEqual_NoExecuteBecause1stExpressionIsLT() {
		Expression expression1 = new Expression(ROOM1_TEMPERATURE, Operator.EQUALS, new IntValue(20));
		Expression expression2 = new Expression(ROOM2_TEMPERATURE, Operator.EQUALS, new IntValue(21));
		Statement thenStatement = new SetStatement(WING1_HEATER, new BooleanValue(false));
		
		IfStatement ifStatement = new IfStatement();
		ifStatement.addExpression(expression1);
		ifStatement.addExpression(expression2);
		ifStatement.addThenStatement(thenStatement);
		
		Assert.assertTrue(((BooleanValue) BuildingDAO.getValue(WING1_HEATER)).getValue());
		ifStatement.execute();
		Assert.assertTrue(((BooleanValue) BuildingDAO.getValue(WING1_HEATER)).getValue());
	}

	@Test
	public void simpleEqual_NoExecuteBecause2ndExpressionIsLT() {
		Expression expression1 = new Expression(ROOM1_TEMPERATURE, Operator.EQUALS, new IntValue(21));
		Expression expression2 = new Expression(ROOM2_TEMPERATURE, Operator.EQUALS, new IntValue(20));
		Statement thenStatement = new SetStatement(WING1_HEATER, new BooleanValue(false));
		
		IfStatement ifStatement = new IfStatement();
		ifStatement.addExpression(expression1);
		ifStatement.addExpression(expression2);
		ifStatement.addThenStatement(thenStatement);
		
		Assert.assertTrue(((BooleanValue) BuildingDAO.getValue(WING1_HEATER)).getValue());
		ifStatement.execute();
		Assert.assertTrue(((BooleanValue) BuildingDAO.getValue(WING1_HEATER)).getValue());
	}

	//TODO All tests should be implemented.

}