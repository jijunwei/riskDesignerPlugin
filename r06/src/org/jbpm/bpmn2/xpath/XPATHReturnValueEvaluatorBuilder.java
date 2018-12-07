/*    */ package org.jbpm.bpmn2.xpath;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.drools.compiler.compiler.DescrBuildError;
/*    */ import org.drools.compiler.compiler.ReturnValueDescr;
/*    */ import org.drools.compiler.rule.builder.PackageBuildContext;
/*    */ import org.jbpm.process.builder.ReturnValueEvaluatorBuilder;
/*    */ import org.jbpm.process.core.ContextResolver;
/*    */ import org.jbpm.process.instance.impl.ReturnValueConstraintEvaluator;
/*    */ import org.jbpm.process.instance.impl.XPATHReturnValueEvaluator;
/*    */ 
/*    */ public class XPATHReturnValueEvaluatorBuilder
/*    */   implements ReturnValueEvaluatorBuilder
/*    */ {
/*    */   public void build(PackageBuildContext context, ReturnValueConstraintEvaluator constraintNode, ReturnValueDescr descr, ContextResolver contextResolver)
/*    */   {
/* 40 */     String text = descr.getText();
/*    */     try
/*    */     {
/* 43 */       XPATHReturnValueEvaluator expr = new XPATHReturnValueEvaluator(text, null);
/*    */ 
/* 46 */       constraintNode.setEvaluator(expr);
/*    */     }
/*    */     catch (Exception e) {
/* 49 */       context.getErrors().add(
/* 52 */         new DescrBuildError(context.getParentDescr(), descr, null, "Unable to build expression for 'constraint' " + descr
/* 52 */         .getText() + "': " + e));
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xpath.XPATHReturnValueEvaluatorBuilder
 * JD-Core Version:    0.6.0
 */