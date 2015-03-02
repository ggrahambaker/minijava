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
    //    private int depth = -2;
    
    /** getTable returns the entire table */
    public ClassTable getTable() {
	    return table;
    } 
    
    /** 
     * Figure out which visitor methods to override...
     */
    
    
    public SymTableVisitor(PrintWriter out) {
	   this.out = out;
    }
   
   
   /** When descending into a new node, increment the indentation level.
    * Decrement the level when leaving a node. */
 
   
   public void caseStart(Start node)
   {
      inStart(node);
      node.getPProgram().apply(this);
      node.getEOF().apply(this);
      outStart(node);
   }
   
   
   public void caseAMainClassDecl(AMainClassDecl node)
   {
       table.putMain(node.getId(),'main');
      inAMainClassDecl(node);
      //out.print("class ");
      //depth++;
      if(node.getId() != null)
      {
    	  table.putMain(node.getId(), 'main');
    	  node.getId().apply(this);
      }
      //out.println(" {");
      //indent(); out.println("public static void main(String[] bogus) {");
      if(node.getStmt() != null)
      {
	      node.getStmt().apply(this);
      }
      //indent(); out.println("}");
      //depth--;
      //indent(); out.println("}\n");
      outAMainClassDecl(node);
   }
   
   
   public void caseABaseClassDecl(ABaseClassDecl node)
   {
      inABaseClassDecl(node);
      
      //indent(); out.print("class ");
      if(node.getId() != null)
      {
	  node.getId().apply(this);	  
	  //out.print(node.getId().getText());
      }
      List<PVarDecl> copy1 = new ArrayList<PVarDecl>(node.getVarDecl());
      List<PMethod> copy2 = new ArrayList<PMethod>(node.getMethod());
      table.put(node.getId(), node.getId(),copy1, copy2);
      //      out.println(" {");
      {
         for(PVarDecl e : copy1)
         {
            e.apply(this);
         }
      }
      {
         for(PMethod e : copy2)
         {
            e.apply(this);
         }
      }
      //indent(); out.println("}\n");
      outABaseClassDecl(node);
   }
   
   
   public void caseASubClassDecl(ASubClassDecl node)
   {
      inASubClassDecl(node);
<<<<<<< HEAD
      // out.print("class ");
=======
      //out.print("class ");
>>>>>>> 92a52cf137bd6fe04eb242ccfa0aec56cdd62a1c
      if(node.getId() != null)
      {
	  node.getId().apply(this);
      }
<<<<<<< HEAD
      // out.print(" extends ");
=======
      //out.print(" extends ");
>>>>>>> 92a52cf137bd6fe04eb242ccfa0aec56cdd62a1c
      if(node.getExtends() != null)
      {
         node.getExtends().apply(this);
         node.getExtends().apply(this);
      }
<<<<<<< HEAD
      // out.println(" {");
=======
      List<PVarDecl> copy1 = new ArrayList<PVarDecl>(node.getVarDecl());
      List<PMethod> copy2 = new ArrayList<PMethod>(node.getMethod());
      table.put(node.getId(), node.getExtends(),copy1, copy2);
      //out.println(" {");
>>>>>>> 92a52cf137bd6fe04eb242ccfa0aec56cdd62a1c
      {
         for(PVarDecl e : copy1)
         {
            e.apply(this);
         }
      }
      {
         for(PMethod e : copy2)
         {
            e.apply(this);
         }
      }
<<<<<<< HEAD
      // out.println("}\n");
=======
      //out.println("}\n");
>>>>>>> 92a52cf137bd6fe04eb242ccfa0aec56cdd62a1c
      outASubClassDecl(node);
   }
   
   
   public void caseAVarDecl(AVarDecl node)
   {
      inAVarDecl(node);
      // indent();
      if(node.getType() != null)
      {
         node.getType().apply(this);
      }
      // out.print(" ");
      if(node.getId() != null)
      {
         out.print(node.getId().getText());
      }
      // out.println(";");
      outAVarDecl(node);
   }
   
   
   public void caseAMethod(AMethod node)
   {
      inAMethod(node);
      // out.println();
      // indent(); out.print("public ");
      if(node.getType() != null)
      {
         node.getType().apply(this);
      }
      // out.print(" ");
      if(node.getId() != null)
      {
         node.getId().getText();
      }
      // out.print("(");
      {
         List<PFormal> copy = new ArrayList<PFormal>(node.getFormal());
         int len = copy.size();
         for(int i=0; i<len-1; i++) {
            copy.get(i).apply(this);
            // out.print(", ");
         }
         if (len > 0)
            copy.get(len-1).apply(this);
      }
      // out.println(") {");
      {
         List<PVarDecl> copy = new ArrayList<PVarDecl>(node.getVarDecl());
         for(PVarDecl e : copy)
         {
            e.apply(this);
         }
      }
      {
         List<PStmt> copy = new ArrayList<PStmt>(node.getStmt());
         for(PStmt e : copy)
         {
            e.apply(this);
         }
      }
      // indent(); out.println("}");
      outAMethod(node);
   }
   
   
   public void caseAFormal(AFormal node)
   {
      inAFormal(node);
      if(node.getType() != null)
      {
         node.getType().apply(this);
      }
      out.print(" ");
      if(node.getId() != null)
      {
         out.print(node.getId().getText());
      }
      outAFormal(node);
   }
   
   
   public void caseAIntType(AIntType node)
   {
      out.print("int");
   }
   
   
   public void caseABoolType(ABoolType node)
   {
      out.print("bool");
   }
   
   
   public void caseAIntArrayType(AIntArrayType node)
   {
      out.print("int[]");
   }
   
   
   public void caseAUserType(AUserType node)
   {
      inAUserType(node);
      if(node.getId() != null)
      {
         out.print(node.getId().getText());
      }
      outAUserType(node);
   }
   
   
   public void caseAReturnStmt(AReturnStmt node)
   {
      inAReturnStmt(node);
      indent(); out.print("return ");
      if(node.getExp() != null)
      {
         node.getExp().apply(this);
      }
      out.println(";");
      outAReturnStmt(node);
   }
   
   
   public void caseABlockStmt(ABlockStmt node)
   {
      inABlockStmt(node);
      indent(); out.println("{");
      {
         List<PStmt> copy = new ArrayList<PStmt>(node.getStmt());
         for(PStmt e : copy)
         {
            e.apply(this);
         }
      }
      indent(); out.println("}");
      outABlockStmt(node);
   }
   
   
   public void caseAIfStmt(AIfStmt node)
   {
      inAIfStmt(node);
      indent(); out.print("if (");
      if(node.getExp() != null)
      {
         node.getExp().apply(this);
      }
      out.println(")");
      if(node.getYes() != null)
      {
         node.getYes().apply(this);
      }
      indent(); out.println("else");
      if(node.getNo() != null)
      {
         node.getNo().apply(this);
      }
      outAIfStmt(node);
   }
   
   
   public void caseAWhileStmt(AWhileStmt node)
   {
      inAWhileStmt(node);
      indent(); out.print("while (");
      if(node.getExp() != null)
      {
         node.getExp().apply(this);
      }
      out.println(")");
      if(node.getStmt() != null)
      {
         node.getStmt().apply(this);
      }
      outAWhileStmt(node);
   }
   
   
   public void caseAPrintStmt(APrintStmt node)
   {
      inAPrintStmt(node);
      indent(); out.print("System.out.println(");
      if(node.getExp() != null)
      {
         node.getExp().apply(this);
      }
      out.println(");");
      outAPrintStmt(node);
   }
   
   
   public void caseAAsmtStmt(AAsmtStmt node)
   {
      inAAsmtStmt(node);
      indent();
      if(node.getId() != null)
      {
         out.print(node.getId().getText());
      }
      out.print(" = ");
      if(node.getExp() != null)
      {
         node.getExp().apply(this);
      }
      out.println(";");
      outAAsmtStmt(node);
   }
   
   
   public void caseAArrayAsmtStmt(AArrayAsmtStmt node)
   {
      inAArrayAsmtStmt(node);
      indent();
      if(node.getId() != null)
      {
         out.print(node.getId().getText());
      }
      out.print("[");
      if(node.getIdx() != null)
      {
         node.getIdx().apply(this);
      }
      out.print("] = ");
      if(node.getVal() != null)
      {
         node.getVal().apply(this);
      }
      out.println(";");
      outAArrayAsmtStmt(node);
   }
   
   
   public void caseAAndExp(AAndExp node)
   {
      inAAndExp(node);
      out.print("(");
      if(node.getLeft() != null)
      {
         node.getLeft().apply(this);
      }
      out.print(" && ");
      if(node.getRight() != null)
      {
         node.getRight().apply(this);
      }
      out.print(")");
      outAAndExp(node);
   }
   
   
   public void caseALtExp(ALtExp node)
   {
      inALtExp(node);
      out.print("(");
      if(node.getLeft() != null)
      {
         node.getLeft().apply(this);
      }
      out.print(" < ");
      if(node.getRight() != null)
      {
         node.getRight().apply(this);
      }
      out.print(")");
      outALtExp(node);
   }
   
   
   public void caseAPlusExp(APlusExp node)
   {
      inAPlusExp(node);
      out.print("(");
      if(node.getLeft() != null)
      {
         node.getLeft().apply(this);
      }
      out.print(" + ");
      if(node.getRight() != null)
      {
         node.getRight().apply(this);
      }
      out.print(")");
      outAPlusExp(node);
   }
   
   
   public void caseAMinusExp(AMinusExp node)
   {
      inAMinusExp(node);
      out.print("(");
      if(node.getLeft() != null)
      {
         node.getLeft().apply(this);
      }
      out.print(" - ");
      if(node.getRight() != null)
      {
         node.getRight().apply(this);
      }
      out.print(")");
      outAMinusExp(node);
   }
   
   
   public void caseATimesExp(ATimesExp node)
   {
      inATimesExp(node);
      out.print("(");
      if(node.getLeft() != null)
      {
         node.getLeft().apply(this);
      }
      out.print(" * ");
      if(node.getRight() != null)
      {
         node.getRight().apply(this);
      }
      out.print(")");
      outATimesExp(node);
   }
   
   
   public void caseANotExp(ANotExp node)
   {
      inANotExp(node);
      out.print("!(");
      if(node.getExp() != null)
      {
         node.getExp().apply(this);
      }
      out.print(")");
      outANotExp(node);
   }
   
   
   public void caseARefExp(ARefExp node)
   {
      inARefExp(node);
      out.print("(");
      if(node.getName() != null)
      {
         node.getName().apply(this);
      }
      out.print("[");
      if(node.getIdx() != null)
      {
         node.getIdx().apply(this);
      }
      out.print("]");
      out.print(")");
      outARefExp(node);
   }
   
   
   public void caseALengthExp(ALengthExp node)
   {
      inALengthExp(node);
      out.print("(");
      if(node.getExp() != null)
      {
         node.getExp().apply(this);
      }
      out.print(".length)");
      outALengthExp(node);
   }
   
   
   public void caseAMethodExp(AMethodExp node)
   {
      inAMethodExp(node);
      out.print("(");
      if(node.getObj() != null)
      {
         node.getObj().apply(this);
      }
      out.print(".");
      if(node.getId() != null)
      {
         out.print(node.getId().getText());
      }
      out.print("(");
      {
         List<PExp> copy = new ArrayList<PExp>(node.getArgs());
         int len = copy.size();
         for(int i=0; i<len-1; i++) {
            copy.get(i).apply(this);
            out.print(", ");
         }
         if (len > 0)
            copy.get(len-1).apply(this); 
         
      }
      out.print(")");
      out.print(")");
      outAMethodExp(node);
   }
   
   
   public void caseANumExp(ANumExp node)
   {
      inANumExp(node);
      if(node.getNum() != null)
      {
         out.print(node.getNum().getText());
      }
      outANumExp(node);
   }
   
   
   public void caseAIdExp(AIdExp node)
   {
      inAIdExp(node);
      if(node.getId() != null)
      {
         out.print(node.getId().getText());
      }
      outAIdExp(node);
   }
   
   
   public void caseATrueExp(ATrueExp node)
   {
      out.print("true");
   }
   
   
   public void caseAFalseExp(AFalseExp node)
   {
      out.print("false");
   }
   
   
   public void caseAThisExp(AThisExp node)
   {
      out.print("this");
   }
   
   
   public void caseAAllocExp(AAllocExp node)
   {
      inAAllocExp(node);
      //out.print("(new int[");
      if(node.getExp() != null)
      {
         node.getExp().apply(this);
      }
      //out.print("])");
      outAAllocExp(node);
   }
   
   
   public void caseANewExp(ANewExp node)
   {
      inANewExp(node);
      if(node.getId() != null)
      {
	  node.getId().apply(this);
      }
      outANewExp(node);
   }


}
