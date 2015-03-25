package symtable;

import java.util.*;

import types.Types;
import minijava.node.PMethod;
import minijava.node.PVarDecl;
import minijava.node.TId;

import Mips.*;  // These two are needed for the IRT phase
import Arch.*;


/** 
 * A ClassInfo instance records infofmation about a single class.  It stores 
 * the name of its superclass (or null if there isn't one), a VarTable containing 
 * the class's instance variables, and a MethodTable containing information on 
 * the methods in the class, in addition to the name of the class itself.
 * 
 * @author Brad Richards
 */
 
public class ClassInfo {
   private TId className;         // TId holding our name, line number, etc.
   private TId superClass;        // Our superclass, if we have one
   private VarTable vars;         // A VarTable holding info on all instance vars
   private MethodTable methods;   // Table of info on methods

    /*
   We'll add these once we get to the IRT phase.  The IRTinfo object records
   the total number of words required for the instance variables in a class
   (including those we inherit).  
    */
   private Access info;
   public Access getIRTinfo() { return info; }
   public void setIRTinfo(Access i) { info = i; }


   /** 
    * The constructor takes all info associated with a subclass definition,
    * but can be passed null for unused fields in the case of a base or main
    * class declaration.  Names are passed as TId rather than String so we 
    * can retrieve line number, etc, from the token if necessary.
    * @param className  The name of the class
    * @param superClass The name of its superclass
    * @param vars       A list of all instance vars in the class
    * @param methods    A list of method descriptors
    */
   public ClassInfo(TId className, TId superClass,
                    LinkedList<PVarDecl> vars,
                    LinkedList<PMethod> methods) throws Exception { 

    System.out.println("class info");
      this.className = className;
      this.superClass = superClass;
      try{
	  this.vars = new VarTable(vars);}           // Populate table from list
      catch(VarClashException e){
	        System.out.println("throwing e");
       throw e;
     }
      try{
	  this.methods = new MethodTable(methods);}  // Ditto.
      catch(Exception e){
	  System.out.println("throwing methods");
    throw e;

  }
      this.info = new InFrame(0);

      String[] tempKeys = (String[])this.vars.getVarNames().toArray();
      for(int i=0; i<vars.size(); i++){

            Access temp = new InFrame((i*4));
	           this.vars.getInfo(tempKeys[i]).setAccess(temp);


      }
   }
   
   public TId getName() { return className; }
   public TId getSuper() { return superClass; }
   public VarTable getVarTable() { return vars; }
   public MethodTable getMethodTable() { return methods; }
   
   public void dump() {
       String s = className.getText();
       if(superClass != null) 
	       s+="  Extends: "+getSuper().getText();
       
       System.out.println("-------------------------------------");
       System.out.println("Class: " +s);
       System.out.println("-------------------------------------");
       vars.dump();
       methods.dump();
   } 
   
   public void dumpIRT() {
		// TODO:  You'll complete this one on the next checkpoint
       String s = className.getText();
       if(superClass != null) 
	       s+="  Extends: "+getSuper().getText();
       
       System.out.println("-------------------------------------");
       System.out.println("Class: " +s);
       System.out.println("-------------------------------------");
       vars.dumpIRT();
       methods.dumpIRT();

       

   } 
}
