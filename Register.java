package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.Teacher;
import screen.Popup;
import javafx.stage.Stage;
import model.Student;
//import model.Teacher;
//import model.User;

import java.io.*;

public class Register {
    @FXML
    public Text loginMessage;
    @FXML
    public PasswordField passwordField;
    @FXML
    public PasswordField passwordFieldConfirm;
    @FXML
    public TextField usernameField;
    @FXML
    public TextField emailField;
    @FXML
    public CheckBox devCheckBox;

    private boolean checkArgs(String email, String username, String password, String passwordConfirm)
    {
        if (email == null || email.isEmpty()) {
            loginMessage.setText("Please type in an e-mail address!");
            return false;
        }

        if(!email.contains("@") || email.contains(" "))
        {
            loginMessage.setText("Wrong e-mail format!");
            return false;
        }
        if(email.split("@").length != 2)
        {
            loginMessage.setText("Wrong e-mail format!");
            return false;
        }

        if (username == null || username.isEmpty()) {
            loginMessage.setText("Please type in a username!");
            return false;
        }

        if(username.contains(" "))
        {
            loginMessage.setText("Username cannot contain spaces!");
            return false;
        }

        if (password == null || password.isEmpty()) {
            loginMessage.setText("Password cannot be empty");
            return false;
        }

        if(password.contains(" "))
        {
            loginMessage.setText("Password cannot contain spaces!");
            return false;
        }

        if (passwordConfirm == null || passwordConfirm.isEmpty()) {
            loginMessage.setText("Password confirmation needed!");
            return false;
        }

        if(!password.equals(passwordConfirm))
        {
            loginMessage.setText("Password confirmation does not match!");
            return false;
        }

        return true;
    }

}
