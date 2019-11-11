package com.app.ui;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory;

public class InputDialog extends View {

	@FXML
	private DatePicker datePicker;

	@FXML
	private Spinner<Double> spinner;

	@FXML
	private Button btAccept;

	private String itemId;

	@FXML
	private Label lbWarning;

	@Override
	public void init(App app) {

		super.init(app);
		DoubleSpinnerValueFactory valueFactory = new DoubleSpinnerValueFactory(1, Double.MAX_VALUE);
		valueFactory.setValue(1.0);
		valueFactory.setAmountToStepBy(1);
		spinner.setValueFactory(valueFactory);
		spinner.setEditable(true);

		btAccept.setOnAction((evt) -> {
			LocalDate date = datePicker.getValue();

			double value = spinner.getValue();

			if (value >= 1) {
				getApp().addDemand(itemId, value, date);

				this.show(false);
				lbWarning.setText("");
			} else {
				lbWarning.setText("El valor a ingresar debe ser mayor a 1 y menor a: "+Double.MAX_VALUE);
			}

		});

	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemId() {
		return itemId;
	}

}
