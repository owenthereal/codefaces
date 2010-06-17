package org.codefaces.ui.lookandfeel.spica.stacks;

import org.eclipse.rap.ui.interactiondesign.ConfigurableStack;
import org.eclipse.rap.ui.interactiondesign.internal.ConfigurableStackProxy;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.presentations.IPresentablePart;
import org.eclipse.ui.presentations.IStackPresentationSite;
import org.eclipse.ui.presentations.StackDropResult;
import org.eclipse.ui.presentations.StackPresentation;
import org.eclipse.ui.presentations.WorkbenchPresentationFactory;


@SuppressWarnings("restriction")
public class SpicaStackPresentation extends ConfigurableStack{
	StackPresentation stack;

	/**
	 * reuse the default stack presentation
	 */
	//@Override
	public void init(final IStackPresentationSite site, final String stackId,
			final Composite parent, final ConfigurableStackProxy proxy) {
		super.init(site, stackId, parent, proxy);
		WorkbenchPresentationFactory factory = new WorkbenchPresentationFactory();
		stack = factory.createViewPresentation(parent, site);
		init();
	}
	
	//@Override
	public void init() {
		// Do Nothing 
	}

	@Override
	public void addPart(IPresentablePart newPart, Object cookie) {
		stack.addPart(newPart, cookie);
	}

	@Override
	public void dispose() {
		stack.dispose();
	}

	@Override
	public StackDropResult dragOver(Control currentControl, Point location) {
		return stack.dragOver(currentControl, location);
	}

	@Override
	public Control getControl() {
		return stack.getControl();
	}

	@Override
	public Control[] getTabList(IPresentablePart part) {
		return stack.getTabList(part);
	}

	@Override
	public void removePart(IPresentablePart oldPart) {
		stack.removePart(oldPart);
	}

	@Override
	public void selectPart(IPresentablePart toSelect) {
		stack.selectPart(toSelect);
	}

	@Override
	public void setActive(int newState) {
		stack.setActive(newState);
	}

	@Override
	public void setBounds(Rectangle bounds) {
		stack.setBounds(bounds);
	}

	@Override
	public void setState(int state) {
		stack.setState(state);
	}

	@Override
	public void setVisible(boolean isVisible) {
		stack.setVisible(isVisible);	
	}

	@Override
	public void showPaneMenu() {
		stack.showPaneMenu();
	}

	@Override
	public void showSystemMenu() {
		stack.showSystemMenu();
	}


}
