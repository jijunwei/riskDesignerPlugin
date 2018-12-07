/*     */ package org.jbpm.bpmn2.xml;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.drools.core.xml.BaseAbstractHandler;
/*     */ import org.drools.core.xml.ExtensibleXmlParser;
/*     */ import org.drools.core.xml.Handler;
/*     */ import org.jbpm.bpmn2.core.Association;
/*     */ import org.jbpm.bpmn2.core.IntermediateLink;
/*     */ import org.jbpm.bpmn2.core.Lane;
/*     */ import org.jbpm.bpmn2.core.SequenceFlow;
/*     */ import org.jbpm.process.core.context.variable.Variable;
/*     */ import org.jbpm.ruleflow.core.RuleFlowProcess;
/*     */ import org.jbpm.workflow.core.NodeContainer;
/*     */ import org.jbpm.workflow.core.node.CompositeNode;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class SequenceFlowHandler extends BaseAbstractHandler
/*     */   implements Handler
/*     */ {
/*     */   public SequenceFlowHandler()
/*     */   {
/*  42 */     initValidParents();
/*  43 */     initValidPeers();
/*  44 */     this.allowNesting = false;
/*     */   }
/*     */ 
/*     */   protected void initValidParents() {
/*  48 */     this.validParents = new HashSet();
/*  49 */     this.validParents.add(NodeContainer.class);
/*     */   }
/*     */ 
/*     */   protected void initValidPeers() {
/*  53 */     this.validPeers = new HashSet();
/*  54 */     this.validPeers.add(null);
/*  55 */     this.validPeers.add(Lane.class);
/*  56 */     this.validPeers.add(Variable.class);
/*  57 */     this.validPeers.add(org.jbpm.workflow.core.Node.class);
/*  58 */     this.validPeers.add(SequenceFlow.class);
/*  59 */     this.validPeers.add(Lane.class);
/*  60 */     this.validPeers.add(Association.class);
/*     */ 
/*  62 */     this.validPeers.add(IntermediateLink.class);
/*     */   }
/*     */ 
/*     */   public Object start(String uri, String localName, Attributes attrs, ExtensibleXmlParser parser)
/*     */     throws SAXException
/*     */   {
/*  70 */     parser.startElementBuilder(localName, attrs);
/*     */ 
/*  72 */     String id = attrs.getValue("id");
/*  73 */     String sourceRef = attrs.getValue("sourceRef");
/*  74 */     String targetRef = attrs.getValue("targetRef");
/*  75 */     String bendpoints = attrs.getValue("g:bendpoints");
/*  76 */     String name = attrs.getValue("name");
/*  77 */     String priority = attrs.getValue("http://www.jboss.org/drools", "priority");
/*     */ 
/*  80 */     NodeContainer nodeContainer = (NodeContainer)parser.getParent();
/*     */ 
/*  82 */     List connections = null;
/*  83 */     if ((nodeContainer instanceof RuleFlowProcess)) {
/*  84 */       RuleFlowProcess process = (RuleFlowProcess)nodeContainer;
/*     */ 
/*  86 */       connections = (List)process
/*  86 */         .getMetaData("BPMN.Connections");
/*     */ 
/*  87 */       if (connections == null) {
/*  88 */         connections = new ArrayList();
/*  89 */         process.setMetaData("BPMN.Connections", connections);
/*     */       }
/*     */ 
/*     */     }
/*  93 */     else if ((nodeContainer instanceof CompositeNode))
/*     */     {
/*  95 */       CompositeNode compositeNode = (CompositeNode)nodeContainer;
/*     */ 
/*  97 */       connections = (List)compositeNode
/*  97 */         .getMetaData("BPMN.Connections");
/*     */ 
/*  98 */       if (connections == null) {
/*  99 */         connections = new ArrayList();
/* 100 */         compositeNode.setMetaData("BPMN.Connections", connections);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 105 */     SequenceFlow connection = new SequenceFlow(id, sourceRef, targetRef);
/* 106 */     connection.setBendpoints(bendpoints);
/* 107 */     connection.setName(name);
/* 108 */     if (priority != null) {
/* 109 */       connection.setPriority(Integer.parseInt(priority));
/*     */     }
/*     */ 
/* 112 */     connections.add(connection);
/*     */ 
/* 114 */     return connection;
/*     */   }
/*     */ 
/*     */   public Object end(String uri, String localName, ExtensibleXmlParser parser) throws SAXException
/*     */   {
/* 119 */     Element element = parser.endElementBuilder();
/* 120 */     SequenceFlow sequenceFlow = (SequenceFlow)parser.getCurrent();
/*     */ 
/* 122 */     org.w3c.dom.Node xmlNode = element.getFirstChild();
/* 123 */     while (xmlNode != null) {
/* 124 */       String nodeName = xmlNode.getNodeName();
/* 125 */       if ("conditionExpression".equals(nodeName)) {
/* 126 */         String expression = xmlNode.getTextContent();
/*     */ 
/* 128 */         org.w3c.dom.Node languageNode = xmlNode.getAttributes()
/* 128 */           .getNamedItem("language");
/*     */ 
/* 129 */         if (languageNode != null) {
/* 130 */           String language = languageNode.getNodeValue();
/* 131 */           if ("http://www.java.com/java".equals(language)) {
/* 132 */             sequenceFlow.setLanguage("java");
/*     */           }
/* 134 */           else if ("http://www.mvel.org/2.0"
/* 134 */             .equals(language))
/*     */           {
/* 135 */             sequenceFlow.setLanguage("mvel");
/*     */           }
/* 137 */           else if ("http://www.jboss.org/drools/rule"
/* 137 */             .equals(language))
/*     */           {
/* 138 */             sequenceFlow.setType("rule");
/*     */           }
/* 140 */           else if ("http://www.w3.org/1999/XPath"
/* 140 */             .equals(language))
/*     */           {
/* 141 */             sequenceFlow.setLanguage("XPath");
/*     */           }
/* 143 */           else if ("http://www.javascript.com/javascript"
/* 143 */             .equals(language))
/*     */           {
/* 144 */             sequenceFlow.setLanguage("JavaScript");
/*     */           }
/* 146 */           else throw new IllegalArgumentException("Unknown language " + language);
/*     */ 
/*     */         }
/*     */ 
/* 150 */         sequenceFlow.setExpression(expression);
/*     */       }
/* 152 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/* 154 */     return sequenceFlow;
/*     */   }
/*     */ 
/*     */   public Class<?> generateNodeFor() {
/* 158 */     return SequenceFlow.class;
/*     */   }
/*     */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.SequenceFlowHandler
 * JD-Core Version:    0.6.0
 */