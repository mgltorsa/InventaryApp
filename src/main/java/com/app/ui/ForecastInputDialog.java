package com.app.ui;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

public class ForecastInputDialog extends View {

	@FXML
	private Button btAccept;

	@FXML
	private Label lbWarning;

	@FXML
	private DatePicker datePicker;

	private String forecastMethod;

	private String itemId;

	@Override
	public void init(App app) {
		// TODO Auto-generated method stub
		super.init(app);

		btAccept.setOnAction((evt) -> {

			LocalDate date = datePicker.getValue();
			LocalDate minDate = getApp().getMinDate();
			if (date.compareTo(minDate) > 0 && getApp().isAvailableDate(date)) {
				getApp().forecast(itemId, date.getYear(), date.getMonthValue(), this.forecastMethod);
				lbWarning.setText("");
				this.show(false);
			} else {
				lbWarning.setText("Solo se permiten fechas mayores a: " + minDate.toString());
			}
		});

	}

	public void setForecastMethod(String method) {
		this.forecastMethod = method;
	}

	public void setItem(String itemId) {
		this.itemId = itemId;

	}

}
