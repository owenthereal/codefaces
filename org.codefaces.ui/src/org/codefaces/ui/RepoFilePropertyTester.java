package org.codefaces.ui;

import org.apache.commons.lang.StringUtils;
import org.codefaces.core.models.RepoFile;
import org.eclipse.core.expressions.PropertyTester;

public class RepoFilePropertyTester extends PropertyTester {
	private static final String PROP_CAN_OPEN = "canOpen";

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		if (receiver instanceof RepoFile && PROP_CAN_OPEN.equals(property)) {
			return canOpen((RepoFile) receiver);
		}

		return false;
	}

	private boolean canOpen(RepoFile file) {
		return StringUtils.isEmpty(file.getContent());
	}
}
