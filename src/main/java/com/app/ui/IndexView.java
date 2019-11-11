package com.app.ui;


import javafx.fxml.FXML;
import javafx.scene.control.Button;


public class IndexView extends View {

	
	@FXML
	private Button btnInit;

	@Override
	public void init(App app) {
		// TODO Auto-generated method stub
		super.init(app);
		btnInit.setOnAction((evt)->{
			app.initAction();
		});
	}
	

}
