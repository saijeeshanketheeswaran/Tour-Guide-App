package sample;
/**
 * Created by 100589716 on 4/7/2017.
 */
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.image.*;

import java.io.*;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.MessageDigestSpi;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class LogInController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField fnameField;
    @FXML private PasswordField lnameField;
    @FXML private TextField emailField;
    private BorderPane layout;
    private String hashedPassword;
    public static final char BULLET = '\u2022';
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public void initialize() throws Exception{

    }
    public static boolean validate(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }
    public void cuser(ActionEvent event) throws Exception{
        Stage form = new Stage();
        GridPane editArea = new GridPane();
        editArea.setPadding(new Insets(10, 10, 10, 10));
        editArea.setVgap(10);
        editArea.setHgap(10);

        Label fnameLabel = new Label("First name:");
        editArea.add(fnameLabel, 0, 0);
        TextField fnameField = new TextField();
        editArea.add(fnameField, 1, 0);

        Label lnameLabel = new Label("Last name:");
        editArea.add(lnameLabel, 0, 1);
        TextField lnameField = new TextField();
        editArea.add(lnameField, 1, 1);

        Label emailLabel = new Label("E-Mail:");
        editArea.add(emailLabel, 0, 2);
        TextField emailField = new TextField();
        editArea.add(emailField, 1, 2);

        Label usernameLabel = new Label("Username:");
        editArea.add(usernameLabel, 0, 3);
        TextField usernameField = new TextField();
        editArea.add(usernameField, 1, 3);

        Label passwordLabel = new Label("Password:");
        editArea.add(passwordLabel, 0, 4);

        final TextField passwordField = new TextField();
        // Set initial state
        passwordField.setManaged(false);
        passwordField.setVisible(false);

        // Actual password field
        final PasswordField passwordField2 = new PasswordField();

        CheckBox checkBox = new CheckBox("Show/Hide password");
        // Bind properties. Toggle textField and passwordField
        // visibility and managability properties mutually when checkbox's state is changed.
        // Because we want to display only one component (textField or passwordField)
        // on the scene at a time.
        passwordField.managedProperty().bind(checkBox.selectedProperty());
        passwordField.visibleProperty().bind(checkBox.selectedProperty());

        passwordField2.managedProperty().bind(checkBox.selectedProperty().not());
        passwordField2.visibleProperty().bind(checkBox.selectedProperty().not());

        // Bind the textField and passwordField text values bidirectionally.
        passwordField.textProperty().bindBidirectional(passwordField2.textProperty());
        editArea.add(passwordField,1, 4);
        editArea.add(passwordField2, 1,4);
        editArea.add(checkBox, 1,6);


        Button addButton = new Button("Create User");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e){
                File file = new File("database.txt");
                try {
                    FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
                    BufferedWriter bw = new BufferedWriter(fw);

                    String username = usernameField.getText();
                    String password = passwordField.getText();
                    try {
                        MessageDigest md = MessageDigest.getInstance("SHA-256");
                        md.update(password.getBytes());

                        byte byteData[] = md.digest();
                        StringBuffer sb = new StringBuffer();
                        for (int i = 0; i < byteData.length; i++) {
                            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
                        }
                        hashedPassword = sb.toString();
                    }catch(java.security.NoSuchAlgorithmException E){System.out.println(E);}
                    String email = emailField.getText();
                    String firstName = fnameField.getText();
                    String lastName = lnameField.getText();
                    FileReader fr = new FileReader(file);
                    BufferedReader br = new BufferedReader(fr);
                    String line;
                    if (fnameField.getText().equals("")||lnameField.getText().equals("")||emailField.getText().equals("")|| usernameField.getText().equals("") || passwordField.getText().equals(""))
                    {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Entry/Entries are empty");

                        alert.showAndWait();
                        bw.write(" ");
                        bw.flush();
                        bw.close();
                        usernameField.setText("");
                        passwordField.setText("");
                        emailField.setText("");
                        fnameField.setText("");
                        lnameField.setText("");
                    }
                    if (validate(email) != true)
                    {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("E-mail not Valid");

                        alert.showAndWait();
                        bw.write(" ");
                        bw.flush();
                        bw.close();
                        usernameField.setText("");
                        passwordField.setText("");
                        emailField.setText("");
                        fnameField.setText("");
                        lnameField.setText("");
                    }
                    while ((line = br.readLine()) != null) {
                        if (line.trim().length() != 0) {
                            String[] dataFields = line.split("~");
                            for (int i = 0; i < dataFields.length; i++) {
                                if (dataFields[0].equals(firstName) || dataFields[1].equals(lastName))
                                {
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("Error Message");
                                    alert.setHeaderText(null);
                                    alert.setContentText("User has been registered into our system.");
                                    // + "if forgotten username" + " click on the forgot username link");

                                    alert.showAndWait();
                                    bw.write(" ");
                                    bw.flush();
                                    bw.close();
                                    usernameField.setText("");
                                    passwordField.setText("");
                                    emailField.setText("");
                                    fnameField.setText("");
                                    lnameField.setText("");
                                }
                                else if (dataFields[3].equals(username)) {
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("Error Message");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Username already taken");

                                    alert.showAndWait();
                                    bw.write(" ");
                                    bw.flush();
                                    bw.close();
                                    usernameField.setText("");
                                    passwordField.setText("");
                                    emailField.setText("");
                                    fnameField.setText("");
                                    lnameField.setText("");
                                }

                            }
                            //total += nextValue;
                            //count++;
                        }
                    }
                /*
                System.out.println(firstName);
                System.out.println(lastName);
                System.out.println(email);
                System.out.println(username);
                System.out.println(password);
                */
                    bw.write(firstName + "~" + lastName + "~" + email + "~" + username + "~" + hashedPassword + "\n");
                    usernameField.setText("");
                    passwordField.setText("");
                    emailField.setText("");
                    fnameField.setText("");
                    lnameField.setText("");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully created User, returning back to the main screen... ");
                    alert.showAndWait();
                    bw.flush();
                    bw.close();
                    form.close();
                }
                catch (java.io.IOException ioE){System.out.println(ioE);}

                //Scene mScreen = new Scene(, 600, 600);
            }
        });
        editArea.add(addButton, 24, 42);
        layout = new BorderPane();
        layout.setCenter(editArea);

        Scene scene = new Scene(layout, 600, 600);
        form.setScene(scene);
        form.show();
    }

    public void login(ActionEvent event) throws Exception{
        Stage login = new Stage();
        GridPane editArea = new GridPane();
        editArea.setPadding(new Insets(10, 10, 10, 10));
        editArea.setVgap(10);
        editArea.setHgap(10);

        Label usernameLabel = new Label("Username:");
        editArea.add(usernameLabel, 0, 1);
        TextField usernameField = new TextField();
        editArea.add(usernameField, 1, 1);

        Label passwordLabel = new Label("Password:");
        editArea.add(passwordLabel, 0, 2);
        TextField passwordField = new TextField();
        // Actual password field
        final PasswordField passwordField2 = new PasswordField();

        CheckBox checkBox = new CheckBox("Show/Hide password");
        // Bind properties. Toggle textField and passwordField
        // visibility and managability properties mutually when checkbox's state is changed.
        // Because we want to display only one component (textField or passwordField)
        // on the scene at a time.
        passwordField.managedProperty().bind(checkBox.selectedProperty());
        passwordField.visibleProperty().bind(checkBox.selectedProperty());

        passwordField2.managedProperty().bind(checkBox.selectedProperty().not());
        passwordField2.visibleProperty().bind(checkBox.selectedProperty().not());

        // Bind the textField and passwordField text values bidirectionally.
        passwordField.textProperty().bindBidirectional(passwordField2.textProperty());
        editArea.add(passwordField, 1, 2);
        editArea.add(passwordField2, 1,2);
        editArea.add(checkBox, 1,4);

        Button addButton = new Button("Log In");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

                String username = usernameField.getText();
                String password = passwordField.getText();
                try {
                    MessageDigest md = MessageDigest.getInstance("SHA-256");
                    md.update(password.getBytes());

                    byte byteData[] = md.digest();
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < byteData.length; i++) {
                        sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
                    }
                    hashedPassword = sb.toString();
                    //System.out.println(hashedPassword);
                }catch(java.security.NoSuchAlgorithmException E){System.out.println(E);}
                //System.out.println(username);
                //System.out.println(password);

                File database = new File("database.txt");
                try {
                    FileReader fr = new FileReader(database);
                    BufferedReader br = new BufferedReader(fr);
                    String line;
                    boolean in=false,wrongPass=false;
                    while ((line = br.readLine()) != null) {
                        if (line.trim().length() != 0) {
                            String[] Fields = line.split("~");
                            //System.out.println(Fields[3]);
                            //System.out.println(Fields[4]);
                            boolean isUser = Fields[3].equals(username);
                            boolean isPass = Fields[4].equals(hashedPassword);
                            if(usernameField.getText().equals("")|| passwordField.getText().equals(""))
                            {
                                wrongPass = true;
                            }
                            if (isUser && isPass) {
                                in=true;
                                wrongPass = false;
                                break;
                            }
                            else if (!isUser || !isPass){
                                wrongPass = true;
                            }
                        }
                    }
                    if(in){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("");
                        alert.setHeaderText(null);
                        alert.setContentText("Welcome to the App");
                        alert.showAndWait();
                        try {
                            Parent root = FXMLLoader.load(getClass().getResource("submenu.fxml"));
                            Scene nextScene = new Scene(root, 690,490) ;
                            login.setScene(nextScene);
                            login.setResizable(false);
                        }catch(java.io.IOException E){System.out.println(E);}
                    }
                    else if (wrongPass){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("");
                        alert.setHeaderText(null);
                        alert.setContentText("Log In Wasn't Successful");
                        alert.showAndWait();
                    }

                }catch (java.io.IOException IO){System.out.println(IO);}

                usernameField.setText("");
                passwordField.setText("");

            }
        });
        editArea.add(addButton, 24, 42);
        layout = new BorderPane();
        layout.setCenter(editArea);

        Scene scene = new Scene(layout, 600, 600);
        login.setScene(scene);
        login.show();
    }
}
