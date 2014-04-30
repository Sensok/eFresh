package efresh.desktop;

import efresh.service.Alert;

import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.collections.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.stage.Stage;
import javafx.stage.Popup;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Games Gui
 * @author Jake Stevens and Adam Harper
 */
public class Games
{
   /**
    * The pane the Game GUI elements are built; This is then sent
    * to Main for the GUI Games Tab
    */
   private static Pane mView;

   /**
    * The playabel pane for the loaded game
    */
   private static Pane mGameArea;

   /**
    * The playable games
    */
   private Button mGameSelect;

   /**
    * The list of loadable saved games
    */
   private ComboBox<String> mLoadGame;

   /**
    * Button to save a game
    */
   private Button mSaveGame;

   /**
    * Label over the listView of loaded data
    */
   private Label mFileLabel;

   /**
    * Contains the list of the imported files.
    * Used to build a game based on the data from the listed file.
    */
   private ObservableList<String> mLoadedFiles;

   /**
    * Stores the game options.
    */
   private ObservableList<String> mGameOptions;

   /**
    * Stores the list of saved games the user is able to load.
    */
   private ObservableList<String> mSavedGames;

   /**
    * Holds the loadedFile Obserable List
    */
   private static ListView<String> mFileList;

   /**
    *The horizontal box that contains all the the 2 main vertical boxes.
    */
   private HBox mGameBox;

   /**
    * Contains the playable game area and the loaded file list.
    */
   private HBox mScreen;

   /**
    * The Horizontal box that contains the combo boxes and button.
    */
   private HBox mButtons;

   /**
    * Contains the ListView of the loaded files.
    */
   private VBox mFileTree;

   /**
    * Contains the pane that will be received whent a game is loaded.
    */
   private VBox mGameLayout;

   /**
    * Initial ImageView for opening eFresh
    */
   private ImageView mScreenDemo;

   /**
    * Initial Image for opening eFresh
    */
   private Image mScreenImage;

   /**
    * Constuctor - Initializes all GUI elements and establishes the
    * action event handlers.
    */
   Games()
   {
      mGameOptions = FXCollections.observableArrayList("Flash Card");

      //Need to populate this with the saved games
      mSavedGames = FXCollections.observableArrayList("");
      mView = new Pane();
      mSaveGame = new Button("Save Game");

      //*******Hiding the Save Game Button*********
      mSaveGame.setVisible(false);

      // SaveGame Action Handler
      mSaveGame.setOnAction(
         new EventHandler<ActionEvent>()
         {
            @Override
            public void handle(ActionEvent event)
            {
               String gameSaved = "Game Saved";
               Stage stage =
                  new Alert(Main.mStage, gameSaved, gameSaved +
                            " Successfully");
               stage.showAndWait();
            }
         });

      mScreenImage = new Image("resources/images/test.jpg");

      mScreenDemo = new ImageView();
      mScreenDemo.setImage(mScreenImage);
      mScreenDemo.setFitWidth(225);
      mScreenDemo.setPreserveRatio(true);
      mScreenDemo.setSmooth(true);
      mScreenDemo.setCache(true);
      mGameArea = new Pane();
      mGameArea.getChildren().add(mScreenDemo);

      mGameSelect = new Button("Flash Card");

      // Gamse Select Action Handler
      mGameSelect.setOnAction(
         new EventHandler<ActionEvent>()
         {
            @Override
            public void handle(ActionEvent event)
            {
               String fileToLoad = mFileList.getSelectionModel().getSelectedItem();

               if (fileToLoad == null)
               {
                  Stage stage =
                     new Alert(Main.mStage, "File Not Selected",
                        "Please select a file to load");
                  stage.showAndWait();
               }
               else
               {
                  loadFlashCard(fileToLoad);

               }
            }
         });

      mLoadGame = new ComboBox(mSavedGames);
      mLoadGame.setPromptText("Saved Games");

      //************Hiding the mLoadGame ComboBox**********
      mLoadGame.setVisible(false);

      // Load Saved Game Action Handler
      mLoadGame.setOnAction(
         new EventHandler<ActionEvent>()
         {
            @Override
            public void handle(ActionEvent event)
            {
               String savedGame = mLoadGame.getValue();
               Stage stage = new Alert(Main.mStage, savedGame, "Game Loaded");
               stage.showAndWait();
               mLoadGame.hide();
            }
         });

      mSaveGame.setMinWidth(160);
      mGameSelect.setMinWidth(160);
      mLoadGame.setMinWidth(160);
      mFileLabel = new Label("Files");

      // Add a loop to load the loadeFiles witht the keys for the data
      mLoadedFiles = FXCollections.observableArrayList();
      mFileList = new ListView<String>(mLoadedFiles);

      mScreen = new HBox();
      mButtons = new HBox();
      mFileTree = new VBox();
      mGameLayout = new VBox();
      mGameBox = new HBox();

      setUpView();
      createView();
   }

