/*    */ package org.jbpm.bpmn2.xpath;
/*    */ 
/*    */ import org.drools.compiler.rule.builder.PackageBuildContext;
/*    */ import org.jbpm.process.builder.AssignmentBuilder;
/*    */ import org.jbpm.process.core.ContextResolver;
/*    */ import org.jbpm.workflow.core.node.Assignment;
/*    */ 
/*    */ public class XPATHAssignmentBuilder
/*    */   implements AssignmentBuilder
/*    */ {
/*    */   public void build(PackageBuildContext context, Assignment assignment, String sourceExpr, String targetExpr, ContextResolver contextResolver, boolean isInput)
/*    */   {
/* 28 */     assignment.setMetaData("Action", new XPATHAssignmentAction(assignment, sourceExpr, targetExpr, isInput));
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xpath.XPATHAssignmentBuilder
 * JD-Core Version:    0.6.0
 */