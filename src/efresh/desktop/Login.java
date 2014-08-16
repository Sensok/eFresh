package efresh.desktop;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import efresh.service.RSA;
import efresh.service.StringSplit;
import efresh.service.Upload;

import efresh.system.Download;
import efresh.system.Starter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.math.BigInteger;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.application.Platform;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

/**
 * This will allow the user to login into the program
 * and will also allow the user to create new accounts
 * @author Adam Harper and Jordan Jensen
 */
public class Login
   extends Application
{
   /**
    * This is the Window for the Login to be in
    */
   public static Window mWindow;

   /**
    * This is the user name of the user
    */
   public static String username;

   /**
    * This holds the password of the user
    */
   public static String password;

   /**
    * This is the main stage for the program
    */
   public static Stage mStage;

   /**
    * This is a text field to hold the users name
    */
   public static TextField userTextField;

   /**
    * This is a password text field that will display
    * the dots and not the password
    */
   public static PasswordField pwBox;

   /**
    * Instantiates the Info for the rest of the program to use
    */
   public static DbxClient dropBox;

   /**
    * This will launch the GUI
    *
    * @param args These are the args coming in from command line
    */
   public static void main(String[] args)
   {
      launch(args);
   }

   /**
    * Creates all the necessary classes to run the
    * Login and then calls the Main class
    *
    * @param primaryStage current Stage for the JavaFX Application
    */
   @Override
   public void start(Stage primaryStage)
   {
      initDropbox();
      mStage = primaryStage;
      primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>()
         {
            public void handle(WindowEvent event)
            {
               new Upload(dropBox);
               event.consume();

               try
               {
                  Runtime.getRuntime().addShutdownHook(new Thread()
                     {
                        @Override
                        public void run()
                        {
                           System.out.println("Folder Deleted");

                           File dir =
                              new File(System.getProperty("user.home") +
                                 System.getProperty("file.separator") + "efresh-tmp");

                           if (dir.exists())
                           {
                           }
                        }
                     });
                  Platform.exit();
                  System.exit(0);
               }
               catch (Exception e)
               {
                  System.out.println(e.getMessage());
               }
            }
         });

      new Starter();

      try
      {
         new Download(dropBox);
      }
      catch (Exception e)
      {
         System.out.println("Error " + e);
      }

      primaryStage.setTitle("GERSII Login Screen");

      GridPane grid = new GridPane();
      grid.setAlignment(Pos.CENTER);
      grid.setHgap(10);
      grid.setVgap(10);
      grid.setPadding(new Insets(25, 25, 25, 25));

      Text scenetitle = new Text("  GERSII");
      scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
      grid.add(scenetitle, 0, 0, 2, 1);

      Label userName = new Label("User Name:");
      grid.add(userName, 0, 1);

      userTextField = new TextField();
      grid.add(userTextField, 1, 1);

      Label pw = new Label("Password:");
      grid.add(pw, 0, 2);

      pwBox = new PasswordField();
      grid.add(pwBox, 1, 2);

      Button btn = new Button("Sign in");
      Button create = new Button("Create Account");

      final ProgressIndicator p1 = new ProgressIndicator();
      p1.setMinWidth(85);
      p1.setMinHeight(85);
      p1.setVisible(false);

      HBox hbBtn = new HBox(10);
      hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
      hbBtn.getChildren().addAll(btn, create);
      grid.add(hbBtn, 1, 4);

      grid.add(p1, 0, 0);

      final Text actiontarget = new Text();

      grid.add(actiontarget, 1, 6);

      btn.setOnAction(new EventHandler<ActionEvent>()
         {
            @Override
            public void handle(ActionEvent e)
            {
               if (! userTextField.getText().equals("") &&
                      ! pwBox.getText().equals(""))
               {
                  // p1.setVisible(true);
                  BigInteger N = new BigInteger("41");
                  RSA key = new RSA(N);
                  BigInteger userNameRSA =
                     key.encrypt(key.prepare(userTextField.getText()));
                  BigInteger passwordRSA =
                     key.encrypt(key.prepare(pwBox.getText()));

                  boolean loginOkay = false;
                  BufferedReader buff;
                  File selectedFile =
                     new File(System.getProperty("user.home") +
                        System.getProperty("file.separator") + "efresh-tmp" +
                              System.getProperty("file.separator") + "Login.info");

                  try
                  {
                     buff = new BufferedReader(new FileReader(selectedFile));

                     String parseLine = "";

                     while ((parseLine = buff.readLine()) != null)
                     {
                        StringSplit splitTest = new StringSplit(parseLine);

                        if (compare(key, splitTest.getUser(), userNameRSA) &&
                               compare(key, splitTest.getPass(), passwordRSA))
                        {
                           username = splitTest.getUser();
                           password = splitTest.getPass();

                           loginOkay = true;

                           break;
                        }
                     }
                  }
                  catch (Exception b)
                  {
                  }

                  if (loginOkay)
                  {
                      try {
                          new Main(userTextField.getText(), username);
                      } catch (DbxException ex) {
                          Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                      } catch (IOException ex) {
                          Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                      }
                  }
               }

               userTextField.setText("");
               pwBox.setText("");
               actiontarget.setFill(Color.FIREBRICK);
               actiontarget.setText("Invalid User/Password");
            }

            private boolean compare(RSA pKey, String pString,
               BigInteger pBigInteger)
            {
               BigInteger tester = pKey.encrypt(new BigInteger(pString));

               return pKey.decrypt(tester).equals(pKey.decrypt(pBigInteger));
            }
         });

      create.setOnAction(new EventHandler<ActionEvent>()
         {
            @Override
            public void handle(ActionEvent e)
            {
               CreateAccount acct = new CreateAccount();
            }
         });

      Scene scene = new Scene(grid, 425, 425);
      primaryStage.setScene(scene);
      primaryStage.show();
   }

    private void initDropbox() {
        String accessToken = new String("CH7MRSYzE7MAAAAAAAAAB31ANHyUmV2hPR5_xKu84yU0I_jM4C1KTTxx3m00aqJv");
        DbxRequestConfig config = new DbxRequestConfig("MiniPierre", Locale.getDefault().toString());
        dropBox = new DbxClient(config, accessToken);
    }
}
