package dk.itu.kben.gsd;

import java.sql.Time;

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

public class Policy_With_Nested_Ifs {
	
	private static String ROOM1_TEMPERATURE = "ROOM1.TEMPERATURE";
	private static String ROOM1_HEATER 		= "ROOM1.HEATER";
	private static String ROOM1_BLINDS 		= "ROOM1.BLINDS";
	
	private static long _08_00 = 7 * 60 * 60 * 1000;
	private static long _16_00 = 15 * 60 * 60 * 1000;
	private static long _21_00 = 20 * 60 * 60 * 1000;
	
	@Before
	public void setupDatabase() {
		BuildingDAO.setValue(ROOM1_TEMPERATURE, new IntValue(11));
		BuildingDAO.setValue(ROOM1_HEATER, new BooleanValue(false));
		BuildingDAO.setValue(ROOM1_BLINDS, new BooleanValue(false));
	}

	@Test
	public void persistNewIfStatement() {
		//BuildingDAL.deleteAll();
		
		IfStatement outerIfStatement = new IfStatement();
		Expression outerIfStatementExpression = new Expression(ROOM1_TEMPERATURE, Operator.GREATER_THAN, new IntValue(25));
		outerIfStatement.addExpression(outerIfStatementExpression);
		
		IfStatement innerIfStatement = new IfStatement();
		Expression innerIfStatementExpression = new Expression(ROOM1_BLINDS, Operator.EQUALS, new BooleanValue(false));
		innerIfStatement.addExpression(innerIfStatementExpression);
		
		Statement innerIfThenSetStatement = new SetStatement(ROOM1_BLINDS, new BooleanValue(true));
		innerIfStatement.addThenStatement(innerIfThenSetStatement);
		
		outerIfStatement.addThenStatement(innerIfStatement);
		
		Policy policy = new Policy();
		policy.addStatement(outerIfStatement);
		
		Time fromTime = new Time(_08_00);
		Time toTime = new Time(_21_00);
		
		PolicyEntity policyEntity = new PolicyEntity();
		
		policyEntity.setPolicy(policy);
		policyEntity.setFromTime(fromTime);
		policyEntity.setToTime(toTime);
		policyEntity.setActive(true);
		
		policyEntity = BuildingDAL.persist(policyEntity);
	}
}