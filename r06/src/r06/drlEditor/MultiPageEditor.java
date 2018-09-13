package r06.drlEditor;


import java.io.StringWriter;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;

/**
 * An example showing how to create a multi-page editor.
 * This example has 3 pages:
 * <ul>
 * <li>page 0 contains a nested text editor.
 * <li>page 1 allows you to change the font used in page 2
 * <li>page 2 shows the words in page 0 in sorted order
 * </ul>
 */
public class MultiPageEditor extends MultiPageEditorPart implements IResourceChangeListener{

	/** The text editor used in page 0. */
	private JavaEditor editor;

	/** The font chosen in page 1. */
	private Font font;

	/** The text widget used in page 2. */
	private StyledText text;
	/**
	 * Creates a multi-page editor example.
	 */
	public MultiPageEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}
	/**
	 * Creates page 0 of the multi-page editor,
	 * which contains a text editor.
	 */
	void createPage0() {
		try {
			editor = new JavaEditor();
			int index = addPage(editor, getEditorInput());
			setPageText(index, editor.getTitle());
		} catch (PartInitException e) {
			ErrorDialog.openError(
				getSite().getShell(),
				"Error creating nested text editor",
				null,
				e.getStatus());
		}
	}
	/**
	 * Creates page 1 of the multi-page editor,
	 * which allows you to change the font used in page 2.
	 */
	void createPage1() {

		Composite composite = new Composite(getContainer(), SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		layout.numColumns = 5;
		
		Group group1 = new Group(composite, SWT.SHADOW_ETCHED_OUT);
		group1.setLayout(layout);
		group1.setText("设置drl value：");
		final Label salience = new Label(group1, SWT.LEFT);
		salience.setText("salience");		   
		   // 创建可编辑Text  
        final Text text = new Text(group1, SWT.BORDER);  
        // 创建不可编辑Text组件，用于输出提示信息  
        final Text hintText = new Text(group1, SWT.READ_ONLY);
        final Text oldText1 = new Text(group1, SWT.READ_ONLY);
        final Text oldTextrow1 = new Text(group1, SWT.READ_ONLY);
        oldText1.setVisible(false);
        oldTextrow1.setVisible(false);
        
        //hintText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));  
        // 为text组件加入文本修改事件监听器 
        //salience 作用是用来设置规则执行的优先级，salience 属性的值是一个数字，数字越大执行优先级越高，同时它的值可以是一个负数。默认情况下，规则的 salience 默认值为 0，所以如果我们不手动设置规则的 salience 属性，那么它的执行顺序是随机的。
        text.addModifyListener(new ModifyListener() {  
            public void modifyText(ModifyEvent e) {  
                String string = text.getText();  
                int index = 0;  
                // 遍历文本内容，检查是否为字母或数字  
                while (index < string.length()) {  
                    char ch = string.charAt(index);  
                    if (!Character.isDigit(ch))  
                        break;  
                    index++;  
                }  
                // 若text中输入不合法，则输出错误提示信息  
                if (index != string.length()) {  
                    hintText.setText("invalid input");  
                } else {  
                    hintText.setText("");  
                }  
            }  
        }); 
        
        Label lockOnActive =new Label(group1, SWT.LEFT);
        lockOnActive.setText("lock-on-active");
        final Text oldText2 = new Text(group1, SWT.READ_ONLY);
        final Text oldTextrow2 = new Text(group1, SWT.READ_ONLY);
        oldText2.setVisible(false);
        oldTextrow2.setVisible(false);
        final Combo combo = new Combo(group1, SWT.SIMPLE);
        String[] items = new String[2];
       /* for (int i = 0; i < items.length; i++)
         items[i] = "选项" + i;
        */
        items[0]="true";
        items[1]="false";
        combo.setItems(items);
        // 注册键盘事件
        combo.addKeyListener(new KeyAdapter() {
         public void keyPressed(KeyEvent e) {
          // 如果单击了向左的箭头按键，则选中上一个选项
          if (e.keyCode == SWT.ARROW_LEFT)
           combo.select(combo.getSelectionIndex() - 1);
          // 如果单击了向右的箭头按键，则选中下一个选项
          else if (e.keyCode == SWT.ARROW_RIGHT)
           combo.select(combo.getSelectionIndex() + 1);
         }
        });
        
        String editorText =
				editor.getDocumentProvider().getDocument(editor.getEditorInput()).get();
        
        System.out.println("old contents:\n"+editorText);
    	String[] s=editorText.split("\n");
    	String salienceValue="";
    	String oldSalienceRow="";
    	String oldLockOnActiveRow="";
        String lockOnActiveValue="";
    	for(int i=0;i<s.length;i++){
    		if(s[i].contains("salience")){
    			System.out.println(s[i]);
    			oldSalienceRow=s[i];
    			oldTextrow1.setText(oldSalienceRow);
    			String[] subs=s[i].split(" ");
    			for(int j=0;j<subs.length;j++){
    				if(subs[j].equals("salience")){
    					salienceValue=subs[j+1];
    				}
    			}
    			
    			System.out.println("value:"+salienceValue);
    			text.setText(salienceValue);
    			oldText1.setText(salienceValue);
    			
    		}
    		if(s[i].contains("lock-on-active")){
    			oldLockOnActiveRow=s[i];
    			oldTextrow2.setText(oldLockOnActiveRow);
    			System.out.println(s[i]);
    			String[] subs=s[i].split(" ");
    			for(int j=0;j<subs.length;j++){
    				
    				if(subs[j].equals("lock-on-active")){
    					lockOnActiveValue=subs[j+1];
    				}
    			}
    			System.out.println("value:"+lockOnActiveValue);
    			oldText2.setText(lockOnActiveValue);
    			if(lockOnActiveValue.equals("true")){
    	        	combo.select(0);
    	        }
    	        
    	        if(lockOnActiveValue.equals("false")){
    	        	combo.select(1);
    	        }
    			
    		}
    	}
    	
      
		Button okButton = new Button(composite, SWT.PUSH | SWT.LEFT);
		GridData gd = new GridData(GridData.BEGINNING);
		gd.horizontalSpan = 2;
		okButton.setLayoutData(gd);
		okButton.setText("confirm");
		okButton.setToolTipText("make it effective"); //悬浮提示
		
		okButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				//setFont();
				
				//modify drl content
				
				String editorText =
						editor.getDocumentProvider().getDocument(editor.getEditorInput()).get();
				System.out.println(editorText);
				
				
				String newsalienceValue=text.getText();
				String oldSalienceValue=oldText1.getText();
				String oldSalienceRow=oldTextrow1.getText();
				String oldLockOnActiveRow=oldTextrow2.getText();
				if(!oldSalienceValue.equals(newsalienceValue)){
		    	String newSalienceRow=oldSalienceRow.replace(oldSalienceValue, newsalienceValue);
		    	editorText=editorText.replace(oldSalienceRow, newSalienceRow);
		    	oldText1.setText(newsalienceValue);
		    	oldTextrow1.setText(newSalienceRow);
		    	
				}
		    	String newlockOnActiveValue=combo.getText();
		    	String oldLockOnActiveValue=oldText2.getText();
		    	if(!oldLockOnActiveValue.equals(newlockOnActiveValue)){
		    	String newLockOnActiveRow=oldLockOnActiveRow.replace(oldLockOnActiveValue, newlockOnActiveValue);
		    	editorText=editorText.replace(oldLockOnActiveRow, newLockOnActiveRow);
		    	oldText2.setText(newlockOnActiveValue);
		    	oldTextrow2.setText(newLockOnActiveRow);
		    	}
		    	System.out.println("new Contents:\n"+editorText);
		    	
				//SWT有不同类型的对话框。有些对话框具有特殊的属性。
				MessageBox messageBox = 
				  new MessageBox(getSite().getShell(), SWT.OK| 
						    
						    SWT.ICON_WARNING); 
				
				
				messageBox.setMessage(editorText); 
				if (messageBox.open() == SWT.OK) 
				{ 
				  System.out.println("Ok is pressed."); 
				  //doSave(monitor);
				}
				editor.getDocumentProvider().getDocument(editor.getEditorInput()).set(editorText);
				
				
			}
		});		
		
		int index = addPage(composite);
		setPageText(index, "drlModifyWindow");
	}
	/**
	 * Creates page 2 of the multi-page editor,
	 * which shows the sorted text.
	 */
	void createPage2() {
		Composite composite = new Composite(getContainer(), SWT.NONE);
		FillLayout layout = new FillLayout();
		composite.setLayout(layout);
		text = new StyledText(composite, SWT.H_SCROLL | SWT.V_SCROLL);
		text.setEditable(false);

		int index = addPage(composite);
		setPageText(index, "Preview");
	}
	/**
	 * Creates the pages of the multi-page editor.
	 */
	protected void createPages() {
		createPage0();
		createPage1();
		//createPage2();
	}
	/**
	 * The <code>MultiPageEditorPart</code> implementation of this 
	 * <code>IWorkbenchPart</code> method disposes all nested editors.
	 * Subclasses may extend.
	 */
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}
	/**
	 * Saves the multi-page editor's document.
	 */
	public void doSave(IProgressMonitor monitor) {
		getEditor(0).doSave(monitor);
	}
	/**
	 * Saves the multi-page editor's document as another file.
	 * Also updates the text for page 0's tab, and updates this multi-page editor's input
	 * to correspond to the nested editor's.
	 */
	public void doSaveAs() {
		JavaEditor editor = (JavaEditor) getEditor(0);
		editor.doSaveAs();
		setPageText(0, editor.getTitle());
		setInput(editor.getEditorInput());
	}
	/* (non-Javadoc)
	 * Method declared on IEditorPart
	 */
	public void gotoMarker(IMarker marker) {
		setActivePage(0);
		IDE.gotoMarker(getEditor(0), marker);
	}
	/**
	 * The <code>MultiPageEditorExample</code> implementation of this method
	 * checks that the input is an instance of <code>IFileEditorInput</code>.
	 */
	public void init(IEditorSite site, IEditorInput editorInput)
		throws PartInitException {
		if (!(editorInput instanceof IFileEditorInput))
			throw new PartInitException("Invalid Input: Must be IFileEditorInput");
		super.init(site, editorInput);
	}
	/* (non-Javadoc)
	 * Method declared on IEditorPart.
	 */
	public boolean isSaveAsAllowed() {
		return true;
	}
	/**
	 * Calculates the contents of page 2 when the it is activated.
	 */
	/*protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
		if (newPageIndex == 2) {
			sortWords();
		}
	}*/
	/**
	 * Closes all project files on project close.
	 */
	public void resourceChanged(final IResourceChangeEvent event){
		if(event.getType() == IResourceChangeEvent.PRE_CLOSE){
			Display.getDefault().asyncExec(new Runnable(){
				public void run(){
					IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
					for (int i = 0; i<pages.length; i++){
						if(((FileEditorInput)editor.getEditorInput()).getFile().getProject().equals(event.getResource())){
							IEditorPart editorPart = pages[i].findEditor(editor.getEditorInput());
							pages[i].closeEditor(editorPart,true);
						}
					}
				}            
			});
		}
	}
	/**
	 * Sets the font related data to be applied to the text in page 2.
	 */
	void setFont() {
		FontDialog fontDialog = new FontDialog(getSite().getShell());
		fontDialog.setFontList(text.getFont().getFontData());
		FontData fontData = fontDialog.open();
		if (fontData != null) {
			if (font != null)
				font.dispose();
			font = new Font(text.getDisplay(), fontData);
			text.setFont(font);
		}
	}
	/**
	 * Sorts the words in page 0, and shows them in page 2.
	 */
	void sortWords() {

		String editorText =
			editor.getDocumentProvider().getDocument(editor.getEditorInput()).get();

		StringTokenizer tokenizer =
			new StringTokenizer(editorText, " \t\n\r\f!@#\u0024%^&*()-_=+`~[]{};:'\",.<>/?|\\");
		ArrayList editorWords = new ArrayList();
		while (tokenizer.hasMoreTokens()) {
			editorWords.add(tokenizer.nextToken());
		}

		Collections.sort(editorWords, Collator.getInstance());
		StringWriter displayText = new StringWriter();
		for (int i = 0; i < editorWords.size(); i++) {
			displayText.write(((String) editorWords.get(i)));
			displayText.write(System.getProperty("line.separator"));
		}
		text.setText(displayText.toString());
	}
}
