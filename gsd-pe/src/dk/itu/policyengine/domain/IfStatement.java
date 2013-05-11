package dk.itu.policyengine.domain;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import dk.itu.policyengine.integration.Connection;
import dk.itu.policyengine.persistence.SensorValueCache;
import dk.itu.policyengine.utils.Wildcards;

public class IfStatement implements Statement {
	
	ArrayList<Expression> conditionalExpressions = new ArrayList<Expression>();
	ArrayList<Statement> thenStatements = new ArrayList<Statement>();
	ArrayList<Statement> elseStatements = new ArrayList<Statement>();
	
	public IfStatement() {
	}
	
	public void addExpression(Expression expression) {
		conditionalExpressions.add(expression);
	}
	
	public void addThenStatement(Statement statement) {
		thenStatements.add(statement);
	}

	public void addElseStatement(Statement statement) {
		elseStatements.add(statement);
	}
	public ArrayList<Expression> getExpressions(){
		return conditionalExpressions;
	}
	public ArrayList<Statement> getElseStatements() {
		return elseStatements;
	}
	
	public void setElseStatements(ArrayList<Statement> elseStatements) {
		this.elseStatements = elseStatements;
	}

	public void execute() {
		boolean fail = false;
		boolean execute = true;
		//TODO prefixOperator not yet supported.
		
		for (Expression e : conditionalExpressions) {
			if(e.isWildcard()){
				try {
					String [] type = e.getSensorId().split("-");
					List<String> sensorList = new Wildcards().getSensorListByWildcard(e.getSensorId());
					for(String s : sensorList){
						String id = s+"-"+type[type.length-1];
						System.out.println(id);
						SensorValueCache.setValue(id, new FloatValue(Float.parseFloat(new Connection().getSensorValue(id))));
						if(e.eval(id)){
							for (Statement statement : thenStatements) {
								statement.execute(id);
							}
						}
						else{
							for(Statement statement : elseStatements){
								statement.execute(id);
							}
						}
					}
					
				} catch (MalformedURLException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				} catch (IOException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				} catch (URISyntaxException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}
				execute = false;
			}
			else{
				if (!e.evaluate()) {
					fail = true;
					break;
				}
			}
		}
		if(execute){
			if (!fail) {
				for (Statement statement : thenStatements) {
					statement.execute();
				}
			}
			else {
				for (Statement statement : elseStatements) {
					statement.execute();
				}
			}
		}
	}

	@Override
	public void execute(String sensorId) {
		// TODO Auto-generated method stub
		
	}
}