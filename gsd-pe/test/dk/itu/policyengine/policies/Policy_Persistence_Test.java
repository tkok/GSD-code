package dk.itu.policyengine.policies;

import java.sql.Time;
import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import dk.itu.policyengine.domain.Expression;
import dk.itu.policyengine.domain.FloatValue;
import dk.itu.policyengine.domain.IfStatement;
import dk.itu.policyengine.domain.Operator;
import dk.itu.policyengine.domain.Policy;
import dk.itu.policyengine.domain.PolicyEntities;
import dk.itu.policyengine.domain.PolicyEntity;
import dk.itu.policyengine.domain.SetStatement;
import dk.itu.policyengine.domain.Statement;
import dk.itu.policyengine.persistence.DataAccessLayer;
import dk.itu.policyengine.persistence.SensorValueCache;

public class Policy_Persistence_Test {
	
	private static String ROOM1_TEMPERATURE = "ROOM1.TEMPERATURE";
	private static String ROOM1_HEATER 		= "ROOM1.HEATER";
	private static String ROOM1_BLINDS 		= "ROOM1.BLINDS";
	
	// From 00:00:00
	Time fromTime = new Time(0, 0, 0);
	
	// To 23:59:59
	Time toTime = new Time(23, 59, 59);

	
	@Before
	public void setupDatabase() {
		SensorValueCache.setValue(ROOM1_TEMPERATURE, new FloatValue(11));
		SensorValueCache.setValue(ROOM1_HEATER, new FloatValue(0));
		SensorValueCache.setValue(ROOM1_BLINDS, new FloatValue(0));
	}

	@Test
	public void persistNewIfStatement() {
		DataAccessLayer.deleteAll();
		
		Expression expression = new Expression(ROOM1_TEMPERATURE, Operator.EQUALS, new FloatValue(10));
		Statement thenStatement = new SetStatement(ROOM1_HEATER, new FloatValue(1));
		Statement elseStatement = new SetStatement(ROOM1_BLINDS, new FloatValue(1));
		
		IfStatement ifStatement = new IfStatement();
		ifStatement.addExpression(expression);
		ifStatement.addThenStatement(thenStatement);
		ifStatement.addElseStatement(elseStatement);
		
		Policy policy = new Policy();
		policy.addStatement(ifStatement);
		
		PolicyEntity policyEntity = new PolicyEntity();
		
		policyEntity.setName("Test policy");
		policyEntity.setDescription("This is a test policy doing nothing, really.");
		policyEntity.setPolicy(policy);
		policyEntity.setFromTime(fromTime);
		policyEntity.setToTime(toTime);
		policyEntity.setActive(true);
		
		policyEntity = DataAccessLayer.persist(policyEntity);
	}
	
	@Test
	public void getsThePolicyAndDeletesTheElseStatementAsAProofOfSQLUpdate() {
		PolicyEntities policyEntities = DataAccessLayer.getActivePolicies(); 
		
		Assert.assertEquals(1, policyEntities.getSize());
		
		for (Statement statement: policyEntities.getPolicyEntities().get(0).getPolicy().getStatements()) {
			if (statement instanceof IfStatement) {
				IfStatement theIfStatement = (IfStatement) statement;
				
				Assert.assertEquals(1, theIfStatement.getElseStatements().size());
				
				// Delete the ElseStatement
				theIfStatement.setElseStatements(new ArrayList<Statement>());
			}
		}
		
		PolicyEntity policyEntity = DataAccessLayer.persist(policyEntities.getPolicyEntities().get(0));
		
		policyEntities = DataAccessLayer.getActivePolicies();
		Assert.assertEquals(1, policyEntities.getSize());

		for (Statement statement: policyEntities.getPolicyEntities().get(0).getPolicy().getStatements()) {
			if (statement instanceof IfStatement) {
				IfStatement theIfStatement = (IfStatement) statement;
				
				Assert.assertEquals(0,  theIfStatement.getElseStatements().size());
			}
		}
	}
}