package Learn;

import Util.Util;
import Util.Output;
import Util.FileHandler;
import java.io.File;
import java.util.*;
import Controller.Controller;
import Lang.Lang;

public class Chapter
{
    private String name;
    private final Map<Integer, Lesson> lessons;
    private Test finaltest;

    public Chapter(String name)
    {
        this.name = name;
        this.lessons =  new LinkedHashMap<>();
    }
    public boolean removeTest()
    {
        File fileTest = new File("data/"+name + "_tests/finaltest.txt");
        if(!fileTest.delete())
        {
            Output.PopUp(Lang.GenericError);
            System.out.println("We got a problem at chapter removeTest->file.delete");
            return false;
        }
        finaltest = null;
        return true;
    }
    public void init()
    {
        List<Lesson> lessonsList = new ArrayList<>();
        String key = "data/"+name+"_lessons";
        int currChapter = Controller.getChapters().size();
        if(Util.Director(key)) // the folder with lessons exists, so we read the files in it
        {
            String fileName;
            File folder = new File(key);
            File[] files = folder.listFiles();
            if (files != null)
            {
                for (File file : files)
                {
                    if (file.isFile() && !file.getName().equals("clients.txt"))
                    {
                        fileName=file.getName();
                        Lesson lesson = new Lesson(currChapter,key + "/" + fileName,fileName);
                        lessonsList.add(lesson);
                    }
                }
            }
        }

        Collections.sort(lessonsList);

        int index;
        for (Lesson lesson : lessonsList)
        {
            index = lesson.getIndex();
            lessons.put(index, lesson);
            lessons.get(index).init();
        }
        key = "data/"+name+"_tests";
        if(Util.Director(key)) // the folder with tests exists, so we check if the final test exists
        {
            File file = new File(key + "/finaltest.txt");

            if (file.exists())
                finaltest = new Test(currChapter, -1);
        }
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        try {
            String[] location = {"_lessons","_tests" };
            for(int i=0;i<2;++i)
            {
                File currFolder = new File("data/" + this.name + location[i]);
                File destFolder = new File("data/" + name + location[i]);
                if (!currFolder.renameTo(destFolder))
                {
                    Output.PopUpAlert(Lang.ErrorChangeChapterName);
                    return;
                }
            }
        }
        catch (Exception e)
        {
            Output.PopUpAlert(Lang.ErrorChangeChapterName);
            System.out.println("We found this error: "+e.getMessage());
            return;
        }
        FileHandler file = new FileHandler("data/chapters.txt");
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("name",name);

        if(file.edit("name",this.name,hashMap))
            Output.PopUp(Lang.SuccessEditData);
        else
        {
            Output.PopUpAlert(Lang.ErrorChangeChapterName);
            System.out.println("We found a problem at chapter file edit");
            return;
        }
        this.name = name;
    }

    public Map<Integer, Lesson> getLessons()
    {
        return lessons;
    }
    public Test getFinalTest()
    {
        return finaltest;
    }
    public int getLessonsSize()
    {
        return lessons.size();
    }
    public int getTestsSize()
    {
        int size= 0;
        for (int i = 1,szLesson=lessons.size(); i <= szLesson; ++i)
            if (lessons.get(i).hasTest())
                ++size;

        if(finaltest != null)
            ++size;
        return size;
    }
    public void setTest(Test test)
    {
        this.finaltest=test;
    }
    public boolean hasTest()
    {
        return finaltest!=null;
    }
}
