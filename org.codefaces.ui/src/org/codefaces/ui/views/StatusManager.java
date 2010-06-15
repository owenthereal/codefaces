package org.codefaces.ui.views;

import org.codefaces.core.models.RepoResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredViewer;

public class StatusManager implements ISelectionChangedListener,
		IPropertyChangeListener {
	private static final String AT = "@";

	private static final String SEPERATOR = " - ";

	private final IStatusLineManager statusLineManager;

	public StatusManager(IStatusLineManager statusLineManager,
			StructuredViewer viewer) {
		this.statusLineManager = statusLineManager;
		viewer.addPostSelectionChangedListener(this);
	}

	public StatusManager(IStatusLineManager statusLineManager,
			CodeExplorerViewPart part) {
		this.statusLineManager = statusLineManager;
		part.addPropertyChangeListener(this);
	}

	public void showStatusMessage(IPath fullPath, String repoUrl,
			String branchName) {
		if (fullPath == null) {
			statusLineManager.setMessage(null);
			return;
		}

		StringBuilder builder = new StringBuilder();

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

		builder.append(branchName);
		builder.append(AT);
		builder.append(repoUrl);

		statusLineManager.setMessage(builder.toString());
	}

	public void showStatusMessage(RepoResource resource) {
		if (resource == null) {
			showStatusMessage(null, null, null);
		} else {
			showStatusMessage(resource.getFullPath(), resource.getRoot()
					.getBranch().getRepo().getUrl(), resource.getRoot()
					.getBranch().getName());
		}
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

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getProperty() == CodeExplorerViewPart.PROP_REPO_RESOURCE) {
			showStatusMessage((RepoResource) event.getNewValue());
		}
	}
}
