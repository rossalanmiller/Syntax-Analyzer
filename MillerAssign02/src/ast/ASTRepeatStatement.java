package ast;


public class ASTRepeatStatement extends ASTStatement
{
	
	
	public ASTRepeatStatement(ASTNode parent) 
	{
		super(parent);
	}

	public String toString()
	{
		return  "REPEAT\n" 
				+ listToString(reqStmts, tabsToString(0))
				+ tabsToString(0) + "UNTIL " + exp.toString();
	}
}
