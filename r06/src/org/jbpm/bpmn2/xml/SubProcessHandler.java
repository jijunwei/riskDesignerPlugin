/*     */ package org.jbpm.bpmn2.xml;
/*     */ 
/*     */ import java.util.List;

/*     */ import org.drools.core.xml.ExtensibleXmlParser;
/*     */ import org.jbpm.bpmn2.core.Definitions;
/*     */ import org.jbpm.compiler.xml.ProcessBuildData;
/*     */ import org.jbpm.process.core.context.variable.VariableScope;
/*     */ import org.jbpm.workflow.core.NodeContainer;
/*     */ import org.jbpm.workflow.core.node.CompositeContextNode;
/*     */ import org.jbpm.workflow.core.node.EventSubProcessNode;
/*     */ import org.jbpm.workflow.core.node.ForEachNode;
/*     */ import org.jbpm.workflow.core.node.StartNode;
/*     */ import org.kie.api.definition.process.Connection;
import org.kie.api.definition.process.Node;
/*     */ import org.w3c.dom.Element;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class SubProcessHandler extends AbstractNodeHandler
/*     */ {
/*     */   protected org.jbpm.workflow.core.Node createNode(Attributes attrs)
/*     */   {
/*  44 */     CompositeContextNode subProcessNode = new CompositeContextNode();
/*  45 */     String eventSubprocessAttribute = attrs.getValue("triggeredByEvent");
/*  46 */     if ((eventSubprocessAttribute != null) && (Boolean.parseBoolean(eventSubprocessAttribute))) {
/*  47 */       subProcessNode = new EventSubProcessNode();
/*     */     }
/*  49 */     VariableScope variableScope = new VariableScope();
/*  50 */     subProcessNode.addContext(variableScope);
/*  51 */     subProcessNode.setDefaultContext(variableScope);
/*     */ 
/*  53 */     String compensation = attrs.getValue("isForCompensation");
/*  54 */     if (compensation != null) {
/*  55 */       boolean isForCompensation = Boolean.parseBoolean(compensation);
/*  56 */       if (isForCompensation) {
/*  57 */         subProcessNode.setMetaData("isForCompensation", Boolean.valueOf(isForCompensation));
/*     */       }
/*     */     }
/*  60 */     subProcessNode.setAutoComplete(true);
/*  61 */     return subProcessNode;
/*     */   }
/*     */ 
/*     */   public Class generateNodeFor()
/*     */   {
/*  66 */     return CompositeContextNode.class;
/*     */   }
/*     */ 
/*     */   public Object end(String uri, String localName, ExtensibleXmlParser parser) throws SAXException
/*     */   {
/*  71 */     Element element = parser.endElementBuilder();
/*  72 */     org.jbpm.workflow.core.Node node = (org.jbpm.workflow.core.Node)parser.getCurrent();
/*     */ 
/*  75 */     boolean found = false;
/*  76 */     org.w3c.dom.Node xmlNode = element.getFirstChild();
/*  77 */     while (xmlNode != null) {
/*  78 */       String nodeName = xmlNode.getNodeName();
/*  79 */       if ("multiInstanceLoopCharacteristics".equals(nodeName)) {
/*  80 */         Boolean isAsync = Boolean.valueOf(Boolean.parseBoolean((String)node.getMetaData().get("customAsync")));
/*     */ 
/*  82 */         ForEachNode forEachNode = new ForEachNode();
/*  83 */         forEachNode.setId(node.getId());
/*  84 */         forEachNode.setName(node.getName());
/*     */ 
/*  86 */         forEachNode.setAutoComplete(((CompositeContextNode)node).isAutoComplete());
/*     */ 
/*  88 */         for (org.kie.api.definition.process.Node subNode : ((CompositeContextNode)node).getNodes())
/*     */         {
/*  90 */           forEachNode.addNode(subNode);
/*     */         }
/*  92 */         forEachNode.setMetaData("UniqueId", ((CompositeContextNode)node).getMetaData("UniqueId"));
/*  93 */         forEachNode.setMetaData("BPMN.Connections", ((CompositeContextNode)node).getMetaData("BPMN.Connections"));
/*  94 */         VariableScope v = (VariableScope)((CompositeContextNode)node).getDefaultContext("VariableScope");
/*  95 */         ((VariableScope)((CompositeContextNode)forEachNode.internalGetNode(2L)).getDefaultContext("VariableScope")).setVariables(v.getVariables());
/*  96 */         node = forEachNode;
/*  97 */         handleForEachNode(node, element, uri, localName, parser, isAsync.booleanValue());
/*  98 */         found = true;
/*  99 */         break;
/*     */       }
/* 101 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/* 103 */     if (!found) {
/* 104 */       handleCompositeContextNode(node, element, uri, localName, parser);
/*     */     }
/*     */ 
/* 107 */     NodeContainer nodeContainer = (NodeContainer)parser.getParent();
/* 108 */     nodeContainer.addNode(node);
/* 109 */     ((ProcessBuildData)parser.getData()).addNode(node);
/*     */ 
/* 111 */     return node;
/*     */   }
/*     */ 
/*     */   protected void handleCompositeContextNode(org.jbpm.workflow.core.Node node, Element element, String uri, String localName, ExtensibleXmlParser parser)
/*     */     throws SAXException
/*     */   {
/* 117 */     super.handleNode(node, element, uri, localName, parser);
/* 118 */     CompositeContextNode compositeNode = (CompositeContextNode)node;
/*     */ 
/* 120 */     List connections = (List)compositeNode
/* 120 */       .getMetaData("BPMN.Connections");
/*     */ 
/* 122 */     handleScript(compositeNode, element, "onEntry");
/* 123 */     handleScript(compositeNode, element, "onExit");
/*     */ 
/* 125 */     List throwLinks = (List)compositeNode.getMetaData("BPMN.ThrowLinks");
/* 126 */     ProcessHandler.linkIntermediateLinks(compositeNode, throwLinks);
/*     */ 
/* 128 */     ProcessHandler.linkConnections(compositeNode, connections);
/* 129 */     ProcessHandler.linkBoundaryEvents(compositeNode);
/*     */ 
/* 133 */     List associations = (List)compositeNode.getMetaData("BPMN.Associations");
/* 134 */     ProcessHandler.linkAssociations((Definitions)compositeNode.getMetaData("Definitions"), compositeNode, associations);
/*     */   }
/*     */ 
/*     */   protected void handleForEachNode(org.jbpm.workflow.core.Node node, Element element, String uri, String localName, ExtensibleXmlParser parser, boolean isAsync)
/*     */     throws SAXException
/*     */   {
/* 152 */     super.handleNode(node, element, uri, localName, parser);
/* 153 */     ForEachNode forEachNode = (ForEachNode)node;
/* 154 */     org.w3c.dom.Node xmlNode = element.getFirstChild();
/* 155 */     while (xmlNode != null) {
/* 156 */       String nodeName = xmlNode.getNodeName();
/* 157 */       if ("ioSpecification".equals(nodeName))
/* 158 */         readIoSpecification(xmlNode, this.dataInputs, this.dataOutputs);
/* 159 */       else if ("dataInputAssociation".equals(nodeName))
/* 160 */         readDataInputAssociation(xmlNode, this.inputAssociation);
/* 161 */       else if ("dataOutputAssociation".equals(nodeName))
/* 162 */         readDataOutputAssociation(xmlNode, this.outputAssociation);
/* 163 */       else if ("multiInstanceLoopCharacteristics".equals(nodeName)) {
/* 164 */         readMultiInstanceLoopCharacteristics(xmlNode, forEachNode, parser);
/*     */       }
/* 166 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/* 168 */     handleScript(forEachNode, element, "onEntry");
/* 169 */     handleScript(forEachNode, element, "onExit");
/*     */ 
/* 172 */     List connections = (List)forEachNode
/* 172 */       .getMetaData("BPMN.Connections");
/*     */ 
/* 173 */     ProcessHandler.linkConnections(forEachNode, connections);
/* 174 */     ProcessHandler.linkBoundaryEvents(forEachNode);
/*     */ 
/* 179 */     List associations = (List)forEachNode.getMetaData("BPMN.Associations");
/* 180 */     ProcessHandler.linkAssociations((Definitions)forEachNode.getMetaData("Definitions"), forEachNode, associations);
/* 181 */     applyAsync(node, isAsync);
/*     */   }
/*     */ 
/*     */   protected void applyAsync(Node node, boolean isAsync)
/*     */   {
/*     */     
/* 185 */     for (Node subNode : ((CompositeContextNode)node).getNodes())
/* 186 */       if (isAsync) {
/* 187 */         List<Connection> incoming = subNode.getIncomingConnections("DROOLS_DEFAULT");
/* 188 */         if (incoming != null)
/* 189 */           for (Connection con : incoming)
/* 190 */             if ((con.getFrom() instanceof StartNode)) {
/* 191 */               ((org.jbpm.workflow.core.Node)subNode).setMetaData("customAsync", Boolean.toString(isAsync));
/* 192 */               return;
/*     */             }
/*     */       }
/*     */   }
/*     */ 
/*     */   public void writeNode(org.jbpm.workflow.core.Node node, StringBuilder xmlDump, int metaDataType)
/*     */   {
/* 202 */     throw new IllegalArgumentException("Writing out should be handled by specific handlers");
/*     */   }
/*     */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.SubProcessHandler
 * JD-Core Version:    0.6.0
 */