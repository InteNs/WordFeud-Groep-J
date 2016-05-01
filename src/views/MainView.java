package views;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.*;
import models.User;

import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

public class MainView implements Initializable {
    //@FXML private VBox loginView;
    @FXML private VBox welcomeView;
    @FXML private VBox registerView;
    @FXML private VBox loginView;
    

    @FXML private TabPane control;
    @FXML private SplitPane mainContent;
    @FXML private StackPane content;


    @FXML private GameListView gameListViewController;
    @FXML private LoginView    loginViewController;   //1st child of Stackpane
    @FXML private WelcomeView  welcomeViewController; //2nd child of Stackpane
    @FXML private RegisterView  registerViewController; //3th child of Stackpane
    
    //some sort of session placeholder
    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {    	
        loginViewController.setParent(this);
        welcomeViewController.setParent(this);
        gameListViewController.setParent(this);
        registerViewController.setParent(this);
        setContent(loginView);
    }

    @FXML
    public void switchLayout() {
        Collections.swap(mainContent.getItems(), 0, 1);
    }

    public void login(User user) {
        currentUser = user;
        control.setDisable(false);
        setContent(welcomeView);
        refresh();
    }
    
    public void register (){
    	setContent(registerView);
    }
    
    public void toLoginView (){
    	setContent(loginView);
    }
    /**
     * set set or add content to app's view (clears content if node == null)
     * @param node the node to set as content
     */
    public void setContent(Node node) {
        content.getChildren().forEach(child -> child.setVisible(false));

        if(content.getChildren().contains(node))
            content.getChildren().get(content.getChildren().indexOf(node)).setVisible(true);
        else
            content.getChildren().add(node);
    }

    @FXML
    public void refresh() {
        gameListViewController.refresh();
        welcomeViewController.refresh();
    }
}
