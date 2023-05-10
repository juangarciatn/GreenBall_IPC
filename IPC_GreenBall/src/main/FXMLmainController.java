/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import login.FXMLloginController;

/**
 * FXML Controller class
 *
 * @author Juan
 */
public class FXMLmainController implements Initializable {

    @FXML
    private Label labelUser;
    
    private String user;
    
    private boolean loggedIn = false;
    @FXML
    private Label labelLogin;
    @FXML
    private DatePicker dpBookingDay;
    @FXML
    private ImageView myImageView;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void labelUserClick(MouseEvent event) throws IOException {
        if (loggedIn) {
            FXMLLoader miCargador = new FXMLLoader(getClass().getResource("/profile/FXMLprofile.fxml"));
            Parent root = miCargador.load();
            // acceso al controlador de datos persona
            FXMLloginController controladorLogin = miCargador.getController();
            Scene scene = new Scene(root,500,300);
            Stage stageLogin = new Stage();
            stageLogin.setScene(scene);
            stageLogin.setTitle("Perfil");
            stageLogin.initModality(Modality.WINDOW_MODAL);
            stageLogin.showAndWait();
        } else {
            FXMLLoader miCargador = new FXMLLoader(getClass().getResource("/login/FXMLlogin.fxml"));
            Parent root = miCargador.load();
            // acceso al controlador de datos persona
            FXMLloginController controladorLogin = miCargador.getController();
            Scene scene = new Scene(root,500,300);
            Stage stageLogin = new Stage();
            stageLogin.setScene(scene);
            stageLogin.setTitle("Iniciar sesión");
            stageLogin.initModality(Modality.WINDOW_MODAL);
            stageLogin.showAndWait();
            userChange();
        }
    }
    
    public void userChange() throws FileNotFoundException {
        if (FXMLloginController.isOk()){
            user = FXMLloginController.getUsername();
            labelLogin.setText("");
            labelUser.setText("Hola, " + user);
            loggedIn = true;
            
            String url = File.separator+"images"+File.separator+"default.PNG";
            Image avatar = new Image(new FileInputStream(url));
            myImageView.imageProperty().setValue(avatar);  
        }
    }

    @FXML
    private void dpBookingDayOnAction(ActionEvent event) {
        dpBookingDay.setDayCellFactory((DatePicker picker) -> {
            return new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) < 0 );
                }
            };
        });
    }
    
    
}
