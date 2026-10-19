import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.Test;
import static org.junit.Assert.*;

public class QuickChatTest
{
    String registration = "Katso\nMosime\nkat_1\nPassword1!\n+27718693002\n";
    String login = "kat_1\nPassword1!\n";

    public String runChat(String input)
    {
        InputStream originalInput = System.in;
        PrintStream originalOutput = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try
        {
            System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
            System.setOut(new PrintStream(output));
            new QuickChat();
        }
        finally
        {
            System.setIn(originalInput);
            System.setOut(originalOutput);
        }
        return output.toString(StandardCharsets.UTF_8);
    }

    @Test
    public void testFailedLoginCannotSend()
    {
        String output = runChat(registration + "kat_1\nwrong\n");
        assertTrue(output.contains("Username or password is wrong please try again"));
        assertFalse(output.contains("Welcome to QuickChat"));
        assertFalse(output.contains("Enter recipient number"));
    }

    @Test
    public void testSendDiscardLimitAndMenu()
    {
        String output = runChat(registration + login + "2\n2\n1\n+27718693002\nHi Mike, can you join us for dinner tonight?\n1\n+27718693002\nHi Keegan, did you receive the payment?\n2\n0\n1\n3\n");
        assertTrue(output.contains("Welcome to QuickChat"));
        assertTrue(output.contains("Coming Soon"));
        assertTrue(output.contains("Message successfully sent"));
        assertTrue(output.contains(":0:HITONIGHT"));
        assertTrue(output.contains("Press 0 to delete the message"));
        assertTrue(output.contains("Total messages sent: 1"));
        assertEquals(2, output.split("Enter recipient number:", -1).length - 1);
        assertEquals(1, output.split("Message successfully sent", -1).length - 1);
    }

    @Test
    public void testInvalidRecipientAndLongMessageCanBeCorrected()
    {
        String output = runChat(registration + login + "1\n1\n0857595889\n+27718693002\n" + "a".repeat(251) + "\nHi again\n1\n3\n");
        assertTrue(output.contains("Cell phone number is incorrectly formatted"));
        assertTrue(output.contains("Message exceeds 250 characters by 1"));
        assertTrue(output.contains("Message successfully sent"));
        assertTrue(output.contains("Total messages sent: 1"));
    }
}
