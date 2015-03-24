package symtable;

import minijava.analysis.DepthFirstAdapter;
import minijava.node.*;
import java.io.PrintWriter;

import java.util.*;

public class TypeCheckVisitor extends DepthFirstAdapter
{

    private ClassTable table;
    // for evaluating expressions
    private PType typeMedium;
    private String currentClass;
    private Node classInfo;
    private AMethod methodInfo;
    // private InitTable[] initTable;
    private InitTable init;
    private InitTable whileTable;
    private InitTable ifTable;
    private InitTable elseTable;
    private InitTable currentTable;
    private String initable;    // for passing ids
    private Node medium;


    private int lineNumber;


    public TypeCheckVisitor(ClassTable st) {
	    table = st;
	    classInfo = null;
      methodInfo = null;
	    medium = null;
      typeMedium = null;
      
      init = new InitTable();
      initable = "init";

    }



    public void caseAProgram(AProgram node) {
      List<PClassDecl> copy = new ArrayList<PClassDecl>(node.getClassDecl());
      for(PClassDecl e : copy){
          e.apply(this);
      } 
    }


    public void caseAMainClassDecl(AMainClassDecl node) {
        classInfo = node;
        currentClass = node.getId().getText();
        node.getId().apply(this);
        node.getStmt().apply(this);    
        // System.out.println("WOW, we made it. ");   
    }


    public void caseABaseClassDecl(ABaseClassDecl node)
    {
        
        classInfo = node;
        currentClass = node.getId().getText();
        node.getId().apply(this);
        List<PVarDecl> copyVar = new ArrayList<PVarDecl>(node.getVarDecl());
        for(PVarDecl e : copyVar) {
            e.apply(this);
        }
  
        List<PMethod> copyMethod = new ArrayList<PMethod>(node.getMethod());
        for(PMethod e : copyMethod) {
            e.apply(this);
        }       
    }

 
    public void caseASubClassDecl(ASubClassDecl node) {
        classInfo = node;
        currentClass = node.getId().getText();
        
        node.getId().apply(this);
    
        node.getExtends().apply(this);
    
        List<PVarDecl> copyVar = new ArrayList<PVarDecl>(node.getVarDecl());
        for(PVarDecl e : copyVar) {
            e.apply(this);
        }
    
        List<PMethod> copyMethod = new ArrayList<PMethod>(node.getMethod());
        for(PMethod e : copyMethod) {
            e.apply(this);
        }
    }


    public void caseAVarDecl(AVarDecl node) {



      PType varType = node.getType();
      if(!(varType instanceof ABoolType) && !(varType instanceof AIntType) && !(varType instanceof AIntArrayType)){
        // this means we have a user type, lets go check it out
        validateType(varType);
      }

      node.getType().apply(this);
      node.getId().apply(this);
    }


