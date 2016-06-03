package services;


import controllers.ControllerFactory;
import javafx.application.Platform;
import javafx.scene.control.ProgressIndicator;
import views.View;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RefreshService {

    private ScheduledExecutorService scheduler;
    private ControllerFactory controllerFactory;
    private ArrayList<View> views;
    private ProgressIndicator loadIndicator;
    private Runnable refreshTask;

    public RefreshService(ControllerFactory controllerFactory, ArrayList<View> views, ProgressIndicator loadIndicator) {
        this.controllerFactory = controllerFactory;
        this.views = views;
        this.loadIndicator = loadIndicator;

        //Creates the task that scheduler should complete
       refreshTask = () -> {
           loadIndicator.setVisible(true);
           controllerFactory.fetchControllers();
           Platform.runLater(() -> {
               controllerFactory.refreshControllers();
               views.forEach(View::refresh);
               loadIndicator.setVisible(false);
           });
       };
        startRefresh();
    }

    public void stopRefresh(){
        scheduler.shutdown();
    }

    public void startRefresh(){
        //define and start live reload thread
        scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        });
        scheduler.scheduleAtFixedRate(refreshTask,0,5, TimeUnit.SECONDS);
    }
}
