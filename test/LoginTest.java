import org.junit.Test;
import static org.junit.Assert.*;

public class LoginTest
{
    @Test
    public void testUsernameCorrectlyFormatted()
    {
        Login login = new Login();
        assertTrue(login.CheckUserName("lbj_6"));
    }

    @Test
    public void testUsernameIncorrectlyFormatted()
    {
        Login login = new Login();
        assertFalse(login.CheckUserName("lebron james"));
    }

    @Test
    public void testPasswordMeetsComplexity()
    {
        Login login = new Login();
        assertTrue(login.CheckPassWordComplaxity("Ch&&8sec@ke9!"));
    }

    @Test
    public void testPasswordDoesNotMeetComplexity()
    {
        Login login = new Login();
        assertFalse(login.CheckPassWordComplaxity("password"));
    }

    @Test
    public void testCellPhoneCorrectlyFormatted()
    {
        Login login = new Login();
        assertTrue(login.CheckCellPhoneNumber("+27838968976"));
    }

    @Test
    public void testCellPhoneIncorrectlyFormatted()
    {
        Login login = new Login();
        assertFalse(login.CheckCellPhoneNumber("08966553"));
    }

    @Test
    public void testRegisterUserUsernameMessage()
    {
        Login login = new Login();
        assertEquals("Username is not correctly formatted,please ensure that your username contains an underscore and is no more than five characters in length.",
                login.registerUser("LeBron", "James", "lebron james", "Ch&&8sec@ke9!", "+27838968976"));
    }

    @Test
    public void testRegisterUserPasswordMessage()
    {
        Login login = new Login();
        assertEquals("Password is not correctly formatted,please ensure that the password contains at least eight characters,a capital letter, a number, and a special character.",
                login.registerUser("LeBron", "James", "lbj_6", "password", "+27838968976"));
    }

    @Test
    public void testRegisterUserCellPhoneMessage()
    {
        Login login = new Login();
        assertEquals("Cell phone number is incorrectly formatted or does not contain international code,please correct the number and try again.",
                login.registerUser("LeBron", "James", "lbj_6", "Ch&&8sec@ke9!", "08966553"));
    }

    @Test
    public void testLoginSuccessful()
    {
        Login login = new Login();
        login.registerUser("LeBron", "James", "lbj_6", "Ch&&8sec@ke9!", "+27838968976");
        assertTrue(login.LoginUser("lbj_6", "Ch&&8sec@ke9!"));
    }

    @Test
    public void testLoginFailed()
    {
        Login login = new Login();
        login.registerUser("LeBron", "James", "lbj_6", "Ch&&8sec@ke9!", "+27838968976");
        assertFalse(login.LoginUser("lbj_6", "wrongpassword"));
    }

    @Test
    public void testReturnLoginStatusSuccess()
    {
        Login login = new Login();
        login.registerUser("LeBron", "James", "lbj_6", "Ch&&8sec@ke9!", "+27838968976");
        assertEquals("welcome LeBron, James welcome back again", login.returnLoginStatus(true));
    }

    @Test
    public void testReturnLoginStatusFailure()
    {
        Login login = new Login();
        assertEquals("Username or password is wrong please try again", login.returnLoginStatus(false));
    }
}
