package r06.views.word;

import java.util.List;
import org.eclipse.jface.viewers.*;

public class WordContentProvider 
	implements IStructuredContentProvider, WordFile.Listener
{
	WordFile input;
	ListViewer viewer;

	/**
	 * @see IStructuredContentProvider#getElements(Object)
	 */
	public Object[] getElements(Object element) {
		if (element == input)
			return input.elements().toArray();
		return new Object[0];
	}

	/**
	 * @see IContentProvider#dispose()
	 */
	public void dispose() {
		if (input != null)
			input.setListener(null);
		input = null;
		viewer = null;
	}

	/**
	 * @see IContentProvider#inputChanged(Viewer, Object, Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (viewer instanceof ListViewer) {
			this.viewer = (ListViewer)viewer;
		}
		if (newInput instanceof WordFile) {
			input = (WordFile)newInput;
			input.setListener(this);
		}
	}

	/**
	 * @see Listener#added()
	 */
	public void added(Word e) {
		if (viewer != null)
			viewer.add(e);
	}
	
	/**
	 * @see Listener#removed()
	 */
	public void removed(Word e) {
		if (viewer != null) {
			viewer.setSelection(null);
			viewer.remove(e);
		}
	}
}

