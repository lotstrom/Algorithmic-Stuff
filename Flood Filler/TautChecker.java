class TautChecker {

 public static String union(String s1, String s2) {
  int i;
  for (i = 0; i < s2.length(); i++) {  // for each character in s1
   if (s1.indexOf(s2.charAt(i)) == -1) {  // the character is not already in s2
    s1 = s1 + s2.charAt(i);  // add the character to s2
   }
  }
  return s1;
 }

 public static String formulaVars(Formula f) {
  switch (f.form()) {
   case VARIABLE:
    return f.variableName();
   case NOT:
    return formulaVars(f.subFormula(1));
   default: // it's a binary connective
    return union(formulaVars(f.subFormula(1)), formulaVars(f.subFormula(2)));
     // we compute the union of the two subformulas to avoid several occurrences of the same var
  }
 }

 public static void main(String[] args) {
  Formula f = Formula.parse(args[0]);
  System.out.println(f.toString());
  System.out.println(formulaVars(f));
 }
}
