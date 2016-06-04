package controllers;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import database.ShittyDatabaseException;
import enumerations.Feedback;
import main.Main;
import views.subviews.FeedbackView;

public class FeedbackController extends Controller {

    private Main application;

    @Override
    public void refresh() {

    }

    @Override
    public void refill() {

    }

    @Override
    public void fetch() {

    }

    public void showError(Exception e) {

        String message = "";
        Feedback feedback = null;
        if ( e instanceof CommunicationsException
                || e instanceof ShittyDatabaseException) {
            message =
                    "Geen connectie met de database mogelijk,\n" +
                    "check uw internet verbidning of meld dit bij de systeembeheerder";
            feedback = Feedback.ERROR;
        }

        if ( e instanceof NullPointerException) {
            message = "Er is iets mis gegaan!";
            feedback = Feedback.WARNING;
        }

        new FeedbackView(application.getWindow(), message, feedback);
    }

    public void setApplication(Main application) {
        this.application = application;
    }
}
