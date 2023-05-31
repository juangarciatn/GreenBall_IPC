/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package profile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import main.FXMLmainController;
import model.Club;
import model.ClubDAOException;
import model.Member;

/**
 * FXML Controller class
 *
 * @author Juan y yo
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
    
    private Image selectedImage;
    
    private boolean menuAbierto = false;
    private ContextMenu contextMenu = null;
    
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
        labelPicture.setImage(FXMLmainController.getUser().getImage());
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
                        if(esNumero(telefonoField.getText())){
                            FXMLmainController.getUser().setName(nombreField.getText());
                            FXMLmainController.getUser().setSurname(apellidosField.getText());
                            FXMLmainController.getUser().setTelephone(telefonoField.getText());
                            FXMLmainController.getUser().setImage(selectedImage);
                            mensajeError.setText("");
                            mensajeCorrecto.setText("Cambios guardados");
                            bloqueoCambios();
                            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(3000), event1 -> {
                                 Stage stage = (Stage) guardarButton.getScene().getWindow();
                                 stage.close();
                             }));
                             timeline.setCycleCount(1);
                             timeline.play();
                        } else{
                            mensajeError.setText("El número de teléfono no puede contener letras ni caracteres especiales.");
                            mensajeCorrecto.setText("");
                        }
                   }
               } else {
               
               if (tarjetaField.getText().length() == 16 && svcField.getText().length() == 3) {
                    if(esNumero(telefonoField.getText())){
                        if(esNumero(tarjetaField.getText())){
                            if(esNumero(svcField.getText())){
                                FXMLmainController.getUser().setName(nombreField.getText());
                                FXMLmainController.getUser().setSurname(apellidosField.getText());
                                FXMLmainController.getUser().setTelephone(telefonoField.getText());
                                FXMLmainController.getUser().setCreditCard(tarjetaField.getText());
                                FXMLmainController.getUser().setSvc(Integer.parseInt(svcField.getText()));
                                FXMLmainController.getUser().setImage(selectedImage);
                                mensajeError.setText("");
                                mensajeCorrecto.setText("Cambios guardados");
                                bloqueoCambios();
                                Timeline timeline = new Timeline(new KeyFrame(Duration.millis(3000), event1 -> {
                                    Stage stage = (Stage) guardarButton.getScene().getWindow();
                                    stage.close();
                                }));
                                timeline.setCycleCount(1);
                                timeline.play();
                            } else{
                            mensajeError.setText("El svc no puede contener letras ni caracteres especiales.");
                            mensajeCorrecto.setText("");
                        }
                        } else{
                            mensajeError.setText("El número de tarjeta no puede contener letras ni caracteres especiales.");
                            mensajeCorrecto.setText("");
                        }
                    } else{
                            mensajeError.setText("El número de teléfono no puede contener letras ni caracteres especiales.");
                            mensajeCorrecto.setText("");
                        }
               }    else if (tarjetaField.getText().equals("") && svcField.getText().equals("")) {
                        if(esNumero(telefonoField.getText())){
                            FXMLmainController.getUser().setName(nombreField.getText());
                            FXMLmainController.getUser().setSurname(apellidosField.getText());
                            FXMLmainController.getUser().setTelephone(telefonoField.getText());
                            FXMLmainController.getUser().setCreditCard(null);
                            FXMLmainController.getUser().setSvc(0);
                            FXMLmainController.getUser().setImage(selectedImage);
                            mensajeError.setText("");
                            mensajeCorrecto.setText("Cambios guardados");
                            bloqueoCambios();
                            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(3000), event1 -> {
                                    Stage stage = (Stage) guardarButton.getScene().getWindow();
                                    stage.close();
                                }));
                                timeline.setCycleCount(1);
                                timeline.play();
                        } else{
                            mensajeError.setText("El número de teléfono no puede contener letras ni caracteres especiales.");
                            mensajeCorrecto.setText("");
                        }
                    } else if (tarjetaField.getText().length() != 16) mensajeError.setText("La tarjeta de crédito debe tener 16 dígitos");
                            else if (svcField.getText().length() != 3) mensajeError.setText("El svc de la tarjeta debe tener 3 dígitos");
                  
               }
           } else if (passField.getText().equals(newPassField.getText())) mensajeError.setText("La nueva contraseña no puede ser igual a la anterior");
                else if (newPassField.getText().length() < 7) mensajeError.setText("La nueva contraseña debe tener 7 o más carácteres");
                    else if (tarjetaField.getText().equals("-")) {
                            if (svcField.getText().equals("-")) {
                                if(esNumero(telefonoField.getText())){
                                    FXMLmainController.getUser().setName(nombreField.getText());
                                    FXMLmainController.getUser().setSurname(apellidosField.getText());
                                    FXMLmainController.getUser().setTelephone(telefonoField.getText());
                                    FXMLmainController.getUser().setPassword(newPassField.getText());
                                    FXMLmainController.getUser().setImage(selectedImage);
                                    mensajeError.setText("");
                                    mensajeCorrecto.setText("Cambios guardados");
                                    bloqueoCambios();
                                    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(3000), event1 -> {
                                        Stage stage = (Stage) guardarButton.getScene().getWindow();
                                        stage.close();
                                    }));
                                    timeline.setCycleCount(1);
                                    timeline.play();
                                } else{
                                    mensajeError.setText("El número de teléfono no puede contener letras ni caracteres especiales.");
                                    mensajeCorrecto.setText("");
                                }   
                            }
                    } else {
                        if (tarjetaField.getText().length() == 16 && svcField.getText().length() == 3) {
                            if(esNumero(telefonoField.getText())){
                                if(esNumero(tarjetaField.getText())){
                                    if(esNumero(svcField.getText())){
                                        FXMLmainController.getUser().setName(nombreField.getText());
                                        FXMLmainController.getUser().setSurname(apellidosField.getText());
                                        FXMLmainController.getUser().setTelephone(telefonoField.getText());
                                        FXMLmainController.getUser().setCreditCard(tarjetaField.getText());
                                        FXMLmainController.getUser().setSvc(Integer.parseInt(svcField.getText()));
                                        FXMLmainController.getUser().setImage(selectedImage);
                                        mensajeError.setText("");
                                        mensajeCorrecto.setText("Cambios guardados");
                                        bloqueoCambios();
                                        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(3000), event1 -> {
                                            Stage stage = (Stage) guardarButton.getScene().getWindow();
                                            stage.close();
                                        }));
                                        timeline.setCycleCount(1);
                                        timeline.play();
                                    }else{
                                        mensajeError.setText("El svc no puede contener letras ni caracteres especiales.");
                                        mensajeCorrecto.setText("");
                                    }
                                } else{
                                        mensajeError.setText("El número de tarjeta no puede contener letras ni caracteres especiales.");
                                        mensajeCorrecto.setText("");
                                    }
                            } else{
                                mensajeError.setText("El número de teléfono no puede contener letras ni caracteres especiales.");
                                mensajeCorrecto.setText("");
                            }
               }    else if (tarjetaField.getText().equals("") && svcField.getText().equals("")) {
                        if(esNumero(telefonoField.getText())){
                            FXMLmainController.getUser().setName(nombreField.getText());
                            FXMLmainController.getUser().setSurname(apellidosField.getText());
                            FXMLmainController.getUser().setTelephone(telefonoField.getText());
                            FXMLmainController.getUser().setCreditCard(null);
                            FXMLmainController.getUser().setSvc(0);
                            FXMLmainController.getUser().setImage(selectedImage);
                            mensajeError.setText("");
                            mensajeCorrecto.setText("Cambios guardados");
                            bloqueoCambios();
                            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(3000), event1 -> {
                                Stage stage = (Stage) guardarButton.getScene().getWindow();
                                stage.close();
                            }));
                            timeline.setCycleCount(1);
                            timeline.play();
                        } else{
                                mensajeError.setText("El número de teléfono no puede contener letras ni caracteres especiales.");
                                mensajeCorrecto.setText("");
                            }
                    } else if (tarjetaField.getText().length() != 16) mensajeError.setText("La tarjeta de crédito debe tener 16 dígitos");
                            else if (svcField.getText().length() != 3) mensajeError.setText("El svc de la tarjeta debe tener 3 dígitos");
                    }
       } else mensajeError.setText("Contraseña incorrecta");
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
        this.menuAbierto = true;
    }

    @FXML
    private void cambiarImagenOnMouseClicked(MouseEvent event) {
        contextMenu = new ContextMenu(); //se crea contextMenu con las 3 opciones
        contextMenu.setStyle("-fx-background-color: #0E3B43; -fx-padding: 5px; -fx-border-color: #357266; -fx-border-width: 1px;");
            MenuItem option1 = new MenuItem("Cambiar imagen de usuario por un avatar");//primera opcion
            option1.setStyle("-fx-text-fill: #FEFCFD; -fx-font-weight: bold;");
            option1.setOnAction(e -> {
                FileChooser avatarFileChooser = new FileChooser();
                avatarFileChooser.setInitialDirectory(new File("src/img/avatars"));
                avatarFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos PNG", "*.png"));
                Window parentWindow = labelPicture.getScene().getWindow();// si fileChooser abierto no se puede clickar en otra ventana
                File selectedFile = avatarFileChooser.showOpenDialog(parentWindow);


                if (selectedFile != null) {
                    selectedImage = new Image(selectedFile.toURI().toString());
                    labelPicture.setImage(selectedImage);
                }
            });

            MenuItem option2 = new MenuItem("Cambiar imagen de usuario por una imagen");//segunda opcion
            option2.setStyle("-fx-text-fill: #FEFCFD; -fx-font-weight: bold;");
            option2.setOnAction(e -> {
                FileChooser imageFileChooser = new FileChooser();
                imageFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.jpg", "*.jpeg", "*.png", "*.gif"));
                Window parentWindow = labelPicture.getScene().getWindow();// si fileChooser abierto no se puede clickar en otra ventana
                File selectedFile = imageFileChooser.showOpenDialog(parentWindow);

                if (selectedFile != null) {
                    selectedImage = new Image(selectedFile.toURI().toString());
                    labelPicture.setImage(selectedImage);
                    
                }
            });

            MenuItem option3 = new MenuItem("Eliminar imagen de usuario");//tercera opcion
            option3.setStyle("-fx-text-fill: #FEFCFD; -fx-font-weight: bold;");
            option3.setOnAction(e -> {
                if (selectedImage == null) {
                    String imagePath = "src/img/avatars/default.png";
                    File file = new File(imagePath);
                    if (file.exists()) {
                        selectedImage = new Image(file.toURI().toString()); // Guarda la imagen default en selectedImage
                        labelPicture.setImage(selectedImage);
                    } else {
                        System.out.println("No se encontró la imagen predeterminada: " + imagePath);
                    }
                }
            });

            contextMenu.getItems().addAll(option1, option2, option3);
            if(!menuAbierto) {
                contextMenu.show(labelPicture, event.getScreenX(), event.getScreenY());//muestra contextMenu
                menuAbierto = true;
            }
            contextMenu.setOnHidden(cerrado -> {
                menuAbierto = false;
            });
            
            
    }
    
    private boolean esNumero(String texto){
        for (int i = 0; i < texto.length(); i++) {
            if (!Character.isDigit(texto.charAt(i))) {
                return false;
            }
        }
        return true;
    }

}

