package org.codefaces.core;

import org.codefaces.core.models.RepoFile;
import org.codefaces.core.models.RepoFileLite;
import org.codefaces.core.models.RepoManager;
import org.codefaces.core.services.RepoConnectionException;
import org.codefaces.core.services.RepoResponseException;
import org.eclipse.core.runtime.IAdapterFactory;

public class RepoFileAdapterFactory implements IAdapterFactory {
	private static final Class<?>[] ADAPTER_LIST = new Class[] { RepoFileLite.class };

	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adapterType != RepoFile.class) {
			return null;
		}

		if (adaptableObject instanceof RepoFile) {
			return (RepoFile) adaptableObject;
		}

		if (adaptableObject instanceof RepoFileLite) {
			try {
				return RepoManager.getInstance().getRepoService().getRepoFile(
						(RepoFileLite) adaptableObject);
			} catch (RepoResponseException e) {
				e.printStackTrace();
			} catch (RepoConnectionException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	@Override
	public Class[] getAdapterList() {
		return ADAPTER_LIST;
	}
}
