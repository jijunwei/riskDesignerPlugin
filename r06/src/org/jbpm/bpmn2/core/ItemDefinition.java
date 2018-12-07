/*    */ package org.jbpm.bpmn2.core;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class ItemDefinition
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 510L;
/*    */   private String id;
/*    */   private String structureRef;
/*    */ 
/*    */   public ItemDefinition(String id)
/*    */   {
/* 29 */     this.id = id;
/*    */   }
/*    */ 
/*    */   public String getId() {
/* 33 */     return this.id;
/*    */   }
/*    */ 
/*    */   public String getStructureRef() {
/* 37 */     return this.structureRef;
/*    */   }
/*    */ 
/*    */   public void setStructureRef(String structureRef) {
/* 41 */     this.structureRef = structureRef;
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.core.ItemDefinition
 * JD-Core Version:    0.6.0
 */