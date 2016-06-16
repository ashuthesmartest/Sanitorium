package com.controller.Dialog;

import com.DBConnect;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import org.controlsfx.dialog.Dialogs;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.security.SecureRandom;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author 404NotFound
 */
public class CodeSenderController implements Initializable 
{
    public static String code;
    public static String mail;
    
    @FXML
    private AnchorPane codePane;
    @FXML
    private TextField tfEmail;
    @FXML
    private Button btGo;
    private Properties mailServerProperties;
    private Session getMailSession;
    private MimeMessage generateMailMessage;
    Service<Void> mailService = new Service<Void>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    updateMessage("Setting up server...");
                    updateProgress(0, 100);
                    mailServerProperties = System.getProperties();
                    mailServerProperties.put("mail.smtp.port", "465");
                    updateProgress(10, 100);
                    mailServerProperties.put("mail.smtp.auth", "true");
                    updateProgress(20, 100);
                    mailServerProperties.put("mail.smtp.starttls.enable", "true");
                    updateProgress(30, 100);

                    updateMessage("Get Mail Session...");
                    getMailSession = Session.getDefaultInstance(mailServerProperties, null);
                    updateProgress(40, 100);
                    generateMailMessage = new MimeMessage(getMailSession);
                    updateProgress(50, 100);
                    generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(mail));
                    generateMailMessage.setSubject("Reset password");
                    String emailBody = "Your code: " + code + "<br><br> Regards, <br>Nguyen Hai Dang";
                    generateMailMessage.setContent(emailBody, "text/html");

                    updateMessage("Sending code...");
                    Transport transport = getMailSession.getTransport("smtp");
                    updateProgress(60, 100);
                    transport.connect("smtp.gmail.com", "ashu.the.smartest", "therevoltof1857");
                    updateProgress(80, 100);
                    transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
                    Notifications.create().title("").text("Code has been sent to your email").showInformation();
                    transport.close();
                    updateProgress(100, 100);
                    updateMessage("Code sent.");
                    return null;
                }
            };
        }
    };
    private Connection con;
    private PreparedStatement ps;
    private Statement st;
    private ResultSet rs;
    private List<String> emailList = new ArrayList<>();
    private SecureRandom random = new SecureRandom();
    private Thread t;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        code = new BigInteger(130, random).toString(32);
        createConnection();
    }

    private void createConnection() {
        try {
            String sql = "SELECT Email FROM APP_Account";
            rs = DBConnect.st.executeQuery(sql);
            while (rs.next()) {
                emailList.add(rs.getString("Email"));
            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBtGo(ActionEvent e) throws IOException {
        mail = tfEmail.getText();
        if (emailList.stream().anyMatch((email) -> email.equals(mail))) 
        {
            Dialogs.create().owner(codePane.getScene().getWindow()).title("Send Email").masthead("Sending Verification Code...").showWorkerProgress(mailService);
            mailService.start();
            try 
            {
                loadForgotScene();
                codePane.getScene().getWindow().hide();
            } 
            catch (IOException ex) 
            {
                ex.printStackTrace();
            }

        } else {
            Notifications.create().title("Error").text("Email doesn't exist").showError();
        }
    }

    private void loadForgotScene() throws IOException {
        // Load the fxml file and create a new stage for the popup dialog.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/view/Dialog/ForgotPasswordDialog.fxml"));
        AnchorPane page = loader.load();

        // Create the dialog Stage.
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Forgot Password");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        dialogStage.show();
    }

}
