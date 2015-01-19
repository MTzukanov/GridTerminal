package com.example.gridtest.widgetset.client.keylistener;

import com.example.gridtest.KeyListenerExtension;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.shared.ui.Connect;

@SuppressWarnings("serial")
@Connect(KeyListenerExtension.class)
public class KeyListenerConnector extends AbstractExtensionConnector {
	KeyListenerServerRpc rpc = RpcProxy
			.create(KeyListenerServerRpc.class, this);

	private ServerConnector target;

	private Widget getWidget()
	{
		return  ((ComponentConnector) target).getWidget();
	}

	@Override
	protected void extend(ServerConnector target) {
		this.target = target;
		
		getWidget().addDomHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (event.getCharCode() != 0)
					rpc.charEntered(event.getCharCode());
			}
		}, KeyPressEvent.getType());
	}	
}
