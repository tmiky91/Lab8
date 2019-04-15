package it.polito.tdp.metrodeparis;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.metrodeparis.model.Fermata;
import it.polito.tdp.metrodeparis.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class MetroDeParisController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Fermata> btnComboBoxPartenza;

    @FXML
    private ComboBox<Fermata> btnComboBoxArrivo;

    @FXML
    private Button btnCalcolaPercorso;

    @FXML
    private TextArea txtResult;

	private Model model;

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	Fermata partenza = btnComboBoxPartenza.getValue();
    	Fermata arrivo = btnComboBoxPartenza.getValue();
    	if(partenza!=null) {
    		if(arrivo!=null) {
    			txtResult.setText(model.calcolaPercorso(partenza, arrivo));
    		}
    		else {
    			showMessage("Seleziona una fermata di arrivo");
    		}
    	}
    	else {
    		showMessage("Seleziona una fermata di partenza");
    	}

    }

    @FXML
    void initialize() {
        assert btnComboBoxPartenza != null : "fx:id=\"btnComboBoxPartenza\" was not injected: check your FXML file 'MetroDeParis.fxml'.";
        assert btnComboBoxArrivo != null : "fx:id=\"btnComboBoxArrivo\" was not injected: check your FXML file 'MetroDeParis.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'MetroDeParis.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'MetroDeParis.fxml'.";
        
        btnComboBoxPartenza.getItems().addAll(Model.getTutteLeFermate());
        btnComboBoxArrivo.getItems().addAll(Model.getTutteLeFermate());

    }

	public void setModel(Model model) {
		this.model = model;
	}
	
	private void showMessage(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setContentText(message);
		alert.show();
	}
}
