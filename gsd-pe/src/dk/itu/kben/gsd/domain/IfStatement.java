package dk.itu.kben.gsd.domain;

import java.util.ArrayList;

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
		
		//TODO prefixOperator not yet supported.
		for (Expression e : conditionalExpressions) {
			if (!e.evaluate()) {
				fail = true;
				break;
			}
		}
		
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