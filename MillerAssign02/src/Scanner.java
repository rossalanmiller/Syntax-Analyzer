import java.util.*;

public class Scanner 
{

   private Token token;
   private StringBuffer tokenBuffer;
   private Map<String, Token> keywords;
   private Map<String, Token> singleOps;
   private Map<String, Token> doubleOps;
   private Chario chario;
   private char ch;

   private static final int MAX_KEY_SPELLING = 9;

   public Scanner(Chario c)
   {
      chario = c;
      initKeywords();
      initSingleOps();
      initDoubleOps();
      ch = chario.getChar();
   }

   public void reset()
   {
      chario.reset();
      ch = chario.getChar();
   }
      
   private void initKeywords()
   {
      keywords = new HashMap<String, Token>();    
      keywords.put("IF", new Token(Token.IF));
      keywords.put("THEN", new Token(Token.THEN));    
      keywords.put("END", new Token(Token.END));      
      keywords.put("ELSE", new Token(Token.ELSE));      
      keywords.put("REPEAT", new Token(Token.REPEAT));
      keywords.put("UNTIL", new Token(Token.UNTIL));      
      keywords.put("READ", new Token(Token.READ));
      keywords.put("WRITE", new Token(Token.WRITE));  
   }

   private void initSingleOps()
   {
      singleOps = new HashMap<String, Token>();    
     
      singleOps.put("=", new Token(Token.EQ)); 
      singleOps.put("<", new Token(Token.LT));
      singleOps.put("(", new Token(Token.LPAREN));
      singleOps.put("-", new Token(Token.MINUS));
      singleOps.put("*", new Token(Token.MULT));
      singleOps.put("/", new Token(Token.DIV));
      singleOps.put("+", new Token(Token.PLUS));
      singleOps.put(")", new Token(Token.RPAREN)); 
      singleOps.put(";", new Token(Token.SEMI)); 
   }

   private void initDoubleOps()
   {
      doubleOps = new HashMap<String, Token>();      
      doubleOps.put(":=", new Token(Token.ASSIGN));     
   }

   private Token findToken(Map<String, Token> table, String target)
   {
      Token t = table.get(target);
      if (t == null)
      {
         return new Token(Token.ERROR);
      }
      else
      {
         return t;
      }
   }

   private void skipBlanks()
   {
      while (ch == ' ' || ch == Chario.EL || ch == Chario.TAB)
      {
         ch = chario.getChar();
      }
   }

   private void getIdentifierOrKeyword()
   {
      int i = 0;
      int barCount = 0;
      StringBuffer id = new StringBuffer(MAX_KEY_SPELLING);
      tokenBuffer = new StringBuffer();
      token = new Token(Token.IDENTIFIER);
      if (ch == '_')
         chario.putError("illegal leading '_'");
      do
      {
         ch = Character.toUpperCase(ch);
         i++;
         tokenBuffer.append(ch);
         if (i <= MAX_KEY_SPELLING)
            id.append(ch);
         if (ch == '_'){
            ch = chario.getChar();
            if (ch == '_')
               barCount++;
            if (! Character.isLetterOrDigit(ch) && ch != '_')
               chario.putError("letter or digit expected after '_'");
         }
         else
            ch = chario.getChar();
      }
      while (Character.isLetterOrDigit(ch) || ch == '_');
      
      if (barCount > 0)
         chario.putError("letter or digit expected after '_'");
      if (i <= MAX_KEY_SPELLING)
      {
         token = findToken(keywords, id.toString());
         if (token.code == Token.ERROR)
            token.code = Token.IDENTIFIER;
      }
      if (token.code == Token.IDENTIFIER)
         token.string = tokenBuffer.toString();
   }

   private void getInteger()
   {
      int base = 16;
      
      token = new Token(Token.NUMBER);
      getBasedInteger(10);
      if (ch == '#')
      {
         base = token.integer;
         if (base < 2 || base > 16)
         {
            chario.putError("base must be between 2 and 16"); 
            base = 16;
         }
         ch = chario.getChar();
         if (! Character.isLetterOrDigit(ch))
            chario.putError("letter or digit expected after '#'");
         getBasedInteger(base);
         if (ch == '#')
         {
            ch = chario.getChar();
         }
         else
         {
            chario.putError("'#' expected");
         }
      }
   }

   private void getBasedInteger(int base)
   {
      int barCount = 0;
      token.integer = 0;
      while (Character.isLetterOrDigit(ch) || ch == '_')
      {
         if (ch == '_')
         {
            ch = chario.getChar();
            if (ch == '_')
               barCount++;
            if (! Character.isLetterOrDigit(ch) && ch != '_')
               chario.putError("letter or digit expected after '_'");
         }
         else
         {
            token.integer = base * token.integer + charToInt(ch, base);
            ch = chario.getChar();
         }
      }
      if (barCount > 0)
      {
         chario.putError("letter or digit expected after '_'");
      }
   }

   private int charToInt(char ch, int base){
      int digit = Character.digit(ch, base);
      if (digit == -1){
         chario.putError("digit not in range of base");
         digit = 0;
      }
      return digit;
   }
      
   private void getCharacter()
   {
      token = new Token(Token.IDENTIFIER); 
      ch = chario.getChar();
      if (ch == Chario.EL)
      {
         chario.putError("''' expected");
         tokenBuffer.append(' ');
         ch = chario.getChar();
      }
      else
      {
         token.string = "" + ch;
         ch = chario.getChar();
         if (ch == '\'')
            ch = chario.getChar();
         else
            chario.putError("''' expected");
      }
   }
      
   private void getDoubleOp()
   {
      tokenBuffer = new StringBuffer(2);
      tokenBuffer.append(ch);
      ch = chario.getChar();
      tokenBuffer.append(ch);
      token = findToken(doubleOps, tokenBuffer.toString());
      if (token.code != Token.ERROR)
         ch = chario.getChar();
   }

   private void getSingleOp()
   {
      token = findToken(singleOps, "" + tokenBuffer.charAt(0));
   }

   
   public Token nextToken()
   {
      do
      {
         skipBlanks();
         if (Character.isLetter(ch) || ch == '_')
            getIdentifierOrKeyword();
         else if (Character.isDigit(ch))
            getInteger();
         else if (ch == '\'')
            getCharacter();
         else if (ch == Chario.EF)
            token = new Token(Token.EOF);
         else
         {
            getDoubleOp();
            if (token.code == Token.ERROR)
            {
               getSingleOp();
               if (token.code == Token.ERROR)
                  chario.putError("unrecognized symbol");
            }
         }
      }
      while (token.code == Token.ERROR);
      return token;
   }

}