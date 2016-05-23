package views.components;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
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
            Label userLabel = new Label(item.getUser().getName() + " ");
            Region fill = new Region();
            fill.setPrefWidth(10);
            HBox stampBox = new HBox(userLabel, new Separator(Orientation.VERTICAL), timeLabel);
            stampBox.setMaxWidth(stampBox.getPrefWidth());
            timeLabel.getStyleClass().setAll("chat-label");
            userLabel.getStyleClass().setAll("chat-label");

            text.textProperty().bind(itemProperty().asString());
            textFlow.prefWidthProperty().bind(list.widthProperty().divide(1.4));
            textBox.setMaxWidth(Control.USE_PREF_SIZE);
            setGraphic(listBox);

            if(item.getUser().equals(currentUser)) {
                textBox.getStyleClass().setAll("chat-owned");
                listBox.setAlignment(Pos.CENTER_RIGHT);
                stampBox.setAlignment(Pos.CENTER_RIGHT);
                stampBox.getChildren().add(fill);
                text.setStyle("-fx-fill: #ffffff");
            }
            else {
                textBox.getStyleClass().setAll("chat-norm");
                listBox.setAlignment(Pos.CENTER_LEFT);
                stampBox.setAlignment(Pos.CENTER_LEFT);
                stampBox.getChildren().add(0, fill);
                text.setStyle("-fx-fill: #000000");
            }
            listBox.getChildren().add(stampBox);
        }
    }
}
