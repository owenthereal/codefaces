package org.codefaces.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

/**
 * Ref: http://xantorohara.110mb.com/core-icons/Eclipse-16x16.html
 * 
 * @author kklo
 * 
 */
public class Images {

	private Images() {
	}

	public static final String IMG_BRANCHES = "IMG_BRANCHES";
	public static final String IMG_ERRORS = "IMG_ERRORS";

	public static ImageDescriptor getImageDescriptor(String imageId) {
		return CodeFacesUIActivator.getDefault().getImageRegistry()
				.getDescriptor(imageId);
	}

	public static Image getImage(String imageId) {
		return CodeFacesUIActivator.getDefault().getImageRegistry()
				.get(imageId);
	}
}
