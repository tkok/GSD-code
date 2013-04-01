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

public class IfStatement_Else_Test {
	
	private static String ROOM1_TEMPERATURE = "ROOM1.TEMPERATURE";
	private static String ROOM1_HEATER 		= "ROOM1.HEATER";
	private static String ROOM1_BLINDS 		= "ROOM1.BLINDS";
	
	@Before
	public void setupDatabase() {
		BuildingDAO.setValue(ROOM1_TEMPERATURE, new IntValue(11));
		BuildingDAO.setValue(ROOM1_HEATER, new BooleanValue(false));
		BuildingDAO.setValue(ROOM1_BLINDS, new BooleanValue(false));
	}

	@Test
	public void simpleEqual_Execute() {
		Expression expression = new Expression(ROOM1_TEMPERATURE, Operator.EQUALS, new IntValue(10));
		Statement thenStatement = new SetStatement(ROOM1_HEATER, new BooleanValue(true));
		Statement elseStatement = new SetStatement(ROOM1_BLINDS, new BooleanValue(true));
		
		IfStatement ifStatement = new IfStatement();
		ifStatement.addExpression(expression);
		ifStatement.addThenStatement(thenStatement);
		ifStatement.addElseStatement(elseStatement);
		
		Assert.assertFalse(((BooleanValue) BuildingDAO.getValue(ROOM1_HEATER)).getValue());
		Assert.assertFalse(((BooleanValue) BuildingDAO.getValue(ROOM1_BLINDS)).getValue());
		ifStatement.execute();
		Assert.assertFalse(((BooleanValue) BuildingDAO.getValue(ROOM1_HEATER)).getValue());
		Assert.assertTrue(((BooleanValue) BuildingDAO.getValue(ROOM1_BLINDS)).getValue());
	}
}