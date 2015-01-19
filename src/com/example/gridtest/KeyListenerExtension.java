package com.example.gridtest;

import com.example.gridtest.widgetset.client.keylistener.KeyListenerServerRpc;
import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.AbstractExtension;

@SuppressWarnings("serial")
public class KeyListenerExtension extends AbstractExtension {
	public interface KeyListener {
		void charEntered(char code);
	}
	
	private KeyListener listener;
	
	private KeyListenerServerRpc rpc = new KeyListenerServerRpc() {
		@Override
		public void charEntered(char code) {
			listener.charEntered(code);
		}
	};
	
	public KeyListenerExtension(AbstractClientConnector target, KeyListener listener) {
		registerRpc(rpc);
		extend(target);
		this.listener = listener;
	}

	public KeyListener getListener() {
		return listener;
	}

	public void setListener(KeyListener listener) {
		this.listener = listener;
	}	
}
