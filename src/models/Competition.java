package models;

import java.util.ArrayList;

import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;

import javafx.scene.Node;
import sun.security.action.GetBooleanAction;

public class Competition {

	private int ID;
	private User owner;
	private String competitionName;
    private ArrayList<User> players;
    
    
	public void setPlayers(ArrayList<User> players) {
		this.players = players;
	}
	
	public void addPlayer(User user){
	    players.add(user);
	}


	public ArrayList<User> getPlayers() {
		return players;
	}
	
	public User getOwner(){
	    return owner;
	}

	public boolean containsUser(User user){
		for(User u : players){
			if(u.equals(user)){
				return true;
			}
		}
		return  false;

	}
	
	public Competition(User user, String competitionName){
	    this.owner             = user;
	    this.competitionName   = competitionName;
	    
	}

	public Competition(int ID, User owner, String competitionName,ArrayList<User> players){
		this.ID 				= ID;
		this.owner 				= owner;
		this.competitionName 	= competitionName;
		this.players			= players;
	}
	
	public int getID(){
		return ID;
	}
	
	public int getAmountOfPlayers(){
		return players.size();
	}

	public String getName(){
		return competitionName;
	}
	
	public String toString(){
		return owner.getName() + " - " + competitionName;
	}

	
	
	
	
}
