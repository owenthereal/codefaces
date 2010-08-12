package org.codefaces.ui.internal.editors;

import java.io.IOException;

import org.codefaces.core.models.RepoFile;
import org.codefaces.ui.internal.CodeFacesUIActivator;
import org.codefaces.ui.internal.codeLanguages.CodeLanguage;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.rwt.lifecycle.UICallBack;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

public class CodeExplorer extends EditorPart {
	private class UpdateContentJob extends Job {
		private final Display display;

		private final RepoFile file;

		public UpdateContentJob(Display display, RepoFile file) {
			super("");
			this.display = display;
			this.file = file;
			setSystem(true);
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			UICallBack.runNonUIThreadWithFakeContext(display, new Runnable() {
				@Override
				public void run() {
					file.getContent();
				}
			});

			display.asyncExec(new Runnable() {

				@Override
				public void run() {
					setContent(file);
				}
			});

			return Status.OK_STATUS;
		}
	}

	public static final String ID = "org.codefaces.ui.editor.codeExplorer";

	private static final String LOADING_HTML = readLoadingHTML();

	public static final String PROP_REPO_RESOURCE = "REPO_RESOURCE";

	private static String readLoadingHTML() {
		String template = "Loading...";
		try {
			template = CodeFacesUIActivator.getDefault().readFileContent(
					"public/templates/code_editor_loading.html");
		} catch (IOException e) {
			IStatus status = new Status(Status.ERROR,
					CodeFacesUIActivator.PLUGIN_ID,
					"Errors occur when reading code loading html", e);
			CodeFacesUIActivator.getDefault().getLog().log(status);
		}

		return template;
	}

	private Browser browser;

	@Override
	public void createPartControl(Composite parent) {
		browser = new Browser(parent, SWT.NONE);
		setTitle();
		setLoading();
		new UpdateContentJob(browser.getShell().getDisplay(), getRepoFile())
				.schedule();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// do nothing
	}

	@Override
	public void doSaveAs() {
		// do nothing
	}

	public RepoFile getRepoFile() {
		return ((RepoFileInput) getEditorInput()).getFile();
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

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	private void setContent(RepoFile repoFile) {
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
					+ repoFile.getPath().toString();
			browser.setText(errorMsg);
			IStatus status = new Status(Status.ERROR,
					CodeFacesUIActivator.PLUGIN_ID, errorMsg, e);
			CodeFacesUIActivator.getDefault().getLog().log(status);
		}
	}

	@Override
	public void setFocus() {
		browser.setFocus();
	}

	private void setLoading() {
		browser.setText(LOADING_HTML);
	}

	private void setTitle() {
		IEditorInput input = getEditorInput();
		setPartName(input.getName());
		setTitleToolTip(input.getToolTipText());
	}
}
