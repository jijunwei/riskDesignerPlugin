/*    */ package org.jbpm.bpmn2.xml;
/*    */ 
/*    */ import org.jbpm.workflow.core.Node;
/*    */ import org.jbpm.workflow.core.node.Split;
/*    */ import org.xml.sax.Attributes;
/*    */ 
/*    */ public class EventBasedGatewayHandler extends AbstractNodeHandler
/*    */ {
/*    */   protected Node createNode(Attributes attrs)
/*    */   {
/* 26 */     String type = attrs.getValue("gatewayDirection");
/* 27 */     if ("Diverging".equals(type)) {
/* 28 */       Split split = new Split();
/* 29 */       split.setType(4);
/* 30 */       split.setMetaData("EventBased", "true");
/* 31 */       return split;
/*    */     }
/* 33 */     throw new IllegalArgumentException("Unknown gateway direction: " + type);
/*    */   }
/*    */ 
/*    */   public Class generateNodeFor()
/*    */   {
/* 40 */     return Node.class;
/*    */   }
/*    */ 
/*    */   public void writeNode(Node node, StringBuilder xmlDump, int metaDataType) {
/* 44 */     throw new IllegalArgumentException("Writing out should be handled by split / join handler");
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.EventBasedGatewayHandler
 * JD-Core Version:    0.6.0
 */