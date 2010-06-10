package org.codefaces.ui.views;

import org.codefaces.core.models.RepoResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredViewer;

public class StatusManager implements ISelectionChangedListener {
	private static final String AT = "@";

	private static final String SEPERATOR = " - ";

	private final IStatusLineManager statusLineManager;

	public StatusManager(IStatusLineManager statusLineManager,
			StructuredViewer viewer) {
		this.statusLineManager = statusLineManager;
		viewer.addPostSelectionChangedListener(this);
	}

	private void showStatusMessage(RepoResource resource) {
		if (resource == null) {
			statusLineManager.setMessage(null);
		}

		StringBuilder builder = new StringBuilder();

		IPath fullPath = resource.getFullPath();
		String fileName = fullPath.lastSegment();
		if (fileName != null) {
			builder.append(fileName);
		}

		IPath folderPath = fullPath.removeLastSegments(1);
		String folderName = folderPath.isRoot() ? null : folderPath.toString()
				.substring(1);
		if (folderName != null) {
			builder.append(SEPERATOR);
			builder.append(folderName);
		}

		if (fileName != null || folderName != null) {
			builder.append(SEPERATOR);
		}

		builder.append(resource.getRoot().getBranch().getName());
		builder.append(AT);
		builder.append(resource.getRoot().getBranch().getRepo().getUrl());

		statusLineManager.setMessage(builder.toString());
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		IStructuredSelection structuredSelection = (IStructuredSelection) event
				.getSelection();
		Object selectedObject = structuredSelection.getFirstElement();
		if (selectedObject == null) {
			showStatusMessage(null);
			return;
		}

		showStatusMessage((RepoResource) selectedObject);
	}

}
