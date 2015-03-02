package symtable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import minijava.node.AMethod;
import minijava.node.PMethod;
import minijava.node.PVarDecl;
import minijava.node.TId;

/** 
 * A ClassTable records information about a COLLECTION of class definitions.
 */
public class ClassTable {
   HashMap<String, ClassInfo> table = new HashMap<String, ClassInfo>();
   
   /** 
    * This method adds a new table entry for class "id".  It will throw a
    * ClassClashException if the new class name is already in the table,
    * and might also pass along Var or Method clash exceptions encountered
    * while processing the lists of instance variables and methods.  (This
    * method doesn't inspect the lists, but the constructor for ClassInfo
    * does.)
    * 
    * @param id         The name of the class (a TId, not a String)
    * @param extendsId  The name of its superclass (or null)
    * @param vars       A list of the class's instance variables
    * @param methods    A list of the methods in the class
    */
   public void put(TId id, 
                   TId extendsId,
                   LinkedList<PVarDecl> vars,
                   LinkedList<PMethod> methods) throws Exception {
      String name = id.getText();
      //if name is already in the table, throw exception
      if(table.containsKey(name)){
    	  String msg = name + " redeclared on line " + id.getLine();
          throw new ClassClashException(msg); // There was a clash
      } else{ //otherwise, try to add the class
    	  try { 
    	     ClassInfo newClass = new ClassInfo(id, extendsId, vars, methods);
    	     table.put(name, newClass);
    	  } catch(Exception e){ //pass along any exceptions that occur when making the class
    		  throw e;
    	  }
      }
   }
   
   public void putMain(TId className, String methodName) throws Exception {
	   try {
		  // handle the method list
	      LinkedList<PMethod> methodList = new LinkedList<PMethod>();
	      methodList.add(new AMethod(null, new TId(methodName), null, null, null));
	      // generate the appropriate class info
	      TId name = className;
	      ClassInfo main = new ClassInfo(name, null, null, methodList);
	      // check for duplicates and add the main class info if it is good
	      if(table.containsKey(className)){
        	  throw new ClassClashException("The name " + name.getText() + " is already used at line " + name.getPos() + ". Try another name for this class");
          } else {
        	  table.put(className, main);
	      }
	   } catch (Exception e){ //throw any exceptions that occur
	       throw e;
	   }
	   
   }
   
   /** Lookup and return the ClassInfo record for the specified class */
   public ClassInfo get(String id) {
      return table.get(id);
   }
   
   /** Return all method names in the table */
   public Set<String> getClassNames() {
      return table.keySet();
   }
   
   /** dump prints info on each of the classes in the table */
   public void dump() {
      for(String name : table.keySet()){
    	  table.get(name).dump();
	  System.out.println();
      }
   }
   
      
   /** dump prints info on each of the classes in the table and 
    * displays IRT info as well. 
    * @param dot	Are we generating output for dot?
    */
   public void dumpIRT(boolean dot) {
      //TODO Fill in the guts of this method -- but not until the IRT checkpoint.
   }
}
