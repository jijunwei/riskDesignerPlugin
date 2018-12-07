/*    */ package org.jbpm.bpmn2.xml;
/*    */ 
/*    */ import org.drools.compiler.compiler.xml.XmlDumper;
/*    */ import org.jbpm.workflow.core.Node;
/*    */ import org.jbpm.workflow.core.node.FaultNode;
/*    */ import org.xml.sax.Attributes;
/*    */ 
/*    */ public class FaultNodeHandler extends AbstractNodeHandler
/*    */ {
/*    */   protected Node createNode(Attributes attrs)
/*    */   {
/* 33 */     throw new IllegalArgumentException("Reading in should be handled by end event handler");
/*    */   }
/*    */ 
/*    */   public Class generateNodeFor()
/*    */   {
/* 38 */     return FaultNode.class;
/*    */   }
/*    */ 
/*    */   public void writeNode(Node node, StringBuilder xmlDump, int metaDataType) {
/* 42 */     FaultNode faultNode = (FaultNode)node;
/* 43 */     writeNode("endEvent", faultNode, xmlDump, metaDataType);
/* 44 */     xmlDump.append(">" + EOL);
/* 45 */     writeExtensionElements(node, xmlDump);
/* 46 */     if (faultNode.getFaultVariable() != null) {
/* 47 */       xmlDump.append("      <dataInput id=\"" + XmlBPMNProcessDumper.getUniqueNodeId(faultNode) + "_Input\" name=\"error\" />" + EOL);
/* 48 */       xmlDump.append("      <dataInputAssociation>" + EOL);
/* 49 */       xmlDump.append("        <sourceRef>" + 
/* 50 */         XmlDumper.replaceIllegalChars(faultNode
/* 50 */         .getFaultVariable()) + "</sourceRef>" + EOL + "        <targetRef>" + 
/* 51 */         XmlBPMNProcessDumper.getUniqueNodeId(faultNode) + 
/* 51 */         "_Input</targetRef>" + EOL);
/* 52 */       xmlDump.append("      </dataInputAssociation>" + EOL);
/* 53 */       xmlDump.append("      <inputSet>" + EOL);
/* 54 */       xmlDump.append("        <dataInputRefs>" + XmlBPMNProcessDumper.getUniqueNodeId(faultNode) + "_Input</dataInputRefs>" + EOL);
/* 55 */       xmlDump.append("      </inputSet>" + EOL);
/*    */     }
/* 57 */     if (faultNode.isTerminateParent()) {
/* 58 */       String errorCode = faultNode.getFaultName();
/* 59 */       String errorId = getErrorIdForErrorCode(errorCode, faultNode);
/* 60 */       xmlDump.append("      <errorEventDefinition errorRef=\"" + XmlBPMNProcessDumper.replaceIllegalCharsAttribute(errorId) + "\" />" + EOL);
/*    */     } else {
/* 62 */       xmlDump.append("      <escalationEventDefinition escalationRef=\"" + XmlBPMNProcessDumper.replaceIllegalCharsAttribute(faultNode.getFaultName()) + "\" />" + EOL);
/*    */     }
/* 64 */     endNode("endEvent", xmlDump);
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.FaultNodeHandler
 * JD-Core Version:    0.6.0
 */