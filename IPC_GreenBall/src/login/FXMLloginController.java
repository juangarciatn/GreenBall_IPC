/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package login;

import application.GreenBallApplication;
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
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import main.FXMLmainController;
import signup.FXMLsignupController;

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
    @FXML
    private Button cancelarLogin;
    @FXML
    private Text registro;
    
    private static boolean okPressed = false;
    private static String username;
    private static String passw;
    @FXML
    private ToggleButton mostrarContraseña;
    
    private boolean mostrarContraseñaPresionado = false;
    
    private Image closeEye;
    private Image openEye;
    private ImageView closeEyeView;
    private ImageView openEyeView;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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
    private void enviarLoginOnAction(ActionEvent event) throws ClubDAOException, IOException, InterruptedException {
        String usuario = user.getText();
        String clave = password.getText();
        FXMLmainController mc = new FXMLmainController();
        if (user.getText().length() != 0 && password.getText().length() != 0) {
            labelLogin.setText("");
            Club club = Club.getInstance();
            try {
                if (club.getMemberByCredentials(usuario, clave) != null) {
                    okPressed = true;
                    username = user.getText();
                    passw = password.getText();
                    Stage stage = (Stage) enviarLoginButton.getScene().getWindow();
                    stage.close();
                } else {
                    labelLogin.setText("El usuario o la contraseña no existen");
                }
            } catch (NullPointerException e) {
                labelLogin.setText("El usuario o la contraseña no existen");
            }
        } else {
            labelLogin.setText("Debes rellenar los campos obligatorios");
        }
    }


    @FXML
    private void cancelarLoginOnAction(ActionEvent event) throws IOException {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void registroLogin(MouseEvent event) throws IOException {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("/signup/FXMLsignup.fxml"));
        Parent root = miCargador.load();
        // acceso al controlador de datos persona
        FXMLsignupController controladorSignup = miCargador.getController();
        Scene scene = new Scene(root,500,300);
        Stage stageRegistro = new Stage();
        stageRegistro.setScene(scene);
        stageRegistro.initStyle(StageStyle.UNDECORATED);
        stageRegistro.setTitle("Registrarse");
        stageRegistro.setMinHeight(400);
        stageRegistro.setMinWidth(600);
        stageRegistro.initModality(Modality.WINDOW_MODAL); 
        stageRegistro.show();
    }
    
    public static void setIsOk(boolean s) {
        okPressed = s;
    }
    
    public static boolean isOk() {
        return okPressed;
    }
    
    public static String getUsername() {
        return username;
    }

    @FXML
    private void enterLogin(KeyEvent event) throws ClubDAOException, IOException, InterruptedException {
        if(event.getCode() == KeyCode.ENTER){
            ActionEvent ac1 = new ActionEvent();
            enviarLoginOnAction(ac1);
        }
    }
    
    public static String getPassword() {
        return passw;
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
}
