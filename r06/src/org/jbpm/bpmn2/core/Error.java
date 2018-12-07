/*    */ package org.jbpm.bpmn2.core;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class Error extends Signal
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 510L;
/*    */   private String errorCode;
/*    */ 
/*    */   public Error(String id, String errorCode, String itemRef)
/*    */   {
/* 28 */     super(id, itemRef);
/* 29 */     this.errorCode = errorCode;
/*    */   }
/*    */ 
/*    */   public String getErrorCode() {
/* 33 */     return this.errorCode;
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.core.Error
 * JD-Core Version:    0.6.0
 */