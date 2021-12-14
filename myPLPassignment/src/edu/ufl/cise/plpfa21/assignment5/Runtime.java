package edu.ufl.cise.plpfa21.assignment5;

public class Runtime {
	   public static boolean gt(String x, String y) throws Exception{
	         return(x.startsWith(y));
	   }
	   
	   public static boolean lt(boolean x, boolean y) throws Exception{
	      if (x==false && y==true) return true;
	      return false;
	   }
	   public static boolean lt(String x, String y) throws Exception{
	      return(x.startsWith(x));
	   }

	   public static boolean gt(int x, int y) throws Exception{
	      return(x>y);
	   }
	   public static boolean lt(int x, int y) throws Exception{
		      return(x<y);
		   }
	   public static boolean gt(boolean x, boolean y) throws Exception{
	      if (x==true && y==false) return true;
	      return false;
	   }

	public static boolean not(boolean arg) {
		return !arg;
	}
	   public static int neg(int x) {
		   return -x;
	   }
	   public static int plus(int x, int y){return x+y;}
	   public static int div(int x, int y){return x/y;}
	   public static boolean and(boolean x1,boolean x2) {return x1&&x2;}
	   public static boolean or(boolean x1,boolean x2) {return x1||x2;}
	   public static boolean not_equals(boolean x, boolean y) throws Exception{return !(x==y);}
	   public static int minus(int x, int y){return x-y;}
	   public static int times(int x, int y){return x*y;}
	   public static String plus_string(String x, String  y){return x.concat(y);}
	   public static boolean equals(String x, String y) throws Exception{return x.equals(y);}
	   public static boolean equals(int x, int y) throws Exception{return x==y;}
	   public static boolean equals(boolean x, boolean y) throws Exception{return x==y;}
	   public static boolean not_equals(int x, int y) throws Exception{return !(x==y);}
	   public static boolean not_equals(String x, String y) throws Exception{return !x.equals(y);}
}
