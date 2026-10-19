public class Registration
{
    String FirstName;
    String LastName;
    String Username;
    String Password;
    String CellPhoneNumber;

    public boolean CheckUserName(String Username)
    {
        return Username.contains("_") && Username.length() <= 5;
    }

    public boolean CheckPassWordComplaxity(String Password)
    {
        return Password.length() >= 8 &&
                Password.matches(".*[A-Z].*") &&
                Password.matches(".*[a-z].*") &&
                Password.matches(".*\\d.*") &&
                Password.matches(".*[^a-zA-Z0-9].*");
    }

    public boolean CheckCellPhoneNumber(String CellPhoneNumber)
    {
        return CellPhoneNumber.matches("\\+27[0-9]{9}");
    }

    public String registerUser(String fname, String lname, String username, String password, String cellphone)
    {
        if (!CheckUserName(username))
        {
            return "Username is not correctly formatted,please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        else if (!CheckPassWordComplaxity(password))
        {
            return "Password is not correctly formatted,please ensure that the password contains at least eight characters,a capital letter, a number, and a special character.";
        }
        else if (!CheckCellPhoneNumber(cellphone))
        {
            return "Cell phone number is incorrectly formatted or does not contain international code,please correct the number and try again.";
        }
        else
        {
            FirstName = fname;
            LastName = lname;
            Username = username;
            Password = password;
            CellPhoneNumber = cellphone;
            return "Username successfully captured.\nPassword successfully captured.\nCell phone number successfully added.\nRegistration successful, you can now log in.";
        }
    }

    public boolean registerStatus(String username, String password, String cellphone)
    {
        return CheckUserName(username) &&
                CheckPassWordComplaxity(password) &&
                CheckCellPhoneNumber(cellphone);
    }
}