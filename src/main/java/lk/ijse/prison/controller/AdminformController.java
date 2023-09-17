package lk.ijse.prison.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.prison.bo.BoFactory;
import lk.ijse.prison.bo.coustom.impl.AdminBOImpl;
import lk.ijse.prison.dto.AdminDTO;

import java.io.IOException;
import java.sql.SQLException;

public class AdminformController {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private TextField oldPassword;
    @FXML
    private AnchorPane adminanchor;

    AdminBOImpl adminBO = BoFactory.getBoFactory().getBO(BoFactory.BOTypes.ADMIN);
    @FXML
    private void btnlsaveOnaction(ActionEvent actionEvent) {
        String olduser = oldPassword.getText();
        String usename = username.getText();
        String pass = password.getText();
        if (!oldPassword.getText().isBlank() && !username.getText().isBlank() && !password.getText().isBlank()) {
            try {
                boolean exist = adminBO.checkPassword(olduser);
                if (exist) {
                    boolean isUpdated = adminBO.Update(new AdminDTO(usename, pass), olduser);
                    if (isUpdated) {
                        new Alert(Alert.AlertType.CONFIRMATION, "Username & Password is updated!").show();
                        oldPassword.setText("");
                        username.setText("");
                        password.setText("");
                    } else if (isUpdated == false) {
                        new Alert(Alert.AlertType.CONFIRMATION, "Username & Password Not updated!").show();
                    }
                }else { new Alert(Alert.AlertType.ERROR, "Invaild Password!").show();}
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Please Fill Details!").show();
        }
    }

    @FXML
    private void btnlbackOnaction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) adminanchor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/dashboard.fxml"))));
        stage.setTitle("Dashboard");
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    private void btnlogoutOnaction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) adminanchor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/loginform.fxml"))));
        stage.setTitle("Login");
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    private void oldPassOnAction(ActionEvent actionEvent) {
        if (!oldPassword.getText().isBlank()){
            detailTxt();
        }
    }
    @FXML
    private void usernameONAction(ActionEvent actionEvent) {
        if (!username.getText().isBlank()){
            detailTxt();
        }
    }
    @FXML
    private void passOnAction(ActionEvent actionEvent) {
        if (!password.getText().isBlank()){
            detailTxt();
        }
    }

    @FXML
    private void detailTxt() {
        int emptyFieldIndex = -1;
        switch (emptyFieldIndex) {
            case -1:
                if (oldPassword.getText().isBlank()) {
                    oldPassword.requestFocus();
                    emptyFieldIndex = 0;
                }
            case 0:
                if (emptyFieldIndex < 0 && (username.getText().isBlank())) {
                    username.requestFocus();
                    emptyFieldIndex = 1;
                }
            case 1:
                if (emptyFieldIndex < 0 && (password.getText().isBlank())) {
                    password.requestFocus();
                    emptyFieldIndex = 2;
                }
                break;
            default:
                break;
        }
    }
}
