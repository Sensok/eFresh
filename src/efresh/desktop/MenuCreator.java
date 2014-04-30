package efresh.desktop;

import javafx.application.Application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;

import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This will create things for the menu to display
 * @author Adam Harper and Jordan Jensen
 */
public final class MenuCreator
{
   /**
    * Menubar for the main GUI
    */
   private MenuBar menuBar;

   /**
    * This will make a Separator Menu Item
    */
   private SeparatorMenuItem separatorMenuItem;

   /**
    * Menu for gui
    */
   private Menu menu1;

   /**
    * Menu 2 for gui
    */
   private Menu menu2;

   /**
    * menu 3 for gui
    */
   private Menu menu3;

   /**
    * MenuItems for game options
    */
   private MenuItem gameOptions;

   //private MenuItem instruct;
   /**
    * Creates a new MenuCreator object.
    */
   public MenuCreator()
   {
      menuBar = new MenuBar();
      createMenu();
      createMenuItems();
      setMenuOptions();
   }

   /**
    * MenuBar to return the menu bar to main
    *
    * @return MenuBar the menu bar
    */
   public MenuBar getItems()
   {
      return menuBar;
   }

   /**
    * Creates Menu for the main GUI
    */
   private void createMenu()
   {
      menu1 = new Menu("File");
      menu2 = new Menu("Edit");
      menu3 = new Menu("Help");
   }

   /**
    * This will create the menu items with keyboard short
    * cuts
    */
   private void createMenuItems()
   {
      String comm =
         System.getProperty("os.name").contains("Mac") ? "Meta" : "Ctrl";
      Main.exit = new ActionEvents("Exit", comm + "+Q", null);
      Main.browser = new ActionEvents("Explorer", comm + "+E", Main.browserTab);
      Main.games = new ActionEvents("Games", comm + "+G", Main.gamesTab);

      if (! (Main.mUser.contains("Student")))
      {
         Main.random = new ActionEvents("Randomize", comm + "+R", Main.randomTab);
         Main.studentInfo = new ActionEvents("Student Info", comm + "+S",
               Main.studentTab);
      }

      Main.importData = new ActionEvents("Import/Remove", comm + "+I",
            Main.impAndRemTab);
      separatorMenuItem = new SeparatorMenuItem();
      gameOptions = new MenuItem("Game Options");
      Main.about = new MenuItem("About");
      Main.instruct = new MenuItem("Instructions");

      Main.about.setOnAction(new EventHandler<ActionEvent>()
         {
            @Override
            public void handle(ActionEvent event)
            {
               final Stage myDialog = new Stage();
               myDialog.initModality(Modality.WINDOW_MODAL);

               Button okButton = new Button("CLOSE");
               okButton.setOnAction(new EventHandler<ActionEvent>()
                  {
                     @Override
                     public void handle(ActionEvent arg0)
                     {
                        myDialog.close();
                     }
                  });

               Image screenTemp =
                  new Image(
                     "http://3.bp.blogspot.com/_x8iFo2g4qP8/TTIhSasEO-I/AAAAAAAAAA0/Id49lC3PNyQ/s1600/Moraine+Lakes+Ten+Peaks.jpg");
               ImageView screenDemo = new ImageView();
               screenDemo.setFitWidth(100);
               screenDemo.setFitHeight(100);
               screenDemo.setImage(screenTemp);

               Scene myDialogScene =
                  new Scene(VBoxBuilder.create()
                                       .children(new Text(
                           "Welcome, To the About Page!\n Please Enjoy! \n This program was brought to you " +
                           "by Adam Harper, Jordan Jensen and Jake Stevens.\n\n If you need to learn more " +
                           "about how to use GERSII then I would recommend going and reading the instructions " +
                           "tab.\n This program was made for Brother Neff for cs246.\n\n We are all CS majors, " +
                           "ever since Jordan converted from the Dark Side!\n"),
                        screenDemo, new Text("\n"), okButton)
                                       .alignment(Pos.CENTER)
                                       .padding(new Insets(10)).build());

               myDialog.setScene(myDialogScene);
               myDialog.setTitle("About Us");
               myDialog.show();
            }
         });

      Main.instruct.setOnAction(new EventHandler<ActionEvent>()
         {
            @Override
            public void handle(ActionEvent event)
            {
               final Stage myDialog = new Stage();
               myDialog.initModality(Modality.WINDOW_MODAL);

               Button okButton = new Button("CLOSE");
               okButton.setOnAction(new EventHandler<ActionEvent>()
                  {
                     @Override
                     public void handle(ActionEvent arg0)
                     {
                        myDialog.close();
                     }
                  });

               Scene myDialogScene =
                  new Scene(VBoxBuilder.create()
                                       .children(new Text(
                           "Welcome, To the Instructions Page!\n Please Enjoy!"),
                        okButton).alignment(Pos.CENTER).padding(new Insets(10))
                                       .build());

               myDialog.setScene(myDialogScene);
               myDialog.setTitle("Instructions");
               myDialog.show();
            }
         });
   }

   /**
    * This will set the menu options the menu bar
    */
   public void setMenuOptions()
   {
      menu1.getItems().add(Main.exit.getItem());

      if (Main.mUser.contains("Student"))
      {
         menu2.getItems()
              .addAll(Main.games.getItem(), Main.browser.getItem(),
            Main.importData.getItem(), separatorMenuItem, gameOptions);
      }
      else
      {
         menu2.getItems()
              .addAll(Main.games.getItem(), Main.browser.getItem(),
            Main.random.getItem(), Main.studentInfo.getItem(),
            Main.importData.getItem(), separatorMenuItem, gameOptions);
      }

      menu3.getItems().addAll(Main.about, Main.instruct);
      menuBar.getMenus().addAll(menu1, menu2, menu3);
   }
}
