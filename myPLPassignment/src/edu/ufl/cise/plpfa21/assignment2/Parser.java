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
	@SuppressWarnings("rawtypes")
	ArrayList myIntValues=new ArrayList();
	@SuppressWarnings("rawtypes")
	ArrayList myStringValues=new ArrayList();
	IPLPToken token;
	ArrayList<IDeclaration> myDeclarationList=new ArrayList<IDeclaration>();	
	public Parser(String input) {
		s=input;
	}
	
	@SuppressWarnings("unchecked")
	public IASTNode parse() throws SyntaxException, LexicalException {
		
		lexer = CompilerComponentFactory.getLexer(s);
		token = lexer.nextToken();
		myList.add(token.getKind());
		myLineList.add(token.getLine());
		myPositionList.add(token.getCharPositionInLine());
		myNameList.add(token.getText());
		if(token.getKind()==Kind.INT_LITERAL||token.getKind()==Kind.STRING_LITERAL) {
			if(token.getKind()==Kind.INT_LITERAL) {
				myIntValues.add(token.getIntValue());
			}
			else {
				myStringValues.add(token.getStringValue());
			}
		}
		else {
			myIntValues.add(null);
			myStringValues.add(null);
		}
		
		while(token.getKind()!=Kind.EOF){
			token = lexer.nextToken();
			myList.add(token.getKind());
			myLineList.add(token.getLine());
			myPositionList.add(token.getCharPositionInLine());
			myNameList.add(token.getText());
			if(token.getKind()==Kind.INT_LITERAL||token.getKind()==Kind.STRING_LITERAL) {
				if(token.getKind()==Kind.INT_LITERAL) {
					myIntValues.add(token.getIntValue());
				}
				else {
					myStringValues.add(token.getStringValue());
				}
			}
			else {
				myIntValues.add(null);
				myStringValues.add(null);
			}
		}
		ArrayList<IDeclaration> myDec=null;
		while(myList.get(x)!=Kind.EOF) myDec=myDeclaration();
		
		if(myList.get(x)!=Kind.EOF) {
			throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
		}
		return new Program__((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x)),myDec);
	}
	
	public  ArrayList<IDeclaration> myDeclaration() throws SyntaxException {
		if(myList.get(x)==Kind.KW_VAR) {
			myDeclarationList.add(myVar());
		}
		if(myList.get(x)==Kind.KW_VAL) {
			myDeclarationList.add(myVal());
			
		}
		if(myList.get(x)==Kind.KW_FUN){
			temp=x;
			myDeclarationList.add(myFunction());
			if(temp==x) {
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x)); 
			}
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
				//System.out.println(node2);
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
			//System.out.println(node4);
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
				//System.out.println(exp);
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
			//System.out.println(switchExpression);
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
				//System.out.println(branchExpression);
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
			//System.out.println(ex);
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
			//System.out.println(ie);
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
			//System.out.println(myExp);
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
			//System.out.println(l);
			if(temp==x) {
				
				throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
			}
			if(myList.get(x)==Kind.ASSIGN) {
				x+=1;
				temp=x;
				//Expression required
				r=myExpression();
				//System.out.println(r);
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
		//System.out.println(myList.get(x));
		temp=x;
		IExpression node=myLogicalExpression();
		if(temp==x) {
			throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
		}
		//System.out.println(myList.get(x));
		return node;
	}
	
	public IExpression myLogicalExpression() throws SyntaxException {
		//System.out.println(myList.get(x));
		temp=x;
		IExpression right=null;
		Kind myLocalKind=null;
		IExpression left=myComparisonExpression();
		if(temp==x) {
			
			throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
		}
		if(myList.get(x)==Kind.AND||myList.get(x)==Kind.OR) {
			while(myList.get(x)==Kind.AND||myList.get(x)==Kind.OR) {
				myLocalKind=(Kind)myList.get(x);
				x+=1;
				temp=x;
				right=myComparisonExpression();
				if(temp==x) {		
					throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
				}
			}
			//System.out.println(myList.get(x));
			return new BinaryExpression__((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x)),left,right,myLocalKind);
		}
		//System.out.println(myList.get(x));
		return left;
	}
	
	public IExpression myComparisonExpression() throws SyntaxException{
		//System.out.println(myList.get(x));
		temp=x;
		IExpression left=myAdditiveExpression();
		IExpression right=null;
		Kind myLocalKind=null;
		if(temp==x) {
			
			throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
		}
		if(myList.get(x)==Kind.GT||myList.get(x)==Kind.LT||myList.get(x)==Kind.EQUALS||myList.get(x)==Kind.NOT_EQUALS) {
			while(myList.get(x)==Kind.GT||myList.get(x)==Kind.LT||myList.get(x)==Kind.EQUALS||myList.get(x)==Kind.NOT_EQUALS) {
				myLocalKind=(Kind)myList.get(x);
				x+=1;
				temp=x;
				right=myAdditiveExpression();
				if(temp==x) {
					
					throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
				}
			}
			//System.out.println(myList.get(x));
			return new BinaryExpression__((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x)),left,right,myLocalKind);
		}
		//System.out.println(myList.get(x));
		return left;
	}
	
	public IExpression myAdditiveExpression() throws SyntaxException{
		//System.out.println(myList.get(x));
		temp=x;
		IExpression left=myMultiplicativeExpression();
		IExpression right=null;
		Kind myLocalKind=null;
		if(temp==x) {
			
			throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
		}
		if(myList.get(x)==Kind.PLUS||myList.get(x)==Kind.MINUS) {
			while(myList.get(x)==Kind.PLUS||myList.get(x)==Kind.MINUS) {
				myLocalKind=(Kind)myList.get(x);
				x+=1;
				temp=x;
				right=myMultiplicativeExpression();
				if(temp==x) {
					
					throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
				}
			}
			//System.out.println(myList.get(x));
			return new BinaryExpression__((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x)),left,right,myLocalKind);
		}
		//System.out.println(myList.get(x));
		return left;
	}
	
	public IExpression myMultiplicativeExpression() throws SyntaxException{
		//System.out.println(myList.get(x));
		temp=x;
		IExpression left=myUnaryExpression();
		IExpression right=null;
		Kind myLocalKind=null;
		if(temp==x) {
			
			throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
		}
		if(myList.get(x)==Kind.DIV||myList.get(x)==Kind.TIMES){
			while(myList.get(x)==Kind.DIV||myList.get(x)==Kind.TIMES) {
				myLocalKind=(Kind)myList.get(x);
				x+=1;
				temp=x;
				right=myUnaryExpression();
				if(temp==x) {
					throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
				}
			}
			//System.out.println(left+" "+right);
			return new BinaryExpression__((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x)),left,right,myLocalKind);
		}
		//System.out.println(left);
		return left;
	}
	
	public IExpression myUnaryExpression() throws SyntaxException {
		//System.out.println(myList.get(x));
		IExpression node1=null;
		if(myList.get(x)==Kind.BANG||myList.get(x)==Kind.MINUS) {
			x+=1;
			node1=myPrimaryExpression();
			//System.out.println(node1);
			return new UnaryExpression__((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x)),node1,(Kind)myList.get(x));
		}
		temp=x;
		node1=myPrimaryExpression();
		if(temp==x) {
			
			throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
		}
		//System.out.println(node1);
		return node1;
	}
	
	public IExpression myPrimaryExpression() throws SyntaxException {
		//System.out.println(myList.get(x));
		if(myList.get(x)==Kind.KW_NIL||myList.get(x)==Kind.KW_TRUE||myList.get(x)==Kind.KW_FALSE||myList.get(x)==Kind.INT_LITERAL||myList.get(x)==Kind.STRING_LITERAL) {
			if(myList.get(x)==Kind.KW_NIL) {
				x+=1;
				//System.out.println(myList.get(x));
				return new NilConstantExpression__((Integer)myLineList.get(x-1),(Integer)myPositionList.get(x-1),String.valueOf(myNameList.get(x-1)));
			}
			if(myList.get(x)==Kind.KW_TRUE) {
				x+=1;
				boolean value=true;
				//System.out.println(myList.get(x));
				return new BooleanLiteralExpression__((Integer)myLineList.get(x-1),(Integer)myPositionList.get(x-1),String.valueOf(myNameList.get(x-1)),value);
			}
			if(myList.get(x)==Kind.KW_FALSE) {
				x+=1;
				boolean value=false;
				//System.out.println(myList.get(x));
				return new BooleanLiteralExpression__((Integer)myLineList.get(x-1),(Integer)myPositionList.get(x-1),String.valueOf(myNameList.get(x-1)),value);
			}
			if(myList.get(x)==Kind.INT_LITERAL) {
				x+=1;
				//System.out.println(myList.get(x));
				return new IntLiteralExpression__((Integer)myLineList.get(x-1),(Integer)myPositionList.get(x-1),String.valueOf(myNameList.get(x-1)),(int)myIntValues.get(x-1));
			}
			if(myList.get(x)==Kind.STRING_LITERAL) {
				x+=1;
				//System.out.println(myList.get(x));
				return new StringLiteralExpression__((Integer)myLineList.get(x-1),(Integer)myPositionList.get(x-1),String.valueOf(myNameList.get(x-1)),(String)myStringValues.get(x-1));
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
			//System.out.println(myList.get(x));
			return node;
		}
		else if(myList.get(x)==Kind.IDENTIFIER) {
			
			x+=1;
			ArrayList<IExpression> nodeList=new ArrayList<IExpression>();
			IIdentifier myName=new Identifier__((Integer)myLineList.get(x-1),(Integer)myPositionList.get(x-1),String.valueOf(myNameList.get(x-1)),String.valueOf(myNameList.get(x-1)));
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
				//System.out.println(myList.get(x));
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
				//System.out.println(myList.get(x));
				return new ListSelectorExpression__((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x)),myName,node);
			}
			else {
				//System.out.println(myList.get(x));
				return new IdentExpression__((Integer)myLineList.get(x),(Integer)myPositionList.get(x),String.valueOf(myNameList.get(x)),myName);
			}
		}
		else {
			throw new SyntaxException(s,(Integer)myLineList.get(x),(Integer)myPositionList.get(x));
		}
		//System.out.println(myList.get(x));
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
