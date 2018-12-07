/*    */ package org.jbpm.bpmn2.core;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class Lane
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 510L;
/*    */   private String id;
/*    */   private String name;
/* 31 */   private List<String> flowElementIds = new ArrayList();
/* 32 */   private Map<String, Object> metaData = new HashMap();
/*    */ 
/*    */   public Lane(String id) {
/* 35 */     this.id = id;
/*    */   }
/*    */ 
/*    */   public String getId() {
/* 39 */     return this.id;
/*    */   }
/*    */ 
/*    */   public String getName() {
/* 43 */     return this.name;
/*    */   }
/*    */ 
/*    */   public void setName(String name) {
/* 47 */     this.name = name;
/*    */   }
/*    */ 
/*    */   public List<String> getFlowElements() {
/* 51 */     return this.flowElementIds;
/*    */   }
/*    */ 
/*    */   public void addFlowElement(String id) {
/* 55 */     this.flowElementIds.add(id);
/*    */   }
/*    */ 
/*    */   public Map<String, Object> getMetaData() {
/* 59 */     return this.metaData;
/*    */   }
/*    */ 
/*    */   public void setMetaData(String name, Object data) {
/* 63 */     this.metaData.put(name, data);
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.core.Lane
 * JD-Core Version:    0.6.0
 */