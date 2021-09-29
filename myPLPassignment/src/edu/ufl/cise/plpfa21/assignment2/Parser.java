package edu.ufl.cise.plpfa21.assignment2;
import java.util.ArrayList;

import edu.ufl.cise.plpfa21.assignment1.*;
import edu.ufl.cise.plpfa21.assignment1.PLPTokenKinds.Kind;

public class Parser implements IPLPParser {

	String s;
	int x=0;
	IPLPLexer lexer;
	ArrayList<Kind> myList=new ArrayList<Kind>();
	IPLPToken token;
	
	
	public Parser(String input) {
		s=input;
	}

	@Override
	public void parse() throws SyntaxException, LexicalException {
		
		lexer = CompilerComponentFactory.getLexer(s);
		token = lexer.nextToken();
		myList.add(token.getKind());
		
		
		while(token.getKind()!=Kind.EOF){
			token = lexer.nextToken();
			myList.add(token.getKind());
		}
		System.out.println(myList.get(x)+"  Start of program");
		myDeclaration();
		System.out.println(myList.get(x)+"  End of program");
		
	}
	
	public void myDeclaration() {
		if(myList.get(x)==Kind.KW_VAR) {
			x+=1;
			myNameDef();
			if(myList.get(x)==Kind.ASSIGN) {
					x+=1;
					myExpression();
					if(myList.get(x)==Kind.SEMI) {
						x+=1;
					}
			}
			else if(myList.get(x)==Kind.SEMI){
				x+=1;
			}
		}
		else if(myList.get(x)==Kind.KW_VAL) {
			x+=1;
			myNameDef();
			if(myList.get(x)==Kind.ASSIGN) {
				x+=1;
				myExpression();
				if(myList.get(x)==Kind.SEMI) {
					x+=1;
				}
			}		
		}
		else if(myList.get(x)==Kind.KW_FUN){
			myFunction();
		}
		else {
			return;
		}
	}
	
	public void myFunction() {
		if(myList.get(x)==Kind.KW_FUN) {
			x+=1;
			if(myList.get(x)==Kind.IDENTIFIER) {
				x+=1;
				if(myList.get(x)==Kind.LPAREN) {
					x+=1;
					if(myList.get(x)==Kind.RPAREN) {
						x+=1;
					}
					else{
						myNameDef();
						while(myList.get(x)==Kind.COMMA) {
							x+=1;
							myNameDef();
						}
						if(myList.get(x)==Kind.RPAREN) {
							x+=1;
						}
					}
					if(myList.get(x)==Kind.COLON) {
						x+=1;
						myType();
						if(myList.get(x)==Kind.KW_DO) {
							x+=1;
							myBlock();
							if(myList.get(x)==Kind.KW_END) {
								x+=1;
							}
						}
					}
					else {
						if(myList.get(x)==Kind.KW_DO) {
							x+=1;
							myBlock();
							if(myList.get(x)==Kind.KW_END) {
								x+=1;
							}
						}

					}
				}
			}
		}
		else {
			return;
		}
	}
	
	public void myBlock() {
		myStatement();
		while(myList.get(x)==Kind.KW_LET||myList.get(x)==Kind.KW_SWITCH||myList.get(x)==Kind.KW_IF||myList.get(x)==Kind.KW_WHILE||myList.get(x)==Kind.KW_RETURN||myList.get(x)==Kind.KW_NIL||myList.get(x)==Kind.KW_TRUE||myList.get(x)==Kind.KW_FALSE||myList.get(x)==Kind.INT_LITERAL||myList.get(x)==Kind.STRING_LITERAL||myList.get(x)==Kind.IDENTIFIER) {
			myStatement();
		}
	}
	
	public void myNameDef() {
		if(myList.get(x)==Kind.IDENTIFIER) {
			x+=1;
			if(myList.get(x)==Kind.COLON) {
				x+=1;
				myType();
			}
		}
		else {
			return;
		}
	}
	
