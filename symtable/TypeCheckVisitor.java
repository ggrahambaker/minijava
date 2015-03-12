package symtable;

import minijava.analysis.DepthFirstAdapter;
import minijava.node.*;
import java.io.PrintWriter;

import java.util.*;

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

    public void inStart(Start node)
    {
        defaultIn(node);
    }

    public void outStart(Start node)
    {
        defaultOut(node);
    }

    public void defaultIn(@SuppressWarnings("unused") Node node)
    {
        // Do nothing
    }

    public void defaultOut(@SuppressWarnings("unused") Node node)
    {
        // Do nothing
    }

    @Override
    public void caseStart(Start node)
    {
        inStart(node);
        node.getPProgram().apply(this);
        node.getEOF().apply(this);
        outStart(node);
    }

    public void inAProgram(AProgram node)
    {
        defaultIn(node);
    }

    public void outAProgram(AProgram node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAProgram(AProgram node)
    {
        inAProgram(node);
        {
            List<PClassDecl> copy = new ArrayList<PClassDecl>(node.getClassDecl());
            for(PClassDecl e : copy)
            {
                e.apply(this);
            }
        }
        outAProgram(node);
    }

    public void inAMainClassDecl(AMainClassDecl node)
    {
        defaultIn(node);
    }

    public void outAMainClassDecl(AMainClassDecl node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAMainClassDecl(AMainClassDecl node)
    {
        inAMainClassDecl(node);
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        if(node.getStmt() != null)
        {
            node.getStmt().apply(this);
        }
        outAMainClassDecl(node);
    }

    public void inABaseClassDecl(ABaseClassDecl node)
    {
        defaultIn(node);
    }

    public void outABaseClassDecl(ABaseClassDecl node)
    {
        defaultOut(node);
    }

    @Override
    public void caseABaseClassDecl(ABaseClassDecl node)
    {
        inABaseClassDecl(node);
        if(node.getId() != null)
        {
            node.getId().apply(this);
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
        outABaseClassDecl(node);
    }

    public void inASubClassDecl(ASubClassDecl node)
    {
        defaultIn(node);
    }

    public void outASubClassDecl(ASubClassDecl node)
    {
        defaultOut(node);
    }

    @Override
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

    public void inAVarDecl(AVarDecl node)
    {
        defaultIn(node);
    }

    public void outAVarDecl(AVarDecl node)
    {
        defaultOut(node);
    }

    @Override
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

    public void inAMethod(AMethod node)
    {
        defaultIn(node);
    }

    public void outAMethod(AMethod node)
    {
        defaultOut(node);
    }

    @Override
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

    public void inAFormal(AFormal node)
    {
        defaultIn(node);
    }

    public void outAFormal(AFormal node)
    {
        defaultOut(node);
    }

    @Override
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

    public void inAIntType(AIntType node)
    {
        defaultIn(node);
    }

    public void outAIntType(AIntType node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAIntType(AIntType node)
    {
        inAIntType(node);
        outAIntType(node);
    }

    public void inABoolType(ABoolType node)
    {
        defaultIn(node);
    }

    public void outABoolType(ABoolType node)
    {
        defaultOut(node);
    }

    @Override
    public void caseABoolType(ABoolType node)
    {
        inABoolType(node);
        outABoolType(node);
    }

    public void inAIntArrayType(AIntArrayType node)
    {
        defaultIn(node);
    }

    public void outAIntArrayType(AIntArrayType node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAIntArrayType(AIntArrayType node)
    {
        inAIntArrayType(node);
        outAIntArrayType(node);
    }

    public void inAUserType(AUserType node)
    {
        defaultIn(node);
    }

    public void outAUserType(AUserType node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAUserType(AUserType node)
    {
        inAUserType(node);
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        outAUserType(node);
    }

    public void inAReturnStmt(AReturnStmt node)
    {
        defaultIn(node);
    }

    public void outAReturnStmt(AReturnStmt node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAReturnStmt(AReturnStmt node)
    {
        inAReturnStmt(node);
        if(node.getExp() != null)
        {
            node.getExp().apply(this);
            
        }
        outAReturnStmt(node);
    }

    public void inABlockStmt(ABlockStmt node)
    {
        defaultIn(node);
    }

    public void outABlockStmt(ABlockStmt node)
    {
        defaultOut(node);
    }

    @Override
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

    public void inAIfStmt(AIfStmt node)
    {
        defaultIn(node);
    }

    public void outAIfStmt(AIfStmt node)
    {
        defaultOut(node);
    }

    @Override
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

    public void inAWhileStmt(AWhileStmt node)
    {
        defaultIn(node);
    }

    public void outAWhileStmt(AWhileStmt node)
    {
        defaultOut(node);
    }

    @Override
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

    public void inAPrintStmt(APrintStmt node)
    {
        defaultIn(node);
    }

    public void outAPrintStmt(APrintStmt node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAPrintStmt(APrintStmt node)
    {
        inAPrintStmt(node);
        if(node.getExp() != null)
        {
            node.getExp().apply(this);
        }
        outAPrintStmt(node);
    }

    public void inAAsmtStmt(AAsmtStmt node)
    {
        defaultIn(node);
    }

    public void outAAsmtStmt(AAsmtStmt node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAAsmtStmt(AAsmtStmt node)
    {
        // if we are in here, we have to be in a method. 
        // so we want to see if we are in a base, or sub method
        // we know parent is method, we dont know about its parent

        if(node.parent().parent() instanceof ABaseClassDecl){
          System.out.println(node.parent().parent().getClass() +  "   : ABaseClassDecl");
        } else {
          System.out.println(node.parent().parent().getClass() +  "   : ASubClassDecl");
        }
        inAAsmtStmt(node);
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        if(node.getExp() != null)
        {
            node.getExp().apply(this);
        }
        // first get class info

             
        // table.get(node.parent());

        outAAsmtStmt(node);
    }

    public void inAArrayAsmtStmt(AArrayAsmtStmt node)
    {
        defaultIn(node);
    }

    public void outAArrayAsmtStmt(AArrayAsmtStmt node)
    {
        defaultOut(node);
    }

    @Override
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

    public void inAAndExp(AAndExp node)
    {
        defaultIn(node);
    }

    public void outAAndExp(AAndExp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAAndExp(AAndExp node)
    {
        inAAndExp(node);
        if(node.getLeft() != null)
	    if(node.getLeft() instanceof ATrueExp || node.getLeft() instanceof AFalseExp)	    
		node.getLeft().apply(this);
	    else {
		System.out.println("Error: "+node.getLeft().toString()+" is not of type boolean");
		System.exit(1);}	
	if(node.getRight() != null)
	    if(node.getRight() instanceof ATrueExp || node.getLeft() instanceof AFalseExp)
		node.getRight().apply(this);
	    else {
		System.out.println("Error: "+node.getRight().toString()+" is not of type boolean");
		System.exit(1);
	    }	
	outAAndExp(node);
    }

    public void inALtExp(ALtExp node)
    {
        defaultIn(node);
    }

    public void outALtExp(ALtExp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseALtExp(ALtExp node)
    {
        inALtExp(node);
        if(node.getLeft() != null)
	    if(node.getLeft() instanceof ATrueExp || node.getLeft() instanceof AFalseExp)	    
		node.getLeft().apply(this);
	    else {
		System.out.println("Error: "+node.getLeft().toString()+" is not of type boolean");
		System.exit(1);}	
	if(node.getRight() != null)
	    if(node.getRight() instanceof ATrueExp || node.getLeft() instanceof AFalseExp)
		node.getRight().apply(this);
	    else {
		System.out.println("Error: "+node.getRight().toString()+" is not of type boolean");
		System.exit(1);	    }	
        outALtExp(node);
    }

    public void inAPlusExp(APlusExp node)
    {
        defaultIn(node);
    }

    public void outAPlusExp(APlusExp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAPlusExp(APlusExp node)
    {
        inAPlusExp(node);
<<<<<<< HEAD
	Node left = null;
	Node right = null;
        if(node.getLeft() != null){
	    node.getLeft().apply(this);
	    left = medium;
	    if(!(left instanceof ANumExp)){
		System.out.println("Error: "+node.getLeft().toString()+" is not of type int");
		System.exit(1);}}
	if(node.getRight() != null){
	    node.getRight().apply(this);
	    right = medium;
	    if(!(right instanceof ANumExp)){
          	System.out.println("Error: "+node.getRight().toString()+" is not of type int");
		System.exit(1);
	    }
	}
	int result = Integer.parseInt(((ANumExp)medium).getNum().getText()) +Integer.parseInt(((ANumExp)left).getNum().getText());
	//System.out.println(result + " -> the result!"); 
	TNum con = new TNum(Integer.toString(result));
	medium = new ANumExp(con);
	//System.out.println(medium.toString());
	//System.out.println(medium.getClass());
	outAPlusExp(node);
=======
        
        Node left = null;
        if(node.getLeft() != null)
	       if(node.getLeft() instanceof ANumExp){
          node.getLeft().apply(this);
          left = medium;
         }
	       else {
		      System.out.println("Error: "+node.getLeft().toString()+" is not of type int");
		      System.exit(1);}		
        if(node.getRight() != null)
          if(node.getRight() instanceof ANumExp){
            node.getRight().apply(this);
            int result = Integer.parseInt(((ANumExp)medium).getNum().getText()) +Integer.parseInt(((ANumExp)left).getNum().getText());
            System.out.println(result + " -> the result!"); 
            TNum con = new TNum(Integer.toString(result));
            medium = new ANumExp(con);
            System.out.println(medium.toString());
            System.out.println(medium.getClass());
          } else {
          	System.out.println("Error: "+node.getRight().toString()+" is not of type int");
            System.exit(1);
          }
        outAPlusExp(node);
>>>>>>> 6415276ccd876d6e17026d3a494593b7557cd781
    }

    public void inAMinusExp(AMinusExp node)
    {
        defaultIn(node);
    }

    public void outAMinusExp(AMinusExp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAMinusExp(AMinusExp node)
    {
        inAMinusExp(node);
        if(node.getLeft() != null)
	    if(node.getLeft() instanceof ANumExp)
		node.getLeft().apply(this);
	    else {
		System.out.println("Error: "+node.getLeft().toString()+" is not of type int");
		System.exit(1);}		
	if(node.getRight() != null)
	    if(node.getRight() instanceof ANumExp)
		node.getRight().apply(this);
	    else {
		System.out.println("Error: "+node.getRight().toString()+" is not of type int");
		System.exit(1);}
        outAMinusExp(node);
    }

    public void inATimesExp(ATimesExp node)
    {
        defaultIn(node);
    }

    public void outATimesExp(ATimesExp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseATimesExp(ATimesExp node)
    {
        inATimesExp(node);
        if(node.getLeft() != null)

	    if(node.getLeft() instanceof ANumExp)
		node.getLeft().apply(this);
	    else {
		System.out.println("Error: "+node.getLeft().toString()+" is not of type int");
		System.exit(1);}		
	if(node.getRight() != null)
	    if(node.getRight() instanceof ANumExp)
		node.getRight().apply(this);
	    else {
		System.out.println("Error: "+node.getRight().toString()+" is not of type int");
		System.exit(1);}
    	outATimesExp(node);
    }

    public void inANotExp(ANotExp node)
    {
        defaultIn(node);
    }

    public void outANotExp(ANotExp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseANotExp(ANotExp node)
    {
        inANotExp(node);
        if(node.getExp() != null)
        {
	    if(node.getExp() instanceof ATrueExp || node.getExp() instanceof AFalseExp)
		node.getExp().apply(this);
	    else {
		System.out.println("Error: "+node.getExp().toString()+" is not of type boolean");
		System.exit(1);}
        }
        outANotExp(node);
    }

    public void inARefExp(ARefExp node)
    {
        defaultIn(node);
    }

    public void outARefExp(ARefExp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseARefExp(ARefExp node)
    {
        inARefExp(node);
        if(node.getName() != null)
        {
            node.getName().apply(this);
        }
        if(node.getIdx() != null)
        {
            node.getIdx().apply(this);
        }
        outARefExp(node);
    }

    public void inALengthExp(ALengthExp node)
    {
        defaultIn(node);
    }

    public void outALengthExp(ALengthExp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseALengthExp(ALengthExp node)
    {
        inALengthExp(node);
        if(node.getExp() != null)
        {
            node.getExp().apply(this);
        }
        outALengthExp(node);
    }

    public void inAMethodExp(AMethodExp node)
    {
        defaultIn(node);
    }

    public void outAMethodExp(AMethodExp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAMethodExp(AMethodExp node)
    {
        inAMethodExp(node);
        if(node.getObj() != null)
        {
            node.getObj().apply(this);
        }
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        {
            List<PExp> copy = new ArrayList<PExp>(node.getArgs());
            for(PExp e : copy)
            {
                e.apply(this);
            }
        }
        outAMethodExp(node);
    }

    public void inANumExp(ANumExp node)
    {
        defaultIn(node);
    }

    public void outANumExp(ANumExp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseANumExp(ANumExp node)
    {
        inANumExp(node);
        if(node.getNum() != null)
        {
            node.getNum().apply(this);
            medium = node;
        }
        outANumExp(node);
    }

    public void inAIdExp(AIdExp node)
    {
        defaultIn(node);
    }

    public void outAIdExp(AIdExp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAIdExp(AIdExp node)
    {
        inAIdExp(node);
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        outAIdExp(node);
    }

    public void inATrueExp(ATrueExp node)
    {
        defaultIn(node);
    }

    public void outATrueExp(ATrueExp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseATrueExp(ATrueExp node)
    {
        inATrueExp(node);
        medium = node;
        outATrueExp(node);
    }

    public void inAFalseExp(AFalseExp node)
    {
        defaultIn(node);
    }

    public void outAFalseExp(AFalseExp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAFalseExp(AFalseExp node)
    {
        inAFalseExp(node);
        medium = node;
        outAFalseExp(node);
    }

    public void inAThisExp(AThisExp node)
    {
        defaultIn(node);
    }

    public void outAThisExp(AThisExp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAThisExp(AThisExp node)
    {
        inAThisExp(node);
        outAThisExp(node);
    }

    public void inAAllocExp(AAllocExp node)
    {
        defaultIn(node);
    }

    public void outAAllocExp(AAllocExp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAAllocExp(AAllocExp node)
    {
        inAAllocExp(node);
        if(node.getExp() != null)
        {
            node.getExp().apply(this);
        }
        outAAllocExp(node);
    }

    public void inANewExp(ANewExp node)
    {
        defaultIn(node);
    }

    public void outANewExp(ANewExp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseANewExp(ANewExp node)
    {
        inANewExp(node);
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        outANewExp(node);
    }
}
/*
package symtable;

import minijava.analysis.DepthFirstAdapter;
import minijava.node.*;
import java.io.PrintWriter;
import java.util.*;

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

    public void inStart(Start node)
    {
        defaultIn(node);
    }

    public void outStart(Start node)
    {
        defaultOut(node);
    }

    public void defaultIn(@SuppressWarnings("unused") Node node)
    {
        // Do nothing
    }

    public void defaultOut(@SuppressWarnings("unused") Node node)
    {
        // Do nothing
    }

    @Override
    public void caseStart(Start node)
    {
        inStart(node);
        node.getPProgram().apply(this);
        node.getEOF().apply(this);
        outStart(node);
    }

    public void inAProgram(AProgram node)
    {
        defaultIn(node);
    }

    public void outAProgram(AProgram node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAProgram(AProgram node)
    {
        inAProgram(node);
        {
            List<PClassDecl> copy = new ArrayList<PClassDecl>(node.getClassDecl());
            for(PClassDecl e : copy)
            {
                e.apply(this);
            }
        }
        outAProgram(node);
    }

    public void inAMainClassDecl(AMainClassDecl node)
    {
        defaultIn(node);
    }

    public void outAMainClassDecl(AMainClassDecl node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAMainClassDecl(AMainClassDecl node)
    {
        inAMainClassDecl(node);
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        if(node.getStmt() != null)
        {
            node.getStmt().apply(this);
        }
        outAMainClassDecl(node);
    }

    public void inABaseClassDecl(ABaseClassDecl node)
    {
        defaultIn(node);
    }

    public void outABaseClassDecl(ABaseClassDecl node)
    {
        defaultOut(node);
    }

    @Override
    public void caseABaseClassDecl(ABaseClassDecl node)
    {
        inABaseClassDecl(node);
        if(node.getId() != null)
        {
            node.getId().apply(this);
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
        outABaseClassDecl(node);
    }

    public void inASubClassDecl(ASubClassDecl node)
    {
        defaultIn(node);
    }

    public void outASubClassDecl(ASubClassDecl node)
    {
        defaultOut(node);
    }

    @Override
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

    public void inAVarDecl(AVarDecl node)
    {
        defaultIn(node);
    }

    public void outAVarDecl(AVarDecl node)
    {
        defaultOut(node);
    }

    @Override
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

    public void inAMethod(AMethod node)
    {
        defaultIn(node);
    }

    public void outAMethod(AMethod node)
    {
        defaultOut(node);
    }

    @Override
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

    public void inAFormal(AFormal node)
    {
        defaultIn(node);
    }

    public void outAFormal(AFormal node)
    {
        defaultOut(node);
    }

    @Override
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

    public void inAIntType(AIntType node)
    {
        defaultIn(node);
    }

    public void outAIntType(AIntType node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAIntType(AIntType node)
    {
        inAIntType(node);
        outAIntType(node);
    }

    public void inABoolType(ABoolType node)
    {
        defaultIn(node);
    }

    public void outABoolType(ABoolType node)
    {
        defaultOut(node);
    }

    @Override
    public void caseABoolType(ABoolType node)
    {
        inABoolType(node);
        outABoolType(node);
    }

    public void inAIntArrayType(AIntArrayType node)
    {
        defaultIn(node);
    }

    public void outAIntArrayType(AIntArrayType node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAIntArrayType(AIntArrayType node)
    {
        inAIntArrayType(node);
        outAIntArrayType(node);
    }

    public void inAUserType(AUserType node)
    {
        defaultIn(node);
    }

    public void outAUserType(AUserType node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAUserType(AUserType node)
    {
        inAUserType(node);
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        outAUserType(node);
    }

    public void inAReturnStmt(AReturnStmt node)
    {
        defaultIn(node);
    }

    public void outAReturnStmt(AReturnStmt node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAReturnStmt(AReturnStmt node)
    {
        inAReturnStmt(node);
        if(node.getExp() != null)
        {
            node.getExp().apply(this);
        }
        outAReturnStmt(node);
    }

    public void inABlockStmt(ABlockStmt node)
    {
        defaultIn(node);
    }

    public void outABlockStmt(ABlockStmt node)
    {
        defaultOut(node);
    }

    @Override
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

    public void inAIfStmt(AIfStmt node)
    {
        defaultIn(node);
    }

    public void outAIfStmt(AIfStmt node)
    {
        defaultOut(node);
    }

    @Override
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

    public void inAWhileStmt(AWhileStmt node)
    {
        defaultIn(node);
    }

    public void outAWhileStmt(AWhileStmt node)
    {
        defaultOut(node);
    }

    @Override
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

    public void inAPrintStmt(APrintStmt node)
    {
        defaultIn(node);
    }

    public void outAPrintStmt(APrintStmt node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAPrintStmt(APrintStmt node)
    {
        inAPrintStmt(node);
        if(node.getExp() != null)
        {
            node.getExp().apply(this);
        }
        outAPrintStmt(node);
    }

    public void inAAsmtStmt(AAsmtStmt node)
    {
        defaultIn(node);
    }

    public void outAAsmtStmt(AAsmtStmt node)
    {
        defaultOut(node);
    }

    @Override
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

    public void inAArrayAsmtStmt(AArrayAsmtStmt node)
    {
        defaultIn(node);
    }

    public void outAArrayAsmtStmt(AArrayAsmtStmt node)
    {
        defaultOut(node);
    }

    @Override
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

    public void inAAndExp(AAndExp node)
    {
        defaultIn(node);
    }

    public void outAAndExp(AAndExp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAAndExp(AAndExp node)
    {
        inAAndExp(node);
        if(node.getLeft() != null)
	    if(node.getLeft() instanceof ATrueExp || node.getLeft() instanceof AFalseExp)	    
		node.getLeft().apply(this);
	    else {
		System.out.println("Error: "+node.getLeft().toString()+" is not of type boolean");
		System.exit(1);}	
	if(node.getRight() != null)
	    if(node.getRight() instanceof ATrueExp || node.getLeft() instanceof AFalseExp)
		node.getRight().apply(this);
	    else {
		System.out.println("Error: "+node.getRight().toString()+" is not of type boolean");
		System.exit(1);
	    }	
	outAAndExp(node);
    }

    public void inALtExp(ALtExp node)
    {
        defaultIn(node);
    }

    public void outALtExp(ALtExp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseALtExp(ALtExp node)
    {
        inALtExp(node);
        if(node.getLeft() != null)
	    if(node.getLeft() instanceof ATrueExp || node.getLeft() instanceof AFalseExp)	    
		node.getLeft().apply(this);
	    else {
		System.out.println("Error: "+node.getLeft().toString()+" is not of type boolean");
		System.exit(1);}	
	if(node.getRight() != null)
	    if(node.getRight() instanceof ATrueExp || node.getLeft() instanceof AFalseExp)
		node.getRight().apply(this);
	    else {
		System.out.println("Error: "+node.getRight().toString()+" is not of type boolean");
		System.exit(1);	    }	
        outALtExp(node);
    }

    public void inAPlusExp(APlusExp node)
    {
        defaultIn(node);
    }

    public void outAPlusExp(APlusExp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAPlusExp(APlusExp node)
    {
        inAPlusExp(node);
        if(node.getLeft() != null){
	    node.getLeft().apply(this);
	    if ( ! (medium instanceof ANumExp)){
		System.out.println("Error: "+node.getLeft().toString()+" is not of type int");
		System.exit(1);}}		
	if(node.getRight() != null){
	    node.getRight().apply(this);
	    if( ! (medium instanceof ANumExp)){
		System.out.println("Error: "+node.getRight().toString()+" is not of type int");
		System.exit(1);}}
	medium = new ANumExp(new TNum(Integer.toString(Integer.parseInt(node.getLeft().toString()) * Integer.parseInt(node.getRight().toString()))));
        outAPlusExp(node);
    }

    public void inAMinusExp(AMinusExp node)
    {
        defaultIn(node);
    }

    public void outAMinusExp(AMinusExp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAMinusExp(AMinusExp node)
    {
        inAMinusExp(node);
        if(node.getLeft() != null){
	    node.getLeft().apply(this);
	    if ( ! (medium instanceof ANumExp)){
		System.out.println("Error: "+node.getLeft().toString()+" is not of type int");
		System.exit(1);}}		
	if(node.getRight() != null){
	    node.getRight().apply(this);
	    if( ! (medium instanceof ANumExp)){
		System.out.println("Error: "+node.getRight().toString()+" is not of type int");
		System.exit(1);}}
	medium = new ANumExp(new TNum(Integer.toString(Integer.parseInt(node.getLeft().toString()) - Integer.parseInt(node.getRight().toString()))));
        outAMinusExp(node);
    }

    public void inATimesExp(ATimesExp node)
    {
        defaultIn(node);
    }

    public void outATimesExp(ATimesExp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseATimesExp(ATimesExp node)
    {
        inATimesExp(node);
        if(node.getLeft() != null){
	    Node left;
	    node.getLeft().apply(this);
	    left = (Node)medium.clone();
	    if ( ! (left instanceof ANumExp)){
		System.out.println("Error: "+node.getLeft().toString()+" is not of type int");
		System.exit(1);}}		
	if(node.getRight() != null){
	    node.getRight().apply(this);
	    if( ! (medium instanceof ANumExp)){
		System.out.println("Error: "+node.getRight().toString()+" is not of type int");
		System.exit(1);}}
	medium = new ANumExp(new TNum(Integer.toString(Integer.parseInt(node.getLeft().toString()) * Integer.parseInt(node.getRight().toString()))));
    	outATimesExp(node);
    }

    public void inANotExp(ANotExp node)
    {
        defaultIn(node);
    }

    public void outANotExp(ANotExp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseANotExp(ANotExp node)
    {
        inANotExp(node);
        if(node.getExp() != null)
        {
	    if(node.getExp() instanceof ATrueExp || node.getExp() instanceof AFalseExp)
		node.getExp().apply(this);
	    else {
		System.out.println("Error: "+node.getExp().toString()+" is not of type boolean");
		System.exit(1);}
        }
        outANotExp(node);
    }

    public void inARefExp(ARefExp node)
    {
        defaultIn(node);
    }

    public void outARefExp(ARefExp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseARefExp(ARefExp node)
    {
        inARefExp(node);
        if(node.getName() != null)
        {
            node.getName().apply(this);
        }
        if(node.getIdx() != null)
        {
            node.getIdx().apply(this);
        }
        outARefExp(node);
    }

    public void inALengthExp(ALengthExp node)
    {
        defaultIn(node);
    }

    public void outALengthExp(ALengthExp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseALengthExp(ALengthExp node)
    {
        inALengthExp(node);
        if(node.getExp() != null)
        {
            node.getExp().apply(this);
        }
        outALengthExp(node);
    }

    public void inAMethodExp(AMethodExp node)
    {
        defaultIn(node);
    }

    public void outAMethodExp(AMethodExp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAMethodExp(AMethodExp node)
    {
        inAMethodExp(node);
        if(node.getObj() != null)
        {
            node.getObj().apply(this);
        }
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        {
            List<PExp> copy = new ArrayList<PExp>(node.getArgs());
            for(PExp e : copy)
            {
                e.apply(this);
            }
        }
        outAMethodExp(node);
    }

    public void inANumExp(ANumExp node)
    {
        defaultIn(node);
    }

    public void outANumExp(ANumExp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseANumExp(ANumExp node)
    {
        inANumExp(node);
        if(node.getNum() != null)
        {
            node.getNum().apply(this);
        }
	medium =(Node)node.clone();
        outANumExp(node);
    }

    public void inAIdExp(AIdExp node)
    {
        defaultIn(node);
    }

    public void outAIdExp(AIdExp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAIdExp(AIdExp node)
    {
        inAIdExp(node);
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        outAIdExp(node);
    }

    public void inATrueExp(ATrueExp node)
    {
        defaultIn(node);
    }

    public void outATrueExp(ATrueExp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseATrueExp(ATrueExp node)
    {
        inATrueExp(node);
        outATrueExp(node);
    }

    public void inAFalseExp(AFalseExp node)
    {
        defaultIn(node);
    }

    public void outAFalseExp(AFalseExp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAFalseExp(AFalseExp node)
    {
        inAFalseExp(node);
        outAFalseExp(node);
    }

    public void inAThisExp(AThisExp node)
    {
        defaultIn(node);
    }

    public void outAThisExp(AThisExp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAThisExp(AThisExp node)
    {
        inAThisExp(node);
        outAThisExp(node);
    }

    public void inAAllocExp(AAllocExp node)
    {
        defaultIn(node);
    }

    public void outAAllocExp(AAllocExp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAAllocExp(AAllocExp node)
    {
        inAAllocExp(node);
        if(node.getExp() != null)
	    if(node.getExp() instanceof ANumExp)
		node.getExp().apply(this);
	    else{
		System.out.println("Error: "+node.getExp().toString()+" is not of type int");
		System.exit(1);}
        outAAllocExp(node);
    }

    public void inANewExp(ANewExp node)
    {
        defaultIn(node);
    }

    public void outANewExp(ANewExp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseANewExp(ANewExp node)
    {
        inANewExp(node);
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        outANewExp(node);
    }
}
*/
