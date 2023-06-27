package Client;

import java.util.HashMap;
import java.util.Map;

import Controller.Controller;
import Learn.Chapter;
import Learn.Lesson;
import Util.FileHandler;
import Util.Util;

public class User extends Client
{
    private final Map<String,Integer> lessonsRead,testsResult;
    public User(String username, String name,String lastname,String email,int age)
    {
        super(username, name,lastname,email,age);
        lessonsRead = new HashMap<>();
        testsResult = new HashMap<>();
        Map<Integer,Chapter> chapters = Controller.getChapters();
        for(int i = 1,szChapter = chapters.size(); i<= szChapter; ++i)
        {
            final Chapter chapter = chapters.get(i);
            final String chapterName = chapter.getName();
            final int szLesson=chapter.getLessonsSize();
            Map<Integer, Lesson> lessons = chapter.getLessons();
            final String fileName = "data/"+chapterName+"_lessons/clients.txt";
            final FileHandler file = new FileHandler(fileName);
            file.read(new FileHandler.GetReadDataCallback()
            {
                public boolean getHash(Map<String, String> hash)
                {
                    if (!hash.get("user").equals(username)) // not our username, continue reading
                        return true;

                    for(int j=1;j<=szLesson;++j)
                    {
                        int index = lessons.get(j).getIndex();
                        String key = index+"_lesson";
                        if(hash.containsKey(key)) // we check if the user has read the lesson
                        {
                            String less = hash.get(key);
                            if(Util.isValidNumber(less) == 0)
                                lessonsRead.put(chapterName+"_"+index, Integer.parseInt(less));
                        }
                        key = index+"_test";
                        if(hash.containsKey(key)) // we check if the user has passed the test
                        {
                            String less = hash.get(key);
                            if(Util.isValidNumber(less) == 0)
                                testsResult.put(chapterName+"_"+index, Integer.parseInt(less));
                        }
                    }
                    String key = "final_test";
                    if(hash.containsKey(key)) // we check if the user has passed the final test
                    {
                        String less = hash.get(key);
                        if(Util.isValidNumber(less) == 0)
                            testsResult.put(chapterName+"_finaltest", Integer.parseInt(less));
                    }
                    return false;
                }
                @Override
                public void onComplete()
                {
                    // we didn't find the account in the load file, so we will register it
                    Map<String,String> hashTemp = new HashMap<>();
                    hashTemp.put("user",username);
                    if(!file.write(hashTemp))
                        System.out.println("We encountered a problem while writing the user "+username+" in the load file "+fileName+".");
                }
            });
        }
    }
    public int getTest(String name, int idLesson)
    {
        if(idLesson == 0)
        {
            String key = name+"_finaltest";
            if(testsResult.containsKey(key))
                return testsResult.get(key);
            return -1;
        }
        String key = name+"_"+idLesson;
        if(testsResult.containsKey(key))
            return testsResult.get(key);
        return -1;
    }
    public boolean getLesson(String name, int idLesson)
    {
        String key = name+"_"+idLesson;
        if(lessonsRead.containsKey(key))
            return lessonsRead.get(key) > 0;
        return false;
    }
    public boolean setLesson(String name, int idLesson)
    {
        lessonsRead.put(name+"_"+idLesson,1);
        FileHandler file = new FileHandler("data/"+name+"_lessons/clients.txt");
        Map<String,String> hashMap=new HashMap<>();
        hashMap.put(idLesson+"_lesson","1");
        return file.edit("user",username,hashMap);
    }
    public boolean setTest(String name, int idLesson, int points)
    {
        if(idLesson == 0)
            testsResult.put(name + "_finaltest", points);
        else
            testsResult.put(name+"_"+idLesson,points);

        FileHandler file = new FileHandler("data/"+name+"_lessons/clients.txt");
        Map<String,String> hashMap=new HashMap<>();
        if(idLesson == 0)
            hashMap.put("final_test",String.valueOf(points));
        else
            hashMap.put(idLesson+"_test",String.valueOf(points));
        return file.edit("user",username,hashMap);
    }
    public int getLessons(String name) // get the total number of lessons read in the chapter
    {
        int i = 0;
        for (Map.Entry<String, Integer> entry : lessonsRead.entrySet())
            if(entry.getKey().contains(name))
                i+=entry.getValue(); // 0 - not read | 1 - read
        return i;
    }
    public int getTests(String name)
    {
        int i = 0;
        for (Map.Entry<String, Integer> entry : testsResult.entrySet())
            if(entry.getKey().contains(name))
                i+=entry.getValue() > 0 ? 1 : 0;
        return i;
    }
}
