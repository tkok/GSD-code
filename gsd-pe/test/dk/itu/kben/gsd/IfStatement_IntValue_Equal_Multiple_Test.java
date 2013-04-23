package dk.itu.kben.gsd;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import dk.itu.kben.gsd.domain.Expression;
import dk.itu.kben.gsd.domain.FloatValue;
import dk.itu.kben.gsd.domain.IfStatement;
import dk.itu.kben.gsd.domain.Operator;
import dk.itu.kben.gsd.domain.SetStatement;
import dk.itu.kben.gsd.domain.Statement;
import dk.itu.scas.gsd.utils.SensorValueCache;

public class IfStatement_IntValue_Equal_Multiple_Test {
	
	private static String ROOM1_TEMPERATURE = "ROOM1.TEMPERATURE";
	private static String ROOM2_TEMPERATURE = "ROOM2.TEMPERATURE";
	private static String WING1_HEATER 		= "WING1.HEATER";
	
	@Before
	public void setupDatabase() {
		SensorValueCache.setValue(ROOM1_TEMPERATURE, new FloatValue(21));
		SensorValueCache.setValue(ROOM2_TEMPERATURE, new FloatValue(21));
		SensorValueCache.setValue(WING1_HEATER, new FloatValue(1));
	}

	@Test
	public void doubleEqual_Execute() {
		Expression expression1 = new Expression(ROOM1_TEMPERATURE, Operator.EQUALS, new FloatValue(21));
		Expression expression2 = new Expression(ROOM2_TEMPERATURE, Operator.EQUALS, new FloatValue(21));
		Statement thenStatement = new SetStatement(WING1_HEATER, new FloatValue(0));
		
		IfStatement ifStatement = new IfStatement();
		ifStatement.addExpression(expression1);
		ifStatement.addExpression(expression2);
		ifStatement.addThenStatement(thenStatement);
		
		Assert.assertEquals(1f, SensorValueCache.getValue(WING1_HEATER).getValue());
		ifStatement.execute();
		Assert.assertEquals(0f, SensorValueCache.getValue(WING1_HEATER).getValue());
	}
	
	@Test
	public void simpleEqual_NoExecuteBecause1stExpressionIsLT() {
		Expression expression1 = new Expression(ROOM1_TEMPERATURE, Operator.EQUALS, new FloatValue(20));
		Expression expression2 = new Expression(ROOM2_TEMPERATURE, Operator.EQUALS, new FloatValue(21));
		Statement thenStatement = new SetStatement(WING1_HEATER, new FloatValue(0));
		
		IfStatement ifStatement = new IfStatement();
		ifStatement.addExpression(expression1);
		ifStatement.addExpression(expression2);
		ifStatement.addThenStatement(thenStatement);
		
		Assert.assertEquals(1f, SensorValueCache.getValue(WING1_HEATER).getValue());
		ifStatement.execute();
		Assert.assertEquals(1f, SensorValueCache.getValue(WING1_HEATER).getValue());
	}

	@Test
	public void simpleEqual_NoExecuteBecause2ndExpressionIsLT() {
		Expression expression1 = new Expression(ROOM1_TEMPERATURE, Operator.EQUALS, new FloatValue(21));
		Expression expression2 = new Expression(ROOM2_TEMPERATURE, Operator.EQUALS, new FloatValue(20));
		Statement thenStatement = new SetStatement(WING1_HEATER, new FloatValue(0));
		
		IfStatement ifStatement = new IfStatement();
		ifStatement.addExpression(expression1);
		ifStatement.addExpression(expression2);
		ifStatement.addThenStatement(thenStatement);
		
		Assert.assertEquals(1f, SensorValueCache.getValue(WING1_HEATER).getValue());

		ifStatement.execute();
		Assert.assertEquals(1f, SensorValueCache.getValue(WING1_HEATER).getValue());
	}
}