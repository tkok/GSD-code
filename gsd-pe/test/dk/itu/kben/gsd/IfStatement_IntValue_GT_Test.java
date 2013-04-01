package dk.itu.kben.gsd;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import dk.itu.kben.gsd.domain.BooleanValue;
import dk.itu.kben.gsd.domain.Expression;
import dk.itu.kben.gsd.domain.IfStatement;
import dk.itu.kben.gsd.domain.IntValue;
import dk.itu.kben.gsd.domain.Operator;
import dk.itu.kben.gsd.domain.SetStatement;
import dk.itu.kben.gsd.domain.Statement;
import dk.itu.kben.gsd.persistence.BuildingDAO;

public class IfStatement_IntValue_GT_Test {
	
	private static String ROOM1_TEMPERATURE = "ROOM1.TEMPERATURE";
	private static String ROOM1_HEATER 		= "ROOM1.HEATER";
	
	@Before
	public void setupDatabase() {
		BuildingDAO.setValue(ROOM1_TEMPERATURE, new IntValue(30));
		BuildingDAO.setValue(ROOM1_HEATER, new BooleanValue(true));
	}

	@Test
	public void simpleGT_Execute() {
		Expression expression = new Expression(ROOM1_TEMPERATURE, Operator.GREATER_THAN, new IntValue(25));
		Statement thenStatement = new SetStatement(ROOM1_HEATER, new BooleanValue(false));
		
		IfStatement ifStatement = new IfStatement();
		ifStatement.addExpression(expression);
		ifStatement.addThenStatement(thenStatement);
		
		Assert.assertTrue(((BooleanValue) BuildingDAO.getValue(ROOM1_HEATER)).getValue());
		ifStatement.execute();
		Assert.assertFalse(((BooleanValue) BuildingDAO.getValue(ROOM1_HEATER)).getValue());
	}
	
	@Test
	public void simpleGT_NoExecuteBecauseEqual() {
		Expression expression = new Expression(ROOM1_TEMPERATURE, Operator.GREATER_THAN, new IntValue(30));
		Statement thenStatement = new SetStatement(ROOM1_HEATER, new BooleanValue(false));
		
		IfStatement ifStatement = new IfStatement();
		ifStatement.addExpression(expression);
		ifStatement.addThenStatement(thenStatement);
		
		Assert.assertTrue(((BooleanValue) BuildingDAO.getValue(ROOM1_HEATER)).getValue());
		ifStatement.execute();
		Assert.assertTrue(((BooleanValue) BuildingDAO.getValue(ROOM1_HEATER)).getValue());
	}	

	@Test
	public void simpleGT_NoExecuteBecauseGT() {
		Expression expression = new Expression(ROOM1_TEMPERATURE, Operator.GREATER_THAN, new IntValue(31));
		Statement thenStatement = new SetStatement(ROOM1_HEATER, new BooleanValue(false));
		
		IfStatement ifStatement = new IfStatement();
		ifStatement.addExpression(expression);
		ifStatement.addThenStatement(thenStatement);
		
		Assert.assertTrue(((BooleanValue) BuildingDAO.getValue(ROOM1_HEATER)).getValue());
		ifStatement.execute();
		Assert.assertTrue(((BooleanValue) BuildingDAO.getValue(ROOM1_HEATER)).getValue());
	}
}