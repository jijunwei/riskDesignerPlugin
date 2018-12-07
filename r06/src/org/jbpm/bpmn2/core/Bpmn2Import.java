/*    */ package org.jbpm.bpmn2.core;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class Bpmn2Import
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 6625038042886559671L;
/*    */   private String type;
/*    */   private String location;
/*    */   private String namespace;
/*    */ 
/*    */   public Bpmn2Import(String type, String location, String namespace)
/*    */   {
/* 30 */     this.type = type;
/* 31 */     this.location = location;
/* 32 */     this.namespace = namespace;
/*    */   }
/*    */ 
/*    */   public String getType() {
/* 36 */     return this.type;
/*    */   }
/*    */   public void setType(String type) {
/* 39 */     this.type = type;
/*    */   }
/*    */   public String getLocation() {
/* 42 */     return this.location;
/*    */   }
/*    */   public void setLocation(String location) {
/* 45 */     this.location = location;
/*    */   }
/*    */   public String getNamespace() {
/* 48 */     return this.namespace;
/*    */   }
/*    */   public void setNamespace(String namespace) {
/* 51 */     this.namespace = namespace;
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.core.Bpmn2Import
 * JD-Core Version:    0.6.0
 */