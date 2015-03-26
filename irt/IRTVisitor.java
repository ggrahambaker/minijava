// irt visitor
package irt;

import minijava.analysis.DepthFirstAdapter;
import java.util.*;
import minijava.node.*;
import symtable.*;


public class IRTVisitor extends DepthFirstAdapter {

    private ClassTable table;
    private Node classInfo;


    public IRTVisitor(ClassTable t){
        System.out.println("irt has been created!");
        table = t;
    }


	public void caseAMainClassDecl(AMainClassDecl node) {
        inAMainClassDecl(node);
        classInfo = node;
        System.out.println(((AMainClassDecl)classInfo).getId().getText() + "-- main method!");
        //table.dump();

        ClassInfo ci = table.get(((AMainClassDecl)classInfo).getId().getText() + " ");
        if(ci == null){

        }
        //System.out.println(ci.getName().getText() + " eee");
        // /ci.allocateMem();
        
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

    public void caseABaseClassDecl(ABaseClassDecl node) {
        inABaseClassDecl(node);
        classInfo = node;
        ClassInfo ci = table.get(((ABaseClassDecl)classInfo).getId().getText());
        ci.allocateMem();



        // if(classInfo instanceof AMainClassDecl){
        //     // do nothing
        // } else if (classInfo instanceof ABaseClassDecl){
        
           

        // } else if(classInfo instanceof ASubClassDecl){

     
            
        // }


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

    public void caseASubClassDecl(ASubClassDecl node)
    {
        inASubClassDecl(node);
        classInfo = node;
            ClassInfo ci = table.get(((ASubClassDecl)classInfo).getId().getText());
        
            ci.allocateMem();

        

        // if(classInfo instanceof AMainClassDecl){
        //     // do nothing
        // } else if (classInfo instanceof ABaseClassDecl){
        
        //     ClassInfo ci = table.get(((ABaseClassDecl)classInfo).getId().getText());
        //     ci.allocateMem();

        // } else if(classInfo instanceof ASubClassDecl){

        //     ClassInfo ci = table.get(((ASubClassDecl)classInfo).getId().getText());
        
        //     ci.allocateMem();
            
        // }
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

    public void caseAMethod(AMethod node)
    {

        if(classInfo instanceof AMainClassDecl){
            // do nothing
        } else if (classInfo instanceof ABaseClassDecl){
           
            ClassInfo ci = table.get(((ABaseClassDecl)classInfo).getId().getText());
            MethodTable mt = ci.getMethodTable();
            MethodInfo mi = mt.get(node.getId().getText());
            mi.allocateMem();

        } else if(classInfo instanceof ASubClassDecl){

            ClassInfo ci = table.get(((ASubClassDecl)classInfo).getId().getText());
            MethodTable mt = ci.getMethodTable();
            MethodInfo mi = mt.get(node.getId().getText());
            mi.allocateMem();
            
        }



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
    }

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




}



