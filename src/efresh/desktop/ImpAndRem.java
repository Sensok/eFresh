package efresh.desktop;

import efresh.service.Alert;
import efresh.service.PDFTextParser;
import efresh.system.CopyFiles;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.*;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.control.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import javafx.stage.FileChooser;
import javafx.stage.FileChooserBuilder;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;

/**
 * This will import and remove files from the
 * program
 * @author Adam Harper
 */
public final class ImpAndRem
{
   /**
    * Pane which the entire ImpAndRem view is saved too
    */
   private Pane mView;

   /**
    * Ability to choose a file from the users directory
    */
   private FileChooser mFileChooser;

   /**
    * Window that pop ups to allow the user to see which file they want to enter
    */
   private PopupWindow mWindow;

   /**
    * Button allowing user to find a file
    */
   private Button mButton;

   /**
    * Button allowing user to remove the chosen file and its contents
    */
   private Button mRButton;

   /**
    * Button allowing the user to import the chose file
    */
   private Button mImportButton;

   /**
    * Stores the filechooser and the button to browse and import a file
    */
   private HBox mTopBox;

   /**
    * Stores the files and remove ability
    */
   private HBox mBotBox;

   /**
    * Builds the file choose for the user to choose a file
    */
   private FileChooserBuilder fcb;

   /**
    * saves the selected file of the user to be imported
    */
   private File selectedFile;

   /**
    * textfield to show the selected file's name
    */
   private TextField mTextField;

   /**
    * Stage to find the owner for the file choose builder to work
    */
   private Stage mPrimaryStage;

   /**
    * Options of the names of the files to delete
    */
   public ObservableList<String> mOptions;

   /**
    * Combo box to show which files want to be deleted
    */
   public static ComboBox<String> mComboBox;

   /**
    * A vbox to hold the top and bottom box
    */
   private VBox mVBox;

   /**
    * Label to show the "Import File"
    */
   private Label mImport;

   /**
    * Label to show the "Remove File"
    */
   private Label mRemove;

   /**
    * Mid box holds the line to seperate topbox and bottombox
    */
   private HBox midBox;

   /**
    * DOCUMENT ME!
    */
   private String username;

   /**
    * Creates a new ImpAndRem object.
    */

   //TODO: If the incoming file is a zip but does not contain
   //a .csv or anything we need to not allow that.. It will
   //break the GUI.
   public ImpAndRem(String pUsername)
   {
      username = pUsername;
      mView = new Pane();
      mFileChooser = new FileChooser();
      mButton = new Button("Browse");
      mTextField = new TextField("");
      mTextField.setEditable(false);
      mPrimaryStage = Main.mStage;
      mImportButton = new Button("Import");
      mRButton = new Button("Remove");
      mVBox = new VBox();
      mImport = new Label("Import File");
      mRemove = new Label("Remove File");
      mOptions = FXCollections.observableArrayList();
      mComboBox = new ComboBox();
      mComboBox.setItems(mOptions);
      mComboBox.setPromptText("Removable Files");
      mComboBox.setMinWidth(160);

      mImportButton.setOnAction(new EventHandler<ActionEvent>()
         {
            @Override
            public void handle(ActionEvent Event)
            {
               if (mTextField.getText().equals(""))
               {
                  System.out.println("Nothing to do");
               }
               else
               {
                  mTextField.setText("");

                  try
                  {
                     DataParser newParse =
                        new DataParser(selectedFile, username);
                  }
                  catch (FileNotFoundException ex)
                  {
                     Logger.getLogger(ImpAndRem.class.getName())
                           .log(Level.SEVERE, null, ex);
                  }
                  catch (IOException ex)
                  {
                     Logger.getLogger(ImpAndRem.class.getName())
                           .log(Level.SEVERE, null, ex);
                  }
                  catch (Exception e)
                  {
                  }
               }
            }
         });

      mButton.setOnAction(new EventHandler<ActionEvent>()
         {
            @Override
            public void handle(ActionEvent Event)
            {
               String currentDir =
                  System.getProperty("user.dir") + File.separator;
               StringBuilder sb = null;
               fcb = FileChooserBuilder.create();

               FileChooser fc =
                  fcb.title("Open Dialog").initialDirectory(new File(currentDir))
                     .build();
               selectedFile = fc.showOpenDialog(Main.mStage);

               if (selectedFile != null)
               {
                  if ((selectedFile.getName().endsWith("csv")) ||
                      (selectedFile.getName().endsWith("zip")) ||
                      (selectedFile.getName().endsWith("pdf")))
                  {
                     if (selectedFile.getName().endsWith("pdf"))
                     {
                        PDFTextParser mParse = new PDFTextParser(selectedFile.getAbsolutePath(), username, selectedFile.getName());
                        selectedFile = mParse.getZipFile();
                     }
                     mTextField.setText(selectedFile.getName());
                  }
                  else
                  {
                     mTextField.setText("");
                     showMessage("Invalid file type, needs to be in format .csv or .zip or pdf",
                        "File is \"" + selectedFile.getName() + "\"");
                  }
               }
            }
         });

      mRButton.setOnAction(new EventHandler<ActionEvent>()
         {
            @Override
            public void handle(ActionEvent Event)
            {
               String filename =
                  mComboBox.getSelectionModel().getSelectedItem();

               if (filename != null)
               {
                  Main.data.remove(filename);
               }
               else
               {
                  System.out.println("Nothing to remove");
               }
            }
         });

      setUpTopBox();
      setUpBotBox();
      setMiddleBox();
      setPane();
      setUpVBox();
   }