	public void myStatement() {
		if(myList.get(x)==Kind.KW_LET) {
			x+=1;
			myNameDef();
			if(myList.get(x)==Kind.ASSIGN) {
				x+=1;
				myExpression();
				if(myList.get(x)==Kind.SEMI) {
					x+=1;
				}
			}
			else if(myList.get(x)==Kind.SEMI) {
				x+=1;
			}
		}
		else if(myList.get(x)==Kind.KW_SWITCH) {
			x+=1;
			myExpression();
			if(myList.get(x)==Kind.KW_CASE) {
				while(myList.get(x)==Kind.KW_CASE) {
					x+=1;
					myExpression();
					if(myList.get(x)==Kind.COLON) {
						x+=1;
					}
					myBlock();
				}
				if(myList.get(x)==Kind.KW_DEFAULT) {
					x+=1;
					myBlock();
					if(myList.get(x)==Kind.KW_END) {
						x+=1;
					}
				}
			}
			else if(myList.get(x)==Kind.KW_DEFAULT) {
				x+=1;
				myBlock();
				if(myList.get(x)==Kind.KW_END) {
					x+=1;
				}
			}
		}
		else if(myList.get(x)==Kind.KW_IF){
			x+=1;
			myExpression();
			if(myList.get(x)==Kind.KW_DO) {
				x+=1;
				myBlock();
				if(myList.get(x)==Kind.KW_END) {
					x+=1;
				}
			}
		}
		else if(myList.get(x)==Kind.KW_WHILE) {
			x+=1;
			myExpression();
			if(myList.get(x)==Kind.KW_DO) {
				x+=1;
				myBlock();
				if(myList.get(x)==Kind.KW_END) {
					x+=1;
				}
			}
		}
		else if(myList.get(x)==Kind.KW_RETURN) {
			x+=1;
			myExpression();
		}
		else {
			myExpression();
			if(myList.get(x)==Kind.ASSIGN) {
				x+=1;
				myExpression();
				if(myList.get(x)==Kind.SEMI) {
					x+=1;
				}
			}
		}
	}
	
	public void myExpression() {
		myLogicalExpression();
	}
	
	public void myLogicalExpression() {
		myComparisonExpression();
		while(myList.get(x)==Kind.AND||myList.get(x)==Kind.OR) {
			x+=1;
			myComparisonExpression();
		}
	}
	
	public void myComparisonExpression() {
		myAdditiveExpression();
		while(myList.get(x)==Kind.GT||myList.get(x)==Kind.LT||myList.get(x)==Kind.EQUALS||myList.get(x)==Kind.NOT_EQUALS) {
			x+=1;
			myAdditiveExpression();
		}
	}
	
	public void myAdditiveExpression() {
		myMultiplicativeExpression();
		while(myList.get(x)==Kind.PLUS||myList.get(x)==Kind.MINUS) {
			x+=1;
			myMultiplicativeExpression();
		}
	}
	
	public void myMultiplicativeExpression() {
		myUnaryExpression();
		while(myList.get(x)==Kind.DIV||myList.get(x)==Kind.TIMES) {
			x+=1;
			myUnaryExpression();
		}
	}
	
	public void myUnaryExpression() {
		if(myList.get(x)==Kind.BANG||myList.get(x)==Kind.MINUS) {
			x+=1;
		}
		myPrimaryExpression();
	}
	
	public void myPrimaryExpression() {
		if(myList.get(x)==Kind.KW_NIL||myList.get(x)==Kind.KW_TRUE||myList.get(x)==Kind.KW_FALSE||myList.get(x)==Kind.INT_LITERAL||myList.get(x)==Kind.STRING_LITERAL) {
			x+=1;
		}
		else if(myList.get(x)==Kind.LPAREN) {
			x+=1;
			myExpression();
			if(myList.get(x)==Kind.RPAREN) {
				x+=1;
			}
		}
		else if(myList.get(x)==Kind.IDENTIFIER) {
			x+=1;
			if(myList.get(x)==Kind.LPAREN) {
				x+=1;
				if(myList.get(x)==Kind.RPAREN) {
					x+=1;
				}
				else {
					myExpression();
					while(myList.get(x)==Kind.COMMA) {
						x+=1;
						myExpression();
					}
					if(myList.get(x)==Kind.RPAREN) {
						x+=1;
					}
				}
			}
			else if(myList.get(x)==Kind.LSQUARE) {
				x+=1;
				myExpression();
				if(myList.get(x)==Kind.RSQUARE) {
					x+=1;
				}
			}
		}
	}
	
	public void myType() {
		if(myList.get(x)==Kind.KW_INT) {
			x+=1;
		}
		else if(myList.get(x)==Kind.KW_STRING) {
			x+=1;
		}
		else if(myList.get(x)==Kind.KW_BOOLEAN) {
			x+=1;
		}
		else if(myList.get(x)==Kind.KW_LIST) {
			x+=1;
			if(myList.get(x)==Kind.LSQUARE) {
				x+=1;
				if(myList.get(x)==Kind.RSQUARE) {
					x+=1;
				}
				else {
					myType();
					if(myList.get(x)==Kind.RSQUARE) {
						x+=1;
					}
				}
			}
		}
		else {
			return;
		}
	}
	
	
}
