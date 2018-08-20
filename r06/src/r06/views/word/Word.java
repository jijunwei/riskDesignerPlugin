package r06.views.word;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IActionFilter;

public class Word implements IAdaptable {

	private String str;
	
	public Word(String str) {
		super();
		this.str = str;
	}
	
	public String toString() {
		return str;
	}

	public Object getAdapter(Class adapter) {
		if (adapter == IActionFilter.class) {
			return WordActionFilter.getSingleton();
		}
		return null;
	}

}

