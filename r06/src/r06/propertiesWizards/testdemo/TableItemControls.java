package r06.propertiesWizards.testdemo;

import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.widgets.Text;

public class TableItemControls{
	   Text text;
	   //CCombo combo;
	   Text text1;
	   //TableEditor tableeditor;
	   Text text2;
	   TableEditor tableeditor1;
	  
	   public TableItemControls(Text text, Text text1,
			   Text text2, TableEditor tableeditor1) {
       //	    super();
	    this.text = text;
	    //this.combo = combo;
	    this.text1=text1;
	    //this.tableeditor = tableeditor;
	    this.text2=text2;
	    this.tableeditor1 = tableeditor1;
	   }
	   public void dispose()
	   {
	    text.dispose();
	    text1.dispose();
	    text2.dispose();
	    //tableeditor.dispose();
	    tableeditor1.dispose();
	   }
	}
	
