package ast;
import java.util.LinkedList;

public class ASTIfStatement extends ASTStatement
{
	public ASTIfStatement(ASTNode parent) 
	{
		super(parent);
	}


	private String optStr = ""; //may or may not have this part
	
	private LinkedList<ASTStatement> optStmts = new LinkedList<ASTStatement>();
	private boolean isInElse = false;
	
	public boolean isInElse()
	{
		return isInElse;
	}
	
	public void setIsInElse(boolean b)
	{
		isInElse = b;
	}
	
	public void addStatement(ASTStatement s)
	{
		if(!isInElse)
		{
			reqStmts.add(s);
			s.parent = this;
		}
		else
		{
			addOptStatement(s);
		}
	}
	
	
	/**
	 * adds a statement to the statement list in the else
	 * section of an if statement.
	 * @param s
	 */
	private void addOptStatement(ASTStatement s)
	{
		optStmts.add(s);
		s.parent = this;
	}
	
	
	public String toString()
	{
		
		String retStr;
		reqStr = listToString(reqStmts, tabsToString(0));
		
		if(!(optStmts.isEmpty()))
		{
			optStr = new String(new char[getTabLevel() - 4]) + "ELSE\n";
			optStr = optStr.concat(listToString(optStmts, tabsToString(0)));
		}
		retStr = "IF " + exp.toString() + " THEN\n" + reqStr + optStr 
				   + tabsToString(-1) + "END";
		
		return retStr;
	}
	
}
