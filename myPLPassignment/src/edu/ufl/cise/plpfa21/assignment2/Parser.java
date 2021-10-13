package edu.ufl.cise.plpfa21.assignment2;
import java.util.ArrayList;

import edu.ufl.cise.plpfa21.assignment3.ast.*;
import edu.ufl.cise.plpfa21.assignment3.ast.IType.TypeKind;
import edu.ufl.cise.plpfa21.assignment3.astimpl.*;

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
	@SuppressWarnings("rawtypes")
	ArrayList myNameList=new ArrayList();
	IPLPToken token;
	ArrayList<IDeclaration> myDeclarationList=new ArrayList<IDeclaration>();	
	public Parser(String input) {
		s=input;
	}
	@SuppressWarnings("unchecked")
	@Override
	public IASTNode parse() throws SyntaxException, LexicalException {
		
		lexer = CompilerComponentFactory.getLexer(s);
		token = lexer.nextToken();
		myList.add(token.getKind());
		myLineList.add(token.getLine());
		myPositionList.add(token.getCharPositionInLine());
		myNameList.add(token.getText());
		//myStringValues.add(token.getStringValue());
		//myIntValues.add(token.getIntValue());
		
		
		while(token.getKind()!=Kind.EOF){
			token = lexer.nextToken();
			myList.add(token.getKind());
			myLineList.add(token.getLine());
			myPositionList.add(token.getCharPositionInLine());
			myNameList.add(token.getText());
			//myStringValues.add(token.getStringValue());
			//myIntValues.add(token.getIntValue());
		}
		ArrayList<IDeclaration> myDec=myDeclaration();
		if(myList.get(x)!=Kind.EOF) {
			throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
		}
		return new Program__((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x)),myDec);
	}
	
	public  ArrayList<IDeclaration> myDeclaration() throws SyntaxException {
		if(myList.get(x)==Kind.KW_VAR) {
			myDeclarationList.add(myVar());
		}
		else if(myList.get(x)==Kind.KW_VAL) {
			myDeclarationList.add(myVal());
			
		}
		else if(myList.get(x)==Kind.KW_FUN){
			temp=x;
			myDeclarationList.add(myFunction());
			if(temp==x) {
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x)); 
			}
		}
		else {
			return null;
		}
		//return (IDeclaration) new Declaration__((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x));
		return myDeclarationList;
	}
	
	public IMutableGlobal myVar() throws SyntaxException{
		x+=1;
		temp=x;
		IExpression node2=null;
		//NameDef Required
		INameDef node1=myNameDef();
		if(temp==x) {
			throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
		}
		if(myList.get(x)==Kind.ASSIGN) {
				x+=1;
				temp=x;
				//Expression required
				node2=myExpression();
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
		return new MutableGlobal__((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x)),node1,node2);
		//return null;
	}
	
	public IImmutableGlobal myVal() throws SyntaxException{
		x+=1;
		temp=x;
		INameDef node3=null;
		IExpression node4=null;
		//NameDef Required
		node3=myNameDef();
		if(temp==x) {
			throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
		}
		if(myList.get(x)==Kind.ASSIGN) {
			x+=1;
			temp=x;
			//Expression required
			node4=myExpression();
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
		return new ImmutableGlobal__((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x)),node3,node4);
	}
	
	public IFunctionDeclaration myFunction() throws SyntaxException {
		IIdentifier name=null;
		ArrayList<INameDef> nameDefList=new ArrayList<INameDef>();
		IType resultType=null;
		IBlock block=null;
		if(myList.get(x)==Kind.KW_FUN) {
			x+=1;
			if(myList.get(x)==Kind.IDENTIFIER) {
				name=new Identifier__((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x)),String.valueOf(myNameList.get(x)));
				x+=1;
				if(myList.get(x)==Kind.LPAREN) {
					x+=1;
					if(myList.get(x)==Kind.RPAREN) {
						x+=1;
					}
					else{
						temp=x;
						//NameDef Required
						nameDefList.add(myNameDef());
						if(temp==x) {
							throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
						}
						while(myList.get(x)==Kind.COMMA) {
							x+=1;
							temp=x;
							//NameDef Required
							nameDefList.add(myNameDef());
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
						resultType=myType();
						if(temp==x) {							
							throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
						}
						if(myList.get(x)==Kind.KW_DO) {
							x+=1;
							temp=x;
							//Block needed
							block=myBlock();
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
							block=myBlock();
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
		return new FunctionDeclaration___((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x)),name,nameDefList,resultType,block);
	}
	
	public IBlock myBlock() throws SyntaxException{
		ArrayList<IStatement> statementList=new ArrayList<IStatement>();
		statementList.add(myStatement());
		while(myList.get(x)==Kind.KW_LET||myList.get(x)==Kind.KW_SWITCH||myList.get(x)==Kind.KW_IF||myList.get(x)==Kind.KW_WHILE||myList.get(x)==Kind.KW_RETURN||myList.get(x)==Kind.KW_NIL||myList.get(x)==Kind.KW_TRUE||myList.get(x)==Kind.KW_FALSE||myList.get(x)==Kind.INT_LITERAL||myList.get(x)==Kind.STRING_LITERAL||myList.get(x)==Kind.IDENTIFIER) {
			statementList.add(myStatement());
		}
		return new Block__((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x)),statementList);
	}
	
	public INameDef myNameDef() throws SyntaxException{
		IIdentifier node5=null;
		IType node6=null;
		if(myList.get(x)==Kind.IDENTIFIER) {
			node5=new Identifier__((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x)),String.valueOf(myNameList.get(x)));
			x+=1;
			if(myList.get(x)==Kind.COLON) {
				x+=1;
				temp=x;
				//Type needed
				node6=myType();
				if(temp==x) {		
					throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
				}
			}
		}
		else {	
			throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
		}
		return new NameDef__((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x)),node5,node6);
	}
	
	public IStatement myStatement() throws SyntaxException{
		IBlock block=null;
		IExpression exp=null;
		INameDef nameDef=null; 
		if(myList.get(x)==Kind.KW_LET) {
			x+=1;
			temp=x;
			//NameDef required
			nameDef=myNameDef();
			if(temp==x) {	
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
			}
			if(myList.get(x)==Kind.ASSIGN) {
				x+=1;
				temp=x;
				//Expression required
				exp=myExpression();
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
			else if(myList.get(x)==Kind.KW_DO) {
				x+=1;
				temp=x;
				//Block needed
				block=myBlock();
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
			return new LetStatement__((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x)),block,exp,nameDef);
		}
		else if(myList.get(x)==Kind.KW_SWITCH) {
			IExpression switchExpression=null;
			ArrayList<IExpression> branchExpression=new ArrayList<IExpression>();
			ArrayList<IBlock> blockList=new ArrayList<IBlock>();
			IBlock defaultBlock=null;
			x+=1;
			temp=x;
			//Expression required
			switchExpression=myExpression();
			if(temp==x) {		
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
			}
			if(myList.get(x)==Kind.KW_CASE) {
				while(myList.get(x)==Kind.KW_CASE) {
					x+=1;
					temp=x;
					//Expression required
					branchExpression.add(myExpression());
					if(temp==x) {
						throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
					}
					if(myList.get(x)==Kind.COLON) {
						x+=1;
						//Block needed
						temp=x;
						blockList.add(myBlock());
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
					defaultBlock=myBlock();
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
				defaultBlock=myBlock();
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
			return new SwitchStatement__((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x)),switchExpression,branchExpression,blockList,defaultBlock);
		}
		else if(myList.get(x)==Kind.KW_IF){
			x+=1;
			temp=x;
			IExpression ex=null;
			IBlock blockNode=null;
			//Expression required
			ex=myExpression();
			if(temp==x) {	
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
			}
			if(myList.get(x)==Kind.KW_DO) {
				x+=1;
				temp=x;
				//Block needed
				blockNode = myBlock();
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
			return new IfStatement__((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x)),ex,blockNode);
		}
		else if(myList.get(x)==Kind.KW_WHILE) {
			IExpression ie=null;
			IBlock ib=null;
			x+=1;
			temp=x;
			//Expression required
			ie=myExpression();
			if(temp==x) {
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
			}
			if(myList.get(x)==Kind.KW_DO) {
				x+=1;
				temp=x;
				//Block needed
				ib=myBlock();
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
			return new WhileStatement__((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x)),ie,ib);
		}
		else if(myList.get(x)==Kind.KW_RETURN) {
			IExpression myExp=null;
			x+=1;
			temp=x;
			//Expression required
			myExp=myExpression();
			if(temp==x) {
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
			}
			if(myList.get(x)==Kind.SEMI) {
				x+=1;
			}
			else {
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
			}
			return new ReturnStatement__((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x)),myExp);
		}
		else {
			temp=x;
			IExpression l=null;
			IExpression r=null;
			//Expression required
			l=myExpression();
			if(temp==x) {
				
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
			}
			if(myList.get(x)==Kind.ASSIGN) {
				x+=1;
				temp=x;
				//Expression required
				r=myExpression();
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
			return new AssignmentStatement__((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x)),l,r);
		}
	}
	
	public IExpression myExpression() throws SyntaxException{
		temp=x;
		IBinaryExpression node=myLogicalExpression();
		if(temp==x) {
			throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
		}
		return node;
	}
	
	public IBinaryExpression myLogicalExpression() throws SyntaxException {
		temp=x;
		IExpression right=null;
		Kind myLocalKind=null;
		IExpression left=myComparisonExpression();
		if(temp==x) {
			
			throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
		}
		while(myList.get(x)==Kind.AND||myList.get(x)==Kind.OR) {
			myLocalKind=(Kind)myList.get(x);
			x+=1;
			temp=x;
			right=myComparisonExpression();
			if(temp==x) {		
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
			}
		}
		return new BinaryExpression__((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x)),left,right,myLocalKind);
	}
	
	public IBinaryExpression myComparisonExpression() throws SyntaxException{
		temp=x;
		IExpression left=myAdditiveExpression();
		IExpression right=null;
		Kind myLocalKind=null;
		if(temp==x) {
			
			throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
		}
		while(myList.get(x)==Kind.GT||myList.get(x)==Kind.LT||myList.get(x)==Kind.EQUALS||myList.get(x)==Kind.NOT_EQUALS) {
			myLocalKind=(Kind)myList.get(x);
			x+=1;
			temp=x;
			right=myAdditiveExpression();
			if(temp==x) {
				
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
			}
		}
		return new BinaryExpression__((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x)),left,right,myLocalKind);
	}
	
	public IBinaryExpression myAdditiveExpression() throws SyntaxException{
		temp=x;
		IExpression left=myMultiplicativeExpression();
		IExpression right=null;
		Kind myLocalKind=null;
		if(temp==x) {
			
			throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
		}
		while(myList.get(x)==Kind.PLUS||myList.get(x)==Kind.MINUS) {
			myLocalKind=(Kind)myList.get(x);
			x+=1;
			temp=x;
			right=myMultiplicativeExpression();
			if(temp==x) {
				
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
			}
		}
		return new BinaryExpression__((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x)),left,right,myLocalKind);
	}
	
	public IBinaryExpression myMultiplicativeExpression() throws SyntaxException{
		temp=x;
		IExpression left=myUnaryExpression();
		IExpression right=null;
		Kind myLocalKind=null;
		if(temp==x) {
			
			throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
		}
		while(myList.get(x)==Kind.DIV||myList.get(x)==Kind.TIMES) {
			myLocalKind=(Kind)myList.get(x);
			x+=1;
			temp=x;
			right=myUnaryExpression();
			if(temp==x) {
				
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
			}
		}
		return new BinaryExpression__((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x)),left,right,myLocalKind);
	}
	
	public IUnaryExpression myUnaryExpression() throws SyntaxException {
		if(myList.get(x)==Kind.BANG||myList.get(x)==Kind.MINUS) {
			x+=1;
		}
		temp=x;
		IExpression node1=myPrimaryExpression();
		if(temp==x) {
			
			throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
		}
		return new UnaryExpression__((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x)),node1,(Kind)myList.get(x));
	}
	
	public IExpression myPrimaryExpression() throws SyntaxException{
		if(myList.get(x)==Kind.KW_NIL||myList.get(x)==Kind.KW_TRUE||myList.get(x)==Kind.KW_FALSE||myList.get(x)==Kind.INT_LITERAL||myList.get(x)==Kind.STRING_LITERAL) {
			x+=1;
			if(myList.get(x)==Kind.KW_NIL) {
				return new NilConstantExpression__((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x)));
			}
			if(myList.get(x)==Kind.KW_TRUE) {
				boolean value=true;
				return new BooleanLiteralExpression__((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x)),value);
			}
			if(myList.get(x)==Kind.KW_FALSE) {
				boolean value=false;
				return new BooleanLiteralExpression__((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x)),value);
			}
			if(myList.get(x)==Kind.INT_LITERAL) {
				int abc=(int)myList.get(x);
				return new IntLiteralExpression__((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x)),abc);
			}
			if(myList.get(x)==Kind.STRING_LITERAL) {
				String def=(String)myList.get(x);
				return new StringLiteralExpression__((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x)),def);
			}
		}
		else if(myList.get(x)==Kind.LPAREN) {
			x+=1;
			temp=x;
			//Expression required
			IExpression node=myExpression();
			if(temp==x) {
				
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
			}
			if(myList.get(x)==Kind.RPAREN) {
				x+=1;
			}
			else {
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
			}
			return node;
		}
		else if(myList.get(x)==Kind.IDENTIFIER) {
			
			x+=1;
			ArrayList<IExpression> nodeList=new ArrayList<IExpression>();
			IIdentifier myName=new Identifier__((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x)),String.valueOf(myNameList.get(x)));
			if(myList.get(x)==Kind.LPAREN) {
				x+=1;
				if(myList.get(x)==Kind.RPAREN) {
					x+=1;
				}
				else {
					temp=x;
					nodeList.add(myExpression());
					if(temp!=x) {
					while(myList.get(x)==Kind.COMMA) {
						x+=1;
						temp=x;
						//Expression required
						nodeList.add(myExpression());
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
				return new FunctionCallExpression__((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x)),myName,nodeList);
			}
			else if(myList.get(x)==Kind.LSQUARE) {
				x+=1;
				temp=x;
				//Expression required
				IExpression node=myExpression();
				if(temp==x) {
					
					throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
				}
				if(myList.get(x)==Kind.RSQUARE) {
					x+=1;
				}
				else {
					
					throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
				}
				return new ListSelectorExpression__((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x)),myName,node);
			}
			else {
				return new IdentExpression__((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x)),myName);
			}
		}
		else {
			
			throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
		}
		return null;
	}
	
	public IType myType() throws SyntaxException{
		IType it=null;
		if(myList.get(x)==Kind.KW_INT) {
			x+=1;
			return new PrimitiveType__((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x)),TypeKind.INT);
		}
		else if(myList.get(x)==Kind.KW_STRING) {
			x+=1;
			return new PrimitiveType__((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x)),TypeKind.STRING);
		}
		else if(myList.get(x)==Kind.KW_BOOLEAN) {
			x+=1;
			return new PrimitiveType__((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x)),TypeKind.BOOLEAN);
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
					it=myType();
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
			return new ListType__((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x)),it);
		}
		else {
			
			throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
		}
	}
	
	
}
