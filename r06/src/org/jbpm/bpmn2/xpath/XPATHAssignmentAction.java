/*     */ package org.jbpm.bpmn2.xpath;
/*     */ 
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.xpath.XPath;
/*     */ import javax.xml.xpath.XPathConstants;
/*     */ import javax.xml.xpath.XPathExpression;
/*     */ import javax.xml.xpath.XPathFactory;
/*     */ import org.drools.core.process.instance.WorkItem;
/*     */ import org.jbpm.process.instance.impl.AssignmentAction;
/*     */ import org.jbpm.workflow.core.node.Assignment;
/*     */ import org.kie.api.runtime.process.ProcessContext;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.Text;
/*     */ 
/*     */ public class XPATHAssignmentAction
/*     */   implements AssignmentAction
/*     */ {
/*     */   private String sourceExpr;
/*     */   private String targetExpr;
/*     */   private Assignment assignment;
/*     */   private boolean isInput;
/*     */ 
/*     */   public XPATHAssignmentAction(Assignment assignment, String sourceExpr, String targetExpr, boolean isInput)
/*     */   {
/*  44 */     this.assignment = assignment;
/*  45 */     this.sourceExpr = sourceExpr;
/*  46 */     this.targetExpr = targetExpr;
/*  47 */     this.isInput = isInput;
/*     */   }
/*     */ 
/*     */   public void execute(WorkItem workItem, ProcessContext context) throws Exception {
/*  51 */     String from = this.assignment.getFrom();
/*  52 */     String to = this.assignment.getTo();
/*     */ 
/*  54 */     XPathFactory factory = XPathFactory.newInstance();
/*  55 */     XPath xpathFrom = factory.newXPath();
/*     */ 
/*  57 */     XPathExpression exprFrom = xpathFrom.compile(from);
/*     */ 
/*  59 */     XPath xpathTo = factory.newXPath();
/*     */ 
/*  61 */     XPathExpression exprTo = xpathTo.compile(to);
/*     */ 
/*  63 */     Object target = null;
/*  64 */     Object source = null;
/*     */ 
/*  66 */     if (this.isInput) {
/*  67 */       source = context.getVariable(this.sourceExpr);
/*  68 */       target = workItem.getParameter(this.targetExpr);
/*     */     } else {
/*  70 */       target = context.getVariable(this.targetExpr);
/*  71 */       source = workItem.getResult(this.sourceExpr);
/*     */     }
/*     */ 
/*  74 */     Object targetElem = null;
/*     */ 
/*  81 */     if (target != null) {
/*  82 */       Node parent = null;
/*  83 */       parent = ((Node)target).getParentNode();
/*     */ 
/*  86 */       targetElem = exprTo.evaluate(parent, XPathConstants.NODE);
/*     */ 
/*  88 */       if (targetElem == null) {
/*  89 */         throw new RuntimeException("Nothing was selected by the to expression " + to + " on " + this.targetExpr);
/*     */       }
/*     */     }
/*  92 */     NodeList nl = null;
/*  93 */     if ((source instanceof Node)) {
/*  94 */       nl = (NodeList)exprFrom.evaluate(source, XPathConstants.NODESET);
/*  95 */     } else if ((source instanceof String)) {
/*  96 */       DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
/*  97 */       Document doc = builder.newDocument();
/*     */ 
/*  99 */       Element temp = doc.createElementNS(null, "temp");
/* 100 */       temp.appendChild(doc.createTextNode((String)source));
/* 101 */       nl = temp.getChildNodes();
/* 102 */     } else if (source == null)
/*     */     {
/* 104 */       throw new RuntimeException("Source value was null for source " + this.sourceExpr);
/*     */     }
/*     */ 
/* 107 */     if (nl.getLength() == 0) {
/* 108 */       throw new RuntimeException("Nothing was selected by the from expression " + from + " on " + this.sourceExpr);
/*     */     }
/* 110 */     for (int i = 0; i < nl.getLength(); i++)
/*     */     {
/* 112 */       if (!(targetElem instanceof Node)) {
/* 113 */         if ((nl.item(i) instanceof Attr)) {
/* 114 */           targetElem = ((Attr)nl.item(i)).getValue();
/* 115 */         } else if ((nl.item(i) instanceof Text)) {
/* 116 */           targetElem = ((Text)nl.item(i)).getWholeText();
/*     */         } else {
/* 118 */           DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
/* 119 */           Document doc = builder.newDocument();
/* 120 */           targetElem = doc.importNode(nl.item(i), true);
/*     */         }
/* 122 */         target = targetElem;
/*     */       } else {
/* 124 */         Node n = ((Node)targetElem).getOwnerDocument().importNode(nl.item(i), true);
/* 125 */         if ((n instanceof Attr))
/* 126 */           ((Element)targetElem).setAttributeNode((Attr)n);
/*     */         else {
/* 128 */           ((Node)targetElem).appendChild(n);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 133 */     if (this.isInput)
/* 134 */       workItem.setParameter(this.targetExpr, target);
/*     */     else
/* 136 */       context.setVariable(this.targetExpr, target);
/*     */   }
/*     */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xpath.XPATHAssignmentAction
 * JD-Core Version:    0.6.0
 */