package dk.itu.kben.gsd;

import java.sql.Time;

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

public class PolicyLightning {
	String lightId = "room-2-light-5-gain";
	String heaterId = "room-2-heater-2-gain";
	
	long _2100 = 20*60*60*1000;
	long _0800 = 6*60*60*1000;
	
	@Test
	public void execute() {
		// If the light is on then turn it off (pretty annoying, but hey!)
		
		Expression expressionLightIsOn = new Expression(lightId, Operator.EQUALS, new FloatValue(1f));
		
		Statement turnOffLight = new SetStatement(lightId, new FloatValue(0f));
		
		IfStatement ifLightIsOn = new IfStatement();
		ifLightIsOn.addExpression(expressionLightIsOn);
		ifLightIsOn.addThenStatement(turnOffLight);
		
		Policy policy = new Policy();
		policy.addStatement(ifLightIsOn);
		
		PolicyEntity entity = new PolicyEntity();
		entity.setPolicy(policy);
		
		entity.setFromTime(new Time(_2100));
		entity.setToTime(new Time(_0800));
		entity.setActive(true);
		
		entity = BuildingDAL.persist(entity);		
	}
}