    public void caseAMethod(AMethod node) {
      methodInfo = node;
      node.getType().apply(this);
      node.getId().apply(this);

      currentTable = new InitTable();

      {      
        List<PFormal> copy = new ArrayList<PFormal>(node.getFormal());

          for(PFormal e : copy) {
              // System.out.println(((AFormal) e).getId().getText() + " - formal");
              currentTable.put(((AFormal) e).getId().getText(), Status.Yes);
              
              e.apply(this);
          }
        }
        List<PVarDecl> methodVars = new ArrayList<PVarDecl>(node.getVarDecl());
        {
            
            for(PVarDecl e : methodVars)
            {
                //init.put(((AVarDecl)e).getId().getText(), Status.Maybe);
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
        // is there like a compare sets? 
        // Set<String> a = currentTable.getVarNames();
        for(PVarDecl var : methodVars){

          AVarDecl varDec = (AVarDecl)var;
          TId varId = varDec.getId();
          String varName = varId.getText();
          // System.out.println(currentTable.get(varName) +" --  - " + varName);
          if(currentTable.get(varName) == null){
            System.out.println("Variable " + varName + " was never initialized");
            System.exit(1);
          }
        }
    }



    public void caseAReturnStmt(AReturnStmt node)
    {
       
        PType ty = getType((PExp)node.getExp());  
        PType retType = methodInfo.getType();

        boolean match = compareTypes(retType, ty);

        if(!match){
          System.out.println("Expecting " + Types.toStr(retType) + 
            ", got " + Types.toStr(ty) + 
            "on line " );
          System.exit(1);
        }
        node.getExp().apply(this);
        

    }


    public void caseAIfStmt(AIfStmt node)
    {

        PType expType = getType(node.getExp());
        if(!(expType instanceof ABoolType)) {
          System.out.println("Error: "+node.getExp().toString()+" is not of type boolean");
          System.exit(1);
        }


        node.getExp().apply(this);
        //String tableRefPlaceholder = initable;
        InitTable parent = currentTable;

        initable = "yes";
        ifTable = new InitTable(parent);
        currentTable = ifTable;
        node.getYes().apply(this);
        initable = "no";
        elseTable = new InitTable(parent);
        currentTable = elseTable;
        node.getNo().apply(this);

        parent.mergeIf(ifTable, elseTable);
        currentTable = parent;

        // default expect we are going back to init,
        // if we are not, while or if will change accordingly
        initable = "init";
    }


    public void caseAWhileStmt(AWhileStmt node)
    {

      PType expType = getType(node.getExp());
        if(!(expType instanceof ABoolType)) {
        System.out.println("Error: "+node.getExp().toString()+" is not of type boolean");
        System.exit(1);
      }

      InitTable parent = currentTable;
      initable = "while";
      whileTable = new InitTable(currentTable);
      currentTable = whileTable;
      node.getExp().apply(this);
      node.getStmt().apply(this);


      parent.mergeWhile(whileTable);
      currentTable = parent;
      // default expect we are going back to init,
      // if we are not, while or if will change accordingly
      initable = "init";

    }

      public void caseAAsmtStmt(AAsmtStmt node){

        PType id = getIdType(node.getId());
        //System.out.println(node.getId().getText() + " assign feedback " + node.getId().getLine());

        // get exp type
        PType exp = getType(node.getExp());


        // do they match?
        boolean match = compareTypes(id, exp);

        if(match){
          
        } else {
          System.out.println("the types dont match on line " + ((TId)node.getId()).getLine());
          System.exit(1);
        }

        //System.out.println(node.getId().getText() + " assign right before " + node.getId().getLine());
        addToInitTable("init", node.getId().getText());

        node.getId().apply(this);
        node.getExp().apply(this);
        // once we have type checked, lets upgrade the status
        // we want to add to our InitTable from here 
      }
    


    public void caseAArrayAsmtStmt(AArrayAsmtStmt node) 
    {
           PType id = getIdType(node.getId());
           if(! (id instanceof AIntArrayType))
            System.out.println("NOT AN ARRAY TYPE");

        
           PType index = getType(node.getIdx());
           // System.out.println(Types.toStr(index) + " -- index");
           PType value = getType(node.getVal());

           if((index instanceof AIntType) && (value instanceof AIntType))
              System.out.println("Thats great! it all works out");
            else {
                // throw TypeClashExecption(i);

              System.out.println("the types dont match on line " + ((TId)node.getId()).getLine());
              System.exit(1);
            }
            node.getId().apply(this);
            node.getIdx().apply(this);
            node.getVal().apply(this);

            
            addToInitTable(initable, node.getId().getText());
    }

    public void caseARefExp(ARefExp node){
      

      PExp exp = node.getName();
      TId objectID = ((AIdExp)exp).getId();
      PType name = getIdType(objectID);

      
      if(! (name instanceof AIntArrayType)){
        System.out.println("the id is not of type int array");
        System.exit(1);
      }

      PType index = getType(node.getIdx());
      if(!(index instanceof AIntType)){
        System.out.println("the index must be an int");
        System.exit(1);
      }
      node.getName().apply(this);
      node.getIdx().apply(this);
    }


    public void caseAAllocExp(AAllocExp node){
      PType exp = getType(node.getExp());
       if(! (exp instanceof AIntType)){
        System.out.println("the index must be an int");
        System.exit(1);
      }
      node.getExp().apply(this);
    }


     public void caseAAndExp(AAndExp node) {

      PExp left = node.getLeft();
      PExp right = node.getRight();
      PType l = getType(left);
      PType r = getType(right);

      // do they match?      

      if((l instanceof ABoolType) && (r instanceof ABoolType)){
        
      } else {
        System.out.println("the types dont match for and expr " );
        System.exit(1);
      }

      node.getLeft().apply(this);
      node.getRight().apply(this);
     }



public void caseALtExp(ALtExp node){
  PExp left = node.getLeft();
  PExp right = node.getRight();
  PType l = getType(left);
  PType r = getType(right);

  // do they match?      

  if((l instanceof AIntType) && (r instanceof AIntType)){
    
  } else {
    // System.out.println(node.get);
    System.out.println("the types dont match for lt expr " );
    System.exit(1);
  }

  node.getLeft().apply(this);
  node.getRight().apply(this);

}



public void caseAPlusExp(APlusExp node){
  PExp left = node.getLeft();
  PExp right = node.getRight();
  PType l = getType(left);
  PType r = getType(right);

  // do they match?      

  if((l instanceof AIntType) && (r instanceof AIntType)){
    
  } else {
    System.out.println("the types dont match for plus expr " );
    System.exit(1);
  }

  node.getLeft().apply(this);
  node.getRight().apply(this);


}
  public void caseAMinusExp(AMinusExp node){
    PExp left = node.getLeft();
    PExp right = node.getRight();
    PType l = getType(left);
    PType r = getType(right);

    // do they match?      

    if((l instanceof AIntType) && (r instanceof AIntType)){
      
    } else {
      System.out.println("the types dont match for minus expr " );
      System.exit(1);
    }

    node.getLeft().apply(this);
    node.getRight().apply(this);


  }


  public void caseATimesExp(ATimesExp node){
    PExp left = node.getLeft();
    PExp right = node.getRight();
    PType l = getType(left);
    PType r = getType(right);

    // do they match?      

    if((l instanceof AIntType) && (r instanceof AIntType)){
      
    } else {
      System.out.println("the types dont match for times expr " );
      System.exit(1);
    }

    node.getLeft().apply(this);
    node.getRight().apply(this);


  }

    public void caseANotExp(ANotExp node) {
        
      
      PExp exp = node.getExp();
      PType expType = getType(exp);
      

      // do they match?      

      if(expType instanceof ABoolType){
        
      } else {
        System.out.println("Expecting a boolean, got a " + Types.toStr(expType) + " on line "  );
        System.exit(1);
      }

    }

    public void caseALengthExp(ALengthExp node){
      node.getExp().apply(this);


      if(!(getType(node.getExp()) instanceof AIntArrayType)){
        
        System.out.println("A type " + Types.toStr(getType(node.getExp())) + " does not have length" );
        System.exit(1);
      }

    }

    public void caseAMethodExp(AMethodExp node){
        
            node.getObj().apply(this);
        
            node.getId().apply(this);

            
            if(!(node.getObj() instanceof AThisExp) && !(node.getObj() instanceof AMethodExp)&& !(node.getObj() instanceof ANewExp)){
              // we dont have to look if this is in init table
              AIdExp ob = (AIdExp)node.getObj();
              inTable(initable, ob.getId().getText(), ob.getId().getLine());
              
              // we need to look up the method and compare the args


              AUserType type = (AUserType)getIdType(ob.getId());

              ClassInfo ci = table.get(type.getId().getText());
              MethodTable mt = ci.getMethodTable();
              MethodInfo mi = mt.get(node.getId().getText());

              LinkedList<PFormal> formals = mi.getFormals();
              List<PExp> copy = new ArrayList<PExp>(node.getArgs());

              int i = 0;
              for(PFormal var : formals){
                PType formalType = ((AFormal)var).getType();
                PExp runVar = copy.get(i);
                PType valueType = getType(runVar);
                // System.out.println(Types.toStr(formalType) + " formal "+ Types.toStr(valueType) + " runVar ");
                // if(!Types.sameType(formalType, valueType)){
                //   System.out.println("Method param types dont match ");
                //   System.exit(1);

                // }
                i++;
              }
              
              for(PExp e : copy) {
               
                  e.apply(this);
              }
            }



    }

    public void caseAIdExp(AIdExp node){
      if(inTable(initable, node.getId().getText(), node.getId().getLine())){

      } else {
        System.out.println("Variable " + node.getId().getText() + " is used without being initialized on line " + node.getId().getLine());
        System.exit(1);
      }
    }


    private AUserType getThisType(){
      ClassInfo ci = table.get(currentClass);

      TId a = ci.getName();

      //System.out.println(a.getText() + " text??");
      return new AUserType(a);
    }
  

    private PType getIdType(TId id){

      
      ClassInfo ci = table.get(currentClass);

      MethodTable mt = ci.getMethodTable();
      MethodInfo mi = mt.get(methodInfo.getId().getText());
      VarTable vt = mi.getLocals();
    
      
      String ident = id.getText();
      ident = ident.replaceAll("\\s","");
      VarInfo vi = vt.getInfo(ident); 
      // if our var is in there, we can return the type!
      if(vi != null)
        return vi.getType();
      // if not, lets keep going up! now check instance varz

      vt = ci.getVarTable();
      vi = vt.getInfo(ident);
      // if var is here, lets return
      if(vi != null)
        return vi.getType();
      // if not, lets ask the super class
      //System.out.println(ci.getName().getText());
      // System.out.println(ident );
      if(ci.getSuper() == null){
        return null;
      }

      TId superCl = ci.getSuper();
      ci = table.get(superCl.getText());


      vt = ci.getVarTable();
      vi = vt.getInfo(ident);
      // if var is here, lets return
      if(vi != null)
        return vi.getType();
      // else, its no where to be found!!
      return null;
    }

    private boolean compareTypes(PType expected, PType actual){
      if((expected instanceof AUserType) && (actual instanceof AUserType)){

        ClassInfo ci = table.get(((AUserType)actual).getId().getText());
        // System.out.println(ci.getName().getText());
        
        if(Types.sameType(expected, actual)){
          return true;
        }
        if(ci.getSuper() != null){
          TId id = ci.getSuper();
          // System.out.println(" we are in ere!");
          AUserType user = new AUserType(id);
          return Types.sameType(expected, user);
        }
        
      }
      return Types.sameType(expected, actual);
    }



    private PType findRetType(PExp obj){


      AMethodExp methodCall = (AMethodExp) obj;
      
      PExp objExp = methodCall.getObj(); 

      PType obyType;
      if(objExp instanceof AThisExp){
         obyType = getThisType();
     } else if (objExp instanceof AMethodExp){
        obyType = findRetType(objExp);
      } else {
        TId objectID = ((AIdExp)objExp).getId();
        obyType = getIdType(objectID);
      }

      String objID = Types.toStr(obyType);

      Set<String> names = table.getClassNames();
      for(String name : names){
        if(name.equals(objID)){
         

          ClassInfo ci = table.get(name);
          MethodTable mt = ci.getMethodTable();

          MethodInfo mi = mt.get(methodCall.getId().getText());
  
          
          return mi.getRetType();
        }
      }
      // if we make it here, something is wrong.. 
      System.out.println("undefined method " + methodCall.getId().getText() + " in class " + objID);
      System.exit(1);


      return null;
      
    }

    private boolean validateType(PType objectType){
      Set<String> names = table.getClassNames();
      String objId = ((AUserType)objectType).getId().getText();
      // System.out.println("the id we are looking for " + objId);
      for(String name: names){
        if(name.equals(objId)){
          return true;
        }
      }
      // if we got this far, its undefined
      System.out.println("undefined type " + objId);
      System.exit(1);
      return false;
    }
    // pass in line?
    private PType findUserType(String objId){
      // what is the best way to do this? 
      Set<String> names = table.getClassNames();
      // String objId = obj.getId().getText();
      // System.out.println("the id we are looking for "+ obj.getId().getText());
      for(String name : names){
        //System.out.println(name + " name "+ objId + " objId " + name.equals(objId));
        if(name.equals(objId)){
        

          return new AUserType(new TId(name));
        
          
        }
      }
      // if we make it here, looks like this is an undefined type
      System.out.println("Undefined type " + objId + " on line " );
      System.exit(1);

      return null;
    }

    private boolean inTable(String tableName, String idName, int line){
      // System.out.println("can we use : " + idName + " on line "+ line );
      // System.out.println(currentTable.get(idName));
      // currentTable.print();
      //System.out.println(currentTable.get(idName) == Status.Maybe);
      if(currentTable.get(idName) == Status.Maybe){
        System.out.println("Warning: " + idName +" might not have been itialized on line " + line);
        return true;
      } else if(currentTable.get(idName) == Status.Yes){
        return true;
      } else {
        // we need to look in the local vars
        
        if(classInfo instanceof ABaseClassDecl){
          ABaseClassDecl baseClass = (ABaseClassDecl) classInfo; 
          LinkedList<PVarDecl> vars = baseClass.getVarDecl();
          for(PVarDecl var : vars){
            AVarDecl avar = (AVarDecl)var;
            //System.out.println(avar.getId().getText() + " a var text");
            // System.out.println(idName.equals(avar.getId().getText()));
            if(idName.equals(avar.getId().getText())){
              System.out.println("Variable " + idName + " has not been initialized on line " + line);
              System.exit(1);
              //return false;
            }
          }
          // System.out.println("Nonlocal variable " + idName + " might not be initialized on line " + line);
          return true;

        } else if (classInfo instanceof ASubClassDecl){
          ASubClassDecl baseClass = (ASubClassDecl) classInfo; 
          LinkedList<PVarDecl> vars = baseClass.getVarDecl();
          for(PVarDecl var : vars){
            AVarDecl avar = (AVarDecl)var;
            //System.out.println(avar.getId().getText() + " a sub text");
            //System.out.println(idName.equals(avar.getId().getText()));
            if(idName.equals(avar.getId().getText())){
              System.out.println("Variable " + idName + " has not been initialized on line " + line);
              return false;
            }
          }
          System.out.println("Nonlocal variable " + idName + " might not be initialized on line " + line);
          return true;
        }

        return false;
      }
    }

    private void addToInitTable(String tableName, String idName){
      currentTable.put(idName, Status.Yes);
    }


    private PType getType(PExp exp){
      
      if(exp instanceof AAndExp)
        return new ABoolType();
      if(exp instanceof ALtExp)
        return new ABoolType();
      if(exp instanceof APlusExp)
        return new AIntType();
      if(exp instanceof AMinusExp)
        return new AIntType();
      if(exp instanceof ATimesExp)
        return new AIntType();
      if(exp instanceof ANotExp)
        return new ABoolType();
      if(exp instanceof ARefExp)
        return new AIntType();
      if(exp instanceof ALengthExp)
        return new AIntType();
      if(exp instanceof AMethodExp)
        return findRetType(exp);
      if(exp instanceof ANumExp)
        return new AIntType();
      if(exp instanceof AIdExp)
        return getIdType(((AIdExp)exp).getId());
      if(exp instanceof ATrueExp)
        return new ABoolType();
      if(exp instanceof AFalseExp)
        return new ABoolType();
      if(exp instanceof AAndExp)
        return new ABoolType();
      if(exp instanceof AAllocExp){
        //System.out.println("ALLOC!");
        return new AIntArrayType();
      }
      if(exp instanceof ANewExp){
        ANewExp a = (ANewExp)exp;
        // System.out.println(a.getId().getText() + " a type");
        return findUserType(a.getId().getText());
      }
      if(exp instanceof ARefExp)
        return new AIntType();
      if(exp instanceof AThisExp)
        return getThisType();
      // if(exp instanceof AAndExp)
      //   return new ABoolType();
      else
        return null;
    }
}
