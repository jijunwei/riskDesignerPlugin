/*    */ package org.jbpm.bpmn2.core;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class Interface
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 510L;
/*    */   private String id;
/*    */   private String name;
/*    */   private String implementationRef;
/* 30 */   private Map<String, Operation> operations = new HashMap();
/*    */ 
/*    */   public Interface(String id, String name) {
/* 33 */     this.id = id;
/* 34 */     this.name = name;
/*    */   }
/*    */ 
/*    */   public String getId() {
/* 38 */     return this.id;
/*    */   }
/*    */ 
/*    */   public String getName() {
/* 42 */     return this.name;
/*    */   }
/*    */ 
/*    */   public Operation addOperation(String id, String name) {
/* 46 */     Operation operation = new Operation(id, name);
/* 47 */     this.operations.put(id, operation);
/* 48 */     return operation;
/*    */   }
/*    */ 
/*    */   public Operation getOperation(String name) {
/* 52 */     return (Operation)this.operations.get(name);
/*    */   }
/*    */ 
/*    */   public void setImplementationRef(String implementationRef) {
/* 56 */     this.implementationRef = implementationRef;
/*    */   }
/*    */ 
/*    */   public String getImplementationRef() {
/* 60 */     return this.implementationRef; } 
/*    */   public class Operation { private String id;
/*    */     private String name;
/*    */     private Message message;
/*    */     private String implementationRef;
/*    */ 
/* 69 */     public Operation(String id, String name) { this.id = id;
/* 70 */       this.name = name; }
/*    */ 
/*    */     public String getId() {
/* 73 */       return this.id;
/*    */     }
/*    */     public void setId(String id) {
/* 76 */       this.id = id;
/*    */     }
/*    */     public String getName() {
/* 79 */       return this.name;
/*    */     }
/*    */     public void setName(String name) {
/* 82 */       this.name = name;
/*    */     }
/*    */     public Message getMessage() {
/* 85 */       return this.message;
/*    */     }
/*    */     public void setMessage(Message message) {
/* 88 */       this.message = message;
/*    */     }
/*    */     public Interface getInterface() {
/* 91 */       return Interface.this;
/*    */     }
/*    */     public void setImplementationRef(String implementationRef) {
/* 94 */       this.implementationRef = implementationRef;
/*    */     }
/*    */     public String getImplementationRef() {
/* 97 */       return this.implementationRef;
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.core.Interface
 * JD-Core Version:    0.6.0
 */