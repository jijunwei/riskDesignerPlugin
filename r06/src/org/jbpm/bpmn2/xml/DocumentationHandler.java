/*    */ package org.jbpm.bpmn2.xml;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import org.drools.core.xml.BaseAbstractHandler;
/*    */ import org.drools.core.xml.ExtensibleXmlParser;
/*    */ import org.drools.core.xml.Handler;
/*    */ import org.jbpm.workflow.core.impl.NodeImpl;
/*    */ import org.w3c.dom.Element;
/*    */ import org.w3c.dom.NodeList;
/*    */ import org.w3c.dom.Text;
/*    */ import org.xml.sax.Attributes;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public class DocumentationHandler extends BaseAbstractHandler
/*    */   implements Handler
/*    */ {
/*    */   public DocumentationHandler()
/*    */   {
/* 34 */     if ((this.validParents == null) && (this.validPeers == null)) {
/* 35 */       this.validParents = new HashSet();
/* 36 */       this.validParents.add(Object.class);
/*    */ 
/* 38 */       this.validPeers = new HashSet();
/* 39 */       this.validPeers.add(null);
/* 40 */       this.validPeers.add(Object.class);
/*    */ 
/* 42 */       this.allowNesting = false;
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
/*    */   public Object end(String uri, String localName, ExtensibleXmlParser parser) throws SAXException
/*    */   {
/* 56 */     Element element = parser.endElementBuilder();
/* 57 */     Object parent = parser.getParent();
/* 58 */     if ((parent instanceof NodeImpl)) {
/* 59 */       String text = ((Text)element.getChildNodes().item(0)).getWholeText();
/* 60 */       if (text != null) {
/* 61 */         text = text.trim();
/* 62 */         if ("".equals(text)) {
/* 63 */           text = null;
/*    */         }
/*    */       }
/* 66 */       ((NodeImpl)parent).getMetaData().put("Documentation", text);
/*    */     }
/* 68 */     return parser.getCurrent();
/*    */   }
/*    */ 
/*    */   public Class<?> generateNodeFor() {
/* 72 */     return null;
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.DocumentationHandler
 * JD-Core Version:    0.6.0
 */