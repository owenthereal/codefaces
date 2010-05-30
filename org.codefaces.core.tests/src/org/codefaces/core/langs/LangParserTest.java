package org.codefaces.core.langs;

import static org.junit.Assert.assertEquals;

import org.codefaces.core.langs.Lang;
import org.codefaces.core.langs.LangParser;
import org.junit.Test;

public class LangParserTest {
	@Test
	public void test_parse_existedLang() {
		String fileName = "codefaces.java";
		Lang actualLang = LangParser.parse(fileName);

		assertEquals(Lang.LANG_JAVA, actualLang);
	}

	@Test
	public void test_parse_nonExistedLang() {
		String fileName = "codefaces.notExisted";
		Lang actualLang = LangParser.parse(fileName);

		assertEquals(Lang.getDefault(), actualLang);
	}
	
	@Test
	public void test_parse_noFileExtension() {
		String fileName = "RakeFile";
		Lang actualLang = LangParser.parse(fileName);

		assertEquals(Lang.LANG_RUBY, actualLang);
	}
	
	@Test
	public void test_parse_extensionIgnoreCase() {
		String fileName = "codefaces.HTML";
		Lang actualLang = LangParser.parse(fileName);

		assertEquals(Lang.LANG_HTML, actualLang);
	}
}
