package controllers;

import database.access.CompetitionDAO;
import database.access.UserDAO;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Competition;
import models.User;
import sun.security.krb5.internal.ccache.CCacheInputStream;

import java.util.ArrayList;

public class CompetitionController extends Controller {

    private ArrayList<Competition> competitions;
    private ObjectProperty<Competition> selectedCompetition = new SimpleObjectProperty<>();
    

    public CompetitionController(ControllerFactory factory) {
        super(factory);
    }

    public Competition getSelectedCompetition() {
        return selectedCompetition.get();
    }

    public ObjectProperty<Competition> selectedCompetitionProperty() {
        return selectedCompetition;
    }

    public void setSelectedCompetition(Competition competition) {
        selectedCompetition.set(competition);
    }
    
    public boolean createCompetition(String competitionName){
    	if(userHasCompetition()){
    	    return false;    	   
    	}
    	CompetitionDAO.insertCompetition(competitionName, getSession().getCurrentUser());
    	refresh();
    	// onderstaande code klopt maar werkt niet. 
    	for(Competition c: competitions){
    	    if(getSession().getCurrentUser().equals(c.getOwner())){
    	        CompetitionDAO.insertPlayer(getSession().getCurrentUser().getName(), c.getID()); 
    	    }
    	
        }
        return true;
    	
    }
    
  
    
    public boolean userHasCompetition(){
       for(Competition c: competitions){
            if(getSession().getCurrentUser().equals(c.getOwner())){
                return true;
            }
        }
        return false;
    }
    
    public boolean isValidCompetitionName(String competitionName) {
        return competitionName.length() >= 5 & competitionName.length() <= 25
                && competitionName.matches("[a-zA-Z0-9]+");
    }

    public ArrayList<User> getUser(Competition comp) {
        return comp.getPlayers();
    }

    public ObservableList<Competition> getCompetitions() {
        return FXCollections.observableArrayList(competitions);
    }

    public ObservableList<String> getCompetitions(User user) {
        ArrayList<String> competitionList = new ArrayList<>();
        for (Competition c : getCompetitions()) {
            if (c.containsUser(user)) {
                competitionList.add(c.getName());
            }
        }
        return FXCollections.observableArrayList(competitionList);
    }

    @Override
    public void refresh() {
        this.competitions = CompetitionDAO.selectCompetition();
    }
}
