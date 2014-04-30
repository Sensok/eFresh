package efresh.desktop;

import efresh.service.RSA;
import efresh.desktop.Main;
import efresh.system.Starter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.math.BigInteger;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import javafx.stage.Stage;

/**
 * Allows the user to create a new account
 * @author Adam Harper
 */
public class CreateAccount
{
   /**
    * String holding the current user
    */
   private String mUsername;

   /**
    * String holding the encrypted password of the current user
    */
   private String mPassword;

   /**
    * A new stage to hold the createAccount GUI
    */
   private Stage primaryStage;

   /**
    * String holding the value needed to access dropbox
    */
   private String dropBoxURL;

   /**
    * This is the pass code for the teacher access
    */
   private String passCodeValue;

   /**
    * The user name
    */
   public static String user;

   /**
    * Creates a new CreateAccount object.
    */
   CreateAccount()
   {

      passCodeValue = "12345";
      dropBoxURL = (System.getProperty("user.home") + System.getProperty("file.separator")
                    + "efresh-tmp" + System.getProperty("file.separator") + "login.info");
      primaryStage = new Stage();

      GridPane grid = new GridPane();
      grid.setAlignment(Pos.CENTER);
      grid.setHgap(10);
      grid.setVgap(10);
      grid.setPadding(new Insets(25, 25, 25, 25));

      Text scenetitle = new Text("  GERSII Create Account");
      scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
      grid.add(scenetitle, 0, 0, 2, 1);

      Label userName = new Label("User Name:");
      grid.add(userName, 0, 1);

      final TextField userTextField = new TextField();
      grid.add(userTextField, 1, 1);

      Label pw = new Label("Password:");
      grid.add(pw, 0, 2);

      final PasswordField pwBox = new PasswordField();
      grid.add(pwBox, 1, 2);

      Label passCode = new Label("Pass Code:");
      grid.add(passCode, 0, 3);

      final PasswordField passCodeBox = new PasswordField();
      grid.add(passCodeBox, 1, 3);

      Button btn = new Button("Cancel");
      Button create = new Button("Create");

      HBox hbBtn = new HBox(10);
      hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
      hbBtn.getChildren().addAll(create, btn);
      grid.add(hbBtn, 1, 4);

      final Text actiontarget1 = new Text();
      grid.add(actiontarget1, 1, 6);

      final Text actiontarget2 = new Text();
      grid.add(actiontarget2, 1, 7);

      create.setOnAction(new EventHandler<ActionEvent>()
         {
            @Override
            public void handle(ActionEvent e)
            {
               boolean test = true;

               if (userTextField.getText().equals("") ||
                      pwBox.getText().equals(""))
               {
                  actiontarget1.setFill(Color.FIREBRICK);
                  actiontarget1.setText("Missing Information");
                  test = false;
               }

               if (! passCodeBox.getText().equals(passCodeValue))
               {
                  test = false;
                  actiontarget2.setFill(Color.FIREBRICK);
                  actiontarget2.setText("Wrong Passcode");
               }

               if (test)
               {
                  //run RSA
                  BigInteger N = new BigInteger("41");
                  user = new RSA(N).prepare(userTextField.getText()).toString();

                  String pass = new RSA(N).prepare(pwBox.getText()).toString();

                  if (checkForDuplicates(user, pass))
                  {
                     actiontarget1.setFill(Color.FIREBRICK);
                     actiontarget1.setText("Account Already Exists!");
                  }
                  else
                  {
                     addToFile(user, pass);
                  }
               }
            }

            /**
             * This will add a new user to the Login.info file
             */
            private void addToFile(String user, String pass)
            {
               Login.userTextField.setText(userTextField.getText());
               Login.pwBox.setText(pwBox.getText());

               String full = user + ":::" + pass;
               String test =
                  System.getProperty("user.home") + System.getProperty("file.separator")
                  + "efresh-tmp" + System.getProperty("file.separator") + "Login.info";

               try
               {
                  new Starter(user);

                  PrintWriter out =
                     new PrintWriter(new BufferedWriter(
                           new FileWriter(test, true)));
                  out.println(full);
                  out.close();
                  primaryStage.close();
               }
               catch (IOException e)
               {
               }
            }
         });

      btn.setOnAction(new EventHandler<ActionEvent>()
         {
            @Override
            public void handle(ActionEvent e)
            {
               primaryStage.close();
            }
         });

      Scene scene = new Scene(grid, 375, 375);
      primaryStage.setScene(scene);
      primaryStage.show();
   }

   /**
    * Checks for duplicates int the login info
    *
    * @param mUser user name encrypted
    * @param mPass password encrypted
    *
    * @return boolean - Returns a boolean of whether or not
    * the User name is taken
    */
   public boolean checkForDuplicates(String mUser, String mPass)
   {
      String fullUser = mUser + ":::" + mPass;
      BufferedReader buff;
      File selectedFile =
         new File(System.getProperty("user.home") + System.getProperty("file.separator")
                  + "efresh-tmp" + System.getProperty("file.separator") + "Login.info");

      try
      {
         buff = new BufferedReader(new FileReader(selectedFile));

         String parseLine = "";

         while ((parseLine = buff.readLine()) != null)
         {
            if (parseLine.equals(fullUser))
            {
               return true;
            }
         }
      }
      catch (Exception e)
      {
      }

      return false;
   }
}
