/*     */ package org.jbpm.bpmn2.core;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class SequenceFlow
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 510L;
/*     */   private String id;
/*     */   private String sourceRef;
/*     */   private String targetRef;
/*     */   private String bendpoints;
/*     */   private String expression;
/*     */   private String type;
/*     */   private String language;
/*     */   private String name;
/*     */   private int priority;
/*  36 */   private Map<String, Object> metaData = new HashMap();
/*     */ 
/*     */   public SequenceFlow(String id, String sourceRef, String targetRef) {
/*  39 */     this.id = id;
/*  40 */     this.sourceRef = sourceRef;
/*  41 */     this.targetRef = targetRef;
/*     */   }
/*     */ 
/*     */   public String getId() {
/*  45 */     return this.id;
/*     */   }
/*     */ 
/*     */   public String getSourceRef() {
/*  49 */     return this.sourceRef;
/*     */   }
/*     */ 
/*     */   public String getTargetRef() {
/*  53 */     return this.targetRef;
/*     */   }
/*     */ 
/*     */   public String getBendpoints() {
/*  57 */     return this.bendpoints;
/*     */   }
/*     */ 
/*     */   public void setBendpoints(String bendpoints) {
/*  61 */     this.bendpoints = bendpoints;
/*     */   }
/*     */ 
/*     */   public String getExpression() {
/*  65 */     return this.expression;
/*     */   }
/*     */ 
/*     */   public void setExpression(String expression) {
/*  69 */     this.expression = expression;
/*     */   }
/*     */ 
/*     */   public String getLanguage() {
/*  73 */     return this.language;
/*     */   }
/*     */ 
/*     */   public void setLanguage(String language) {
/*  77 */     this.language = language;
/*     */   }
/*     */ 
/*     */   public String getType() {
/*  81 */     return this.type;
/*     */   }
/*     */ 
/*     */   public void setType(String type) {
/*  85 */     this.type = type;
/*     */   }
/*     */ 
/*     */   public String getName() {
/*  89 */     return this.name;
/*     */   }
/*     */ 
/*     */   public void setName(String name) {
/*  93 */     this.name = name;
/*     */   }
/*     */ 
/*     */   public int getPriority() {
/*  97 */     return this.priority;
/*     */   }
/*     */ 
/*     */   public void setPriority(int priority) {
/* 101 */     this.priority = priority;
/*     */   }
/*     */ 
/*     */   public Map<String, Object> getMetaData() {
/* 105 */     return this.metaData;
/*     */   }
/*     */ 
/*     */   public void setMetaData(String name, Object data) {
/* 109 */     this.metaData.put(name, data);
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 113 */     return "SequenceFlow (" + this.id + ") [" + this.sourceRef + " -> " + this.targetRef + "]";
/*     */   }
/*     */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.core.SequenceFlow
 * JD-Core Version:    0.6.0
 */