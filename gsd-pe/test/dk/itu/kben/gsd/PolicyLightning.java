package dk.itu.kben.gsd;

import java.sql.Time;

import org.junit.Before;
import org.junit.Test;

import dk.itu.kben.gsd.domain.BooleanValue;
import dk.itu.kben.gsd.domain.Expression;
import dk.itu.kben.gsd.domain.FloatValue;
import dk.itu.kben.gsd.domain.IfStatement;
import dk.itu.kben.gsd.domain.IntValue;
import dk.itu.kben.gsd.domain.Operator;
import dk.itu.kben.gsd.domain.Policy;
import dk.itu.kben.gsd.domain.PolicyEntities;
import dk.itu.kben.gsd.domain.PolicyEntity;
import dk.itu.kben.gsd.domain.SetStatement;
import dk.itu.kben.gsd.domain.Statement;
import dk.itu.kben.gsd.persistence.BuildingDAL;
import dk.itu.kben.gsd.persistence.BuildingDAO;

public class PolicyLightning {
	String lightId = "room-2-light-5-gain";
	String heaterId = "room-2-heater-2-gain";
	long _2100 = 20*60*60*1000;
	long _0800 = 6*60*60*1000;
	@Before
	public void setupDatabase() {
		BuildingDAO.setValue(lightId, new IntValue(1));
		BuildingDAO.setValue(heaterId, new IntValue(1));
	}
	@Test
	public void execute(){
		/**
		 * Expressions for light and heater. 
		 */
		Expression light = new Expression(lightId,Operator.EQUALS, new FloatValue(1f));
		Statement turnOffLight = new SetStatement(lightId, new FloatValue(0f));
		Expression heater = new Expression(heaterId, Operator.EQUALS, new FloatValue(1f));
		Statement turnOffHeater = new SetStatement(heaterId, new FloatValue(0f));
		IfStatement statement = new IfStatement();
		statement.addExpression(light);
		statement.addThenStatement(turnOffLight);
		statement.addElseStatement(new SetStatement());
		statement.addExpression(heater);
		statement.addThenStatement(turnOffHeater);
		statement.addElseStatement(new SetStatement());
		
		/**
		 * Policy
		 */
		Policy policy = new Policy();
		policy.addStatement(statement);
		
		PolicyEntity entity = new PolicyEntity();
		entity.setPolicy(policy);
		
		entity.setFromTime(new Time(_2100));
		entity.setToTime(new Time(_0800));
		entity.setActive(true);
		
		/**
		 * 
		 */
		BuildingDAL.store(entity);
		
		PolicyEntities policyEntities = BuildingDAL.getActivePolicies();
		System.out.println("There is " + policyEntities.getSize() + " active policies.");
	}
}
