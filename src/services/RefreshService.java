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
    private Runnable refreshTask;

    public RefreshService(ControllerFactory controllerFactory, ArrayList<View> views, ProgressIndicator loadIndicator) {
        refreshTask = () -> {
            Platform.runLater(() -> loadIndicator.setVisible(true));
            controllerFactory.fetchControllers();
            Platform.runLater(() -> {
                controllerFactory.refreshControllers();
                views.forEach(View::refresh);
                loadIndicator.setVisible(false);
            });
        };
        buildScheduler();
    }

    public void stopRefresh() {
        scheduler.shutdown();
    }

    public void startRefresh() {
        stopRefresh();
        buildScheduler();
        scheduler.scheduleAtFixedRate(refreshTask, 0, 5, TimeUnit.SECONDS);
    }

    private void buildScheduler() {
        scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        });
    }
}
