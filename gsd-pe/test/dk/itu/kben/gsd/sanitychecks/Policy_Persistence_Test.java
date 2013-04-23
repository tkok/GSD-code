package dk.itu.kben.gsd.sanitychecks;

import java.sql.Time;
import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import dk.itu.kben.gsd.domain.Expression;
import dk.itu.kben.gsd.domain.FloatValue;
import dk.itu.kben.gsd.domain.IfStatement;
import dk.itu.kben.gsd.domain.Operator;
import dk.itu.kben.gsd.domain.Policy;
import dk.itu.kben.gsd.domain.PolicyEntities;
import dk.itu.kben.gsd.domain.PolicyEntity;
import dk.itu.kben.gsd.domain.SetStatement;
import dk.itu.kben.gsd.domain.Statement;
import dk.itu.kben.gsd.persistence.BuildingDAL;
import dk.itu.scas.gsd.utils.SensorValueCache;

public class Policy_Persistence_Test {
	
	private static String ROOM1_TEMPERATURE = "ROOM1.TEMPERATURE";
	private static String ROOM1_HEATER 		= "ROOM1.HEATER";
	private static String ROOM1_BLINDS 		= "ROOM1.BLINDS";
	
	private static long _08_00 = 7 * 60 * 60 * 1000;
	private static long _16_00 = 15 * 60 * 60 * 1000;
	
	@Before
	public void setupDatabase() {
		SensorValueCache.setValue(ROOM1_TEMPERATURE, new FloatValue(11));
		SensorValueCache.setValue(ROOM1_HEATER, new FloatValue(0));
		SensorValueCache.setValue(ROOM1_BLINDS, new FloatValue(0));
	}

	@Test
	public void persistNewIfStatement() {
		BuildingDAL.deleteAll();
		
		Expression expression = new Expression(ROOM1_TEMPERATURE, Operator.EQUALS, new FloatValue(10));
		Statement thenStatement = new SetStatement(ROOM1_HEATER, new FloatValue(1));
		Statement elseStatement = new SetStatement(ROOM1_BLINDS, new FloatValue(1));
		
		IfStatement ifStatement = new IfStatement();
		ifStatement.addExpression(expression);
		ifStatement.addThenStatement(thenStatement);
		ifStatement.addElseStatement(elseStatement);
		
		Policy policy = new Policy();
		policy.addStatement(ifStatement);
		
		Time fromTime = new Time(_08_00);
		Time toTime = new Time(_16_00);
		
		PolicyEntity policyEntity = new PolicyEntity();
		
		policyEntity.setPolicy(policy);
		policyEntity.setFromTime(fromTime);
		policyEntity.setToTime(toTime);
		policyEntity.setActive(true);
		
		policyEntity = BuildingDAL.persist(policyEntity);
	}
	
	//@Test
	public void notWorkingYet() {
		PolicyEntities policyEntities = BuildingDAL.getActivePolicies(); 
		
		Assert.assertEquals(1, policyEntities.getSize());
		
		for (Statement statement: policyEntities.getPolicyEntities().get(0).getPolicy().getStatements()) {
			if (statement instanceof IfStatement) {
				IfStatement theIfStatement = (IfStatement) statement;
				
				Assert.assertEquals(1,  theIfStatement.getElseStatements().size());
				
				// Delete the ElseStatement
				theIfStatement.setElseStatements(new ArrayList<Statement>());
			}
		}
		
		PolicyEntity policyEntity = BuildingDAL.persist(policyEntities.getPolicyEntities().get(0));
		
		policyEntities = BuildingDAL.getActivePolicies();
		Assert.assertEquals(1, policyEntities.getSize());

		for (Statement statement: policyEntities.getPolicyEntities().get(0).getPolicy().getStatements()) {
			if (statement instanceof IfStatement) {
				IfStatement theIfStatement = (IfStatement) statement;
				
				Assert.assertEquals(0,  theIfStatement.getElseStatements().size());
			}
		}
	}
}