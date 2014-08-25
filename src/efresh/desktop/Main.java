package efresh.desktop;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import static efresh.desktop.Login.dropBox;
import efresh.service.Update;
import efresh.service.Upload;
import efresh.system.Download;
import efresh.system.RemoveTempFiles;

import efresh.system.Starter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * This is the main GUI that will show after the login
 * @author Adam Harper and Jordan Jensen
 */
public final class Main
   extends Application
{
   /**
    * Type of user that is running the program
    */
   public static String mUser;

   /**
    * The stage for the program
    */
   public static Stage mStage;

   /**
    * Path separator
    */
   public static String mPathSep;

   /**
    * The current scene for the Main program
    */
   private Scene mScene;

   /**
    * the VBOX that holds everything
    */
   private VBox mRoot;

   /**
    * The MenuBar for the user to see what is available for the gui
    */
   private MenuBar menuBar;

    /**
    * Used to access other tabPanes.
    */
   public static TabPane tabPane;

   /**
    * Used to store all tab panes for the Main GUI
    */
   public BorderPane mainPane;

   /**
    * Games tab to show the games class
    */
   public static Tab gamesTab;

   /**
    * Random tab to show the randomizer class
    */
   public static Tab randomTab;

   /**
    * Student tab to show the student class
    */
   public static Tab studentTab;

   /**
    * import and remove tab to hold the class to import and remove data
    */
   public static Tab impAndRemTab;

   /**
    * Tab to hold the browser class
    */
   public static Tab browserTab;

   /**
    * about action event for pressing ctrl + a
    */
   public static MenuItem about;

   /**
    * browser action event for pressing ctrl + e
    */
   public static ActionEvents browser;

   /**
    * games action event for pressing ctrl + g
    */
   public static ActionEvents games;

   /**
    * studentInfo action event for pressing ctrl + s
    */
   public static ActionEvents studentInfo;

   /**
    * ImportData action event for pressing ctrl + i
    */
   public static ActionEvents importData;

   /**
    * Random action event for pressing ctrl + r
    */
   public static ActionEvents random;

   /**
    * Exit of the action event needed to exit program
    */
   public static ActionEvents exit;

   /**
    * Data holds all of the data for the program
    */
   public static Data data;

   /**
    * The help menu
    */
   public static MenuItem instruct;

   /**
    * The user name of the user
    */
   public static String username;
     
   /**
    * DropBox API
    */
   public static DbxClient dropBox;
   /**
    * List of deleted files
    */
   public static List deletedFiles;
   
    private void initDropbox() {
        String accessToken = new String("CH7MRSYzE7MAAAAAAAAAB31ANHyUmV2hPR5_xKu84yU0I_jM4C1KTTxx3m00aqJv");
        DbxRequestConfig config = new DbxRequestConfig("GERSII", Locale.getDefault().toString());
        dropBox = new DbxClient(config, accessToken);
    }
   /**
    * Creates a new Main object.
    *
    * @param pString the type of user
    * @param pUsername - username of user
    */
   Main(String pString, String pUsername, Boolean isNew) throws DbxException, IOException
   {
      initDropbox();
      deletedFiles = new ArrayList <File>();
      mPathSep = System.getProperty("file.separator");
      username = pUsername;
      mUser = pString;
      mStage = new Stage();
      Download mDown = new Download();
      if(!isNew){     
          mDown.getFiles(username, dropBox);
      }
      start(mStage);
      Login.mStage.close();
      Runtime.getRuntime().addShutdownHook(new Thread()
         {
            @Override
            public void run()
            {
                try {
                    new Upload(dropBox);
                    new Update(dropBox,username,deletedFiles);
                    new RemoveTempFiles(username);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
               mStage.close();
               Platform.exit();
             
               System.exit(0);
            }
         });
   }

   /**
    * Initialize the values for the MAIN GUI
    */
   @Override
   public void init()
   {
      tabPane = new TabPane();
      mainPane = new BorderPane();
      gamesTab = new Tab();
      randomTab = new Tab();
      studentTab = new Tab();
      impAndRemTab = new Tab();
      browserTab = new Tab();
      createTabs();
      menuBar = new MenuCreator().getItems();

      data = new Data();
      startReload();
   }

   /**
    * Loads files that already exist
    */
   public void startReload()
   {
      try
      {
         new ReloadFiles(System.getProperty("user.home") + mPathSep + "efresh-tmp" +
                         mPathSep + username, username);
      }
      catch (Exception e)
      {
         System.out.println(e);
      }
   }

   /**
    * Starts the javaFX Application
    *
    * @param primaryStage current Stage to use
    */
   @Override
   public void start(Stage primaryStage)
   {
      mStage = primaryStage;
      init();
      setStage();
   }

   /**
    * Set the standards for the stage of the Main GUI
    */
   private void setStage()
   {
      mStage.setTitle("eFresh");
      mRoot = new VBox();
      mScene = new Scene(mRoot, 900, 550, Color.WHITE);

      mStage.setMinWidth(900); // set width of the gui
      mStage.setMinHeight(550); // set height of the gui

      mainPane.prefHeightProperty().bind(mStage.heightProperty());
      mainPane.prefWidthProperty().bind(mStage.widthProperty());

      menuBar.setMinWidth(1000);
      mainPane.setMinHeight(1000);
      mainPane.setMinWidth(1000);

      mRoot.getChildren().addAll(menuBar, mainPane);
      mStage.setScene(mScene);
      mStage.show();
   }

   /**
    * Creates the tabs for the main GUI
    */
   public void createTabs()
   {
      gamesTab = new Tab();
      setTab(gamesTab, "Games", false, new Games().getView());

      randomTab = new Tab();
      setTab(randomTab, "Randomizer", true, new Random().getView());

      studentTab = new Tab();
      setTab(studentTab, "Student Information", true,
         new StudentInfo().getView());

      impAndRemTab = new Tab();
      setTab(impAndRemTab, "Import/Remove", false,
         new ImpAndRem(username).getView());

      browserTab = new Tab();
      setTab(browserTab, "Explorer", false, new Browser().getView());

      if (! (mUser.contains("Student")))
      {
         tabPane.getTabs()
                .addAll(gamesTab, browserTab, randomTab, studentTab,
            impAndRemTab);
      }
      else
      {
         tabPane.getTabs().addAll(gamesTab, browserTab, impAndRemTab);
      }

      mainPane.setCenter(tabPane);
   }

   /**
    * Sets the Tabs for the Main GUI
    *
    * @param pTab Current tab that is being added
    * @param pName Name of the Tab being created
    * @param pDisable whether to cehck if it needs to be disabled or not
    * @param pPane the pane of the current class being created.
    */
   public void setTab(Tab pTab, String pName, boolean pDisable, Pane pPane)
   {
      pTab.setText(pName);
      pTab.setClosable(false);

      if (pDisable && mUser.contains("Student"))
      {
         pTab.setDisable(pDisable);
      }
      else
      {
         pTab.setContent(pPane);
      }
   }

}
