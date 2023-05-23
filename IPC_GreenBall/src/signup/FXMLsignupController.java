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
import javafx.geometry.Side;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;


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
    @FXML
    private Button avatarButton;
    @FXML
    private Button imageButton;
    @FXML
    private ImageView imageUser;
    @FXML
    private ToggleButton mostrarContraseña;
    
    private boolean mostrarContraseñaPresionado = false;
    private Image selectedImage;

    
    
    

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
                labelSignupError.setText("El nombre de usuario no puede tener espacios.");
            } else if (passwordS.length() < 7) {
                labelSignupError.setText("La contraseña debe tener al menos 7 caracteres.");
            } else if (tarjetaS.length() != 0 || svcS != 0) {
                if (!tarjetaS.matches("\\d{16}")) {
                    labelSignupError.setText("Introduce una tarjeta de crédito correcta (16 dígitos).");
                } else if (!svcP.matches("\\d{3}")) {
                    labelSignupError.setText("Introduce un código SVC correcto (3 dígitos).");
                } else {
                    try {
                        Member newMember = club.registerMember(nombreS, apellidoS, telefonoS, nicknameS, passwordS, tarjetaS, svcS, selectedImage);
                        if (newMember != null) {
                            labelSignupError.setText("");
                            labelSignup.setText("Usuario creado");
                            bloqueoRegistro();

                            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
                            executorService.schedule(() -> {
                                Platform.runLater(() -> {
                                    Stage stage = (Stage) enviarRegistroButton.getScene().getWindow();
                                    stage.close();
                                });
                            }, 3, TimeUnit.SECONDS);


                            // ...
                        }
                    } catch (ClubDAOException e) {
                        labelSignup.setText("");
                        labelSignupError.setText("Nombre de usuario en uso.");
                    }
                }
            } else {
                try {
                    Member newMember = club.registerMember(nombreS, apellidoS, telefonoS, nicknameS, passwordS, null, 0, selectedImage);
                    if (newMember != null) {
                        labelSignupError.setText("");
                        labelSignup.setText("Usuario creado");
                        bloqueoRegistro();
                        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
                        executorService.schedule(() -> {
                            Platform.runLater(() -> {
                                // Obtener la ventana principal
                                Stage stage = (Stage) enviarRegistroButton.getScene().getWindow();
                                stage.close();
                            });
                        }, 3, TimeUnit.SECONDS);



                    }
                } catch (ClubDAOException e) {
                    labelSignup.setText("");
                    labelSignupError.setText("Nombre de usuario en uso.");
                }
            }
        } else {
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
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("src/img/avatars"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos PNG", "*.png"));
        File archivo = fileChooser.showOpenDialog(null);
        if (archivo != null) {
            String imagePath = archivo.toURI().toString();
            Image image = new Image(imagePath);
            imageUser.setImage(image);
            selectedImage = image; // Guarda la imagen seleccionada en selectedImage
        }
    }




    @FXML
    private void imageButtonOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.jpg", "*.jpeg", "*.png", "*.gif"));
        File archivo = fileChooser.showOpenDialog(null);
        if (archivo != null) {
            String imagePath = archivo.toURI().toString();
            Image image = new Image(imagePath);
            imageUser.setImage(image);
            selectedImage = image; // Guarda la imagen seleccionada en selectedImage
        }
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
        mostrarContraseña();
    }

    @FXML
    private void mostrarContraseñaOnMousePressed(MouseEvent event) {
        mostrarContraseñaPresionado = true;
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
}
