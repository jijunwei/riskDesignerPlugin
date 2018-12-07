/*     */ package org.jbpm.bpmn2.xml;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.drools.compiler.compiler.xml.XmlDumper;
/*     */ import org.drools.core.xml.BaseAbstractHandler;
/*     */ import org.drools.core.xml.ExtensibleXmlParser;
/*     */ import org.drools.core.xml.Handler;
/*     */ import org.jbpm.bpmn2.core.Association;
/*     */ import org.jbpm.bpmn2.core.Definitions;
/*     */ import org.jbpm.bpmn2.core.Error;
/*     */ import org.jbpm.bpmn2.core.ItemDefinition;
/*     */ import org.jbpm.bpmn2.core.Lane;
/*     */ import org.jbpm.bpmn2.core.SequenceFlow;
/*     */ import org.jbpm.bpmn2.core.Signal;
/*     */ import org.jbpm.compiler.xml.ProcessBuildData;
/*     */ import org.jbpm.process.core.context.variable.Variable;
/*     */ import org.jbpm.process.core.datatype.DataType;
/*     */ import org.jbpm.process.core.datatype.impl.type.BooleanDataType;
/*     */ import org.jbpm.process.core.datatype.impl.type.FloatDataType;
/*     */ import org.jbpm.process.core.datatype.impl.type.IntegerDataType;
/*     */ import org.jbpm.process.core.datatype.impl.type.ObjectDataType;
/*     */ import org.jbpm.process.core.datatype.impl.type.StringDataType;
/*     */ import org.jbpm.ruleflow.core.RuleFlowProcess;
/*     */ import org.jbpm.workflow.core.DroolsAction;
/*     */ import org.jbpm.workflow.core.impl.DroolsConsequenceAction;
/*     */ import org.jbpm.workflow.core.impl.ExtendedNodeImpl;
/*     */ import org.jbpm.workflow.core.impl.NodeImpl;
/*     */ import org.jbpm.workflow.core.node.ActionNode;
/*     */ import org.jbpm.workflow.core.node.EndNode;
/*     */ import org.jbpm.workflow.core.node.EventNode;
/*     */ import org.jbpm.workflow.core.node.ForEachNode;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ public abstract class AbstractNodeHandler extends BaseAbstractHandler
/*     */   implements Handler
/*     */ {
/*     */   protected static final Logger logger;
/*     */   static final String PROCESS_INSTANCE_SIGNAL_EVENT = "kcontext.getProcessInstance().signalEvent(";
/*     */   static final String RUNTIME_SIGNAL_EVENT = "kcontext.getKnowledgeRuntime().signalEvent(";
/*     */   static final String RUNTIME_MANAGER_SIGNAL_EVENT = "((org.kie.api.runtime.manager.RuntimeManager)kcontext.getKnowledgeRuntime().getEnvironment().get(\"RuntimeManager\")).signalEvent(";
/*     */   protected static final String EOL;
/*  76 */   protected Map<String, String> dataInputs = new HashMap();
/*  77 */   protected Map<String, String> dataOutputs = new HashMap();
/*  78 */   protected Map<String, String> inputAssociation = new HashMap();
/*  79 */   protected Map<String, String> outputAssociation = new HashMap();
/*     */   private static final String SIGNAL_NAMES = "signalNames";
/*     */ 
/*     */   public AbstractNodeHandler()
/*     */   {
/*  82 */     initValidParents();
/*  83 */     initValidPeers();
/*  84 */     this.allowNesting = true;
/*     */   }
/*     */ 
/*     */   protected void initValidParents() {
/*  88 */     this.validParents = new HashSet();
/*  89 */     this.validParents.add(org.jbpm.workflow.core.NodeContainer.class);
/*     */   }
/*     */ 
/*     */   protected void initValidPeers() {
/*  93 */     this.validPeers = new HashSet();
/*  94 */     this.validPeers.add(null);
/*  95 */     this.validPeers.add(Lane.class);
/*  96 */     this.validPeers.add(Variable.class);
/*  97 */     this.validPeers.add(org.jbpm.workflow.core.Node.class);
/*  98 */     this.validPeers.add(SequenceFlow.class);
/*  99 */     this.validPeers.add(Lane.class);
/* 100 */     this.validPeers.add(Association.class);
/*     */   }
/*     */ 
/*     */   public Object start(String uri, String localName, Attributes attrs, ExtensibleXmlParser parser) throws SAXException
/*     */   {
/* 105 */     parser.startElementBuilder(localName, attrs);
/* 106 */     org.jbpm.workflow.core.Node node = createNode(attrs);
/* 107 */     String id = attrs.getValue("id");
/* 108 */     node.setMetaData("UniqueId", id);
/* 109 */     String name = attrs.getValue("name");
/* 110 */     node.setName(name);
/* 111 */     if ("true".equalsIgnoreCase(System.getProperty("jbpm.v5.id.strategy")))
/*     */     {
/*     */       try {
/* 114 */         id = id.substring(1);
/*     */ 
/* 116 */         id = id.substring(id.lastIndexOf("-") + 1);
/* 117 */         node.setId(Integer.parseInt(id));
/*     */       }
/*     */       catch (NumberFormatException e) {
/* 120 */         long newId = 0L;
/* 121 */         org.jbpm.workflow.core.NodeContainer nodeContainer = (org.jbpm.workflow.core.NodeContainer)parser.getParent();
/* 122 */         for (org.kie.api.definition.process.Node n : nodeContainer.getNodes()) {
/* 123 */           if (n.getId() > newId) {
/* 124 */             newId = n.getId();
/*     */           }
/*     */         }
/* 127 */         node.setId(++newId);
/*     */       }
/*     */     } else {
/* 130 */       AtomicInteger idGen = (AtomicInteger)parser.getMetaData().get("idGen");
/* 131 */       node.setId(idGen.getAndIncrement());
/*     */     }
/* 133 */     return node;
/*     */   }
/*     */ 
/*     */   protected abstract org.jbpm.workflow.core.Node createNode(Attributes paramAttributes);
/*     */ 
/*     */   public Object end(String uri, String localName, ExtensibleXmlParser parser) throws SAXException {
/* 140 */     Element element = parser.endElementBuilder();
/* 141 */     org.jbpm.workflow.core.Node node = (org.jbpm.workflow.core.Node)parser.getCurrent();
/* 142 */     handleNode(node, element, uri, localName, parser);
/* 143 */     org.jbpm.workflow.core.NodeContainer nodeContainer = (org.jbpm.workflow.core.NodeContainer)parser.getParent();
/* 144 */     nodeContainer.addNode(node);
/* 145 */     ((ProcessBuildData)parser.getData()).addNode(node);
/* 146 */     return node;
/*     */   }
/*     */ 
/*     */   protected void handleNode(org.jbpm.workflow.core.Node node, Element element, String uri, String localName, ExtensibleXmlParser parser)
/*     */     throws SAXException
/*     */   {
/* 152 */     String x = element.getAttribute("x");
/* 153 */     if ((x != null) && (x.length() != 0)) {
/*     */       try {
/* 155 */         node.setMetaData("x", Integer.valueOf(Integer.parseInt(x)));
/*     */       } catch (NumberFormatException exc) {
/* 157 */         throw new SAXParseException(new StringBuilder().append("<").append(localName).append("> requires an Integer 'x' attribute").toString(), parser.getLocator());
/*     */       }
/*     */     }
/* 160 */     String y = element.getAttribute("y");
/* 161 */     if ((y != null) && (y.length() != 0)) {
/*     */       try {
/* 163 */         node.setMetaData("y", new Integer(y));
/*     */       } catch (NumberFormatException exc) {
/* 165 */         throw new SAXParseException(new StringBuilder().append("<").append(localName).append("> requires an Integer 'y' attribute").toString(), parser.getLocator());
/*     */       }
/*     */     }
/* 168 */     String width = element.getAttribute("width");
/* 169 */     if ((width != null) && (width.length() != 0)) {
/*     */       try {
/* 171 */         node.setMetaData("width", new Integer(width));
/*     */       } catch (NumberFormatException exc) {
/* 173 */         throw new SAXParseException(new StringBuilder().append("<").append(localName).append("> requires an Integer 'width' attribute").toString(), parser.getLocator());
/*     */       }
/*     */     }
/* 176 */     String height = element.getAttribute("height");
/* 177 */     if ((height != null) && (height.length() != 0))
/*     */       try {
/* 179 */         node.setMetaData("height", new Integer(height));
/*     */       } catch (NumberFormatException exc) {
/* 181 */         throw new SAXParseException(new StringBuilder().append("<").append(localName).append("> requires an Integer 'height' attribute").toString(), parser.getLocator());
/*     */       }
/*     */   }
/*     */ 
/*     */   public abstract void writeNode(org.jbpm.workflow.core.Node paramNode, StringBuilder paramStringBuilder, int paramInt);
/*     */ 
/*     */   protected void writeNode(String name, org.jbpm.workflow.core.Node node, StringBuilder xmlDump, int metaDataType)
/*     */   {
/* 191 */     xmlDump.append(new StringBuilder().append("    <").append(name).append(" ").toString());
/* 192 */     xmlDump.append(new StringBuilder().append("id=\"").append(XmlBPMNProcessDumper.getUniqueNodeId(node)).append("\" ").toString());
/* 193 */     if (node.getName() != null) {
/* 194 */       xmlDump.append(new StringBuilder().append("name=\"").append(XmlBPMNProcessDumper.replaceIllegalCharsAttribute(node.getName())).append("\" ").toString());
/*     */     }
/* 196 */     if (metaDataType == 1) {
/* 197 */       Integer x = (Integer)node.getMetaData().get("x");
/* 198 */       Integer y = (Integer)node.getMetaData().get("y");
/* 199 */       Integer width = (Integer)node.getMetaData().get("width");
/* 200 */       Integer height = (Integer)node.getMetaData().get("height");
/* 201 */       if ((x != null) && (x.intValue() != 0)) {
/* 202 */         xmlDump.append(new StringBuilder().append("g:x=\"").append(x).append("\" ").toString());
/*     */       }
/* 204 */       if ((y != null) && (y.intValue() != 0)) {
/* 205 */         xmlDump.append(new StringBuilder().append("g:y=\"").append(y).append("\" ").toString());
/*     */       }
/* 207 */       if ((width != null) && (width.intValue() != -1)) {
/* 208 */         xmlDump.append(new StringBuilder().append("g:width=\"").append(width).append("\" ").toString());
/*     */       }
/* 210 */       if ((height != null) && (height.intValue() != -1))
/* 211 */         xmlDump.append(new StringBuilder().append("g:height=\"").append(height).append("\" ").toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void endNode(StringBuilder xmlDump)
/*     */   {
/* 217 */     xmlDump.append(new StringBuilder().append("/>").append(EOL).toString());
/*     */   }
/*     */ 
/*     */   protected void endNode(String name, StringBuilder xmlDump) {
/* 221 */     xmlDump.append(new StringBuilder().append("    </").append(name).append(">").append(EOL).toString());
/*     */   }
/*     */ 
/*     */   protected void handleScript(ExtendedNodeImpl node, Element element, String type) {
/* 225 */     NodeList nodeList = element.getChildNodes();
/* 226 */     for (int i = 0; i < nodeList.getLength(); i++)
/* 227 */       if ((nodeList.item(i) instanceof Element)) {
/* 228 */         Element xmlNode = (Element)nodeList.item(i);
/* 229 */         String nodeName = xmlNode.getNodeName();
/* 230 */         if (nodeName.equals("extensionElements")) {
/* 231 */           NodeList subNodeList = xmlNode.getChildNodes();
/* 232 */           for (int j = 0; j < subNodeList.getLength(); j++) {
/* 233 */             org.w3c.dom.Node subXmlNode = subNodeList.item(j);
/* 234 */             if (subXmlNode.getNodeName().contains(new StringBuilder().append(type).append("-script").toString())) {
/* 235 */               List actions = node.getActions(type);
/* 236 */               if (actions == null) {
/* 237 */                 actions = new ArrayList();
/* 238 */                 node.setActions(type, actions);
/*     */               }
/* 240 */               DroolsAction action = extractScript((Element)subXmlNode);
/* 241 */               actions.add(action);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   public static DroolsAction extractScript(Element xmlNode)
/*     */   {
/* 250 */     String dialect = "mvel";
/* 251 */     if ("http://www.java.com/java".equals(xmlNode.getAttribute("scriptFormat")))
/* 252 */       dialect = "java";
/* 253 */     else if ("http://www.javascript.com/javascript".equals(xmlNode.getAttribute("scriptFormat"))) {
/* 254 */       dialect = "JavaScript";
/*     */     }
/* 256 */     NodeList subNodeList = xmlNode.getChildNodes();
/* 257 */     for (int j = 0; j < subNodeList.getLength(); j++) {
/* 258 */       if ((subNodeList.item(j) instanceof Element)) {
/* 259 */         Element subXmlNode = (Element)subNodeList.item(j);
/* 260 */         if ("script".equals(subXmlNode.getNodeName())) {
/* 261 */           String consequence = subXmlNode.getTextContent();
/* 262 */           DroolsConsequenceAction action = new DroolsConsequenceAction(dialect, consequence);
/* 263 */           return action;
/*     */         }
/*     */       }
/*     */     }
/* 267 */     return new DroolsConsequenceAction("mvel", "");
/*     */   }
/*     */ 
/*     */   protected void writeMetaData(org.jbpm.workflow.core.Node node, StringBuilder xmlDump) {
/* 271 */     XmlBPMNProcessDumper.writeMetaData(getMetaData(node), xmlDump);
/*     */   }
/*     */ 
/*     */   protected Map<String, Object> getMetaData(org.jbpm.workflow.core.Node node) {
/* 275 */     return XmlBPMNProcessDumper.getMetaData(node.getMetaData());
/*     */   }
/*     */ 
/*     */   protected void writeExtensionElements(org.jbpm.workflow.core.Node node, StringBuilder xmlDump) {
/* 279 */     if (containsExtensionElements(node)) {
/* 280 */       xmlDump.append(new StringBuilder().append("      <extensionElements>").append(EOL).toString());
/* 281 */       if ((node instanceof ExtendedNodeImpl)) {
/* 282 */         writeScripts("onEntry", ((ExtendedNodeImpl)node).getActions("onEntry"), xmlDump);
/* 283 */         writeScripts("onExit", ((ExtendedNodeImpl)node).getActions("onExit"), xmlDump);
/*     */       }
/* 285 */       writeMetaData(node, xmlDump);
/* 286 */       xmlDump.append(new StringBuilder().append("      </extensionElements>").append(EOL).toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected boolean containsExtensionElements(org.jbpm.workflow.core.Node node) {
/* 291 */     if (!getMetaData(node).isEmpty()) {
/* 292 */       return true;
/*     */     }
/*     */ 
/* 295 */     return ((node instanceof ExtendedNodeImpl)) && (((ExtendedNodeImpl)node).containsActions());
/*     */   }
/*     */ 
/*     */   protected void writeScripts(String type, List<DroolsAction> actions, StringBuilder xmlDump)
/*     */   {
/* 301 */     if ((actions != null) && (actions.size() > 0))
/* 302 */       for (DroolsAction action : actions)
/* 303 */         writeScript(action, type, xmlDump);
/*     */   }
/*     */ 
/*     */   public static void writeScript(DroolsAction action, String type, StringBuilder xmlDump)
/*     */   {
/* 309 */     if ((action instanceof DroolsConsequenceAction)) {
/* 310 */       DroolsConsequenceAction consequenceAction = (DroolsConsequenceAction)action;
/* 311 */       xmlDump.append(new StringBuilder().append("        <tns:").append(type).append("-script").toString());
/* 312 */       String name = consequenceAction.getName();
/* 313 */       if (name != null) {
/* 314 */         xmlDump.append(new StringBuilder().append(" name=\"").append(name).append("\"").toString());
/*     */       }
/* 316 */       String dialect = consequenceAction.getDialect();
/* 317 */       if ("java".equals(dialect))
/* 318 */         xmlDump.append(" scriptFormat=\"http://www.java.com/java\"");
/* 319 */       else if ("JavaScript".equals(dialect)) {
/* 320 */         xmlDump.append(" scriptFormat=\"http://www.javascript.com/javascript\"");
/*     */       }
/* 322 */       String consequence = consequenceAction.getConsequence();
/* 323 */       if (consequence != null) {
/* 324 */         xmlDump.append(new StringBuilder().append(">").append(EOL).append("          <tns:script>")
/* 325 */           .append(XmlDumper.replaceIllegalChars(consequence
/* 325 */           .trim())).append("</tns:script>").append(EOL).toString());
/* 326 */         xmlDump.append(new StringBuilder().append("        </tns:").append(type).append("-script>").append(EOL).toString());
/*     */       } else {
/* 328 */         xmlDump.append(new StringBuilder().append("/>").append(EOL).toString());
/*     */       }
/*     */     } else {
/* 331 */       throw new IllegalArgumentException(new StringBuilder().append("Unknown action ").append(action).toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void readIoSpecification(org.w3c.dom.Node xmlNode, Map<String, String> dataInputs, Map<String, String> dataOutputs)
/*     */   {
/* 337 */     org.w3c.dom.Node subNode = xmlNode.getFirstChild();
/* 338 */     while ((subNode instanceof Element)) {
/* 339 */       String subNodeName = subNode.getNodeName();
/* 340 */       if ("dataInput".equals(subNodeName)) {
/* 341 */         String id = ((Element)subNode).getAttribute("id");
/* 342 */         String inputName = ((Element)subNode).getAttribute("name");
/* 343 */         dataInputs.put(id, inputName);
/*     */       }
/* 345 */       if ("dataOutput".equals(subNodeName)) {
/* 346 */         String id = ((Element)subNode).getAttribute("id");
/* 347 */         String outputName = ((Element)subNode).getAttribute("name");
/* 348 */         dataOutputs.put(id, outputName);
/*     */       }
/* 350 */       subNode = subNode.getNextSibling();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void readDataInputAssociation(org.w3c.dom.Node xmlNode, Map<String, String> forEachNodeInputAssociation)
/*     */   {
/* 356 */     org.w3c.dom.Node subNode = xmlNode.getFirstChild();
/* 357 */     if ("sourceRef".equals(subNode.getNodeName())) {
/* 358 */       String source = subNode.getTextContent();
/*     */ 
/* 360 */       subNode = subNode.getNextSibling();
/* 361 */       String target = subNode.getTextContent();
/* 362 */       forEachNodeInputAssociation.put(target, source);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void readDataOutputAssociation(org.w3c.dom.Node xmlNode, Map<String, String> forEachNodeOutputAssociation)
/*     */   {
/* 368 */     org.w3c.dom.Node subNode = xmlNode.getFirstChild();
/* 369 */     if ("sourceRef".equals(subNode.getNodeName())) {
/* 370 */       String source = subNode.getTextContent();
/*     */ 
/* 372 */       subNode = subNode.getNextSibling();
/* 373 */       String target = subNode.getTextContent();
/* 374 */       forEachNodeOutputAssociation.put(source, target);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void readMultiInstanceLoopCharacteristics(org.w3c.dom.Node xmlNode, ForEachNode forEachNode, ExtensibleXmlParser parser)
/*     */   {
/* 382 */     org.w3c.dom.Node subNode = xmlNode.getFirstChild();
/* 383 */     while (subNode != null) {
/* 384 */       String nodeName = subNode.getNodeName();
/* 385 */       if ("inputDataItem".equals(nodeName)) {
/* 386 */         String variableName = ((Element)subNode).getAttribute("id");
/* 387 */         String itemSubjectRef = ((Element)subNode).getAttribute("itemSubjectRef");
/* 388 */         DataType dataType = null;
/*     */ 
/* 390 */         Map itemDefinitions = (Map)((ProcessBuildData)parser
/* 390 */           .getData()).getMetaData("ItemDefinitions");
/* 391 */         dataType = getDataType(itemSubjectRef, itemDefinitions, parser.getClassLoader());
/*     */ 
/* 393 */         if ((variableName != null) && (variableName.trim().length() > 0))
/* 394 */           forEachNode.setVariable(variableName, dataType);
/*     */       }
/* 396 */       else if ("outputDataItem".equals(nodeName)) {
/* 397 */         String variableName = ((Element)subNode).getAttribute("id");
/* 398 */         String itemSubjectRef = ((Element)subNode).getAttribute("itemSubjectRef");
/* 399 */         DataType dataType = null;
/*     */ 
/* 401 */         Map itemDefinitions = (Map)((ProcessBuildData)parser
/* 401 */           .getData()).getMetaData("ItemDefinitions");
/* 402 */         dataType = getDataType(itemSubjectRef, itemDefinitions, parser.getClassLoader());
/*     */ 
/* 404 */         if ((variableName != null) && (variableName.trim().length() > 0))
/* 405 */           forEachNode.setOutputVariable(variableName, dataType);
/*     */       }
/* 407 */       else if ("loopDataOutputRef".equals(nodeName))
/*     */       {
/* 409 */         String outputDataRef = ((Element)subNode).getTextContent();
/*     */ 
/* 411 */         if ((outputDataRef != null) && (outputDataRef.trim().length() > 0)) {
/* 412 */           String collectionName = (String)this.outputAssociation.get(outputDataRef);
/* 413 */           if (collectionName == null) {
/* 414 */             collectionName = (String)this.dataOutputs.get(outputDataRef);
/*     */           }
/* 416 */           forEachNode.setOutputCollectionExpression(collectionName);
/*     */         }
/*     */ 
/* 419 */         forEachNode.setMetaData("MICollectionOutput", outputDataRef);
/*     */       }
/* 421 */       else if ("loopDataInputRef".equals(nodeName))
/*     */       {
/* 423 */         String inputDataRef = ((Element)subNode).getTextContent();
/*     */ 
/* 425 */         if ((inputDataRef != null) && (inputDataRef.trim().length() > 0)) {
/* 426 */           String collectionName = (String)this.inputAssociation.get(inputDataRef);
/* 427 */           if (collectionName == null) {
/* 428 */             collectionName = (String)this.dataInputs.get(inputDataRef);
/*     */           }
/* 430 */           forEachNode.setCollectionExpression(collectionName);
/*     */         }
/*     */ 
/* 433 */         forEachNode.setMetaData("MICollectionInput", inputDataRef);
/*     */       }
/* 435 */       else if ("completionCondition".equals(nodeName)) {
/* 436 */         String expression = subNode.getTextContent();
/* 437 */         forEachNode.setCompletionConditionExpression(expression);
/*     */       }
/* 439 */       subNode = subNode.getNextSibling();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected DataType getDataType(String itemSubjectRef, Map<String, ItemDefinition> itemDefinitions, ClassLoader cl) {
/* 444 */     DataType dataType = new ObjectDataType();
/* 445 */     if (itemDefinitions == null) {
/* 446 */       return dataType;
/*     */     }
/* 448 */     ItemDefinition itemDefinition = (ItemDefinition)itemDefinitions.get(itemSubjectRef);
/* 449 */     if (itemDefinition != null) {
/* 450 */       String structureRef = itemDefinition.getStructureRef();
/*     */ 
/* 452 */       if (("java.lang.Boolean".equals(structureRef)) || ("Boolean".equals(structureRef))) {
/* 453 */         dataType = new BooleanDataType();
/*     */       }
/* 455 */       else if (("java.lang.Integer".equals(structureRef)) || ("Integer".equals(structureRef))) {
/* 456 */         dataType = new IntegerDataType();
/*     */       }
/* 458 */       else if (("java.lang.Float".equals(structureRef)) || ("Float".equals(structureRef))) {
/* 459 */         dataType = new FloatDataType();
/*     */       }
/* 461 */       else if (("java.lang.String".equals(structureRef)) || ("String".equals(structureRef))) {
/* 462 */         dataType = new StringDataType();
/*     */       }
/* 464 */       else if (("java.lang.Object".equals(structureRef)) || ("Object".equals(structureRef))) {
/* 465 */         dataType = new ObjectDataType(structureRef);
/*     */       }
/*     */       else {
/* 468 */         dataType = new ObjectDataType(structureRef, cl);
/*     */       }
/*     */     }
/*     */ 
/* 472 */     return dataType;
/*     */   }
/*     */ 
/*     */   protected String getErrorIdForErrorCode(String errorCode, org.jbpm.workflow.core.Node node) {
/* 476 */     org.kie.api.definition.process.NodeContainer parent = node.getNodeContainer();
/* 477 */     while ((!(parent instanceof RuleFlowProcess)) && ((parent instanceof org.jbpm.workflow.core.Node))) {
/* 478 */       parent = ((org.jbpm.workflow.core.Node)parent).getNodeContainer();
/*     */     }
/* 480 */     if (!(parent instanceof RuleFlowProcess)) {
/* 481 */       throw new RuntimeException(new StringBuilder().append("This should never happen: !(parent instanceof RuleFlowProcess): parent is ").append(parent.getClass().getSimpleName()).toString());
/*     */     }
/* 483 */     List<Error> errors = ((Definitions)((RuleFlowProcess)parent).getMetaData("Definitions")).getErrors();
/* 484 */     Error error = null;
/* 485 */     for (Error listError : errors) {
/* 486 */       if (errorCode.equals(listError.getErrorCode())) {
/* 487 */         error = listError;
/* 488 */         break;
/* 489 */       }if (errorCode.equals(listError.getId())) {
/* 490 */         error = listError;
/* 491 */         break;
/*     */       }
/*     */     }
/* 494 */     if (error == null) {
/* 495 */       throw new IllegalArgumentException(new StringBuilder().append("Could not find error with errorCode ").append(errorCode).toString());
/*     */     }
/* 497 */     return error.getId();
/*     */   }
/*     */ 
/*     */   protected void handleThrowCompensationEventNode(org.jbpm.workflow.core.Node node, Element element, String uri, String localName, ExtensibleXmlParser parser)
/*     */   {
/* 502 */     org.w3c.dom.Node xmlNode = element.getFirstChild();
/* 503 */     assert (((node instanceof ActionNode)) || ((node instanceof EndNode))) : new StringBuilder().append("Node is neither an ActionNode nor an EndNode but a ")
/* 504 */       .append(node
/* 504 */       .getClass().getSimpleName()).toString();
/* 505 */     while (xmlNode != null) {
/* 506 */       if ("compensateEventDefinition".equals(xmlNode.getNodeName())) {
/* 507 */         String activityRef = ((Element)xmlNode).getAttribute("activityRef");
/* 508 */         if (activityRef == null) {
/* 509 */           activityRef = "";
/*     */         }
/* 511 */         node.setMetaData("compensation-activityRef", activityRef);
/*     */ 
/* 521 */         String nodeId = (String)node.getMetaData().get("UniqueId");
/* 522 */         String waitForCompletionString = ((Element)xmlNode).getAttribute("waitForCompletion");
/* 523 */         boolean waitForCompletion = true;
/* 524 */         if ((waitForCompletionString != null) && (waitForCompletionString.length() > 0)) {
/* 525 */           waitForCompletion = Boolean.parseBoolean(waitForCompletionString);
/*     */         }
/* 527 */         if (!waitForCompletion) {
/* 528 */           throw new IllegalArgumentException(new StringBuilder().append("Asynchronous compensation [").append(nodeId).append(", ").append(node.getName()).append("] is not yet supported!").toString());
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 533 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void writeVariableName(EventNode eventNode, StringBuilder xmlDump) {
/* 538 */     if (eventNode.getVariableName() != null) {
/* 539 */       xmlDump.append(new StringBuilder().append("      <dataOutput id=\"").append(XmlBPMNProcessDumper.getUniqueNodeId(eventNode)).append("_Output\" name=\"event\" />").append(EOL).toString());
/* 540 */       xmlDump.append(new StringBuilder().append("      <dataOutputAssociation>").append(EOL).toString());
/* 541 */       xmlDump.append(new StringBuilder().append("      <sourceRef>")
/* 542 */         .append(XmlBPMNProcessDumper.getUniqueNodeId(eventNode))
/* 542 */         .append("_Output</sourceRef>").append(EOL).append("      <targetRef>")
/* 543 */         .append(XmlDumper.replaceIllegalChars(eventNode
/* 543 */         .getVariableName())).append("</targetRef>").append(EOL).toString());
/* 544 */       xmlDump.append(new StringBuilder().append("      </dataOutputAssociation>").append(EOL).toString());
/* 545 */       xmlDump.append(new StringBuilder().append("      <outputSet>").append(EOL).toString());
/* 546 */       xmlDump.append(new StringBuilder().append("        <dataOutputRefs>").append(XmlBPMNProcessDumper.getUniqueNodeId(eventNode)).append("_Output</dataOutputRefs>").append(EOL).toString());
/* 547 */       xmlDump.append(new StringBuilder().append("      </outputSet>").append(EOL).toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected String getSignalExpression(NodeImpl node, String signalName, String variable) {
/* 552 */     String signalExpression = "kcontext.getKnowledgeRuntime().signalEvent(";
/* 553 */     String scope = (String)node.getMetaData("customScope");
/* 554 */     if ("processInstance".equalsIgnoreCase(scope))
/* 555 */       signalExpression = new StringBuilder().append("kcontext.getProcessInstance().signalEvent(org.jbpm.process.instance.impl.util.VariableUtil.resolveVariable(\"").append(signalName).append("\", kcontext.getNodeInstance()), ").append(variable == null ? "null" : variable).append(");").toString();
/* 556 */     else if (("runtimeManager".equalsIgnoreCase(scope)) || ("project".equalsIgnoreCase(scope)))
/* 557 */       signalExpression = new StringBuilder().append("((org.kie.api.runtime.manager.RuntimeManager)kcontext.getKnowledgeRuntime().getEnvironment().get(\"RuntimeManager\")).signalEvent(org.jbpm.process.instance.impl.util.VariableUtil.resolveVariable(\"").append(signalName).append("\", kcontext.getNodeInstance()), ").append(variable == null ? "null" : variable).append(");").toString();
/* 558 */     else if ("external".equalsIgnoreCase(scope)) {
/* 559 */       signalExpression = new StringBuilder().append("org.drools.core.process.instance.impl.WorkItemImpl workItem = new org.drools.core.process.instance.impl.WorkItemImpl();").append(EOL).append("workItem.setName(\"External Send Task\");").append(EOL).append("workItem.setNodeInstanceId(kcontext.getNodeInstance().getId());").append(EOL).append("workItem.setProcessInstanceId(kcontext.getProcessInstance().getId());").append(EOL).append("workItem.setNodeId(kcontext.getNodeInstance().getNodeId());").append(EOL).append("workItem.setDeploymentId((String) kcontext.getKnowledgeRuntime().getEnvironment().get(\"deploymentId\"));").append(EOL).append("workItem.setParameter(\"Signal\", org.jbpm.process.instance.impl.util.VariableUtil.resolveVariable(\"").append(signalName).append("\", kcontext.getNodeInstance()));").append(EOL).append("workItem.setParameter(\"SignalProcessInstanceId\", kcontext.getVariable(\"SignalProcessInstanceId\"));").append(EOL).append("workItem.setParameter(\"SignalWorkItemId\", kcontext.getVariable(\"SignalWorkItemId\"));").append(EOL).append("workItem.setParameter(\"SignalDeploymentId\", kcontext.getVariable(\"SignalDeploymentId\"));").append(EOL).append(variable == null ? "" : new StringBuilder().append("workItem.setParameter(\"Data\", ").append(variable).append(");").append(EOL).toString()).append("((org.drools.core.process.instance.WorkItemManager) kcontext.getKnowledgeRuntime().getWorkItemManager()).internalExecuteWorkItem(workItem);").toString();
/*     */     }
/*     */     else
/*     */     {
/* 572 */       signalExpression = new StringBuilder().append(signalExpression).append("org.jbpm.process.instance.impl.util.VariableUtil.resolveVariable(\"").append(signalName).append("\", kcontext.getNodeInstance()), ").append(variable == null ? "null" : variable).append(");").toString();
/*     */     }
/*     */ 
/* 575 */     return signalExpression;
/*     */   }
/*     */ 
/*     */   protected String checkSignalAndConvertToRealSignalNam(ExtensibleXmlParser parser, String signalName)
/*     */   {
/* 581 */     ProcessBuildData buildData = (ProcessBuildData)parser.getData();
/*     */ 
/* 583 */     Set signalNames = (Set)buildData.getMetaData("signalNames");
/* 584 */     if (signalNames == null) {
/* 585 */       signalNames = new HashSet();
/* 586 */       buildData.setMetaData("signalNames", signalNames);
/*     */     }
/* 588 */     signalNames.add(signalName);
/*     */ 
/* 590 */     Map signals = (Map)buildData.getMetaData("Signals");
/* 591 */     if ((signals != null) && 
/* 592 */       (signals.containsKey(signalName))) {
/* 593 */       Signal signal = (Signal)signals.get(signalName);
/* 594 */       signalName = signal.getName();
/* 595 */       if (signalName == null) {
/* 596 */         throw new IllegalArgumentException("Signal definition must have a name attribute");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 601 */     return signalName;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  69 */     logger = LoggerFactory.getLogger(AbstractNodeHandler.class);
/*     */ 
/*  75 */     EOL = System.getProperty("line.separator");
/*     */   }
/*     */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.AbstractNodeHandler
 * JD-Core Version:    0.6.0
 */