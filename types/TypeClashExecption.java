package symtable;

/**
 * A TypeClashExecption is thrown when two variables in the same scope
 * are found to have the same name.
 * @author Graham Baker, Jon Simms, Jacob Imlay
 */

@SuppressWarnings("serial")
public class TypeClashExecption extends java.lang.Exception {
   public TypeClashExecption(String varName, int line) {
       System.out.println("Variable " + varName +
       	" on line " + line +
       	" is wrong");
       System.exit(1);
   }
}
