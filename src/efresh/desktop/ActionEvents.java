package efresh.desktop;

import java.io.File;
import java.io.IOException;

import javafx.application.Platform;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.control.MenuItem;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCombination;

/**
 * Handles the Action Events for the Program
 * @author Adam Harper
 */
public final class ActionEvents
{
   /**
    * Menu Item for menu bar
    */
   private MenuItem mItem;

   /**
    * mName - Name of action event to handle
    */
   private String mName;

   /**
    * The controls to bind to the menu item
    */
   private String mControls;

   /**
    * Tab to bind to mControl and the menu item
    */
   private Tab mTab;

   /**
    * Creates a new ActionEvents object.
    *
    * @param pName Name of the menu item to put action event on
    * @param pControls Controls to add to the menu item
    * @param pTab Tab to link it to
    */
   public ActionEvents(String pName, String pControls, Tab pTab)
   {
      mItem = new MenuItem(pName);
      mName = pName;
      mControls = pControls;
      mTab = pTab;
      create();
   }

   /**
    * This will set the action events on the GUI to close it correctly
    */
   public void create()
   {
      mItem.setAccelerator(KeyCombination.keyCombination(mControls));
      mItem.setOnAction(new EventHandler<ActionEvent>()
         {
            @Override
            public void handle(ActionEvent t)
            {
               SingleSelectionModel<Tab> selectTab =
                  Main.tabPane.getSelectionModel();

               if (mName.equals("Exit"))
               {
                  File dir =
                     new File(System.getProperty("user.home") + System.getProperty("file.separator") +
                              "efresh-tmp");

                  if (dir.exists())
                  {
                  }

                  Platform.exit();
                  System.exit(0);
               }
               else
               {
                  selectTab.select(mTab);
               }
            }
         });
   }


   /**
    * A getter for the menu item to return it to the main GUI
    *
    * @return MenuItem - mItem - The menu item to add the the GUI
    *
    */
   public MenuItem getItem()
   {
      return mItem;
   }
}
