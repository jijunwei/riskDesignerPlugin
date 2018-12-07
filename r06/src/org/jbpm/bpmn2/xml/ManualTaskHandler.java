/*    */ package org.jbpm.bpmn2.xml;
/*    */ 
/*    */ import org.jbpm.workflow.core.Node;
/*    */ import org.jbpm.workflow.core.node.WorkItemNode;
/*    */ import org.w3c.dom.Element;
/*    */ import org.xml.sax.Attributes;
/*    */ 
/*    */ public class ManualTaskHandler extends TaskHandler
/*    */ {
/*    */   protected Node createNode(Attributes attrs)
/*    */   {
/* 27 */     return new WorkItemNode();
/*    */   }
/*    */ 
/*    */   public Class generateNodeFor()
/*    */   {
/* 32 */     return Node.class;
/*    */   }
/*    */ 
/*    */   protected String getTaskName(Element element) {
/* 36 */     return "Manual Task";
/*    */   }
/*    */ 
/*    */   public void writeNode(Node node, StringBuilder xmlDump, boolean includeMeta) {
/* 40 */     throw new IllegalArgumentException("Writing out should be handled by TaskHandler");
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.ManualTaskHandler
 * JD-Core Version:    0.6.0
 */