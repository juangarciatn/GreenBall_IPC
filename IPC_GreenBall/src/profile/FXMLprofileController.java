/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package profile;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import main.FXMLmainController;
import model.Club;
import model.ClubDAOException;
import model.Member;

/**
 * FXML Controller class
 *
 * @author Juan
 */
public final class FXMLprofileController implements Initializable {

    @FXML
    private ImageView labelPicture;
    
    private Club club;
    @FXML
    private TextField usuarioField;
    @FXML
    private TextField nombreField;
    @FXML
    private TextField apellidosField;
    @FXML
    private TextField telefonoField;
    @FXML
    private TextField tarjetaField;
    @FXML
    private TextField svcField;
    @FXML
    private TextField passField;
    @FXML
    private TextField newPassField;
    @FXML
    private Label mensajeCorrecto;
    @FXML
    private Label mensajeError;
    private Button cancelarButton;
    @FXML
    private Button guardarButton;
    
    public FXMLprofileController() throws ClubDAOException, IOException {
        this.club = Club.getInstance();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //if (FXMLmainController.getUser().getImage() != null) labelPicture.setImage(FXMLmainController.getUser().getImage());
        usuarioField.setText(FXMLmainController.getUser().getNickName());
        nombreField.setText(FXMLmainController.getUser().getName());
        apellidosField.setText(FXMLmainController.getUser().getSurname());
    }

    @FXML
    private void enterGuardar(KeyEvent event) throws ClubDAOException, IOException {
        if(event.getCode() == KeyCode.ENTER){
            guardarButtonOnAction(new ActionEvent());
        }
    }

    @FXML
    private void cancelarButtonOnAction(ActionEvent event){
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void guardarButtonOnAction(ActionEvent event) throws ClubDAOException, IOException {
       FXMLmainController.getUser().setName(nombreField.getText());
       FXMLmainController.getUser().setSurname(apellidosField.getText());
       Stage stage = (Stage) guardarButton.getScene().getWindow();
       stage.close();
    }

    @FXML
    private void enterGuardar(KeyEvent event) throws ClubDAOException, IOException {
        if(event.getCode() == KeyCode.ENTER){
            ActionEvent ac1 = new ActionEvent();
            guardarButtonOnAction(ac1);
        }
    }

    @FXML
    private void clickImagen(MouseEvent event) {
    }
}

