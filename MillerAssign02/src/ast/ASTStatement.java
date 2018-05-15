package ast;

import java.util.LinkedList;

public abstract class ASTStatement extends ASTNode
{
	protected String reqStr = ""; //every if statement will have this part it is "required"
	protected LinkedList<ASTStatement> reqStmts = new LinkedList<ASTStatement>();
	protected ASTExpression exp;
	
	public ASTStatement(ASTNode parent)
	{
		this.parent = parent;
		this.setTabLevel(parent.getTabLevel() + 1);
	}
	
	public void addStatement(ASTStatement s)
	{
		reqStmts.add(s);
	}
	
	public void addExpression(ASTExpression e)
	{
		exp = e;
	}
	
	/*
	public ASTStatement getReqStatement()
	{
		return reqStmts.getLast();
	}
	*/
	
}
