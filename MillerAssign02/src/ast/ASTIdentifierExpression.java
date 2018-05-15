package ast;

public class ASTIdentifierExpression extends ASTExpression
{
	public ASTIdentifierExpression(ASTNode parent) 
	{
		super(parent);
	}

	private String id = "";
	
	public void setIdentifier(String id)
	{
		this.id = id;
	}
	
	public String toString()
	{
		return id;
	}
}
