package symtable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import minijava.node.AVarDecl;
import minijava.node.PType;
import minijava.node.PVarDecl;
import minijava.node.TId;

import Tree.*;

import java.util.*;
/** 
 * A VarTable records name and type information about a <i>collection</i> 
 * of variables.  An exception is thrown if we try to add a duplicate name.
 * @author Brad Richards
 */
public class VarTable {
    HashMap<String, VarInfo> table = new HashMap<String, VarInfo>();
    
   /** 
    * Constructor populates table from an initial list of VarDecls.
    * @param vars  A list of PVarDecl nodes from our AST.
     */
    public VarTable(LinkedList<PVarDecl> vars) throws VarClashException {
	for(PVarDecl var : vars) { 
	    try {
		put(((AVarDecl)var).getId(), ((AVarDecl) var).getType());}
	    catch(VarClashException e){
		throw e;}}	
    }
    
   /** Allow the option of adding individual entries as well. */
   public void put(TId id, PType type) throws VarClashException {
      String name = id.toString().replaceAll("\\s","");
      if (table.containsKey(name)) {
	  String msg = "VarClassException: "+name + " redeclared on line "+id.getLine();
         throw new VarClashException(msg); // There was a clash
      }
      else 
	  table.put(name, new VarInfo(type));    // No clash; add new binding
   }
   
    /** Lookup and return the type of a variable */
    public PType get(String name) {
	PType val = null;
	//TODO Fill in the guts of this method.
	if (table.containsKey(name))
	    val = table.get(name).getType();
	return val;
  }
   
   /** Lookup and return a variable's VarInfo record */
   public VarInfo getInfo(String name) {
      return table.get(name);
   }
   
   /** Return all var names in the table */
   public Set<String> getVarNames() {
      return table.keySet();
   }
   
   /** Returns the number of entries in the table */
   public int size() { return table.size(); }
   
   /** Print out the entire contents of the table */
   public void dump() {
      //TODO Fill in the guts of this method.
       String s;
       ArrayList<String> it = new ArrayList<String>(table.keySet());
       while(!it.isEmpty()){
	   s=it.remove(0);
	   System.out.println("  "+s+" : "+table.get(s).toString());
       }
       System.out.println();
   }
   
   public void dumpIRT() {
      //TODO Fill in the guts of this method -- but not until the IRT checkpoint
       String s;
       ArrayList<String> it = new ArrayList<String>(table.keySet());
       while(!it.isEmpty()){
	   s=it.remove(0);
	   System.out.println("  "+s+" : "+table.get(s).toString()); 
	   Print.prExp(table.get(s).getAccess().getTree());
       }
       System.out.println();

   }
}
