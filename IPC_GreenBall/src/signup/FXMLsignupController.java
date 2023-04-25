/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package signup;

import javafx.scene.image.Image;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Club;
import model.ClubDAOException;
import model.Member;

/**
 * FXML Controller class
 *
 * @author Juan
 */
public class FXMLsignupController implements Initializable {

    @FXML
    private Button enviarRegistroButton;
    @FXML
    private TextField nombre;
    @FXML
    private TextField telefono;
    @FXML
    private TextField nickname;
    @FXML
    private PasswordField password;
    @FXML
    private TextField tarjeta;
    @FXML
    private PasswordField svc;
    @FXML
    private TextField apellido;
    @FXML
    private Label labelSignup;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void enviarRegistroOnAction(ActionEvent event) throws ClubDAOException, IOException {
        String nombre = this.nombre.getText();
        String apellido = this.apellido.getText();
        String telefono = this.telefono.getText();
        String nickname = this.nickname.getText();
        String password = this.password.getText();
        String tarjeta = this.tarjeta.getText();
        int svc = Integer.parseInt(this.svc.getText());
        Image image = null;
        if (nombre.length() != 0 &&
            apellido.length() != 0 &&
            telefono.length() != 0 &&
            nickname.length() != 0 &&
            password.length() != 0) {
                Club club = Club.getInstance();
                Member newMember = club.registerMember(nombre, apellido, telefono, nickname, password, tarjeta, svc, image);
                if(newMember != null) labelSignup.setText("Nuevo miembro creado satisfactoriamente");
        }
    }
    
}
