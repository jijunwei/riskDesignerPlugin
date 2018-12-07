package r06.propertiesWizards.testtable;
import java.text.SimpleDateFormat;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class TableLabelProvider extends LabelProvider  implements ITableLabelProvider {
		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof People){
				People p = (People)element;
				if(columnIndex == 0){
					return p.getId().toString();
				}else if(columnIndex == 1){
					return p.getName();
				}else if (columnIndex ==2){
					return p.getSex();
				}else if (columnIndex == 3){
					return p.getAge().toString();
				}else if (columnIndex == 4){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					return sdf.format(p.getCreateDate());
				}
			}
			return null;
		}
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
	}