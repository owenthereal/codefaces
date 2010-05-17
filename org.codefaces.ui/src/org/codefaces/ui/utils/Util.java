package org.codefaces.ui.utils;

import org.codefaces.ui.CodeFacesUIActivator;
import org.eclipse.jface.resource.ImageDescriptor;


public class Util {
	
	/**
	 * @return an image descriptor from the image registory 
	 * @param img_id an image id
	 */
	public static ImageDescriptor getImageDescriptor(String img_id){
		return CodeFacesUIActivator.getDefault().getImageRegistry().getDescriptor(img_id);
	}
}
