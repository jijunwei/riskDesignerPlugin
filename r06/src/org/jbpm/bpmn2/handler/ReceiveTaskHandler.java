/*    */ package org.jbpm.bpmn2.handler;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.kie.api.runtime.KieSession;
/*    */ import org.kie.api.runtime.process.ProcessRuntime;
/*    */ import org.kie.api.runtime.process.WorkItem;
/*    */ import org.kie.api.runtime.process.WorkItemHandler;
/*    */ import org.kie.api.runtime.process.WorkItemManager;
/*    */ 
/*    */ public class ReceiveTaskHandler
/*    */   implements WorkItemHandler
/*    */ {
/* 28 */   private Map<String, Long> waiting = new HashMap();
/*    */   private ProcessRuntime ksession;
/*    */ 
/*    */   public ReceiveTaskHandler(KieSession ksession)
/*    */   {
/* 32 */     this.ksession = ksession;
/*    */   }
/*    */ 
/*    */   public void setKnowledgeRuntime(KieSession ksession) {
/* 36 */     this.ksession = ksession;
/*    */   }
/*    */ 
/*    */   public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
/* 40 */     String messageId = (String)workItem.getParameter("MessageId");
/* 41 */     this.waiting.put(messageId, Long.valueOf(workItem.getId()));
/*    */   }
/*    */ 
/*    */   public void messageReceived(String messageId, Object message) {
/* 45 */     Long workItemId = (Long)this.waiting.get(messageId);
/* 46 */     if (workItemId == null) {
/* 47 */       return;
/*    */     }
/* 49 */     Map results = new HashMap();
/* 50 */     results.put("Message", message);
/* 51 */     this.ksession.getWorkItemManager().completeWorkItem(workItemId.longValue(), results);
/*    */   }
/*    */ 
/*    */   public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
/* 55 */     String messageId = (String)workItem.getParameter("MessageId");
/* 56 */     this.waiting.remove(messageId);
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.handler.ReceiveTaskHandler
 * JD-Core Version:    0.6.0
 */