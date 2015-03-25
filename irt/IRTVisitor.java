// irt visitor

import java.util.*;
import minijava.node.*;
import symtable.*;


public class IRTVisitor extends DepthFirstAdapter {

    ClassTable table;

    public IRTVisitor(ClassTable table){
        table = table;
    }

	public void caseAMainClassDecl(AMainClassDecl node) {
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

    public void caseABaseClassDecl(ABaseClassDecl node) {
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



