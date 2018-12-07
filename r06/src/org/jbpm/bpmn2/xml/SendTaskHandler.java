/*    */ package org.jbpm.bpmn2.xml;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.drools.core.xml.ExtensibleXmlParser;
/*    */ import org.jbpm.bpmn2.core.Message;
/*    */ import org.jbpm.compiler.xml.ProcessBuildData;
/*    */ import org.jbpm.process.core.Work;
/*    */ import org.jbpm.workflow.core.Node;
/*    */ import org.jbpm.workflow.core.node.WorkItemNode;
/*    */ import org.w3c.dom.Element;
/*    */ import org.xml.sax.Attributes;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public class SendTaskHandler extends TaskHandler
/*    */ {
/*    */   protected Node createNode(Attributes attrs)
/*    */   {
/* 33 */     return new WorkItemNode();
/*    */   }
/*    */ 
/*    */   public Class generateNodeFor()
/*    */   {
/* 38 */     return Node.class;
/*    */   }
/*    */ 
/*    */   protected void handleNode(Node node, Element element, String uri, String localName, ExtensibleXmlParser parser)
/*    */     throws SAXException
/*    */   {
/* 44 */     super.handleNode(node, element, uri, localName, parser);
/* 45 */     WorkItemNode workItemNode = (WorkItemNode)node;
/* 46 */     String messageRef = element.getAttribute("messageRef");
/*    */ 
/* 48 */     Map messages = (Map)((ProcessBuildData)parser
/* 48 */       .getData()).getMetaData("Messages");
/* 49 */     if (messages == null) {
/* 50 */       throw new IllegalArgumentException("No messages found");
/*    */     }
/* 52 */     Message message = (Message)messages.get(messageRef);
/* 53 */     if (message == null) {
/* 54 */       throw new IllegalArgumentException("Could not find message " + messageRef);
/*    */     }
/* 56 */     workItemNode.getWork().setParameter("MessageType", message.getType());
/*    */   }
/*    */ 
/*    */   protected String getTaskName(Element element) {
/* 60 */     return "Send Task";
/*    */   }
/*    */ 
/*    */   public void writeNode(Node node, StringBuilder xmlDump, boolean includeMeta) {
/* 64 */     throw new IllegalArgumentException("Writing out should be handled by WorkItemNodeHandler");
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.SendTaskHandler
 * JD-Core Version:    0.6.0
 */