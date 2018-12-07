/*    */ package org.jbpm.bpmn2.xml;
/*    */ 
/*    */ import org.drools.core.xml.ExtensibleXmlParser;
/*    */ import org.jbpm.workflow.core.Node;
/*    */ import org.w3c.dom.Element;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public class TransactionHandler extends SubProcessHandler
/*    */ {
/*    */   protected void handleNode(Node node, Element element, String uri, String localName, ExtensibleXmlParser parser)
/*    */     throws SAXException
/*    */   {
/* 28 */     super.handleNode(node, element, uri, localName, parser);
/* 29 */     node.setMetaData("Transaction", Boolean.valueOf(true));
/*    */   }
/*    */ 
/*    */   public void writeNode(Node node, StringBuilder xmlDump, boolean includeMeta) {
/* 33 */     throw new IllegalArgumentException("Writing out should be handled by specific handlers");
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.TransactionHandler
 * JD-Core Version:    0.6.0
 */