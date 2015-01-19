package com.example.gridtest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.annotation.WebServlet;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.Executor;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;


@SuppressWarnings("serial")
@Theme("gridtest")
@Widgetset("com.example.gridtest.widgetset.GridtestWidgetset")
@Push
public class GridtestUI extends UI {
	private final int COLUMNS = 75;
	private final int COLUMN_WIDTH = 12;

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = GridtestUI.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		createApp();
	}
	
	private void createApp() {
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		setContent(layout);
		layout.setSizeFull();

		ConsoleContainer container = new ConsoleContainer(COLUMNS);
		Grid grid = new Grid(container);
//		Table grid = new Table("", container);
		grid.setHeaderVisible(false);
		grid.setSelectionMode(com.vaadin.ui.Grid.SelectionMode.NONE);
//		grid.setEditorEnabled(true);		
		
		grid.setSizeFull();

		new KeyListenerExtension(this, (code) -> container.input(code));

		createConsole(container.getOutputStream(), container.getOutputStream(), container.getInputStream());
//		createConsole(System.out, System.err, System.in);
		
		container.addItemSetChangeListener((event) -> grid.scrollToEnd());
		
		layout.addComponent(new Label("Currently doesn't work in FF, use Chrome to test."+
		"<br>Note that this is not (yet) a full terminal, so you can't use some keys (like ctrl, backspace)"+
		"<br>Plese don't erase anything and don't run anything that can freeze the server.", ContentMode.HTML));
		layout.addComponent(grid);
		layout.setExpandRatio(grid, 1);
		
		grid.getColumns().forEach((c) -> c.setWidth(COLUMN_WIDTH));
		grid.setWidth(COLUMN_WIDTH*COLUMNS + 30, Unit.PIXELS);
		
		Page.getCurrent().getStyles().add(".gridtest .v-grid-cell { border: none !important; padding: 0; font-family: \"Courier New\", Courier, monospace; }");
		Page.getCurrent().getStyles().add(".gridtest .v-grid-cell { background-color: black !important; color: green !important; }");
		Page.getCurrent().getStyles().add(".v-grid { background-color: black !important; }");
	}

	public static void main(String[] args) {
		createConsole(System.out, System.err, System.in);
	}
	
	public static void createConsole(final OutputStream out, final OutputStream err,
            final InputStream input) {
	    Executor ex = new DefaultExecutor();
	    
	    ex.setStreamHandler(new MyPumpStreamHandler(out, err, input));
	    
	    CommandLine cl = new CommandLine("bash");
//	    cl.addArguments("--verbose");
//	    cl.addArguments("--norc");
//	    cl.addArguments("--noprofile");
	    cl.addArguments("-i");

	    new Thread(() -> {
		    try {
				ex.execute(cl);
		        System.out.println("ended");
		    } catch (ExecuteException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
	    }).start();
	}
}