   /**
    * Builds each box with the required elements
    */
   public final void setUpView()
   {
      // Horizontal Box that holds the game buttons
      mButtons.getChildren().addAll(mGameSelect, mLoadGame, mSaveGame);
      mButtons.setMargin(mGameSelect, new Insets(12, 3, 12, 5));
      mButtons.setMargin(mLoadGame, new Insets(12, 2, 12, 3));
      mButtons.setMargin(mSaveGame, new Insets(12, 2, 12, 5));
      mButtons.setAlignment(Pos.CENTER);

      // Horizontal Box that holds gameArea
      mScreen.getChildren().add(mGameArea);
      mScreen.setMargin(mScreenDemo, new Insets(12, 12, 12, 12));
      mScreen.setAlignment(Pos.CENTER);

      // Vertical box that holds the Loaded Files TreeView
      mFileTree.getChildren().addAll(mFileLabel, mFileList);
      mFileTree.setMargin(mFileLabel, new Insets(5, 5, 5, 5));
      mFileTree.setMargin(mFileList, new Insets(5, 5, 5, 5));
      mFileTree.setAlignment(Pos.CENTER);

      // Vertical box that holds the screen and the buttons
      mGameLayout.getChildren().addAll(mScreen, mButtons);
      mGameLayout.setMargin(mScreen, new Insets(10, 10, 10, 10));
      mGameLayout.setMargin(mButtons, new Insets(10, 10, 10, 10));
      mGameLayout.setAlignment(Pos.CENTER);

      // Horizontal box that holds the 2 vertical boxes
      mGameBox.getChildren().addAll(mFileTree, mGameLayout);
      mGameBox.setMargin(mFileTree, new Insets(10, 10, 10, 10));
      mGameBox.setMargin(mGameLayout, new Insets(10, 10, 10, 10));
      mGameBox.setAlignment(Pos.CENTER);
   }

   /**
    * Builds the pane with all the elements stored in gameBox
    */
   public final void createView()
   {
      mView.getChildren().add(mGameBox);
   }

   /**
    * Sends the complete pane to Main.java for the Games tab.
    *
    * @return mView - the complete pane for use in Main.java under the Games Tab
    */
   public Pane getView()
   {
      return mView;
   }

   /**
    * Adds uploaded file names to the fileList when they are imported.
    *
    * @param pName - Tha name of an imported file
    */
   public static void addToFileList(String pName)
   {
      mFileList.getItems().add(pName);
      java.util.Collections.sort(mFileList.getItems());
   }

   /**
    * Removes file names from fileList when they are deleted.
    *
    * @param pName - the name of a deleted file
    */
   public static void removeFromList(String pName)
   {
      mFileList.getItems().remove(pName);
   }

   /**
    * Loads the Flash Card Game into the gameArea pane.
    *
    * @param mPane - Flash Card game pane
    */
   public void loadPane(Pane mPane)
   {
      mScreen.getChildren().remove(mGameArea);
      mGameArea = mPane;
      mScreen.getChildren().add(mGameArea);
   }

   /**
    * Calls the FlashCardGame constructor with the name of a file to load
    *
    * @param pFileToLoad - Name of the key to access the map where the data is stored
    */
   public void loadFlashCard(String pFileToLoad)
   {
       Pane pane = new Pane();
       FlashCardGame game = new FlashCardGame(pFileToLoad);
       pane = game.getGame();
       loadPane(pane);

       Main.mStage.show();
   }
}
