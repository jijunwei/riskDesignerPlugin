/*    */ package org.jbpm.bpmn2.xpath;
/*    */ 
/*    */ import org.jbpm.process.builder.ActionBuilder;
/*    */ import org.jbpm.process.builder.AssignmentBuilder;
/*    */ import org.jbpm.process.builder.ProcessBuildContext;
/*    */ import org.jbpm.process.builder.ProcessClassBuilder;
/*    */ import org.jbpm.process.builder.ReturnValueEvaluatorBuilder;
/*    */ import org.jbpm.process.builder.dialect.ProcessDialect;
/*    */ 
/*    */ public class XPATHProcessDialect
/*    */   implements ProcessDialect
/*    */ {
/*    */   public static final String ID = "XPath";
/* 33 */   private static final ActionBuilder actionBuilder = new XPATHActionBuilder();
/* 34 */   private static final ReturnValueEvaluatorBuilder returnValueBuilder = new XPATHReturnValueEvaluatorBuilder();
/* 35 */   private static final AssignmentBuilder assignmentBuilder = new XPATHAssignmentBuilder();
/*    */ 
/*    */   public void addProcess(ProcessBuildContext context)
/*    */   {
/*    */   }
/*    */ 
/*    */   public ActionBuilder getActionBuilder() {
/* 42 */     return actionBuilder;
/*    */   }
/*    */ 
/*    */   public ProcessClassBuilder getProcessClassBuilder() {
/* 46 */     throw new UnsupportedOperationException("MVELProcessDialect.getProcessClassBuilder is not supported");
/*    */   }
/*    */ 
/*    */   public ReturnValueEvaluatorBuilder getReturnValueEvaluatorBuilder() {
/* 50 */     return returnValueBuilder;
/*    */   }
/*    */ 
/*    */   public AssignmentBuilder getAssignmentBuilder() {
/* 54 */     return assignmentBuilder;
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xpath.XPATHProcessDialect
 * JD-Core Version:    0.6.0
 */