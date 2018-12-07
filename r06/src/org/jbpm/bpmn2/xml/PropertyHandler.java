/*     */ package org.jbpm.bpmn2.xml;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.drools.core.xml.BaseAbstractHandler;
/*     */ import org.drools.core.xml.ExtensibleXmlParser;
/*     */ import org.drools.core.xml.Handler;
/*     */ import org.jbpm.bpmn2.core.Association;
/*     */ import org.jbpm.bpmn2.core.Lane;
/*     */ import org.jbpm.bpmn2.core.SequenceFlow;
/*     */ import org.jbpm.compiler.xml.ProcessBuildData;
/*     */ import org.jbpm.process.core.ContextContainer;
/*     */ import org.jbpm.process.core.context.variable.Variable;
/*     */ import org.jbpm.process.core.context.variable.VariableScope;
/*     */ import org.jbpm.workflow.core.Node;
/*     */ import org.jbpm.workflow.core.node.WorkItemNode;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class PropertyHandler extends BaseAbstractHandler
/*     */   implements Handler
/*     */ {
/*     */   public PropertyHandler()
/*     */   {
/*  40 */     initValidParents();
/*  41 */     initValidPeers();
/*  42 */     this.allowNesting = false;
/*     */   }
/*     */ 
/*     */   protected void initValidParents() {
/*  46 */     this.validParents = new HashSet();
/*  47 */     this.validParents.add(ContextContainer.class);
/*  48 */     this.validParents.add(WorkItemNode.class);
/*     */   }
/*     */ 
/*     */   protected void initValidPeers() {
/*  52 */     this.validPeers = new HashSet();
/*  53 */     this.validPeers.add(null);
/*  54 */     this.validPeers.add(Lane.class);
/*  55 */     this.validPeers.add(Variable.class);
/*  56 */     this.validPeers.add(Node.class);
/*  57 */     this.validPeers.add(SequenceFlow.class);
/*  58 */     this.validPeers.add(Lane.class);
/*  59 */     this.validPeers.add(Association.class);
/*     */   }
/*     */ 
/*     */   public Object start(String uri, String localName, Attributes attrs, ExtensibleXmlParser parser)
/*     */     throws SAXException
/*     */   {
/*  66 */     parser.startElementBuilder(localName, attrs);
/*     */ 
/*  68 */     String id = attrs.getValue("id");
/*  69 */     String name = attrs.getValue("name");
/*  70 */     String itemSubjectRef = attrs.getValue("itemSubjectRef");
/*     */ 
/*  72 */     Object parent = parser.getParent();
/*  73 */     if ((parent instanceof ContextContainer)) {
/*  74 */       ContextContainer contextContainer = (ContextContainer)parent;
/*     */ 
/*  76 */       VariableScope variableScope = (VariableScope)contextContainer
/*  76 */         .getDefaultContext("VariableScope");
/*     */ 
/*  77 */       List variables = variableScope.getVariables();
/*  78 */       Variable variable = new Variable();
/*     */ 
/*  80 */       if ((name != null) && (name.length() > 0))
/*  81 */         variable.setName(name);
/*     */       else {
/*  83 */         variable.setName(id);
/*     */       }
/*  85 */       variable.setMetaData("ItemSubjectRef", itemSubjectRef);
/*  86 */       variables.add(variable);
/*     */ 
/*  88 */       ((ProcessBuildData)parser.getData()).setMetaData("Variable", variable);
/*  89 */       return variable;
/*     */     }
/*     */ 
/*  92 */     return new Variable();
/*     */   }
/*     */ 
/*     */   public Object end(String uri, String localName, ExtensibleXmlParser parser) throws SAXException
/*     */   {
/*  97 */     parser.endElementBuilder();
/*  98 */     return parser.getCurrent();
/*     */   }
/*     */ 
/*     */   public Class<?> generateNodeFor() {
/* 102 */     return Variable.class;
/*     */   }
/*     */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.PropertyHandler
 * JD-Core Version:    0.6.0
 */