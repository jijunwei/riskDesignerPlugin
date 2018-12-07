/*    */ package org.jbpm.bpmn2.core;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class Message
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 510L;
/*    */   private String id;
/*    */   private String type;
/*    */ 
/*    */   public Message(String id)
/*    */   {
/* 29 */     this.id = id;
/*    */   }
/*    */ 
/*    */   public String getId() {
/* 33 */     return this.id;
/*    */   }
/*    */ 
/*    */   public String getType() {
/* 37 */     return this.type;
/*    */   }
/*    */ 
/*    */   public void setType(String type) {
/* 41 */     this.type = type;
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.core.Message
 * JD-Core Version:    0.6.0
 */