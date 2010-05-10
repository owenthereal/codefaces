package org.codefaces.core;

import java.util.Set;

import org.codefaces.core.models.RepoContainer;
import org.codefaces.core.services.RepoService;
import org.eclipse.core.runtime.IAdapterFactory;

public class RepoContainerAdapterFactory implements IAdapterFactory {
	private RepoService repoService;

	private static final Class<?>[] ADAPTER_LIST = new Class[] { Set.class };

	public RepoContainerAdapterFactory() {
		repoService = new RepoService();
	}

	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adaptableObject instanceof RepoContainer
				&& adapterType == Set.class) {
			return repoService.listChildren((RepoContainer) adaptableObject);
		}

		return null;
	}

	@Override
	public Class[] getAdapterList() {
		return ADAPTER_LIST;
	}
}
