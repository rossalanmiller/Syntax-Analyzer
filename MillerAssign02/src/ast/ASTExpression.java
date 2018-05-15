package ast;

public abstract class ASTExpression extends ASTNode
{
	private ASTExpression leftPart;
	private ASTExpression rightPart;
	private String symbol;
	private boolean hasLeftExpression = false;
	String expStr = "";
	
	public ASTExpression(ASTNode parent)
	{
		this.parent = parent;
	}
	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}
	
	public ASTExpression getLeftPart()
	{
		leftPart.parent = this;
		return leftPart;
	}
	
	public ASTExpression getRightPart()
	{
		rightPart.parent = this;
		return rightPart;
	}
	
	public void attachSingleChildToParent()
	{
		leftPart.parent = this.parent;
	}
	
	public void addExpression(ASTExpression e)
	{
		if(!hasLeftExpression)
		{
			leftPart = e;
			hasLeftExpression = true;
		}
		else
		{
			rightPart = e;
		}
		
	}
	
	public String toString()
	{
		
		expStr = expStr.concat(leftPart.toString());
		if(rightPart != null)
		{
			expStr = expStr.concat(symbol);
			expStr = expStr.concat(rightPart.toString()).trim();
		}
		return expStr;
	}
}
