package ast;

import java.util.LinkedList;

public class ASTNode
{	
	public ASTNode parent;
	
	private int tabLevel = 0;

	public int getTabLevel()
	{
		return tabLevel;
	}
	
	public String tabsToString(int offset)
	{
		String tabs = new String();
		for(int i = 0; i < tabLevel + offset; i++)
		{
			tabs += "\t";
		}
		return tabs;
	}

	public void setTabLevel(int tabLevel)
	{
		this.tabLevel = tabLevel;
	}
	
	//helper method for printing out a list
	public static String listToString(LinkedList<ASTStatement> statementList, String tabs)
	{
		String listStr = "";
		
		for(int i = 0; i < statementList.size() - 1; i++) //for the last statements we won't add a semicolon
		{
			listStr = listStr.concat((tabs + statementList.get(i).toString().trim() + ";\n"));
			
		}
		listStr = listStr.concat(tabs + statementList.get(statementList.size() - 1).toString() + "\n");
		return listStr;
	}
	
	public void addStatement(ASTStatement s){}
	
	public void addExpression(ASTExpression next){}
	
	public void setSymbol(String s){}
	
}
