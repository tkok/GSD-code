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

public class Policy_With_Nested_If {
	
	private static String ROOM1_TEMPERATURE = "ROOM1.TEMPERATURE";
	private static String ROOM1_HEATER 		= "ROOM1.HEATER";
	private static String ROOM1_BLINDS 		= "ROOM1.BLINDS";
	
	private static long _08_00 = 7 * 60 * 60 * 1000;
	private static long _16_00 = 15 * 60 * 60 * 1000;
	private static long _21_00 = 20 * 60 * 60 * 1000;
	
	@Before
	public void setupDatabase() {
		SensorValueCache.setValue(ROOM1_TEMPERATURE, new FloatValue(11));
		SensorValueCache.setValue(ROOM1_HEATER, new FloatValue(0));
		SensorValueCache.setValue(ROOM1_BLINDS, new FloatValue(0));
	}

	@Test
	public void persistNewIfStatement() {
		IfStatement outerIfStatement = new IfStatement();
		Expression outerIfExpression = new Expression(ROOM1_TEMPERATURE, Operator.GREATER_THAN, new FloatValue(25));
		outerIfStatement.addExpression(outerIfExpression);
		
		IfStatement innerIfStatement = new IfStatement();
		Expression innerIfExpression = new Expression(ROOM1_HEATER, Operator.EQUALS, new FloatValue(1));
		innerIfStatement.addExpression(innerIfExpression);
		
		Statement innerThenStatement = new SetStatement(ROOM1_BLINDS, new FloatValue(1));
		innerIfStatement.addThenStatement(innerThenStatement);
		
		outerIfStatement.addThenStatement(innerIfStatement);
		
		Assert.assertEquals(0f, SensorValueCache.getValue(ROOM1_BLINDS).getValue());
		
		SensorValueCache.setValue(ROOM1_TEMPERATURE, new FloatValue(25));
		outerIfStatement.execute();
		Assert.assertEquals(0f, SensorValueCache.getValue(ROOM1_BLINDS).getValue());

		// Value set above the defined max at 25 so inner if should be activated
		// but result should remain 0 since second if's condition is not met (heater == 1)
		SensorValueCache.setValue(ROOM1_TEMPERATURE, new FloatValue(26));
		outerIfStatement.execute();
		Assert.assertEquals(0f, SensorValueCache.getValue(ROOM1_BLINDS).getValue());
		
		// Now the second condition will be met (heater == 1) so blinds will be set to 1
		SensorValueCache.setValue(ROOM1_HEATER, new FloatValue(1));
		outerIfStatement.execute();
		Assert.assertEquals(1f, SensorValueCache.getValue(ROOM1_BLINDS).getValue());
	}
}