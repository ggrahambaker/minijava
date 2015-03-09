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
    private String id;

    private PrintWriter out;
    //    private int depth = -2;
    
    /** getTable returns the entire table */
    public ClassTable getTable() {
	    return table;
    } 
    
    /** 
     * Figure out which visitor methods to override...
     */

    public void defaultIn(@SuppressWarnings("unused") Node node)
    {
        // Do nothing
    }

    public void defaultOut(@SuppressWarnings("unused") Node node)
    {
        // Do nothing
    }
    
    
    public SymTableVisitor() {
	     this.out = new PrintWriter(System.out);
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
       //table.putMain(node.getId(),'main');
      inAMainClassDecl(node);
      //System.out.println("Now doing main"+node.getId().toString());

      if(node.getId() != null)
      {
        try {

            table.putMain(node.getId().toString(), "main");}

        catch(Exception e){
            //throw e;
	    }
        
        node.getId().apply(this);
      }

      if(node.getStmt() != null)
      {
	      node.getStmt().apply(this);
      }

      outAMainClassDecl(node);
   }
   
   
   public void caseABaseClassDecl(ABaseClassDecl node)
   {
      inABaseClassDecl(node);
      //System.out.println("Now doing"+node.getId().toString());
      
      //indent(); out.print("class ");
      if(node.getId() != null)
      {
   	  node.getId().apply(this);	  
   	  //out.print(node.getId().getText());
      }
      List<PVarDecl> copy1 = new ArrayList<PVarDecl>(node.getVarDecl());
      List<PMethod> copy2 = new ArrayList<PMethod>(node.getMethod());
      try{
	  table.put(node.getId(), null, new LinkedList<PVarDecl>(copy1),new LinkedList<PMethod>(copy2));
      
      }
      catch(Exception e){}
      //      out.println(" {");
      {

        id = node.getId().toString();
         for(PVarDecl e : copy1)
         {
            e.apply(this);
         }
      }
      id = node.getId().toString();
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
      //System.out.println("Now doing"+node.getId().toString());

      if(node.getId() != null)
      {
	   node.getId().apply(this);
      }
      //out.print(" extends ");
      if(node.getExtends() != null)
      {
         node.getExtends().apply(this);
         node.getExtends().apply(this);
      }

      List<PVarDecl> copy1 = new ArrayList<PVarDecl>(node.getVarDecl());
      List<PMethod> copy2 = new ArrayList<PMethod>(node.getMethod());
      try{
	  table.put(node.getId(), node.getExtends(), new LinkedList<PVarDecl>(copy1),new LinkedList<PMethod>(copy2));}
      catch(Exception e){}
      //out.println(" {");

      {
        id = node.getId().toString();
         for(PVarDecl e : copy1)
         {
            e.apply(this);
         }
      }

      id = node.getId().toString();
      {
         for(PMethod e : copy2)
         {
            e.apply(this);
         }
      }

      //out.println("}\n");

      outASubClassDecl(node);
   }
   
}
