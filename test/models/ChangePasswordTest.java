package models;

import controllers.ControllerFactory;
import controllers.SessionController;
import controllers.UserController;
import database.Database;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ChangePasswordTest {
    private UserController userController;
    private SessionController sessionController;
    private Database database;
          
    @Before
    public void setUp() throws Exception {
        database = new Database();
        ControllerFactory controllers = new ControllerFactory();
        userController = controllers.GetUserController();
        sessionController = controllers.getSessionController();
        userController.insertUser("changepasswordtest", "test");
    }
    
    @Test
    public void succesfullPasswordChange() throws Exception {
        sessionController.login("changepasswordtest", "test");
        userController.changePassword("nieuwwachtwoord");
        userController.refresh();
        assertTrue(userController.getUser("changepasswordtest").getPassword().equals("nieuwwachtwoord"));
    }   
    
    @Test
    public void unsuccesfullPasswordChanged() throws Exception {
        sessionController.login("changepasswordtest", "test");
        userController.changePassword("nieuwwachtwoord");
        userController.refresh();
        assertFalse(userController.getUser("changepasswordtest").getPassword().equals("verkeerdwachtwoord"));
    } 
    
    
    
    @After 
    public void deleteTestAcc (){
       database.delete("DELETE FROM accountrol WHERE account_naam = 'changepasswordtest';") ;
       database.delete("DELETE FROM account WHERE naam = 'changepasswordtest';");
    }
}
