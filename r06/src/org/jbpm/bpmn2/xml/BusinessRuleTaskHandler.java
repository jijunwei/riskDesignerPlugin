/*     */ package org.jbpm.bpmn2.xml;
/*     */ 
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import org.drools.compiler.compiler.xml.XmlDumper;
/*     */ import org.drools.core.xml.ExtensibleXmlParser;
/*     */ import org.jbpm.process.core.impl.DataTransformerRegistry;
/*     */ import org.jbpm.workflow.core.node.Assignment;
/*     */ import org.jbpm.workflow.core.node.DataAssociation;
/*     */ import org.jbpm.workflow.core.node.RuleSetNode;
/*     */ import org.jbpm.workflow.core.node.Transformation;
/*     */ import org.kie.api.runtime.process.DataTransformer;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.Text;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class BusinessRuleTaskHandler extends AbstractNodeHandler
/*     */ {
/*     */   private static final String NAMESPACE_PROP = "namespace";
/*     */   private static final String MODEL_PROP = "model";
/*     */   private static final String DECISION_PROP = "decision";
/*  43 */   private DataTransformerRegistry transformerRegistry = DataTransformerRegistry.get();
/*     */ 
/*     */   protected org.jbpm.workflow.core.Node createNode(Attributes attrs) {
/*  46 */     return new RuleSetNode();
/*     */   }
/*     */ 
/*     */   public Class generateNodeFor()
/*     */   {
/*  51 */     return RuleSetNode.class;
/*     */   }
/*     */ 
/*     */   protected void handleNode(org.jbpm.workflow.core.Node node, Element element, String uri, String localName, ExtensibleXmlParser parser) throws SAXException
/*     */   {
/*  56 */     super.handleNode(node, element, uri, localName, parser);
/*  57 */     RuleSetNode ruleSetNode = (RuleSetNode)node;
/*  58 */     String ruleFlowGroup = element.getAttribute("ruleFlowGroup");
/*  59 */     if (ruleFlowGroup != null) {
/*  60 */       ruleSetNode.setRuleFlowGroup(ruleFlowGroup);
/*     */     }
/*  62 */     String language = element.getAttribute("implementation");
/*  63 */     if ((language == null) || (language.equalsIgnoreCase("##unspecified")) || (language.isEmpty())) {
/*  64 */       language = "http://www.jboss.org/drools/rule";
/*     */     }
/*  66 */     ruleSetNode.setLanguage(language);
/*     */ 
/*  68 */     org.w3c.dom.Node xmlNode = element.getFirstChild();
/*  69 */     while (xmlNode != null) {
/*  70 */       String nodeName = xmlNode.getNodeName();
/*  71 */       if ("ioSpecification".equals(nodeName))
/*  72 */         readIoSpecification(xmlNode, this.dataInputs, this.dataOutputs);
/*  73 */       else if ("dataInputAssociation".equals(nodeName))
/*  74 */         readDataInputAssociation(xmlNode, ruleSetNode, this.dataInputs);
/*  75 */       else if ("dataOutputAssociation".equals(nodeName)) {
/*  76 */         readDataOutputAssociation(xmlNode, ruleSetNode, this.dataOutputs);
/*     */       }
/*  78 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/*  80 */     ruleSetNode.setNamespace((String)ruleSetNode.removeParameter("namespace"));
/*  81 */     ruleSetNode.setModel((String)ruleSetNode.removeParameter("model"));
/*  82 */     ruleSetNode.setDecision((String)ruleSetNode.removeParameter("decision"));
/*     */ 
/*  84 */     handleScript(ruleSetNode, element, "onEntry");
/*  85 */     handleScript(ruleSetNode, element, "onExit");
/*     */   }
/*     */ 
/*     */   public void writeNode(org.jbpm.workflow.core.Node node, StringBuilder xmlDump, int metaDataType) {
/*  89 */     RuleSetNode ruleSetNode = (RuleSetNode)node;
/*  90 */     writeNode("businessRuleTask", ruleSetNode, xmlDump, metaDataType);
/*  91 */     if (ruleSetNode.getRuleFlowGroup() != null) {
/*  92 */       xmlDump.append("g:ruleFlowGroup=\"" + XmlBPMNProcessDumper.replaceIllegalCharsAttribute(ruleSetNode.getRuleFlowGroup()) + "\" " + EOL);
/*     */     }
/*     */ 
/*  95 */     xmlDump.append(" implementation=\"" + XmlBPMNProcessDumper.replaceIllegalCharsAttribute(ruleSetNode.getLanguage()) + "\" >" + EOL);
/*     */ 
/*  97 */     writeExtensionElements(ruleSetNode, xmlDump);
/*  98 */     writeIO(ruleSetNode, xmlDump);
/*  99 */     endNode("businessRuleTask", xmlDump);
/*     */   }
/*     */ 
/*     */   protected void readDataInputAssociation(org.w3c.dom.Node xmlNode, RuleSetNode ruleSetNode, Map<String, String> dataInputs)
/*     */   {
/* 104 */     org.w3c.dom.Node subNode = xmlNode.getFirstChild();
/* 105 */     if ("sourceRef".equals(subNode.getNodeName())) {
/* 106 */       String source = subNode.getTextContent();
/*     */ 
/* 108 */       subNode = subNode.getNextSibling();
/* 109 */       String target = subNode.getTextContent();
/*     */ 
/* 111 */       Transformation transformation = null;
/* 112 */       subNode = subNode.getNextSibling();
/* 113 */       if ((subNode != null) && ("transformation".equals(subNode.getNodeName()))) {
/* 114 */         String lang = subNode.getAttributes().getNamedItem("language").getNodeValue();
/* 115 */         String expression = subNode.getTextContent();
/*     */ 
/* 117 */         DataTransformer transformer = this.transformerRegistry.find(lang);
/* 118 */         if (transformer == null) {
/* 119 */           throw new IllegalArgumentException("No transformer registered for language " + lang);
/*     */         }
/* 121 */         transformation = new Transformation(lang, expression);
/*     */ 
/* 123 */         subNode = subNode.getNextSibling();
/*     */       }
/*     */ 
/* 126 */       List assignments = new LinkedList();
/* 127 */       while (subNode != null) {
/* 128 */         org.w3c.dom.Node ssubNode = subNode.getFirstChild();
/* 129 */         String from = ssubNode.getTextContent();
/* 130 */         String to = ssubNode.getNextSibling().getTextContent();
/* 131 */         assignments.add(new Assignment("XPath", from, to));
/* 132 */         subNode = subNode.getNextSibling();
/*     */       }
/* 134 */       ruleSetNode.addInAssociation(
/* 136 */         new DataAssociation(source, 
/* 136 */         (String)dataInputs
/* 136 */         .get(target), 
/* 136 */         assignments, transformation));
/*     */     }
/*     */     else {
/* 139 */       String to = subNode.getTextContent();
/*     */ 
/* 141 */       subNode = subNode.getNextSibling();
/* 142 */       if (subNode != null) {
/* 143 */         org.w3c.dom.Node subSubNode = subNode.getFirstChild();
/* 144 */         NodeList nl = subSubNode.getChildNodes();
/* 145 */         if (nl.getLength() > 1)
/*     */         {
/* 147 */           ruleSetNode.setParameter((String)dataInputs.get(to), subSubNode.getTextContent());
/* 148 */           return;
/* 149 */         }if (nl.getLength() == 0) {
/* 150 */           return;
/*     */         }
/* 152 */         Object result = null;
/* 153 */         Object from = nl.item(0);
/* 154 */         if ((from instanceof Text)) {
/* 155 */           String text = ((Text)from).getTextContent();
/* 156 */           if ((text.startsWith("\"")) && (text.endsWith("\"")))
/* 157 */             result = text.substring(1, text.length() - 1);
/*     */           else
/* 159 */             result = text;
/*     */         }
/*     */         else {
/* 162 */           result = nl.item(0);
/*     */         }
/* 164 */         ruleSetNode.setParameter((String)dataInputs.get(to), result);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void readDataOutputAssociation(org.w3c.dom.Node xmlNode, RuleSetNode ruleSetNode, Map<String, String> dataOutputs)
/*     */   {
/* 171 */     org.w3c.dom.Node subNode = xmlNode.getFirstChild();
/* 172 */     String source = subNode.getTextContent();
/*     */ 
/* 174 */     subNode = subNode.getNextSibling();
/* 175 */     String target = subNode.getTextContent();
/*     */ 
/* 177 */     Transformation transformation = null;
/* 178 */     subNode = subNode.getNextSibling();
/* 179 */     if ((subNode != null) && ("transformation".equals(subNode.getNodeName()))) {
/* 180 */       String lang = subNode.getAttributes().getNamedItem("language").getNodeValue();
/* 181 */       String expression = subNode.getTextContent();
/* 182 */       DataTransformer transformer = this.transformerRegistry.find(lang);
/* 183 */       if (transformer == null) {
/* 184 */         throw new IllegalArgumentException("No transformer registered for language " + lang);
/*     */       }
/* 186 */       transformation = new Transformation(lang, expression, source);
/* 187 */       subNode = subNode.getNextSibling();
/*     */     }
/*     */ 
/* 190 */     List assignments = new LinkedList();
/* 191 */     while (subNode != null) {
/* 192 */       org.w3c.dom.Node ssubNode = subNode.getFirstChild();
/* 193 */       String from = ssubNode.getTextContent();
/* 194 */       String to = ssubNode.getNextSibling().getTextContent();
/* 195 */       assignments.add(new Assignment("XPath", from, to));
/* 196 */       subNode = subNode.getNextSibling();
/*     */     }
/* 198 */     ruleSetNode.addOutAssociation(new DataAssociation((String)dataOutputs.get(source), target, assignments, transformation));
/*     */   }
/*     */ 
/*     */   protected void writeIO(RuleSetNode ruleSetNode, StringBuilder xmlDump) {
/* 202 */     xmlDump.append("      <ioSpecification>" + EOL);
/* 203 */     for (Map.Entry entry : ruleSetNode.getInMappings().entrySet()) {
/* 204 */       xmlDump.append("        <dataInput id=\"" + XmlBPMNProcessDumper.getUniqueNodeId(ruleSetNode) + "_" + XmlBPMNProcessDumper.replaceIllegalCharsAttribute((String)entry.getKey()) + "Input\" name=\"" + XmlBPMNProcessDumper.replaceIllegalCharsAttribute((String)entry.getKey()) + "\" />" + EOL);
/*     */     }
/* 206 */     for (Map.Entry entry : ruleSetNode.getParameters().entrySet()) {
/* 207 */       if ((!"ActorId".equals(entry.getKey())) && (entry.getValue() != null)) {
/* 208 */         xmlDump.append("        <dataInput id=\"" + XmlBPMNProcessDumper.getUniqueNodeId(ruleSetNode) + "_" + XmlBPMNProcessDumper.replaceIllegalCharsAttribute((String)entry.getKey()) + "Input\" name=\"" + XmlBPMNProcessDumper.replaceIllegalCharsAttribute((String)entry.getKey()) + "\" />" + EOL);
/*     */       }
/*     */     }
/* 211 */     for (Map.Entry entry : ruleSetNode.getOutMappings().entrySet()) {
/* 212 */       xmlDump.append("        <dataOutput id=\"" + XmlBPMNProcessDumper.getUniqueNodeId(ruleSetNode) + "_" + XmlBPMNProcessDumper.replaceIllegalCharsAttribute((String)entry.getKey()) + "Output\" name=\"" + XmlBPMNProcessDumper.replaceIllegalCharsAttribute((String)entry.getKey()) + "\" />" + EOL);
/*     */     }
/* 214 */     xmlDump.append("        <inputSet>" + EOL);
/* 215 */     for (Map.Entry entry : ruleSetNode.getInMappings().entrySet()) {
/* 216 */       xmlDump.append("          <dataInputRefs>" + XmlBPMNProcessDumper.getUniqueNodeId(ruleSetNode) + "_" + XmlDumper.replaceIllegalChars((String)entry.getKey()) + "Input</dataInputRefs>" + EOL);
/*     */     }
/* 218 */     for (Map.Entry entry : ruleSetNode.getParameters().entrySet()) {
/* 219 */       if ((!"ActorId".equals(entry.getKey())) && (entry.getValue() != null)) {
/* 220 */         xmlDump.append("          <dataInputRefs>" + XmlBPMNProcessDumper.getUniqueNodeId(ruleSetNode) + "_" + XmlDumper.replaceIllegalChars((String)entry.getKey()) + "Input</dataInputRefs>" + EOL);
/*     */       }
/*     */     }
/* 223 */     xmlDump.append("        </inputSet>" + EOL);
/*     */ 
/* 225 */     xmlDump.append("        <outputSet>" + EOL);
/* 226 */     for (Map.Entry entry : ruleSetNode.getOutMappings().entrySet()) {
/* 227 */       xmlDump.append("          <dataOutputRefs>" + XmlBPMNProcessDumper.getUniqueNodeId(ruleSetNode) + "_" + XmlDumper.replaceIllegalChars((String)entry.getKey()) + "Output</dataOutputRefs>" + EOL);
/*     */     }
/* 229 */     xmlDump.append("        </outputSet>" + EOL);
/*     */ 
/* 231 */     xmlDump.append("      </ioSpecification>" + EOL);
/*     */ 
/* 233 */     for (Map.Entry entry : ruleSetNode.getInMappings().entrySet()) {
/* 234 */       xmlDump.append("      <dataInputAssociation>" + EOL);
/* 235 */       xmlDump.append("        <sourceRef>" + 
/* 236 */         XmlDumper.replaceIllegalChars((String)entry
/* 236 */         .getValue()) + "</sourceRef>" + EOL + "        <targetRef>" + 
/* 237 */         XmlBPMNProcessDumper.getUniqueNodeId(ruleSetNode) + 
/* 237 */         "_" + XmlDumper.replaceIllegalChars((String)entry.getKey()) + "Input</targetRef>" + EOL);
/* 238 */       xmlDump.append("      </dataInputAssociation>" + EOL);
/*     */     }
/* 240 */     for (Map.Entry entry : ruleSetNode.getParameters().entrySet()) {
/* 241 */       if ((!"ActorId".equals(entry.getKey())) && (entry.getValue() != null)) {
/* 242 */         xmlDump.append("      <dataInputAssociation>" + EOL);
/* 243 */         xmlDump.append("        <targetRef>" + 
/* 244 */           XmlBPMNProcessDumper.getUniqueNodeId(ruleSetNode) + 
/* 244 */           "_" + XmlDumper.replaceIllegalChars((String)entry.getKey()) + "Input</targetRef>" + EOL + "        <assignment>" + EOL + "          <from xsi:type=\"tFormalExpression\">" + 
/* 246 */           XmlDumper.replaceIllegalChars(entry
/* 246 */           .getValue().toString()) + "</from>" + EOL + "          <to xsi:type=\"tFormalExpression\">" + 
/* 247 */           XmlBPMNProcessDumper.getUniqueNodeId(ruleSetNode) + 
/* 247 */           "_" + XmlDumper.replaceIllegalChars((String)entry.getKey()) + "Input</to>" + EOL + "        </assignment>" + EOL);
/*     */ 
/* 249 */         xmlDump.append("      </dataInputAssociation>" + EOL);
/*     */       }
/*     */     }
/* 252 */     for (Map.Entry entry : ruleSetNode.getOutMappings().entrySet()) {
/* 253 */       xmlDump.append("      <dataOutputAssociation>" + EOL);
/* 254 */       xmlDump.append("        <sourceRef>" + 
/* 255 */         XmlBPMNProcessDumper.getUniqueNodeId(ruleSetNode) + 
/* 255 */         "_" + XmlDumper.replaceIllegalChars((String)entry.getKey()) + "Output</sourceRef>" + EOL + "        <targetRef>" + 
/* 256 */         XmlDumper.replaceIllegalChars((String)entry
/* 256 */         .getValue()) + "</targetRef>" + EOL);
/* 257 */       xmlDump.append("      </dataOutputAssociation>" + EOL);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.BusinessRuleTaskHandler
 * JD-Core Version:    0.6.0
 */