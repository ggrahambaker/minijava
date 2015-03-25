package symtable;

import minijava.node.PType;
import Mips.*;  // These two are needed for the IRT phase
import Arch.*;
import Tree.*;
import types.Types;



/** 
 * VarInfo records information about a single local variable.  We need
 * to know its type, for typechecking purposes, and we also store IRT
 * related info (the IRT code required to access it) -- or at least we
 * will once we get to the IRT checkpoint...
 */
public class VarInfo {
    private PType type;
    // private InFrame acc;
   
   public VarInfo(PType t) { 
       this.type = t; 
       //this.acc = new InFrame(frame.SP());
   }
   public PType getType() { return type; }
   public String toString() { return Types.toStr(type); }
   /* 
    * Stuff to add once we got the the IRT phase
    */
   private Access acc;
   public Access getAccess() { return acc; }
   public void setAccess(Access a) { acc = a; }
      
}


