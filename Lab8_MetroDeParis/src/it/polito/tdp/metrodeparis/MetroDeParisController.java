package it.polito.tdp.metrodeparis;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.metrodeparis.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class MetroDeParisController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<?> btnComboBoxPartenza;

    @FXML
    private ComboBox<?> btnComboBoxArrivo;

    @FXML
    private Button btnCalcolaPercorso;

    @FXML
    private TextArea txtResult;

	private Model model;

    @FXML
    void doCalcolaPercorso(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert btnComboBoxPartenza != null : "fx:id=\"btnComboBoxPartenza\" was not injected: check your FXML file 'MetroDeParis.fxml'.";
        assert btnComboBoxArrivo != null : "fx:id=\"btnComboBoxArrivo\" was not injected: check your FXML file 'MetroDeParis.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'MetroDeParis.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'MetroDeParis.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		
	}
}
