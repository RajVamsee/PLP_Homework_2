package edu.ufl.cise.plpfa21.assignment2;
import java.util.ArrayList;

import edu.ufl.cise.plpfa21.assignment1.*;
import edu.ufl.cise.plpfa21.assignment1.PLPTokenKinds.Kind;

public class Parser implements IPLPParser {

	String s;
	int x=0;
	int temp=0;
	IPLPLexer lexer;
	@SuppressWarnings("rawtypes")
	ArrayList myList=new ArrayList();
	IPLPToken token;
	
	public Parser(String input) {
		s=input;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void parse() throws SyntaxException, LexicalException {
		
		lexer = CompilerComponentFactory.getLexer(s);
		token = lexer.nextToken();
		myList.add(token.getKind());
		myList.add(token.getLine());
		myList.add(token.getCharPositionInLine());
		
		
		while(token.getKind()!=Kind.EOF){
			token = lexer.nextToken();
			myList.add(token.getKind());
			myList.add(token.getLine());
			myList.add(token.getCharPositionInLine());
		}
		
		myDeclaration();
		if(myList.get(x)!=Kind.EOF) {
			throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
		}
	}
	
	public void myDeclaration() throws SyntaxException {
		if(myList.get(x)==Kind.KW_VAR) {
			x+=3;
			temp=x;
			//NameDef Required
			myNameDef();
			if(temp==x) {
				throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
			}
			if(myList.get(x)==Kind.ASSIGN) {
					x+=3;
					temp=x;
					//Expression required
					myExpression();
					if(temp==x) {
						throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
					}
					if(myList.get(x)==Kind.SEMI) {
						x+=3;
					}
					else {
						throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
					}
			}
			else if(myList.get(x)==Kind.SEMI){
				x+=3;
			}
			else {
				throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
			}
		}
		else if(myList.get(x)==Kind.KW_VAL) {
			x+=3;
			temp=x;
			//NameDef Required
			myNameDef();
			if(temp==x) {
				throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
			}
			if(myList.get(x)==Kind.ASSIGN) {
				x+=3;
				temp=x;
				//Expression required
				myExpression();
				if(temp==x) {
					throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
				}
				if(myList.get(x)==Kind.SEMI) {
					x+=3;
				}
				else {
					throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
				}
			}
			else {
				throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
			}
		}
		else if(myList.get(x)==Kind.KW_FUN){
			temp=x;
			myFunction();
			if(temp==x) {
				throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
			}
		}
		else {
			return;
		}
	}
	
	public void myFunction() throws SyntaxException {
		if(myList.get(x)==Kind.KW_FUN) {
			x+=3;
			if(myList.get(x)==Kind.IDENTIFIER) {
				x+=3;
				if(myList.get(x)==Kind.LPAREN) {
					x+=3;
					if(myList.get(x)==Kind.RPAREN) {
						x+=3;
					}
					else{
						temp=x;
						//NameDef Required
						myNameDef();
						if(temp==x) {
							throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
						}
						while(myList.get(x)==Kind.COMMA) {
							x+=3;
							temp=x;
							//NameDef Required
							myNameDef();
							if(temp==x) {
								throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
								
							}
						}
						if(myList.get(x)==Kind.RPAREN) {
							x+=3;
						}
						else {
							throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
						}
					}
					if(myList.get(x)==Kind.COLON) {
						x+=3;
						temp=x;
						//Type needed
						myType();
						if(temp==x) {
							throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
						}
						if(myList.get(x)==Kind.KW_DO) {
							x+=3;
							temp=x;
							//Block needed
							myBlock();
							if(temp==x) {
								throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
							}
							if(myList.get(x)==Kind.KW_END) {
								x+=3;
							}
							else {
								throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
							}
						}
						else {
							throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
						}
					}
					else {
						if(myList.get(x)==Kind.KW_DO) {
							x+=3;
							temp=x;
							//Block needed
							myBlock();
							if(temp==x) {
								throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
							}
							if(myList.get(x)==Kind.KW_END) {
								x+=3;
							}
							else {
								throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
							}
						}
						else {
							throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
						}

					}
				}
				else {
					throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
				}
			}
			else {
				throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
			}
		}
		else {
			throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
		}
	}
	
	public void myBlock() throws SyntaxException{
		myStatement();
		while(myList.get(x)==Kind.KW_LET||myList.get(x)==Kind.KW_SWITCH||myList.get(x)==Kind.KW_IF||myList.get(x)==Kind.KW_WHILE||myList.get(x)==Kind.KW_RETURN||myList.get(x)==Kind.KW_NIL||myList.get(x)==Kind.KW_TRUE||myList.get(x)==Kind.KW_FALSE||myList.get(x)==Kind.INT_LITERAL||myList.get(x)==Kind.STRING_LITERAL||myList.get(x)==Kind.IDENTIFIER) {
			myStatement();
		}
	}
	
	public void myNameDef() throws SyntaxException{
		if(myList.get(x)==Kind.IDENTIFIER) {
			x+=3;
			if(myList.get(x)==Kind.COLON) {
				x+=3;
				temp=x;
				//Type needed
				myType();
				if(temp==x) {
					throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
				}
			}
		}
		else {
			throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
		}
	}
	
	public void myStatement() throws SyntaxException{
		if(myList.get(x)==Kind.KW_LET) {
			x+=3;
			temp=x;
			//NameDef required
			myNameDef();
			if(temp==x) {
				throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
			}
			if(myList.get(x)==Kind.ASSIGN) {
				x+=3;
				temp=x;
				//Expression required
				myExpression();
				if(temp==x) {
					throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
				}
				if(myList.get(x)==Kind.SEMI) {
					x+=3;
				}
				else {
					throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
				}
			}
			else if(myList.get(x)==Kind.SEMI) {
				x+=3;
			}
			else {
				throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
			}
		}
		else if(myList.get(x)==Kind.KW_SWITCH) {
			x+=3;
			temp=x;
			//Expression required
			myExpression();
			if(temp==x) {
				throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
			}
			if(myList.get(x)==Kind.KW_CASE) {
				while(myList.get(x)==Kind.KW_CASE) {
					x+=3;
					temp=x;
					//Expression required
					myExpression();
					if(temp==x) {
						throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
					}
					if(myList.get(x)==Kind.COLON) {
						x+=3;
						//Block needed
						temp=x;
						myBlock();
						if(temp==x) {
							throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
						}
					}
					else {
						throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
					}
				}
				if(myList.get(x)==Kind.KW_DEFAULT) {
					x+=3;
					temp=x;
					//Block needed
					myBlock();
					if(temp==x) {
						throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
					}
					if(myList.get(x)==Kind.KW_END) {
						x+=3;
					}
					else {
						throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
					}
				}
				else {
					throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
				}
			}
			else if(myList.get(x)==Kind.KW_DEFAULT) {
				x+=3;
				temp=x;
				//Block needed
				myBlock();
				if(temp==x) {
					throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
				}
				if(myList.get(x)==Kind.KW_END) {
					x+=3;
				}
				else {
					throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
				}
			}
			else {
				throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
			}
		}
		else if(myList.get(x)==Kind.KW_IF){
			x+=3;
			temp=x;
			//Expression required
			myExpression();
			if(temp==x) {
				throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
			}
			if(myList.get(x)==Kind.KW_DO) {
				x+=3;
				temp=x;
				//Block needed
				myBlock();
				if(temp==x) {
					throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
				}
				if(myList.get(x)==Kind.KW_END) {
					x+=3;
				}
				else {
					throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
				}
			}
			else {
				throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
			}
		}
		else if(myList.get(x)==Kind.KW_WHILE) {
			x+=3;
			temp=x;
			//Expression required
			myExpression();
			if(temp==x) {
				throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
			}
			if(myList.get(x)==Kind.KW_DO) {
				x+=3;
				temp=x;
				//Block needed
				myBlock();
				if(temp==x) {
					throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
				}
				if(myList.get(x)==Kind.KW_END) {
					x+=3;
				}
				else {
					throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
				}
			}
			else {
				throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
			}
		}
		else if(myList.get(x)==Kind.KW_RETURN) {
			x+=3;
			temp=x;
			//Expression required
			myExpression();
			if(temp==x) {
				throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
			}
			if(myList.get(x)==Kind.SEMI) {
				x+=3;
			}
			else {
				throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
			}
		}
		else {
			temp=x;
			//Expression required
			myExpression();
			if(temp==x) {
				throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
			}
			if(myList.get(x)==Kind.ASSIGN) {
				x+=3;
				temp=x;
				//Expression required
				myExpression();
				if(temp==x) {
					throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
				}
				if(myList.get(x)==Kind.SEMI) {
					x+=3;
				}
				else {
					throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
				}
			}
			else if(myList.get(x)==Kind.SEMI) {
				x+=3;
			}
			else {
				throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
			}
		}
	}
	
	public void myExpression() throws SyntaxException{
		temp=x;
		myLogicalExpression();
		if(temp==x) {
			throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
		}
	}
	
	public void myLogicalExpression() throws SyntaxException {
		temp=x;
		myComparisonExpression();
		if(temp==x) {
			throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
		}
		while(myList.get(x)==Kind.AND||myList.get(x)==Kind.OR) {
			x+=3;
			temp=x;
			myComparisonExpression();
			if(temp==x) {
				throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
			}
		}
	}
	
	public void myComparisonExpression() throws SyntaxException{
		temp=x;
		myAdditiveExpression();
		if(temp==x) {
			throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
		}
		while(myList.get(x)==Kind.GT||myList.get(x)==Kind.LT||myList.get(x)==Kind.EQUALS||myList.get(x)==Kind.NOT_EQUALS) {
			x+=3;
			temp=x;
			myAdditiveExpression();
			if(temp==x) {
				throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
			}
		}
	}
	
	public void myAdditiveExpression() throws SyntaxException{
		temp=x;
		myMultiplicativeExpression();
		if(temp==x) {
			throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
		}
		while(myList.get(x)==Kind.PLUS||myList.get(x)==Kind.MINUS) {
			x+=3;
			temp=x;
			myMultiplicativeExpression();
			if(temp==x) {
				throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
			}
		}
	}
	
	public void myMultiplicativeExpression() throws SyntaxException{
		temp=x;
		myUnaryExpression();
		if(temp==x) {
			throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
		}
		while(myList.get(x)==Kind.DIV||myList.get(x)==Kind.TIMES) {
			x+=3;
			temp=x;
			myUnaryExpression();
			if(temp==x) {
				throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
			}
		}
	}
	
	public void myUnaryExpression() throws SyntaxException {
		if(myList.get(x)==Kind.BANG||myList.get(x)==Kind.MINUS) {
			x+=3;
		}
		temp=x;
		myPrimaryExpression();
		if(temp==x) {
			throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
		}
	}
	
	public void myPrimaryExpression() throws SyntaxException{
		if(myList.get(x)==Kind.KW_NIL||myList.get(x)==Kind.KW_TRUE||myList.get(x)==Kind.KW_FALSE||myList.get(x)==Kind.INT_LITERAL||myList.get(x)==Kind.STRING_LITERAL) {
			x+=3;
		}
		else if(myList.get(x)==Kind.LPAREN) {
			x+=3;
			temp=x;
			//Expression required
			myExpression();
			if(temp==x) {
				throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
			}
			if(myList.get(x)==Kind.RPAREN) {
				x+=3;
			}
			else {
				throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
			}
		}
		else if(myList.get(x)==Kind.IDENTIFIER) {
			x+=3;
			if(myList.get(x)==Kind.LPAREN) {
				x+=3;
				if(myList.get(x)==Kind.RPAREN) {
					x+=3;
				}
				else {
					myExpression();
					while(myList.get(x)==Kind.COMMA) {
						x+=3;
						temp=x;
						//Expression required
						myExpression();
						if(temp==x) {
							throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
						}
					}
					if(myList.get(x)==Kind.RPAREN) {
						x+=3;
					}
					else {
						throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
					}
				}
			}
			else if(myList.get(x)==Kind.LSQUARE) {
				x+=3;
				temp=x;
				//Expression required
				myExpression();
				if(temp==x) {
					throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
				}
				if(myList.get(x)==Kind.RSQUARE) {
					x+=3;
				}
				else {
					throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
				}
			}
			else {
				throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
			}
		}
		else {
			throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
		}
	}
	
	public void myType() throws SyntaxException{
		if(myList.get(x)==Kind.KW_INT) {
			x+=3;
		}
		else if(myList.get(x)==Kind.KW_STRING) {
			x+=3;
		}
		else if(myList.get(x)==Kind.KW_BOOLEAN) {
			x+=3;
		}
		else if(myList.get(x)==Kind.KW_LIST) {
			x+=3;
			if(myList.get(x)==Kind.LSQUARE) {
				x+=3;
				if(myList.get(x)==Kind.RSQUARE) {
					x+=3;
				}
				else {
					temp=x;
					myType();
					//Type needed
					if(temp==x) {
						throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
					}
					if(myList.get(x)==Kind.RSQUARE) {
						x+=3;
					}
					else {
						throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
					}
				}
			}
			else {
				throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
			}
		}
		else {
			throw new SyntaxException(s,(Integer)myList.get(x+1),(Integer)myList.get(x+2));
		}
	}
	
	
}
