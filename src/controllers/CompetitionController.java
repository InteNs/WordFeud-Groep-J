package controllers;

import database.access.CompetitionDAO;
import models.Competition;

import java.util.ArrayList;

public class CompetitionController extends Controller {

	private ArrayList<Competition> competitions;
	
	public CompetitionController(){
		this.competitions = CompetitionDAO.selectCompetition();
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
