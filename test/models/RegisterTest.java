package models;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import controllers.UserController;

public class RegisterTest {
	
	private UserController userController;

	public RegisterTest() {		
	}
	
	@Before
	public void setUp() throws Exception{
		userController = new UserController();
	}
		
	@Test
	public void unvalidPassword() throws Exception{
		assertFalse(userController.isValidPassword("fail"));
	}
	
	@Test
	public void validPassword() throws Exception{
		assertTrue(userController.isValidPassword("succes"));
	}
	
	@Test
	public void unvalidUsername() throws Exception{
		assertFalse(userController.isValidUsername("fail"));
		assertFalse(userController.isValidUsername("failed!"));
	}
	
	@Test
	public void validUsername() throws Exception{
		assertTrue(userController.isValidUsername("succes"));		
	}
	
	
	
	

}
