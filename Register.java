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

    @FXML
    public void handleRegisterButtonAction() {
        String email = emailField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String passwordConfirm = passwordFieldConfirm.getText();
        loginMessage.setFill(Color.WHITE);
        loginMessage.setFont(Font.font("Verdana", 12));

        if(!checkArgs(email, username, password, passwordConfirm))
        {
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
                if(email.equals(credentials[0]))
                {
                    loginMessage.setText("Email address already exists!");
                    return;
                }

                if(username.equals(credentials[1]))
                {
                    loginMessage.setText("Username already exists!");
                    return;
                }
            }
            reader.close();
            File userInfo = new File("src/main/resources/database/" + username + "_info.db");
            BufferedWriter writer = new BufferedWriter(new FileWriter(userInfo, true));
            writer.write("0.0\n");
            writer.flush();
            writer.close();

            File userLibrary = new File("src/main/resources/database/" + username + "_library.db");
            writer = new BufferedWriter(new FileWriter(userLibrary, true));
            writer.flush();
            writer.close();

            Popup.Display("Registered", "Successfully registered!", "Log In");

            writer = new BufferedWriter(new FileWriter(userDatabase, true));
            String accountType = (devCheckBox.isSelected()) ? "dev" : "customer";
            writer.write(email + " " + username + " " + password + " " + accountType + "\n");
            writer.flush();

            writer.close();

            if(accountType.equals("dev"))
            {
                Teacher.openDev(username, (Stage) usernameField.getScene().getWindow());

                return;
            }
            else
            {
                Student.openCustomer(username, (Stage) usernameField.getScene().getWindow());

                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;

    }
}
