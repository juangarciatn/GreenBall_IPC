/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import login.FXMLloginController;

/**
 * FXML Controller class
 *
 * @author Juan
 */
public class FXMLmainController implements Initializable {

    @FXML
    private Label labelUser;
    public String usuario = "";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void labelUserClick(MouseEvent event) throws IOException {
        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("/login/FXMLlogin.fxml"));
        Parent root = miCargador.load();
        // acceso al controlador de datos persona
        FXMLloginController controladorLogin = miCargador.getController();
        Scene scene = new Scene(root,500,300);
        Stage stageLogin = new Stage();
        stageLogin.setScene(scene);
        stageLogin.setTitle("Iniciar sesión");
        stageLogin.initModality(Modality.APPLICATION_MODAL); 
        stageLogin.show();
    }
    
    public void userChange() {
        if (usuario.equals("") == false) {
        //labelUser.setText("Hola, " + "1234");
        System.out.println("Funciona");
        System.out.println(usuario);
        labelUser.setText("Hola");}
    }
    
}
