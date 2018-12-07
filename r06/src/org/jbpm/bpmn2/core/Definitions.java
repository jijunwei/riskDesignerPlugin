/*    */ package org.jbpm.bpmn2.core;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.List;
/*    */ 
/*    */ public class Definitions
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 4L;
/*    */   private String targetNamespace;
/*    */   private List<DataStore> dataStores;
/*    */   private List<Association> associations;
/*    */   private List<Error> errors;
/*    */ 
/*    */   public String getTargetNamespace()
/*    */   {
/* 32 */     return this.targetNamespace;
/*    */   }
/*    */ 
/*    */   public void setTargetNamespace(String targetNamespace) {
/* 36 */     this.targetNamespace = targetNamespace;
/*    */   }
/*    */ 
/*    */   public void setDataStores(List<DataStore> dataStores) {
/* 40 */     this.dataStores = dataStores;
/*    */   }
/*    */ 
/*    */   public List<DataStore> getDataStores() {
/* 44 */     return this.dataStores;
/*    */   }
/*    */ 
/*    */   public void setAssociations(List<Association> associations) {
/* 48 */     this.associations = associations;
/*    */   }
/*    */ 
/*    */   public List<Association> getAssociations() {
/* 52 */     return this.associations;
/*    */   }
/*    */ 
/*    */   public List<Error> getErrors() {
/* 56 */     return this.errors;
/*    */   }
/*    */ 
/*    */   public void setErrors(List<Error> errors) {
/* 60 */     this.errors = errors;
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.core.Definitions
 * JD-Core Version:    0.6.0
 */