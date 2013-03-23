package dk.itu.kben.gsd;

import java.util.ArrayList;

public class IfStatement implements Statement {
	
	ArrayList<Expression> conditionalExpressions = new ArrayList<Expression>();
	ArrayList<Statement> thenStatements = new ArrayList<Statement>();
	ArrayList<Statement> elseStatements = new ArrayList<Statement>();
	
	public void addExpression(Expression expression) {
		conditionalExpressions.add(expression);
	}
	
	public void addThenStatement(Statement statement) {
		thenStatements.add(statement);
	}

	public void addElseStatement(Statement statement) {
		elseStatements.add(statement);
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