/*     */ package org.jbpm.bpmn2.xml;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.drools.core.xml.BaseAbstractHandler;
/*     */ import org.drools.core.xml.ExtensibleXmlParser;
/*     */ import org.drools.core.xml.Handler;
/*     */ import org.jbpm.bpmn2.core.DataStore;
/*     */ import org.jbpm.bpmn2.core.Definitions;
/*     */ import org.jbpm.bpmn2.core.Error;
/*     */ import org.jbpm.bpmn2.core.Escalation;
/*     */ import org.jbpm.bpmn2.core.Interface;
/*     */ import org.jbpm.bpmn2.core.ItemDefinition;
/*     */ import org.jbpm.bpmn2.core.Message;
/*     */ import org.jbpm.bpmn2.core.Signal;
/*     */ import org.jbpm.compiler.xml.ProcessBuildData;
/*     */ import org.jbpm.process.core.datatype.DataType;
/*     */ import org.jbpm.process.core.datatype.impl.type.ObjectDataType;
/*     */ import org.jbpm.ruleflow.core.RuleFlowProcess;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class DataStoreHandler extends BaseAbstractHandler
/*     */   implements Handler
/*     */ {
/*     */   public DataStoreHandler()
/*     */   {
/*  39 */     if ((this.validParents == null) && (this.validPeers == null)) {
/*  40 */       this.validParents = new HashSet();
/*  41 */       this.validParents.add(Definitions.class);
/*     */ 
/*  43 */       this.validPeers = new HashSet();
/*  44 */       this.validPeers.add(null);
/*  45 */       this.validPeers.add(ItemDefinition.class);
/*  46 */       this.validPeers.add(Message.class);
/*  47 */       this.validPeers.add(Interface.class);
/*  48 */       this.validPeers.add(Escalation.class);
/*  49 */       this.validPeers.add(Error.class);
/*  50 */       this.validPeers.add(Signal.class);
/*  51 */       this.validPeers.add(DataStore.class);
/*  52 */       this.validPeers.add(RuleFlowProcess.class);
/*     */ 
/*  54 */       this.allowNesting = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object start(String uri, String localName, Attributes attrs, ExtensibleXmlParser parser)
/*     */     throws SAXException
/*     */   {
/*  61 */     parser.startElementBuilder(localName, attrs);
/*  62 */     DataStore store = new DataStore();
/*  63 */     store.setId(attrs.getValue("id"));
/*  64 */     store.setName(attrs.getValue("name"));
/*  65 */     String itemSubjectRef = attrs.getValue("itemSubjectRef");
/*  66 */     store.setItemSubjectRef(itemSubjectRef);
/*     */ 
/*  68 */     Map itemDefinitions = (Map)((ProcessBuildData)parser
/*  68 */       .getData()).getMetaData("ItemDefinitions");
/*     */ 
/*  71 */     String localItemSubjectRef = itemSubjectRef.substring(itemSubjectRef
/*  72 */       .indexOf(":") + 
/*  72 */       1);
/*  73 */     DataType dataType = new ObjectDataType();
/*  74 */     if (itemDefinitions != null) {
/*  75 */       ItemDefinition itemDefinition = (ItemDefinition)itemDefinitions.get(localItemSubjectRef);
/*  76 */       if (itemDefinition != null) {
/*  77 */         dataType = new ObjectDataType(itemDefinition.getStructureRef(), parser.getClassLoader());
/*     */       }
/*     */     }
/*  80 */     store.setType(dataType);
/*     */ 
/*  82 */     Definitions parent = (Definitions)parser.getParent();
/*  83 */     List dataStores = parent.getDataStores();
/*  84 */     if (dataStores == null) {
/*  85 */       dataStores = new ArrayList();
/*  86 */       parent.setDataStores(dataStores);
/*     */     }
/*  88 */     dataStores.add(store);
/*  89 */     return store;
/*     */   }
/*     */ 
/*     */   public Object end(String uri, String localName, ExtensibleXmlParser parser)
/*     */     throws SAXException
/*     */   {
/*  95 */     parser.endElementBuilder();
/*  96 */     return parser.getCurrent();
/*     */   }
/*     */ 
/*     */   public Class<?> generateNodeFor() {
/* 100 */     return DataStore.class;
/*     */   }
/*     */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.DataStoreHandler
 * JD-Core Version:    0.6.0
 */