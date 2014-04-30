package efresh.service;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBoxBuilder;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Brother Neff
 * @author Adam Harper
 */
public class Alert
   extends Stage
{
   /**
    * Creates a new Alert object.
    *
    * @param pOwner The stage to create it on
    * @param pMsg1 The message to display
    * @param pMsg2 The message to display
    */
   public Alert(Stage pOwner, String pMsg1, String pMsg2)
   {
      setTitle("Alert");
      initOwner(pOwner);
      initStyle(StageStyle.UTILITY);
      initModality(Modality.APPLICATION_MODAL);

      Button btnOk = new Button("OK");

      btnOk.setOnAction(new EventHandler<ActionEvent>()
         {
            @Override
            public void handle(ActionEvent ae)
            {
               close();
            }
         });

      final Scene scene =
         new Scene(VBoxBuilder.create()
                              .children(new Label(pMsg1), new Label(pMsg2),
               btnOk).alignment(Pos.CENTER).padding(new Insets(10)).spacing(20.0)
                              .build());
      setScene(scene);
      sizeToScene();
      setResizable(false);
      show();
      hide();
      setX(750);
      setY(250);
   }
}
