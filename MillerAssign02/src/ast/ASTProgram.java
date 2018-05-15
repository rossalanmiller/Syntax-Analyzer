package ast;
import java.util.LinkedList;

public class ASTProgram extends ASTNode
{
	
	
	LinkedList<ASTStatement> statementList = new LinkedList<ASTStatement>();
	
	public void addStatement(ASTStatement s)
	{
		statementList.add(s);
		s.parent = this;
	}
	
	
	public String toString()
	{
		
		return listToString(statementList, tabsToString(0));
	}
	
	public boolean isEmpty()
	{
		return statementList.isEmpty();
	}
	
}
