/*    */ package org.jbpm.bpmn2.xml;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import org.jbpm.workflow.core.Constraint;
/*    */ import org.jbpm.workflow.core.Node;
/*    */ import org.jbpm.workflow.core.impl.ConnectionRef;
/*    */ import org.jbpm.workflow.core.node.Split;
/*    */ import org.kie.api.definition.process.NodeContainer;
/*    */ import org.xml.sax.Attributes;
/*    */ 
/*    */ public class SplitHandler extends AbstractNodeHandler
/*    */ {
/*    */   protected Node createNode(Attributes attrs)
/*    */   {
/* 30 */     throw new IllegalArgumentException("Reading in should be handled by gateway handler");
/*    */   }
/*    */ 
/*    */   public Class generateNodeFor()
/*    */   {
/* 35 */     return Split.class;
/*    */   }
/*    */ 
/*    */   public void writeNode(Node node, StringBuilder xmlDump, int metaDataType) {
/* 39 */     Split split = (Split)node;
/* 40 */     String type = null;
/* 41 */     switch (split.getType()) {
/*    */     case 1:
/* 43 */       type = "parallelGateway";
/* 44 */       writeNode(type, node, xmlDump, metaDataType);
/* 45 */       break;
/*    */     case 2:
/* 47 */       type = "exclusiveGateway";
/* 48 */       writeNode(type, node, xmlDump, metaDataType);
/* 49 */       for (Map.Entry entry : split.getConstraints().entrySet()) {
/* 50 */         if ((entry.getValue() != null) && (((Constraint)entry.getValue()).isDefault())) {
/* 51 */           xmlDump.append("default=\"" + 
/* 52 */             XmlBPMNProcessDumper.getUniqueNodeId(split) + 
/* 52 */             "-" + 
/* 53 */             XmlBPMNProcessDumper.getUniqueNodeId(node
/* 53 */             .getNodeContainer().getNode(((ConnectionRef)entry.getKey()).getNodeId())) + "\" ");
/*    */ 
/* 55 */           break;
/*    */         }
/*    */       }
/* 58 */       break;
/*    */     case 3:
/* 60 */       type = "inclusiveGateway";
/* 61 */       writeNode(type, node, xmlDump, metaDataType);
/* 62 */       for (Map.Entry entry : split.getConstraints().entrySet()) {
/* 63 */         if ((entry.getValue() != null) && (((Constraint)entry.getValue()).isDefault())) {
/* 64 */           xmlDump.append("default=\"" + 
/* 65 */             XmlBPMNProcessDumper.getUniqueNodeId(split) + 
/* 65 */             "-" + 
/* 66 */             XmlBPMNProcessDumper.getUniqueNodeId(node
/* 66 */             .getNodeContainer().getNode(((ConnectionRef)entry.getKey()).getNodeId())) + "\" ");
/*    */ 
/* 68 */           break;
/*    */         }
/*    */       }
/* 71 */       break;
/*    */     case 4:
/* 73 */       type = "eventBasedGateway";
/* 74 */       writeNode(type, node, xmlDump, metaDataType);
/* 75 */       break;
/*    */     default:
/* 77 */       type = "complexGateway";
/* 78 */       writeNode(type, node, xmlDump, metaDataType);
/*    */     }
/* 80 */     xmlDump.append("gatewayDirection=\"Diverging\" >" + EOL);
/* 81 */     writeExtensionElements(node, xmlDump);
/* 82 */     endNode(type, xmlDump);
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.SplitHandler
 * JD-Core Version:    0.6.0
 */