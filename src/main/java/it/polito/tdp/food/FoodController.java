/**
 * Sample Skeleton for 'Food.fxml' Controller Class
 */

package it.polito.tdp.food;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.FoodAndCongiunte;
import it.polito.tdp.food.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FoodController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtPorzioni"
    private TextField txtPorzioni; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalorie"
    private Button btnCalorie; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="boxFood"
    private ComboBox<Food> boxFood; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	Integer porzioni;
    	
    	try {
    		porzioni = Integer.parseInt(this.txtPorzioni.getText());
    		
    	}catch (NumberFormatException e) {
    		txtResult.appendText("Inserisci un numero valido di calorie!");
    	    return;
    	}
    	this.model.creaGrafo(porzioni);
    	this.boxFood.getItems().clear();
    	List<Food>  cibi = this.model.getFoods(porzioni);
    	Collections.sort(cibi);
    	this.boxFood.getItems().addAll(cibi);
    	
    	txtResult.appendText(String.format("Grafo creato con %d vertici e %d archi", this.model.nVertici(), this.model.nArchi()));
    }
    
    @FXML
    void doCalorie(ActionEvent event) {
    	txtResult.clear();
    	
    	Food sorgente = this.boxFood.getSelectionModel().getSelectedItem();
    	if(sorgente == null) {
    		txtResult.appendText("Errore seleziona un cibo");
    		return;
    	}
    	
    	List<FoodAndCongiunte> lista = this.model.getCongiunti(sorgente);
    	for(int i=0; i<5 && i<lista.size(); i++) {
    		txtResult.appendText(String.format("%s %f  \n", lista.get(i).getF().getDisplay_name(),
    				                                        lista.get(i).getCalorieCongiunte()));
    	}
    	
    }

    @FXML
    void doSimula(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Simulazione...");
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtPorzioni != null : "fx:id=\"txtPorzioni\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCalorie != null : "fx:id=\"btnCalorie\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Food.fxml'.";
        assert boxFood != null : "fx:id=\"boxFood\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
