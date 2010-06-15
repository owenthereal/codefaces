package org.codefaces.ui.views;

import java.util.concurrent.CopyOnWriteArrayList;

import org.codefaces.core.models.RepoFile;
import org.codefaces.ui.CodeFacesUIActivator;
import org.codefaces.ui.codeLanguages.CodeLanguage;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class CodeExplorerViewPart extends ViewPart {
	public static final String PROP_REPO_RESOURCE = "REPO_RESOURCE";

	public static final String ID = "org.codefaces.ui.view.codeExplorer";

	private Browser browser;

	private RepoFile repoFile;

	private StatusManager statusManager;


	private CopyOnWriteArrayList<IPropertyChangeListener> propertyChangeListeners = new CopyOnWriteArrayList<IPropertyChangeListener>();

	@Override
	public void createPartControl(final Composite parent) {
		browser = new Browser(parent, SWT.NONE);
		statusManager = new StatusManager(getViewSite().getActionBars()
				.getStatusLineManager(), this);
	}

	public StatusManager getStatusManager() {
		return statusManager;
	}

	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		propertyChangeListeners.add(listener);
	}

	@Override
	public void setFocus() {
		browser.setFocus();
	}

	public void setInput(RepoFile repoFile) {
		setPartName(repoFile.getName());

		CodeLanguage language = CodeFacesUIActivator.getDefault()
				.getCodeLanguages().parseFileName(repoFile.getName());
		String langName = language.getName();
		String resourceURL = language.getResource();

		CodeExplorerHTMLTemplate template = new CodeExplorerHTMLTemplate(
				repoFile.getName(), langName, resourceURL, repoFile
						.getContent());

		browser.setText(template.toHTML());

		firePropertyChanged(this.repoFile, repoFile);
		this.repoFile = repoFile;
	}

	private void firePropertyChanged(Object oldValue, Object newValue) {
		PropertyChangeEvent event = new PropertyChangeEvent(this,
				PROP_REPO_RESOURCE, oldValue, newValue);
		for (IPropertyChangeListener listener : propertyChangeListeners) {
			listener.propertyChange(event);
		}
	}
}
