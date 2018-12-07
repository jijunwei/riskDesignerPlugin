/*    */ package org.jbpm.bpmn2.xml;
/*    */ 
/*    */ import org.jbpm.workflow.core.Node;
/*    */ import org.jbpm.workflow.core.node.Join;
/*    */ import org.xml.sax.Attributes;
/*    */ 
/*    */ public class JoinHandler extends AbstractNodeHandler
/*    */ {
/*    */   protected Node createNode(Attributes attrs)
/*    */   {
/* 26 */     throw new IllegalArgumentException("Reading in should be handled by gateway handler");
/*    */   }
/*    */ 
/*    */   public Class generateNodeFor()
/*    */   {
/* 31 */     return Join.class;
/*    */   }
/*    */ 
/*    */   public void writeNode(Node node, StringBuilder xmlDump, int metaDataType) {
/* 35 */     Join join = (Join)node;
/* 36 */     String type = null;
/* 37 */     switch (join.getType()) {
/*    */     case 1:
/* 39 */       type = "parallelGateway";
/* 40 */       break;
/*    */     case 2:
/* 42 */       type = "exclusiveGateway";
/* 43 */       break;
/*    */     case 5:
/* 45 */       type = "inclusiveGateway";
/* 46 */       break;
/*    */     case 3:
/*    */     case 4:
/*    */     default:
/* 48 */       type = "complexGateway";
/*    */     }
/* 50 */     writeNode(type, node, xmlDump, metaDataType);
/* 51 */     xmlDump.append("gatewayDirection=\"Converging\" >" + EOL);
/* 52 */     writeExtensionElements(join, xmlDump);
/* 53 */     endNode(type, xmlDump);
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.JoinHandler
 * JD-Core Version:    0.6.0
 */