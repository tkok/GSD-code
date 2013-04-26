package dk.itu.kben.gsd;
/**
 * @author Stefan
 * Policy for shutting off all the lights during the night.
 * 
 */
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

public class PolicyLightsOff {
	
	
	long _2300 = 21*60*60*1000;
	long _0700 = 6*60*60*1000;
	List<String> lightIds;
	@Test
	public void execute(){
		lightIds = new Connection().getAllSensorIdsByType("light");
		Policy lightOff = new Policy();
		for(String s: lightIds){
			Expression expression = new Expression(s,Operator.EQUALS,new FloatValue(1f));
			Statement turnOffLight = new SetStatement(s, new FloatValue(0f));
			IfStatement ifLightIsOn = new IfStatement();
			ifLightIsOn.addExpression(expression);
			ifLightIsOn.addThenStatement(turnOffLight);
			lightOff.addStatement(ifLightIsOn);
		}
		PolicyEntity policyEntity = new PolicyEntity();
		policyEntity.setPolicy(lightOff);
		policyEntity.setFromTime(new Time(_2300));
		policyEntity.setToTime(new Time(_0700));
		policyEntity.setActive(true);
		
		BuildingDAL.persist(policyEntity);
	}
}
