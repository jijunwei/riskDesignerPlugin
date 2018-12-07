/*    */ package org.jbpm.bpmn2.xml;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.drools.core.xml.Handler;
/*    */ import org.jbpm.workflow.core.Node;
/*    */ import org.jbpm.workflow.core.node.CatchLinkNode;
/*    */ import org.xml.sax.Attributes;
/*    */ 
/*    */ public class CatchLinkNodeHandler extends AbstractNodeHandler
/*    */   implements Handler
/*    */ {
/*    */   public Class<?> generateNodeFor()
/*    */   {
/* 28 */     return CatchLinkNode.class;
/*    */   }
/*    */ 
/*    */   protected Node createNode(Attributes attrs)
/*    */   {
/* 33 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */   public void writeNode(Node node, StringBuilder xmlDump, int metaDataType)
/*    */   {
/* 39 */     CatchLinkNode linkNode = (CatchLinkNode)node;
/* 40 */     writeNode("intermediateCatchEvent", linkNode, xmlDump, metaDataType);
/* 41 */     xmlDump.append(">" + EOL);
/* 42 */     writeExtensionElements(linkNode, xmlDump);
/*    */ 
/* 44 */     String name = (String)node.getMetaData().get("LinkName");
/*    */ 
/* 47 */     xmlDump.append("<linkEventDefinition name=\"" + name + "\" >" + EOL);
/*    */ 
/* 49 */     Object target = linkNode.getMetaData("target");
/* 50 */     if (null != target) {
/* 51 */       xmlDump.append(String.format("<target>%s</target>", new Object[] { target }) + EOL);
/*    */     }
/* 53 */     xmlDump.append("</linkEventDefinition>" + EOL);
/* 54 */     endNode("intermediateCatchEvent", xmlDump);
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.CatchLinkNodeHandler
 * JD-Core Version:    0.6.0
 */