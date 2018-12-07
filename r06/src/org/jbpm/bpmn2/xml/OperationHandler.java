/*    */ package org.jbpm.bpmn2.xml;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import org.drools.core.xml.BaseAbstractHandler;
/*    */ import org.drools.core.xml.ExtensibleXmlParser;
/*    */ import org.drools.core.xml.Handler;
/*    */ import org.jbpm.bpmn2.core.Interface;
/*    */ import org.jbpm.bpmn2.core.Interface.Operation;
/*    */ import org.xml.sax.Attributes;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public class OperationHandler extends BaseAbstractHandler
/*    */   implements Handler
/*    */ {
/*    */   public OperationHandler()
/*    */   {
/* 33 */     if ((this.validParents == null) && (this.validPeers == null)) {
/* 34 */       this.validParents = new HashSet();
/* 35 */       this.validParents.add(Interface.class);
/*    */ 
/* 37 */       this.validPeers = new HashSet();
/* 38 */       this.validPeers.add(null);
/* 39 */       this.validPeers.add(Interface.Operation.class);
/*    */ 
/* 41 */       this.allowNesting = false;
/*    */     }
/*    */   }
/*    */ 
/*    */   public Object start(String uri, String localName, Attributes attrs, ExtensibleXmlParser parser)
/*    */     throws SAXException
/*    */   {
/* 48 */     parser.startElementBuilder(localName, attrs);
/*    */ 
/* 50 */     String id = attrs.getValue("id");
/* 51 */     String name = attrs.getValue("name");
/* 52 */     String implRef = attrs.getValue("implementationRef");
/*    */ 
/* 54 */     Interface i = (Interface)parser.getParent();
/* 55 */     Interface.Operation operation = i.addOperation(id, name);
/* 56 */     if (implRef != null) {
/* 57 */       operation.setImplementationRef(implRef);
/*    */     }
/* 59 */     return operation;
/*    */   }
/*    */ 
/*    */   public Object end(String uri, String localName, ExtensibleXmlParser parser) throws SAXException
/*    */   {
/* 64 */     parser.endElementBuilder();
/* 65 */     return parser.getCurrent();
/*    */   }
/*    */ 
/*    */   public Class<?> generateNodeFor() {
/* 69 */     return Interface.Operation.class;
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.OperationHandler
 * JD-Core Version:    0.6.0
 */