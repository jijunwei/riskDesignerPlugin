/*    */ package org.jbpm.bpmn2.xml;
/*    */ 
/*    */ import org.drools.compiler.compiler.xml.XmlDumper;
/*    */ import org.jbpm.workflow.core.Node;
/*    */ import org.jbpm.workflow.core.node.StateNode;
/*    */ import org.xml.sax.Attributes;
/*    */ 
/*    */ public class StateNodeHandler extends AbstractNodeHandler
/*    */ {
/*    */   protected Node createNode(Attributes attrs)
/*    */   {
/* 27 */     throw new IllegalArgumentException("Reading in should be handled by intermediate catch event handler");
/*    */   }
/*    */ 
/*    */   public Class generateNodeFor()
/*    */   {
/* 32 */     return StateNode.class;
/*    */   }
/*    */ 
/*    */   public void writeNode(Node node, StringBuilder xmlDump, int metaDataType) {
/* 36 */     StateNode stateNode = (StateNode)node;
/* 37 */     String condition = (String)stateNode.getMetaData("Condition");
/* 38 */     writeNode("intermediateCatchEvent", stateNode, xmlDump, metaDataType);
/* 39 */     xmlDump.append(">" + EOL);
/* 40 */     writeExtensionElements(node, xmlDump);
/* 41 */     xmlDump.append("      <conditionalEventDefinition>" + EOL);
/* 42 */     xmlDump.append("        <condition xsi:type=\"tFormalExpression\" language=\"http://www.jboss.org/drools/rule\">" + XmlDumper.replaceIllegalChars(condition) + "</condition>" + EOL);
/* 43 */     xmlDump.append("      </conditionalEventDefinition>" + EOL);
/* 44 */     endNode("intermediateCatchEvent", xmlDump);
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.StateNodeHandler
 * JD-Core Version:    0.6.0
 */