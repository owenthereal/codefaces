package org.codefaces.ui.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

/**
 * An editor with a browser showing a web page of a given URL
 */
public class BrowserEditor extends EditorPart {

	private Browser browser;
	private String url;
	
	/**
	 * Create an Browser Editor with an initial url
	 * @param url
	 */
	public BrowserEditor(String url){
		this.url = url;
	}
	
	/**
	 * @return the browser
	 */
	public Browser getBrowser(){
		return browser;
	}
	
	@Override
	public void createPartControl(Composite parent) {
		browser = new Browser(parent, SWT.NONE);
		browser.setUrl(url);
	}

	@Override
	public void setFocus() {
		browser.setFocus();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// do nothing
	}

	@Override
	public void doSaveAs() {
		// do nothing
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
}
