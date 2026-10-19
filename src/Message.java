import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Message
{
    String messageID = "";
    String recipient = "";
    String message = "";
    String filename = "messages.json";
    int number = -1;
    int total;
    ArrayList<String> ids = new ArrayList<>();
    ArrayList<String> sent = new ArrayList<>();

    public void createMessageID()
    {
        Random random = new Random();
        do
        {
            messageID = "" + (1000000000L + random.nextLong(9000000000L));
        }
        while (ids.contains(messageID));
        ids.add(messageID);
        number++;
    }

    public boolean checkMessageID()
    {
        return messageID.matches("[0-9]{10}");
    }

    public String checkRecipientCell()
    {
        Registration registration = new Registration();
        if (registration.CheckCellPhoneNumber(recipient))
        {
            return "Cell phone number successfully captured";
        }
        else
        {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again";
        }
    }

    public boolean checkMessageLength()
    {
        return message.length() <= 250;
    }

    public String messageStatus()
    {
        if (checkMessageLength())
        {
            return "Message ready to send";
        }
        else
        {
            return "Message exceeds 250 characters by " + (message.length() - 250) + "; please reduce the size";
        }
    }

    public String createMessageHash()
    {
        String[] words = message.trim().split("\\s+");
        String first = words[0].replaceAll("[^a-zA-Z0-9]", "");
        String last = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "");
        return (messageID.substring(0, 2) + ":" + number + ":" + first + last).toUpperCase();
    }

    public String SentMessage(String choice)
    {
        if (choice.equals("1"))
        {
            sent.add(messageDetails());
            total++;
            return "Message successfully sent";
        }
        else if (choice.equals("2"))
        {
            return "Press 0 to delete the message";
        }
        else if (choice.equals("3"))
        {
            return storeMessage();
        }
        else
        {
            return "Invalid option";
        }
    }

    public String messageDetails()
    {
        return "Message ID: " + messageID + "\nMessage Hash: " + createMessageHash()
                + "\nRecipient: " + recipient + "\nMessage: " + message;
    }

    public String printMessages()
    {
        String details = "";
        for (int i = 0; i < sent.size(); i++)
        {
            details = details + sent.get(i) + "\n";
        }
        return details;
    }

    public int returnTotalMessages()
    {
        return total;
    }

    public String storeMessage()
    {
        try
        {
            JsonArray stored = new JsonArray();
            File file = new File(filename);
            if (file.exists())
            {
                try (FileReader reader = new FileReader(file))
                {
                    stored = JsonParser.parseReader(reader).getAsJsonArray();
                }
            }
            JsonObject item = new JsonObject();
            item.addProperty("messageID", messageID);
            item.addProperty("messageHash", createMessageHash());
            item.addProperty("recipient", recipient);
            item.addProperty("message", message);
            stored.add(item);
            try (FileWriter writer = new FileWriter(file))
            {
                new GsonBuilder().setPrettyPrinting().create().toJson(stored, writer);
            }
            return "Message successfully stored";
        }
        catch (IOException error)
        {
            return "Message could not be stored";
        }
    }
}
