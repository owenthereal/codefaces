package org.codefaces.ui.supportedCodeLanguages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

public class CodeLanguages {
	private Set<CodeLanguage> langs;
	
	public CodeLanguages() {
		langs = retrieveLangsFromExtensionPoints();
	}

	public CodeLanguage parseFileName(String fileName) {
		for (CodeLanguage lang : langs) {
			if (lang.matchesFileName(fileName)) {
				return lang;
			}
		}

		return CodeLanguage.PLAIN_TEXT;
	}

	public Collection<CodeLanguage> getCodeLanguages() {
		return Collections.unmodifiableSet(langs);
	}

	private Set<CodeLanguage> retrieveLangsFromExtensionPoints() {
		Set<CodeLanguage> langs = new HashSet<CodeLanguage>();

		IConfigurationElement[] extensionPoints = Platform
				.getExtensionRegistry().getConfigurationElementsFor(
						"org.codefaces.ui", "codeLanguages");
		for (IConfigurationElement extensionPoint : extensionPoints) {
			String id = extensionPoint.getAttribute("id");
			String name = extensionPoint.getAttribute("name");
			String filePatterns = extensionPoint.getAttribute("filePatterns");
			String resource = extensionPoint.getAttribute("resource");

			CodeLanguage lang = new CodeLanguage(id, name, resource,
					parseFilePatterns(filePatterns));
			langs.add(lang);
		}

		return langs;
	}

	private String[] parseFilePatterns(String filePatterns) {
		List<String> patterns = new ArrayList<String>();
		for (String pattern : filePatterns.split(",")) {
			patterns.add(pattern.trim());
		}

		return patterns.toArray(new String[patterns.size()]);
	}
}
