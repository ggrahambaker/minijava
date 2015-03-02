package symtable;

import minijava.analysis.DepthFirstAdapter;
import minijava.node.*;
import java.io.PrintWriter;
import java.util.*;

/** 
 * This visitor class builds a symbol table as it traverses the tree.  The
 * table, an instance of ClassTable, can be returned via getTable().
 * @author Brad Richards
 */

public class SymTableVisitor extends DepthFirstAdapter
{
   private ClassTable table = new ClassTable();
   private PrintWriter out;
   
   /** getTable returns the entire table */
   public ClassTable getTable() {
      return table;
   } 

   /** 
    * Figure out which visitor methods to override...
    */


    public PrintVisitor(PrintWriter out) {
      this.out = out;
   	}

   	public void caseStart(Start node)
   	{
   	   inStart(node);
   	   node.getPProgram().apply(this);
   	   node.getEOF().apply(this);
   	   outStart(node);
   	}

   


}
