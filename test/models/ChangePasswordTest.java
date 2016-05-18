package models;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import controllers.ControllerFactory;
import controllers.SessionController;
import controllers.UserController;
import database.Database;

public class ChangePasswordTest {
    
    private UserController userController;
    private Database database;
          
    @Before
    public void setUp() throws Exception {
        database = new Database();
        userController = new ControllerFactory().GetUserController();               
        userController.insertUser("changepasswordtest", "test");
    }
    
    @Test
    public void succesfullPasswordChange() throws Exception {
        userController.login("changepasswordtest", "test");
        userController.changePassword("nieuwwachtwoord");
        userController.refresh();
        assertTrue(userController.getUser("changepasswordtest").getPassword().equals("nieuwwachtwoord"));
    }   
    
    @Test
    public void unsuccesfullPasswordChanged() throws Exception {
        userController.login("changepasswordtest", "test");
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
