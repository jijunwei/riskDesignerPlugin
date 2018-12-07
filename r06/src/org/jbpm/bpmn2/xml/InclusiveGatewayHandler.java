/*    */ package org.jbpm.bpmn2.xml;
/*    */ 
/*    */ import org.jbpm.workflow.core.Node;
/*    */ import org.jbpm.workflow.core.node.Join;
/*    */ import org.jbpm.workflow.core.node.Split;
/*    */ import org.xml.sax.Attributes;
/*    */ 
/*    */ public class InclusiveGatewayHandler extends AbstractNodeHandler
/*    */ {
/*    */   protected Node createNode(Attributes attrs)
/*    */   {
/* 27 */     String type = attrs.getValue("gatewayDirection");
/* 28 */     if ("Converging".equals(type)) {
/* 29 */       Join join = new Join();
/* 30 */       join.setType(5);
/* 31 */       return join;
/* 32 */     }if ("Diverging".equals(type)) {
/* 33 */       Split split = new Split();
/* 34 */       split.setType(3);
/* 35 */       String isDefault = attrs.getValue("default");
/* 36 */       split.setMetaData("Default", isDefault);
/* 37 */       return split;
/*    */     }
/* 39 */     throw new IllegalArgumentException("Unknown gateway direction: " + type);
/*    */   }
/*    */ 
/*    */   public Class generateNodeFor()
/*    */   {
/* 46 */     return Node.class;
/*    */   }
/*    */ 
/*    */   public void writeNode(Node node, StringBuilder xmlDump, int metaDataType) {
/* 50 */     throw new IllegalArgumentException("Writing out should be handled by split / join handler");
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.InclusiveGatewayHandler
 * JD-Core Version:    0.6.0
 */