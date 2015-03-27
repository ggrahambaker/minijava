package symtable;

import java.util.*;

import types.Types;
import minijava.node.PMethod;
import minijava.node.PVarDecl;
import minijava.node.TId;

import Mips.*;  // These two are needed for the IRT phase
import Arch.*;
import Tree.*;

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
   private ClassInfo sup;

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

    
      this.className = className;
      this.superClass = superClass;
      try {
    this.vars = new VarTable(vars);}           // Populate table from list
      catch(VarClashException e){
          System.out.println("throwing e");
       throw e;
     }
      try {
    this.methods = new MethodTable(methods);
    }  // Ditto.
      catch(Exception e){
    System.out.println("throwing methods");
    throw e;

      }
   }

   public void addSuper(ClassInfo s){
    this.sup = s;
    System.out.println("added super!! " + this.sup.getName().getText());
   }
    
    public void allocateMem(){
      System.out.println(this.getName().toString() + " -- alloc mem");
      this.info = new InFrame(0);
      String[] tempKeys = new String[this.vars.getVarNames().size()];
      this.vars.getVarNames().toArray(tempKeys);
      for(int i=0; i<vars.size(); i++){
          Access temp = new InFrame((i*4));
          this.vars.getInfo(tempKeys[i]).setAccess(temp);
      }
    }

    public void allocateMemWithSuper(){
      
      this.info = new InFrame(0);
      int numVars = this.vars.getVarNames().size() + this.sup.getVarTable().getVarNames().size();
      String[] tempKeys = new String[numVars];
      Set<String> superVars = this.sup.getVarTable().getVarNames();
      Set<String> sub = this.vars.getVarNames();
     int i = 0;
      for(String a : superVars){
         tempKeys[i] = a;
        i++;
      }
      for(String a : sub){
      tempKeys[i] = a;
        i++;
      }

      
     // System.out.println(tempKeys.length + " size of created! " + i + " num varz");
      
      
     // System.out.println(sub.addAll(superVars) + " -- did this work?");
      
     
      for( i=0; i < tempKeys.length; i++){
          Access temp = new InFrame((i*4));
          if (this.vars.getInfo(tempKeys[i]) != null)
            this.vars.getInfo(tempKeys[i]).setAccess(temp);
          else
            this.sup.getVarTable().getInfo(tempKeys[i]).setAccess(temp);
      }
    }
    


    /*public void allocateMem(int off){
      System.out.println(this.getName().toString() + " -- alloc mem");
      this.info = new InFrame(0);
      String[] tempKeys = new String[this.vars.getVarNames().size()];
      this.vars.getVarNames().toArray(tempKeys);
      for(int i=0; i<vars.size(); i++){
          Access temp = new InFrame(((i+off)*4));
          this.vars.getInfo(tempKeys[i]).setAccess(temp);
      }
    }
    */
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

       String s = className.getText();


       if(superClass != null){

         s += "  Extends: "+getSuper().getText();



       } 
        

       
       System.out.println("-------------------------------------");
       System.out.println("Class: " +s);
       System.out.println("-------------------------------------");

       REG dest = new REG(new Reg("$dest"));
       if (this.vars.getVarNames().size()==0){
     MOVE static_link = new MOVE(dest, ((InFrame)info).getTree());
     ESEQ eseq = new ESEQ(static_link,dest);
     Print.prExp(eseq);
       }
       else {

        String[] tempKeys;
        if(superClass != null){
          int numVars = this.vars.getVarNames().size() + this.sup.getVarTable().getVarNames().size();
          tempKeys = new String[numVars];
          Set<String> superVars = this.sup.getVarTable().getVarNames();
          Set<String> sub = this.vars.getVarNames();
          int i = 0;
          for(String a : sub){
            tempKeys[i] = a;
            i++;
          }
          for(String a : superVars){
            tempKeys[i] = a;
            i++;
          }
        }
        else{
          int numVars = this.vars.getVarNames().size();
          tempKeys = new String[numVars];
          this.vars.getVarNames().toArray(tempKeys);
        }
        ExpList el = new ExpList(new CONST(tempKeys.length*4), null); 
       
        MOVE malloc = new MOVE(dest, new CALL(new NAME( new Label("malloc")),el  ));
        Stm temp;
        if (this.vars.getInfo(tempKeys[0]) != null)
          temp = new SEQ(malloc, new MOVE(new MEM(new BINOP(0, new REG(new Reg("base")), this.vars.getInfo(tempKeys[0]).getAccess().getTree())),new CONST(0)) );
        else
          temp = new SEQ(malloc, new MOVE(new MEM(new BINOP(0, new REG(new Reg("base")), this.sup.getVarTable().getInfo(tempKeys[0]).getAccess().getTree())),new CONST(0)) );
 
     for(int i=1;i<tempKeys.length;i++)
        if (this.vars.getInfo(tempKeys[i]) != null)
          temp = new SEQ(temp, new MOVE(new MEM(new BINOP(0, new REG(new Reg("base")), this.vars.getInfo(tempKeys[i]).getAccess().getTree())),new CONST(0)) );          
        else
         temp = new SEQ(temp, new MOVE(new MEM(new BINOP(0, new REG(new Reg("base")), this.sup.getVarTable().getInfo(tempKeys[i]).getAccess().getTree())),new CONST(0)) );
     
     Print.prExp(new ESEQ(temp, new REG(new Reg("$dest"))));

       }
       //new BINOP(0, new REG(new Reg("base")), table.get(s).getAccess().getTree()))
       // Print.prExp(((InFrame)info).getTree(new REG(new Reg("dest"))));


       System.out.println("-------------------------------------");
       System.out.println("Instance var accessors:");
       vars.dumpIRT();
       methods.dumpIRT();
       

   } 
}