   /**
    * Sets up the Vbox for the top section and middle and bottom to add everything together
    */
   public void setUpVBox()
   {
      mVBox.getChildren().addAll(mTopBox, midBox, mBotBox);
      mVBox.setAlignment(Pos.CENTER);
   }

   /**
    * Prints an alert message of an error that occurred
    *
    * @param pMsg1 String of the message
    * @param pMsg2 String second part of the message
    */
   private void showMessage(String pMsg1, String pMsg2)
   {
      Stage stage = new Alert(mPrimaryStage, pMsg1, pMsg2);
      stage.showAndWait();
   }

   /**
    * Set up the top box by adding all the elements for the import together
    */
   public void setUpTopBox()
   {
      mImport.setFont(Font.font("Verdana", FontWeight.BOLD, 30));

      HBox importFile = new HBox();
      importFile.getChildren().addAll(mTextField, mButton, mImportButton);
      importFile.setMargin(mButton, new Insets(12, 0, 10, 10));
      importFile.setMargin(mTextField, new Insets(12, 0, 10, 10));
      importFile.setMargin(mImportButton, new Insets(12, 0, 10, 10));

      VBox importData = new VBox();
      importData.getChildren().addAll(mImport, importFile);
      importData.setAlignment(Pos.CENTER);

      mTopBox = new HBox();
      mTopBox.getChildren().add(importData);
      mTopBox.setMargin(importData, new Insets(50, 0, 10, 10));
   }

   /**
    * set up the bottom box by adding all the elements for the remove together
    */
   public void setUpBotBox()
   {
      mRemove.setFont(Font.font("Verdana", FontWeight.BOLD, 30));

      VBox removeBox = new VBox();
      HBox comboButton = new HBox();
      mBotBox = new HBox();

      comboButton.getChildren().addAll(mComboBox, mRButton);
      comboButton.setMargin(mComboBox, new Insets(12, 0, 10, 10));
      comboButton.setMargin(mRButton, new Insets(12, 0, 10, 10));
      comboButton.autosize();
      removeBox.getChildren().addAll(mRemove, comboButton);
      removeBox.setAlignment(Pos.CENTER);

      mBotBox.getChildren().add(removeBox);
      mBotBox.setMargin(removeBox, new Insets(50, 0, 10, 35));
   }

   /**
    * Adds a solid line to the middle area
    */
   public void setMiddleBox()
   {
      Line line = new Line();
      line.setStartX(0.0f);
      line.setStartY(0.0f);
      line.setEndX(295.0f);
      line.setEndY(0.0f);

      midBox = new HBox();
      midBox.getChildren().add(line);
      midBox.setMargin(line, new Insets(15, 0, 0, 15));
   }

   /**
    * Sets the pane for Main to be able to view this class
    */
   public void setPane()
   {
      mView.getChildren().add(mVBox);
   }

   /**
    * Allows other functions to access the pane, ie Main
    *
    * @return Pane the current Pane made by this class
    */
   public Pane getView()
   {
      return mView;
   }

   /**
    * Ability to add to the combo box from another section of the program
    *
    * @param pName the name of the key
    */
   public static void addToBox(String pName)
   {
      mComboBox.getItems().add(pName);
      java.util.Collections.sort(mComboBox.getItems());
   }

   /**
    * Ability to remove from the combobox from another section
    *
    * @param pName removes that name for the list
    */
   public static void removeFromBox(String pName)
   {
      mComboBox.getItems().remove(pName);
   }
}
