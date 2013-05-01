package dk.itu.policyengine.policies;
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
		policyEntity.getInterval().setFromTime(new Time(_2300));
		policyEntity.getInterval().setToTime(new Time(_0700));
		policyEntity.setActive(true);
		policyEntity.setName("Lights off");
		policyEntity.setDescription("Turn off the lights during the night");
		DataAccessLayer.persist(policyEntity);
	}
}
