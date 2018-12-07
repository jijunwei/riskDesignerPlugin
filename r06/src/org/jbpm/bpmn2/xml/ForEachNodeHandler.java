/*    */ package org.jbpm.bpmn2.xml;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.drools.compiler.compiler.xml.XmlDumper;
import org.jbpm.workflow.core.node.CompositeNode;
/*    */ import org.jbpm.workflow.core.node.CompositeNode.CompositeNodeEnd;
/*    */ import org.jbpm.workflow.core.node.CompositeNode.CompositeNodeStart;
/*    */ import org.jbpm.workflow.core.node.ForEachNode;
/*    */ import org.xml.sax.Attributes;
/*    */ 
/*    */ public class ForEachNodeHandler extends AbstractCompositeNodeHandler
/*    */ {
/*    */   protected org.jbpm.workflow.core.Node createNode(Attributes attrs)
/*    */   {
/* 32 */     throw new IllegalArgumentException("Reading in should be handled by end event handler");
/*    */   }
/*    */ 
/*    */   public Class generateNodeFor()
/*    */   {
/* 37 */     return ForEachNode.class;
/*    */   }
/*    */ 
/*    */   public void writeNode(org.jbpm.workflow.core.Node node, StringBuilder xmlDump, int metaDataType) {
/* 41 */     ForEachNode forEachNode = (ForEachNode)node;
/* 42 */     writeNode("subProcess", forEachNode, xmlDump, metaDataType);
/* 43 */     xmlDump.append(" >" + EOL);
/* 44 */     writeExtensionElements(node, xmlDump);
/*    */ 
/* 46 */     xmlDump.append("      <ioSpecification>" + EOL);
/*    */ 
/* 48 */     String parameterName = forEachNode.getVariableName();
/* 49 */     if (parameterName != null) {
/* 50 */       xmlDump.append("        <dataInput id=\"" + 
/* 51 */         XmlBPMNProcessDumper.getUniqueNodeId(forEachNode) + 
/* 51 */         "_input\" name=\"MultiInstanceInput\" />" + EOL);
/*    */     }
/* 53 */     xmlDump.append("        <inputSet/>" + EOL + "        <outputSet/>" + EOL + "      </ioSpecification>" + EOL);
/*    */ 
/* 57 */     String collectionExpression = forEachNode.getCollectionExpression();
/* 58 */     if (collectionExpression != null) {
/* 59 */       xmlDump.append("      <dataInputAssociation>" + EOL + "        <sourceRef>" + 
/* 61 */         XmlDumper.replaceIllegalChars(collectionExpression) + 
/* 61 */         "</sourceRef>" + EOL + "        <targetRef>" + 
/* 62 */         XmlBPMNProcessDumper.getUniqueNodeId(forEachNode) + 
/* 62 */         "_input</targetRef>" + EOL + "      </dataInputAssociation>" + EOL);
/*    */     }
/*    */ 
/* 66 */     xmlDump.append("      <multiInstanceLoopCharacteristics>" + EOL + "        <loopDataInputRef>" + 
/* 68 */       XmlBPMNProcessDumper.getUniqueNodeId(forEachNode) + 
/* 68 */       "_input</loopDataInputRef>" + EOL);
/* 69 */     if (parameterName != null) {
/* 70 */       xmlDump.append("        <inputDataItem id=\"" + XmlBPMNProcessDumper.replaceIllegalCharsAttribute(parameterName) + "\" itemSubjectRef=\"" + XmlBPMNProcessDumper.getUniqueNodeId(forEachNode) + "_multiInstanceItemType\"/>" + EOL);
/*    */     }
/* 72 */     xmlDump.append("      </multiInstanceLoopCharacteristics>" + EOL);
/*    */ 
/* 74 */     List subNodes = getSubNodes(forEachNode);
/* 75 */     XmlBPMNProcessDumper.INSTANCE.visitNodes(subNodes, xmlDump, metaDataType);
/*    */ 
/* 78 */     visitConnectionsAndAssociations(forEachNode, xmlDump, metaDataType);
/*    */ 
/* 80 */     endNode("subProcess", xmlDump);
/*    */   }
/*    */ 
/*    */   protected List<org.jbpm.workflow.core.Node> getSubNodes(ForEachNode forEachNode) {
/* 84 */     List subNodes = new ArrayList();
/*    */ 
/* 86 */     for (org.kie.api.definition.process.Node subNode : forEachNode.getNodes())
/*    */     {
/* 88 */       if (((subNode instanceof CompositeNode.CompositeNodeStart)) || ((subNode instanceof CompositeNode.CompositeNodeEnd)))
/*    */         continue;
/* 90 */       subNodes.add((org.jbpm.workflow.core.Node)subNode);
/*    */     }
/*    */ 
/* 93 */     return subNodes;
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.ForEachNodeHandler
 * JD-Core Version:    0.6.0
 */