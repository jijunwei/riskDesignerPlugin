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
/*     */ import org.jbpm.workflow.core.Node;
/*     */ import org.jbpm.workflow.core.NodeContainer;
/*     */ import org.jbpm.workflow.core.node.CompositeContextNode;
/*     */ import org.jbpm.workflow.core.node.CompositeNode;
/*     */ import org.kie.api.definition.process.Process;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class AssociationHandler extends BaseAbstractHandler
/*     */   implements Handler
/*     */ {
/*     */   public AssociationHandler()
/*     */   {
/*  43 */     if ((this.validParents == null) && (this.validPeers == null)) {
/*  44 */       this.validParents = new HashSet();
/*  45 */       this.validParents.add(Process.class);
/*  46 */       this.validParents.add(CompositeContextNode.class);
/*     */ 
/*  48 */       this.validPeers = new HashSet();
/*  49 */       this.validPeers.add(null);
/*  50 */       this.validPeers.add(Lane.class);
/*  51 */       this.validPeers.add(Variable.class);
/*  52 */       this.validPeers.add(Node.class);
/*  53 */       this.validPeers.add(SequenceFlow.class);
/*  54 */       this.validPeers.add(Lane.class);
/*  55 */       this.validPeers.add(Association.class);
/*  56 */       this.allowNesting = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object start(String uri, String localName, Attributes attrs, ExtensibleXmlParser parser)
/*     */     throws SAXException
/*     */   {
/*  63 */     parser.startElementBuilder(localName, attrs);
/*     */ 
/*  65 */     Association association = new Association();
/*  66 */     association.setId(attrs.getValue("id"));
/*  67 */     association.setSourceRef(attrs.getValue("sourceRef"));
/*  68 */     association.setTargetRef(attrs.getValue("targetRef"));
/*  69 */     String direction = attrs.getValue("associationDirection");
/*  70 */     if (direction != null) {
/*  71 */       boolean acceptableDirection = false;
/*  72 */       direction = direction.toLowerCase();
/*  73 */       String[] possibleDirections = { "none", "one", "both" };
/*  74 */       for (String acceptable : possibleDirections) {
/*  75 */         if (acceptable.equals(direction)) {
/*  76 */           acceptableDirection = true;
/*  77 */           break;
/*     */         }
/*     */       }
/*  80 */       if (!acceptableDirection) {
/*  81 */         throw new IllegalArgumentException("Unknown direction '" + direction + "' used in Association " + association.getId());
/*     */       }
/*     */     }
/*  84 */     association.setDirection(direction);
/*     */ 
/*  96 */     List associations = null;
/*  97 */     NodeContainer nodeContainer = (NodeContainer)parser.getParent();
/*  98 */     if ((nodeContainer instanceof Process)) {
/*  99 */       RuleFlowProcess process = (RuleFlowProcess)nodeContainer;
/* 100 */       associations = (List)process.getMetaData("BPMN.Associations");
/* 101 */       if (associations == null) {
/* 102 */         associations = new ArrayList();
/* 103 */         process.setMetaData("BPMN.Associations", associations);
/*     */       }
/* 105 */     } else if ((nodeContainer instanceof CompositeNode)) {
/* 106 */       CompositeContextNode compositeNode = (CompositeContextNode)nodeContainer;
/* 107 */       associations = (List)compositeNode.getMetaData("BPMN.Associations");
/* 108 */       if (associations == null) {
/* 109 */         associations = new ArrayList();
/* 110 */         compositeNode.setMetaData("BPMN.Associations", associations);
/*     */       }
/*     */     }
/* 113 */     associations.add(association);
/*     */ 
/* 115 */     return association;
/*     */   }
/*     */ 
/*     */   public Object end(String uri, String localName, ExtensibleXmlParser parser) throws SAXException
/*     */   {
/* 120 */     parser.endElementBuilder();
/* 121 */     return parser.getCurrent();
/*     */   }
/*     */ 
/*     */   public Class<?> generateNodeFor() {
/* 125 */     return Association.class;
/*     */   }
/*     */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.AssociationHandler
 * JD-Core Version:    0.6.0
 */