package views.components;

import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import models.Message;
import models.User;

public class ChatCell extends ListCell<Message> {
    private ListView<Message> list;
    private User currentUser;

    public ChatCell(ListView<Message> list, User currentUser) {
        this.list = list;
        this.currentUser = currentUser;
    }


    @Override
    protected void updateItem(Message item, boolean empty) {
        super.updateItem(item, empty);
        this.setStyle("-fx-background-color: white");

        if(empty) setGraphic(null);
        else {

            Text text = new Text();
            TextFlow textFlow = new TextFlow(text);
            HBox textBox = new HBox(textFlow);
            VBox listBox = new VBox(textBox);
            Label timeLabel = new Label(item.getTimeString());
            timeLabel.getStyleClass().setAll("time-label");
            text.textProperty().bind(itemProperty().asString());
            textFlow.prefWidthProperty().bind(list.widthProperty().divide(1.3));

            textBox.setMaxWidth(Control.USE_PREF_SIZE);
            setGraphic(listBox);
            if(item.getUser().equals(currentUser)) {
                textBox.getStyleClass().setAll("chat-owned");
                listBox.setAlignment(Pos.CENTER_RIGHT);
                text.setStyle("-fx-fill: #ffffff");
            }
            else {
                textBox.getStyleClass().setAll("chat-norm");
                listBox.setAlignment(Pos.CENTER_LEFT);
                text.setStyle("-fx-fill: #000000");
            }
            listBox.getChildren().add(timeLabel);
        }
    }
}
