package efresh.desktop;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * Browser for the GUI
 * @author Adam Harper and Jordan Jensen
 */
public class Browser
{
   /**
    * Standard home page for the Browser
    */
   public static final String DEFAULT_URL = "http://www.google.com";

   /**
    * Pane of the browser to be sent back to main in order to view the browser
    */
   public Pane mPane;

   /**
    * Creates a new Browser object.
    */
   public Browser()
   {
      mPane = new Pane();

      String version = System.getProperty("java.version");

      if (version.compareTo("1.7.0_15") >= 0)
      {
         Group root = new Group();

         WebView webView = new WebView();

         final WebEngine webEngine = webView.getEngine();
         webEngine.load(DEFAULT_URL);

         final TextField locationField = new TextField(DEFAULT_URL);
         webEngine.locationProperty().addListener(new ChangeListener<String>()
            {
               @Override
               public void changed(
                  ObservableValue<?extends String> observable, String oldValue,
                  String newValue)
               {
                  locationField.setText(newValue);
               }
            });

         EventHandler<ActionEvent> goAction =
            new EventHandler<ActionEvent>()
            {
               @Override
               public void handle(ActionEvent event)
               {
                  webEngine.load(locationField.getText().startsWith("www.")
                     ? locationField.getText()
                     : ("http://www." + locationField.getText()));
               }
            };

         locationField.setOnAction(goAction);

         Button goButton = new Button("Go");
         goButton.setDefaultButton(true);
         goButton.setOnAction(goAction);

         // Layout logic
         HBox hBox = new HBox(5);
         hBox.getChildren().setAll(locationField, goButton);
         HBox.setHgrow(locationField, Priority.ALWAYS);

         HBox webBox = new HBox();
         webBox.getChildren().add(webView);
         HBox.setHgrow(webView, Priority.ALWAYS);

         webView.setPrefWidth(900);

         VBox vBox = new VBox(5);
         vBox.getChildren().setAll(hBox, webBox);
         VBox.setVgrow(webView, Priority.ALWAYS);

         mPane.getChildren().addAll(vBox);
      }
      else
      {
         Label myLabel =
            new Label(
               "We are sorry for the inconvenince; however, your version of Java is out dated!");
         mPane.getChildren().add(myLabel);
      }
   }

   /**
    * Allows Main to be able to have access to this pane
    *
    * @return Pane the pane of the Browser Tab
    */
   public Pane getView()
   {
      return mPane;
   }
}
