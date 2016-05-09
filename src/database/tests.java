package database;


import controllers.CompetitionController;
import controllers.UserController;
import database.access.CompetitionDAO;
import database.access.UserDAO;
import models.Competition;
import models.User;

import javax.management.openmbean.CompositeType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by cedric on 5/4/2016.
 */
public class tests {
    static UserController userc = new UserController();
    static CompetitionController compc = new CompetitionController();

    public static void main(String[] args) {
        User user = new User("jager684");


      //  Competition comp = new Competition(1, user, "ditiseencomp", ArrayList users);
       // compc.getUser()

    }


}
