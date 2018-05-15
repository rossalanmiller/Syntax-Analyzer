package ast;

public class ASTWriteStatement extends ASTStatement
{
	public ASTWriteStatement(ASTNode parent) 
	{
		super(parent);
	}

	//private ASTExpression exp;
	
	public void setExpression(ASTExpression exp)
	{
		this.exp = exp;
	}
	
	public String toString()
	{
		return "WRITE " + exp.toString();
	}
	
	
}
