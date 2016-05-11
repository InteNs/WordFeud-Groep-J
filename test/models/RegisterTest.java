package models;

import controllers.ControllerFactory;
import controllers.UserController;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RegisterTest {

    private UserController userController;

    @Before
    public void setUp() throws Exception {
        userController = new ControllerFactory().GetUserController();       
    }

    @Test
    public void unvalidPassword() throws Exception {
        assertFalse(userController.isValidPassword("fail"));
        assertFalse(userController.isValidPassword("      "));
        assertFalse(userController.isValidPassword("   fail   "));
    }    
    

    @Test
    public void validPassword() throws Exception {
        assertTrue(userController.isValidPassword("succes!"));
    }

    @Test
    public void unvalidUsername() throws Exception {
        assertFalse(userController.isValidUsername("fail"));
        assertFalse(userController.isValidUsername("failed!"));
        assertFalse(userController.isValidUsername("      "));
        assertFalse(userController.isValidUsername("  fail   "));
    }
    
    

    @Test
    public void validUsername() throws Exception {
        assertTrue(userController.isValidUsername("succes"));
    }

    


}
