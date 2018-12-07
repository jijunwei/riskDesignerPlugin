/*    */ package org.jbpm.bpmn2.core;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class IntermediateLink
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 201105091147L;
/*    */   private static final String THROW = "throw";
/*    */   private String uniqueId;
/*    */   private String target;
/*    */   private String name;
/* 35 */   private String type = null;
/*    */   private List<String> sources;
/*    */ 
/*    */   public IntermediateLink()
/*    */   {
/* 40 */     this.sources = new ArrayList();
/*    */   }
/*    */ 
/*    */   public void setUniqueId(String id) {
/* 44 */     this.uniqueId = id;
/*    */   }
/*    */ 
/*    */   public String getUniqueId() {
/* 48 */     return this.uniqueId;
/*    */   }
/*    */ 
/*    */   public void setTarget(String target) {
/* 52 */     this.target = target;
/*    */   }
/*    */ 
/*    */   public String getTarget() {
/* 56 */     return this.target;
/*    */   }
/*    */ 
/*    */   public void addSource(String sources) {
/* 60 */     this.sources.add(sources);
/*    */   }
/*    */ 
/*    */   public List<String> getSources() {
/* 64 */     return this.sources;
/*    */   }
/*    */ 
/*    */   public void setName(String name) {
/* 68 */     this.name = name;
/*    */   }
/*    */ 
/*    */   public String getName() {
/* 72 */     return this.name;
/*    */   }
/*    */ 
/*    */   public boolean isThrowLink() {
/* 76 */     return "throw".equals(this.type);
/*    */   }
/*    */ 
/*    */   public void configureThrow()
/*    */   {
/* 83 */     this.type = "throw";
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.core.IntermediateLink
 * JD-Core Version:    0.6.0
 */