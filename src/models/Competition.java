package models;

import java.util.ArrayList;

public class Competition {

	private int ID;
	private User owner;
	private String competitionName;
	private ArrayList<User> players;
	
	public Competition(int ID, User owner, String competitionName){
		this.ID 				= ID;
		this.owner 				= owner;
		this.competitionName 	= competitionName;
		players					= new ArrayList<User>();
	}
	
	public int getID(){
		return ID;
	}
	
	public int getAmountOfPlayers(){
		return players.size();
	}
	
	
	
}
