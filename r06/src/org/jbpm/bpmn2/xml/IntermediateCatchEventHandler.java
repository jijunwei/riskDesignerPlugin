/*     */ package org.jbpm.bpmn2.xml;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.drools.core.xml.ExtensibleXmlParser;
/*     */ import org.jbpm.bpmn2.core.IntermediateLink;
/*     */ import org.jbpm.bpmn2.core.Message;
/*     */ import org.jbpm.compiler.xml.ProcessBuildData;
/*     */ import org.jbpm.process.core.event.EventTransformerImpl;
/*     */ import org.jbpm.process.core.event.EventTypeFilter;
/*     */ import org.jbpm.process.core.impl.DataTransformerRegistry;
/*     */ import org.jbpm.process.core.timer.Timer;
/*     */ import org.jbpm.ruleflow.core.RuleFlowProcess;
/*     */ import org.jbpm.workflow.core.NodeContainer;
/*     */ import org.jbpm.workflow.core.node.CatchLinkNode;
/*     */ import org.jbpm.workflow.core.node.CompositeNode;
/*     */ import org.jbpm.workflow.core.node.EventNode;
/*     */ import org.jbpm.workflow.core.node.StateNode;
/*     */ import org.jbpm.workflow.core.node.TimerNode;
/*     */ import org.jbpm.workflow.core.node.Transformation;
/*     */ import org.kie.api.runtime.process.DataTransformer;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class IntermediateCatchEventHandler extends AbstractNodeHandler
/*     */ {
/*  50 */   private DataTransformerRegistry transformerRegistry = DataTransformerRegistry.get();
/*     */   public static final String LINK_NAME = "LinkName";
/*     */ 
/*     */   protected org.jbpm.workflow.core.Node createNode(Attributes attrs)
/*     */   {
/*  55 */     return new EventNode();
/*     */   }
/*     */ 
/*     */   public Class generateNodeFor()
/*     */   {
/*  60 */     return EventNode.class;
/*     */   }
/*     */ 
/*     */   public Object end(String uri, String localName, ExtensibleXmlParser parser) throws SAXException
/*     */   {
/*  65 */     Element element = parser.endElementBuilder();
/*  66 */     org.jbpm.workflow.core.Node node = (org.jbpm.workflow.core.Node)parser.getCurrent();
/*     */ 
/*  69 */     org.w3c.dom.Node xmlNode = element.getFirstChild();
/*  70 */     while (xmlNode != null) {
/*  71 */       String nodeName = xmlNode.getNodeName();
/*  72 */       if ("signalEventDefinition".equals(nodeName))
/*     */       {
/*  74 */         handleSignalNode(node, element, uri, localName, parser);
/*  75 */         break;
/*  76 */       }if ("messageEventDefinition".equals(nodeName))
/*     */       {
/*  78 */         handleMessageNode(node, element, uri, localName, parser);
/*  79 */         break;
/*  80 */       }if ("timerEventDefinition".equals(nodeName))
/*     */       {
/*  82 */         TimerNode timerNode = new TimerNode();
/*  83 */         timerNode.setId(node.getId());
/*  84 */         timerNode.setName(node.getName());
/*  85 */         timerNode.setMetaData("UniqueId", node
/*  86 */           .getMetaData().get("UniqueId"));
/*  87 */         node = timerNode;
/*  88 */         handleTimerNode(node, element, uri, localName, parser);
/*  89 */         break;
/*  90 */       }if ("conditionalEventDefinition".equals(nodeName))
/*     */       {
/*  92 */         StateNode stateNode = new StateNode();
/*  93 */         stateNode.setId(node.getId());
/*  94 */         stateNode.setName(node.getName());
/*  95 */         stateNode.setMetaData("UniqueId", node
/*  96 */           .getMetaData().get("UniqueId"));
/*  97 */         node = stateNode;
/*  98 */         handleStateNode(node, element, uri, localName, parser);
/*  99 */         break;
/* 100 */       }if ("linkEventDefinition".equals(nodeName)) {
/* 101 */         CatchLinkNode linkNode = new CatchLinkNode();
/* 102 */         linkNode.setId(node.getId());
/* 103 */         node = linkNode;
/* 104 */         handleLinkNode(element, node, xmlNode, parser);
/* 105 */         break;
/*     */       }
/* 107 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/* 109 */     NodeContainer nodeContainer = (NodeContainer)parser.getParent();
/* 110 */     nodeContainer.addNode(node);
/* 111 */     ((ProcessBuildData)parser.getData()).addNode(node);
/* 112 */     return node;
/*     */   }
/*     */ 
/*     */   protected void handleLinkNode(Element element, org.jbpm.workflow.core.Node node, org.w3c.dom.Node xmlLinkNode, ExtensibleXmlParser parser)
/*     */   {
/* 117 */     NodeContainer nodeContainer = (NodeContainer)parser.getParent();
/*     */ 
/* 119 */     node.setName(element.getAttribute("name"));
/*     */ 
/* 121 */     NamedNodeMap linkAttr = xmlLinkNode.getAttributes();
/* 122 */     String name = linkAttr.getNamedItem("name").getNodeValue();
/* 123 */     String id = element.getAttribute("id");
/*     */ 
/* 125 */     node.setMetaData("UniqueId", id);
/* 126 */     node.setMetaData("LinkName", name);
/*     */ 
/* 128 */     org.w3c.dom.Node xmlNode = xmlLinkNode.getFirstChild();
/*     */ 
/* 130 */     IntermediateLink aLink = new IntermediateLink();
/* 131 */     aLink.setName(name);
/* 132 */     aLink.setUniqueId(id);
/*     */ 
/* 134 */     while (null != xmlNode) {
/* 135 */       String nodeName = xmlNode.getNodeName();
/* 136 */       if ("target".equals(nodeName)) {
/* 137 */         String target = xmlNode.getTextContent();
/* 138 */         node.setMetaData("target", target);
/* 139 */         aLink.setTarget(target);
/*     */       }
/* 141 */       if ("source".equals(nodeName)) {
/* 142 */         String source = xmlNode.getTextContent();
/* 143 */         node.setMetaData("source", source);
/* 144 */         aLink.addSource(source);
/*     */       }
/* 146 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/*     */ 
/* 149 */     if ((nodeContainer instanceof RuleFlowProcess)) {
/* 150 */       RuleFlowProcess process = (RuleFlowProcess)nodeContainer;
/*     */ 
/* 152 */       List links = (List)process
/* 152 */         .getMetaData().get("BPMN.ThrowLinks");
/* 153 */       if (null == links) {
/* 154 */         links = new ArrayList();
/*     */       }
/* 156 */       links.add(aLink);
/* 157 */       process.setMetaData("BPMN.ThrowLinks", links);
/* 158 */     } else if ((nodeContainer instanceof CompositeNode)) {
/* 159 */       CompositeNode subprocess = (CompositeNode)nodeContainer;
/*     */ 
/* 161 */       List links = (List)subprocess
/* 161 */         .getMetaData().get("BPMN.ThrowLinks");
/* 162 */       if (null == links) {
/* 163 */         links = new ArrayList();
/*     */       }
/* 165 */       links.add(aLink);
/* 166 */       subprocess.setMetaData("BPMN.ThrowLinks", links);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void handleSignalNode(org.jbpm.workflow.core.Node node, Element element, String uri, String localName, ExtensibleXmlParser parser)
/*     */     throws SAXException
/*     */   {
/* 174 */     super.handleNode(node, element, uri, localName, parser);
/* 175 */     EventNode eventNode = (EventNode)node;
/* 176 */     org.w3c.dom.Node xmlNode = element.getFirstChild();
/* 177 */     while (xmlNode != null) {
/* 178 */       String nodeName = xmlNode.getNodeName();
/* 179 */       if ("dataOutput".equals(nodeName)) {
/* 180 */         String id = ((Element)xmlNode).getAttribute("id");
/* 181 */         String outputName = ((Element)xmlNode).getAttribute("name");
/* 182 */         this.dataOutputs.put(id, outputName);
/* 183 */       } else if ("dataOutputAssociation".equals(nodeName)) {
/* 184 */         readDataOutputAssociation(xmlNode, eventNode);
/* 185 */       } else if ("signalEventDefinition".equals(nodeName)) {
/* 186 */         String type = ((Element)xmlNode).getAttribute("signalRef");
/* 187 */         if ((type != null) && (type.trim().length() > 0))
/*     */         {
/* 189 */           type = checkSignalAndConvertToRealSignalNam(parser, type);
/*     */ 
/* 191 */           List eventFilters = new ArrayList();
/* 192 */           EventTypeFilter eventFilter = new EventTypeFilter();
/* 193 */           eventFilter.setType(type);
/* 194 */           eventFilters.add(eventFilter);
/* 195 */           eventNode.setEventFilters(eventFilters);
/*     */         }
/*     */       }
/* 198 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void handleMessageNode(org.jbpm.workflow.core.Node node, Element element, String uri, String localName, ExtensibleXmlParser parser)
/*     */     throws SAXException
/*     */   {
/* 206 */     super.handleNode(node, element, uri, localName, parser);
/* 207 */     EventNode eventNode = (EventNode)node;
/* 208 */     org.w3c.dom.Node xmlNode = element.getFirstChild();
/* 209 */     while (xmlNode != null) {
/* 210 */       String nodeName = xmlNode.getNodeName();
/* 211 */       if ("dataOutput".equals(nodeName)) {
/* 212 */         String id = ((Element)xmlNode).getAttribute("id");
/* 213 */         String outputName = ((Element)xmlNode).getAttribute("name");
/* 214 */         this.dataOutputs.put(id, outputName);
/* 215 */       } else if ("dataOutputAssociation".equals(nodeName)) {
/* 216 */         readDataOutputAssociation(xmlNode, eventNode);
/* 217 */       } else if ("messageEventDefinition".equals(nodeName))
/*     */       {
/* 219 */         String messageRef = ((Element)xmlNode)
/* 219 */           .getAttribute("messageRef");
/*     */ 
/* 221 */         Map messages = (Map)((ProcessBuildData)parser
/* 221 */           .getData()).getMetaData("Messages");
/* 222 */         if (messages == null) {
/* 223 */           throw new IllegalArgumentException("No messages found");
/*     */         }
/* 225 */         Message message = (Message)messages.get(messageRef);
/* 226 */         if (message == null) {
/* 227 */           throw new IllegalArgumentException("Could not find message " + messageRef);
/*     */         }
/*     */ 
/* 230 */         eventNode.setMetaData("MessageType", message.getType());
/* 231 */         List eventFilters = new ArrayList();
/* 232 */         EventTypeFilter eventFilter = new EventTypeFilter();
/* 233 */         eventFilter.setType("Message-" + messageRef);
/* 234 */         eventFilters.add(eventFilter);
/* 235 */         eventNode.setEventFilters(eventFilters);
/*     */       }
/* 237 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void handleTimerNode(org.jbpm.workflow.core.Node node, Element element, String uri, String localName, ExtensibleXmlParser parser)
/*     */     throws SAXException
/*     */   {
/* 244 */     super.handleNode(node, element, uri, localName, parser);
/* 245 */     TimerNode timerNode = (TimerNode)node;
/* 246 */     org.w3c.dom.Node xmlNode = element.getFirstChild();
/* 247 */     while (xmlNode != null) {
/* 248 */       String nodeName = xmlNode.getNodeName();
/* 249 */       if ("timerEventDefinition".equals(nodeName)) {
/* 250 */         Timer timer = new Timer();
/* 251 */         org.w3c.dom.Node subNode = xmlNode.getFirstChild();
/* 252 */         while ((subNode instanceof Element)) {
/* 253 */           String subNodeName = subNode.getNodeName();
/* 254 */           if ("timeCycle".equals(subNodeName)) {
/* 255 */             String delay = subNode.getTextContent();
/* 256 */             int index = delay.indexOf("###");
/* 257 */             if (index != -1) {
/* 258 */               String period = delay.substring(index + 3);
/* 259 */               delay = delay.substring(0, index);
/* 260 */               timer.setPeriod(period);
/*     */             }
/* 262 */             timer.setTimeType(2);
/* 263 */             timer.setDelay(delay);
/* 264 */             break;
/* 265 */           }if ("timeDuration".equals(subNodeName)) {
/* 266 */             String delay = subNode.getTextContent();
/* 267 */             timer.setTimeType(1);
/* 268 */             timer.setDelay(delay);
/* 269 */             break;
/* 270 */           }if ("timeDate".equals(subNodeName)) {
/* 271 */             String date = subNode.getTextContent();
/* 272 */             timer.setTimeType(3);
/* 273 */             timer.setDate(date);
/* 274 */             break;
/*     */           }
/* 276 */           subNode = subNode.getNextSibling();
/*     */         }
/* 278 */         timerNode.setTimer(timer);
/*     */       }
/* 280 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void handleStateNode(org.jbpm.workflow.core.Node node, Element element, String uri, String localName, ExtensibleXmlParser parser)
/*     */     throws SAXException
/*     */   {
/* 287 */     super.handleNode(node, element, uri, localName, parser);
/* 288 */     StateNode stateNode = (StateNode)node;
/* 289 */     org.w3c.dom.Node xmlNode = element.getFirstChild();
/* 290 */     while (xmlNode != null) {
/* 291 */       String nodeName = xmlNode.getNodeName();
/* 292 */       if ("conditionalEventDefinition".equals(nodeName)) {
/* 293 */         org.w3c.dom.Node subNode = xmlNode.getFirstChild();
/* 294 */         while (subNode != null) {
/* 295 */           String subnodeName = subNode.getNodeName();
/* 296 */           if ("condition".equals(subnodeName)) {
/* 297 */             stateNode.setMetaData("Condition", xmlNode
/* 298 */               .getTextContent());
/* 299 */             break;
/*     */           }
/* 301 */           subNode = subNode.getNextSibling();
/*     */         }
/*     */       }
/* 304 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void readDataOutputAssociation(org.w3c.dom.Node xmlNode, EventNode eventNode)
/*     */   {
/* 311 */     org.w3c.dom.Node subNode = xmlNode.getFirstChild();
/* 312 */     String from = subNode.getTextContent();
/*     */ 
/* 314 */     subNode = subNode.getNextSibling();
/* 315 */     String to = subNode.getTextContent();
/* 316 */     eventNode.setVariableName(to);
/*     */ 
/* 318 */     Transformation transformation = null;
/* 319 */     subNode = subNode.getNextSibling();
/* 320 */     if ((subNode != null) && ("transformation".equals(subNode.getNodeName()))) {
/* 321 */       String lang = subNode.getAttributes().getNamedItem("language").getNodeValue();
/* 322 */       String expression = subNode.getTextContent();
/* 323 */       DataTransformer transformer = this.transformerRegistry.find(lang);
/* 324 */       if (transformer == null) {
/* 325 */         throw new IllegalArgumentException("No transformer registered for language " + lang);
/*     */       }
/* 327 */       transformation = new Transformation(lang, expression, (String)this.dataOutputs.get(from));
/* 328 */       eventNode.setMetaData("Transformation", transformation);
/*     */ 
/* 330 */       eventNode.setEventTransformer(new EventTransformerImpl(transformation));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeNode(org.jbpm.workflow.core.Node node, StringBuilder xmlDump, int metaDataType) {
/* 335 */     throw new IllegalArgumentException("Writing out should be handled by specific handlers");
/*     */   }
/*     */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.IntermediateCatchEventHandler
 * JD-Core Version:    0.6.0
 */