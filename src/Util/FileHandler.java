package Util;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileHandler
{
    public interface GetReadDataCallback
    {
        // true - continue reading
        // false - stop and close the file
        default boolean getHash(Map<String,String> hash)
        {
            return true;
        }
        default void onError(String line)
        {
            System.out.println(line);
        }
        default void onComplete()
        {
        }
    }
    private final String filePath;

    public FileHandler(String filePath)
    {
        this.filePath = filePath;
    }

    private String decodeHtml(String str)
    {
        return str.replaceAll("&spc", " ")
                .replaceAll("&nw","\n")
                .replaceAll("&rw","\r")
                .replaceAll("&tw","\t")
                .replaceAll("&asp","&");
    }

    private String encodeHtml(String str)
    {
        return str.replaceAll("&","&asp")
                .replaceAll(" ","&spc")
                .replaceAll("\n","&nw")
                .replaceAll("\r","&rw")
                .replaceAll("\t","&tw");
    }

    public void read(GetReadDataCallback output)
    {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                Map<String,String> hashMap = new HashMap<>();
                String[] tokens = line.split(" ");
                for (int i = 0; i < tokens.length; i += 2)
                    hashMap.put(decodeHtml(tokens[i]), decodeHtml(tokens[i + 1]));

                if(!output.getHash(hashMap))
                {
                    reader.close();
                    return;
                }
            }
        }
        catch (IOException e)
        {
            output.onError(e.toString());
        }
        output.onComplete();
    }

    /* false = error
    true = success
    */
    private boolean editFile(String key,String value, Map<String,String> hash, int typeEdit)
    {
        try
        {
            File inputFile = new File(filePath);
            File tempFile = new File("temp.txt");
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String line;
            while ((line = reader.readLine()) != null)
            {
                Map<String, String> tempHash = new HashMap<>();
                String[] tokens = line.split(" ");
                for (int i = 0; i < tokens.length; i += 2)
                    tempHash.put(decodeHtml(tokens[i]), decodeHtml(tokens[i + 1]));

                if (!tempHash.get(key).equals(value))
                {
                    writer.write(line + System.lineSeparator());
                    continue;
                }

                if (typeEdit == 2)
                    continue;

                StringBuilder aux = new StringBuilder();
                if (typeEdit == 0)
                {
                    tempHash.putAll(hash);
                    for (Map.Entry<String, String> entry : tempHash.entrySet())
                        aux.append(encodeHtml(entry.getKey())).append(" ").append(encodeHtml(entry.getValue())).append(" ");
                }
                else if (typeEdit == 1)
                {
                    for (Map.Entry<String, String> entry : hash.entrySet())
                        aux.append(encodeHtml(entry.getKey())).append(" ").append(encodeHtml(entry.getValue())).append(" ");
                }
                writer.write(aux + System.lineSeparator());
            }

            writer.close();
            reader.close();

            return inputFile.delete() && tempFile.renameTo(inputFile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /* false = error
    true = success
     */
    public boolean edit(String key, String value, Map<String,String> hash)
    {
        return editFile(key,value,hash,0);
    }

    /* false = error
    true = success
    */
    public boolean replace(String key, String value, Map<String,String> hash)
    {
        return editFile(key,value,hash,1);
    }

    /* false = error
    true = success
    */
    public boolean deleteLine(String key, String value)
    {
        return editFile(key,value,null,2);
    }

    /* false = error
    true = success
     */
    public boolean write(Map<String,String> hash) {
        try (FileWriter fileWriter = new FileWriter(filePath, true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter))
        {
            StringBuilder aux= new StringBuilder();
            for (Map.Entry<String, String> entry : hash.entrySet())
                aux.append(encodeHtml(entry.getKey())).append(" ").append(encodeHtml(entry.getValue())).append(" ");

            bufferedWriter.write(aux.toString());
            bufferedWriter.newLine();
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


}