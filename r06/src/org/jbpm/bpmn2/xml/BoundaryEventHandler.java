/*     */ package org.jbpm.bpmn2.xml;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.drools.compiler.compiler.xml.XmlDumper;
/*     */ import org.drools.core.xml.ExtensibleXmlParser;
/*     */ import org.jbpm.bpmn2.core.Error;
/*     */ import org.jbpm.bpmn2.core.Escalation;
/*     */ import org.jbpm.bpmn2.core.ItemDefinition;
/*     */ import org.jbpm.bpmn2.core.Message;
/*     */ import org.jbpm.compiler.xml.ProcessBuildData;
/*     */ import org.jbpm.process.core.event.EventTransformerImpl;
/*     */ import org.jbpm.process.core.event.EventTypeFilter;
/*     */ import org.jbpm.process.core.event.NonAcceptingEventTypeFilter;
/*     */ import org.jbpm.process.core.impl.DataTransformerRegistry;
/*     */ import org.jbpm.ruleflow.core.RuleFlowProcess;
/*     */ import org.jbpm.workflow.core.NodeContainer;
/*     */ import org.jbpm.workflow.core.node.BoundaryEventNode;
/*     */ import org.jbpm.workflow.core.node.EventNode;
/*     */ import org.jbpm.workflow.core.node.Transformation;
/*     */ import org.kie.api.runtime.process.DataTransformer;
/*     */ import org.slf4j.Logger;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class BoundaryEventHandler extends AbstractNodeHandler
/*     */ {
/*  51 */   private DataTransformerRegistry transformerRegistry = DataTransformerRegistry.get();
/*     */ 
/*     */   protected org.jbpm.workflow.core.Node createNode(Attributes attrs) {
/*  54 */     return new BoundaryEventNode();
/*     */   }
/*     */ 
/*     */   public Class generateNodeFor()
/*     */   {
/*  59 */     return BoundaryEventNode.class;
/*     */   }
/*     */ 
/*     */   public Object end(String uri, String localName, ExtensibleXmlParser parser) throws SAXException
/*     */   {
/*  64 */     Element element = parser.endElementBuilder();
/*  65 */     org.jbpm.workflow.core.Node node = (org.jbpm.workflow.core.Node)parser.getCurrent();
/*  66 */     String attachedTo = element.getAttribute("attachedToRef");
/*  67 */     Attr cancelActivityAttr = element.getAttributeNode("cancelActivity");
/*  68 */     boolean cancelActivity = Boolean.parseBoolean(cancelActivityAttr.getValue());
/*     */ 
/*  71 */     org.w3c.dom.Node xmlNode = element.getFirstChild();
/*  72 */     while (xmlNode != null) {
/*  73 */       String nodeName = xmlNode.getNodeName();
/*  74 */       if ("escalationEventDefinition".equals(nodeName))
/*     */       {
/*  76 */         handleEscalationNode(node, element, uri, localName, parser, attachedTo, cancelActivity);
/*  77 */         break;
/*  78 */       }if ("errorEventDefinition".equals(nodeName))
/*     */       {
/*  80 */         handleErrorNode(node, element, uri, localName, parser, attachedTo, cancelActivity);
/*  81 */         break;
/*  82 */       }if ("timerEventDefinition".equals(nodeName))
/*     */       {
/*  84 */         handleTimerNode(node, element, uri, localName, parser, attachedTo, cancelActivity);
/*  85 */         break;
/*  86 */       }if ("compensateEventDefinition".equals(nodeName))
/*     */       {
/*  88 */         handleCompensationNode(node, element, uri, localName, parser, attachedTo, cancelActivity);
/*  89 */         break;
/*  90 */       }if ("signalEventDefinition".equals(nodeName))
/*     */       {
/*  92 */         handleSignalNode(node, element, uri, localName, parser, attachedTo, cancelActivity);
/*  93 */         break;
/*  94 */       }if ("conditionalEventDefinition".equals(nodeName)) {
/*  95 */         handleConditionNode(node, element, uri, localName, parser, attachedTo, cancelActivity);
/*  96 */         break;
/*  97 */       }if ("messageEventDefinition".equals(nodeName)) {
/*  98 */         handleMessageNode(node, element, uri, localName, parser, attachedTo, cancelActivity);
/*  99 */         break;
/*     */       }
/* 101 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/* 103 */     NodeContainer nodeContainer = (NodeContainer)parser.getParent();
/* 104 */     nodeContainer.addNode(node);
/* 105 */     ((ProcessBuildData)parser.getData()).addNode(node);
/* 106 */     return node;
/*     */   }
/*     */ 
/*     */   protected void handleEscalationNode(org.jbpm.workflow.core.Node node, Element element, String uri, String localName, ExtensibleXmlParser parser, String attachedTo, boolean cancelActivity)
/*     */     throws SAXException
/*     */   {
/* 113 */     super.handleNode(node, element, uri, localName, parser);
/* 114 */     BoundaryEventNode eventNode = (BoundaryEventNode)node;
/* 115 */     eventNode.setMetaData("AttachedTo", attachedTo);
/*     */ 
/* 124 */     eventNode.setMetaData("CancelActivity", Boolean.valueOf(cancelActivity));
/* 125 */     eventNode.setAttachedToNodeId(attachedTo);
/* 126 */     org.w3c.dom.Node xmlNode = element.getFirstChild();
/* 127 */     while (xmlNode != null) {
/* 128 */       String nodeName = xmlNode.getNodeName();
/* 129 */       if ("dataOutput".equals(nodeName)) {
/* 130 */         String id = ((Element)xmlNode).getAttribute("id");
/* 131 */         String outputName = ((Element)xmlNode).getAttribute("name");
/* 132 */         this.dataOutputs.put(id, outputName);
/* 133 */       } else if ("dataOutputAssociation".equals(nodeName)) {
/* 134 */         readDataOutputAssociation(xmlNode, eventNode);
/* 135 */       } else if ("escalationEventDefinition".equals(nodeName)) {
/* 136 */         String escalationRef = ((Element)xmlNode).getAttribute("escalationRef");
/* 137 */         if ((escalationRef != null) && (escalationRef.trim().length() > 0))
/*     */         {
/* 139 */           Map escalations = (Map)((ProcessBuildData)parser
/* 139 */             .getData()).getMetaData("BPMN.Escalations");
/* 140 */           if (escalations == null) {
/* 141 */             throw new IllegalArgumentException("No escalations found");
/*     */           }
/* 143 */           Escalation escalation = (Escalation)escalations.get(escalationRef);
/* 144 */           if (escalation == null) {
/* 145 */             throw new IllegalArgumentException(new StringBuilder().append("Could not find escalation ").append(escalationRef).toString());
/*     */           }
/* 147 */           List eventFilters = new ArrayList();
/* 148 */           EventTypeFilter eventFilter = new EventTypeFilter();
/* 149 */           String type = escalation.getEscalationCode();
/* 150 */           eventFilter.setType(new StringBuilder().append("Escalation-").append(attachedTo).append("-").append(type).toString());
/* 151 */           eventFilters.add(eventFilter);
/* 152 */           eventNode.setEventFilters(eventFilters);
/* 153 */           eventNode.setMetaData("EscalationEvent", type);
/*     */         } else {
/* 155 */           throw new UnsupportedOperationException("General escalation is not yet supported.");
/*     */         }
/*     */       }
/* 158 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void handleErrorNode(org.jbpm.workflow.core.Node node, Element element, String uri, String localName, ExtensibleXmlParser parser, String attachedTo, boolean cancelActivity)
/*     */     throws SAXException
/*     */   {
/* 166 */     super.handleNode(node, element, uri, localName, parser);
/* 167 */     BoundaryEventNode eventNode = (BoundaryEventNode)node;
/* 168 */     eventNode.setMetaData("AttachedTo", attachedTo);
/* 169 */     eventNode.setAttachedToNodeId(attachedTo);
/* 170 */     org.w3c.dom.Node xmlNode = element.getFirstChild();
/* 171 */     while (xmlNode != null) {
/* 172 */       String nodeName = xmlNode.getNodeName();
/* 173 */       if ("dataOutput".equals(nodeName)) {
/* 174 */         String id = ((Element)xmlNode).getAttribute("id");
/* 175 */         String outputName = ((Element)xmlNode).getAttribute("name");
/* 176 */         this.dataOutputs.put(id, outputName);
/* 177 */       } else if ("dataOutputAssociation".equals(nodeName)) {
/* 178 */         readDataOutputAssociation(xmlNode, eventNode);
/* 179 */       } else if ("errorEventDefinition".equals(nodeName)) {
/* 180 */         String errorRef = ((Element)xmlNode).getAttribute("errorRef");
/* 181 */         if ((errorRef != null) && (errorRef.trim().length() > 0)) {
/* 182 */           List<Error> errors = (List)((ProcessBuildData)parser.getData()).getMetaData("Errors");
/* 183 */           if (errors == null) {
/* 184 */             throw new IllegalArgumentException("No errors found");
/*     */           }
/* 186 */           Error error = null;
/* 187 */           for (Error listError : errors) {
/* 188 */             if (errorRef.equals(listError.getId())) {
/* 189 */               error = listError;
/*     */             }
/*     */           }
/* 192 */           if (error == null) {
/* 193 */             throw new IllegalArgumentException(new StringBuilder().append("Could not find error ").append(errorRef).toString());
/*     */           }
/* 195 */           String type = error.getErrorCode();
/* 196 */           boolean hasErrorCode = true;
/* 197 */           if (type == null) {
/* 198 */             type = error.getId();
/* 199 */             hasErrorCode = false;
/*     */           }
/* 201 */           String structureRef = error.getStructureRef();
/* 202 */           if (structureRef != null)
/*     */           {
/* 204 */             Map itemDefs = (Map)((ProcessBuildData)parser
/* 204 */               .getData()).getMetaData("ItemDefinitions");
/*     */ 
/* 206 */             if (itemDefs.containsKey(structureRef)) {
/* 207 */               structureRef = ((ItemDefinition)itemDefs.get(structureRef)).getStructureRef();
/*     */             }
/*     */           }
/*     */ 
/* 211 */           List eventFilters = new ArrayList();
/* 212 */           EventTypeFilter eventFilter = new EventTypeFilter();
/* 213 */           eventFilter.setType(new StringBuilder().append("Error-").append(attachedTo).append("-").append(type).toString());
/* 214 */           eventFilters.add(eventFilter);
/* 215 */           eventNode.setEventFilters(eventFilters);
/* 216 */           eventNode.setMetaData("ErrorEvent", type);
/* 217 */           eventNode.setMetaData("HasErrorEvent", Boolean.valueOf(hasErrorCode));
/* 218 */           eventNode.setMetaData("ErrorStructureRef", structureRef);
/*     */         }
/*     */       }
/* 221 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void handleTimerNode(org.jbpm.workflow.core.Node node, Element element, String uri, String localName, ExtensibleXmlParser parser, String attachedTo, boolean cancelActivity)
/*     */     throws SAXException
/*     */   {
/* 228 */     super.handleNode(node, element, uri, localName, parser);
/* 229 */     BoundaryEventNode eventNode = (BoundaryEventNode)node;
/* 230 */     eventNode.setMetaData("AttachedTo", attachedTo);
/* 231 */     eventNode.setMetaData("CancelActivity", Boolean.valueOf(cancelActivity));
/* 232 */     eventNode.setAttachedToNodeId(attachedTo);
/* 233 */     org.w3c.dom.Node xmlNode = element.getFirstChild();
/* 234 */     while (xmlNode != null) {
/* 235 */       String nodeName = xmlNode.getNodeName();
/* 236 */       if ("timerEventDefinition".equals(nodeName)) {
/* 237 */         String timeDuration = null;
/* 238 */         String timeCycle = null;
/* 239 */         String timeDate = null;
/* 240 */         String language = "";
/* 241 */         org.w3c.dom.Node subNode = xmlNode.getFirstChild();
/* 242 */         while ((subNode instanceof Element)) {
/* 243 */           String subNodeName = subNode.getNodeName();
/* 244 */           if ("timeDuration".equals(subNodeName)) {
/* 245 */             timeDuration = subNode.getTextContent();
/* 246 */             break;
/* 247 */           }if ("timeCycle".equals(subNodeName)) {
/* 248 */             timeCycle = subNode.getTextContent();
/* 249 */             language = ((Element)subNode).getAttribute("language");
/* 250 */             break;
/* 251 */           }if ("timeDate".equals(subNodeName)) {
/* 252 */             timeDate = subNode.getTextContent();
/* 253 */             break;
/*     */           }
/* 255 */           subNode = subNode.getNextSibling();
/*     */         }
/* 257 */         if ((timeDuration != null) && (timeDuration.trim().length() > 0)) {
/* 258 */           List eventFilters = new ArrayList();
/* 259 */           EventTypeFilter eventFilter = new EventTypeFilter();
/* 260 */           eventFilter.setType(new StringBuilder().append("Timer-").append(attachedTo).append("-").append(timeDuration).append("-").append(eventNode.getId()).toString());
/* 261 */           eventFilters.add(eventFilter);
/* 262 */           eventNode.setEventFilters(eventFilters);
/* 263 */           eventNode.setMetaData("TimeDuration", timeDuration);
/* 264 */         } else if ((timeCycle != null) && (timeCycle.trim().length() > 0)) {
/* 265 */           List eventFilters = new ArrayList();
/* 266 */           EventTypeFilter eventFilter = new EventTypeFilter();
/* 267 */           eventFilter.setType(new StringBuilder().append("Timer-").append(attachedTo).append("-").append(timeCycle).append("-").append(eventNode.getId()).toString());
/* 268 */           eventFilters.add(eventFilter);
/* 269 */           eventNode.setEventFilters(eventFilters);
/* 270 */           eventNode.setMetaData("TimeCycle", timeCycle);
/* 271 */           eventNode.setMetaData("Language", language);
/* 272 */         } else if ((timeDate != null) && (timeDate.trim().length() > 0)) {
/* 273 */           List eventFilters = new ArrayList();
/* 274 */           EventTypeFilter eventFilter = new EventTypeFilter();
/* 275 */           eventFilter.setType(new StringBuilder().append("Timer-").append(attachedTo).append("-").append(timeDate).append("-").append(eventNode.getId()).toString());
/* 276 */           eventFilters.add(eventFilter);
/* 277 */           eventNode.setEventFilters(eventFilters);
/* 278 */           eventNode.setMetaData("TimeDate", timeDate);
/*     */         }
/*     */       }
/*     */ 
/* 282 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void handleCompensationNode(org.jbpm.workflow.core.Node node, Element element, String uri, String localName, ExtensibleXmlParser parser, String attachedTo, boolean cancelActivity)
/*     */     throws SAXException
/*     */   {
/* 289 */     BoundaryEventNode eventNode = (BoundaryEventNode)parser.getCurrent();
/*     */ 
/* 291 */     super.handleNode(node, element, uri, localName, parser);
/* 292 */     NodeList childs = element.getChildNodes();
/* 293 */     for (int i = 0; i < childs.getLength(); i++) {
/* 294 */       if ((childs.item(i) instanceof Element)) {
/* 295 */         Element el = (Element)childs.item(i);
/* 296 */         if ("compensateEventDefinition".equalsIgnoreCase(el.getNodeName())) {
/* 297 */           String activityRef = el.getAttribute("activityRef");
/* 298 */           if ((activityRef != null) && (activityRef.length() > 0)) {
/* 299 */             logger.warn(new StringBuilder().append("activityRef value [").append(activityRef).append("] on Boundary Event '").append(eventNode.getMetaData("UniqueId")).append("' ignored per the BPMN2 specification.").toString());
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 305 */     eventNode.setMetaData("AttachedTo", attachedTo);
/* 306 */     eventNode.setAttachedToNodeId(attachedTo);
/*     */ 
/* 309 */     NodeContainer parentContainer = (NodeContainer)parser.getParent();
/*     */ 
/* 312 */     EventTypeFilter eventFilter = new NonAcceptingEventTypeFilter();
/* 313 */     eventFilter.setType("Compensation");
/* 314 */     List eventFilters = new ArrayList();
/* 315 */     eventNode.setEventFilters(eventFilters);
/* 316 */     eventFilters.add(eventFilter);
/*     */ 
/* 319 */     ProcessHandler.addCompensationScope((RuleFlowProcess)parser.getParent(RuleFlowProcess.class), eventNode, parentContainer, attachedTo);
/*     */   }
/*     */ 
/*     */   protected void handleSignalNode(org.jbpm.workflow.core.Node node, Element element, String uri, String localName, ExtensibleXmlParser parser, String attachedTo, boolean cancelActivity)
/*     */     throws SAXException
/*     */   {
/* 326 */     super.handleNode(node, element, uri, localName, parser);
/* 327 */     BoundaryEventNode eventNode = (BoundaryEventNode)node;
/* 328 */     eventNode.setMetaData("AttachedTo", attachedTo);
/* 329 */     eventNode.setMetaData("CancelActivity", Boolean.valueOf(cancelActivity));
/* 330 */     eventNode.setAttachedToNodeId(attachedTo);
/* 331 */     org.w3c.dom.Node xmlNode = element.getFirstChild();
/* 332 */     while (xmlNode != null) {
/* 333 */       String nodeName = xmlNode.getNodeName();
/* 334 */       if ("dataOutput".equals(nodeName)) {
/* 335 */         String id = ((Element)xmlNode).getAttribute("id");
/* 336 */         String outputName = ((Element)xmlNode).getAttribute("name");
/* 337 */         this.dataOutputs.put(id, outputName);
/* 338 */       }if ("dataOutputAssociation".equals(nodeName)) {
/* 339 */         readDataOutputAssociation(xmlNode, eventNode);
/* 340 */       } else if ("signalEventDefinition".equals(nodeName)) {
/* 341 */         String type = ((Element)xmlNode).getAttribute("signalRef");
/* 342 */         if ((type != null) && (type.trim().length() > 0))
/*     */         {
/* 344 */           type = checkSignalAndConvertToRealSignalNam(parser, type);
/*     */ 
/* 346 */           List eventFilters = new ArrayList();
/* 347 */           EventTypeFilter eventFilter = new EventTypeFilter();
/* 348 */           eventFilter.setType(type);
/* 349 */           eventFilters.add(eventFilter);
/* 350 */           eventNode.setEventFilters(eventFilters);
/* 351 */           eventNode.setScope("external");
/* 352 */           eventNode.setMetaData("SignalName", type);
/*     */         }
/*     */       }
/* 355 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void handleConditionNode(org.jbpm.workflow.core.Node node, Element element, String uri, String localName, ExtensibleXmlParser parser, String attachedTo, boolean cancelActivity)
/*     */     throws SAXException
/*     */   {
/* 363 */     super.handleNode(node, element, uri, localName, parser);
/* 364 */     BoundaryEventNode eventNode = (BoundaryEventNode)node;
/* 365 */     eventNode.setMetaData("AttachedTo", attachedTo);
/* 366 */     eventNode.setMetaData("CancelActivity", Boolean.valueOf(cancelActivity));
/* 367 */     eventNode.setAttachedToNodeId(attachedTo);
/* 368 */     org.w3c.dom.Node xmlNode = element.getFirstChild();
/* 369 */     while (xmlNode != null) {
/* 370 */       String nodeName = xmlNode.getNodeName();
/* 371 */       if ("dataOutput".equals(nodeName)) {
/* 372 */         String id = ((Element)xmlNode).getAttribute("id");
/* 373 */         String outputName = ((Element)xmlNode).getAttribute("name");
/* 374 */         this.dataOutputs.put(id, outputName);
/* 375 */       } else if ("dataOutputAssociation".equals(nodeName)) {
/* 376 */         readDataOutputAssociation(xmlNode, eventNode);
/* 377 */       } else if ("conditionalEventDefinition".equals(nodeName)) {
/* 378 */         org.w3c.dom.Node subNode = xmlNode.getFirstChild();
/* 379 */         while (subNode != null) {
/* 380 */           String subnodeName = subNode.getNodeName();
/* 381 */           if ("condition".equals(subnodeName)) {
/* 382 */             eventNode.setMetaData("Condition", xmlNode.getTextContent());
/* 383 */             List eventFilters = new ArrayList();
/* 384 */             EventTypeFilter eventFilter = new EventTypeFilter();
/* 385 */             eventFilter.setType(new StringBuilder().append("Condition-").append(attachedTo).toString());
/* 386 */             eventFilters.add(eventFilter);
/* 387 */             eventNode.setScope("external");
/* 388 */             eventNode.setEventFilters(eventFilters);
/* 389 */             break;
/*     */           }
/* 391 */           subNode = subNode.getNextSibling();
/*     */         }
/*     */       }
/* 394 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void handleMessageNode(org.jbpm.workflow.core.Node node, Element element, String uri, String localName, ExtensibleXmlParser parser, String attachedTo, boolean cancelActivity)
/*     */     throws SAXException
/*     */   {
/* 402 */     super.handleNode(node, element, uri, localName, parser);
/* 403 */     BoundaryEventNode eventNode = (BoundaryEventNode)node;
/* 404 */     eventNode.setMetaData("AttachedTo", attachedTo);
/* 405 */     eventNode.setMetaData("CancelActivity", Boolean.valueOf(cancelActivity));
/* 406 */     eventNode.setAttachedToNodeId(attachedTo);
/* 407 */     org.w3c.dom.Node xmlNode = element.getFirstChild();
/* 408 */     while (xmlNode != null) {
/* 409 */       String nodeName = xmlNode.getNodeName();
/* 410 */       if ("dataOutput".equals(nodeName)) {
/* 411 */         String id = ((Element)xmlNode).getAttribute("id");
/* 412 */         String outputName = ((Element)xmlNode).getAttribute("name");
/* 413 */         this.dataOutputs.put(id, outputName);
/* 414 */       } else if ("dataOutputAssociation".equals(nodeName)) {
/* 415 */         readDataOutputAssociation(xmlNode, eventNode);
/* 416 */       } else if ("messageEventDefinition".equals(nodeName)) {
/* 417 */         String messageRef = ((Element)xmlNode).getAttribute("messageRef");
/*     */ 
/* 419 */         Map messages = (Map)((ProcessBuildData)parser
/* 419 */           .getData()).getMetaData("Messages");
/* 420 */         if (messages == null) {
/* 421 */           throw new IllegalArgumentException("No messages found");
/*     */         }
/* 423 */         Message message = (Message)messages.get(messageRef);
/* 424 */         if (message == null) {
/* 425 */           throw new IllegalArgumentException(new StringBuilder().append("Could not find message ").append(messageRef).toString());
/*     */         }
/* 427 */         eventNode.setMetaData("MessageType", message.getType());
/* 428 */         List eventFilters = new ArrayList();
/* 429 */         EventTypeFilter eventFilter = new EventTypeFilter();
/* 430 */         eventFilter.setType(new StringBuilder().append("Message-").append(messageRef).toString());
/* 431 */         eventFilters.add(eventFilter);
/* 432 */         eventNode.setScope("external");
/* 433 */         eventNode.setEventFilters(eventFilters);
/*     */       }
/* 435 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void readDataOutputAssociation(org.w3c.dom.Node xmlNode, EventNode eventNode)
/*     */   {
/* 441 */     org.w3c.dom.Node subNode = xmlNode.getFirstChild();
/* 442 */     String from = subNode.getTextContent();
/*     */ 
/* 444 */     subNode = subNode.getNextSibling();
/* 445 */     String to = subNode.getTextContent();
/*     */ 
/* 447 */     Transformation transformation = null;
/* 448 */     subNode = subNode.getNextSibling();
/* 449 */     if ((subNode != null) && ("transformation".equals(subNode.getNodeName()))) {
/* 450 */       String lang = subNode.getAttributes().getNamedItem("language").getNodeValue();
/* 451 */       String expression = subNode.getTextContent();
/* 452 */       DataTransformer transformer = this.transformerRegistry.find(lang);
/* 453 */       if (transformer == null) {
/* 454 */         throw new IllegalArgumentException(new StringBuilder().append("No transformer registered for language ").append(lang).toString());
/*     */       }
/* 456 */       transformation = new Transformation(lang, expression, (String)this.dataOutputs.get(from));
/* 457 */       eventNode.setMetaData("Transformation", transformation);
/*     */ 
/* 459 */       eventNode.setEventTransformer(new EventTransformerImpl(transformation));
/*     */     }
/*     */ 
/* 462 */     eventNode.setVariableName(to);
/*     */   }
/*     */ 
/*     */   public void writeNode(org.jbpm.workflow.core.Node node, StringBuilder xmlDump, int metaDataType)
/*     */   {
/* 467 */     EventNode eventNode = (EventNode)node;
/* 468 */     String attachedTo = (String)eventNode.getMetaData("AttachedTo");
/* 469 */     if (attachedTo != null) {
/* 470 */       String type = ((EventTypeFilter)eventNode.getEventFilters().get(0)).getType();
/* 471 */       if (type.startsWith("Escalation-")) {
/* 472 */         type = type.substring(attachedTo.length() + 12);
/* 473 */         boolean cancelActivity = ((Boolean)eventNode.getMetaData("CancelActivity")).booleanValue();
/* 474 */         writeNode("boundaryEvent", eventNode, xmlDump, metaDataType);
/* 475 */         xmlDump.append(new StringBuilder().append("attachedToRef=\"").append(attachedTo).append("\" ").toString());
/* 476 */         if (!cancelActivity) {
/* 477 */           xmlDump.append("cancelActivity=\"false\" ");
/*     */         }
/* 479 */         xmlDump.append(new StringBuilder().append(">").append(EOL).toString());
/* 480 */         writeExtensionElements(node, xmlDump);
/* 481 */         xmlDump.append(new StringBuilder().append("      <escalationEventDefinition escalationRef=\"").append(XmlBPMNProcessDumper.replaceIllegalCharsAttribute(type)).append("\" />").append(EOL).toString());
/* 482 */         endNode("boundaryEvent", xmlDump);
/* 483 */       } else if (type.startsWith("Error-")) {
/* 484 */         type = type.substring(attachedTo.length() + 7);
/* 485 */         writeNode("boundaryEvent", eventNode, xmlDump, metaDataType);
/* 486 */         xmlDump.append(new StringBuilder().append("attachedToRef=\"").append(attachedTo).append("\" ").toString());
/* 487 */         xmlDump.append(new StringBuilder().append(">").append(EOL).toString());
/* 488 */         writeExtensionElements(node, xmlDump);
/* 489 */         writeVariableName(eventNode, xmlDump);
/* 490 */         String errorId = getErrorIdForErrorCode(type, eventNode);
/* 491 */         xmlDump.append(new StringBuilder().append("      <errorEventDefinition errorRef=\"").append(XmlBPMNProcessDumper.replaceIllegalCharsAttribute(errorId)).append("\" ").toString());
/* 492 */         xmlDump.append(new StringBuilder().append("/>").append(EOL).toString());
/* 493 */         endNode("boundaryEvent", xmlDump);
/* 494 */       } else if (type.startsWith("Timer-")) {
/* 495 */         type = type.substring(attachedTo.length() + 7);
/* 496 */         boolean cancelActivity = ((Boolean)eventNode.getMetaData("CancelActivity")).booleanValue();
/* 497 */         writeNode("boundaryEvent", eventNode, xmlDump, metaDataType);
/* 498 */         xmlDump.append(new StringBuilder().append("attachedToRef=\"").append(attachedTo).append("\" ").toString());
/* 499 */         if (!cancelActivity) {
/* 500 */           xmlDump.append("cancelActivity=\"false\" ");
/*     */         }
/* 502 */         xmlDump.append(new StringBuilder().append(">").append(EOL).toString());
/* 503 */         writeExtensionElements(node, xmlDump);
/* 504 */         String duration = (String)eventNode.getMetaData("TimeDuration");
/* 505 */         String cycle = (String)eventNode.getMetaData("TimeCycle");
/* 506 */         String date = (String)eventNode.getMetaData("TimeDate");
/*     */ 
/* 509 */         if ((duration != null) && (cycle != null)) {
/* 510 */           String lang = (String)eventNode.getMetaData("Language");
/* 511 */           String language = "";
/* 512 */           if ((lang != null) && (!lang.isEmpty())) {
/* 513 */             language = new StringBuilder().append("language=\"").append(lang).append("\" ").toString();
/*     */           }
/* 515 */           xmlDump.append(new StringBuilder().append("      <timerEventDefinition>").append(EOL).append("        <timeDuration xsi:type=\"tFormalExpression\">")
/* 517 */             .append(XmlDumper.replaceIllegalChars(duration))
/* 517 */             .append("</timeDuration>").append(EOL).append("        <timeCycle xsi:type=\"tFormalExpression\" ").append(language).append(">")
/* 518 */             .append(XmlDumper.replaceIllegalChars(cycle))
/* 518 */             .append("</timeCycle>").append(EOL).append("      </timerEventDefinition>").append(EOL).toString());
/*     */         }
/* 520 */         else if (duration != null) {
/* 521 */           xmlDump.append(new StringBuilder().append("      <timerEventDefinition>").append(EOL).append("        <timeDuration xsi:type=\"tFormalExpression\">")
/* 523 */             .append(XmlDumper.replaceIllegalChars(duration))
/* 523 */             .append("</timeDuration>").append(EOL).append("      </timerEventDefinition>").append(EOL).toString());
/*     */         }
/* 525 */         else if (date != null) {
/* 526 */           xmlDump.append(new StringBuilder().append("      <timerEventDefinition>").append(EOL).append("        <timeDate xsi:type=\"tFormalExpression\">")
/* 528 */             .append(XmlDumper.replaceIllegalChars(date))
/* 528 */             .append("</timeDate>").append(EOL).append("      </timerEventDefinition>").append(EOL).toString());
/*     */         }
/*     */         else {
/* 531 */           String lang = (String)eventNode.getMetaData("Language");
/* 532 */           String language = "";
/* 533 */           if ((lang != null) && (!lang.isEmpty())) {
/* 534 */             language = new StringBuilder().append("language=\"").append(lang).append("\" ").toString();
/*     */           }
/* 536 */           xmlDump.append(new StringBuilder().append("      <timerEventDefinition>").append(EOL).append("        <timeCycle xsi:type=\"tFormalExpression\" ").append(language).append(">")
/* 538 */             .append(XmlDumper.replaceIllegalChars(cycle))
/* 538 */             .append("</timeCycle>").append(EOL).append("      </timerEventDefinition>").append(EOL).toString());
/*     */         }
/*     */ 
/* 541 */         endNode("boundaryEvent", xmlDump);
/* 542 */       } else if (type.equals("Compensation")) {
/* 543 */         writeNode("boundaryEvent", eventNode, xmlDump, metaDataType);
/* 544 */         xmlDump.append(new StringBuilder().append("attachedToRef=\"").append(attachedTo).append("\" ").toString());
/* 545 */         xmlDump.append(new StringBuilder().append(">").append(EOL).toString());
/* 546 */         writeExtensionElements(node, xmlDump);
/* 547 */         xmlDump.append(new StringBuilder().append("      <compensateEventDefinition/>").append(EOL).toString());
/* 548 */         endNode("boundaryEvent", xmlDump);
/* 549 */       } else if (node.getMetaData().get("SignalName") != null) {
/* 550 */         boolean cancelActivity = ((Boolean)eventNode.getMetaData("CancelActivity")).booleanValue();
/* 551 */         writeNode("boundaryEvent", eventNode, xmlDump, metaDataType);
/* 552 */         xmlDump.append(new StringBuilder().append("attachedToRef=\"").append(attachedTo).append("\" ").toString());
/* 553 */         if (!cancelActivity) {
/* 554 */           xmlDump.append("cancelActivity=\"false\" ");
/*     */         }
/* 556 */         xmlDump.append(new StringBuilder().append(">").append(EOL).toString());
/* 557 */         writeExtensionElements(node, xmlDump);
/* 558 */         xmlDump.append(new StringBuilder().append("      <signalEventDefinition signalRef=\"").append(type).append("\"/>").append(EOL).toString());
/* 559 */         endNode("boundaryEvent", xmlDump);
/* 560 */       } else if (node.getMetaData().get("Condition") != null)
/*     */       {
/* 562 */         boolean cancelActivity = ((Boolean)eventNode.getMetaData("CancelActivity")).booleanValue();
/* 563 */         writeNode("boundaryEvent", eventNode, xmlDump, metaDataType);
/* 564 */         xmlDump.append(new StringBuilder().append("attachedToRef=\"").append(attachedTo).append("\" ").toString());
/* 565 */         if (!cancelActivity) {
/* 566 */           xmlDump.append("cancelActivity=\"false\" ");
/*     */         }
/* 568 */         xmlDump.append(new StringBuilder().append(">").append(EOL).toString());
/* 569 */         writeExtensionElements(node, xmlDump);
/* 570 */         xmlDump.append(new StringBuilder().append("      <conditionalEventDefinition>").append(EOL).toString());
/* 571 */         xmlDump.append(new StringBuilder().append("        <condition xsi:type=\"tFormalExpression\" language=\"http://www.jboss.org/drools/rule\">").append(eventNode.getMetaData("Condition")).append("</condition>").append(EOL).toString());
/* 572 */         xmlDump.append(new StringBuilder().append("      </conditionalEventDefinition>").append(EOL).toString());
/* 573 */         endNode("boundaryEvent", xmlDump);
/* 574 */       } else if (type.startsWith("Message-")) {
/* 575 */         type = type.substring(8);
/* 576 */         writeNode("boundaryEvent", eventNode, xmlDump, metaDataType);
/* 577 */         xmlDump.append(new StringBuilder().append("attachedToRef=\"").append(attachedTo).append("\" ").toString());
/* 578 */         xmlDump.append(new StringBuilder().append(">").append(EOL).toString());
/* 579 */         writeExtensionElements(node, xmlDump);
/* 580 */         xmlDump.append(new StringBuilder().append("      <messageEventDefinition messageRef=\"").append(type).append("\"/>").append(EOL).toString());
/* 581 */         endNode("boundaryEvent", xmlDump);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.BoundaryEventHandler
 * JD-Core Version:    0.6.0
 */