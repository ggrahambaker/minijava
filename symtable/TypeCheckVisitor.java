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
    // for evaluating expressions
    private Token carrier;
    // for passing ids
    private Node medium;


    public TypeCheckVisitor(ClassTable st) {
	    table = st;
	    carrier = null;
	    medium = null;
	    // table.dump();
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
	    	// System.out.println(ci.getName().toString() +  " MAIN!");
	    	// System.out.println(ci.getVarTable().size() + " var table size");
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
	    // // System.out.println(" NODE GET PARENT ");
	    // System.out.println(node.parent().toString());
	    if(node.getId() != null)
	    {
	        ClassInfo ci = table.get(node.getId().toString());
	        // System.out.println(" ----- ");
	    	// System.out.println(ci.getName().toString() +  " class name!");
	    	// System.out.println(ci.getVarTable().size() + " var table size");
	        node.getId().apply(this);
	    }
	    {

	    	// we need to store all these as refrence 
	        List<PVarDecl> copy = new ArrayList<PVarDecl>(node.getVarDecl());
	        medium = node;
	        for(PVarDecl e : copy)
	        {
	        	// send these down to the caseAVarDecl
	        	// System.out.println("about to go get the instance variables");
	        	// System.out.println("---");
	         	e.apply(this);
	        }
	    }
	    {
	        List<PMethod> copy = new ArrayList<PMethod>(node.getMethod());
	        for(PMethod e : copy)
	        {
	        	// System.out.println("about to go get the methods");
	        	// System.out.println("---");
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
	        ClassInfo ci = table.get(node.getId().toString());
	        // System.out.println(" ----- ");
	    	// System.out.println(ci.getName().toString() +  " class name!");
	    	// System.out.println(ci.getVarTable().size() + " var table size");
	        node.getId().apply(this);
	    }
	    if(node.getExtends() != null)
	    {
	        ClassInfo ci = table.get(node.getExtends().toString());
	        // System.out.println(" ----- ");
	    	// System.out.println(ci.getName().toString() +  " super class name!");
	    	// System.out.println(ci.getVarTable().size() + " var table size");
	        node.getId().apply(this);
	    }
	    {
	        List<PVarDecl> copy = new ArrayList<PVarDecl>(node.getVarDecl());

	        medium = node;
	        for(PVarDecl e : copy)
	        {
	        	// System.out.println(" about to go get the instance variables");
	        	// System.out.println("---");
	            e.apply(this);
	        }
	    }
	    {
	        List<PMethod> copy = new ArrayList<PMethod>(node.getMethod());
	        for(PMethod e : copy)
	        {
	        	// System.out.println("about to go get the methods");
	        	// System.out.println("---");
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
	    	// System.out.print("Type - >" + Types.toStr(node.getType()) + " name - > ");
	        

	        node.getType().apply(this);
	    }
	    if(node.getId() != null)
	    {
	    	// System.out.println(node.getId().toString());
	        node.getId().apply(this);
	        // after we come back from ID, we can go see if we already used it,

	        findAbove(node);
	    }
	    outAVarDecl(node);
	}

	private boolean findIt(AVarDecl needle, ListIterator<PVarDecl> hay){
		// ListIterator<PVarDecl> listIterator = hay.listIterator();
		System.out.println(needle.toString() + "  - node to string");
		
		while(hay.hasNext()){
			AVarDecl temp = (AVarDecl) hay.next();
			System.out.println(temp.toString() + "  - piece to string");
			if(needle == temp && needle.getType() == temp.getType()) {
				System.out.println("WOOO HOO! Found it with the same type!");
				System.out.println(needle.getType().toString());
				return true;
			}

		
		}
		return false;
	}
	private Node findAbove(AVarDecl node){
		// System.out.println("A Var Decl is asking about a parent");
		if(medium == null) return null;
		
		// we have to worry about this scope and the one above
		if(medium instanceof ASubClassDecl){
			// System.out.println(" ---- ASubClassDecl ---- ");
			// System.out.println("medium -> " + medium.getClass());
			// System.out.println( "node -> " + node.getId().toString());
			// System.out.println("Did we find it? " + findIt(node, ((ASubClassDecl)medium).getVarDecl()));
			ListIterator<PVarDecl> listIterator = ((ASubClassDecl)medium).getVarDecl().listIterator();
			if(findIt(node, listIterator)){
				System.out.println("--------> Var Redeclared in sub class!");

			}

			ClassInfo superId = table.get(((ASubClassDecl)medium).getExtends().toString());
			VarTable superVar = superId.getVarTable();

			System.out.println("SUPER CLASS ALERT");
			// System.out.println(superId.getName().toString());

			// if(findIt(node, superVar.getVarNames()))
				// System.out.println(" --------> Var Redeclared in super class!");
			// System.out.println(superId.getSuper().toString());
		}

		// we dont need to look here at all, its just a stm
		if(medium instanceof AMainClassDecl){
			System.out.println(" ---- AMainClassDecl ---- ");
		}

		// we only have to worry about this scope
		if(medium instanceof ABaseClassDecl){
			// System.out.println(" ---- ABaseClassDecl ---- ");
			// System.out.println("medium -> " + medium.getClass());
			// System.out.println( "node -> " + node.getId().toString());
			ListIterator<PVarDecl> listIterator = ((ABaseClassDecl)medium).getVarDecl().listIterator();
			if(findIt(node, listIterator))
				System.out.println("--------> Var Redeclared in base class!");


		}

		// we have to worry about the scope and then our class scope,
		// then we need to see if that class has a super class and look
		// there too
		if(medium instanceof AMethod){
			System.out.println(" ---- AMethod ---- ");
			System.out.println("medium -> " + medium.getClass());
			System.out.println( "node -> " + node.getId().toString());
		}

	    // if(node.parent() != null){
	    // 	System.out.println("We are not null, printing parent");
	    // 	// System.out.println(node.parent().getClass());
	    // 	Node parent = node.parent();
	    // 		if(parent.parent() != null){
	    // 			System.out.println("	Grandpa ??? ");
	    // 			System.out.println(parent.parent().getClass());
	    // 			Node g_pa = parent.parent();
	    // 			if(medium instanceof AMethod){
	    				
	    // 				// this means we need to look in the super class
	    // 				// of this subclass
	    // 				System.out.println("I am a god");
	    // 				// System.out.println(g_pa.parent().getClass());
	    // 			}
	    // 		}
	    // }
	    return null;
	}

	public void caseAMethod(AMethod node){
	    inAMethod(node);
	   // System.out.println("Lets see the method's return type");
	    System.out.println(Types.toStr(node.getType()));
	    if(node.getType() != null)
	    {
	    	// System.out.println("we are in!");
	        node.getType().apply(this);
	    }
	    if(node.getId() != null) {
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
	        // System.out.println("Size of PVarDecl - > " + copy.size());
	        for(PVarDecl e : copy)
	        {
	        	medium = node;
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

	// Formals are already in varDecl

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
    	System.out.println("THIS SHOULD BE CALLED!");
        inAReturnStmt(node);
        if(node.getExp() != null)
        {
        	System.out.println("about to send the carrier");
            node.getExp().apply(this);
            // when it comes back, 
            // we need to know what the exp resolves to

            System.out.println("The carrier came back!");
            System.out.println(carrier.getText());
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
            System.out.println("    id="+node.getId()+"  ");
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


public void caseAPlusExp(APlusExp node)
   {
      inAPlusExp(node);
      if(node.getLeft() != null)
      {
         node.getLeft().apply(this);
	 System.out.println(node.getLeft() instanceof ANumExp);
      }
      if(node.getRight() != null)
      {
         node.getRight().apply(this);
      }
      outAPlusExp(node);
   }
   
   
   public void caseAMinusExp(AMinusExp node)
   {
      inAMinusExp(node);
      if(node.getLeft() != null)
      {
         node.getLeft().apply(this);
      }
      if(node.getRight() != null)
      {
         node.getRight().apply(this);
      }
      outAMinusExp(node);
   }
   
   
   public void caseATimesExp(ATimesExp node)
   {
      inATimesExp(node);
      if(node.getLeft() != null)
      {
         node.getLeft().apply(this);
      }
      if(node.getRight() != null)
      {
         node.getRight().apply(this);
      }
      outATimesExp(node);
   }



    public void caseANumExp(ANumExp node)
    {
        inANumExp(node);
        if(node.getNum() != null)
        {
        	carrier = node.getNum();
		System.out.println(" YO "+node);
            // node.getNum().apply(this);

        }
        outANumExp(node);
    }

public void caseAIntType(AIntType node)
    {
	System.out.println(node);
   }
   
   
   public void caseABoolType(ABoolType node)
   {
   }
   
   

    public void caseATrueExp(ATrueExp node)
    {
        carrier = new TTrue();
    }
    
    public void caseAFalseExp(AFalseExp node)
    {
        carrier = new TFalse();
    }
   
   public void caseAIntArrayType(AIntArrayType node)
   {
   		System.out.println(node.toString() + " WE JUST WENT DOWN TO AN ARRAY");
       // carrier = new AIntArrayType();
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
