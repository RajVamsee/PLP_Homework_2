package edu.ufl.cise.plpfa21.assignment1;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Lexer implements IPLPLexer {
	public String s;
	int pos=0;
	int lineCounter=1;
	public Lexer(String input) {
		this.s = input;
	}

	@Override
	public IPLPToken nextToken() throws LexicalException {
		// TODO Auto-generated method stub
		for(int i=0;i<s.length();i++) {
			if(s.charAt(i)==' '){
				pos+=1;
			}
			else if(s.charAt(i)=='\n') {
				pos=0;
				lineCounter+=1;
			}
			else {
				break;
			}
		}
		//System.out.println(pos);
		s = s.trim();
		String ans = "";
		if (s.length() != 0) {
			Character c = s.charAt(0);
			String temp = c.toString();
			if (temp.matches("[~`@#%^]")) {
				throw new LexicalException("Invalid token", 1, 1);
			} else if (s.charAt(0) == '\\') {
				if (s.length() < 2) {
					throw new LexicalException("Invalid token", 1, 1);
				} else {
					if (s.charAt(1) == 'b') {
						ans += "\\b";
						s = s.substring(ans.length(), s.length());
						pos+=ans.length();
						return new Tokens(ans,pos-ans.length(),lineCounter);

					} else if (s.charAt(1) == 't') {
						ans += "\\t";
						s = s.substring(ans.length(), s.length());
						pos+=ans.length();
						return new Tokens(ans,pos-ans.length(),lineCounter);

					} else if (s.charAt(1) == 'n') {
						ans += "\\n";
						s = s.substring(ans.length(), s.length());
						pos+=ans.length();
						return new Tokens(ans,pos-ans.length(),lineCounter);

					} else if (s.charAt(1) == 'r') {
						ans += "\\r";
						s = s.substring(ans.length(), s.length());
						pos+=ans.length();
						return new Tokens(ans,pos-ans.length(),lineCounter);

					} else if (s.charAt(1) == 'f') {
						ans += "\\f";
						s = s.substring(ans.length(), s.length());
						pos+=ans.length();
						return new Tokens(ans,pos-ans.length(),lineCounter);

					} else if (s.charAt(1) == '\"') {
						ans += "\\\"";
						s = s.substring(ans.length(), s.length());
						pos+=ans.length();
						return new Tokens(ans,pos-ans.length(),lineCounter);

					} else if (s.charAt(1) == '\'') {
						ans += "\\\'";
						s = s.substring(ans.length(), s.length());
						pos+=ans.length();
						return new Tokens(ans,pos-ans.length(),lineCounter);

					} else if (s.charAt(1) == '\\') {
						ans += "\\\\";
						s = s.substring(ans.length(), s.length());
						pos+=ans.length();
						return new Tokens(ans,pos-ans.length(),lineCounter);

					} else {
						throw new LexicalException("Invalid Escape Character", 1, 1);
					}
				}

			} else if (temp.matches("\"")) {
				if (s.length() >= 2) {
					Pattern pattern = Pattern.compile("(\"[^\"]*\")");
					Matcher matcher = pattern.matcher(s);
					if (matcher.find()) {
						ans += matcher.group(1);
						s = s.substring(ans.length(), s.length());
						pos+=ans.length();
						return new Tokens(ans,pos-ans.length(),lineCounter);

					} else {
						throw new LexicalException("incomplete string literal", 1, 1);
					}
				} else {
					throw new LexicalException("Invalid token", 1, 1);
				}

			} else if (temp.matches("\'")) {
				if (s.length() >= 2) {
					Pattern pattern = Pattern.compile("(\'[^\"]*\')");
					Matcher matcher = pattern.matcher(s);
					if (matcher.find()) {
						ans += matcher.group(1);
						s = s.substring(ans.length(), s.length());
						pos+=ans.length();
						return new Tokens(ans,pos-ans.length(),lineCounter);

					} else {
						throw new LexicalException("incomplete string literal", 1, 1);
					}
				} else {
					throw new LexicalException("Invalid token", 1, 1);
				}

			} else if (temp.matches("[a-zA-Z_$]")) {
				int asc = 0;
				for (int i = 0; i < s.length(); i++) {
					asc = (int) s.charAt(i);
					if ((asc >= 97 && asc <= 122) || (asc >= 65 && asc <= 90) || (asc == 95) || (asc == 36)
							|| (asc >= 48 && asc <= 57)) {
						ans += s.charAt(i);
					} else {
						break;
					}
				}
				s = s.substring(ans.length(), s.length());
				pos+=ans.length();
				return new Tokens(ans,pos-ans.length(),lineCounter);

			}

			else if (temp.matches("[0-9]")) {
				int asc = 0;
				for (int i = 0; i < s.length(); i++) {
					asc = (int) s.charAt(i);
					if (asc >= 48 && asc <= 57) {
						ans += s.charAt(i);
					} else {
						break;
					}
				}
				try {
					long value = Long.parseLong(ans);
					if (value < Integer.MAX_VALUE && value > Integer.MIN_VALUE) {
						s = s.substring(ans.length(), s.length());
						pos+=ans.length();
						return new Tokens(ans,pos-ans.length(),lineCounter);

					} else {
						throw new LexicalException("Integer out of range", 1, 1);
					}
				} catch (Exception e) {
					throw new LexicalException("Integer out of range", 1, 1);
				}
			} else if (s.charAt(0) == '=') {
				if (s.length() >= 2) {
					if (s.charAt(1) == '=') {
						ans = "==";
					} else {
						ans = "=";
					}
				} else {
					ans = "=";
				}
				s = s.substring(ans.length(), s.length());
				pos+=ans.length();
				return new Tokens(ans,pos-ans.length(),lineCounter);

			} else if (s.charAt(0) == ',') {
				ans = s.substring(0, 1);
				s = s.substring(1, s.length());
				pos+=ans.length();
				return new Tokens(ans,pos-ans.length(),lineCounter);

			} else if (s.charAt(0) == ';') {
				ans = s.substring(0, 1);
				s = s.substring(1, s.length());
				pos+=ans.length();
				return new Tokens(ans,pos-ans.length(),lineCounter);

			} else if (s.charAt(0) == ':') {
				ans = s.substring(0, 1);
				s = s.substring(1, s.length());
				pos+=ans.length();
				return new Tokens(ans,pos-ans.length(),lineCounter);

			} else if (s.charAt(0) == '(') {
				ans = s.substring(0, 1);
				s = s.substring(1, s.length());
				pos+=ans.length();
				return new Tokens(ans,pos-ans.length(),lineCounter);

			} else if (s.charAt(0) == ')') {
				ans = s.substring(0, 1);
				s = s.substring(1, s.length());
				pos+=ans.length();
				return new Tokens(ans,pos-ans.length(),lineCounter);

			} else if (s.charAt(0) == '[') {
				ans = s.substring(0, 1);
				s = s.substring(1, s.length());
				pos+=ans.length();
				return new Tokens(ans,pos-ans.length(),lineCounter);

			} else if (s.charAt(0) == ']') {
				ans = s.substring(0, 1);
				s = s.substring(1, s.length());
				pos+=ans.length();
				return new Tokens(ans,pos-ans.length(),lineCounter);

			} else if (s.charAt(0) == '&') {
				if (s.length() >= 2) {
					if (s.charAt(1) == '&') {
						ans = "&&";
						s = s.substring(ans.length(), s.length());
						pos+=ans.length();
						return new Tokens(ans,pos-ans.length(),lineCounter);

					} else {
						throw new LexicalException("Invalid token", 1, 1);
					}
				} else {
					throw new LexicalException("Invalid token", 1, 1);
				}
			} else if (s.charAt(0) == '|') {
				if (s.length() >= 2) {
					if (s.charAt(1) == '|') {
						ans = "||";
						s = s.substring(ans.length(), s.length());
						pos+=ans.length();
						return new Tokens(ans,pos-ans.length(),lineCounter);

					} else {
						throw new LexicalException("Invalid token", 1, 1);
					}
				} else {
					throw new LexicalException("Invalid token", 1, 1);
				}
			} else if (s.charAt(0) == '>') {
				ans = s.substring(0, 1);
				s = s.substring(1, s.length());
				pos+=ans.length();
				return new Tokens(ans,pos-ans.length(),lineCounter);

			} else if (s.charAt(0) == '<') {
				ans = s.substring(0, 1);
				s = s.substring(1, s.length());
				pos+=ans.length();
				return new Tokens(ans,pos-ans.length(),lineCounter);

			} else if (s.charAt(0) == '!') {
				if (s.length() >= 2) {
					if (s.charAt(1) == '=') {
						ans = "!=";
					} else {
						ans = "!";
					}
				} else {
					ans = "!";
				}
				s = s.substring(ans.length(), s.length());
				pos+=ans.length();
				return new Tokens(ans,pos-ans.length(),lineCounter);

			} else if (s.charAt(0) == '+') {
				ans = s.substring(0, 1);
				s = s.substring(1, s.length());
				pos+=ans.length();
				return new Tokens(ans,pos-ans.length(),lineCounter);

			} else if (s.charAt(0) == '-') {
				ans = s.substring(0, 1);
				s = s.substring(1, s.length());
				pos+=ans.length();
				return new Tokens(ans,pos-ans.length(),lineCounter);

			} else if (s.charAt(0) == '*') {
				ans = s.substring(0, 1);
				s = s.substring(1, s.length());
				pos+=ans.length();
				return new Tokens(ans,pos-ans.length(),lineCounter);

			} else if (s.charAt(0) == '/') {
				if (s.length() >= 2) {
					if (s.charAt(1) == '*') {
						Pattern pattern = Pattern.compile("((?s)/\\*(.)*?\\*/)");
						Matcher matcher = pattern.matcher(s);
						if (matcher.find()) {
							ans += matcher.group(1);
							s = s.substring(ans.length(), s.length());
							pos+=ans.length();
							return new Tokens(s,pos-ans.length(),lineCounter);

						} else {
							throw new LexicalException("incomplete comment", 1, 1);

						}

					} else {
						ans = "/";
						s = s.substring(ans.length(), s.length());
						pos+=ans.length();
						return new Tokens(ans,pos-ans.length(),lineCounter);

					}
				} else {
					ans = "/";
					s = s.substring(ans.length(), s.length());
					pos+=ans.length();
					return new Tokens(ans,pos-ans.length(),lineCounter);

				}

			} else if (s.charAt(0) == 'v' && s.charAt(1) == 'a' && s.charAt(2) == 'r' && s.charAt(3) == ' ') {
				ans = s.substring(0, 3);
				s = s.substring(3, s.length());
				pos+=ans.length();
				return new Tokens(ans,pos-ans.length(),lineCounter);

			} else if (s.charAt(0) == 'v' && s.charAt(1) == 'a' && s.charAt(2) == 'l' && s.charAt(3) == ' ') {
				ans = s.substring(0, 3);
				s = s.substring(3, s.length());
				pos+=ans.length();
				return new Tokens(ans,pos-ans.length(),lineCounter);

			} else if (s.charAt(0) == 'f' && s.charAt(1) == 'u' && s.charAt(2) == 'n' && s.charAt(3) == ' ') {
				ans = s.substring(0, 3);
				s = s.substring(3, s.length());
				pos+=ans.length();
				return new Tokens(ans,pos-ans.length(),lineCounter);

			} else if (s.charAt(0) == 'e' && s.charAt(1) == 'n' && s.charAt(2) == 'd' && s.charAt(3) == ' ') {
				ans = s.substring(0, 3);
				s = s.substring(3, s.length());
				pos+=ans.length();
				return new Tokens(ans,pos-ans.length(),lineCounter);

			} else if (s.charAt(0) == 'd' && s.charAt(1) == 'o' && s.charAt(3) == ' ') {
				ans = s.substring(0, 2);
				s = s.substring(2, s.length());
				pos+=ans.length();
				return new Tokens(ans,pos-ans.length(),lineCounter);

			} else if (s.charAt(0) == 'l' && s.charAt(1) == 'e' && s.charAt(2) == 't' && s.charAt(3) == ' ') {
				ans = s.substring(0, 3);
				s = s.substring(3, s.length());
				pos+=ans.length();
				return new Tokens(ans,pos-ans.length(),lineCounter);

			} else if (s.substring(0, 6).equals("switch") && s.charAt(6) == ' ') {
				ans = s.substring(0, 6);
				s = s.substring(6, s.length());
				pos+=ans.length();
				return new Tokens(ans,pos-ans.length(),lineCounter);

			} else if (s.substring(0, 4).equals("case") && s.charAt(4) == ' ') {
				ans = s.substring(0, 4);
				s = s.substring(4, s.length());
				pos+=ans.length();
				return new Tokens(ans,pos-ans.length(),lineCounter);

			} else if (s.substring(0, 7).equals("default") && s.charAt(7) == ' ') {
				ans = s.substring(0, 7);
				s = s.substring(7, s.length());
				pos+=ans.length();
				return new Tokens(ans,pos-ans.length(),lineCounter);

			} else if (s.substring(0, 2).equals("if") && s.charAt(2) == ' ') {
				ans = s.substring(0, 2);
				s = s.substring(2, s.length());
				pos+=ans.length();
				return new Tokens(ans,pos-ans.length(),lineCounter);

			} else if (s.substring(0, 4).equals("else") && s.charAt(4) == ' ') {
				ans = s.substring(0, 4);
				s = s.substring(4, s.length());
				pos+=ans.length();
				return new Tokens(ans,pos-ans.length(),lineCounter);

			} else if (s.substring(0, 5).equals("while") && s.charAt(5) == ' ') {
				ans = s.substring(0, 5);
				s = s.substring(5, s.length());
				pos+=ans.length();
				return new Tokens(ans,pos-ans.length(),lineCounter);

			} else if (s.substring(0, 6).equals("return") && s.charAt(6) == ' ') {
				ans = s.substring(0, 6);
				s = s.substring(6, s.length());
				pos+=ans.length();
				return new Tokens(ans,pos-ans.length(),lineCounter);

			} else if (s.substring(0, 3).equals("nil") && s.charAt(3) == ' ') {
				ans = s.substring(0, 3);
				s = s.substring(3, s.length());
				pos+=ans.length();
				return new Tokens(ans,pos-ans.length(),lineCounter);

			} else if (s.substring(0, 4).equals("true") && s.charAt(4) == ' ') {
				ans = s.substring(0, 4);
				s = s.substring(4, s.length());
				pos+=ans.length();
				return new Tokens(ans,pos-ans.length(),lineCounter);

			} else if (s.substring(0, 5).equals("false") && s.charAt(5) == ' ') {
				ans = s.substring(0, 5);
				s = s.substring(5, s.length());
				pos+=ans.length();
				return new Tokens(ans,pos-ans.length(),lineCounter);

			} else if (s.substring(0, 3).equals("int") && s.charAt(3) == ' ') {
				ans = s.substring(0, 3);
				s = s.substring(3, s.length());
				pos+=ans.length();
				return new Tokens(ans,pos-ans.length(),lineCounter);

			} else if (s.substring(0, 6).equals("string") && s.charAt(6) == ' ') {
				ans = s.substring(0, 6);
				s = s.substring(6, s.length());
				pos+=ans.length();
				return new Tokens(ans,pos-ans.length(),lineCounter);

			} else if (s.substring(0, 7).equals("boolean") && s.charAt(7) == ' ') {
				ans = s.substring(0, 7);
				s = s.substring(7, s.length());
				pos+=ans.length();
				return new Tokens(ans,pos-ans.length(),lineCounter);

			} else if (s.substring(0, 4).equals("list") && s.charAt(4) == ' ') {
				ans = s.substring(0, 4);
				s = s.substring(4, s.length());
				pos+=ans.length();
				return new Tokens(ans,pos-ans.length(),lineCounter);

			}
		}
		
		return new Tokens("\n",pos-ans.length(),lineCounter);

	}
}
