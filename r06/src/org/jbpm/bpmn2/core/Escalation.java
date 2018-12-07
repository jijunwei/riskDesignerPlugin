/*    */ package org.jbpm.bpmn2.core;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class Escalation extends Signal
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 510L;
/*    */   private String escalationCode;
/*    */ 
/*    */   public Escalation(String id, String structureRef, String escalationCode)
/*    */   {
/* 28 */     super(id, structureRef);
/* 29 */     this.escalationCode = escalationCode;
/*    */   }
/*    */ 
/*    */   public Escalation(String id, String escalationCode) {
/* 33 */     super(id, null);
/* 34 */     this.escalationCode = escalationCode;
/*    */   }
/*    */ 
/*    */   public String getEscalationCode() {
/* 38 */     return this.escalationCode;
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.core.Escalation
 * JD-Core Version:    0.6.0
 */