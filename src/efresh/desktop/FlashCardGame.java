package efresh.desktop;

import java.util.*;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javafx.scene.text.Font;
import javafx.scene.text.*;

/**
 * Flash Card class.
 * This creates a pane with the elements for the Flash Card game.
 *
 * @author Jacob Stevens
 */
public class FlashCardGame
{
   /**
    * Map containing the imported file data.
    */
   private Map<String, String> mGameData;
    /**
    * ArrayList to hold the list of words.
    */
   public ArrayList<String> mWords;

   /**
    * ArrayList to hold Definitions.
    */
   public ArrayList<String> mDefinitions;

   /**
    * Pane to hold the GUI elements.
    */
   private static Pane mGamePane;

   /**
    * Button to move to the next element of the ArrayLists.
    */
   private Button mNextSet;

   /**
    * Button to set the visaibility of the definition to true.
    */
   private Button mShowDef;

   /**
    * GUI Element to hold the word text for the game. This text is stored
    * in the ArrayList word.
    */
   private Text mWordText;

   /**
    * GUI Element to hold the definition text for the game. This text is stored
    * in the ArrayList definition.
    */
   private Text mDefinitionText;

   /**
    * The background image for the word side of the game.
    */
   private Image mLeftCard;

   /**
    * The background image for the definition side of the game.
    */
   private Image mRightCard;

   /**
    * Holds and displays the word side image.
    */
   private ImageView mLeftCardView;

   /**
    * Holds and displays the definition side image.
    */
   private ImageView mRightCardView;

   /**
    * Stack pane used to display first the LeftCardView and then the
    * word Text for the game.
    */
   private StackPane mWordCard;

   /**
    * Stack pane used to display first the RightCardView and then the
    * definition Text for the game.
    */
   private StackPane mDefCard;

   /**
    * Holds all the GUI Elements for the full game.
    */
   private HBox mGame;

   /**
    * Holds the word side StackPane.
    */
   private VBox mLeft;

   /**
    * Holds the Definition side StackPane.
    */
   private VBox mRight;

   /**
    * Holds the 2 buttons.
    */
   private VBox mCenter;

   /**
    * Used to access the ArrayLists for the word and the definition.
    */
   public int mIndex;

   /**
    * Creates a new FlashCardGame object. Also contains the action handlers
    * for the game.
    */
   FlashCardGame(String pFileToLoad)
   {
      mGameData = Main.data.gameMap.get(pFileToLoad).getMap();
      mWords = new ArrayList();
      mWords = Main.data.gameMap.get(pFileToLoad).getList();
      mDefinitions = new ArrayList();
      for(int i = 0; i < mWords.size(); i++)
      {
         mDefinitions.add(mGameData.get(mWords.get(i)));
      }

      mIndex = 0;

      mGamePane = new Pane();
      mNextSet = new Button("Next");
      mNextSet.setMinWidth(100);

      // Action Handler for loading the next element of the ArrayLists
      mNextSet.setOnAction(new EventHandler<ActionEvent>()
         {
            @Override
            public void handle(ActionEvent e)
            {
               loadNextSet();
               Main.mStage.show();
            }
         });

      mShowDef = new Button("Show");
      mShowDef.setMinWidth(100);
      mWordCard = new StackPane();
      mDefCard = new StackPane();
      mWordText = new Text("Word");
      mWordText.setWrappingWidth(200);
      mWordText.setFont(new Font(20));
      mWordText.setTextAlignment(TextAlignment.CENTER);
      mDefinitionText = new Text("Definition");
      mDefinitionText.setWrappingWidth(200);
      mDefinitionText.setFont(new Font(20));
      mDefinitionText.setTextAlignment(TextAlignment.CENTER);
      mLeftCard = new Image("resources/images/FlashCard.png");
      mRightCard = new Image("resources/images/FlashCard.png");
      mLeftCardView = new ImageView();
      mRightCardView = new ImageView();
      mLeftCardView.setImage(mLeftCard);
      mRightCardView.setImage(mRightCard);
      mLeftCardView.setFitWidth(225);
      mLeftCardView.setFitHeight(350);
      mRightCardView.setFitWidth(225);
      mRightCardView.setFitHeight(350);

      // Action Handler to show the definition
      mShowDef.setOnAction(new EventHandler<ActionEvent>()
         {
            @Override
            public void handle(ActionEvent e)
            {
               if (mIndex == 0)
               {
               }
               else
               {
                  mRightCardView.setVisible(false);
                  mDefinitionText.setVisible(true);
               }
            }
         });
      mGame = new HBox();
      mLeft = new VBox();
      mRight = new VBox();
      mCenter = new VBox();

      setUpGame();
      createGame();
   }

   /**
    * Builds the Flash Card GUI
    */
   public final void setUpGame()
   {
      //StackPane for image and Text
      mWordCard.getChildren().addAll(mLeftCardView, mWordText);
      mWordCard.setAlignment(Pos.CENTER);
      mDefCard.getChildren().addAll(mRightCardView, mDefinitionText);
      mDefCard.setAlignment(Pos.CENTER);

      //VBoxes for StackPanes
      mLeft.getChildren().add(mWordCard);
      mRight.getChildren().add(mDefCard);
      mCenter.getChildren().addAll(mShowDef, mNextSet);
      mCenter.setMargin(mShowDef, new Insets(5, 1, 5, 1));
      mCenter.setMargin(mNextSet, new Insets(5, 1, 5, 1));
      mCenter.setAlignment(Pos.CENTER);

      //HBoxes for game
      mGame.getChildren().addAll(mLeft, mCenter, mRight);
      mGame.setMargin(mLeft, new Insets(5, 10, 5, 5));
      mGame.setMargin(mCenter, new Insets(5, 5, 5, 5));
      mGame.setMargin(mRight, new Insets(5, 5, 5, 10));
   }

   /**
    * Adds the Game HBox into the gamePane
    */
   public final void createGame()
   {
      mGamePane.getChildren().add(mGame);
   }

   /**
    * Returns the gamePane so it can be added to the Games.java class
    *
    * @return mGamePane - The Flash Card pane used when Flash Card is loaded
    */
   public Pane getGame()
   {
      return mGamePane;
   }

   /**
    * Loads the next word and definition elements into the game.
    */
   public void loadNextSet()
   {
      mWordCard.getChildren().remove(mWordText);
      mDefCard.getChildren().remove(mDefinitionText);
      if (mIndex == mWords.size())
      {
         mWordText.setText("Game");
         mDefinitionText.setText("Over");
         mLeftCardView.setVisible(true);
         mRightCardView.setVisible(true);
         mDefinitionText.setVisible(true);
         mShowDef.setDisable(true);
         mNextSet.setDisable(true);
      }
      else
      {
         mWordText.setText(mWords.get(mIndex));
         mDefinitionText.setText(mDefinitions.get(mIndex));
         mLeftCardView.setVisible(false);
         mRightCardView.setVisible(true);
         mDefinitionText.setVisible(false);
         mIndex++;
      }
      mWordCard.getChildren().add(mWordText);
      mDefCard.getChildren().add(mDefinitionText);
   }
}
