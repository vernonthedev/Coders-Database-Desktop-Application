package com.veike.crud;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.xml.transform.Result;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class HelloController implements Initializable {


    @FXML
    private BorderPane login_pane;

    @FXML
    private Button si_createAccountBtn;

    @FXML
    private TextField si_username;

    @FXML
    private PasswordField si_password;

    @FXML
    private Button si_loginBtn;

    @FXML
    private BorderPane signup_pane;

    @FXML
    private Button su_loginAccountBtn;

    @FXML
    private TextField su_username;

    @FXML
    private PasswordField su_password;

    @FXML
    private Button su_signupBtn;

    //LETS CONNECT TO OUR DATABASE
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    //the login function
    public void loginAccount(){
        String sql = "SELECT username, password FROM admin WHERE username = ? and password = ?";
        connect = Database.connect();

        try {
            Alert alert;

            //validate if any input has been entered
            if(si_username.getText().isEmpty() || si_password.getText().isEmpty()){
                //show an alert
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please Fill in the blank Fields");
                alert.showAndWait();
            }else{
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, si_username.getText());
                prepare.setString(2, si_password.getText());

                result = prepare.executeQuery();

                if(result.next()){
                    //IF CORRECT USERNAME AND PASSWORD
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successfully Logged In");
                    alert.setHeaderText(null);
                    alert.setContentText("Your login has been successful!");
                    alert.showAndWait();

                    //to hide the login form
                    si_loginBtn.getScene().getWindow().hide();

                    //change the root panel to that of the form after logon
                    Parent root = FXMLLoader.load(getClass().getResource("codersform.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();


                }else{
                    //IF WRONG USERNAME AND PASSWORD
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Login Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Please Enter Valid Information");
                    alert.showAndWait();
                }
            }




        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //the register method
    public void registerAccount(){
        String sql = "INSERT INTO admin (username, password) VALUES (?,?)";
        connect = Database.connect();

        try {
            Alert alert;

            //validate if any input has been entered
            if(su_username.getText().isEmpty() || su_password.getText().isEmpty()){
                //show an alert
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please Fill in the blank Fields");
                alert.showAndWait();
            }else{
                //validate username or check if it already exists in the system
                String checkData = "SELECT username FROM admin WHERE username = '" + su_username.getText()+"'";
                prepare = connect.prepareStatement(checkData);
                result = prepare.executeQuery();

                if(result.next()){
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText(su_username.getText() + " username is already Taken!");
                    alert.showAndWait();
                }else{
                    if(su_password.getText().length() < 8){
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Password has to be atleast 8 characters in length.");
                        alert.showAndWait();
                    }else{
                        prepare = connect.prepareStatement(sql);
                        prepare.setString(1,su_username.getText());
                        prepare.setString(2, su_password.getText());
                        prepare.executeUpdate();

                            alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Information Message");
                            alert.setHeaderText(null);
                            alert.setContentText("New Admin User Created Successfully");
                            alert.showAndWait();

                            login_pane.setVisible(true);
                            signup_pane.setVisible(false);

                            su_username.setText("");
                            su_password.setText("");

                    }
                }


            }


        }catch(Exception e){
            e.printStackTrace();
        }
    }


    //method to switch between the login form and the registration form
    public void switchForm(ActionEvent event){
        if(event.getSource() == su_loginAccountBtn){
            login_pane.setVisible(true);
            signup_pane.setVisible(false);
        }else if(event.getSource() == si_createAccountBtn){
            login_pane.setVisible(false);
            signup_pane.setVisible(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}