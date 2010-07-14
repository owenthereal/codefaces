package org.codefaces.core.connectors;


public class SCMConnectorDescriber {
	private String id;

	private String kind;

	private SCMConnector connector;

	public SCMConnectorDescriber(String id, String kind, SCMConnector connector) {
		this.id = id;
		this.kind = kind;
		this.connector = connector;
	}

	public String getId() {
		return id;
	}

	public String getKind() {
		return kind;
	}

	public SCMConnector getConnector() {
		return connector;
	}
}
