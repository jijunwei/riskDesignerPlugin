/*     */ package org.jbpm.bpmn2.xml;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.drools.core.xml.BaseAbstractHandler;
/*     */ import org.drools.core.xml.ExtensibleXmlParser;
/*     */ import org.drools.core.xml.Handler;
/*     */ import org.jbpm.bpmn2.core.Lane;
/*     */ import org.jbpm.bpmn2.core.SequenceFlow;
/*     */ import org.jbpm.process.core.ValueObject;
/*     */ import org.jbpm.process.core.context.variable.Variable;
/*     */ import org.jbpm.process.core.datatype.DataType;
/*     */ import org.jbpm.process.core.datatype.impl.type.StringDataType;
/*     */ import org.jbpm.ruleflow.core.RuleFlowProcess;
/*     */ import org.jbpm.workflow.core.Node;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class MetaDataHandler extends BaseAbstractHandler
/*     */   implements Handler
/*     */ {
/*     */   public MetaDataHandler()
/*     */   {
/*  40 */     if ((this.validParents == null) && (this.validPeers == null)) {
/*  41 */       this.validParents = new HashSet();
/*  42 */       this.validParents.add(Node.class);
/*  43 */       this.validParents.add(RuleFlowProcess.class);
/*  44 */       this.validParents.add(Variable.class);
/*  45 */       this.validParents.add(SequenceFlow.class);
/*  46 */       this.validParents.add(Lane.class);
/*     */ 
/*  48 */       this.validPeers = new HashSet();
/*  49 */       this.validPeers.add(null);
/*     */ 
/*  51 */       this.allowNesting = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object start(String uri, String localName, Attributes attrs, ExtensibleXmlParser parser)
/*     */     throws SAXException
/*     */   {
/*  59 */     parser.startElementBuilder(localName, attrs);
/*     */ 
/*  61 */     Object parent = parser.getParent();
/*  62 */     String name = attrs.getValue("name");
/*  63 */     emptyAttributeCheck(localName, "name", name, parser);
/*  64 */     return new MetaDataWrapper(parent, name);
/*     */   }
/*     */ 
/*     */   public Object end(String uri, String localName, ExtensibleXmlParser parser)
/*     */     throws SAXException
/*     */   {
/*  70 */     parser.endElementBuilder();
/*  71 */     return null;
/*     */   }
/*     */ 
/*     */   public Class generateNodeFor() {
/*  75 */     return MetaDataWrapper.class;
/*     */   }
/*     */   public class MetaDataWrapper implements ValueObject { private Object parent;
/*     */     private String name;
/*     */ 
/*  82 */     public MetaDataWrapper(Object parent, String name) { this.parent = parent;
/*  83 */       this.name = name; }
/*     */ 
/*     */     public Object getValue() {
/*  86 */       return getMetaData().get(this.name);
/*     */     }
/*     */     public void setValue(Object value) {
/*  89 */       getMetaData().put(this.name, value);
/*     */     }
/*     */     public Map<String, Object> getMetaData() {
/*  92 */       if ((this.parent instanceof Node))
/*  93 */         return ((Node)this.parent).getMetaData();
/*  94 */       if ((this.parent instanceof RuleFlowProcess))
/*  95 */         return ((RuleFlowProcess)this.parent).getMetaData();
/*  96 */       if ((this.parent instanceof Variable))
/*  97 */         return ((Variable)this.parent).getMetaData();
/*  98 */       if ((this.parent instanceof SequenceFlow))
/*  99 */         return ((SequenceFlow)this.parent).getMetaData();
/* 100 */       if ((this.parent instanceof Lane)) {
/* 101 */         return ((Lane)this.parent).getMetaData();
/*     */       }
/* 103 */       throw new IllegalArgumentException("Unknown parent " + this.parent);
/*     */     }
/*     */ 
/*     */     public DataType getType() {
/* 107 */       return new StringDataType();
/*     */     }
/*     */ 
/*     */     public void setType(DataType type)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.MetaDataHandler
 * JD-Core Version:    0.6.0
 */