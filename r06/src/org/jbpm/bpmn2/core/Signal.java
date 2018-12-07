/*    */ package org.jbpm.bpmn2.core;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class Signal
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 510L;
/*    */   private String id;
/*    */   private String name;
/*    */   private String structureRef;
/*    */ 
/*    */   public Signal(String id, String structureRef)
/*    */   {
/* 30 */     this.id = id;
/* 31 */     this.structureRef = structureRef;
/*    */   }
/*    */ 
/*    */   public Signal(String id, String name, String structureRef) {
/* 35 */     this(id, structureRef);
/* 36 */     this.name = name;
/*    */   }
/*    */ 
/*    */   public String getId() {
/* 40 */     return this.id;
/*    */   }
/*    */ 
/*    */   public String getStructureRef() {
/* 44 */     return this.structureRef;
/*    */   }
/*    */ 
/*    */   public String getName() {
/* 48 */     return this.name;
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.core.Signal
 * JD-Core Version:    0.6.0
 */