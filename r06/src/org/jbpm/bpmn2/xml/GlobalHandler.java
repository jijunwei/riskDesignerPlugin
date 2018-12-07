/*    */ package org.jbpm.bpmn2.xml;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.HashSet;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import org.drools.core.xml.BaseAbstractHandler;
/*    */ import org.drools.core.xml.ExtensibleXmlParser;
/*    */ import org.drools.core.xml.Handler;
/*    */ import org.jbpm.workflow.core.impl.WorkflowProcessImpl;
/*    */ import org.kie.api.definition.process.Process;
/*    */ import org.xml.sax.Attributes;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public class GlobalHandler extends BaseAbstractHandler
/*    */   implements Handler
/*    */ {
/*    */   public GlobalHandler()
/*    */   {
/* 34 */     if ((this.validParents == null) && (this.validPeers == null)) {
/* 35 */       this.validParents = new HashSet();
/* 36 */       this.validParents.add(Process.class);
/*    */ 
/* 38 */       this.validPeers = new HashSet();
/* 39 */       this.validPeers.add(null);
/*    */ 
/* 41 */       this.allowNesting = false;
/*    */     }
/*    */   }
/*    */ 
/*    */   public Object start(String uri, String localName, Attributes attrs, ExtensibleXmlParser parser)
/*    */     throws SAXException
/*    */   {
/* 49 */     parser.startElementBuilder(localName, attrs);
/* 50 */     WorkflowProcessImpl process = (WorkflowProcessImpl)parser.getParent();
/*    */ 
/* 52 */     String identifier = attrs.getValue("identifier");
/* 53 */     String type = attrs.getValue("type");
/* 54 */     emptyAttributeCheck(localName, "identifier", identifier, parser);
/* 55 */     emptyAttributeCheck(localName, "type", type, parser);
/*    */ 
/* 57 */     Map map = process.getGlobals();
/* 58 */     if (map == null) {
/* 59 */       map = new HashMap();
/* 60 */       process.setGlobals(map);
/*    */     }
/* 62 */     map.put(identifier, type);
/*    */ 
/* 64 */     return null;
/*    */   }
/*    */ 
/*    */   public Object end(String uri, String localName, ExtensibleXmlParser parser)
/*    */     throws SAXException
/*    */   {
/* 70 */     parser.endElementBuilder();
/* 71 */     return null;
/*    */   }
/*    */ 
/*    */   public Class generateNodeFor() {
/* 75 */     return null;
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.GlobalHandler
 * JD-Core Version:    0.6.0
 */