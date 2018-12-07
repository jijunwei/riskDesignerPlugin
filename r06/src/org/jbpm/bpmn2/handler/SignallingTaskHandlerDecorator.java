/*     */ package org.jbpm.bpmn2.handler;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.kie.api.runtime.process.WorkItemHandler;
/*     */ 
/*     */ public class SignallingTaskHandlerDecorator extends AbstractExceptionHandlingTaskHandler
/*     */ {
/*     */   private final String eventType;
/*  45 */   private String workItemExceptionParameterName = "jbpm.workitem.exception";
/*     */ 
/*  47 */   private final Map<Long, Integer> processInstanceExceptionMap = new HashMap();
/*  48 */   private int exceptionCountLimit = 1;
/*     */ 
/*     */   public SignallingTaskHandlerDecorator(Class<? extends WorkItemHandler> originalTaskHandlerClass, String eventType)
/*     */   {
/*  58 */     super(originalTaskHandlerClass);
/*  59 */     this.eventType = eventType;
/*     */   }
/*     */ 
/*     */   public SignallingTaskHandlerDecorator(WorkItemHandler originalTaskHandler, String eventType) {
/*  63 */     super(originalTaskHandler);
/*  64 */     this.eventType = eventType;
/*     */   }
/*     */ 
/*     */   public SignallingTaskHandlerDecorator(Class<? extends WorkItemHandler> originalTaskHandlerClass, String eventType, int exceptionCountLimit) {
/*  68 */     super(originalTaskHandlerClass);
/*  69 */     this.eventType = eventType;
/*  70 */     this.exceptionCountLimit = exceptionCountLimit;
/*     */   }
/*     */ 
/*     */   public SignallingTaskHandlerDecorator(WorkItemHandler originalTaskHandler, String eventType, int exceptionCountLimit) {
/*  74 */     super(originalTaskHandler);
/*  75 */     this.eventType = eventType;
/*  76 */     this.exceptionCountLimit = exceptionCountLimit;
/*     */   }
/*     */ 
/*     */   public void setWorkItemExceptionParameterName(String parameterName) {
/*  80 */     this.workItemExceptionParameterName = parameterName;
/*     */   }
/*     */ 
/*     */   public String getWorkItemExceptionParameterName() {
/*  84 */     return this.workItemExceptionParameterName;
/*     */   }
/*     */ 
/*     */   public void handleExecuteException(Throwable cause, org.kie.api.runtime.process.WorkItem workItem, org.kie.api.runtime.process.WorkItemManager manager)
/*     */   {
/*  89 */     if (getAndIncreaseExceptionCount(Long.valueOf(workItem.getProcessInstanceId())) < this.exceptionCountLimit) {
/*  90 */       workItem.getParameters().put(this.workItemExceptionParameterName, cause);
/*  91 */       ((org.drools.core.process.instance.WorkItemManager)manager).signalEvent(this.eventType, (org.drools.core.process.instance.WorkItem)workItem, workItem.getProcessInstanceId());
/*     */     } else {
/*  93 */       if ((cause instanceof RuntimeException)) {
/*  94 */         throw ((RuntimeException)cause);
/*     */       }
/*     */ 
/*  97 */       throw new WorkItemHandlerRuntimeException(cause, "Signalling process instance " + workItem
/*  97 */         .getProcessInstanceId() + " with '" + this.eventType + "' resulted this exception.");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void handleAbortException(Throwable cause, org.kie.api.runtime.process.WorkItem workItem, org.kie.api.runtime.process.WorkItemManager manager)
/*     */   {
/* 104 */     if (getAndIncreaseExceptionCount(Long.valueOf(workItem.getProcessInstanceId())) < this.exceptionCountLimit) {
/* 105 */       workItem.getParameters().put(this.workItemExceptionParameterName, cause);
/* 106 */       ((org.drools.core.process.instance.WorkItemManager)manager).signalEvent(this.eventType, (org.drools.core.process.instance.WorkItem)workItem, workItem.getProcessInstanceId());
/*     */     }
/*     */   }
/*     */ 
/*     */   private int getAndIncreaseExceptionCount(Long processInstanceId) {
/* 111 */     Integer count = (Integer)this.processInstanceExceptionMap.get(processInstanceId);
/* 112 */     if (count == null) {
/* 113 */       count = Integer.valueOf(0);
/*     */     }
/* 115 */     this.processInstanceExceptionMap.put(processInstanceId, count = Integer.valueOf(count.intValue() + 1));
/* 116 */     return count.intValue() - 1;
/*     */   }
/*     */ 
/*     */   public void setExceptionCountLimit(int limit) {
/* 120 */     this.exceptionCountLimit = limit;
/*     */   }
/*     */ 
/*     */   public void clearProcessInstance(Long processInstanceId) {
/* 124 */     this.processInstanceExceptionMap.remove(processInstanceId);
/*     */   }
/*     */ 
/*     */   public void clear() {
/* 128 */     this.processInstanceExceptionMap.clear();
/*     */   }
/*     */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.handler.SignallingTaskHandlerDecorator
 * JD-Core Version:    0.6.0
 */