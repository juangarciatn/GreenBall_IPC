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
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
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
    @FXML
    private Label labelSignupError;
    @FXML
    private Button CancelarRegistro;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void enviarRegistroOnAction(ActionEvent event) throws ClubDAOException, IOException, InterruptedException {
        Club club = Club.getInstance();
        String nombreS = nombre.getText();
        String apellidoS = apellido.getText();
        String telefonoS = telefono.getText();
        String nicknameS = nickname.getText();  
        String passwordS = password.getText();
        String tarjetaS = tarjeta.getText();
        String svcP = svc.getText();
        int svcS = 0;
        if (svcP.length() != 0) svcS = Integer.parseInt(svcP);
        Image image = null;
        
        if (nombreS.length() != 0 &&
            apellidoS.length() != 0 &&
            telefonoS.length() != 0 &&
            nicknameS.length() != 0 &&
            passwordS.length() != 0) {
                if (nicknameS.contains(" ")) labelSignupError.setText("El nombre de usuario no puede tener espacios.");
                    else if (passwordS.length() < 6) labelSignupError.setText("La contraseña debe ser mayor de 6 carácteres.");
                        else if (tarjetaS.length() != 0 || svcS != 0) {
                            if (tarjetaS.length() < 13 || tarjetaS.length() > 18) labelSignupError.setText("Introduce una tarjeta de crédito correcta");
                                else if (svcP.length() < 3 || svcP.length() > 4) labelSignupError.setText("Introduce un svc correcto");
                                else if ((tarjetaS.length() > 12 || tarjetaS.length() < 19) && (svcS > 2 || svcS < 5)){
                                    try {
                                        Member newMember = club.registerMember(nombreS, apellidoS, telefonoS, nicknameS, passwordS, tarjetaS, svcS, image);
                                        if(newMember != null) {
                                        labelSignupError.setText("");
                                        labelSignup.setText("Usuario creado");
                                        wait(3*1000);
                                        Node source = (Node) event.getSource();
                                        Stage stage = (Stage) source.getScene().getWindow();
                                        stage.close();
                                        }
                                    }
                                        catch(ClubDAOException e) {
                                            labelSignup.setText("");
                                            labelSignupError.setText("Nombre de usuario en uso");
                                        }
                        }       
                    } else {
                        try {
                            Member newMember = club.registerMember(nombreS, apellidoS, telefonoS, nicknameS, passwordS, null, 0, image);
                            if(newMember != null) {
                                labelSignupError.setText("");
                                labelSignup.setText("Usuario creado");
                            }
                        } catch(ClubDAOException e) {
                            labelSignup.setText("");
                            labelSignupError.setText("Nombre de usuario en uso");
                          }
                    }
            }
            else {
                labelSignup.setText("");
                labelSignupError.setText("Rellena los campos obligatorios");
            }
    }

    @FXML
    private void CancelarRegistroOnAction(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void enterRegistro(KeyEvent event) {
    }
}
