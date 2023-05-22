/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package profile;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
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
    private Label mensajeCorrecto;
    @FXML
    private Label mensajeError;
    @FXML
    private Button guardarButton;
    @FXML
    private PasswordField passField;
    @FXML
    private PasswordField newPassField;
    @FXML
    private Button cancelarButton;
    
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
        telefonoField.setText(FXMLmainController.getUser().getTelephone());
        //System.out.println(FXMLmainController.getUser().getCreditCard());
        //System.out.println(Integer.toString(FXMLmainController.getUser().getSvc()));
        if (FXMLmainController.getUser().getCreditCard() == null) tarjetaField.setText("-");
            else tarjetaField.setText(FXMLmainController.getUser().getCreditCard());
        if (FXMLmainController.getUser().getSvc() == 0) svcField.setText("-");
            else svcField.setText(Integer.toString(FXMLmainController.getUser().getSvc()));
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
        if (passField.getText().equals(FXMLmainController.getUser().getPassword())) {
           if (newPassField.getText().equals("")) {
               if (tarjetaField.getText().equals("-")) {
                   if (svcField.getText().equals("-")) {
                       FXMLmainController.getUser().setName(nombreField.getText());
                       FXMLmainController.getUser().setSurname(apellidosField.getText());
                       FXMLmainController.getUser().setTelephone(telefonoField.getText());
                       mensajeError.setText("");
                       mensajeCorrecto.setText("Cambios guardados");
                       bloqueoCambios();
                       ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
                                     executorService.schedule(() -> {Platform.runLater(() -> {
                                    Stage stage = (Stage) guardarButton.getScene().getWindow();
                                    stage.close(); });}, 3, TimeUnit.SECONDS);
                   }
               } else {
               if (tarjetaField.getText().length() != 16) mensajeError.setText("La tarjeta de crédito debe tener 16 dígitos");
               if (svcField.getText().length() != 3) mensajeError.setText("El svc de la tarjeta debe tener 3 dígitos");
               if (tarjetaField.getText().length() == 16 && svcField.getText().length() == 3) {
                    FXMLmainController.getUser().setName(nombreField.getText());
                    FXMLmainController.getUser().setSurname(apellidosField.getText());
                    FXMLmainController.getUser().setTelephone(telefonoField.getText());
                    FXMLmainController.getUser().setCreditCard(tarjetaField.getText());
                    FXMLmainController.getUser().setSvc(Integer.parseInt(svcField.getText()));
                    mensajeError.setText("");
                    mensajeCorrecto.setText("Cambios guardados");
                    bloqueoCambios();
                    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
                                     executorService.schedule(() -> {Platform.runLater(() -> {
                                    Stage stage = (Stage) guardarButton.getScene().getWindow();
                                    stage.close(); });}, 3, TimeUnit.SECONDS);
               }    else {
                        FXMLmainController.getUser().setName(nombreField.getText());
                        FXMLmainController.getUser().setSurname(apellidosField.getText());
                        FXMLmainController.getUser().setTelephone(telefonoField.getText());
                        FXMLmainController.getUser().setCreditCard(null);
                        FXMLmainController.getUser().setSvc(0);
                        mensajeError.setText("");
                        mensajeCorrecto.setText("Cambios guardados");
                        bloqueoCambios();
                        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
                                    executorService.schedule(() -> {Platform.runLater(() -> {
                                    Stage stage = (Stage) guardarButton.getScene().getWindow();
                                    stage.close(); });}, 3, TimeUnit.SECONDS);
                    }    
               }
           } else if (passField.getText().equals(newPassField.getText())) mensajeError.setText("La nueva contraseña no puede ser igual a la anterior");
                else if (newPassField.getText().length() < 7) mensajeError.setText("La nueva contraseña debe tener 7 o más carácteres");
                    else {
                        FXMLmainController.getUser().setName(nombreField.getText());
                        FXMLmainController.getUser().setSurname(apellidosField.getText());
                        FXMLmainController.getUser().setTelephone(telefonoField.getText());
                        FXMLmainController.getUser().setCreditCard(tarjetaField.getText());
                        FXMLmainController.getUser().setSvc(Integer.parseInt(svcField.getText()));
                        FXMLmainController.getUser().setPassword(newPassField.getText());
                        mensajeError.setText("");
                        mensajeCorrecto.setText("Cambios guardados");
                        bloqueoCambios();
                        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
                                    executorService.schedule(() -> {Platform.runLater(() -> {
                                    Stage stage = (Stage) guardarButton.getScene().getWindow();
                                    stage.close(); });}, 3, TimeUnit.SECONDS);
                    }
       } else mensajeError.setText("Contraseña incorrecta");
      }

    @FXML
    private void clickImagen(MouseEvent event) {
        System.out.println("Imagen");
    }

    public void bloqueoCambios() {
        guardarButton.setDisable(true);
        nombreField.setDisable(true);
        apellidosField.setDisable(true);
        telefonoField.setDisable(true);
        tarjetaField.setDisable(true);
        svcField.setDisable(true);
        passField.setDisable(true);
        newPassField.setDisable(true);
        cancelarButton.setDisable(true);
    }

}

