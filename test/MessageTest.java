import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

public class MessageTest
{
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    Message messages;

    @Before
    public void setUp()
    {
        messages = new Message();
        messages.messageID = "0012345678";
        messages.number = 0;
        messages.recipient = "+27718693002";
        messages.message = "Hi Mike, can you join us for dinner tonight?";
        messages.filename = new File(folder.getRoot(), "messages.json").getPath();
    }

    @Test
    public void testMessageLengthSuccess()
    {
        messages.message = "a".repeat(250);
        assertTrue(messages.checkMessageLength());
        assertEquals("Message ready to send", messages.messageStatus());
    }

    @Test
    public void testMessageLengthFailure()
    {
        messages.message = "a".repeat(251);
        assertFalse(messages.checkMessageLength());
        assertEquals("Message exceeds 250 characters by 1; please reduce the size", messages.messageStatus());
        messages.message = "a".repeat(300);
        assertEquals("Message exceeds 250 characters by 50; please reduce the size", messages.messageStatus());
    }

    @Test
    public void testRecipientSuccess()
    {
        assertEquals("Cell phone number successfully captured", messages.checkRecipientCell());
    }

    @Test
    public void testRecipientFailure()
    {
        messages.recipient = "0857595889";
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again", messages.checkRecipientCell());
    }

    @Test
    public void testMessageHashes()
    {
        String[] text = {"Hi Mike, can you join us for dinner tonight?", "Hi Keegan, did you receive the payment?"};
        String[] hashes = {"00:0:HITONIGHT", "00:1:HIPAYMENT"};
        for (int i = 0; i < text.length; i++)
        {
            messages.message = text[i];
            messages.number = i;
            assertEquals(hashes[i], messages.createMessageHash());
        }
    }

    @Test
    public void testMessageIDValidation()
    {
        assertTrue(messages.checkMessageID());
        messages.messageID = "12345678901";
        assertFalse(messages.checkMessageID());
        messages.messageID = "123456789";
        assertFalse(messages.checkMessageID());
        messages.messageID = "abcdefghij";
        assertFalse(messages.checkMessageID());
    }

    @Test
    public void testGeneratedMessageIDs()
    {
        Message generated = new Message();
        ArrayList<String> ids = new ArrayList<>();
        for (int i = 0; i < 100; i++)
        {
            generated.createMessageID();
            assertTrue(generated.checkMessageID());
            assertFalse(ids.contains(generated.messageID));
            assertEquals(i, generated.number);
            ids.add(generated.messageID);
        }
    }

    @Test
    public void testSendMessage()
    {
        assertEquals("Message successfully sent", messages.SentMessage("1"));
        assertEquals(1, messages.returnTotalMessages());
        assertEquals("Message ID: 0012345678\nMessage Hash: 00:0:HITONIGHT\nRecipient: +27718693002\nMessage: Hi Mike, can you join us for dinner tonight?\n", messages.printMessages());
    }

    @Test
    public void testDisregardMessage()
    {
        assertEquals("Press 0 to delete the message", messages.SentMessage("2"));
        assertEquals(0, messages.returnTotalMessages());
        assertEquals("", messages.printMessages());
        assertFalse(new File(messages.filename).exists());
    }

    @Test
    public void testStoreMessage() throws Exception
    {
        assertEquals("Message successfully stored", messages.SentMessage("3"));
        assertEquals(0, messages.returnTotalMessages());
        assertEquals("", messages.printMessages());
        try (FileReader reader = new FileReader(messages.filename))
        {
            JsonArray stored = JsonParser.parseReader(reader).getAsJsonArray();
            assertEquals(1, stored.size());
            assertEquals("0012345678", stored.get(0).getAsJsonObject().get("messageID").getAsString());
            assertEquals("00:0:HITONIGHT", stored.get(0).getAsJsonObject().get("messageHash").getAsString());
            assertEquals(messages.recipient, stored.get(0).getAsJsonObject().get("recipient").getAsString());
            assertEquals(messages.message, stored.get(0).getAsJsonObject().get("message").getAsString());
        }
    }

    @Test
    public void testStorageKeepsEarlierMessages() throws Exception
    {
        assertEquals("Message successfully stored", messages.storeMessage());
        Message next = new Message();
        next.filename = messages.filename;
        next.createMessageID();
        next.recipient = "+27718693002";
        next.message = "Text with \"quotes\", a slash \\ and a newline\nGoodbye";
        assertEquals("Message successfully stored", next.storeMessage());
        try (FileReader reader = new FileReader(messages.filename))
        {
            JsonArray stored = JsonParser.parseReader(reader).getAsJsonArray();
            assertEquals(2, stored.size());
            assertEquals(messages.message, stored.get(0).getAsJsonObject().get("message").getAsString());
            assertEquals(next.message, stored.get(1).getAsJsonObject().get("message").getAsString());
        }
    }

    @Test
    public void testTotalOnlyCountsSentMessages()
    {
        messages.SentMessage("1");
        messages.SentMessage("2");
        messages.SentMessage("3");
        assertEquals(1, messages.returnTotalMessages());
        messages.SentMessage("1");
        assertEquals(2, messages.returnTotalMessages());
    }
}
