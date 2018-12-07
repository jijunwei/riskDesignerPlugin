/*    */ package org.jbpm.bpmn2.core;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.jbpm.process.core.datatype.DataType;
/*    */ 
/*    */ public class DataStore
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 4L;
/*    */   private String id;
/*    */   private String name;
/*    */   private DataType type;
/*    */   private String itemSubjectRef;
/*    */ 
/*    */   public void setId(String id)
/*    */   {
/* 32 */     this.id = id;
/*    */   }
/*    */ 
/*    */   public void setName(String name) {
/* 36 */     this.name = name;
/*    */   }
/*    */ 
/*    */   public void setType(DataType dataType) {
/* 40 */     this.type = dataType;
/*    */   }
/*    */ 
/*    */   public String getId() {
/* 44 */     return this.id;
/*    */   }
/*    */ 
/*    */   public String getName() {
/* 48 */     return this.name;
/*    */   }
/*    */ 
/*    */   public DataType getType() {
/* 52 */     return this.type;
/*    */   }
/*    */ 
/*    */   public void setItemSubjectRef(String itemSubjectRef) {
/* 56 */     this.itemSubjectRef = itemSubjectRef;
/*    */   }
/*    */ 
/*    */   public String getItemSubjectRef() {
/* 60 */     return this.itemSubjectRef;
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.core.DataStore
 * JD-Core Version:    0.6.0
 */