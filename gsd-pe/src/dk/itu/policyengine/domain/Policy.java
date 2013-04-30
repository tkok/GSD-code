package dk.itu.policyengine.domain;

import java.util.ArrayList;

import com.google.gson.Gson;

public class Policy {

	ArrayList<Statement> statements = new ArrayList<Statement>();
	
	public Policy() {
	}
	
	public void addStatement(Statement statement) {
		statements.add(statement);
	}
	
	public void execute() {
		for (Statement statement : statements) {
			statement.execute();
		}
	}
	
	public String getJSON() {
		Gson gson = GsonFactory.getInstance();
		
		return gson.toJson(this);
	}
	
	public ArrayList<Statement> getStatements() {
		return statements;
	}
}