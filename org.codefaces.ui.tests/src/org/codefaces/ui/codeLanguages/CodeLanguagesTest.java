package org.codefaces.ui.codeLanguages;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class CodeLanguagesTest {
	private CodeLanguages langs;

	@Before
	public void setUp() {
		langs = new CodeLanguages() {
			@Override
			protected Collection<CodeLanguage> retrieveLangsFromExtensionPoints() {
				Set<CodeLanguage> supportedLangs = new HashSet<CodeLanguage>();
				supportedLangs.add(new CodeLanguage("ruby", "ruby",
						"ruby_resource", "*.rb", "RakeFile"));
				supportedLangs.add(new CodeLanguage("java", "java",
						"java_resource", "*.java"));

				return supportedLangs;
			}
		};
	}

	@Test
	public void test_parseFileName_definiedFilePattern() {
		CodeLanguage lang = langs.parseFileName("test.java");
		assertEquals("java", lang.getName());
	}
	
	@Test
	public void test_parseFileName_undefiniedFilePattern() {
		CodeLanguage lang = langs.parseFileName("test.py");
		assertEquals(CodeLanguage.PLAIN_TEXT, lang);
	}
	
	@Test
	public void test_splitFilePatterns_oneEntry() {
		String[] patterns = langs.splitFilePatterns("*.java");
		assertEquals(1, patterns.length);
		assertEquals("*.java", patterns[0]);
	}
	
	@Test
	public void test_splitFilePatterns_multipleEntries() {
		String[] patterns = langs.splitFilePatterns("*.rb,*.java,*.py");
		assertEquals(3, patterns.length);
	}
}
