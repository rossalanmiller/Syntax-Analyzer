package ast;

public class ASTReadStatement extends ASTStatement
{
	public ASTReadStatement(ASTNode parent) {
		super(parent);
		// TODO Auto-generated constructor stub
	}

	private String id;
	
	public void setIdentifier(String i)
	{
		id = i;
	}
	
	public String toString()
	{
		return "READ " + id.toString();
	}
}
