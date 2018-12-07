/*    */ package org.jbpm.bpmn2.xml;
/*    */ 
/*    */ import org.drools.core.xml.DefaultSemanticModule;
/*    */ import org.jbpm.bpmn2.xml.di.BPMNEdgeHandler;
/*    */ import org.jbpm.bpmn2.xml.di.BPMNPlaneHandler;
/*    */ import org.jbpm.bpmn2.xml.di.BPMNShapeHandler;
/*    */ 
/*    */ public class BPMNDISemanticModule extends DefaultSemanticModule
/*    */ {
/*    */   public BPMNDISemanticModule()
/*    */   {
/* 27 */     super("http://www.omg.org/spec/BPMN/20100524/DI");
/*    */ 
/* 29 */     addHandler("BPMNPlane", new BPMNPlaneHandler());
/* 30 */     addHandler("BPMNShape", new BPMNShapeHandler());
/* 31 */     addHandler("BPMNEdge", new BPMNEdgeHandler());
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.BPMNDISemanticModule
 * JD-Core Version:    0.6.0
 */