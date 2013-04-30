package dk.itu.kben.gsd.sanitychecks;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import dk.itu.policyengine.domain.Expression;
import dk.itu.policyengine.domain.FloatValue;
import dk.itu.policyengine.domain.IfStatement;
import dk.itu.policyengine.domain.Operator;
import dk.itu.policyengine.domain.SetStatement;
import dk.itu.policyengine.domain.Statement;
import dk.itu.policyengine.persistence.SensorValueCache;

public class IfStatement_IntValue_GT_Test {
	
	private static String ROOM1_TEMPERATURE = "ROOM1.TEMPERATURE";
	private static String ROOM1_HEATER 		= "ROOM1.HEATER";
	
	@Before
	public void setupDatabase() {
		SensorValueCache.setValue(ROOM1_TEMPERATURE, new FloatValue(30));
		SensorValueCache.setValue(ROOM1_HEATER, new FloatValue(1));
	}

	@Test
	public void simpleGT_Execute() {
		Expression expression = new Expression(ROOM1_TEMPERATURE, Operator.GREATER_THAN, new FloatValue(25));
		Statement thenStatement = new SetStatement(ROOM1_HEATER, new FloatValue(0));
		
		IfStatement ifStatement = new IfStatement();
		ifStatement.addExpression(expression);
		ifStatement.addThenStatement(thenStatement);
		
		Assert.assertEquals(1f, SensorValueCache.getValue(ROOM1_HEATER).getValue());
		ifStatement.execute();
		Assert.assertEquals(0f, SensorValueCache.getValue(ROOM1_HEATER).getValue());
	}
	
	@Test
	public void simpleGT_NoExecuteBecauseEqual() {
		Expression expression = new Expression(ROOM1_TEMPERATURE, Operator.GREATER_THAN, new FloatValue(30));
		Statement thenStatement = new SetStatement(ROOM1_HEATER, new FloatValue(0));
		
		IfStatement ifStatement = new IfStatement();
		ifStatement.addExpression(expression);
		ifStatement.addThenStatement(thenStatement);
		
		Assert.assertEquals(1f, SensorValueCache.getValue(ROOM1_HEATER).getValue());
		ifStatement.execute();
		Assert.assertEquals(1f, SensorValueCache.getValue(ROOM1_HEATER).getValue());
	}	

	@Test
	public void simpleGT_NoExecuteBecauseGT() {
		Expression expression = new Expression(ROOM1_TEMPERATURE, Operator.GREATER_THAN, new FloatValue(31));
		Statement thenStatement = new SetStatement(ROOM1_HEATER, new FloatValue(0));
		
		IfStatement ifStatement = new IfStatement();
		ifStatement.addExpression(expression);
		ifStatement.addThenStatement(thenStatement);
		
		Assert.assertEquals(1f, SensorValueCache.getValue(ROOM1_HEATER).getValue());
		ifStatement.execute();
		Assert.assertEquals(1f, SensorValueCache.getValue(ROOM1_HEATER).getValue());
	}
}