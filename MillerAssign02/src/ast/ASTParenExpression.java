package ast;

public class ASTParenExpression extends ASTExpression
{
	public ASTParenExpression(ASTNode parent) 
	{
		super(parent);
	}

	public String toString()
	{
		return "(" + super.toString() + ")";
	}
}	
