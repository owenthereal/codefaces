package org.codefaces.ui.viewers;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class DefaultRepoResourceLabelProvider  extends LabelProvider {

	private DefaultRepoResourceTreeViewManager manager;

	public DefaultRepoResourceLabelProvider(DefaultRepoResourceTreeViewManager manager) {
		this.manager = manager;
	}

	public String getText(Object obj) {
		return manager.getText(obj);
	}

	public Image getImage(Object obj) {
		return manager.getImage(obj);
	}
}
