
// Parsing shell partially completed
// Note that EBNF rules are provided in comments
// Just add new methods below rules without them

import java.util.*;

import ast.*;

public class Parser extends Object
{
	private Chario chario;
	private Scanner scanner;
	private Token token;

	private ASTNode current;// = new ASTNode();
	private ASTNode next;// 	= new ASTNode();
	private ASTProgram prg;// = new ASTProgram();
	
	private Set<Integer> addOperators, multOperators, comparisonOperator;

	public Parser(Chario c, Scanner s)
	{
		chario = c;
		scanner = s;
		initHandles();
		this.token = scanner.nextToken();
	}

	public void reset()
	{
		scanner.reset();
		this.token = scanner.nextToken();
	}

	private void initHandles()
	{
		addOperators = new HashSet<Integer>();
		addOperators.add(Token.PLUS);
		addOperators.add(Token.MINUS);

		multOperators = new HashSet<Integer>();
		multOperators.add(Token.MULT);
		multOperators.add(Token.DIV);

		comparisonOperator = new HashSet<Integer>();
		comparisonOperator.add(Token.LT);
		comparisonOperator.add(Token.EQ);
	}

	private void accept(int expected, String errorMessage)
	{
		if (this.token.code != expected)
			fatalError(errorMessage);
		this.token = scanner.nextToken();
	}

	private void fatalError(String errorMessage)
	{
		chario.putError(errorMessage);
		throw new RuntimeException("Fatal error");
	}

	public void parse()
	{
		
		current = new ASTNode();
		next 	= new ASTNode();
		prg = new ASTProgram();
		
		current = prg;
		while (this.token.code != Token.EOF)
		{
			stmt_sequence();
		}
		
		System.out.println(prg);
		
	}

	/*-------------------------------------------------------
	 stmt-sequence -> statement ; stmt-sequence | statement
	--------------------------------------------------------*/
	private void stmt_sequence()
	{
		
		statement();
		if(token.code == Token.SEMI)
		{
			accept(Token.SEMI,"Semicolon not found");
			stmt_sequence();
		}
		
	}

	/*---------------------------------------------------------------
	  statement -> if-stmt | repeat-stmt | assign-stmt | 
	  				read-stmt | write-stmt 
	----------------------------------------------------------------*/
	private void statement()
	{
		switch(token.code)
		{
			case Token.IF:
				if_stmt();
				break;
			case Token.REPEAT:
				repeat_stmt();
				break;
			case Token.IDENTIFIER:
				assign_stmt();
				break;
			case Token.READ:
				read_stmt();
				break;
			case Token.WRITE:
				write_stmt();
				break;
			default: fatalError("Statement not found");
		}
	}

	/*---------------------------------------------------------------
	  if-stmt -> if exp then stmt-sequence end 
		        | if exp then stmt-sequence else stmt-sequence end
	----------------------------------------------------------------*/
	private void if_stmt()
	{
		next = new ASTIfStatement(current);
		current.addStatement((ASTStatement) next);
		current = next;
		
		accept(Token.IF,"keword 'if' not found");
		exp();
		accept(Token.THEN,"keword 'then' not found");
		
		
		stmt_sequence();
		
		if(token.code == Token.ELSE)
		{
			((ASTIfStatement) current).setIsInElse(true);
			this.token = scanner.nextToken();
			
			stmt_sequence(); //statement sequences are a bit of a special case
			((ASTIfStatement) current).setIsInElse(false);
		}
		accept(Token.END, "keyword 'end' not found");
		
		current = current.parent;
	}

	/*---------------------------------------------------------------
	   repeat-stmt -> repeat stmt-sequence until exp
	----------------------------------------------------------------*/
	private void repeat_stmt()
	{
		next = new ASTRepeatStatement(current);
		current.addStatement((ASTStatement) next);
		current = next;
		
		accept(Token.REPEAT,"keyword 'repeat' not found");
		
		stmt_sequence();
		
		accept(Token.UNTIL, "keyword 'until' not found");
		exp();
		
		current = current.parent;
	}

	/*---------------------------------------------------------------
	   assign-stmt -> identifier := exp
	----------------------------------------------------------------*/
	private void assign_stmt()
	{
		next = new ASTAssignStatement(current);
		current.addStatement((ASTStatement) next);
		current = next;
		((ASTAssignStatement) current).setId(this.token.string);
		
		accept(Token.IDENTIFIER, "identifier not found");
		accept(Token.ASSIGN, ":= not found");
		
		exp();
		
		current = current.parent;
	}

