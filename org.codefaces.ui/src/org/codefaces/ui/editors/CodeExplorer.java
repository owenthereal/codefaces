package org.codefaces.ui.editors;

import org.codefaces.core.models.RepoFile;
import org.codefaces.ui.CodeFacesUIActivator;
import org.codefaces.ui.codeLanguages.CodeLanguage;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

public class CodeExplorer extends EditorPart {
	public static final String PROP_REPO_RESOURCE = "REPO_RESOURCE";

	public static final String ID = "org.codefaces.ui.editor.codeExplorer";

	private Browser browser;

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
		if (!(input instanceof RepoFileInput)) {
			throw new PartInitException("");
		}
		setSite(site);
		setInput(input);
	}

	public RepoFile getRepoFile() {
		return ((RepoFileInput) getEditorInput()).getFile();
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		browser = new Browser(parent, SWT.NONE);

		updateTitle();
		updateContent(getRepoFile());
	}

	private void updateTitle() {
		IEditorInput input = getEditorInput();
		setPartName(input.getName());
		setTitleToolTip(input.getToolTipText());
	}

	@Override
	public void setFocus() {
		browser.setFocus();
	}

	private void updateContent(RepoFile repoFile) {
		try {
			CodeLanguage language = CodeFacesUIActivator.getDefault()
					.getCodeLanguages().parseFileName(repoFile.getName());
			String langName = language.getName();
			String resourceURL = language.getResource();

			CodeExplorerHTMLTemplate template = new CodeExplorerHTMLTemplate(
					repoFile.getName(), langName, resourceURL,
					repoFile.getContent());

			browser.setText(template.toHTML());
		} catch (Exception e) {
			String errorMsg = "Errors occurs when loading content of the file "
					+ repoFile.getFullPath().toString();
			browser.setText(errorMsg);
			IStatus status = new Status(Status.ERROR,
					CodeFacesUIActivator.PLUGIN_ID, errorMsg, e);
			CodeFacesUIActivator.getDefault().getLog().log(status);
		}
	}
}
