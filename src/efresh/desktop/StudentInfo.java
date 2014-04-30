package efresh.desktop;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import javafx.stage.FileChooser;
import javafx.stage.FileChooserBuilder;
import javafx.stage.Stage;

/**
 * This is the GUI for the StudentInfo tab
 *
 * @author Jordan Jensen
 */
public final class StudentInfo
{
   /**
    * A view by class of students
    */
   private static Accordion accordion;

   /**
    * The pane of Student Info
    */
   private Pane mView;

   /**
    * Array of text fields for the Student info
    */
   private static TextField[] mStudenInfo;

   /**
    * The labels for Student Info
    */
   private Label[] mLabelInfo;

   /**
    * Hbox used for containing information
    */
   private HBox mHBox;

   /**
    * The Left VBox
    */
   private VBox mLeft;

   /**
    * The Right VBox
    */
   private VBox mRight;

   /**
    * List view of Strings holding student names
    */
   private static ListView<String> mListView;

   /**
    * The main stage from Main
    */
   private Stage primaryStage;

   /**
    * The array list of titled panes to go into the accordion
    */
   private static ArrayList<TitledPane> mTitlePanes;

   /**
    * The image vBox
    */
   private VBox mImageBox;

   /**
    * Image view for the students image
    */
   private static ImageView mImageView;

   /**
    * The image
    */
   private static Image mImage;

   /**
    * A save info button
    */
   private Button mSave;

   /**
    * Changes the student image
    */
   private Button mChangeImage;

   /**
    * Resets the text fields to the original
    * the status
    */
   private Button mResetField;

   /**
    * Clears all the fields and resets the image
    */
   private Button mClearField;

   /**
    * The border pane for the gui
    */
   private BorderPane mPane;

   /**
    * The number of text fields
    */
   private int numFields;

   /**
    * The gride to hold the text fields and labels
    */
   private GridPane mGrid;

   /**
    * the number of fields
    */
   private int mI;

   /**
    * Number of classes in the array list
    */
   private static int numClasses;

   /**
    * Creates a new StudentInfo object.
    */
   StudentInfo()
   {
      init();
      setGrid();
      setImageBox("/resources/images/test.jpg");
      setActions();
      setPane();
   }

   /**
    * This will initialize the GUI
    */
   public void init()
   {
      //start at -1 because you increment this when you add a class
      numClasses = -1;
      primaryStage = Main.mStage;
      mView = new Pane();
      mGrid = new GridPane();
      mPane = new BorderPane();
      mHBox = new HBox();
      mLeft = new VBox();
      mRight = new VBox();
      accordion = new Accordion();

      numFields = 8;
      mImageView = new ImageView();

      mTitlePanes = new ArrayList<TitledPane>();

      mImageBox = new VBox();

      mI = 0;

      mStudenInfo = new TextField[]
         {
            new TextField(), new TextField(), new TextField(), new TextField(),
            new TextField(), new TextField(), new TextField(), new TextField()
         };

      mLabelInfo = new Label[]
         {
            new Label("First Name"), new Label("Last Name"),
            new Label("Preferred Name"), new Label("I-Number"),
            new Label("Email"), new Label("City"), new Label("State"),
            new Label("Major")
         };

      mSave = new Button("Save Changes");
      mChangeImage = new Button("Change Image");
      mResetField = new Button("Revert Changes");
      mClearField = new Button("Clear Information");
   }

   /**
    * This will set the pane for the student info
    */
   public void setPane()
   {
      mHBox.getChildren().addAll(mLeft, mRight);

      HBox studentIn = new HBox();

      studentIn.getChildren().addAll(accordion, mHBox);
      studentIn.setMargin(mHBox, new Insets(0, 200, 0, 0));
      studentIn.setMargin(accordion, new Insets(10, 0, 0, 8));
      mView.getChildren().add(studentIn);
   }

   /**
    * This will set the image path
    *
    * @param pPath path of the image
    */
   public void setImagePath(String pPath)
   {
      try
      {
         InputStream in = new FileInputStream(pPath);

         mImage = new Image(in);

         mImageView.setImage(mImage);
         //resize the image and keep all the aspect ratios
         mImageView.setFitWidth(225);
         mImageView.setPreserveRatio(true);
         mImageView.setSmooth(true);
         mImageView.setCache(true);
      }
      catch (Exception e)
      {
         System.out.println(e);
      }
   }

   /**
    * This will set the image box
    *
    * @param pPath This is the path of the image
    */
   public void setImageBox(String pPath)
   {
      mImage = new Image("/resources/images/test.jpg");

      mImageView.setImage(mImage);
      //resize the image and keep all the aspect ratios
      mImageView.setFitWidth(225);
      mImageView.setPreserveRatio(true);
      mImageView.setSmooth(true);
      mImageView.setCache(true);
   }

   /**
    * This sets up the grid of labels and text views
    */
   public void setGrid()
   {
      mGrid.setHgap(10);
      mGrid.setVgap(10);
      mGrid.setPadding(new Insets(25, 25, 25, 25));

      GridPane temp = new GridPane();
      temp.setHgap(10);
      temp.setVgap(10);
      temp.setPadding(new Insets(25, 25, 25, 25));

      mGrid.setAlignment(Pos.CENTER);

      for (mI = 0; mI < 3; mI++)
      {
         temp.add(mLabelInfo[mI], 0, mI);
         temp.add(mStudenInfo[mI], 1, mI);
      }

      HBox newBox = new HBox();
      newBox.getChildren().addAll(temp, mImageView);
      newBox.setMargin(mImageView, new Insets(15, 0, 0, 15));

      for (mI = 3; mI < numFields; mI++)
      {
         mGrid.add(mLabelInfo[mI], 0, mI - 3);
         mGrid.add(mStudenInfo[mI], 7, mI - 3);
      }

      HBox textButtons = new HBox(3);
      textButtons.getChildren()
                 .addAll(mResetField, mClearField, mSave, mChangeImage);
      mLeft.getChildren().addAll(newBox, mGrid, textButtons);
      mLeft.setMargin(mGrid, new Insets(0, 400, 0, 0));
      mLeft.setMargin(textButtons, new Insets(0, 0, 0, 20));
      mI = 0;
   }

