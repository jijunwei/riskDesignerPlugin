/*    */ package org.jbpm.bpmn2.xml;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import org.drools.core.xml.BaseAbstractHandler;
/*    */ import org.drools.core.xml.ExtensibleXmlParser;
/*    */ import org.drools.core.xml.Handler;
import org.jbpm.bpmn2.core.Interface;
/*    */ import org.jbpm.bpmn2.core.Interface.Operation;
/*    */ import org.jbpm.bpmn2.core.Message;
/*    */ import org.jbpm.compiler.xml.ProcessBuildData;
/*    */ import org.w3c.dom.Element;
/*    */ import org.xml.sax.Attributes;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public class InMessageRefHandler extends BaseAbstractHandler
/*    */   implements Handler
/*    */ {
/*    */   public InMessageRefHandler()
/*    */   {
/* 36 */     if ((this.validParents == null) && (this.validPeers == null)) {
/* 37 */       this.validParents = new HashSet();
/* 38 */       this.validParents.add(Interface.Operation.class);
/*    */ 
/* 40 */       this.validPeers = new HashSet();
/* 41 */       this.validPeers.add(null);
/*    */ 
/* 43 */       this.allowNesting = false;
/*    */     }
/*    */   }
/*    */ 
/*    */   public Object start(String uri, String localName, Attributes attrs, ExtensibleXmlParser parser)
/*    */     throws SAXException
/*    */   {
/* 50 */     parser.startElementBuilder(localName, attrs);
/* 51 */     return null;
/*    */   }
/*    */ 
/*    */   public Object end(String uri, String localName, ExtensibleXmlParser parser)
/*    */     throws SAXException
/*    */   {
/* 57 */     Element element = parser.endElementBuilder();
/* 58 */     String messageId = element.getTextContent();
/*    */ 
/* 60 */     Map messages = (Map)((ProcessBuildData)parser
/* 60 */       .getData()).getMetaData("Messages");
/* 61 */     if (messages == null) {
/* 62 */       throw new IllegalArgumentException("No messages found");
/*    */     }
/* 64 */     Message message = (Message)messages.get(messageId);
/* 65 */     if (message == null) {
/* 66 */       throw new IllegalArgumentException("Could not find message " + messageId);
/*    */     }
/* 68 */     Interface.Operation operation = (Interface.Operation)parser.getParent();
/* 69 */     operation.setMessage(message);
/* 70 */     return parser.getCurrent();
/*    */   }
/*    */ 
/*    */   public Class<?> generateNodeFor() {
/* 74 */     return Message.class;
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.InMessageRefHandler
 * JD-Core Version:    0.6.0
 */