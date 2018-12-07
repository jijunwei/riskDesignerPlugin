/*     */ package org.jbpm.bpmn2.xml;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.drools.core.xml.ExtensibleXmlParser;
/*     */ import org.jbpm.process.core.context.variable.VariableScope;
/*     */ import org.jbpm.workflow.core.node.DynamicNode;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class AdHocSubProcessHandler extends CompositeContextNodeHandler
/*     */ {
/*     */   public static final String AUTOCOMPLETE_COMPLETION_CONDITION = "autocomplete";
/*  34 */   public static final List<String> AUTOCOMPLETE_EXPRESSIONS = Arrays.asList(new String[] { "getActivityInstanceAttribute(\"numberOfActiveInstances\") == 0", "autocomplete" });
/*     */ 
/*     */   protected org.jbpm.workflow.core.Node createNode(Attributes attrs)
/*     */   {
/*  38 */     DynamicNode result = new DynamicNode();
/*  39 */     VariableScope variableScope = new VariableScope();
/*  40 */     result.addContext(variableScope);
/*  41 */     result.setDefaultContext(variableScope);
/*  42 */     return result;
/*     */   }
/*     */ 
/*     */   public Class generateNodeFor()
/*     */   {
/*  47 */     return DynamicNode.class;
/*     */   }
/*     */ 
/*     */   protected void handleNode(org.jbpm.workflow.core.Node node, Element element, String uri, String localName, ExtensibleXmlParser parser)
/*     */     throws SAXException
/*     */   {
/*  53 */     super.handleNode(node, element, uri, localName, parser);
/*  54 */     DynamicNode dynamicNode = (DynamicNode)node;
/*  55 */     String cancelRemainingInstances = element.getAttribute("cancelRemainingInstances");
/*  56 */     if ("false".equals(cancelRemainingInstances)) {
/*  57 */       dynamicNode.setCancelRemainingInstances(false);
/*     */     }
/*     */ 
/*  60 */     dynamicNode.setAutoComplete(false);
/*  61 */     dynamicNode.setActivationExpression((String)dynamicNode.getMetaData("customActivationCondition"));
/*     */ 
/*  63 */     org.w3c.dom.Node xmlNode = element.getFirstChild();
/*  64 */     while (xmlNode != null) {
/*  65 */       String nodeName = xmlNode.getNodeName();
/*  66 */       if ("completionCondition".equals(nodeName)) {
/*  67 */         String expression = xmlNode.getTextContent();
/*  68 */         if (AUTOCOMPLETE_EXPRESSIONS.contains(expression))
/*  69 */           dynamicNode.setAutoComplete(true);
/*     */         else {
/*  71 */           dynamicNode.setCompletionExpression(expression == null ? "" : expression);
/*     */         }
/*  73 */         org.w3c.dom.Node languageNode = xmlNode.getAttributes().getNamedItem("language");
/*  74 */         if (languageNode != null) {
/*  75 */           String language = languageNode.getNodeValue();
/*  76 */           if ("http://www.mvel.org/2.0".equals(language))
/*  77 */             dynamicNode.setLanguage("mvel");
/*  78 */           else if ("http://www.jboss.org/drools/rule".equals(language))
/*  79 */             dynamicNode.setLanguage("rule");
/*     */           else
/*  81 */             throw new IllegalArgumentException("Unknown language " + language);
/*     */         }
/*     */         else {
/*  84 */           dynamicNode.setLanguage("mvel");
/*     */         }
/*     */       }
/*  87 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/*     */ 
/*  90 */     List connections = (List)dynamicNode
/*  90 */       .getMetaData("BPMN.Connections");
/*     */ 
/*  91 */     ProcessHandler.linkConnections(dynamicNode, connections);
/*  92 */     ProcessHandler.linkBoundaryEvents(dynamicNode);
/*     */ 
/*  94 */     handleScript(dynamicNode, element, "onEntry");
/*  95 */     handleScript(dynamicNode, element, "onExit");
/*     */   }
/*     */ 
/*     */   public void writeNode(org.jbpm.workflow.core.Node node, StringBuilder xmlDump, int metaDataType) {
/*  99 */     DynamicNode dynamicNode = (DynamicNode)node;
/* 100 */     writeNode("adHocSubProcess", dynamicNode, xmlDump, metaDataType);
/* 101 */     if (!dynamicNode.isCancelRemainingInstances()) {
/* 102 */       xmlDump.append(" cancelRemainingInstances=\"false\"");
/*     */     }
/* 104 */     xmlDump.append(" ordering=\"Parallel\" >" + EOL);
/* 105 */     writeExtensionElements(dynamicNode, xmlDump);
/*     */ 
/* 107 */     List subNodes = getSubNodes(dynamicNode);
/* 108 */     XmlBPMNProcessDumper.INSTANCE.visitNodes(subNodes, xmlDump, metaDataType);
/*     */ 
/* 111 */     visitConnectionsAndAssociations(dynamicNode, xmlDump, metaDataType);
/*     */ 
/* 113 */     if (dynamicNode.isAutoComplete()) {
/* 114 */       xmlDump.append("    <completionCondition xsi:type=\"tFormalExpression\">autocomplete</completionCondition>" + EOL);
/*     */     }
/* 116 */     endNode("adHocSubProcess", xmlDump);
/*     */   }
/*     */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.AdHocSubProcessHandler
 * JD-Core Version:    0.6.0
 */