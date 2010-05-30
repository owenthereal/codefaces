package org.eclipse.ui.actions;

import java.util.Arrays;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.IViewDescriptor;

public class ShowViewMenu extends ContributionItem {
 
	private static final String CODEFACES_CATEGORY = "org.codefaces.ui.category.codefaces";
	
	public void fill(Menu menu, int index) {

		IViewDescriptor[] viewDescriptors = PlatformUI.getWorkbench()
				.getViewRegistry().getViews();
		for(IViewDescriptor viewDescriptor: viewDescriptors){
			String[] categories = viewDescriptor.getCategoryPath();
			if (categories != null
					&& Arrays.asList(categories).contains(CODEFACES_CATEGORY)) {
				boolean allowMultiple = viewDescriptor.getAllowMultiple();
				//Filter out the editor view
				if(allowMultiple) { continue; }
				
				final String viewName = viewDescriptor.getLabel();
				final ImageDescriptor imageDescriptor = viewDescriptor.getImageDescriptor();
				final String viewId = viewDescriptor.getId();

				MenuItem menuItem = new MenuItem(menu, SWT.PUSH);
				menuItem.setText(viewName);
				if(imageDescriptor != null){
					menuItem.setImage(imageDescriptor.createImage());
				}
				
				menuItem.addSelectionListener(new SelectionListener(){
					@Override
					public void widgetDefaultSelected(SelectionEvent evt) {}

					@Override
					public void widgetSelected(SelectionEvent evt) {
						IWorkbenchPage activePage = PlatformUI.getWorkbench()
								.getActiveWorkbenchWindow().getActivePage();
						try {
							activePage.showView(viewId);
						} catch (PartInitException e) {
							e.printStackTrace();
						}
						
					}
				});
			}
		}
	}


	public boolean isDynamic(){
		return true;
	}
}