   /**
    * This will get the view to put into the Main GUI
    *
    * @return Pane - the current view
    */
   public Pane getView()
   {
      return mView;
   }

   /**
    * This will update the Accordion view to hold the new classed added
    */
   public static void updateAcc()
   {
      accordion.getPanes().add(mTitlePanes.get(numClasses));
   }

   /**
    * This sets the list view actions on the buttons
    */
   public void setActions()
   {
      mChangeImage.setOnAction(new EventHandler<ActionEvent>()
         {
            private FileChooserBuilder fcb;
            private File selectedFile;

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
                  setImagePath(selectedFile.getAbsolutePath());
               }
            }
         });

      mClearField.setOnAction(new EventHandler<ActionEvent>()
         {
            @Override
            public void handle(ActionEvent Event)
            {
               for (TextField test : mStudenInfo)
               {
                  test.setText("");
               }

               setImagePath("resources/images/test.jpg");
            }
         });
   }

   /**
    * This will add a name to the class list view
    *
    * @param pName The name to add
    * @param pList The list view that holds the classes name
    */
   public static void addName(String pName, ListView pList)
   {
      pList.getItems().add(pName);
   }

   /**
    * This will add a class to the accordion
    *
    * @param pClass This is the name of the class to add
    * @param pNames This is an array of names to add
    */
   public static void addClass(String pClass, String[] pNames)
   {
      numClasses++;
      mListView = new ListView<String>();
      mListView.setOnMouseClicked(new EventHandler<MouseEvent>()
         {
            @Override
            public void handle(MouseEvent event)
            {
               String className = getCurrentName();
               String name = getListView();
               changeData(Main.data.personMap.get(className).getData(name),
                  Main.data.personMap.get(className).getPersonImage(name));
            }
         });

      for (String temp : pNames)
      {
         if (! temp.contains("Name") && ! temp.contains("Name,I-Num"))
         {
            addName(temp, mListView);
         }
      }

      mListView.setMaxWidth(175);
      mTitlePanes.add(new TitledPane(pClass, mListView));
      updateAcc();
   }

   /**
    * This will remove a class by number from the accordion view
    *
    * @param toDelete the number of class to remove
    */
   public static void removeClass(int toDelete)
   {
      accordion.getPanes().remove(mTitlePanes.get(toDelete));
      mTitlePanes.remove(toDelete);
      numClasses--;

      String[] data = { " ", " ", " ", " ", " ", " " };

      mImage = new Image("/resources/images/test.jpg");
      mImageView.setImage(mImage);

      for (int i = 0; i < 8; i++)
      {
         mStudenInfo[i].setText("");
      }
   }

   /**
    * This will change the data to be the correct
    * format for the text fields
    * @param data the data coming in
    * @param image the students image
    */
   public static void changeData(String[] data, File image)
   {
      mImage = new Image(image.toString());
      mImageView.setImage(mImage);

      int j = 0;
      int size = data.length;

      for (int i = 0; i < size; i++)
      {
         if (i == 0)
         {
            String name = data[0];
            String lastName = name.substring(0, name.indexOf(','));
            String firstName =
               name.substring(name.indexOf(' ') + 1, name.length());
            mStudenInfo[0].setText(firstName);
            mStudenInfo[1].setText(lastName);

            if (lastName.contains("Harper") || lastName.contains("Jensen"))
            {
               mStudenInfo[2].setText("Awesome");
            }
            else
            {
               mStudenInfo[2].setText(firstName);
            }
         }
         else if (i == 7)
         {
            mStudenInfo[2].setText(data[i]);
         }
         else if (i == 6)
         {
            mStudenInfo[3].setText(data[i]);
         }
         else if (i == 4)
         {
            String home = data[i];
            String city = home.substring(0, home.indexOf(','));
            String state = home.substring(home.indexOf(',') + 2, home.length());
            mStudenInfo[5].setText(city);
            mStudenInfo[6].setText(state);
         }
         else if (i == 3)
         {
            mStudenInfo[7].setText(data[i]);
         }
         else if (i == 2)
         {
            mStudenInfo[4].setText(data[i]);
         }
         else if (i == 1)
         {
            mStudenInfo[3].setText(data[i]);
         }
      }
   }

   /**
    * This will get the current class name
    *
    * @return String - accorion's expanded pane
    */
   public static String getCurrentName()
   {
      return accordion.getExpandedPane().getText();
   }

   /**
    * This will get the list view of the expanded pane
    *
    * @return String  - the name of the list view
    */
   public static String getListView()
   {
      String name = "";

      for (int i = 0; i < mTitlePanes.size(); i++)
      {
         if (mTitlePanes.get(i).getText().equals(getCurrentName()))
         {
            ListView nameValue = (ListView) mTitlePanes.get(i).getContent();
            name = nameValue.getSelectionModel().getSelectedItem().toString();
         }
      }

      return name;
   }
}
