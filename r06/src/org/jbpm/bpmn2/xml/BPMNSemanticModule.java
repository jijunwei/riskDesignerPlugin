/*    */ package org.jbpm.bpmn2.xml;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.drools.core.xml.DefaultSemanticModule;
/*    */ import org.jbpm.workflow.core.node.ActionNode;
/*    */ import org.jbpm.workflow.core.node.CatchLinkNode;
/*    */ import org.jbpm.workflow.core.node.CompositeContextNode;
/*    */ import org.jbpm.workflow.core.node.EndNode;
/*    */ import org.jbpm.workflow.core.node.EventNode;
/*    */ import org.jbpm.workflow.core.node.FaultNode;
/*    */ import org.jbpm.workflow.core.node.ForEachNode;
/*    */ import org.jbpm.workflow.core.node.Join;
/*    */ import org.jbpm.workflow.core.node.Split;
/*    */ import org.jbpm.workflow.core.node.StateNode;
/*    */ import org.jbpm.workflow.core.node.ThrowLinkNode;
/*    */ import org.jbpm.workflow.core.node.TimerNode;
/*    */ import org.jbpm.workflow.core.node.WorkItemNode;
/*    */ 
/*    */ public class BPMNSemanticModule extends DefaultSemanticModule
/*    */ {
/*    */   public static final String BPMN2_URI = "http://www.omg.org/spec/BPMN/20100524/MODEL";
/*    */ 
/*    */   public BPMNSemanticModule()
/*    */   {
/* 39 */     super("http://www.omg.org/spec/BPMN/20100524/MODEL");
/*    */ 
/* 41 */     addHandler("definitions", new DefinitionsHandler());
/* 42 */     addHandler("import", new Bpmn2ImportHandler());
/* 43 */     addHandler("process", new ProcessHandler());
/*    */ 
/* 45 */     addHandler("property", new PropertyHandler());
/* 46 */     addHandler("lane", new LaneHandler());
/*    */ 
/* 48 */     addHandler("startEvent", new StartEventHandler());
/* 49 */     addHandler("endEvent", new EndEventHandler());
/* 50 */     addHandler("exclusiveGateway", new ExclusiveGatewayHandler());
/* 51 */     addHandler("inclusiveGateway", new InclusiveGatewayHandler());
/* 52 */     addHandler("parallelGateway", new ParallelGatewayHandler());
/* 53 */     addHandler("eventBasedGateway", new EventBasedGatewayHandler());
/* 54 */     addHandler("complexGateway", new ComplexGatewayHandler());
/* 55 */     addHandler("scriptTask", new ScriptTaskHandler());
/* 56 */     addHandler("task", new TaskHandler());
/* 57 */     addHandler("userTask", new UserTaskHandler());
/* 58 */     addHandler("manualTask", new ManualTaskHandler());
/* 59 */     addHandler("serviceTask", new ServiceTaskHandler());
/* 60 */     addHandler("sendTask", new SendTaskHandler());
/* 61 */     addHandler("receiveTask", new ReceiveTaskHandler());
/* 62 */     addHandler("businessRuleTask", new BusinessRuleTaskHandler());
/* 63 */     addHandler("callActivity", new CallActivityHandler());
/* 64 */     addHandler("subProcess", new SubProcessHandler());
/* 65 */     addHandler("adHocSubProcess", new AdHocSubProcessHandler());
/* 66 */     addHandler("intermediateThrowEvent", new IntermediateThrowEventHandler());
/* 67 */     addHandler("intermediateCatchEvent", new IntermediateCatchEventHandler());
/* 68 */     addHandler("boundaryEvent", new BoundaryEventHandler());
/* 69 */     addHandler("dataObject", new DataObjectHandler());
/* 70 */     addHandler("transaction", new TransactionHandler());
/*    */ 
/* 72 */     addHandler("sequenceFlow", new SequenceFlowHandler());
/*    */ 
/* 74 */     addHandler("itemDefinition", new ItemDefinitionHandler());
/* 75 */     addHandler("message", new MessageHandler());
/* 76 */     addHandler("signal", new SignalHandler());
/* 77 */     addHandler("interface", new InterfaceHandler());
/* 78 */     addHandler("operation", new OperationHandler());
/* 79 */     addHandler("inMessageRef", new InMessageRefHandler());
/* 80 */     addHandler("escalation", new EscalationHandler());
/* 81 */     addHandler("error", new ErrorHandler());
/* 82 */     addHandler("dataStore", new DataStoreHandler());
/* 83 */     addHandler("association", new AssociationHandler());
/* 84 */     addHandler("documentation", new DocumentationHandler());
/*    */ 
/* 86 */     this.handlersByClass.put(Split.class, new SplitHandler());
/* 87 */     this.handlersByClass.put(Join.class, new JoinHandler());
/* 88 */     this.handlersByClass.put(EventNode.class, new EventNodeHandler());
/* 89 */     this.handlersByClass.put(TimerNode.class, new TimerNodeHandler());
/* 90 */     this.handlersByClass.put(EndNode.class, new EndNodeHandler());
/* 91 */     this.handlersByClass.put(FaultNode.class, new FaultNodeHandler());
/* 92 */     this.handlersByClass.put(WorkItemNode.class, new WorkItemNodeHandler());
/* 93 */     this.handlersByClass.put(ActionNode.class, new ActionNodeHandler());
/* 94 */     this.handlersByClass.put(StateNode.class, new StateNodeHandler());
/* 95 */     this.handlersByClass.put(CompositeContextNode.class, new CompositeContextNodeHandler());
/* 96 */     this.handlersByClass.put(ForEachNode.class, new ForEachNodeHandler());
/* 97 */     this.handlersByClass.put(ThrowLinkNode.class, new ThrowLinkNodeHandler());
/* 98 */     this.handlersByClass.put(CatchLinkNode.class, new CatchLinkNodeHandler());
/*    */   }
/*    */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xml.BPMNSemanticModule
 * JD-Core Version:    0.6.0
 */