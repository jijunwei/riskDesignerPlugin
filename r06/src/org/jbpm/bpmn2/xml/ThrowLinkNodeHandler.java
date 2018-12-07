/*    */ package org.jbpm.bpmn2.xml;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.jbpm.workflow.core.Node;
/*    */ import org.jbpm.workflow.core.node.ThrowLinkNode;
/*    */ import org.xml.sax.Attributes;
/*    */ 
/*    */ public class ThrowLinkNodeHandler extends AbstractNodeHandler
/*    */ {
/*    */   public Class<?> generateNodeFor()
/*    */   {
/* 28 */     return ThrowLinkNode.class;
/*    */   }
/*    */ 
/*    */   protected Node createNode(Attributes attrs)
/*    */   {
/* 33 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */   public void writeNode(Node node, StringBuilder xmlDump, int metaDataType)
/*    */   {
/* 39 */     ThrowLinkNode linkNode = (ThrowLinkNode)node;
/*    */ 
/* 41 */     writeNode("intermediateThrowEvent", linkNode, xmlDump, metaDataType);
/* 42 */     xmlDump.append(new StringBuilder().append(">").append(EOL).toString());
/* 43 */     writeExtensionElements(node, xmlDump);
/*    */ 
/* 45 */     String name = (String)node.getMetaData().get("linkName");
/*    */ 
/* 48 */     xmlDump.append(new StringBuilder().append("<linkEventDefinition name=\"").append(name).append("\" >").append(EOL).toString());
/*    */ 
/* 51 */     List<String> sources = (List)linkNode
/* 51 */       .getMetaData("source");
/*    */ 
/* 53 */     if (null != sources) {
/* 54 */       for (String s : sources) {
/* 55 */         xmlDump.append(new StringBuilder().append(String.format("<source>%s</source>", new Object[] { s })).append(EOL).toString());
/*    */       }
/*    */     }
/* 58 */     xmlDump.append(new StringBuilder().append("</linkEventDefinition>").append(EOL).toString());
/*    */ 
/* 60 */     endNode("intermediateThrowEvent", xmlDump);
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.ThrowLinkNodeHandler
 * JD-Core Version:    0.6.0
 */