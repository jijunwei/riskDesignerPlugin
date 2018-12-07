/*    */ package org.jbpm.bpmn2.core;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class Association
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 4L;
/*    */   private String id;
/*    */   private String sourceRef;
/*    */   private String targetRef;
/* 27 */   private String direction = "none";
/*    */ 
/*    */   public String getId() {
/* 30 */     return this.id;
/*    */   }
/*    */ 
/*    */   public void setId(String id) {
/* 34 */     this.id = id;
/*    */   }
/*    */ 
/*    */   public String getSourceRef() {
/* 38 */     return this.sourceRef;
/*    */   }
/*    */ 
/*    */   public void setSourceRef(String sourceRef) {
/* 42 */     this.sourceRef = sourceRef;
/*    */   }
/*    */ 
/*    */   public String getTargetRef() {
/* 46 */     return this.targetRef;
/*    */   }
/*    */ 
/*    */   public void setTargetRef(String targetRef) {
/* 50 */     this.targetRef = targetRef;
/*    */   }
/*    */ 
/*    */   public String getDirection() {
/* 54 */     return this.direction;
/*    */   }
/*    */ 
/*    */   public void setDirection(String direction) {
/* 58 */     this.direction = direction;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 62 */     return "Association (" + this.id + ") [" + this.sourceRef + " -> " + this.targetRef + "]";
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.core.Association
 * JD-Core Version:    0.6.0
 */