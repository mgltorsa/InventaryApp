package com.app.ui;

import java.util.HashMap;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public abstract class View {
	
	private App app;
	
	private Pane pane;
	private Stage stage;
	private HashMap<String, Node> nodes;
	
	public void init(App app) {
		this.app = app;
		nodes = new HashMap<String, Node>();
		for (Node node : pane.getChildren()) {
			nodes.put(node.getId(), node);
		}
	}
	
	public Node getNode(String id) {
		return nodes.get(id);
	}
	
	

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public HashMap<String, Node> getNodes() {
		return nodes;
	}

	public void setNodes(HashMap<String, Node> nodes) {
		this.nodes = nodes;
	}

	public Pane getPane() {
		return pane;
	}

	public Stage getStage() {
		return stage;
	}

	public void setPane(Pane pane) {
		this.pane = pane;
	}

	public void setStage(Stage stage) {
		this.stage = stage;

	}

	public void show(boolean show) {
		if (show) {
			stage.show();
		} else if(stage.isShowing()) {
			stage.hide();
		}
	}
	
	
	
	

}
