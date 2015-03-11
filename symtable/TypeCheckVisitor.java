package symtable;

import minijava.analysis.DepthFirstAdapter;
import minijava.node.*;
import java.io.PrintWriter;
import java.util.*;
/** 
 * This visitor class goes through the .
 * @author Graham Baker
 */
public class TypeCheckVisitor extends DepthFirstAdapter
{
    private ClassTable table;
    // what scope we are in!
    // private String scope;
    // private ArrayList<VarInfo> vi = new ArrayList(); 



    public TypeCheckVisitor(ClassTable st) {
	    table = st;
	    table.dump();
    	System.out.println("Got our class table, ready to continue");
    }

    public void defaultIn(@SuppressWarnings("unused") Node node)
    {
        // Do nothing
        // System.out.println("The node: " + node.toString());
    }

    public void defaultOut(@SuppressWarnings("unused") Node node)
    {
        // Do nothing
    }
  
	public void caseAProgram(AProgram node){
	  		
	      inAProgram(node);
	    {
	        List<PClassDecl> copy = new ArrayList<PClassDecl>(node.getClassDecl());
	        for(PClassDecl e : copy) {
	            e.apply(this);
	          }
	    }
	    outAProgram(node);
	}

	public void caseAMainClassDecl(AMainClassDecl node)
	{
	    inAMainClassDecl(node);
	    if(node.getId() != null)
	    {
	    	ClassInfo ci = table.get(node.getId().toString());
	    	System.out.println(ci.getName().toString() +  " MAIN!");
	    	System.out.println(ci.getVarTable().size() + " var table size");
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
	    if(node.getId() != null)
	    {
	        ClassInfo ci = table.get(node.getId().toString());
	        System.out.println(" ----- ");
	    	System.out.println(ci.getName().toString() +  " class name!");
	    	System.out.println(ci.getVarTable().size() + " var table size");
	        node.getId().apply(this);
	    }
	    {

	    	// we need to store all these as refrence 
	        List<PVarDecl> copy = new ArrayList<PVarDecl>(node.getVarDecl());
	        for(PVarDecl e : copy)
	        {
	            e.apply(this);
	        }
	    }
	    {
	        List<PMethod> copy = new ArrayList<PMethod>(node.getMethod());
	        for(PMethod e : copy)
	        {
	            e.apply(this);
	        }
	    }
	    outABaseClassDecl(node);
	}

	public void caseASubClassDecl(ASubClassDecl node)
	{
	    inASubClassDecl(node);
	    if(node.getId() != null)
	    {
	        node.getId().apply(this);
	    }
	    if(node.getExtends() != null)
	    {
	        node.getExtends().apply(this);
	    }
	    {
	        List<PVarDecl> copy = new ArrayList<PVarDecl>(node.getVarDecl());
	        for(PVarDecl e : copy)
	        {
	            e.apply(this);
	        }
	    }
	    {
	        List<PMethod> copy = new ArrayList<PMethod>(node.getMethod());
	        for(PMethod e : copy)
	        {
	            e.apply(this);
	        }
	    }
	    outASubClassDecl(node);
	}

	public void caseAVarDecl(AVarDecl node)
	{
	    inAVarDecl(node);
	    if(node.getType() != null)
	    {
	        node.getType().apply(this);
	    }
	    if(node.getId() != null)
	    {
	        node.getId().apply(this);
	    }
	    outAVarDecl(node);
	}


	public void caseAMethod(AMethod node)
	{
	    inAMethod(node);
	    if(node.getType() != null)
	    {
	        node.getType().apply(this);
	    }
	    if(node.getId() != null)
	    {
	        node.getId().apply(this);
	    }
	    {
	        List<PFormal> copy = new ArrayList<PFormal>(node.getFormal());
	        for(PFormal e : copy)
	        {
	            e.apply(this);
	        }
	    }
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
	    outAMethod(node);
	}

	public void caseAFormal(AFormal node)
	{
	    inAFormal(node);
	    if(node.getType() != null)
	    {
	        node.getType().apply(this);
	    }
	    if(node.getId() != null)
	    {
	        node.getId().apply(this);
	    }
	    outAFormal(node);
	}


    // go to the parent method's table, and get return type
    // see if it resolves to the correct type 
    public void caseAReturnStmt(AReturnStmt node)
    {
        inAReturnStmt(node);
        if(node.getExp() != null)
        {
            node.getExp().apply(this);
        }
        outAReturnStmt(node);
    }



    public void caseABlockStmt(ABlockStmt node)
    {
        inABlockStmt(node);
        {
            List<PStmt> copy = new ArrayList<PStmt>(node.getStmt());
            for(PStmt e : copy)
            {
                e.apply(this);
            }
        }
        outABlockStmt(node);
    }


    public void caseAIfStmt(AIfStmt node)
    {
        inAIfStmt(node);
        if(node.getExp() != null)
        {
            node.getExp().apply(this);
        }
        if(node.getYes() != null)
        {
            node.getYes().apply(this);
        }
        if(node.getNo() != null)
        {
            node.getNo().apply(this);
        }
        outAIfStmt(node);
    }


    public void caseAWhileStmt(AWhileStmt node)
    {
        inAWhileStmt(node);
        if(node.getExp() != null)
        {
            node.getExp().apply(this);
        }
        if(node.getStmt() != null)
        {
            node.getStmt().apply(this);
        }
        outAWhileStmt(node);
    }


    public void caseAPrintStmt(APrintStmt node)
    {
        inAPrintStmt(node);
        if(node.getExp() != null)
        {
            node.getExp().apply(this);
        }
        outAPrintStmt(node);
    }

    // 
    public void caseAAsmtStmt(AAsmtStmt node)
    {
        inAAsmtStmt(node);

        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        if(node.getExp() != null)
        {
            node.getExp().apply(this);
        }
        outAAsmtStmt(node);
    }

    public void caseAArrayAsmtStmt(AArrayAsmtStmt node)
    {
        inAArrayAsmtStmt(node);
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        if(node.getIdx() != null)
        {
            node.getIdx().apply(this);
        }
        if(node.getVal() != null)
        {
            node.getVal().apply(this);
        }
        outAArrayAsmtStmt(node);
    }

    public void caseAAndExp(AAndExp node)
    {
        inAAndExp(node);
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }
        outAAndExp(node);
    }

