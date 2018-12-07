/*     */ package org.jbpm.bpmn2.xml;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import org.drools.compiler.compiler.xml.XmlDumper;
/*     */ import org.drools.core.xml.ExtensibleXmlParser;
/*     */ import org.jbpm.process.core.Work;
/*     */ import org.jbpm.workflow.core.node.HumanTaskNode;
/*     */ import org.jbpm.workflow.core.node.WorkItemNode;
/*     */ import org.w3c.dom.Element;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class UserTaskHandler extends TaskHandler
/*     */ {
/*     */   protected org.jbpm.workflow.core.Node createNode(Attributes attrs)
/*     */   {
/*  37 */     return new HumanTaskNode();
/*     */   }
/*     */ 
/*     */   public Class generateNodeFor()
/*     */   {
/*  42 */     return HumanTaskNode.class;
/*     */   }
/*     */ 
/*     */   protected void handleNode(org.jbpm.workflow.core.Node node, Element element, String uri, String localName, ExtensibleXmlParser parser) throws SAXException
/*     */   {
/*  47 */     super.handleNode(node, element, uri, localName, parser);
/*  48 */     HumanTaskNode humanTaskNode = (HumanTaskNode)node;
/*  49 */     Work work = humanTaskNode.getWork();
/*  50 */     work.setName("Human Task");
/*  51 */     Map dataInputs = new HashMap();
/*  52 */     Map dataOutputs = new HashMap();
/*  53 */     List owners = new ArrayList();
/*  54 */     org.w3c.dom.Node xmlNode = element.getFirstChild();
/*  55 */     while (xmlNode != null) {
/*  56 */       String nodeName = xmlNode.getNodeName();
/*     */ 
/*  58 */       if ("potentialOwner".equals(nodeName)) {
/*  59 */         String owner = readPotentialOwner(xmlNode, humanTaskNode);
/*  60 */         if (owner != null) {
/*  61 */           owners.add(owner);
/*     */         }
/*     */       }
/*  64 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/*  66 */     if (owners.size() > 0) {
/*  67 */       String owner = (String)owners.get(0);
/*  68 */       for (int i = 1; i < owners.size(); i++) {
/*  69 */         owner = new StringBuilder().append(owner).append(",").append((String)owners.get(i)).toString();
/*     */       }
/*  71 */       humanTaskNode.getWork().setParameter("ActorId", owner);
/*     */     }
/*  73 */     humanTaskNode.getWork().setParameter("NodeName", humanTaskNode.getName());
/*     */   }
/*     */ 
/*     */   protected String readPotentialOwner(org.w3c.dom.Node xmlNode, HumanTaskNode humanTaskNode) {
/*  77 */     org.w3c.dom.Node node = xmlNode.getFirstChild();
/*  78 */     if (node != null) {
/*  79 */       node = node.getFirstChild();
/*  80 */       if (node != null) {
/*  81 */         node = node.getFirstChild();
/*  82 */         if (node != null) {
/*  83 */           return node.getTextContent();
/*     */         }
/*     */       }
/*     */     }
/*  87 */     return null;
/*     */   }
/*     */ 
/*     */   public void writeNode(org.jbpm.workflow.core.Node node, StringBuilder xmlDump, int metaDataType) {
/*  91 */     HumanTaskNode humanTaskNode = (HumanTaskNode)node;
/*  92 */     writeNode("userTask", humanTaskNode, xmlDump, metaDataType);
/*  93 */     xmlDump.append(new StringBuilder().append(">").append(EOL).toString());
/*  94 */     writeExtensionElements(humanTaskNode, xmlDump);
/*  95 */     writeIO(humanTaskNode, xmlDump);
/*  96 */     String ownerString = (String)humanTaskNode.getWork().getParameter("ActorId");
/*  97 */     if (ownerString != null) {
/*  98 */       String[] owners = ownerString.split(",");
/*  99 */       for (String owner : owners) {
/* 100 */         xmlDump.append(new StringBuilder().append("      <potentialOwner>").append(EOL).append("        <resourceAssignmentExpression>").append(EOL).append("          <formalExpression>").append(owner).append("</formalExpression>").append(EOL).append("        </resourceAssignmentExpression>").append(EOL).append("      </potentialOwner>").append(EOL).toString());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 108 */     endNode("userTask", xmlDump);
/*     */   }
/*     */ 
/*     */   protected void writeIO(WorkItemNode workItemNode, StringBuilder xmlDump) {
/* 112 */     xmlDump.append(new StringBuilder().append("      <ioSpecification>").append(EOL).toString());
/* 113 */     for (Map.Entry entry : workItemNode.getInMappings().entrySet()) {
/* 114 */       xmlDump.append(new StringBuilder().append("        <dataInput id=\"").append(XmlBPMNProcessDumper.getUniqueNodeId(workItemNode)).append("_").append(XmlBPMNProcessDumper.replaceIllegalCharsAttribute((String)entry.getKey())).append("Input\" name=\"").append(XmlBPMNProcessDumper.replaceIllegalCharsAttribute((String)entry.getKey())).append("\" />").append(EOL).toString());
/*     */     }
/* 116 */     for (Map.Entry entry : workItemNode.getWork().getParameters().entrySet()) {
/* 117 */       if ((!"ActorId".equals(entry.getKey())) && (entry.getValue() != null)) {
/* 118 */         xmlDump.append(new StringBuilder().append("        <dataInput id=\"").append(XmlBPMNProcessDumper.getUniqueNodeId(workItemNode)).append("_").append(XmlBPMNProcessDumper.replaceIllegalCharsAttribute((String)entry.getKey())).append("Input\" name=\"").append(XmlBPMNProcessDumper.replaceIllegalCharsAttribute((String)entry.getKey())).append("\" />").append(EOL).toString());
/*     */       }
/*     */     }
/* 121 */     for (Map.Entry entry : workItemNode.getOutMappings().entrySet()) {
/* 122 */       xmlDump.append(new StringBuilder().append("        <dataOutput id=\"").append(XmlBPMNProcessDumper.getUniqueNodeId(workItemNode)).append("_").append(XmlBPMNProcessDumper.replaceIllegalCharsAttribute((String)entry.getKey())).append("Output\" name=\"").append(XmlBPMNProcessDumper.replaceIllegalCharsAttribute((String)entry.getKey())).append("\" />").append(EOL).toString());
/*     */     }
/* 124 */     xmlDump.append(new StringBuilder().append("        <inputSet>").append(EOL).toString());
/* 125 */     for (Map.Entry entry : workItemNode.getInMappings().entrySet()) {
/* 126 */       xmlDump.append(new StringBuilder().append("          <dataInputRefs>").append(XmlBPMNProcessDumper.getUniqueNodeId(workItemNode)).append("_").append(XmlDumper.replaceIllegalChars((String)entry.getKey())).append("Input</dataInputRefs>").append(EOL).toString());
/*     */     }
/* 128 */     for (Map.Entry entry : workItemNode.getWork().getParameters().entrySet()) {
/* 129 */       if ((!"ActorId".equals(entry.getKey())) && (entry.getValue() != null)) {
/* 130 */         xmlDump.append(new StringBuilder().append("          <dataInputRefs>").append(XmlBPMNProcessDumper.getUniqueNodeId(workItemNode)).append("_").append(XmlDumper.replaceIllegalChars((String)entry.getKey())).append("Input</dataInputRefs>").append(EOL).toString());
/*     */       }
/*     */     }
/* 133 */     xmlDump.append(new StringBuilder().append("        </inputSet>").append(EOL).toString());
/*     */ 
/* 135 */     xmlDump.append(new StringBuilder().append("        <outputSet>").append(EOL).toString());
/* 136 */     for (Map.Entry entry : workItemNode.getOutMappings().entrySet()) {
/* 137 */       xmlDump.append(new StringBuilder().append("          <dataOutputRefs>").append(XmlBPMNProcessDumper.getUniqueNodeId(workItemNode)).append("_").append(XmlDumper.replaceIllegalChars((String)entry.getKey())).append("Output</dataOutputRefs>").append(EOL).toString());
/*     */     }
/* 139 */     xmlDump.append(new StringBuilder().append("        </outputSet>").append(EOL).toString());
/*     */ 
/* 141 */     xmlDump.append(new StringBuilder().append("      </ioSpecification>").append(EOL).toString());
/*     */ 
/* 143 */     for (Map.Entry entry : workItemNode.getInMappings().entrySet()) {
/* 144 */       xmlDump.append(new StringBuilder().append("      <dataInputAssociation>").append(EOL).toString());
/* 145 */       xmlDump.append(new StringBuilder().append("        <sourceRef>")
/* 146 */         .append(XmlDumper.replaceIllegalChars((String)entry
/* 146 */         .getValue())).append("</sourceRef>").append(EOL).append("        <targetRef>")
/* 147 */         .append(XmlBPMNProcessDumper.getUniqueNodeId(workItemNode))
/* 147 */         .append("_").append(XmlDumper.replaceIllegalChars((String)entry.getKey())).append("Input</targetRef>").append(EOL).toString());
/* 148 */       xmlDump.append(new StringBuilder().append("      </dataInputAssociation>").append(EOL).toString());
/*     */     }
/* 150 */     for (Map.Entry entry : workItemNode.getWork().getParameters().entrySet()) {
/* 151 */       if ((!"ActorId".equals(entry.getKey())) && (entry.getValue() != null)) {
/* 152 */         xmlDump.append(new StringBuilder().append("      <dataInputAssociation>").append(EOL).toString());
/* 153 */         xmlDump.append(new StringBuilder().append("        <targetRef>")
/* 154 */           .append(XmlBPMNProcessDumper.getUniqueNodeId(workItemNode))
/* 154 */           .append("_").append(XmlDumper.replaceIllegalChars((String)entry.getKey())).append("Input</targetRef>").append(EOL).append("        <assignment>").append(EOL).append("          <from xsi:type=\"tFormalExpression\">")
/* 156 */           .append(XmlDumper.replaceIllegalChars(entry
/* 156 */           .getValue().toString())).append("</from>").append(EOL).append("          <to xsi:type=\"tFormalExpression\">")
/* 157 */           .append(XmlBPMNProcessDumper.getUniqueNodeId(workItemNode))
/* 157 */           .append("_").append(XmlDumper.replaceIllegalChars((String)entry.getKey())).append("Input</to>").append(EOL).append("        </assignment>").append(EOL).toString());
/*     */ 
/* 159 */         xmlDump.append(new StringBuilder().append("      </dataInputAssociation>").append(EOL).toString());
/*     */       }
/*     */     }
/* 162 */     for (Map.Entry entry : workItemNode.getOutMappings().entrySet()) {
/* 163 */       xmlDump.append(new StringBuilder().append("      <dataOutputAssociation>").append(EOL).toString());
/* 164 */       xmlDump.append(new StringBuilder().append("        <sourceRef>")
/* 165 */         .append(XmlBPMNProcessDumper.getUniqueNodeId(workItemNode))
/* 165 */         .append("_").append(XmlDumper.replaceIllegalChars((String)entry.getKey())).append("Output</sourceRef>").append(EOL).append("        <targetRef>")
/* 166 */         .append(XmlDumper.replaceIllegalChars((String)entry
/* 166 */         .getValue())).append("</targetRef>").append(EOL).toString());
/* 167 */       xmlDump.append(new StringBuilder().append("      </dataOutputAssociation>").append(EOL).toString());
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.UserTaskHandler
 * JD-Core Version:    0.6.0
 */