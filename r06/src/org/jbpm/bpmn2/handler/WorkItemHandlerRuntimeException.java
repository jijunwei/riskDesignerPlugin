/*    */ package org.jbpm.bpmn2.handler;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class WorkItemHandlerRuntimeException extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 1217036861831832336L;
/*    */   public static final String WORKITEMHANDLERTYPE = "workItemHandlerType";
/* 34 */   private HashMap<String, Object> info = new HashMap();
/*    */ 
/*    */   public WorkItemHandlerRuntimeException(Throwable cause, String message) {
/* 37 */     super(message, cause);
/*    */   }
/*    */ 
/*    */   public WorkItemHandlerRuntimeException(Throwable cause) {
/* 41 */     super(cause);
/*    */   }
/*    */ 
/*    */   public void setInformation(String informationName, Object information) {
/* 45 */     this.info.put(informationName, information);
/*    */   }
/*    */ 
/*    */   public Map<String, Object> getInformationMap() {
/* 49 */     return Collections.unmodifiableMap(this.info);
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.handler.WorkItemHandlerRuntimeException
 * JD-Core Version:    0.6.0
 */