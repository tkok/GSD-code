package dk.itu.kben.gsd;
/**
 * @author Stefan
 * Policy for cooling the environment if it is too hot.  
 * 
 */
import static org.junit.Assert.*;

import java.sql.Time;
import java.util.ArrayList;
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

public class PolicyCooling {
	private List<String> ids;
	private List<String> acIds;

	long _2400 = 23*60*60*1000;
	long _0000 = 24*60*60*1000;
	@Before
	public void getAcIDS(){
		
		Connection connection = new Connection();
		acIds = new ArrayList<String>();
		addAcId(connection.getSensorListByRoomId("room-6"));
		addAcId(connection.getSensorListByRoomId("room-13"));
		addAcId(connection.getSensorListByRoomId("room-20"));
	}
	public void addAcId(List<String> id){
		for(String s : id){
			if(s.contains("ac")){
				System.out.println(s);
				acIds.add(s+"-gain");
			}
		}
	}
	@Test 
	public void checkNumberOfAcs(){
		assertEquals(6,acIds.size());
	}
	@Test
	public void test() {
		Expression expression = new Expression("environment.temp",Operator.GREATER_THAN,new FloatValue(23f));
		Policy policy = new Policy();
		for(String id : acIds){
			IfStatement ifStatement = new IfStatement();
			Statement turnOnAc = new SetStatement(id,new FloatValue(1f));
			Statement turnOffAc = new SetStatement(id,new FloatValue(0f));
			ifStatement.addExpression(expression);
			ifStatement.addThenStatement(turnOnAc);
			ifStatement.addElseStatement(turnOffAc);
			policy.addStatement(ifStatement);
		}
		PolicyEntity policyEntity = new PolicyEntity();
		policyEntity.setPolicy(policy);
		policyEntity.setFromTime(new Time(_2400));
		policyEntity.setToTime(new Time(23,59,0));
		policyEntity.setActive(true);
		policyEntity.setName("Cool the hallways");
		policyEntity.setDescription("If the environment temperature is higher than 23 degrees, then turn on  " +
			"turn on the air conditioning units");
		
		BuildingDAL.persist(policyEntity);
		
		
	}

}
