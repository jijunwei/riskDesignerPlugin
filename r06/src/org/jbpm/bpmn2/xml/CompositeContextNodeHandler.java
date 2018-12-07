/*    */ package org.jbpm.bpmn2.xml;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.jbpm.process.core.context.variable.Variable;
/*    */ import org.jbpm.process.core.context.variable.VariableScope;
/*    */ import org.jbpm.workflow.core.node.CompositeContextNode;
/*    */ import org.jbpm.workflow.core.node.CompositeNode;
/*    */ import org.jbpm.workflow.core.node.CompositeNode.CompositeNodeEnd;
/*    */ import org.jbpm.workflow.core.node.CompositeNode.CompositeNodeStart;
/*    */ import org.jbpm.workflow.core.node.EventSubProcessNode;
/*    */ import org.xml.sax.Attributes;
/*    */ 
/*    */ public class CompositeContextNodeHandler extends AbstractCompositeNodeHandler
/*    */ {
/*    */   protected org.jbpm.workflow.core.Node createNode(Attributes attrs)
/*    */   {
/* 33 */     throw new IllegalArgumentException("Reading in should be handled by end event handler");
/*    */   }
/*    */ 
/*    */   public Class generateNodeFor()
/*    */   {
/* 38 */     return CompositeContextNode.class;
/*    */   }
/*    */ 
/*    */   public void writeNode(org.jbpm.workflow.core.Node node, StringBuilder xmlDump, int metaDataType) {
/* 42 */     CompositeContextNode compositeNode = (CompositeContextNode)node;
/* 43 */     String nodeType = "subProcess";
/* 44 */     if (node.getMetaData().get("Transaction") != null) {
/* 45 */       nodeType = "transaction";
/*    */     }
/* 47 */     writeNode(nodeType, compositeNode, xmlDump, metaDataType);
/* 48 */     if ((compositeNode instanceof EventSubProcessNode)) {
/* 49 */       xmlDump.append(" triggeredByEvent=\"true\" ");
/*    */     }
/* 51 */     Object isForCompensationObject = compositeNode.getMetaData("isForCompensation");
/* 52 */     if ((isForCompensationObject != null) && (((Boolean)isForCompensationObject).booleanValue())) {
/* 53 */       xmlDump.append("isForCompensation=\"true\" ");
/*    */     }
/* 55 */     xmlDump.append(">" + EOL);
/* 56 */     writeExtensionElements(compositeNode, xmlDump);
/*    */ 
/* 59 */     VariableScope variableScope = (VariableScope)compositeNode
/* 59 */       .getDefaultContext("VariableScope");
/*    */ 
/* 60 */     if ((variableScope != null) && (!variableScope.getVariables().isEmpty())) {
/* 61 */       xmlDump.append("    <!-- variables -->" + EOL);
/* 62 */       for (Variable variable : variableScope.getVariables()) {
/* 63 */         xmlDump.append("    <property id=\"" + XmlBPMNProcessDumper.replaceIllegalCharsAttribute(variable.getName()) + "\" ");
/* 64 */         if (variable.getType() != null) {
/* 65 */           xmlDump.append("itemSubjectRef=\"" + XmlBPMNProcessDumper.getUniqueNodeId(compositeNode) + "-" + XmlBPMNProcessDumper.replaceIllegalCharsAttribute(variable.getName()) + "Item\"");
/*    */         }
/*    */ 
/* 68 */         xmlDump.append("/>" + EOL);
/*    */       }
/*    */     }
/*    */ 
/* 72 */     List subNodes = getSubNodes(compositeNode);
/* 73 */     XmlBPMNProcessDumper.INSTANCE.visitNodes(subNodes, xmlDump, metaDataType);
/*    */ 
/* 76 */     visitConnectionsAndAssociations(compositeNode, xmlDump, metaDataType);
/*    */ 
/* 78 */     endNode(nodeType, xmlDump);
/*    */   }
/*    */ 
/*    */   protected List<org.jbpm.workflow.core.Node> getSubNodes(CompositeNode compositeNode) {
/* 82 */     List subNodes = new ArrayList();
/*    */ 
/* 84 */     for (org.kie.api.definition.process.Node subNode : compositeNode.getNodes())
/*    */     {
/* 86 */       if (((subNode instanceof CompositeNode.CompositeNodeStart)) || ((subNode instanceof CompositeNode.CompositeNodeEnd)))
/*    */         continue;
/* 88 */       subNodes.add((org.jbpm.workflow.core.Node)subNode);
/*    */     }
/*    */ 
/* 91 */     return subNodes;
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.CompositeContextNodeHandler
 * JD-Core Version:    0.6.0
 */