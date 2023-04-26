/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package login;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Club;
import model.ClubDAOException;
import model.Member;

/**
 * FXML Controller class
 *
 * @author Juan
 */
public class FXMLloginController implements Initializable {

    @FXML
    private TextField user;
    @FXML
    private PasswordField password;
    @FXML
    private Button enviarLoginButton;
    @FXML
    private Label labelLogin;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void enviarLoginOnAction(ActionEvent event) throws ClubDAOException, IOException, InterruptedException {
       String usuario = user.getText();
       String clave = password.getText();
       if (user.getText().length() != 0 && password.getText().length() != 0) {
           labelLogin.setText("");
        Club club = Club.getInstance();
        try{
            if (club.getMemberByCredentials(usuario, clave) != null) {
                System.out.println("Login funciona");
                Node source = (Node) event.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
            }
        } catch (NullPointerException e) {
            labelLogin.setText("El usuario o la contraseña no existen");
        }
        //if (club.getMemberByCredentials(usuario, clave) == null) labelLogin.setText("El usuario o la contraseña no existen");
        //    else System.out.print("Login funciona");
       }
       else {
           labelLogin.setText("Debes rellenar los campos obligatorios");
           //Thread.sleep(5*1000); //ms
       }
    }
    
}
