package r06.views.word;

import org.eclipse.swt.widgets.*;
import org.eclipse.ui.part.ViewPart;

public class LabelView extends ViewPart {
	private Label label;
	public LabelView() {
		super();
	}
	public void setFocus() {
		label.setFocus();
	}
	public void createPartControl(Composite parent) {
		label = new Label(parent, 0);
		label.setText("Hello World");
	}

}

