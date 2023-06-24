package Learn;

import Controller.Controller;
import Util.FileHandler;
import Util.Output;
import Lang.Lang;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test
{
    private final List<Subject> subjects;
    private final String namefile,path;
    public Test(int idChapter, int idLesson)
    {
        subjects = new ArrayList<>();
        Chapter chapter = Controller.getChapters().get(idChapter);
        if(idLesson == -1)
            namefile = "finaltest.txt";
        else
            namefile = chapter.getLessons().get(idLesson).getFileName();

        path = "data/"+chapter.getName() + "_tests/" + namefile;
        FileHandler file = new FileHandler(path);
        file.read(new FileHandler.GetReadDataCallback()
        {
            @Override
            public boolean getHash(Map<String, String> hash)
            {
                Subject subj = new Subject();
                Map<String,Integer> hashMap = new HashMap<>();
                for (HashMap.Entry<String, String> entry : hash.entrySet())
                {
                    String key = entry.getKey();
                    if(key.equals("enunt"))
                        subj.setEnunt(entry.getValue());
                    else
                        hashMap.put(key,Integer.parseInt(entry.getValue()));
                }
                subj.setOptions(hashMap);
                subjects.add(subj);
                return true;
            }

            @Override
            public void onComplete() // we check if the subject is empty
            {
                if(subjects.size() == 0)
                {
                    Output.PopUpAlert(Lang.GenericError);
                    System.out.println("We didnt find any subject on the test "+namefile);
                    System.exit(0);
                }
            }
        });

    }
    public int getTotalPoints()
    {
        int points=0;
        for (Subject subject : subjects)
            for (HashMap.Entry<String, Integer> entry : subject.getOptions().entrySet())
                points += entry.getValue();

        return points;
    }
    public String getPath()
    {
        return path;
    }
    public List<Subject> getSubjects()
    {
        return subjects;
    }
}
