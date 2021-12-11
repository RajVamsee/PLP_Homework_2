package edu.ufl.cise.plpfa21.assignment1;
import edu.ufl.cise.plpfa21.assignment2.*;
import edu.ufl.cise.plpfa21.assignment3.ast.ASTVisitor;
import edu.ufl.cise.plpfa21.assignment5.StarterCodeGenVisitor;

public class CompilerComponentFactory {

	public static IPLPLexer getLexer(String input) {
		//TODO  create and return a Lexer instance to parse the given input.
		return new Lexer(input);
	}
	
	public static IPLPParser getParser(String input) {
		//TODO  create and return a Lexer instance to parse the given input.
		return new Parser(input);
	}
	
	public static ASTVisitor getTypeCheckVisitor() {
		// Replace this with whatever is needed for your compiler
		return new ReferenceTypeCheckVisitor(); 
	}
	
	public static ASTVisitor getCodeGenVisitor(String className, String packageName, String sourceFileName) {
		//Replace this with whatever is needed for your compiler
		return new StarterCodeGenVisitor(className,packageName, sourceFileName);
	}
	
}
