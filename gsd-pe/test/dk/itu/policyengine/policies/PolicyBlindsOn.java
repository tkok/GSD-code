package dk.itu.policyengine.policies;
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

import dk.itu.policyengine.domain.Expression;
import dk.itu.policyengine.domain.FloatValue;
import dk.itu.policyengine.domain.IfStatement;
import dk.itu.policyengine.domain.Operator;
import dk.itu.policyengine.domain.Policy;
import dk.itu.policyengine.domain.PolicyEntity;
import dk.itu.policyengine.domain.SetStatement;
import dk.itu.policyengine.domain.Statement;
import dk.itu.policyengine.integration.Connection;
import dk.itu.policyengine.persistence.DataAccessLayer;

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
		policyEntity.getInterval().setFromTime(new Time(23,0,0));
		policyEntity.getInterval().setToTime(new Time(06,0,0));
		policyEntity.setActive(true);
		policyEntity.setName("Roll blinds down");
		policyEntity.setDescription("Roll blinds down to conserve heat.");
		
		DataAccessLayer.persist(policyEntity);
		
	}
	
}
