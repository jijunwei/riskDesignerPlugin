/*    */ package org.jbpm.bpmn2.xml;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.drools.compiler.compiler.xml.XmlDumper;
/*    */ import org.jbpm.workflow.core.Node;
/*    */ import org.jbpm.workflow.core.impl.DroolsConsequenceAction;
/*    */ import org.jbpm.workflow.core.node.EndNode;
/*    */ import org.xml.sax.Attributes;
/*    */ 
/*    */ public class EndNodeHandler extends AbstractNodeHandler
/*    */ {
/*    */   protected Node createNode(Attributes attrs)
/*    */   {
/* 33 */     throw new IllegalArgumentException("Reading in should be handled by end event handler");
/*    */   }
/*    */ 
/*    */   public Class generateNodeFor()
/*    */   {
/* 38 */     return EndNode.class;
/*    */   }
/*    */ 
/*    */   public void writeNode(Node node, StringBuilder xmlDump, int metaDataType) {
/* 42 */     EndNode endNode = (EndNode)node;
/*    */ 
/* 44 */     String eventType = (String)endNode.getMetaData("EventType");
/* 45 */     String ref = (String)endNode.getMetaData("Ref");
/* 46 */     String variableRef = (String)endNode.getMetaData("Variable");
/*    */ 
/* 49 */     writeNode("endEvent", endNode, xmlDump, metaDataType);
/* 50 */     if (endNode.isTerminate()) {
/* 51 */       xmlDump.append(new StringBuilder().append(">").append(EOL).toString());
/* 52 */       writeExtensionElements(endNode, xmlDump);
/* 53 */       xmlDump.append(new StringBuilder().append("        <terminateEventDefinition ").append(endNode.getScope() == 1 ? "tns:scope=\"process\"" : "").append("/>").append(EOL).toString());
/* 54 */       endNode("endEvent", xmlDump);
/*    */     } else {
/* 56 */       String scope = (String)endNode.getMetaData("customScope");
/* 57 */       List actions = endNode.getActions("onEntry");
/* 58 */       if ((actions != null) && (!actions.isEmpty())) {
/* 59 */         if (actions.size() == 1) {
/* 60 */           DroolsConsequenceAction action = (DroolsConsequenceAction)actions.get(0);
/* 61 */           String s = action.getConsequence();
/* 62 */           if (s.startsWith("org.drools.core.process.instance.impl.WorkItemImpl workItem = new org.drools.core.process.instance.impl.WorkItemImpl();")) {
/* 63 */             xmlDump.append(new StringBuilder().append(">").append(EOL).toString());
/* 64 */             writeExtensionElements(endNode, xmlDump);
/* 65 */             String variable = (String)endNode.getMetaData("MappingVariable");
/* 66 */             if (variable != null) {
/* 67 */               xmlDump.append(new StringBuilder().append("      <dataInput id=\"")
/* 68 */                 .append(XmlBPMNProcessDumper.getUniqueNodeId(endNode))
/* 68 */                 .append("_Input\" />").append(EOL).append("      <dataInputAssociation>").append(EOL).append("        <sourceRef>")
/* 70 */                 .append(XmlDumper.replaceIllegalChars(variable))
/* 70 */                 .append("</sourceRef>").append(EOL).append("        <targetRef>")
/* 71 */                 .append(XmlBPMNProcessDumper.getUniqueNodeId(endNode))
/* 71 */                 .append("_Input</targetRef>").append(EOL).append("      </dataInputAssociation>").append(EOL).append("      <inputSet>").append(EOL).append("        <dataInputRefs>")
/* 74 */                 .append(XmlBPMNProcessDumper.getUniqueNodeId(endNode))
/* 74 */                 .append("_Input</dataInputRefs>").append(EOL).append("      </inputSet>").append(EOL).toString());
/*    */             }
/*    */ 
/* 77 */             xmlDump.append(new StringBuilder().append("      <messageEventDefinition messageRef=\"").append(XmlBPMNProcessDumper.getUniqueNodeId(endNode)).append("_Message\"/>").append(EOL).toString());
/* 78 */             endNode("endEvent", xmlDump);
/* 79 */           } else if ("signal".equals(eventType)) {
/* 80 */             xmlDump.append(new StringBuilder().append(">").append(EOL).toString());
/* 81 */             writeExtensionElements(endNode, xmlDump);
/*    */ 
/* 83 */             if (!s.startsWith("null"))
/*    */             {
/* 85 */               xmlDump.append(new StringBuilder().append("      <dataInput id=\"")
/* 86 */                 .append(XmlBPMNProcessDumper.getUniqueNodeId(endNode))
/* 86 */                 .append("_Input\" />").append(EOL).append("      <dataInputAssociation>").append(EOL).append("        <sourceRef>")
/* 88 */                 .append(XmlDumper.replaceIllegalChars(variableRef))
/* 88 */                 .append("</sourceRef>").append(EOL).append("        <targetRef>")
/* 89 */                 .append(XmlBPMNProcessDumper.getUniqueNodeId(endNode))
/* 89 */                 .append("_Input</targetRef>").append(EOL).append("      </dataInputAssociation>").append(EOL).append("      <inputSet>").append(EOL).append("        <dataInputRefs>")
/* 92 */                 .append(XmlBPMNProcessDumper.getUniqueNodeId(endNode))
/* 92 */                 .append("_Input</dataInputRefs>").append(EOL).append("      </inputSet>").append(EOL).toString());
/*    */             }
/*    */ 
/* 95 */             xmlDump.append(new StringBuilder().append("      <signalEventDefinition signalRef=\"").append(XmlBPMNProcessDumper.replaceIllegalCharsAttribute(ref)).append("\"/>").append(EOL).toString());
/* 96 */             endNode("endEvent", xmlDump);
/* 97 */           } else if (s.startsWith("kcontext.getKnowledgeRuntime().signalEvent(")) {
/* 98 */             xmlDump.append(new StringBuilder().append(">").append(EOL).toString());
/* 99 */             writeExtensionElements(endNode, xmlDump);
/* 100 */             s = s.substring(44);
/* 101 */             String type = s.substring(0, s.indexOf("\""));
/* 102 */             s = s.substring(s.indexOf(",") + 2);
/* 103 */             String variable = null;
/* 104 */             if (!s.startsWith("null")) {
/* 105 */               variable = s.substring(0, s.indexOf(")"));
/* 106 */               xmlDump.append(new StringBuilder().append("      <dataInput id=\"")
/* 107 */                 .append(XmlBPMNProcessDumper.getUniqueNodeId(endNode))
/* 107 */                 .append("_Input\" />").append(EOL).append("      <dataInputAssociation>").append(EOL).append("        <sourceRef>")
/* 109 */                 .append(XmlDumper.replaceIllegalChars(variable))
/* 109 */                 .append("</sourceRef>").append(EOL).append("        <targetRef>")
/* 110 */                 .append(XmlBPMNProcessDumper.getUniqueNodeId(endNode))
/* 110 */                 .append("_Input</targetRef>").append(EOL).append("      </dataInputAssociation>").append(EOL).append("      <inputSet>").append(EOL).append("        <dataInputRefs>")
/* 113 */                 .append(XmlBPMNProcessDumper.getUniqueNodeId(endNode))
/* 113 */                 .append("_Input</dataInputRefs>").append(EOL).append("      </inputSet>").append(EOL).toString());
/*    */             }
/*    */ 
/* 116 */             xmlDump.append(new StringBuilder().append("      <signalEventDefinition signalRef=\"").append(XmlBPMNProcessDumper.replaceIllegalCharsAttribute(type)).append("\"/>").append(EOL).toString());
/* 117 */             endNode("endEvent", xmlDump);
/* 118 */           } else if ((s.startsWith("kcontext.getProcessInstance().signalEvent(")) && ("processInstance".equals(scope))) {
/* 119 */             xmlDump.append(new StringBuilder().append(">").append(EOL).toString());
/* 120 */             writeExtensionElements(endNode, xmlDump);
/* 121 */             s = s.substring(43);
/* 122 */             String type = s.substring(0, s.indexOf("\""));
/* 123 */             s = s.substring(s.indexOf(",") + 2);
/* 124 */             String variable = null;
/* 125 */             if (!s.startsWith("null")) {
/* 126 */               variable = s.substring(0, s.indexOf(")"));
/* 127 */               xmlDump.append(new StringBuilder().append("      <dataInput id=\"")
/* 128 */                 .append(XmlBPMNProcessDumper.getUniqueNodeId(endNode))
/* 128 */                 .append("_Input\" />").append(EOL).append("      <dataInputAssociation>").append(EOL).append("        <sourceRef>")
/* 130 */                 .append(XmlDumper.replaceIllegalChars(variable))
/* 130 */                 .append("</sourceRef>").append(EOL).append("        <targetRef>")
/* 131 */                 .append(XmlBPMNProcessDumper.getUniqueNodeId(endNode))
/* 131 */                 .append("_Input</targetRef>").append(EOL).append("      </dataInputAssociation>").append(EOL).append("      <inputSet>").append(EOL).append("        <dataInputRefs>")
/* 134 */                 .append(XmlBPMNProcessDumper.getUniqueNodeId(endNode))
/* 134 */                 .append("_Input</dataInputRefs>").append(EOL).append("      </inputSet>").append(EOL).toString());
/*    */             }
/*    */ 
/* 137 */             xmlDump.append(new StringBuilder().append("      <signalEventDefinition signalRef=\"").append(XmlBPMNProcessDumper.replaceIllegalCharsAttribute(type)).append("\"/>").append(EOL).toString());
/* 138 */             endNode("endEvent", xmlDump);
/* 139 */           } else if (s.startsWith("kcontext.getProcessInstance().signalEvent(")) {
/* 140 */             xmlDump.append(new StringBuilder().append(">").append(EOL).toString());
/* 141 */             writeExtensionElements(endNode, xmlDump);
/* 142 */             int begin = "kcontext.getProcessInstance().signalEvent(Compensation\", ".length() - 2;
/* 143 */             int end = s.length() - 3;
/* 144 */             String compensationEvent = s.substring(begin, end);
/* 145 */             String activityRef = "";
/* 146 */             if (!compensationEvent.startsWith("implicit:"))
/*    */             {
/* 148 */               activityRef = new StringBuilder().append("activityRef=\"").append(XmlBPMNProcessDumper.replaceIllegalCharsAttribute(activityRef)).append("\" ").toString();
/*    */             }
/* 150 */             xmlDump.append(new StringBuilder().append("      <compensateEventDefinition ").append(activityRef).append("/>").append(EOL).toString());
/* 151 */             endNode("endEvent", xmlDump);
/*    */           } else {
/* 153 */             throw new IllegalArgumentException(new StringBuilder().append("Unknown action ").append(s).toString());
/*    */           }
/*    */         }
/*    */       }
/* 157 */       else endNode(xmlDump);
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.EndNodeHandler
 * JD-Core Version:    0.6.0
 */