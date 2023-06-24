package Util;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class Util
{
    /* 0 - valid
    1 - empty
    2 - invalid char
    3 - length < 2
     */
    public static int isValidName(String name)
    {
        if(name.trim().isEmpty())
            return 1;
        if (!name.matches("[a-zA-Z\\s]+"))
            return 2;
        if(name.trim().length() < 2)
            return 3;

        return 0;
    }
    /* 0 - valid
   1 - empty
   2 - invalid char
    */
    public static int isValidEmail(String email)
    {
        if(email.trim().isEmpty())
            return 1;
        if(!email.matches("^[A-Za-z\\d+_.-]+@[A-Za-z\\d.-]+$"))
            return 2;
        return 0;
    }
    /* 0 - valid
       1 - empty
       2 - invalid char
        */
    public static int isValidNumber(String number)
    {
        if(number.trim().isEmpty())
            return 1;
        if(!number.matches("\\d+"))
            return 2;
        return 0;
    }

    /* 0 - valid
    1 - empty
    2 - contains space
    3 - length < 3
    */
    public static int isValidUserName(String user)
    {
        if(user.isEmpty())
            return 1;
        if(user.contains(" "))
            return 2;
        if(user.length() < 3)
            return 3;
        return 0;
    }

    /* 0 - valid
    1 - empty
    2 - length < 6 || length > 32
    */
    public static int isValidPassword(String password)
    {
        if(password.isEmpty())
            return 1;
        if(password.length() < 6||password.length()>32)
            return 2;
        return 0;
    }
    public static String makeFileNameSafe(String fileName)
    {
        fileName = fileName.replaceAll("[/\\\\:*?\"<>|\\s]+", "_");
        int maxLength = 255;
        if (fileName.length() > maxLength)
            fileName = fileName.substring(0, maxLength);

        if(fileName.equals("clients"))
            fileName = fileName+generateRandomString(10);
        else if(fileName.equals("finaltest"))
            fileName = fileName+generateRandomString(10);
        return fileName;
    }
    public static boolean Director(String Path)
    {
        java.nio.file.Path path = Paths.get(Path);

        if (!Files.exists(path))
        {
            try
            {
                Files.createDirectories(path);
            }
            catch (Exception e)
            {
                System.out.println("Failed to create directory: " + e);
                System.exit(0);
            }
            return false;
        }
        return true;
    }
    public static String generateRandomString(int length)
    {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; ++i)
        {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }
    public static String wrapString(String input, int lineWidth)
    {
        StringBuilder sb = new StringBuilder(input);

        int i = 0;
        while (i + lineWidth < sb.length() && (i = sb.lastIndexOf(" ", i + lineWidth)) != -1)
            sb.replace(i, i + 1, "\n");

        return sb.toString();
    }
}
