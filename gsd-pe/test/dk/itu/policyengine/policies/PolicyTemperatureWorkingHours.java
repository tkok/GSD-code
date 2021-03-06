package dk.itu.policyengine.policies;

import java.sql.Time;

import org.junit.Test;

import dk.itu.policyengine.domain.Expression;
import dk.itu.policyengine.domain.FloatValue;
import dk.itu.policyengine.domain.IfStatement;
import dk.itu.policyengine.domain.Interval;
import dk.itu.policyengine.domain.Operator;
import dk.itu.policyengine.domain.Policy;
import dk.itu.policyengine.domain.PolicyEntity;
import dk.itu.policyengine.domain.SetStatement;
import dk.itu.policyengine.domain.Statement;
import dk.itu.policyengine.persistence.DataAccessLayer;

public class PolicyTemperatureWorkingHours {
	String lightId1 = "room-2-light-4-gain";
	String lightId2 = "room-2-light-5-gain";
	String blindId1 = "room-2-blind-4-setpoint";
	String blindId2 = "room-2-blind-5-setpoint";
	String temp = "floor-0-room-2.temp";
	String heaterId = "room-2-heater-2-gain";
	String acId = "room-2-ac-2-gain";
	
	@Test
	public void execute() {
		
		// PLEASE DON'T MESS WITH THE FORMATTING, IT'S HARD ENOUGH AS IT IS ;-)
		
		// ********************************************************
		//      If (TEMP > 20) {
		//			TurnOffHeater
		//          If (TEMP > 22) {
		//              TurnOnAC
		//              If (TEMP > 23) {
		//                  RollDownBlinds
		//                  TurnOnLights
		//              }
		//          } Else {
		//              TurnOffAC
		//          }
		//      } Else {
		//          TurnOnHeater
		//      }
		// ********************************************************
				
		// IF TEMP > 20
		Expression expTempIsAbove20 = new Expression(temp, Operator.GREATER_THAN, new FloatValue(20f));
		IfStatement ifTempIsAbove20 = new IfStatement();
		ifTempIsAbove20.addExpression(expTempIsAbove20);
		
			// THEN HEATER = 0
			Statement turnOffHeater = new SetStatement(heaterId, new FloatValue(0f));
			ifTempIsAbove20.addThenStatement(turnOffHeater);
			
		
			// IF TEMP > 22
			Expression expTempIsAbove22 = new Expression(temp, Operator.GREATER_THAN, new FloatValue(22f));
			IfStatement ifTempIsAbove22 = new IfStatement();
			ifTempIsAbove20.addThenStatement(ifTempIsAbove22);
			ifTempIsAbove22.addExpression(expTempIsAbove22);

				// THEN
				Statement turnOnAc = new SetStatement(acId, new FloatValue(1f));
				ifTempIsAbove22.addThenStatement(turnOnAc);
				
			
				// IF TEMP > 23
				Expression expTempIsAbove23 = new Expression(temp, Operator.GREATER_THAN, new FloatValue(23f));
				IfStatement ifTempIsAbove23 = new IfStatement();
				ifTempIsAbove23.addExpression(expTempIsAbove23);
				
				ifTempIsAbove22.addThenStatement(ifTempIsAbove23);
					// THEN
					Statement rollDownBlind1 = new SetStatement(blindId1, new FloatValue(1f));
					Statement rollDownBlind2 = new SetStatement(blindId2, new FloatValue(1f));
					Statement turnOnLight1 = new SetStatement(lightId1, new FloatValue(1f));
					Statement turnOnLight2 = new SetStatement(lightId2, new FloatValue(1f));
					ifTempIsAbove23.addThenStatement(rollDownBlind1);
					ifTempIsAbove23.addThenStatement(rollDownBlind2);
					ifTempIsAbove23.addThenStatement(turnOnLight1);
					ifTempIsAbove23.addThenStatement(turnOnLight2);
			
			// ELSE AC = 0
			Statement turnOffAc = new SetStatement(acId, new FloatValue(0f));
			ifTempIsAbove22.addElseStatement(turnOffAc);
		
		// ELSE HEATER = 1
		SetStatement turnOnHeater = new SetStatement(heaterId, new FloatValue(1f));
		ifTempIsAbove20.addElseStatement(turnOnHeater);
		
		Policy policy = new Policy();
		policy.addStatement(ifTempIsAbove20);
		
		PolicyEntity entity = new PolicyEntity();
		entity.setPolicy(policy);
		
		// From 07:00
		//entity.setFromTime(new Time(7, 0, 0));
		Time fromTime = new Time(7, 0, 0);
		
		// To 17:00
		//entity.setToTime(new Time(23, 59, 0));
		Time toTime = new Time(23, 59, 0);
		
		entity.setInterval(new Interval(fromTime, toTime));
		
		entity.setActive(true);
		entity.setName("Temperature in room 2");
		entity.setDescription("Monitor the temperature in room 2. Turn off the heater if temperature is over 20 and turn on the AC otherwise");
		entity = DataAccessLayer.persist(entity);		
	}
}
