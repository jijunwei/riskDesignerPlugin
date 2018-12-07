/*    */ package org.jbpm.bpmn2.xml;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import org.drools.core.xml.BaseAbstractHandler;
/*    */ import org.drools.core.xml.ExtensibleXmlParser;
/*    */ import org.drools.core.xml.Handler;
/*    */ import org.jbpm.process.core.ValueObject;
/*    */ import org.jbpm.process.core.datatype.DataType;
/*    */ import org.w3c.dom.Element;
/*    */ import org.w3c.dom.NodeList;
/*    */ import org.w3c.dom.Text;
/*    */ import org.xml.sax.Attributes;
/*    */ import org.xml.sax.SAXException;
/*    */ import org.xml.sax.SAXParseException;
/*    */ 
/*    */ public class MetaValueHandler extends BaseAbstractHandler
/*    */   implements Handler
/*    */ {
/*    */   public MetaValueHandler()
/*    */   {
/* 35 */     if ((this.validParents == null) && (this.validPeers == null)) {
/* 36 */       this.validParents = new HashSet();
/* 37 */       this.validParents.add(ValueObject.class);
/*    */ 
/* 39 */       this.validPeers = new HashSet();
/* 40 */       this.validPeers.add(null);
/*    */ 
/* 42 */       this.allowNesting = false;
/*    */     }
/*    */   }
/*    */ 
/*    */   public Object start(String uri, String localName, Attributes attrs, ExtensibleXmlParser parser)
/*    */     throws SAXException
/*    */   {
/* 50 */     parser.startElementBuilder(localName, attrs);
/*    */ 
/* 52 */     return null;
/*    */   }
/*    */ 
/*    */   public Object end(String uri, String localName, ExtensibleXmlParser parser)
/*    */     throws SAXException
/*    */   {
/* 58 */     Element element = parser.endElementBuilder();
/* 59 */     ValueObject valueObject = (ValueObject)parser.getParent();
/* 60 */     String text = ((Text)element.getChildNodes().item(0)).getWholeText();
/* 61 */     if (text != null) {
/* 62 */       text = text.trim();
/* 63 */       if ("".equals(text)) {
/* 64 */         text = null;
/*    */       }
/*    */     }
/* 67 */     Object value = restoreValue(text, valueObject.getType(), parser);
/* 68 */     valueObject.setValue(value);
/* 69 */     return null;
/*    */   }
/*    */ 
/*    */   private Object restoreValue(String text, DataType dataType, ExtensibleXmlParser parser) throws SAXException {
/* 73 */     if ((text == null) || ("".equals(text))) {
/* 74 */       return null;
/*    */     }
/* 76 */     if (dataType == null)
/*    */     {
/* 78 */       throw new SAXParseException("Null datatype", parser
/* 78 */         .getLocator());
/*    */     }
/* 80 */     return dataType.readValue(text);
/*    */   }
/*    */ 
/*    */   public Class<?> generateNodeFor() {
/* 84 */     return null;
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.MetaValueHandler
 * JD-Core Version:    0.6.0
 */