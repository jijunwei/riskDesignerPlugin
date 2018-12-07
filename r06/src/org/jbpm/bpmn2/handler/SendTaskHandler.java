/*    */ package org.jbpm.bpmn2.handler;
/*    */ 
/*    */ import org.kie.api.runtime.process.WorkItem;
/*    */ import org.kie.api.runtime.process.WorkItemHandler;
/*    */ import org.kie.api.runtime.process.WorkItemManager;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ public class SendTaskHandler
/*    */   implements WorkItemHandler
/*    */ {
/* 27 */   private static final Logger logger = LoggerFactory.getLogger(SendTaskHandler.class);
/*    */ 
/*    */   public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
/* 30 */     String message = (String)workItem.getParameter("Message");
/* 31 */     logger.debug("Sending message: {}", message);
/* 32 */     manager.completeWorkItem(workItem.getId(), null);
/*    */   }
/*    */ 
/*    */   public void abortWorkItem(WorkItem workItem, WorkItemManager manager)
/*    */   {
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.handler.SendTaskHandler
 * JD-Core Version:    0.6.0
 */