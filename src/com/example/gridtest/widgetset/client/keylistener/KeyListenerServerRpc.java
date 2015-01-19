package com.example.gridtest.widgetset.client.keylistener;

import com.vaadin.shared.communication.ServerRpc;

public interface KeyListenerServerRpc extends ServerRpc {
	void charEntered(char code);
}
