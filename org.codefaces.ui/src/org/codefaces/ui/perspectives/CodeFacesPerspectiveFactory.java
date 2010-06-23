package org.codefaces.ui.perspectives;

import org.codefaces.ui.views.CodeExplorerViewPart;
import org.codefaces.ui.views.ProjectExplorerViewPart;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class CodeFacesPerspectiveFactory implements IPerspectiveFactory {
	public static final String ID = "org.codefaces.ui.perspective.codefaces";

	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);

		layout.addStandaloneView(ProjectExplorerViewPart.ID, true,
				IPageLayout.LEFT, 0.25f,
				editorArea);

		IFolderLayout rightFolderLayout = layout.createFolder("right",
				IPageLayout.RIGHT, IPageLayout.DEFAULT_VIEW_RATIO, editorArea);
		rightFolderLayout.addPlaceholder(CodeExplorerViewPart.ID);
	}
}
