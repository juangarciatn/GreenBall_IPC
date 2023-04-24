/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package startPage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import login.FXMLloginController;
import signup.FXMLsignupController;

/**
 * FXML Controller class
 *
 * @author Juan
 */
public class FXMLstartPageController implements Initializable {

    @FXML
    private Button loginButton;
    @FXML
    private Button signupButton;
    @FXML
    private Button disponibleButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void loginOnAction(ActionEvent event) throws IOException {
        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("/login/FXMLlogin.fxml"));
        Parent root = miCargador.load();
        // acceso al controlador de datos persona
        FXMLloginController controladorLogin = miCargador.getController();
        Scene scene = new Scene(root,500,300);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Iniciar sesión");
        stage.initModality(Modality.APPLICATION_MODAL); 
        stage.show();
    }

    @FXML
    private void signupOnAction(ActionEvent event) throws IOException {
        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("/signup/FXMLsignup.fxml"));
        Parent root = miCargador.load();
        // acceso al controlador de datos persona
        FXMLsignupController controladorSignup = miCargador.getController();
        Scene scene = new Scene(root,500,300);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Iniciar sesión");
        stage.initModality(Modality.APPLICATION_MODAL); 
        stage.show();
    }

    @FXML
    private void disponibleOnAction(ActionEvent event) {
    }
    
}
