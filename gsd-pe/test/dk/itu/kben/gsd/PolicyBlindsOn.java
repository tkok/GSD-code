package dk.itu.kben.gsd;
/**
 * @author = Stefan
 * Roll down the blinds during the night - it should help preserve heat
 * 
 */
import static org.junit.Assert.assertEquals;

import java.sql.Time;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import dk.itu.kben.gsd.domain.Expression;
import dk.itu.kben.gsd.domain.FloatValue;
import dk.itu.kben.gsd.domain.IfStatement;
import dk.itu.kben.gsd.domain.Operator;
import dk.itu.kben.gsd.domain.Policy;
import dk.itu.kben.gsd.domain.PolicyEntity;
import dk.itu.kben.gsd.domain.SetStatement;
import dk.itu.kben.gsd.domain.Statement;
import dk.itu.kben.gsd.persistence.BuildingDAL;
import dk.itu.scas.gsd.net.Connection;

public class PolicyBlindsOn {
	private List<String> blindIds;
	
	@Before
	public void execute(){
		blindIds = new Connection().getAllSensorIdsByType("blind");
		assertEquals(30,blindIds.size());
	}
	@Test 
	public void insertPolicy(){
		Policy policy = new Policy();
		for(String id : blindIds){
			Expression expression = new Expression(id+"-setpoint", Operator.EQUALS, new FloatValue(0f));
			IfStatement ifStatement = new IfStatement();
			Statement blindsDown = new SetStatement(id+"-setpoint",new FloatValue(1f));
			ifStatement.addExpression(expression);
			ifStatement.addThenStatement(blindsDown);
			policy.addStatement(ifStatement);
		}
		
		PolicyEntity policyEntity = new PolicyEntity();
		policyEntity.setPolicy(policy);
		policyEntity.setFromTime(new Time(23,0,0));
		policyEntity.setToTime(new Time(06,0,0));
		policyEntity.setActive(true);
		policyEntity.setName("Roll blinds down");
		policyEntity.setDescription("Roll blinds down to conserve heat.");
		
		BuildingDAL.persist(policyEntity);
		
	}
	
}
