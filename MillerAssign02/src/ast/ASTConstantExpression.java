package ast;

public class ASTConstantExpression extends ASTExpression
{
	public ASTConstantExpression(ASTNode parent) 
	{
		super(parent);
	}

	private int constant;
	
	public void setConstant(int constant)
	{
		this.constant = constant;
	}
	
	public String toString()
	{
		return constant + "";
	}
}
