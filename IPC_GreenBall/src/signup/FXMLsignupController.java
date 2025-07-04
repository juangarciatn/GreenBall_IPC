/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package signup;

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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Club;
import model.ClubDAOException;
import model.Member;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import java.io.File;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Side;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Window;
import javafx.util.Duration;


/**
 * FXML Controller class
 *
 * @author Juan, Dani, Nando
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
    @FXML
    private Button avatarButton;
    @FXML
    private Button imageButton;
    @FXML
    private ImageView imageUser;
    @FXML
    private ToggleButton mostrarContraseña;
    
    private Image closeEye;
    private Image openEye;
    private ImageView closeEyeView;
    private ImageView openEyeView;
    
    private boolean mostrarContraseñaPresionado = false;
    private Image selectedImage;
    
    private boolean avatarFileChooserAbierto = false;
    private boolean imageFileChooserAbierto = false;
    
    
    FileChooser avatarFileChooser;
    FileChooser imageFileChooser;

    
    
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        avatarFileChooser = new FileChooser();
        avatarFileChooser.setInitialDirectory(new File("src/img/avatars"));
        avatarFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos PNG", "*.png"));
        
        imageFileChooser = new FileChooser();
        imageFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.jpg", "*.jpeg", "*.png", "*.gif"));
        
        closeEye = new Image("/img/closeEyeWhite.png");
        openEye = new Image("/img/openEyeWhite.png");
        closeEyeView = new ImageView(closeEye);
        openEyeView = new ImageView(openEye);
        closeEyeView.setFitHeight(15);
        closeEyeView.setFitWidth(15);
        openEyeView.setFitHeight(15);
        openEyeView.setFitWidth(15);
        mostrarContraseña.setGraphic(closeEyeView);

    
    }    

    @FXML
    private void enviarRegistroOnAction(ActionEvent event) throws ClubDAOException, IOException, InterruptedException {
        removeStyles();
        Club club = Club.getInstance();
        String nombreS = nombre.getText();
        String apellidoS = apellido.getText();
        String telefonoS = telefono.getText();
        String nicknameS = nickname.getText();  
        String passwordS = password.getText();
        String tarjetaS = tarjeta.getText();
        String svcP = svc.getText();
        int svcS = 0;
        
        if (selectedImage == null) {
            String imagePath = "src/img/avatars/default.png";
            File file = new File(imagePath);
            if (file.exists()) {
                selectedImage = new Image(file.toURI().toString());
            } else {
                System.out.println("No se encontró la imagen predeterminada: " + imagePath);
            }
        }

        if (nombreS.length() != 0 &&
            apellidoS.length() != 0 &&
            telefonoS.length() != 0 &&
            nicknameS.length() != 0 &&
            passwordS.length() != 0) {

            if (nicknameS.contains(" ")) {
                this.nickname.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                labelSignupError.setText("El nombre de usuario no puede tener espacios.");
            } else if (passwordS.length() < 7) {
                this.password.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                labelSignupError.setText("La contraseña debe tener al menos 7 caracteres.");
            } else if (tarjetaS.length() != 0 || svcP.length() != 0) {
                if (!esNumero(tarjetaS) || tarjetaS.length() != 16) {
                    this.tarjeta.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                    labelSignupError.setText("Introduce una tarjeta de crédito correcta (16 dígitos).");
                } else if (!esNumero(svcP) || svcP.length() != 3) {
                    this.svc.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                    labelSignupError.setText("Introduce un código SVC correcto (3 dígitos).");
                } else {
                    if (svcP.length() != 0) svcS = Integer.parseInt(svcP);
                    if(esNumero(telefonoS)){
                        
                        try {
                            Member newMember = club.registerMember(nombreS, apellidoS, telefonoS, nicknameS, passwordS, tarjetaS, svcS, selectedImage);
                            if (newMember != null) {
                               labelSignupError.setText("");
                               labelSignup.setText("Usuario creado");
                               bloqueoRegistro();
                               Timeline timeline = new Timeline(new KeyFrame(Duration.millis(3000), event1 -> {
                                   Stage stage = (Stage) enviarRegistroButton.getScene().getWindow();
                                   stage.close();
                               }));
                               timeline.setCycleCount(1);
                               timeline.play();
                            }
                        } catch (ClubDAOException e) {
                            labelSignup.setText("");
                            labelSignupError.setText("Nombre de usuario en uso.");
                        }
                    } else{
                        labelSignup.setText("");
                        this.telefono.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                        labelSignupError.setText("El número de teléfono no puede contener letras ni caracteres especiales.");
                    }
                }
            } else {
                if(esNumero(telefonoS)){
                    try {
                        Member newMember = club.registerMember(nombreS, apellidoS, telefonoS, nicknameS, passwordS, null, 0, selectedImage);
                        if (newMember != null) {
                            labelSignupError.setText("");
                            labelSignup.setText("Usuario creado");
                            bloqueoRegistro();
                            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(3000), event1 -> {
                                Stage stage = (Stage) enviarRegistroButton.getScene().getWindow();
                                stage.close();
                            }));
                            timeline.setCycleCount(1);
                            timeline.play();
                        }
                    } catch (ClubDAOException e) {
                        labelSignup.setText("");
                        labelSignupError.setText("Nombre de usuario en uso.");
                    }
                } else{
                        labelSignup.setText("");
                        this.telefono.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                        labelSignupError.setText("El número de teléfono no puede contener letras ni caracteres especiales.");
                    }
            }
        } else {
            if (this.nombre.getText().equals("")) this.nombre.setStyle("-fx-border-color: red; -fx-border-width: 2px");
            if (this.apellido.getText().equals("")) this.apellido.setStyle("-fx-border-color: red; -fx-border-width: 2px");
            if (this.telefono.getText().equals("")) this.telefono.setStyle("-fx-border-color: red; -fx-border-width: 2px");
            if (this.nickname.getText().equals("")) this.nickname.setStyle("-fx-border-color: red; -fx-border-width: 2px");
            if (this.password.getText().equals("")) this.password.setStyle("-fx-border-color: red; -fx-border-width: 2px");
            
            labelSignup.setText("");
            labelSignupError.setText("Rellena los campos obligatorios.");
        }
    }


    @FXML
    private void CancelarRegistroOnAction(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void enterRegistro(KeyEvent event) throws ClubDAOException, IOException, InterruptedException {
        if (event.getCode() == KeyCode.ENTER) {
            ActionEvent ac1 = new ActionEvent();
            enviarRegistroOnAction(ac1);
        }
    }

    @FXML
    private void avatarButtonOnAction(ActionEvent event) {
        
        /*if (imageFileChooserAbierto) {
            return; // Salir si el imageFileChooser ya está abierto
        }
        
        if (avatarFileChooserAbierto) {
            return; // Salir si el avatarFileChooser ya está abierto
        }*/

        avatarFileChooserAbierto = true; // Marcar el FileChooser como abierto
        
        Window parentWindow = imageUser.getScene().getWindow();
        File selectedFile = avatarFileChooser.showOpenDialog(parentWindow);
        
        if (selectedFile != null) {
            String imagePath = selectedFile.toURI().toString();
            Image image = new Image(imagePath);
            imageUser.setImage(image);
            selectedImage = image; // Guarda la imagen seleccionada en selectedImage
        }

        avatarFileChooserAbierto = false; // Marcar el FileChooser como cerrado
    }




    @FXML
    private void imageButtonOnAction(ActionEvent event) {
        
        /*if (avatarFileChooserAbierto) {
            return; // Salir si el avatarFileChooser ya está abierto
        }
        
        if (imageFileChooserAbierto) {
            return; // Salir si el imageFileChooser ya está abierto
        }*/

        imageFileChooserAbierto = true; // Marcar el FileChooser como abierto
        
        Window parentWindow = imageUser.getScene().getWindow();
        File selectedFile = imageFileChooser.showOpenDialog(parentWindow);

        if (selectedFile != null) {
            String imagePath = selectedFile.toURI().toString();
            Image image = new Image(imagePath);
            imageUser.setImage(image);
            selectedImage = image; // Guarda la imagen seleccionada en selectedImage
        }

        imageFileChooserAbierto = false; // Marcar el FileChooser como cerrado
    }

    
    public void bloqueoRegistro() {
        enviarRegistroButton.setDisable(true);
        nombre.setDisable(true);
        telefono.setDisable(true);
        nickname.setDisable(true);
        password.setDisable(true);
        tarjeta.setDisable(true);
        svc.setDisable(true);
        apellido.setDisable(true);
        labelSignup.setDisable(true);
        labelSignupError.setDisable(true);
        CancelarRegistro.setDisable(true);
        mostrarContraseña.setDisable(true);
        avatarButton.setDisable(true);
        imageButton.setDisable(true);
    }

    @FXML
    private void mostrarContraseñaOnMouseReleased(MouseEvent event) {
        mostrarContraseñaPresionado = false;
        mostrarContraseña.setGraphic(closeEyeView);
        mostrarContraseña();
    }

    @FXML
    private void mostrarContraseñaOnMousePressed(MouseEvent event) {
        mostrarContraseñaPresionado = true;
        mostrarContraseña.setGraphic(openEyeView);
        mostrarContraseña();
    }
    
    private void mostrarContraseña() {
        if (mostrarContraseñaPresionado) {
            password.setPromptText(password.getText());
            password.setText("");
        } else {
            password.setText(password.getPromptText());
            password.setPromptText("");
        }
    }
    
    private boolean esNumero(String texto){
        for (int i = 0; i < texto.length(); i++) {
            if (!Character.isDigit(texto.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    private void removeStyles() {
        this.nickname.setStyle("-fx-border-color: transparent");
        this.nombre.setStyle("-fx-border-color: transparent");
        this.apellido.setStyle("-fx-border-color: transparent");
        this.telefono.setStyle("-fx-border-color: transparent");
        this.tarjeta.setStyle("-fx-border-color: transparent");
        this.svc.setStyle("-fx-border-color: transparent");
        this.nickname.setStyle("-fx-border-color: transparent");
        this.password.setStyle("-fx-border-color: transparent");
    }
}
