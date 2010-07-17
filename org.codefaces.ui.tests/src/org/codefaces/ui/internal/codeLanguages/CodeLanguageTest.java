package org.codefaces.ui.internal.codeLanguages;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.codefaces.ui.internal.codeLanguages.CodeLanguage;
import org.junit.Test;

public class CodeLanguageTest {

	@Test
	public void test_matchesFileName_fileExtension() {
		CodeLanguage lang = new CodeLanguage("ruby", "ruby", "ruby_resource",
				"*.rb");

		assertTrue(lang.matchesFileName("tets.rb"));
		assertFalse(lang.matchesFileName("tets.java"));
		assertFalse(lang.matchesFileName("RakeFile"));
	}

	@Test
	public void test_matchesFileName_fileName() {
		CodeLanguage lang = new CodeLanguage("ruby", "ruby", "ruby_resource",
				"RakeFile");

		assertTrue(lang.matchesFileName("RakeFile"));
		assertTrue(lang.matchesFileName("rakefile"));
		assertFalse(lang.matchesFileName("file"));
		assertFalse(lang.matchesFileName("tets.rb"));
	}

}
