package symtable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import minijava.node.AMethod;
import minijava.node.PFormal;
import minijava.node.PMethod;
import minijava.node.PType;
import minijava.node.PVarDecl;
import minijava.node.TId;
import java.util.*;

/** 
 * This class maintains information on a <i>collection</i> of methods.  It
 * maps method names to MethodInfo records.
 * @author Brad Richards
 */
public class MethodTable {
   private HashMap<String, MethodInfo> table = new HashMap<String, MethodInfo>();
   
   /** 
    * The constructor is passed a list of PMethod nodes as constructed
    * by the parser.  It adds entries for each method in the list via 
    * the local put() method. 
    * @param methods A list of PMethod nodes
    */
   public MethodTable(LinkedList<PMethod> methods) throws Exception {
      //TODO Fill in the guts of this method.

       for(PMethod method : methods){
             try{
                 put(((AMethod) method).getId(), ((AMethod) method).getType(), ((AMethod) method).getFormal(), ((AMethod) method).getVarDecl());}
             catch(MethodClashException e){
                 //System.out.println("eeek about to die");
                 throw e;
               }   
       }
       
	   
	       //  throw new MethodClassError("MethodClashException: "+((AMethod) method).getId().toString()+" redeclaired on line ");}
   
   }
   
   /** 
    * This method adds a single entry to the table, with the method name as 
    * key and the appropriate MethodInfo structure as value.  If the method 
    * name already appears in the table, it should throw a MethodClashException.  
    * We might also encounter a VarClashException while building the MethodInfo 
    * structure, so either could be thrown by put().
    * @param id   The method's name (a TId, not a String)
    * @param retType  The method's return type
    * @param formals  A list of the method's formal variables (params)
    * @param locals   A list of the method's local variables
    */
    public void put(TId id, PType retType, 
                   LinkedList<PFormal> formals,
                   LinkedList<PVarDecl> locals) throws Exception {
      //TODO Fill in the guts of this method.
      String name = id.toString().replaceAll("\\s","");
    
      if (table.containsKey(name)) {
         String msg = "MethodClashException: "+name + " redeclared on line " + id.getLine();
         System.out.println("we are ablout the throw metho clash");
         throw new MethodClashException(msg); // There was a clash
      }
      else {
        
	     table.put(name, new MethodInfo(retType, id, formals, locals));    // No clash; add new binding
      }
   }
   
   /** Lookup and return the MethodInfo for the specified method */
   public MethodInfo get(String name) {
      return table.get(name);
   }
   /** Return all method names in the table */
   public Set<String> getMethodNames() {
      return table.keySet();
   }
   
   /** 
    * Print out info on all methods in the table.  Don't forget that
    * MethodInfo structures already know how to dump themselves.
    */
   public void dump() {
      //TODO Fill in the guts of this method.
       ArrayList<MethodInfo> it = new ArrayList<MethodInfo>(table.values());
       while(!it.isEmpty()) 
      	   it.remove(0).dump();
   }
   
   public void dumpIRT() {
      //TODO Fill in the guts of this method -- but not until IRT checkpoint

       ArrayList<MethodInfo> it = new ArrayList<MethodInfo>(table.values());
       while(!it.isEmpty()) 
      	   it.remove(0).dumpIRT();


   }
}
