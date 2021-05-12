package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Teacher;
import model.Student;

import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class Login {

    @FXML
    public Text loginMessage;
    @FXML
    public PasswordField passwordField;
    @FXML
    public TextField usernameField;

    @FXML
    public void handleLoginButtonAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        loginMessage.setFill(Color.WHITE);
        loginMessage.setFont(Font.font("Verdana", 12));

        if (username == null || username.isEmpty()) {
            loginMessage.setText("Please type in a username!");
            return;
        }

        if (password == null || password.isEmpty()) {
            loginMessage.setText("Password cannot be empty");
            return;
        }

        // see if user is registered by checking the database file
        BufferedReader reader;
        try {
            File userDatabase = new File("src/main/resources/database/users.db");
            reader = new BufferedReader(new FileReader(
                    userDatabase));
            String line;
            while ((line = reader.readLine()) != null) {
                // read next line
                String[] credentials = line.split(" ");
                if(credentials[1].equals(username) && credentials[2].equals(password))
                {
                    if(credentials[3].equals("dev"))
                    {
                        Teacher.openDev(username, (Stage) usernameField.getScene().getWindow());

                        return;
                    }
                    if(credentials[3].equals("student")) {
                        Student.openCustomer(username, (Stage) usernameField.getScene().getWindow());

                        return;
                    }

                    return;
                }
                else if(credentials[1].equals(username) && !credentials[2].equals(password))
                {
                    // we found the user but he entered a wrong password
                    loginMessage.setText("Wrong password!");
                    return;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        loginMessage.setText("Invalid username!");
        return;

    }

    public void handleRegisterButtonAction() {
        try {
            Stage stage = (Stage) loginMessage.getScene().getWindow();
            Parent registerScreen = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/register.fxml"));
            Scene scene = new Scene(registerScreen, 1600, 1000);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
