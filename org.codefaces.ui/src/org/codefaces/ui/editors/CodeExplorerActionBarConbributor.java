package org.codefaces.ui.editors;

import org.codefaces.ui.StatusManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.EditorActionBarContributor;

public class CodeExplorerActionBarConbributor extends
		EditorActionBarContributor {

	private StatusManager statusManager;

	public CodeExplorerActionBarConbributor() {
		statusManager = new StatusManager();
	}

	@Override
	public void setActiveEditor(IEditorPart targetEditor) {
		if (targetEditor instanceof CodeExplorer) {
			IStatusLineManager statusLineManager = targetEditor.getEditorSite()
					.getActionBars().getStatusLineManager();
			statusManager.showStatusMessage(statusLineManager,
					((CodeExplorer) targetEditor).getRepoFile());
		}

	}
}
