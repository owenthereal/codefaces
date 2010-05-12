package org.codefaces.core;

import java.util.Set;

import org.codefaces.core.models.RepoContainer;
import org.codefaces.core.models.RepoManager;
import org.codefaces.core.services.RepoConnectionException;
import org.codefaces.core.services.RepoResponseException;
import org.eclipse.core.runtime.IAdapterFactory;

public class RepoContainerAdapterFactory implements IAdapterFactory {
	private static final Class<?>[] ADAPTER_LIST = new Class[] { Set.class };

	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adaptableObject instanceof RepoContainer
				&& adapterType == Set.class) {
			try {
				return RepoManager.getInstance().getRepoService().listChildren(
						(RepoContainer) adaptableObject);
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
