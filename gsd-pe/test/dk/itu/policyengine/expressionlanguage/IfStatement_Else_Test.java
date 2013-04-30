package dk.itu.policyengine.expressionlanguage;

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

public class IfStatement_Else_Test {
	
	private static String ROOM1_TEMPERATURE = "ROOM1.TEMPERATURE";
	private static String ROOM1_HEATER 		= "ROOM1.HEATER";
	private static String ROOM1_BLINDS 		= "ROOM1.BLINDS";
	
	@Before
	public void setupDatabase() {
		SensorValueCache.setValue(ROOM1_TEMPERATURE, new FloatValue(11));
		SensorValueCache.setValue(ROOM1_HEATER, new FloatValue(0));
		SensorValueCache.setValue(ROOM1_BLINDS, new FloatValue(0));
	}

	@Test
	public void simpleEqual_Execute() {
		Expression expression = new Expression(ROOM1_TEMPERATURE, Operator.EQUALS, new FloatValue(10));
		Statement thenStatement = new SetStatement(ROOM1_HEATER, new FloatValue(1));
		Statement elseStatement = new SetStatement(ROOM1_BLINDS, new FloatValue(1));
		
		IfStatement ifStatement = new IfStatement();
		ifStatement.addExpression(expression);
		ifStatement.addThenStatement(thenStatement);
		ifStatement.addElseStatement(elseStatement);
		
		Assert.assertEquals(0f, SensorValueCache.getValue(ROOM1_HEATER).getValue());
		Assert.assertEquals(0f, SensorValueCache.getValue(ROOM1_BLINDS).getValue());
		
		ifStatement.execute();
		Assert.assertEquals(0f, SensorValueCache.getValue(ROOM1_HEATER).getValue());
		Assert.assertEquals(1f, SensorValueCache.getValue(ROOM1_BLINDS).getValue());
	}
}