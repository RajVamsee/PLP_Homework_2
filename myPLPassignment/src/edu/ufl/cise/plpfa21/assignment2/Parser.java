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
	@SuppressWarnings("rawtypes")
	ArrayList myLineList=new ArrayList();
	@SuppressWarnings("rawtypes")
	ArrayList myPositionList=new ArrayList();
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
		myLineList.add(token.getLine());
		myPositionList.add(token.getCharPositionInLine());
		
		
		while(token.getKind()!=Kind.EOF){
			token = lexer.nextToken();
			myList.add(token.getKind());
			myLineList.add(token.getLine());
			myPositionList.add(token.getCharPositionInLine());
		}
		
		myDeclaration();
		if(myList.get(x)!=Kind.EOF) {
			throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
		}
	}
	
	public void myDeclaration() throws SyntaxException {
		if(myList.get(x)==Kind.KW_VAR) {
			x+=1;
			temp=x;
			//NameDef Required
			myNameDef();
			if(temp==x) {
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
			}
			if(myList.get(x)==Kind.ASSIGN) {
					x+=1;
					temp=x;
					//Expression required
					myExpression();
					if(temp==x) {
						throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
					}
					if(myList.get(x)==Kind.SEMI) {
						x+=1;
					}
					else {	
						throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
					}
			}
			else if(myList.get(x)==Kind.SEMI){
				x+=1;
			}
			else {
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
			}
		}
		else if(myList.get(x)==Kind.KW_VAL) {
			x+=1;
			temp=x;
			//NameDef Required
			myNameDef();
			if(temp==x) {
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
			}
			if(myList.get(x)==Kind.ASSIGN) {
				x+=1;
				temp=x;
				//Expression required
				myExpression();
				if(temp==x) {
					throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
				}
				if(myList.get(x)==Kind.SEMI) {
					x+=1;
				}
				else {		
					throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
				}
			}
			else {
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
			}
		}
		else if(myList.get(x)==Kind.KW_FUN){
			temp=x;
			myFunction();
			if(temp==x) {
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
			}
		}
		else {
			return;
		}
	}
	
	public void myFunction() throws SyntaxException {
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
						temp=x;
						//NameDef Required
						myNameDef();
						if(temp==x) {
							throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
						}
						while(myList.get(x)==Kind.COMMA) {
							x+=1;
							temp=x;
							//NameDef Required
							myNameDef();
							if(temp==x) {
								throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
							}
						}
						if(myList.get(x)==Kind.RPAREN) {
							x+=1;
						}
						else {		
							throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
						}
					}
					if(myList.get(x)==Kind.COLON) {
						x+=1;
						temp=x;
						//Type needed
						myType();
						if(temp==x) {							
							throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
						}
						if(myList.get(x)==Kind.KW_DO) {
							x+=1;
							temp=x;
							//Block needed
							myBlock();
							if(temp==x) {		
								throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
							}
							if(myList.get(x)==Kind.KW_END) {
								x+=1;
							}
							else {
								throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
							}
						}
						else {							
							throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
						}
					}
					else {
						if(myList.get(x)==Kind.KW_DO) {
							x+=1;
							temp=x;
							//Block needed
							myBlock();
							if(temp==x) {	
								throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
							}
							if(myList.get(x)==Kind.KW_END) {
								x+=1;
							}
							else {					
								throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
							}
						}
						else {					
							throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
						}

					}
				}
				else {					
					throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
				}
			}
			else {				
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
			}
		}
		else {			
			throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
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
			x+=1;
			if(myList.get(x)==Kind.COLON) {
				x+=1;
				temp=x;
				//Type needed
				myType();
				if(temp==x) {		
					throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
				}
			}
		}
		else {	
			throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
		}
	}
	
	public void myStatement() throws SyntaxException{
		if(myList.get(x)==Kind.KW_LET) {
			x+=1;
			temp=x;
			//NameDef required
			myNameDef();
			if(temp==x) {	
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
			}
			if(myList.get(x)==Kind.ASSIGN) {
				x+=1;
				temp=x;
				//Expression required
				myExpression();
				if(temp==x) {		
					throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
				}
				if(myList.get(x)==Kind.SEMI) {
					x+=1;
				}
				else {
					throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
				}
			}
			else if(myList.get(x)==Kind.SEMI) {
				x+=1;
			}
			else {
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
			}
		}
		else if(myList.get(x)==Kind.KW_SWITCH) {
			x+=1;
			temp=x;
			//Expression required
			myExpression();
			if(temp==x) {		
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
			}
			if(myList.get(x)==Kind.KW_CASE) {
				while(myList.get(x)==Kind.KW_CASE) {
					x+=1;
					temp=x;
					//Expression required
					myExpression();
					if(temp==x) {
						throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
					}
					if(myList.get(x)==Kind.COLON) {
						x+=1;
						//Block needed
						temp=x;
						myBlock();
						if(temp==x) {	
							throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
						}
					}
					else {	
						throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
					}
				}
				if(myList.get(x)==Kind.KW_DEFAULT) {
					x+=1;
					temp=x;
					//Block needed
					myBlock();
					if(temp==x) {			
						throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
					}
					if(myList.get(x)==Kind.KW_END) {
						x+=1;
					}
					else {	
						throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
					}
				}
				else {			
					throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
				}
			}
			else if(myList.get(x)==Kind.KW_DEFAULT) {
				x+=1;
				temp=x;
				//Block needed
				myBlock();
				if(temp==x) {	
					throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
				}
				if(myList.get(x)==Kind.KW_END) {
					x+=1;
				}
				else {	
					throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
				}
			}
			else {			
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
			}
		}
		else if(myList.get(x)==Kind.KW_IF){
			x+=1;
			temp=x;
			//Expression required
			myExpression();
			if(temp==x) {	
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
			}
			if(myList.get(x)==Kind.KW_DO) {
				x+=1;
				temp=x;
				//Block needed
				myBlock();
				if(temp==x) {
					throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
				}
				if(myList.get(x)==Kind.KW_END) {
					x+=1;
				}
				else {	
					throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
				}
			}
			else {
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
			}
		}
		else if(myList.get(x)==Kind.KW_WHILE) {
			x+=1;
			temp=x;
			//Expression required
			myExpression();
			if(temp==x) {
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
			}
			if(myList.get(x)==Kind.KW_DO) {
				x+=1;
				temp=x;
				//Block needed
				myBlock();
				if(temp==x) {		
					throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
				}
				if(myList.get(x)==Kind.KW_END) {
					x+=1;
				}
				else {
					throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
				}
			}
			else {
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
			}
		}
		else if(myList.get(x)==Kind.KW_RETURN) {
			x+=1;
			temp=x;
			//Expression required
			myExpression();
			if(temp==x) {
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
			}
			if(myList.get(x)==Kind.SEMI) {
				x+=1;
			}
			else {
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
			}
		}
		else {
			temp=x;
			//Expression required
			myExpression();
			if(temp==x) {
				
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
			}
			if(myList.get(x)==Kind.ASSIGN) {
				x+=1;
				temp=x;
				//Expression required
				myExpression();
				if(temp==x) {
					
					throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
				}
				if(myList.get(x)==Kind.SEMI) {
					x+=1;
				}
				else {
					
					throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
				}
			}
			else if(myList.get(x)==Kind.SEMI) {
				x+=1;
			}
			else {
				
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
			}
		}
	}
	
	public void myExpression() throws SyntaxException{
		temp=x;
		myLogicalExpression();
		if(temp==x) {
			
			throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
		}
	}
	
	public void myLogicalExpression() throws SyntaxException {
		temp=x;
		myComparisonExpression();
		if(temp==x) {
			
			throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
		}
		while(myList.get(x)==Kind.AND||myList.get(x)==Kind.OR) {
			x+=1;
			temp=x;
			myComparisonExpression();
			if(temp==x) {
				
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
			}
		}
	}
	
	public void myComparisonExpression() throws SyntaxException{
		temp=x;
		myAdditiveExpression();
		if(temp==x) {
			
			throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
		}
		while(myList.get(x)==Kind.GT||myList.get(x)==Kind.LT||myList.get(x)==Kind.EQUALS||myList.get(x)==Kind.NOT_EQUALS) {
			x+=1;
			temp=x;
			myAdditiveExpression();
			if(temp==x) {
				
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
			}
		}
	}
	
	public void myAdditiveExpression() throws SyntaxException{
		temp=x;
		myMultiplicativeExpression();
		if(temp==x) {
			
			throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
		}
		while(myList.get(x)==Kind.PLUS||myList.get(x)==Kind.MINUS) {
			x+=1;
			temp=x;
			myMultiplicativeExpression();
			if(temp==x) {
				
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
			}
		}
	}
	
	public void myMultiplicativeExpression() throws SyntaxException{
		temp=x;
		myUnaryExpression();
		if(temp==x) {
			
			throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
		}
		while(myList.get(x)==Kind.DIV||myList.get(x)==Kind.TIMES) {
			x+=1;
			temp=x;
			myUnaryExpression();
			if(temp==x) {
				
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
			}
		}
	}
	
	public void myUnaryExpression() throws SyntaxException {
		if(myList.get(x)==Kind.BANG||myList.get(x)==Kind.MINUS) {
			x+=1;
		}
		temp=x;
		myPrimaryExpression();
		if(temp==x) {
			
			throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
		}
	}
	
	public void myPrimaryExpression() throws SyntaxException{
		if(myList.get(x)==Kind.KW_NIL||myList.get(x)==Kind.KW_TRUE||myList.get(x)==Kind.KW_FALSE||myList.get(x)==Kind.INT_LITERAL||myList.get(x)==Kind.STRING_LITERAL) {
			x+=1;
		}
		else if(myList.get(x)==Kind.LPAREN) {
			x+=1;
			temp=x;
			//Expression required
			myExpression();
			if(temp==x) {
				
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
			}
			if(myList.get(x)==Kind.RPAREN) {
				x+=1;
			}
			else {
				
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
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
					temp=x;
					myExpression();
					if(temp!=x) {
					while(myList.get(x)==Kind.COMMA) {
						x+=1;
						temp=x;
						//Expression required
						myExpression();
						if(temp==x) {
							
							throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
						}
					}
					}
					if(myList.get(x)==Kind.RPAREN) {
						x+=1;
					}
					else {
						
						throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
					}
				}
			}
			else if(myList.get(x)==Kind.LSQUARE) {
				x+=1;
				temp=x;
				//Expression required
				myExpression();
				if(temp==x) {
					
					throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
				}
				if(myList.get(x)==Kind.RSQUARE) {
					x+=1;
				}
				else {
					
					throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
				}
			}
		}
		else {
			
			throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
		}
	}
	
	public void myType() throws SyntaxException{
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
					temp=x;
					myType();
					//Type needed
					if(temp==x) {
						
						throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
					}
					if(myList.get(x)==Kind.RSQUARE) {
						x+=1;
					}
					else {
						
						throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
					}
				}
			}
			else {
				
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
			}
		}
		else {
			
			throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
		}
	}
	
	
}
