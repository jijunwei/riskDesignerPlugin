package r06.propertiesWizards.testtable;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

public class TestEditorableTableViewer {
	private static Table table;
	/**
	 * Launch the application
	 * @param args
	 */
	public static void main(String[] args) {
		final Display display = Display.getDefault();
		final Shell shell = new Shell();
		shell.setSize(500, 375);
		shell.setText("Editortable Application");
		//
		final TableViewer tableViewer = new TableViewer(shell, SWT.CHECK|SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER|SWT.V_SCROLL|SWT.H_SCROLL);
		
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setBounds(97, 79, 373, 154);

		final TableColumn newColumnTableColumn = new TableColumn(table, SWT.NONE);
		newColumnTableColumn.setWidth(39);
		newColumnTableColumn.setText("ID");
		/*//加入事件监听器
		newColumnTableColumn.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?Sorter.ID_ASC:Sorter.ID_DESC);
				asc = !asc;
			}
		});*/

		final TableColumn newColumnTableColumn_1 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_1.setWidth(85);
		newColumnTableColumn_1.setText("姓名");
//		加入事件监听器
		/*newColumnTableColumn_1.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?Sorter.NAME_ASC:Sorter.NAME_DESC);
				asc = !asc;
			}
		});*/
		
		final TableColumn newColumnTableColumn_2 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_2.setWidth(41);
		newColumnTableColumn_2.setText("性别");
//		加入事件监听器
		/*newColumnTableColumn_2.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?Sorter.SEX_ASC:Sorter.SEX_DESC);
				asc = !asc;
			}
		});*/
		
		final TableColumn newColumnTableColumn_3 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_3.setWidth(43);
		newColumnTableColumn_3.setText("年龄");
//		加入事件监听器
		/*newColumnTableColumn_3.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?Sorter.AGE_ASC:Sorter.AGE_DESC);
				asc = !asc;
			}
		});*/
		
		final TableColumn newColumnTableColumn_4 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_4.setWidth(126);
		newColumnTableColumn_4.setText("创建日期");
//		加入事件监听器
		/*newColumnTableColumn_4.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?Sorter.CREATE_DATE_ASC:Sorter.CREATE_DATE_DESC);
				asc = !asc;
			}
		});*/
		
		tableViewer.setContentProvider(new ContentProvider());
		tableViewer.setLabelProvider(new TableLabelProvider());
		tableViewer.setInput(People.getPeople());
		
		tableViewer.setColumnProperties(new String[]{"id","name","sex","age","createDate"});
		CellEditor[] cellEditor = new CellEditor[5];
		cellEditor[0] = null;
		cellEditor[1] = new ComboBoxCellEditor(tableViewer.getTable(),MyCellModifier.NAMES,SWT.READ_ONLY);
		cellEditor[2] = new CheckboxCellEditor(tableViewer.getTable());
		cellEditor[3] = new TextCellEditor(tableViewer.getTable());
		cellEditor[4] = null;
		tableViewer.setCellEditors(cellEditor);
		ICellModifier modifier = new MyCellModifier(tableViewer);
		tableViewer.setCellModifier(modifier);
		Text text = (Text)cellEditor[3].getControl();
		text.addVerifyListener(new VerifyListener(){
			public void verifyText(VerifyEvent e){
				String inStr = e.text;
				if (inStr.length() > 0){
					try{
						Integer.parseInt(inStr);
						e.doit = true;
					}catch(Exception ep){
						e.doit = false;
					}
				}
			}
		});
		
		shell.open();
		shell.setLayout(new FillLayout());
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}
}