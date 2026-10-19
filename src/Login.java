public class Login extends Registration
{
    public boolean LoginUser(String UserName, String Password)
    {
        if (Username.equals(UserName) && this.Password.equals(Password))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public String returnLoginStatus(boolean LoginStatus)
    {
        if (LoginStatus)
        {
            return "welcome " + FirstName + ", " + LastName + " welcome back again";
        }
        else
        {
            return "Username or password is wrong please try again";
        }
    }
}