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
                String hashKey = "name";
                if(hash.containsKey(hashKey))
                {
                    lessonName = hash.get(hashKey);
                    hash.remove(hashKey);
                }
                hashKey = "index";
                if(hash.containsKey(hashKey))
                {
                    index = Integer.parseInt(hash.get(hashKey));
                    hash.remove(hashKey);
                }
                hashKey="lesson";
                if(hash.containsKey(hashKey))
                {
                    lessonText = hash.get(hashKey);
                    hash.remove(hashKey);
                }
                for (HashMap.Entry<String, String> entry : hash.entrySet())
                    words.put(entry.getKey(),Util.wrapString(entry.getValue(),150));

                return true;
            }

            @Override
            public void onComplete() // end of file, check if we didnt find the lesson name/test or index
            {
                if(lessonName == null||lessonText == null||index == 0)
                {
                    Output.PopUpAlert(Lang.GenericError);
                    System.out.println("We didnt find lesson's name or the text or the index on the pathLesson " + name);
                    System.exit(0);
                }
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
        for (HashMap.Entry<String, String> entry : words.entrySet()) {
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
        for (HashMap.Entry<String, String> entry : words.entrySet()) {
            String key = entry.getKey();
            if(key.equals(name)) {
                Output.PopUpAlert(Lang.KeyAlreadyExists);
                return false;
            }
        }

        words.put(name,Util.wrapString(info,150));
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(name,info);
        FileHandler file = new FileHandler(path);
        if(!file.write(hashMap)) {
            Output.PopUp(Lang.GenericError);
            System.out.println("We got a problem at lesson addWord->file.write");
            return false;
        }
        Output.PopUp(Lang.AddKeyWordPopUp);
        return true;
    }
    public boolean removeTest()
    {
        File fileTest = new File("data/"+Controller.getChapters().get(idChapter).getName() + "_tests/" + file);
        if(!fileTest.delete())
        {
            Output.PopUp(Lang.GenericError);
            System.out.println("We got a problem at lesson removeTest->file.delete");
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
