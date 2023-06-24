package Learn;

import Controller.Controller;
import Lang.Lang;
import Util.Output;
import Util.FileHandler;
import Util.Util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Lesson implements Comparable<Lesson>
{
    private final String path,file;
    private String lessonName,lessonText;
    private int index;
    private final int idChapter;
    private final Map<String,String> words;
    private Test test;
    public Lesson(int idChapter,String path,final String name)
    {
        words = new HashMap<>();
        this.idChapter = idChapter;
        this.path = path;
        this.file = name;

        FileHandler file = new FileHandler(path);
        file.read(new FileHandler.GetReadDataCallback()
        {
            public boolean getHash(Map<String, String> hash)
            {
                // Check if the key 'name' exists in the line of the file.
                // If it does, we assign the corresponding value to the variable 'lessonName'
                String hashKey = "name";
                if(hash.containsKey(hashKey))
                {
                    lessonName = hash.get(hashKey);
                    hash.remove(hashKey);
                }
                // Check if the key 'index' exists in the line of the file.
                // If it does, we assign the corresponding value to the variable 'index'
                hashKey = "index";
                if(hash.containsKey(hashKey))
                {
                    index = Integer.parseInt(hash.get(hashKey));
                    hash.remove(hashKey);
                }
                // Check if the key 'lesson' exists in the line of the file.
                // If it does, we assign the corresponding value to the variable 'lessonText'
                hashKey="lesson";
                if(hash.containsKey(hashKey))
                {
                    lessonText = hash.get(hashKey);
                    hash.remove(hashKey);
                }
                // The remaining values are keywords represented as pairs of <key, value>
                // where the key represents the word(s) and the value represents the explanation.
                for (HashMap.Entry<String, String> entry : hash.entrySet())
                    words.put(entry.getKey(),Util.wrapString(entry.getValue(),150));

                return true; // keep reading
            }

            @Override
            public void onComplete() // end of file, check if we didn't find the lesson name/test or index
            {
                if(lessonName != null && lessonText != null && index != 0) // the index starts from 1.
                    return;

                Output.PopUpAlert(Lang.GenericError);
                System.out.println("We didn't find the lesson's name, text, or index in the pathLesson " + name);
                System.exit(0);
            }
        });
    }
    public void init()
    {
        File fileTest = new File(this.path.replaceAll("_lessons/","_tests/"));
        if(fileTest.exists())
            this.test = new Test(idChapter,getIndex());
    }
    public String getLessonText()
    {
        String aux = lessonText;
        for (HashMap.Entry<String, String> entry : words.entrySet())
        {
            String key = entry.getKey();
            aux = aux.replaceAll(key,"<a href='"+key+"'>"+key+"</a>");
        }
        return aux;
    }
    public int getIndex()
    {
        return index;
    }
    public Test getTest()
    {
        return test;
    }
    public void setTest(Test test)
    {
        this.test = test;
    }
    public int compareTo(Lesson other)
    {
        return Integer.compare(this.index, other.index);
    }
    public boolean hasTest()
    {
        return test != null;
    }
    public String getName()
    {
        return lessonName;
    }
    public boolean addWord(String name, String info)
    {
        for (HashMap.Entry<String, String> entry : words.entrySet())
        {
            if(!entry.getKey().equals(name))
                continue;

            Output.PopUpAlert(Lang.KeyAlreadyExists);
            return false;
        }

        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(name,info);
        FileHandler file = new FileHandler(path);
        if(!file.write(hashMap))
        {
            Output.PopUp(Lang.GenericError);
            System.out.println("We encountered an issue while adding a keyword to the lesson("+this.lessonName+").");
            return false;
        }
        words.put(name,Util.wrapString(info,150));
        Output.PopUp(Lang.AddKeyWordPopUp);
        return true;
    }
    public boolean removeTest()
    {
        File fileTest = new File("data/"+Controller.getChapters().get(idChapter).getName() + "_tests/" + file);
        if(!fileTest.delete())
        {
            Output.PopUp(Lang.GenericError);
            System.out.println("We encountered a problem while removing the test from the lesson("+this.lessonName+").");
            return false;
        }
        test = null;
        return true;
    }
    public Map<String,String> getWords()
    {
        return words;
    }
    public String getFileName()
    {
        return file;
    }
}
