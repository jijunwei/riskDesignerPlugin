/*     */ package org.jbpm.bpmn2.xml;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.drools.core.xml.BaseAbstractHandler;
/*     */ import org.drools.core.xml.ExtensibleXmlParser;
/*     */ import org.drools.core.xml.Handler;
/*     */ import org.jbpm.bpmn2.core.ItemDefinition;
/*     */ import org.jbpm.bpmn2.core.SequenceFlow;
/*     */ import org.jbpm.compiler.xml.ProcessBuildData;
/*     */ import org.jbpm.process.core.ContextContainer;
/*     */ import org.jbpm.process.core.context.variable.Variable;
/*     */ import org.jbpm.process.core.context.variable.VariableScope;
/*     */ import org.jbpm.process.core.datatype.DataType;
/*     */ import org.jbpm.process.core.datatype.impl.type.BooleanDataType;
/*     */ import org.jbpm.process.core.datatype.impl.type.FloatDataType;
/*     */ import org.jbpm.process.core.datatype.impl.type.IntegerDataType;
/*     */ import org.jbpm.process.core.datatype.impl.type.ObjectDataType;
/*     */ import org.jbpm.process.core.datatype.impl.type.StringDataType;
/*     */ import org.jbpm.workflow.core.Node;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class DataObjectHandler extends BaseAbstractHandler
/*     */   implements Handler
/*     */ {
/*     */   public DataObjectHandler()
/*     */   {
/*  45 */     initValidParents();
/*  46 */     initValidPeers();
/*  47 */     this.allowNesting = false;
/*     */   }
/*     */ 
/*     */   protected void initValidParents() {
/*  51 */     this.validParents = new HashSet();
/*  52 */     this.validParents.add(ContextContainer.class);
/*     */   }
/*     */ 
/*     */   protected void initValidPeers() {
/*  56 */     this.validPeers = new HashSet();
/*  57 */     this.validPeers.add(null);
/*  58 */     this.validPeers.add(Variable.class);
/*  59 */     this.validPeers.add(Node.class);
/*  60 */     this.validPeers.add(SequenceFlow.class);
/*     */   }
/*     */ 
/*     */   public Object start(String uri, String localName, Attributes attrs, ExtensibleXmlParser parser)
/*     */     throws SAXException
/*     */   {
/*  67 */     parser.startElementBuilder(localName, attrs);
/*     */ 
/*  69 */     String id = attrs.getValue("id");
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
/*  79 */       variable.setMetaData("DataObject", "true");
/*  80 */       variable.setName(id);
/*     */ 
/*  82 */       DataType dataType = new ObjectDataType();
/*     */ 
/*  84 */       Map itemDefinitions = (Map)((ProcessBuildData)parser
/*  84 */         .getData()).getMetaData("ItemDefinitions");
/*  85 */       if (itemDefinitions != null) {
/*  86 */         ItemDefinition itemDefinition = (ItemDefinition)itemDefinitions.get(itemSubjectRef);
/*  87 */         if (itemDefinition != null)
/*     */         {
/*  89 */           String structureRef = itemDefinition.getStructureRef();
/*     */ 
/*  91 */           if (("java.lang.Boolean".equals(structureRef)) || ("Boolean".equals(structureRef))) {
/*  92 */             dataType = new BooleanDataType();
/*     */           }
/*  94 */           else if (("java.lang.Integer".equals(structureRef)) || ("Integer".equals(structureRef))) {
/*  95 */             dataType = new IntegerDataType();
/*     */           }
/*  97 */           else if (("java.lang.Float".equals(structureRef)) || ("Float".equals(structureRef))) {
/*  98 */             dataType = new FloatDataType();
/*     */           }
/* 100 */           else if (("java.lang.String".equals(structureRef)) || ("String".equals(structureRef))) {
/* 101 */             dataType = new StringDataType();
/*     */           }
/* 103 */           else if (("java.lang.Object".equals(structureRef)) || ("Object".equals(structureRef)))
/*     */           {
/* 105 */             dataType = new ObjectDataType("java.lang.Object");
/*     */           }
/*     */           else {
/* 108 */             dataType = new ObjectDataType(structureRef, parser.getClassLoader());
/*     */           }
/*     */         }
/*     */       }
/* 112 */       variable.setType(dataType);
/* 113 */       variables.add(variable);
/* 114 */       return variable;
/*     */     }
/*     */ 
/* 117 */     return new Variable();
/*     */   }
/*     */ 
/*     */   public Object end(String uri, String localName, ExtensibleXmlParser parser) throws SAXException
/*     */   {
/* 122 */     parser.endElementBuilder();
/* 123 */     return parser.getCurrent();
/*     */   }
/*     */ 
/*     */   public Class<?> generateNodeFor() {
/* 127 */     return Variable.class;
/*     */   }
/*     */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.DataObjectHandler
 * JD-Core Version:    0.6.0
 */