/*     */ package org.jbpm.bpmn2.xpath;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.drools.compiler.lang.descr.ActionDescr;
/*     */ import org.drools.compiler.rule.builder.PackageBuildContext;
/*     */ import org.jbpm.process.builder.ActionBuilder;
/*     */ import org.jbpm.process.core.ContextResolver;
/*     */ import org.jbpm.workflow.core.DroolsAction;
/*     */ import org.mvel2.Macro;
/*     */ import org.mvel2.MacroProcessor;
/*     */ 
/*     */ public class XPATHActionBuilder
/*     */   implements ActionBuilder
/*     */ {
/*  35 */   private static final Map macros = new HashMap(5);
/*     */ 
/*     */   public void build(PackageBuildContext context, DroolsAction action, ActionDescr actionDescr, ContextResolver contextResolver)
/*     */   {
/*  76 */     String text = processMacros(actionDescr.getText());
/*     */   }
/*     */ 
/*     */   public static String processMacros(String consequence)
/*     */   {
/* 137 */     MacroProcessor macroProcessor = new MacroProcessor();
/* 138 */     macroProcessor.setMacros(macros);
/* 139 */     return macroProcessor.parse(delimitExpressions(consequence));
/*     */   }
/*     */ 
/*     */   public static String delimitExpressions(String s)
/*     */   {
/* 151 */     StringBuilder result = new StringBuilder();
/* 152 */     char[] cs = s.toCharArray();
/* 153 */     int brace = 0;
/* 154 */     int sqre = 0;
/* 155 */     int crly = 0;
/* 156 */     char lastNonWhite = ';';
/* 157 */     for (int i = 0; i < cs.length; i++) {
/* 158 */       char c = cs[i];
/* 159 */       switch (c) {
/*     */       case '(':
/* 161 */         brace++;
/* 162 */         break;
/*     */       case '{':
/* 164 */         crly++;
/* 165 */         break;
/*     */       case '[':
/* 167 */         sqre++;
/* 168 */         break;
/*     */       case ')':
/* 170 */         brace--;
/* 171 */         break;
/*     */       case '}':
/* 173 */         crly--;
/* 174 */         break;
/*     */       case ']':
/* 176 */         sqre--;
/* 177 */         break;
/*     */       }
/*     */ 
/* 181 */       if ((brace == 0) && (sqre == 0) && (crly == 0) && ((c == '\n') || (c == '\r'))) {
/* 182 */         if (lastNonWhite != ';') {
/* 183 */           result.append(';');
/* 184 */           lastNonWhite = ';';
/*     */         }
/* 186 */       } else if (!Character.isWhitespace(c)) {
/* 187 */         lastNonWhite = c;
/*     */       }
/* 189 */       result.append(c);
/*     */     }
/*     */ 
/* 192 */     return result.toString();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  37 */     macros.put("insert", new Macro()
/*     */     {
/*     */       public String doMacro() {
/*  40 */         return "kcontext.getKnowledgeRuntime().insert";
/*     */       }
/*     */     });
/*     */   }
/*     */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\configuration\org.eclipse.osgi\1104\0\.cp\lib\jbpm-bpmn2.jar
 * Qualified Name:     org.jbpm.bpmn2.xpath.XPATHActionBuilder
 * JD-Core Version:    0.6.0
 */