	/*---------------------------------------------------------------
		read-stmt -> read identifier 
	----------------------------------------------------------------*/
	private void read_stmt()
	{
		next = new ASTReadStatement(current);
		current.addStatement((ASTStatement) next);
		current = next;
		
		accept(Token.READ, "keyword 'read' not found");
		
		((ASTReadStatement) current).setIdentifier(this.token.string);
		accept(Token.IDENTIFIER, "identifier not found");

		
		current = current.parent;
	}

	/*---------------------------------------------------------------
		write-stmt -> write exp
	----------------------------------------------------------------*/
	private void write_stmt()
	{
		next = new ASTWriteStatement(current);
		current.addStatement((ASTStatement) next);
		current = next;
		
		accept(Token.WRITE, "keyword 'write' not found");
		exp();
		
		current = current.parent;
	}

	/*---------------------------------------------------------------
		exp -> simple-exp comparison-op simple-exp | simple-exp
	----------------------------------------------------------------*/
	private void exp()
	{
		next = new ASTComparisonExpression(current);
		current.addExpression((ASTExpression) next);
		current = next;

		simple_exp();
		
		//if the next token is a comparison operator
		if(comparisonOperator.contains(token.code))
		{
			switch(token.code)
			{
				case Token.LT:
					current.setSymbol(" < ");
					this.token = scanner.nextToken();
					break;
				case Token.EQ:
					current.setSymbol(" = ");
					this.token = scanner.nextToken();
					break;
				default:
					fatalError("comparison operator not found, either '=' or '<' expected");
			}
			
			simple_exp();
		}
		else
		{
			((ASTExpression) current).attachSingleChildToParent();
		}
		
		current = current.parent;
	}

	/*---------------------------------------------------------------
	  simple-exp -> term addop simple-exp | term
	----------------------------------------------------------------*/
	private void simple_exp()
	{
		next = new ASTAddExpression(current);
		current.addExpression((ASTExpression) next);
		current = next;
		
		term();
		
		if(addOperators.contains(token.code))
		{
			switch(token.code)
			{
				case Token.PLUS:
					current.setSymbol(" + ");
					this.token = scanner.nextToken();
					break;
				case Token.MINUS:
					current.setSymbol(" - ");
					this.token = scanner.nextToken();
					break;
				default:
					fatalError("additive operator not found, either '+' or '-' expected");
			}
			simple_exp();
		}
		else
		{
			((ASTExpression) current).attachSingleChildToParent();
		}
		
		current = current.parent;
	}

	/*---------------------------------------------------------------
	  term -> factor multop term | factor
	----------------------------------------------------------------*/
	private void term()
	{
		next = new ASTMultExpression(current);
		current.addExpression((ASTExpression) next);
		current = next;
		
		factor();
		if(multOperators.contains(token.code))
		{
			switch(token.code)
			{
				case Token.MULT:
					current.setSymbol(" * ");
					this.token = scanner.nextToken();
					break;
				case Token.DIV:
					current.setSymbol(" / ");
					this.token = scanner.nextToken();
					break;
				default:
					fatalError("multiplicative operator not found, either '*' or '/' expected");
			}
			term();
		}
		else
		{
			((ASTExpression) current).attachSingleChildToParent();
		}
		
		current = current.parent;
	}

	/*---------------------------------------------------------------
	  factor -> ( exp ) | number | identifier
	----------------------------------------------------------------*/
	private void factor()
	{
		switch(token.code)
		{
			case Token.LPAREN:
				next = new ASTParenExpression(current);
				current.addExpression((ASTExpression) next);
				current = next;
				
				this.token = scanner.nextToken();
				exp();
				accept(Token.RPAREN, "Right parenthesies expected");
				break;
				
			case Token.NUMBER:
				next = new ASTConstantExpression(current);
				((ASTConstantExpression) next).setConstant(this.token.integer);
				
				current.addExpression((ASTExpression) next);
				current = next;
				
				this.token = scanner.nextToken();
				break;
				
			case Token.IDENTIFIER:
				next = new ASTIdentifierExpression(current);
				((ASTIdentifierExpression) next).setIdentifier(this.token.string);
				
				current.addExpression((ASTExpression) next);
				current = next;
				
				this.token = scanner.nextToken();
				break;
				
			default:
				fatalError("Number or identifiyer expected");
		}
		
		current = current.parent;
	}

}
