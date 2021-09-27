package edu.ufl.cise.plpfa21.assignment2;
import java.util.ArrayList;

import edu.ufl.cise.plpfa21.assignment1.*;
import edu.ufl.cise.plpfa21.assignment1.PLPTokenKinds.Kind;

public class Parser implements IPLPParser {

	String s;
	
	public Parser(String input) {
		s=input;
	}

	@Override
	public void parse() throws SyntaxException, LexicalException {
		
		IPLPLexer lexer = CompilerComponentFactory.getLexer(s);
		ArrayList<Kind> myList=new ArrayList<Kind>();
		IPLPToken token = lexer.nextToken();
		myList.add(token.getKind());
		
		
		while(token.getKind()!=Kind.EOF){
			token = lexer.nextToken();
			myList.add(token.getKind());
		}
		
		System.out.println(myList);
		System.out.println("Test Case Done");
		
		
		
		
		
		
		
		
		
		
		
	}
	

}
