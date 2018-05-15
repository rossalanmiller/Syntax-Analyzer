package ast;

public class ASTAssignStatement extends ASTStatement
{
	public ASTAssignStatement(ASTNode parent) 
	{
		super(parent);
	}

	private String id;
	
	
	public void setId(String id)
	{
		this.id = id;
	}
	
	public void setConstant(ASTExpression exp) 
	{
		this.exp = exp;
	}
	
	public String toString()
	{
		return id.toString() + " := " + exp.toString();
	}
}
