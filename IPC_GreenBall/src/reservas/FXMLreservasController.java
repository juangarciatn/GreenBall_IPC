/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package reservas;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import main.FXMLmainController;

/**
 * FXML Controller class
 *
 * @author nando
 */
public class FXMLreservasController implements Initializable {

    @FXML
    private Label nickname;
    @FXML
    private Button eliminarButton;
    @FXML
    private Button volverButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        nickname.setText(FXMLmainController.getUser().getName());
    }    

    @FXML
    private void eliminarOnAction(ActionEvent event) {
    }

    @FXML
    private void volverOnAction(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    
}
