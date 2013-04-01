package dk.itu.kben.gsd;

import java.sql.Time;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import dk.itu.kben.gsd.domain.BooleanValue;
import dk.itu.kben.gsd.domain.Expression;
import dk.itu.kben.gsd.domain.IfStatement;
import dk.itu.kben.gsd.domain.IntValue;
import dk.itu.kben.gsd.domain.Operator;
import dk.itu.kben.gsd.domain.Policy;
import dk.itu.kben.gsd.domain.PolicyEntity;
import dk.itu.kben.gsd.domain.SetStatement;
import dk.itu.kben.gsd.domain.Statement;
import dk.itu.kben.gsd.persistence.BuildingDAL;
import dk.itu.kben.gsd.persistence.BuildingDAO;

public class Policy_Persistence_Test {
	
	private static String ROOM1_TEMPERATURE = "ROOM1.TEMPERATURE";
	private static String ROOM1_HEATER 		= "ROOM1.HEATER";
	private static String ROOM1_BLINDS 		= "ROOM1.BLINDS";
	
	private static long _08_00 = 7 * 60 * 60 * 1000;
	private static long _16_00 = 15 * 60 * 60 * 1000;
	
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
		
		Policy policy = new Policy();
		policy.addStatement(ifStatement);
		
		Time fromTime = new Time(_08_00);
		Time toTime = new Time(_16_00);
		
		PolicyEntity policyEntity = new PolicyEntity();
		
		policyEntity.setPolicy(policy);
		policyEntity.setFromTime(fromTime);
		policyEntity.setToTime(toTime);
		policyEntity.setActive(true);
		
		BuildingDAL.store(policyEntity);
		
		ArrayList<PolicyEntity> policies = BuildingDAL.getActivePolicies();
		
		System.out.println("There is " + policies.size() + " active policies.");
	}
}