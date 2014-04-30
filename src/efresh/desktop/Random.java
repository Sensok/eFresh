package efresh.desktop;

import efresh.service.*;

import efresh.system.*;

import java.io.*;

import javafx.collections.*;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * This is the Random tab that randomizes student
 * pictures
 * @author Adam Harper
 */
public final class Random
{
   /**
    * The images file
    */
   private static File imageFile;

   /**
    * Pane for the random class
    */
   private static Pane mView;

   /**
    * class to randomize the students
    */
   private Button mButton;

   /**
    * button to be able to pass a student without removing from the queue
    */
   private Button mPassButton;

   /**
    * Label for the GUI
    */
   private Label mLabel;

   /**
    * Hidden name labe;
    */
   private static Label mNameLabel;

   /**
    * Options for the combobox
    */
   private static ObservableList<String> mOptions;

   /**
    * combobox so the user can see the classes
    */
   private static ComboBox<String> mComboBox;

   /**
    * hbox to hold the labels and buttons
    */
   private HBox mHBox;

   /**
    * hbox to hold the hidden name
    */
   private VBox mNameBox;

   /**
    * vbox to hold the hboxes
    */
   private VBox mVBox;

   /**
    * imagebox to hold the image
    */
   private VBox mImageBox;

   /**
    * actual image of the student
    */
   public static Image mImage;

   /**
    * The image view for the students image
    */
   private static ImageView imageView;

   /**
    * The class names to put in the list view
    */
   private String className;

   /**
    * Count of number of students
    */
   private static int count;

   /**
    * The Random Queue to use
    */
   private static RandomQueue myRandomness;

   /**
    * Creates a new Randomizer object.
    */
   public Random()
   {
      imageFile = new File("random.png");
      mView = new Pane();
      count = 0;
      mButton = new Button("Randomize");
      mPassButton = new Button("Next");
      mButton.setMinWidth(200);
      mLabel = new Label("Full Name:  ");
      mNameLabel = new Label("");

      mPassButton.setOnAction(new EventHandler<ActionEvent>()
         {
            @Override
            public void handle(ActionEvent Event)
            {
               if (count == 0)
               {
                  System.out.println("Nothing has been intialized");
               }
               else
               {
                  myRandomness.random();
                  changeData(myRandomness.getImage(), myRandomness.getName());
               }
            }
         });

      mLabel.setOnMouseEntered(new EventHandler<MouseEvent>()
         {
            @Override
            public void handle(MouseEvent me)
            {
               mNameLabel.setVisible(true);
            }
         });

      mLabel.setOnMouseExited(new EventHandler<MouseEvent>()
         {
            @Override
            public void handle(MouseEvent me)
            {
               mNameLabel.setVisible(false);
            }
         });

      mNameLabel.setVisible(false);
      mOptions = FXCollections.observableArrayList();
      mComboBox = new ComboBox();
      mComboBox.setItems(mOptions);
      mComboBox.setPromptText("Classes");
      mComboBox.setMinWidth(200);

      mButton.setOnAction(new EventHandler<ActionEvent>()
         {
            @Override
            public void handle(ActionEvent Event)
            {
               className = mComboBox.getSelectionModel().getSelectedItem();

               if (className != null)
               {
                  count++;
                  myRandomness = new RandomQueue(Main.data.personMap.get(
                           className).getFullNames(),
                        Main.data.personMap.get(className).getImages());
               }
            }
         });

      mHBox = new HBox();
      mNameBox = new VBox();
      mVBox = new VBox();
      mImageBox = new VBox();
      mImage = new Image("/resources/images/test.jpg");

      imageView = new ImageView();
      imageView.setImage(mImage);
      imageView.setFitWidth(225);
      imageView.setPreserveRatio(true);
      imageView.setSmooth(true);
      imageView.setCache(true);
      setUpBox();
      createView();
   }

   /**
    * Sets up the box to make the gui easy to view
    */
   public void setUpBox()
   {
      mNameBox.getChildren().addAll(mLabel, mNameLabel);
      mNameBox.setMargin(mLabel, new Insets(12, 0, 10, 10));
      mNameBox.setMargin(mNameLabel, new Insets(12, 0, 10, 10));
      mNameBox.setMaxWidth(250);
      mNameBox.setAlignment(Pos.CENTER);

      mVBox.getChildren().addAll(mComboBox, mButton, mNameBox);
      mVBox.setMargin(mComboBox, new Insets(120, 0, 10, 10));
      mVBox.setMargin(mButton, new Insets(12, 0, 10, 10));
      mVBox.setMargin(mNameBox, new Insets(12, 0, 10, 10));
      mVBox.setAlignment(Pos.CENTER);

      mImageBox.getChildren().addAll(imageView, mPassButton);
      mImageBox.setMargin(imageView, new Insets(100, 0, 10, 135));
      mImageBox.setMargin(mPassButton, new Insets(0, 0, 10, 135));
      mImageBox.setAlignment(Pos.CENTER);

      mHBox.getChildren().addAll(mVBox, mImageBox);
      mHBox.setAlignment(Pos.CENTER);
   }

   /**
    * creates the actual view for the user by adding the box
    */
   public void createView()
   {
      mView.getChildren().add(mHBox);
   }

   /**
    * Allows Main to be able to access the pane view
    *
    * @return Pane -  mView random's view
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
      mImage = new Image("/resources/images/test.jpg");
      imageView.setImage(mImage);
      myRandomness = null;
      count--;
      mNameLabel.setText("");

      //      File file = new File("random.png");
      if (imageFile.delete())
      {
      }
   }

   /**
    * This will change the data in Random to the next image
    * and name to appear on the screen
    * @param image The new image to display
    * @param name The new name to display
    */
   public void changeData(File image, String name)
   {
      imageFile = image;
      mImage = new Image(image.toString());
      imageView.setImage(mImage);
      mNameLabel.setText(name);
   }
}
