package com.controller.Tab;

import com.DBConnect;
import com.model.Doctor;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.controlsfx.control.Notifications;
import org.controlsfx.dialog.Dialogs;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class DoctorTabController implements Initializable 
{
    private ResultSet rs;
    
    @FXML
    private Button btSave;
    
    @FXML
    private TextField tfDoctorSearch;
    
    @FXML
    private Button btAddDoctor;
    
    @FXML
    private TableView<Doctor> doctorTable;
    
    @FXML
    private TableColumn doctorIdColumn;
    
    @FXML
    private TableColumn doctorNameColumn;
    
    @FXML
    private TableColumn departmentColumn;
    
    @FXML
    private TextField tfAddDoctorID;
    
    @FXML
    private TextField tfAddDoctorName;
    
    @FXML
    private TextField tfAddDepartment;
    
    private ObservableList<Doctor> doctorData = FXCollections.observableArrayList();
    private ObservableList<Doctor> filteredData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {
        initTable();
        setEditable();
        createConnection();
    }

    @FXML
    public void handleBtAddDoctor() 
    {
        if (tfAddDoctorID.getText().equals("") || tfAddDoctorName.getText().equals("") || tfAddDepartment.getText().equals("")) 
        {
            Notifications.create().title("Add Error").text("All Text Field must be filled!").showError();
        } 
        else if (isDuplicate(tfAddDoctorID.getText())) 
        {
            Notifications.create().title("Add Error").text("Duplicate DoctorID").showError();
        } 
        else 
        {
            String id = tfAddDoctorID.getText();
            String name = tfAddDoctorName.getText();
            String department = tfAddDepartment.getText();
            Doctor doctor = new Doctor();
            doctor.setIdProperty(id);
            doctor.setNameProperty(name);
            doctor.setDepartmentProperty(department);
            doctorData.add(doctor);
            try 
            {
                DBConnect.st.executeQuery("insert into app_doctor values('"+id+"','"+name+"','"+department+"',1,0)") ;        
            } 
            catch (SQLException ex) 
            {
                ex.printStackTrace();
            }

            tfAddDoctorID.clear();
            tfAddDoctorName.clear();
            tfAddDepartment.clear();
        }
    }

    @FXML
    public void handleBtDelete() {
        int selectedIndex = doctorTable.getSelectionModel().getSelectedIndex();
        String selectedID = doctorData.get(selectedIndex).getIdProperty();
        String delete = "DELETE FROM app_doctor WHERE ID = '"+selectedID+"'";
        try 
        {
            DBConnect.st.executeUpdate(delete) ;
        } 
        catch (SQLException ex) 
        {
            Dialogs.create()
                    .owner(doctorTable)
                    .title("Exception")
                    .masthead("There is an exception")
                    .message("Ooops, there was an exception!")
                    .showException(ex);
        }

        doctorData.remove(selectedIndex);
    }


    private void createConnection() {
        try 
        {
            String sql = "SELECT * FROM APP_Doctor";
            rs = DBConnect.st.executeQuery(sql);
            while (rs.next()) 
            {
                Doctor doctor = new Doctor();
                doctor.setIdProperty(rs.getString(1));
                doctor.setNameProperty(rs.getString(2));
                doctor.setDepartmentProperty(rs.getString(3));
                doctorData.add(doctor);
            }
        } 
        catch (Exception ex) 
        {
        
        } 
    }

    public void initTable() {
        doctorIdColumn.setCellValueFactory(new PropertyValueFactory<>("IdProperty"));
        
        doctorNameColumn.setCellValueFactory(new PropertyValueFactory<>("nameProperty"));
        
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("departmentProperty"));

        //Create fillter for search field
        FilteredList<Doctor> filteredData = new FilteredList<>(doctorData, p -> true);
        doctorTable.setItems(filteredData);
        tfDoctorSearch.textProperty().addListener((observable, oldValue, newValue) -> 
        {
            filteredData.setPredicate(doctor -> 
            {
                // If filter text is empty, display all
                if (newValue == null || newValue.isEmpty()) 
                {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                        if (doctor.getIdProperty().toLowerCase().contains(lowerCaseFilter)) 
                        {
                            return true;
                        }
                        else if (doctor.getNameProperty().toLowerCase().contains(lowerCaseFilter)) 
                        {
                            return true;
                        }
                        else if (doctor.getDepartmentProperty().toLowerCase().contains(lowerCaseFilter)) 
                        {
                            return true;
                        }
                return false;
            });
        });
    }

    public void setEditable() {
        doctorTable.setEditable(true);
        doctorIdColumn.setEditable(true);
        doctorIdColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        doctorIdColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Doctor, String>>() 
        {
            @Override
            public void handle(TableColumn.CellEditEvent<Doctor, String> t) 
            {
                Doctor doctor = t.getTableView().getItems().get(t.getTablePosition().getRow());
                if (t.getNewValue().length() == 0) 
                {
                    forceRefresh(); // Regain CONTROL for an UNDO
                    Notifications.create().title("Edit Error").text("Field cannot be empty!").showError();
                } 
                else if (isDuplicate(t.getNewValue())) 
                {
                    forceRefresh();
                    Notifications.create().title("Add Error").text("Duplicate DoctorID").showError();
                } 
                else 
                {
                    String update = "UPDATE app_doctor SET ID = ? WHERE Name = ?";
              //      DBConnect.st.executeUpdate(update) ;
                    doctor.setIdProperty(t.getNewValue());
                }
            }
        });
        doctorNameColumn.setEditable(true);
        doctorNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        doctorNameColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Doctor, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Doctor, String> t) {
                Doctor doctor = t.getTableView().getItems().get(t.getTablePosition().getRow());
                if (t.getNewValue().length() == 0) {
                    forceRefresh(); // Regain CONTROL for an UNDO
                    Notifications.create().title("Edit Error")
                            .text("Field cannot be empty!")
                            .showError();
                } else {
                    String update = "UPDATE app_doctor SET Name = '"+t.getNewValue()+"' WHERE ID = '"+doctor.getIdProperty()+"'"  ;
                    try 
                    {
                        DBConnect.st.executeUpdate(update) ;
                    } catch (SQLException ex) {
                        Dialogs.create()
                                .owner(doctorTable)
                                .title("Exception")
                                .masthead("There is an exception")
                                .message("Ooops, there was an exception!")
                                .showException(ex);
                    }
                    doctor.setNameProperty(t.getNewValue());
                }
            }
        });
        departmentColumn.setEditable(true);
        departmentColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        departmentColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Doctor, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Doctor, String> t) {
                Doctor doctor = t.getTableView().getItems().get(t.getTablePosition().getRow());
                if (t.getNewValue().length() == 0) {
                    forceRefresh(); // Regain CONTROL for an UNDO
                    Notifications.create().title("Edit Error")
                            .text("Field cannot be empty!")
                            .showError();
                } else {
                    String update = "UPDATE app_doctor SET Department = '"+t.getNewValue()+"' WHERE ID = '"+doctor.getIdProperty()+"'"  ;
                    try {
                        DBConnect.st.executeUpdate(update) ;
                    } catch (SQLException ex) {
                        Dialogs.create()
                                .owner(doctorTable)
                                .title("Exception")
                                .masthead("There is an exception")
                                .message("Ooops, there was an exception!")
                                .showException(ex);
                    }
                    doctor.setDepartmentProperty(t.getNewValue());
                }
            }
        });
    }

    public void forceRefresh() {
        new java.util.Timer().schedule(new java.util.TimerTask() {
            public void run() {
                Platform.runLater(() -> {
                    TableView<Doctor> docTab;
                    docTab = doctorTable;
                    docTab.getColumns().get(0).setVisible(false);
                    docTab.getColumns().get(0).setVisible(true);
                });
            }
        }, 50);
    }

    public boolean isDuplicate(String s) {
        for (Doctor doctor : doctorData) {
            if (s.equals(doctor.getIdProperty())) {
                return true;
            }
        }
        return false;
    }
}
