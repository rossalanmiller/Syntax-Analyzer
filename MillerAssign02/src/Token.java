public class Token{

	public static final int ASSIGN = 0;
	public static final int DIV = 1;
	public static final int ELSE = 2;
	public static final int END = 3;
	public static final int EOF = 4;
	public static final int EQ = 5;
	public static final int ERROR = 6;
	public static final int IDENTIFIER = 7;
	public static final int IF = 8;
	public static final int LPAREN = 9;
	public static final int LT = 10;
	public static final int MINUS = 11;
	public static final int MULT = 12;
	public static final int NUMBER = 13;
	public static final int PLUS = 14;
	public static final int READ = 15;
	public static final int REPEAT = 16;
	public static final int RPAREN = 17;
	public static final int SEMI = 18;
	public static final int THEN = 19;
	public static final int UNTIL = 20;	
	public static final int WRITE = 21;
	
   public int code;
   public int integer;
   public String string;

   public Token(int newCode){
      code = newCode;
      integer = 0;
      string = "";
   }

   public String toString()
   {	   
      String s = "Code    = " + CODES[code];
      if (code == NUMBER)
          s += "\nInteger = " + integer;
      else if (code == IDENTIFIER)     
      {
          s += "\nString = " + string;
      }
      return s;
   }

   private static final String CODES[] = {"ASSIGN", "DIV", "ELSE", "END", "EOF", "EQ", "ERROR",
		   									"IDENTIFIER", "IF", "LPAREN", "LT", "MINUS", 
		   									"MULT", "NUMBER", "PLUS", "READ", "REPEAT", 
		   									"RPAREN", "SEMI", "THEN", "UNTIL", "WRITE"};
}