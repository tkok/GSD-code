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

public class PolicyTemperatureWorkingHours {
	String lightId = "room-2-light-5-gain";
	String blindsId = "room-2-blinds-gain";
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
					Statement rollDownBlinds = new SetStatement(blindsId, new FloatValue(1f));
					Statement turnOnLights = new SetStatement(lightId, new FloatValue(1f));
					ifTempIsAbove23.addThenStatement(rollDownBlinds);
					ifTempIsAbove23.addThenStatement(turnOnLights);
			
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
		entity.setFromTime(new Time(7, 0, 0));
		
		// To 17:00
		entity.setToTime(new Time(23, 59, 0));
		entity.setActive(true);
		
		entity = BuildingDAL.persist(entity);		
	}
}
