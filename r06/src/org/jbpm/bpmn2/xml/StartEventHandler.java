/*     */ package org.jbpm.bpmn2.xml;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.drools.compiler.compiler.xml.XmlDumper;
/*     */ import org.drools.core.xml.ExtensibleXmlParser;
/*     */ import org.jbpm.bpmn2.core.Error;
/*     */ import org.jbpm.bpmn2.core.Escalation;
/*     */ import org.jbpm.bpmn2.core.Message;
/*     */ import org.jbpm.compiler.xml.ProcessBuildData;
/*     */ import org.jbpm.process.core.event.EventFilter;
/*     */ import org.jbpm.process.core.event.EventTransformerImpl;
/*     */ import org.jbpm.process.core.event.EventTypeFilter;
/*     */ import org.jbpm.process.core.event.NonAcceptingEventTypeFilter;
/*     */ import org.jbpm.process.core.impl.DataTransformerRegistry;
/*     */ import org.jbpm.process.core.timer.Timer;
/*     */ import org.jbpm.workflow.core.impl.DroolsConsequenceAction;
/*     */ import org.jbpm.workflow.core.node.ConstraintTrigger;
/*     */ import org.jbpm.workflow.core.node.EventSubProcessNode;
/*     */ import org.jbpm.workflow.core.node.EventTrigger;
/*     */ import org.jbpm.workflow.core.node.StartNode;
/*     */ import org.jbpm.workflow.core.node.Transformation;
/*     */ import org.jbpm.workflow.core.node.Trigger;
/*     */ import org.kie.api.runtime.process.DataTransformer;
/*     */ import org.slf4j.Logger;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ public class StartEventHandler extends AbstractNodeHandler
/*     */ {
/*  52 */   private DataTransformerRegistry transformerRegistry = DataTransformerRegistry.get();
/*     */ 
/*     */   protected org.jbpm.workflow.core.Node createNode(Attributes attrs) {
/*  55 */     return new StartNode();
/*     */   }
/*     */ 
/*     */   public Class generateNodeFor()
/*     */   {
/*  60 */     return StartNode.class;
/*     */   }
/*     */ 
/*     */   protected void handleNode(org.jbpm.workflow.core.Node node, Element element, String uri, String localName, ExtensibleXmlParser parser)
/*     */     throws SAXException
/*     */   {
/*  66 */     super.handleNode(node, element, uri, localName, parser);
/*  67 */     StartNode startNode = (StartNode)node;
/*     */ 
/*  71 */     startNode.setInterrupting(Boolean.parseBoolean(element.getAttribute("isInterrupting")));
/*     */ 
/*  73 */     org.w3c.dom.Node xmlNode = element.getFirstChild();
/*  74 */     while (xmlNode != null) {
/*  75 */       String nodeName = xmlNode.getNodeName();
/*  76 */       if ("dataOutput".equals(nodeName)) {
/*  77 */         readDataOutput(xmlNode, startNode);
/*  78 */       } else if ("dataOutputAssociation".equals(nodeName)) {
/*  79 */         readDataOutputAssociation(xmlNode, startNode);
/*  80 */       } else if ("outputSet".equals(nodeName))
/*     */       {
/*  84 */         String message = new StringBuilder().append("Ignoring <").append(nodeName).append("> element: <").append(nodeName).append("> elements should not be used on start or other catch events.").toString();
/*     */ 
/*  86 */         SAXParseException saxpe = new SAXParseException(message, parser.getLocator());
/*  87 */         parser.warning(saxpe);
/*     */       } else {
/*  89 */         if ("conditionalEventDefinition".equals(nodeName)) {
/*  90 */           String constraint = null;
/*  91 */           org.w3c.dom.Node subNode = xmlNode.getFirstChild();
/*  92 */           while (subNode != null) {
/*  93 */             String subnodeName = subNode.getNodeName();
/*  94 */             if ("condition".equals(subnodeName)) {
/*  95 */               constraint = xmlNode.getTextContent();
/*  96 */               break;
/*     */             }
/*  98 */             subNode = subNode.getNextSibling();
/*     */           }
/* 100 */           ConstraintTrigger trigger = new ConstraintTrigger();
/* 101 */           trigger.setConstraint(constraint);
/* 102 */           startNode.addTrigger(trigger);
/* 103 */           break;
/* 104 */         }if ("signalEventDefinition".equals(nodeName)) {
/* 105 */           String type = ((Element)xmlNode).getAttribute("signalRef");
/*     */ 
/* 107 */           type = checkSignalAndConvertToRealSignalNam(parser, type);
/*     */ 
/* 109 */           if ((type != null) && (type.trim().length() > 0))
/* 110 */             addTriggerWithInMappings(startNode, type);
/*     */         }
/* 112 */         else if ("messageEventDefinition".equals(nodeName)) {
/* 113 */           String messageRef = ((Element)xmlNode).getAttribute("messageRef");
/*     */ 
/* 115 */           Map messages = (Map)((ProcessBuildData)parser
/* 115 */             .getData()).getMetaData("Messages");
/* 116 */           if (messages == null) {
/* 117 */             throw new IllegalArgumentException("No messages found");
/*     */           }
/* 119 */           Message message = (Message)messages.get(messageRef);
/* 120 */           if (message == null) {
/* 121 */             throw new IllegalArgumentException(new StringBuilder().append("Could not find message ").append(messageRef).toString());
/*     */           }
/* 123 */           startNode.setMetaData("MessageType", message.getType());
/*     */ 
/* 126 */           addTriggerWithInMappings(startNode, new StringBuilder().append("Message-").append(messageRef).toString());
/* 127 */         } else if ("timerEventDefinition".equals(nodeName)) {
/* 128 */           handleTimerNode(startNode, element, uri, localName, parser);
/*     */         }
/* 130 */         else if ("errorEventDefinition".equals(nodeName)) {
/* 131 */           if (!startNode.isInterrupting())
/*     */           {
/* 135 */             String errorMsg = "Error Start Events in an Event Sub-Process always interrupt the containing (sub)process(es).";
/* 136 */             throw new IllegalArgumentException(errorMsg);
/*     */           }
/* 138 */           String errorRef = ((Element)xmlNode).getAttribute("errorRef");
/* 139 */           if ((errorRef != null) && (errorRef.trim().length() > 0)) {
/* 140 */             List<Error> errors = (List)((ProcessBuildData)parser.getData()).getMetaData("Errors");
/* 141 */             if (errors == null) {
/* 142 */               throw new IllegalArgumentException("No errors found");
/*     */             }
/* 144 */             Error error = null;
/* 145 */             for (Error listError : errors) {
/* 146 */               if (errorRef.equals(listError.getId())) {
/* 147 */                 error = listError;
/*     */               }
/*     */             }
/* 150 */             if (error == null) {
/* 151 */               throw new IllegalArgumentException(new StringBuilder().append("Could not find error ").append(errorRef).toString());
/*     */             }
/* 153 */             startNode.setMetaData("FaultCode", error.getErrorCode());
/* 154 */             addTriggerWithInMappings(startNode, new StringBuilder().append("Error-").append(error.getErrorCode()).toString());
/*     */           }
/* 156 */         } else if ("escalationEventDefinition".equals(nodeName)) {
/* 157 */           String escalationRef = ((Element)xmlNode).getAttribute("escalationRef");
/* 158 */           if ((escalationRef != null) && (escalationRef.trim().length() > 0))
/*     */           {
/* 160 */             Map escalations = (Map)((ProcessBuildData)parser
/* 160 */               .getData()).getMetaData("BPMN.Escalations");
/* 161 */             if (escalations == null) {
/* 162 */               throw new IllegalArgumentException("No escalations found");
/*     */             }
/* 164 */             Escalation escalation = (Escalation)escalations.get(escalationRef);
/* 165 */             if (escalation == null) {
/* 166 */               throw new IllegalArgumentException(new StringBuilder().append("Could not find escalation ").append(escalationRef).toString());
/*     */             }
/*     */ 
/* 169 */             addTriggerWithInMappings(startNode, new StringBuilder().append("Escalation-").append(escalation.getEscalationCode()).toString());
/*     */           }
/* 171 */         } else if ("compensateEventDefinition".equals(nodeName)) {
/* 172 */           handleCompensationNode(startNode, element, xmlNode, parser);
/*     */         }
/*     */       }
/* 174 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void addTriggerWithInMappings(StartNode startNode, String triggerEventType) {
/* 179 */     EventTrigger trigger = new EventTrigger();
/* 180 */     EventTypeFilter eventFilter = new EventTypeFilter();
/* 181 */     eventFilter.setType(triggerEventType);
/* 182 */     trigger.addEventFilter(eventFilter);
/*     */ 
/* 184 */     String mapping = (String)startNode.getMetaData("TriggerMapping");
/* 185 */     if (mapping != null) {
/* 186 */       trigger.addInMapping(mapping, startNode.getOutMapping(mapping));
/*     */     }
/*     */ 
/* 189 */     startNode.addTrigger(trigger);
/*     */   }
/*     */ 
/*     */   public Object end(String uri, String localName, ExtensibleXmlParser parser) throws SAXException
/*     */   {
/* 194 */     StartNode startNode = (StartNode)super.end(uri, localName, parser);
/*     */ 
/* 196 */     return startNode;
/*     */   }
/*     */ 
/*     */   protected void readDataOutputAssociation(org.w3c.dom.Node xmlNode, StartNode startNode)
/*     */   {
/* 201 */     org.w3c.dom.Node subNode = xmlNode.getFirstChild();
/* 202 */     if (!"sourceRef".equals(subNode.getNodeName())) {
/* 203 */       throw new IllegalArgumentException("No sourceRef found in dataOutputAssociation in startEvent");
/*     */     }
/* 205 */     String source = subNode.getTextContent();
/* 206 */     if (this.dataOutputs.get(source) == null) {
/* 207 */       throw new IllegalArgumentException("No dataOutput could be found for the dataOutputAssociation.");
/*     */     }
/*     */ 
/* 211 */     subNode = subNode.getNextSibling();
/* 212 */     if (!"targetRef".equals(subNode.getNodeName())) {
/* 213 */       throw new IllegalArgumentException("No targetRef found in dataOutputAssociation in startEvent");
/*     */     }
/* 215 */     String target = subNode.getTextContent();
/* 216 */     startNode.setMetaData("TriggerMapping", target);
/*     */ 
/* 218 */     Transformation transformation = null;
/* 219 */     subNode = subNode.getNextSibling();
/* 220 */     if ((subNode != null) && ("transformation".equals(subNode.getNodeName()))) {
/* 221 */       String lang = subNode.getAttributes().getNamedItem("language").getNodeValue();
/* 222 */       String expression = subNode.getTextContent();
/* 223 */       DataTransformer transformer = this.transformerRegistry.find(lang);
/* 224 */       if (transformer == null) {
/* 225 */         throw new IllegalArgumentException(new StringBuilder().append("No transformer registered for language ").append(lang).toString());
/*     */       }
/* 227 */       transformation = new Transformation(lang, expression, (String)this.dataOutputs.get(source));
/* 228 */       startNode.setMetaData("Transformation", transformation);
/*     */ 
/* 230 */       startNode.setEventTransformer(new EventTransformerImpl(transformation));
/* 231 */       subNode = subNode.getNextSibling();
/*     */     }
/*     */ 
/* 234 */     if (subNode != null)
/*     */     {
/* 236 */       throw new UnsupportedOperationException(new StringBuilder().append(subNode.getNodeName()).append(" elements in dataOutputAssociations are not yet supported.").toString());
/*     */     }
/* 238 */     startNode.addOutMapping(target, (String)this.dataOutputs.get(source));
/*     */   }
/*     */ 
/*     */   protected void readDataOutput(org.w3c.dom.Node xmlNode, StartNode startNode)
/*     */   {
/* 243 */     String id = ((Element)xmlNode).getAttribute("id");
/* 244 */     String outputName = ((Element)xmlNode).getAttribute("name");
/* 245 */     this.dataOutputs.put(id, outputName);
/*     */   }
/*     */ 
/*     */   public void writeNode(org.jbpm.workflow.core.Node node, StringBuilder xmlDump, int metaDataType) {
/* 249 */     StartNode startNode = (StartNode)node;
/* 250 */     writeNode("startEvent", startNode, xmlDump, metaDataType);
/* 251 */     xmlDump.append(" isInterrupting=\"");
/* 252 */     if (startNode.isInterrupting())
/* 253 */       xmlDump.append("true");
/*     */     else {
/* 255 */       xmlDump.append("false");
/*     */     }
/* 257 */     xmlDump.append(new StringBuilder().append("\">").append(EOL).toString());
/* 258 */     writeExtensionElements(startNode, xmlDump);
/*     */ 
/* 260 */     List triggers = startNode.getTriggers();
/* 261 */     if (triggers != null) {
/* 262 */       if (triggers.size() > 1) {
/* 263 */         throw new IllegalArgumentException("Multiple start triggers not supported");
/*     */       }
/*     */ 
/* 266 */       Trigger trigger = (Trigger)triggers.get(0);
/* 267 */       if ((trigger instanceof ConstraintTrigger)) {
/* 268 */         ConstraintTrigger constraintTrigger = (ConstraintTrigger)trigger;
/* 269 */         if (constraintTrigger.getHeader() == null) {
/* 270 */           xmlDump.append(new StringBuilder().append("      <conditionalEventDefinition>").append(EOL).toString());
/* 271 */           xmlDump.append(new StringBuilder().append("        <condition xsi:type=\"tFormalExpression\" language=\"http://www.jboss.org/drools/rule\">").append(constraintTrigger.getConstraint()).append("</condition>").append(EOL).toString());
/* 272 */           xmlDump.append(new StringBuilder().append("      </conditionalEventDefinition>").append(EOL).toString());
/*     */         }
/* 274 */       } else if ((trigger instanceof EventTrigger)) {
/* 275 */         EventTrigger eventTrigger = (EventTrigger)trigger;
/* 276 */         String mapping = null;
/* 277 */         String nameMapping = "event";
/* 278 */         if (!trigger.getInMappings().isEmpty()) {
/* 279 */           mapping = (String)eventTrigger.getInMappings().keySet().iterator().next();
/* 280 */           nameMapping = (String)eventTrigger.getInMappings().values().iterator().next();
/*     */         }
/*     */         else {
/* 283 */           mapping = (String)startNode.getMetaData("TriggerMapping");
/*     */         }
/*     */ 
/* 286 */         if (mapping != null) {
/* 287 */           xmlDump.append(new StringBuilder().append("      <dataOutput id=\"_")
/* 288 */             .append(startNode
/* 288 */             .getId()).append("_Output\" name=\"").append(nameMapping).append("\" />").append(EOL).append("      <dataOutputAssociation>").append(EOL).append("        <sourceRef>_")
/* 290 */             .append(startNode
/* 290 */             .getId()).append("_Output</sourceRef>").append(EOL).append("        <targetRef>").append(mapping).append("</targetRef>").append(EOL).append("      </dataOutputAssociation>").append(EOL).toString());
/*     */         }
/*     */ 
/* 295 */         String type = ((EventTypeFilter)eventTrigger.getEventFilters().get(0)).getType();
/* 296 */         if (type.startsWith("Message-")) {
/* 297 */           type = type.substring(8);
/* 298 */           xmlDump.append(new StringBuilder().append("      <messageEventDefinition messageRef=\"").append(type).append("\"/>").append(EOL).toString());
/* 299 */         } else if (type.startsWith("Error-")) {
/* 300 */           type = type.substring(6);
/* 301 */           String errorId = getErrorIdForErrorCode(type, startNode);
/* 302 */           xmlDump.append(new StringBuilder().append("      <errorEventDefinition errorRef=\"").append(XmlBPMNProcessDumper.replaceIllegalCharsAttribute(errorId)).append("\"/>").append(EOL).toString());
/* 303 */         } else if (type.startsWith("Escalation-")) {
/* 304 */           type = type.substring(11);
/* 305 */           xmlDump.append(new StringBuilder().append("      <escalationEventDefinition escalationRef=\"").append(type).append("\"/>").append(EOL).toString());
/* 306 */         } else if (type.equals("Compensation")) {
/* 307 */           xmlDump.append(new StringBuilder().append("      <compensateEventDefinition/>").append(EOL).toString());
/*     */         } else {
/* 309 */           xmlDump.append(new StringBuilder().append("      <signalEventDefinition signalRef=\"").append(type).append("\" />").append(EOL).toString());
/*     */         }
/*     */       } else {
/* 312 */         throw new IllegalArgumentException(new StringBuilder().append("Unsupported trigger type ").append(trigger).toString());
/*     */       }
/*     */ 
/* 315 */       if (startNode.getTimer() != null) {
/* 316 */         Timer timer = startNode.getTimer();
/* 317 */         xmlDump.append(new StringBuilder().append("      <timerEventDefinition>").append(EOL).toString());
/* 318 */         if ((timer != null) && ((timer.getDelay() != null) || (timer.getDate() != null))) {
/* 319 */           if (timer.getTimeType() == 3)
/* 320 */             xmlDump.append(new StringBuilder().append("        <timeDate xsi:type=\"tFormalExpression\">").append(XmlDumper.replaceIllegalChars(timer.getDate())).append("</timeDate>").append(EOL).toString());
/* 321 */           else if (timer.getTimeType() == 1)
/* 322 */             xmlDump.append(new StringBuilder().append("        <timeDuration xsi:type=\"tFormalExpression\">").append(XmlDumper.replaceIllegalChars(timer.getDelay())).append("</timeDuration>").append(EOL).toString());
/* 323 */           else if (timer.getTimeType() == 2)
/*     */           {
/* 325 */             if (timer.getPeriod() != null)
/* 326 */               xmlDump.append(new StringBuilder().append("        <timeCycle xsi:type=\"tFormalExpression\">").append(XmlDumper.replaceIllegalChars(timer.getDelay())).append("###").append(XmlDumper.replaceIllegalChars(timer.getPeriod())).append("</timeCycle>").append(EOL).toString());
/*     */             else
/* 328 */               xmlDump.append(new StringBuilder().append("        <timeCycle xsi:type=\"tFormalExpression\">").append(XmlDumper.replaceIllegalChars(timer.getDelay())).append("</timeCycle>").append(EOL).toString());
/*     */           }
/* 330 */           else if (timer.getTimeType() == 3) {
/* 331 */             xmlDump.append(new StringBuilder().append("        <timeDate xsi:type=\"tFormalExpression\">").append(XmlDumper.replaceIllegalChars(timer.getDelay())).append("</timeDate>").append(EOL).toString());
/*     */           }
/*     */         }
/* 334 */         xmlDump.append(new StringBuilder().append("      </timerEventDefinition>").append(EOL).toString());
/*     */       }
/* 336 */     } else if (startNode.getTimer() != null) {
/* 337 */       Timer timer = startNode.getTimer();
/* 338 */       xmlDump.append(new StringBuilder().append("      <timerEventDefinition>").append(EOL).toString());
/* 339 */       if ((timer != null) && ((timer.getDelay() != null) || (timer.getDate() != null))) {
/* 340 */         if (timer.getTimeType() == 3)
/* 341 */           xmlDump.append(new StringBuilder().append("        <timeDate xsi:type=\"tFormalExpression\">").append(XmlDumper.replaceIllegalChars(timer.getDate())).append("</timeDate>").append(EOL).toString());
/* 342 */         else if (timer.getTimeType() == 1)
/* 343 */           xmlDump.append(new StringBuilder().append("        <timeDuration xsi:type=\"tFormalExpression\">").append(XmlDumper.replaceIllegalChars(timer.getDelay())).append("</timeDuration>").append(EOL).toString());
/* 344 */         else if (timer.getTimeType() == 2)
/*     */         {
/* 346 */           if (timer.getPeriod() != null)
/* 347 */             xmlDump.append(new StringBuilder().append("        <timeCycle xsi:type=\"tFormalExpression\">").append(XmlDumper.replaceIllegalChars(timer.getDelay())).append("###").append(XmlDumper.replaceIllegalChars(timer.getPeriod())).append("</timeCycle>").append(EOL).toString());
/*     */           else
/* 349 */             xmlDump.append(new StringBuilder().append("        <timeCycle xsi:type=\"tFormalExpression\">").append(XmlDumper.replaceIllegalChars(timer.getDelay())).append("</timeCycle>").append(EOL).toString());
/*     */         }
/* 351 */         else if (timer.getTimeType() == 3) {
/* 352 */           xmlDump.append(new StringBuilder().append("        <timeDate xsi:type=\"tFormalExpression\">").append(XmlDumper.replaceIllegalChars(timer.getDelay())).append("</timeDate>").append(EOL).toString());
/*     */         }
/*     */       }
/* 355 */       xmlDump.append(new StringBuilder().append("      </timerEventDefinition>").append(EOL).toString());
/*     */     }
/* 357 */     endNode("startEvent", xmlDump);
/*     */   }
/*     */ 
/*     */   protected void handleTimerNode(org.jbpm.workflow.core.Node node, Element element, String uri, String localName, ExtensibleXmlParser parser)
/*     */     throws SAXException
/*     */   {
/* 363 */     super.handleNode(node, element, uri, localName, parser);
/* 364 */     StartNode startNode = (StartNode)node;
/* 365 */     org.w3c.dom.Node xmlNode = element.getFirstChild();
/* 366 */     while (xmlNode != null) {
/* 367 */       String nodeName = xmlNode.getNodeName();
/* 368 */       if ("timerEventDefinition".equals(nodeName)) {
/* 369 */         Timer timer = new Timer();
/* 370 */         org.w3c.dom.Node subNode = xmlNode.getFirstChild();
/* 371 */         while ((subNode instanceof Element)) {
/* 372 */           String subNodeName = subNode.getNodeName();
/* 373 */           if ("timeCycle".equals(subNodeName)) {
/* 374 */             String delay = subNode.getTextContent();
/* 375 */             int index = delay.indexOf("###");
/* 376 */             if (index != -1) {
/* 377 */               String period = delay.substring(index + 3);
/* 378 */               delay = delay.substring(0, index);
/* 379 */               timer.setPeriod(period);
/*     */             } else {
/* 381 */               timer.setPeriod(delay);
/*     */             }
/* 383 */             timer.setTimeType(2);
/* 384 */             timer.setDelay(delay);
/* 385 */             break;
/* 386 */           }if ("timeDuration".equals(subNodeName)) {
/* 387 */             String delay = subNode.getTextContent();
/* 388 */             timer.setTimeType(1);
/* 389 */             timer.setDelay(delay);
/* 390 */             break;
/* 391 */           }if ("timeDate".equals(subNodeName)) {
/* 392 */             String date = subNode.getTextContent();
/* 393 */             timer.setTimeType(3);
/* 394 */             timer.setDate(date);
/* 395 */             break;
/*     */           }
/* 397 */           subNode = subNode.getNextSibling();
/*     */         }
/* 399 */         startNode.setTimer(timer);
/* 400 */         if ((parser.getParent() instanceof EventSubProcessNode))
/*     */         {
/* 403 */           EventTrigger trigger = new EventTrigger();
/* 404 */           EventTypeFilter eventFilter = new EventTypeFilter();
/* 405 */           eventFilter.setType(new StringBuilder().append("Timer-").append(((EventSubProcessNode)parser.getParent()).getId()).toString());
/* 406 */           trigger.addEventFilter(eventFilter);
/* 407 */           String mapping = (String)startNode.getMetaData("TriggerMapping");
/* 408 */           if (mapping != null) {
/* 409 */             trigger.addInMapping(mapping, "event");
/*     */           }
/* 411 */           startNode.addTrigger(trigger);
/* 412 */           ((EventSubProcessNode)parser.getParent()).addTimer(timer, new DroolsConsequenceAction("java", ""));
/*     */         }
/*     */       }
/* 415 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void handleCompensationNode(StartNode startNode, Element element, org.w3c.dom.Node xmlNode, ExtensibleXmlParser parser) throws SAXException
/*     */   {
/* 421 */     if (startNode.isInterrupting()) {
/* 422 */       logger.warn(new StringBuilder().append("Compensation Event Sub-Processes [").append(startNode.getMetaData("UniqueId")).append("] may not be specified as interrupting: overriding attribute and setting to not-interrupting.").toString());
/*     */     }
/*     */ 
/* 425 */     startNode.setInterrupting(false);
/*     */ 
/* 440 */     String activityRef = ((Element)xmlNode).getAttribute("activityRef");
/* 441 */     if ((activityRef != null) && (activityRef.length() > 0)) {
/* 442 */       logger.warn(new StringBuilder().append("activityRef value [").append(activityRef).append("] on Start Event '").append(startNode.getMetaData("UniqueId")).append("' ignored per the BPMN2 specification.").toString());
/*     */     }
/*     */ 
/* 447 */     EventTrigger startTrigger = new EventTrigger();
/* 448 */     EventFilter eventFilter = new NonAcceptingEventTypeFilter();
/* 449 */     ((NonAcceptingEventTypeFilter)eventFilter).setType("Compensation");
/* 450 */     startTrigger.addEventFilter(eventFilter);
/* 451 */     List startTriggers = new ArrayList();
/* 452 */     startTriggers.add(startTrigger);
/* 453 */     startNode.setTriggers(startTriggers);
/* 454 */     String mapping = (String)startNode.getMetaData("TriggerMapping");
/* 455 */     if (mapping != null)
/* 456 */       startTrigger.addInMapping(mapping, startNode.getOutMapping(mapping));
/*     */   }
/*     */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.StartEventHandler
 * JD-Core Version:    0.6.0
 */