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
/*     */ import org.jbpm.bpmn2.core.Lane;
/*     */ import org.jbpm.bpmn2.core.SequenceFlow;
/*     */ import org.jbpm.process.core.context.variable.Variable;
/*     */ import org.jbpm.ruleflow.core.RuleFlowProcess;
/*     */ import org.kie.api.definition.process.WorkflowProcess;
/*     */ import org.w3c.dom.Element;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class LaneHandler extends BaseAbstractHandler
/*     */   implements Handler
/*     */ {
/*     */   public static final String LANES = "BPMN.Lanes";
/*     */ 
/*     */   public LaneHandler()
/*     */   {
/*  43 */     if ((this.validParents == null) && (this.validPeers == null)) {
/*  44 */       this.validParents = new HashSet();
/*  45 */       this.validParents.add(RuleFlowProcess.class);
/*     */ 
/*  47 */       this.validPeers = new HashSet();
/*  48 */       this.validPeers.add(null);
/*  49 */       this.validPeers.add(Lane.class);
/*  50 */       this.validPeers.add(Variable.class);
/*  51 */       this.validPeers.add(org.jbpm.workflow.core.Node.class);
/*  52 */       this.validPeers.add(SequenceFlow.class);
/*  53 */       this.validPeers.add(Lane.class);
/*  54 */       this.validPeers.add(Association.class);
/*     */ 
/*  56 */       this.allowNesting = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object start(String uri, String localName, Attributes attrs, ExtensibleXmlParser parser)
/*     */     throws SAXException
/*     */   {
/*  64 */     parser.startElementBuilder(localName, attrs);
/*     */ 
/*  66 */     String id = attrs.getValue("id");
/*  67 */     String name = attrs.getValue("name");
/*     */ 
/*  69 */     WorkflowProcess process = (WorkflowProcess)parser.getParent();
/*     */ 
/*  72 */     List lanes = (List)((RuleFlowProcess)process)
/*  72 */       .getMetaData("BPMN.Lanes");
/*     */ 
/*  73 */     if (lanes == null) {
/*  74 */       lanes = new ArrayList();
/*  75 */       ((RuleFlowProcess)process).setMetaData("BPMN.Lanes", lanes);
/*     */     }
/*  77 */     Lane lane = new Lane(id);
/*  78 */     lane.setName(name);
/*  79 */     lanes.add(lane);
/*  80 */     return lane;
/*     */   }
/*     */ 
/*     */   public Object end(String uri, String localName, ExtensibleXmlParser parser) throws SAXException
/*     */   {
/*  85 */     Element element = parser.endElementBuilder();
/*  86 */     Lane lane = (Lane)parser.getCurrent();
/*     */ 
/*  88 */     org.w3c.dom.Node xmlNode = element.getFirstChild();
/*  89 */     while (xmlNode != null) {
/*  90 */       String nodeName = xmlNode.getNodeName();
/*  91 */       if ("flowNodeRef".equals(nodeName)) {
/*  92 */         String flowElementRef = xmlNode.getTextContent();
/*  93 */         lane.addFlowElement(flowElementRef);
/*     */       }
/*  95 */       xmlNode = xmlNode.getNextSibling();
/*     */     }
/*  97 */     return lane;
/*     */   }
/*     */ 
/*     */   public Class<?> generateNodeFor() {
/* 101 */     return Lane.class;
/*     */   }
/*     */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.LaneHandler
 * JD-Core Version:    0.6.0
 */