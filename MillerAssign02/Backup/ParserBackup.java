
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
		while (this.token.code != Token.EOF)
		{
			stmt_sequence();
		}
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
		accept(Token.IF,"keword 'if' not found");
		exp();
		accept(Token.THEN,"keword 'then' not found");
		stmt_sequence();
		if(token.code == Token.ELSE)
		{
			this.token = scanner.nextToken();
			stmt_sequence();
		}
		accept(Token.END, "keyword 'end' not found");
		
		
	}

	/*---------------------------------------------------------------
	   repeat-stmt -> repeat stmt-sequence until exp
	----------------------------------------------------------------*/
	private void repeat_stmt()
	{
		accept(Token.REPEAT,"keyword 'repeat' not found");
		stmt_sequence();
		accept(Token.UNTIL, "keyword 'until' not found");
		exp();
		
	}

	/*---------------------------------------------------------------
	   assign-stmt -> identifier := exp
	----------------------------------------------------------------*/
	private void assign_stmt()
	{
		accept(Token.IDENTIFIER, "identifier not found");
		accept(Token.ASSIGN, ":= not found");
		exp();
	}

	/*---------------------------------------------------------------
		read-stmt -> read identifier 
	----------------------------------------------------------------*/
	private void read_stmt()
	{
		accept(Token.READ, "keyword 'read' not found");
		accept(Token.IDENTIFIER, "identifier not found");
	}

	/*---------------------------------------------------------------
		write-stmt -> write exp
	----------------------------------------------------------------*/
	private void write_stmt()
	{
		accept(Token.WRITE, "keyword 'write' not found");
		exp();
	}

	/*---------------------------------------------------------------
		exp -> simple-exp comparison-op simple-exp | simple-exp
	----------------------------------------------------------------*/
	private void exp()
	{
		simple_exp();
		
		//if the next token is a comparison operator
		if(comparisonOperator.contains(token.code))
		{
			switch(token.code)
			{
				case Token.LT:
					this.token = scanner.nextToken();
					break;
				case Token.EQ:
					this.token = scanner.nextToken();
					break;
				default:
					fatalError("comparison operator not found, either '=' or '<' expected");
			}
			simple_exp();
		}
		
	}

	/*---------------------------------------------------------------
	  simple-exp -> term addop simple-exp | term
	----------------------------------------------------------------*/
	private void simple_exp()
	{
		term();
		if(addOperators.contains(token.code))
		{
			switch(token.code)
			{
				case Token.PLUS:
					this.token = scanner.nextToken();
					break;
				case Token.MINUS:
					this.token = scanner.nextToken();
					break;
				default:
					fatalError("additive operator not found, either '+' or '-' expected");
			}
			simple_exp();
		}
	}

	/*---------------------------------------------------------------
	  term -> factor multop term | factor
	----------------------------------------------------------------*/
	private void term()
	{
		factor();
		if(multOperators.contains(token.code))
		{
			switch(token.code)
			{
				case Token.MULT:
					this.token = scanner.nextToken();
					break;
				case Token.DIV:
					this.token = scanner.nextToken();
					break;
				default:
					fatalError("multiplicative operator not found, either '*' or '/' expected");
			}
			term();
		}
		
	}

	/*---------------------------------------------------------------
	  factor -> ( exp ) | number | identifier
	----------------------------------------------------------------*/
	private void factor()
	{
		//exp();
		switch(token.code)
		{
			case Token.LPAREN:
				this.token = scanner.nextToken();
				exp();
				accept(Token.RPAREN, "Right parenthesies expected");
				break;
			case Token.NUMBER:
				this.token = scanner.nextToken();
				break;
			case Token.IDENTIFIER:
				this.token = scanner.nextToken();
				break;
			default:
				fatalError("Number or identifiyer expected");
		}
	}

}
