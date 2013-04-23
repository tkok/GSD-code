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

public class IfStatement_IntValue_Not_Test {
	
	private static String ROOM1_TEMPERATURE = "ROOM1.TEMPERATURE";
	private static String ROOM1_HEATER 		= "ROOM1.HEATER";
	
	@Before
	public void setupDatabase() {
		SensorValueCache.setValue(ROOM1_TEMPERATURE, new FloatValue(11));
		SensorValueCache.setValue(ROOM1_HEATER, new FloatValue(0));
	}

	@Test
	public void simpleEqual_ExecuteBecauseLT() {
		Expression expression = new Expression(ROOM1_TEMPERATURE, Operator.NOT, new FloatValue(10));
		Statement thenStatement = new SetStatement(ROOM1_HEATER, new FloatValue(1));
		
		IfStatement ifStatement = new IfStatement();
		ifStatement.addExpression(expression);
		ifStatement.addThenStatement(thenStatement);
		
		Assert.assertEquals(0f, SensorValueCache.getValue(ROOM1_HEATER).getValue());
		ifStatement.execute();
		Assert.assertEquals(1f, SensorValueCache.getValue(ROOM1_HEATER).getValue());
	}
	
	@Test
	public void simpleEqual_ExecuteBecauseGT() {
		Expression expression = new Expression(ROOM1_TEMPERATURE, Operator.NOT, new FloatValue(12));
		Statement thenStatement = new SetStatement(ROOM1_HEATER, new FloatValue(1));
		
		IfStatement ifStatement = new IfStatement();
		ifStatement.addExpression(expression);
		ifStatement.addThenStatement(thenStatement);
		
		Assert.assertEquals(0f, SensorValueCache.getValue(ROOM1_HEATER).getValue());
		ifStatement.execute();
		Assert.assertEquals(1f, SensorValueCache.getValue(ROOM1_HEATER).getValue());
	}
	
	@Test
	public void simpleEquak_NoExecuteEqual() {
		Expression expression = new Expression(ROOM1_TEMPERATURE, Operator.NOT, new FloatValue(11));
		Statement thenStatement = new SetStatement(ROOM1_HEATER, new FloatValue(1));
		
		IfStatement ifStatement = new IfStatement();
		ifStatement.addExpression(expression);
		ifStatement.addThenStatement(thenStatement);
		
		Assert.assertEquals(0f, SensorValueCache.getValue(ROOM1_HEATER).getValue());
		ifStatement.execute();
		Assert.assertEquals(0f, SensorValueCache.getValue(ROOM1_HEATER).getValue());
	}
	
}