/*    */ package org.jbpm.bpmn2.handler;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import org.kie.api.runtime.process.WorkItem;
/*    */ import org.kie.api.runtime.process.WorkItemHandler;
/*    */ import org.kie.api.runtime.process.WorkItemManager;
/*    */ 
/*    */ public abstract class AbstractExceptionHandlingTaskHandler
/*    */   implements WorkItemHandler
/*    */ {
/*    */   private WorkItemHandler originalTaskHandler;
/*    */ 
/*    */   public AbstractExceptionHandlingTaskHandler(WorkItemHandler originalTaskHandler)
/*    */   {
/* 27 */     this.originalTaskHandler = originalTaskHandler;
/*    */   }
/*    */ 
/*    */   public AbstractExceptionHandlingTaskHandler(Class<? extends WorkItemHandler> originalTaskHandlerClass) {
/* 31 */     Class[] clsParams = new Class[0];
/* 32 */     Object[] objParams = new Object[0];
/*    */     try {
/* 34 */       this.originalTaskHandler = ((WorkItemHandler)originalTaskHandlerClass.getConstructor(clsParams).newInstance(objParams));
/*    */     } catch (Exception e) {
/* 36 */       throw new UnsupportedOperationException("The " + WorkItemHandler.class.getSimpleName() + " parameter must have a public no-argument constructor.");
/*    */     }
/*    */   }
/*    */ 
/*    */   public void executeWorkItem(WorkItem workItem, WorkItemManager manager)
/*    */   {
/*    */     try {
/* 43 */       this.originalTaskHandler.executeWorkItem(workItem, manager);
/*    */     } catch (Throwable cause) {
/* 45 */       handleExecuteException(cause, workItem, manager);
/*    */     }
/*    */   }
/*    */ 
/*    */   public void abortWorkItem(WorkItem workItem, WorkItemManager manager)
/*    */   {
/*    */     try {
/* 52 */       this.originalTaskHandler.abortWorkItem(workItem, manager);
/*    */     } catch (RuntimeException re) {
/* 54 */       handleAbortException(re, workItem, manager);
/*    */     }
/*    */   }
/*    */ 
/*    */   public WorkItemHandler getOriginalTaskHandler() {
/* 59 */     return this.originalTaskHandler;
/*    */   }
/*    */ 
/*    */   public abstract void handleExecuteException(Throwable paramThrowable, WorkItem paramWorkItem, WorkItemManager paramWorkItemManager);
/*    */ 
/*    */   public abstract void handleAbortException(Throwable paramThrowable, WorkItem paramWorkItem, WorkItemManager paramWorkItemManager);
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.handler.AbstractExceptionHandlingTaskHandler
 * JD-Core Version:    0.6.0
 */