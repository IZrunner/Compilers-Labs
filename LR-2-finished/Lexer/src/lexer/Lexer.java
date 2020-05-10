package lexer;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Lexer {
  public static enum TokenType {
    // Token types cannot have underscores
    //WHITESPACE("[\t\f\r\n]+")
    //NUMBER("[-+]?[0-9]*\.?[0-9]+") //floating point + integer
    //VARIABLE("^[_$a-z][\\w$]*$")
    //variable("^[_a-z]\\w*$")
    //KEYWORD("\\b(?:INSERT|UPDATE|DELETE|CREATE TABLE|FROM|UNION|GROUP BY|SELECT|WHERE)\\b") //buff
    //KEYWORD("SELECT\s.*FROM\s.*WHERE\s.*")
    NUMBER("[-+]?[0-9]*\\.?[0-9]+"), VARIABLE("[a-z$_][a-zA-Z0-9$_]*"), BINARYOP("[*|/|+|-]"),
    KEYWORD("\\b(?:INSERT|UPDATE|DELETE|CREATE TABLE|FROM|UNION|GROUP BY|SELECT|WHERE)\\b"), 
    COMPARISONOP("(=|>|<|>=|<|<=|<>)\\s"), WHITESPACE("[\t\f\r\n]+"), PUNCTUATION("[.|;|!]");

    public final String pattern;

    private TokenType(String pattern) {
      this.pattern = pattern;
    }
  }

  public static class Token {
    public TokenType type;
    public String data;
    public String inputScript;
    public static int counterStartPos = 1;
    public static int counterPos = 1;

    public Token(TokenType type, String data) {
      this.type = type;
      this.data = data;
    }

    @Override
    public String toString() {  
      return String.format("(%s - \'%s\' - START %d - LENGTH - %d - POSITION %d)", type.name(), data, counterStartPos, data.replace(" ", "").length(), counterPos);
    }
  }

  public static ArrayList<Token> lex(String input) {
    // The tokens to return
    ArrayList<Token> tokens = new ArrayList<Token>();

    // Lexer logic begins here
    StringBuffer tokenPatternsBuffer = new StringBuffer();
    for (TokenType tokenType : TokenType.values())
      tokenPatternsBuffer.append(String.format("|(?<%s>%s)", tokenType.name(), tokenType.pattern));
    Pattern tokenPatterns = Pattern.compile(new String(tokenPatternsBuffer.substring(1)));

    // Begin matching tokens
    Matcher matcher = tokenPatterns.matcher(input);
    while (matcher.find()) {
      if (matcher.group(TokenType.NUMBER.name()) != null) {
        tokens.add(new Token(TokenType.NUMBER, matcher.group(TokenType.NUMBER.name())));
        continue;
      } 
      else if (matcher.group(TokenType.BINARYOP.name()) != null) {
        tokens.add(new Token(TokenType.BINARYOP, matcher.group(TokenType.BINARYOP.name())));
        continue;
      } 
      else if (matcher.group(TokenType.WHITESPACE.name()) != null) {
        tokens.add(new Token(TokenType.WHITESPACE, matcher.group(TokenType.WHITESPACE.name())));
        continue;
      } 
      else if (matcher.group(TokenType.KEYWORD.name()) != null) {
        tokens.add(new Token(TokenType.KEYWORD, matcher.group(TokenType.KEYWORD.name())));
        continue;
      } 
      else if (matcher.group(TokenType.VARIABLE.name()) != null) {
        tokens.add(new Token(TokenType.VARIABLE, matcher.group(TokenType.VARIABLE.name())));
        continue;
      }    
      else if (matcher.group(TokenType.COMPARISONOP.name()) != null) {
        tokens.add(new Token(TokenType.COMPARISONOP, matcher.group(TokenType.COMPARISONOP.name())));
        continue; 
      }
      else if (matcher.group(TokenType.PUNCTUATION.name()) != null) {
        tokens.add(new Token(TokenType.PUNCTUATION, matcher.group(TokenType.PUNCTUATION.name())));
        continue; 
      }   
    }
    return tokens;
  }

  
  public static void main(String[] args) {
    String input = "SELECT table WHERE column <= 33 - 22.556 ;";
    
    // Create tokens and print them
    ArrayList<Token> tokens = lex(input);
    for (Token token : tokens){
      System.out.println(token);
      Token.counterStartPos += token.data.length() + 1;
      Token.counterPos++;
    }
    }
    
}