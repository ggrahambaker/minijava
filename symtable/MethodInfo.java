package symtable;

import java.util.*;

import minijava.node.AFormal;
import minijava.node.PFormal;
import minijava.node.PType;
import minijava.node.PVarDecl;
import minijava.node.AVarDecl;
import minijava.node.TId;
import types.Types;
import Mips.*;  // These two are needed for the IRT phase
import Arch.*;
import Tree.*;


/** 
 * A MethodInfo instance records information about a single MiniJava method.
 * It contains references to the method's return type, formal parameters, and
 * its local variables, in addition to the method's name.  
 * @author Brad Richards
 */
public class MethodInfo {
   // ClassInfo parent;
   private PType retType;
   private TId name;
   private LinkedList<PFormal> formals;
   private VarTable locals;     // Contains entries for parameters as well
   private ClassInfo enclosing; // The class in which method is actually defined
   
   /*
    * Stuff we'll add for the IRT phase
    */
   private Access info;
   public Access getInfo() { return info; }
   public void setInfo(Access i) { info = i; }

   private Access static_link;
   public Access getLink() { return static_link; }
   public void setLink(Access i) { static_link = i; }
    
   
   /** 
    * The constructor stores away references to the return type and formals,
    * and builds a VarTable containing both the local variables and the 
    * formals.  If variable name clashes are found (within locals, formals,
    * or across locals and formals) we throw a VarClashException.
    * @param retType  The method's return type
    * @param name     The method's name (a TId, not a String)
    * @param formals  A list of the method's formal variables (params)
    * @param locals   A list of the method's local variables
    */

    public MethodInfo(PType retType, TId name,
		      LinkedList<PFormal> formals,
		      LinkedList<PVarDecl> locals) throws VarClashException {
	//TODO Fill in the guts of this method.
	this.retType = retType;
  String a = name.toString().replaceAll("\\s","");
	this.name = new TId(a);

	// make formals VarDel, add to 'locals' linked list
	LinkedList<PFormal> newformals = new LinkedList<PFormal>();
	for(PFormal f: formals){
	    boolean notfail = true;
	    String tempname = ((AFormal)f).toString();
	    for (PFormal j: newformals)
		if (((AFormal)j).toString().equals(tempname))
		    notfail=false;
	    if (notfail)
		newformals.add(f);
	    else{
		String msg = "VarClashException: " + ((AFormal)f).toString() + " redeclared on line " + name.getLine();
		throw new VarClashException(msg); // There was a clash
	    }}

      this.formals = newformals;
      
      LinkedList<PVarDecl> newLoc = new LinkedList<PVarDecl>();
      
      
      for(PFormal p: formals) {
          AFormal f = (AFormal)p.clone();
          AVarDecl temp = new AVarDecl();
          temp.setType(f.getType());
          temp.setId(f.getId());
          newLoc.add(temp);
      }
      for(PVarDecl aaa : locals){
          
          ((AVarDecl)aaa).setId(new TId(((AVarDecl)aaa).getId().toString().replaceAll("\\s","")));
          newLoc.add(aaa);
      }

      try {
          this.locals = new VarTable(newLoc);
        }
      catch(VarClashException e){
          System.out.println("we are about to die now");
          throw e;
      }

    }


    public void allocateMem(){

     
      // // for(PVarDecl a : newLoc)
      // //   System.out.println(a.toString());
      


        info = new InFrame(0);
        static_link = new InFrame(4);
        //return address here at 0
        //static link here at 4
        // System.out.println(this.locals.getVarNames() + " size of varz");
        String[] tempKeys = new String[this.locals.getVarNames().size()];
        this.locals.getVarNames().toArray(tempKeys);

        // for(int i = 0; i < tempKeys.length; i++)
        //     System.out.println(tempKeys[i] + " are we dumb ? " + i);

        for(int i=0; i<tempKeys.length; i++){

            this.locals.getInfo(tempKeys[i]).setAccess(new InFrame(8+(i*4)));
      
        }
    }

    
    

/* Accessors */   
    public TId getName() { return name; }
    public PType getRetType() { return retType; }
    public LinkedList<PFormal> getFormals() { return formals; }
    public VarTable getLocals() { return locals; }
    
    /** Print info about the return type, formals, and local variables.
     * It's OK if the formals appear in the local table as well.  In fact,
     * it's a <i>good</i> thing since this output will help us debug later if 
     * necessary, and we'll want to see exactly what's in the VarTable.
    */
    public void dump() {
	//TODO Fill in the guts of this method.
	// build string
	// localFormalClash ( arg:int ) : int
	System.out.print(getName().toString() + " (");
	AFormal s;
	ArrayList<PFormal> it = new ArrayList<PFormal>(getFormals());
	while(!it.isEmpty()){
            s=(AFormal)it.remove(0);
            System.out.print(" "+s.getId().toString()+" : "+Types.toStr(s.getType()));
            if(!it.isEmpty())
		System.out.print(", ");	       
	}
	if (getRetType() != null)
	    System.out.println(" ) : " + Types.toStr(getRetType()));
	else
	    System.out.println(" ) : void");	    
	locals.dump();
	System.out.println();
    }


    
    public void dumpIRT(){

	   //TODO Fill in the guts of this method -- but once we get to the IRT checkpoint
	     // String a = getName().toString();
      //  a = a.replaceAll("\\s","");

      // System.out.println("about to look! "+ getName().toString() + ".    " + getName().toString().equals("main "));
      // if(getName().toString().equals("main ")){
      //   System.out.println("gross");
      //   return;
      // }
	    

      System.out.print(getName().toString() + "(");
      ArrayList<PFormal> it = new ArrayList<PFormal>(getFormals());
      AFormal s;
      while(!it.isEmpty()){
        s=(AFormal)it.remove(0);
        System.out.print(" "+s.getId().toString()+": "+Types.toStr(s.getType()));
        
        if(!it.isEmpty())
          System.out.print(","); 
        }
        if (getRetType() != null)
            System.out.println(" ) : " + Types.toStr(getRetType()));
        else
            System.out.println(" ) : void");         

        System.out.println("Accessors for parameters and locals:");

        Set<String> tempKeys = this.locals.getVarNames();
        this.locals.dumpIRT();

        
	
    }

    
    
}
