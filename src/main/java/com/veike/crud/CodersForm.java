package com.veike.crud;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class CodersForm implements Initializable {


    @FXML
    private TextField crud_studentid;
    @FXML
    private TableView<codersData> crud_tableView;

    @FXML
    private TextField crud_fullname;

    @FXML
    private ComboBox<?> crud_year;

    @FXML
    private ComboBox<?> crud_course;

    @FXML
    private Button crud_addBtn;

    @FXML
    private Button crud_updateBtn;

    @FXML
    private Button crud_clearBtn;

    @FXML
    private Button crud_deleteBtn;

    @FXML
    private ComboBox<?> crud_gender;

    @FXML
    private TableColumn<codersData, String> crud_col_id;

    @FXML
    private TableColumn<codersData, String> crud_col_fullname;

    @FXML
    private TableColumn<codersData, String> crud_col_year;

    @FXML
    private TableColumn<codersData, String> crud_col_course;

    @FXML
    private TableColumn<codersData, String> crud_col_gender;

    private String[] yearList = {
            "1st Year",
            "2nd Year",
            "3rd Year",
            "4th Year"
    };
    //the year list method
    public void codersYearList(){
        List<String> yList = new ArrayList<>();
        for(String data: yearList){
            yList.add(data);
        }
        ObservableList listData = FXCollections.observableArrayList(yList);
        crud_year.setItems(listData);
    }

    private String[] courseList = {
            "Computer Science",
            "Software Engineering",
            "Video Editing",
            "Machine Learning",
            "Data Science"
    };
    public void codersCourseList(){
        List<String> cList = new ArrayList<>();
        for(String data: courseList){
            cList.add(data);
        }
        ObservableList listData = FXCollections.observableArrayList(cList);
        crud_course.setItems(listData);
    }

    private String[] genderList ={
            "Male",
            "Female"
    };
    public void codersGenderList(){
        List<String> gList = new ArrayList<>();
        for(String data: genderList){
            gList.add(data);
        }
        ObservableList listData = FXCollections.observableArrayList(gList);
        crud_gender.setItems(listData);
    }
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Statement statement;
    private Alert alert;

    public void studentAddBtn(){

        connect = Database.connect();
        try{
            //first check if u have not recieved null values
            if(crud_studentid.getText().isEmpty() ||
            crud_fullname.getText().isEmpty() ||
            crud_course.getSelectionModel().getSelectedItem() == null ||
            crud_year.getSelectionModel().getSelectedItem() == null ||
            crud_gender.getSelectionModel().getSelectedItem() == null){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                alert.setHeaderText(null);
                alert.setContentText("Please Fill All the Blank Fields");
                alert.showAndWait();
            }else {
                //check if the data already exists
                String checkData = "SELECT coder_number FROM coders_info WHERE coder_number = "+ crud_studentid.getText();
                prepare = connect.prepareStatement(checkData);
                result = prepare.executeQuery();
                if(result.next()){
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Database Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Coders Number "+ crud_studentid.getText() + " is already taken");
                    alert.showAndWait();
                }else{
                    String insertData = "INSERT INTO coders_info " +
                            "(coder_number, full_name, year, course, gender, date)"+
                            " VALUES(?,?,?,?,?,?)";
                    prepare = connect.prepareStatement(insertData);
                    prepare.setString(1, crud_studentid.getText());
                    prepare.setString(2, crud_fullname.getText());
                    prepare.setString(3, (String) crud_year.getSelectionModel().getSelectedItem());
                    prepare.setString(4, (String) crud_course.getSelectionModel().getSelectedItem());
                    prepare.setString(5, (String) crud_gender.getSelectionModel().getSelectedItem());

                    //get the current date and time
                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                    //set the date now
                    prepare.setString(6, String.valueOf(sqlDate));

                    //finally update the table
                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully added!");
                    alert.showAndWait();

                    //to update the table view
                    coderShowData();
                    //to clear the data
                    codersClearBtn();
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    //update method
    public void codersUpdateBtn(){
        connect = Database.connect();
        try{
            //first check if u have not recieved null values
            if(crud_studentid.getText().isEmpty()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                alert.setHeaderText(null);
                alert.setContentText("Please Fill All the Blank Fields");
                alert.showAndWait();
            }else {

                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE coders ID "+ crud_studentid.getText() + '?');
                Optional<ButtonType> option = alert.showAndWait();

                if(option.get().equals(ButtonType.OK)){
                    String updateData = "UPDATE coders_info SET "+
                            "full_name = '" + crud_fullname.getText()+
                            "', year = '"+ crud_year.getSelectionModel().getSelectedItem()+
                            "', course = '"+ crud_course.getSelectionModel().getSelectedItem()+
                            "', gender = '"+ crud_gender.getSelectionModel().getSelectedItem()+
                            "' WHERE coder_number = "+ crud_studentid.getText();

                    prepare = connect.prepareStatement(updateData);
                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Information Updated Successfully!");
                    alert.showAndWait();

                    //to update the table view
                    coderShowData();
                    //to clear the data
                    codersClearBtn();
                }else{
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Operation Cancelled!");
                    alert.showAndWait();
                }


            }
        }catch (Exception e){e.printStackTrace();}

    }
    public void codersDeleteBtn(){

        connect = Database.connect();
        try{
            //first check if u have not recieved null values
            if(crud_studentid.getText().isEmpty()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                alert.setHeaderText(null);
                alert.setContentText("Please Fill All the Blank Fields");
                alert.showAndWait();
            }else {

                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to DELETE coders ID "+ crud_studentid.getText() + '?');
                Optional<ButtonType> option = alert.showAndWait();

                if(option.get().equals(ButtonType.OK)){
                    String deleteData = "DELETE FROM coders_info WHERE coder_number = "+ crud_studentid.getText();

                    prepare = connect.prepareStatement(deleteData);
                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Information Deleted Successfully!");
                    alert.showAndWait();

                    //to update the table view
                    coderShowData();
                    //to clear the data
                    codersClearBtn();
                }else{
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Operation Cancelled!");
                    alert.showAndWait();
                }


            }
        }catch (Exception e){e.printStackTrace();}

    }
    public void codersClearBtn(){
        crud_studentid.setText("");
        crud_fullname.setText("");
        crud_gender.getSelectionModel().clearSelection();
        crud_course.getSelectionModel().clearSelection();
        crud_year.getSelectionModel().clearSelection();
    }

    //create an observable list
    public ObservableList<codersData> codersListData(){
        ObservableList<codersData> listData = FXCollections.observableArrayList();
        String selectData = "SELECT * FROM coders_info";
        connect = Database.connect();

        try{
            prepare = connect.prepareStatement(selectData);
            result = prepare.executeQuery();

            codersData sData;

            while(result.next()){
                sData = new codersData(
                        result.getInt("coder_number"),
                        result.getString("full_name"),
                        result.getString("year"),
                        result.getString("course"),
                        result.getString("gender"));

                listData.add(sData);


            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<codersData> codersData;
    public void coderShowData(){
        codersData = codersListData();

        crud_col_id.setCellValueFactory(new PropertyValueFactory<>("coders_id"));
        crud_col_fullname.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        crud_col_year.setCellValueFactory(new PropertyValueFactory<>("year"));
        crud_col_course.setCellValueFactory(new PropertyValueFactory<>("course"));
        crud_col_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));

        crud_tableView.setItems(codersData);
    }

    public void selectCodersData(){
        codersData sData = crud_tableView.getSelectionModel().getSelectedItem();
        int num = crud_tableView.getSelectionModel().getSelectedIndex();
        crud_fullname.setText(sData.getFullname());

        if((num - 1) < - 1) return;

        crud_studentid.setText(String.valueOf(sData.getCoders_id()));
        crud_fullname.setText(sData.getFullname());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        codersYearList();
        codersGenderList();
        codersCourseList();
        coderShowData();

    }
}