    public void caseALtExp(ALtExp node)
    {
        inALtExp(node);
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }
        outALtExp(node);
    }

    

   
   // public void caseStart(Start node) {
   	
   //    inStart(node);
   //    node.getPProgram().apply(this);
   //    node.getEOF().apply(this);
   //    outStart(node);
   // }






   
   
   //  public void caseAMainClassDecl(AMainClassDecl node)// throws Exception
   // {
   //     //table.putMain(node.getId(),'main');
   //    inAMainClassDecl(node);
   //    //System.out.println("Now doing main"+node.getId().toString());

   //    if(node.getId() != null)
   //    {
   //      try {

   //          table.putMain(node.getId().toString(), "main");}

   //      catch(Exception e){
	  //   System.exit(-1);
	  //   }
        
   //      node.getId().apply(this);
   //    }

   //    if(node.getStmt() != null)
   //    {
	  //     node.getStmt().apply(this);
   //    }

   //    outAMainClassDecl(node);
   // }
   
   
   //  public void caseABaseClassDecl(ABaseClassDecl node)// throws Exception
   // {
   //    inABaseClassDecl(node);
   //    //System.out.println("Now doing"+node.getId().toString());
      
   //    //indent(); out.print("class ");
   //    if(node.getId() != null)
   //    {
   // 	  node.getId().apply(this);	  
   // 	  //out.print(node.getId().getText());
   //    }
   //    List<PVarDecl> copy1 = new ArrayList<PVarDecl>(node.getVarDecl());
   //    List<PMethod> copy2 = new ArrayList<PMethod>(node.getMethod());
   //    try{
	  // table.put(node.getId(), null, new LinkedList<PVarDecl>(copy1),new LinkedList<PMethod>(copy2));
      
   //    }
   //    catch(Exception e){
	  // System.exit(-1);}
   //    //      out.println(" {");
   //    {

   //      id = node.getId().toString();
   //       for(PVarDecl e : copy1)
   //       {
   //          e.apply(this);
   //       }
   //    }
   //    id = node.getId().toString();
   //    {
   //       for(PMethod e : copy2)
   //       {
   //          e.apply(this);
   //       }
   //    }
      
   //    //indent(); out.println("}\n");
   //    outABaseClassDecl(node);
   // }
   
   
   //  public void caseASubClassDecl(ASubClassDecl node)// throws Exception
   // {
   //    inASubClassDecl(node);
   //    //System.out.println("Now doing"+node.getId().toString());

   //    if(node.getId() != null)
   //    {
	  //  node.getId().apply(this);
   //    }
   //    //out.print(" extends ");
   //    if(node.getExtends() != null)
   //    {
   //       node.getExtends().apply(this);
   //       node.getExtends().apply(this);
   //    }

   //    List<PVarDecl> copy1 = new ArrayList<PVarDecl>(node.getVarDecl());
   //    List<PMethod> copy2 = new ArrayList<PMethod>(node.getMethod());
   //    try{
	  // table.put(node.getId(), node.getExtends(), new LinkedList<PVarDecl>(copy1),new LinkedList<PMethod>(copy2));}
   //    catch(Exception e){
	  // System.exit(-1);}
   //    //out.println(" {");

   //    {
   //      id = node.getId().toString();
   //       for(PVarDecl e : copy1)
   //       {
   //          e.apply(this);
   //       }
   //    }

   //    id = node.getId().toString();
   //    {
   //       for(PMethod e : copy2)
   //       {
   //          e.apply(this);
   //       }
   //    }

   //    //out.println("}\n");

   //    outASubClassDecl(node);
   // }
   
}
