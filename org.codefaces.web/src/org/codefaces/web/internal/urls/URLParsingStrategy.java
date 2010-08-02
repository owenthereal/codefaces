package org.codefaces.web.internal.urls;

import org.codefaces.ui.SCMURLConfigurations;

public interface URLParsingStrategy {
	SCMURLConfigurations buildConfigurations(String url);
	
	String getScmKind(); 

	boolean canParse(String url);
}
