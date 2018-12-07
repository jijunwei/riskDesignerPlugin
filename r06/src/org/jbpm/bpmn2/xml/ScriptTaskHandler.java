/*    */ package org.jbpm.bpmn2.xml;
/*    */ 
/*    */ import org.drools.core.xml.ExtensibleXmlParser;
/*    */ import org.jbpm.workflow.core.impl.DroolsConsequenceAction;
/*    */ import org.jbpm.workflow.core.node.ActionNode;
/*    */ import org.w3c.dom.Element;
/*    */ import org.xml.sax.Attributes;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public class ScriptTaskHandler extends AbstractNodeHandler
/*    */ {
/*    */   protected org.jbpm.workflow.core.Node createNode(Attributes attrs)
/*    */   {
/* 31 */     ActionNode result = new ActionNode();
/* 32 */     result.setAction(new DroolsConsequenceAction());
/* 33 */     return result;
/*    */   }
/*    */ 
/*    */   public Class generateNodeFor()
/*    */   {
/* 38 */     return org.jbpm.workflow.core.Node.class;
/*    */   }
/*    */ 
/*    */   protected void handleNode(org.jbpm.workflow.core.Node node, Element element, String uri, String localName, ExtensibleXmlParser parser) throws SAXException
/*    */   {
/* 43 */     super.handleNode(node, element, uri, localName, parser);
/* 44 */     ActionNode actionNode = (ActionNode)node;
/* 45 */     node.setMetaData("NodeType", "ScriptTask");
/* 46 */     DroolsConsequenceAction action = (DroolsConsequenceAction)actionNode.getAction();
/* 47 */     if (action == null) {
/* 48 */       action = new DroolsConsequenceAction();
/* 49 */       actionNode.setAction(action);
/*    */     }
/* 51 */     String language = element.getAttribute("scriptFormat");
/* 52 */     if ("http://www.java.com/java".equals(language))
/* 53 */       action.setDialect("java");
/* 54 */     else if ("http://www.javascript.com/javascript".equals(language)) {
/* 55 */       action.setDialect("JavaScript");
/*    */     }
/* 57 */     action.setConsequence("");
/* 58 */     org.w3c.dom.Node xmlNode = element.getFirstChild();
/* 59 */     while (xmlNode != null) {
/* 60 */       if (((xmlNode instanceof Element)) && ("script".equals(xmlNode.getNodeName()))) {
/* 61 */         action.setConsequence(xmlNode.getTextContent());
/*    */       }
/* 63 */       xmlNode = xmlNode.getNextSibling();
/*    */     }
/*    */ 
/* 66 */     String compensation = element.getAttribute("isForCompensation");
/* 67 */     if (compensation != null) {
/* 68 */       boolean isForCompensation = Boolean.parseBoolean(compensation);
/* 69 */       if (isForCompensation)
/* 70 */         actionNode.setMetaData("isForCompensation", Boolean.valueOf(isForCompensation));
/*    */     }
/*    */   }
/*    */ 
/*    */   public void writeNode(org.jbpm.workflow.core.Node node, StringBuilder xmlDump, int metaDataType)
/*    */   {
/* 76 */     throw new IllegalArgumentException("Writing out should be handled by action node handler");
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.ScriptTaskHandler
 * JD-Core Version:    0.6.0
 */