package controllers;

import java.util.ArrayList;

import database.access.CompetitionDAO;
import models.Competition;
import models.User;

public class CompetitionController {

	private ArrayList<Competition> competitions;
	private User currentUser;
	
	public CompetitionController(){
		this.competitions = CompetitionDAO.selectCompetition();
	}
	
	public void setCurrentUser(User user){
		currentUser = user;
	}
	
	public ArrayList<Competition> getCompetitions(){
		if(currentUser != null){
			return competitions;
		}
		return new ArrayList<>();
	}
	
	public void refresh(){
		this.competitions = CompetitionDAO.selectCompetition();
	}
}
