package org.codefaces.core;

import org.codefaces.core.models.RepoFile;
import org.codefaces.core.models.RepoFileLite;
import org.codefaces.core.models.RepoManager;
import org.eclipse.core.runtime.IAdapterFactory;

public class RepoFileAdapterFactory implements IAdapterFactory {
	private static final Class<?>[] ADAPTER_LIST = new Class[] { RepoFileLite.class };

	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adaptableObject instanceof RepoFileLite
				&& adapterType == RepoFile.class) {
			return RepoManager.getInstance().getRepoService().getRepoFile(
					(RepoFileLite) adaptableObject);
		}

		return null;
	}

	@Override
	public Class[] getAdapterList() {
		return ADAPTER_LIST;
	}
}
