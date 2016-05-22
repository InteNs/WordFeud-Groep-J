package views.components;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
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
        if(empty) setGraphic(null);
        else {
            Text text = new Text();
            text.wrappingWidthProperty().bind(this.widthProperty().subtract(40));
            text.textProperty().bind(itemProperty().asString());
            if(item.getUser().equals(currentUser)) {
                this.getStyleClass().clear();
                this.getStyleClass().add("chat-owned");
                text.setStyle("-fx-fill: #ffffff");
            }
            else {
                this.getStyleClass().clear();
                this.getStyleClass().add("chat-norm");
                text.setStyle("-fx-fill: #000000");
            }

            setGraphic(text);
        }
    }
}
