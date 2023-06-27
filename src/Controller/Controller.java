package Controller;
import Learn.Chapter;
import UI.*;
import Client.*;
import Util.FileHandler;
import Util.Util;


import javax.swing.JFrame;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class Controller
{
    private static JFrame currentFrame;
    public static JFrame getCurrentFrame()
    {
        return currentFrame;
    }

    public static void setCurrentFrame(JFrame currentFrame)
    {
        Controller.currentFrame = currentFrame;
    }
    private static int preventGoingBack = 0;
    public static int isPreventGoingBack()
    {
        return preventGoingBack;
    }
    public static void addPreventGoingBack()
    {
        ++Controller.preventGoingBack;
    }
    public static void removePreventGoingBack()
    {
        --Controller.preventGoingBack;
    }
    private static int idChapter,idLesson,pageChapter,pageLesson,pageUser;
    public static int getIdChapter()
    {
        return idChapter;
    }

    public static void setIdChapter(int idChapter)
    {
        Controller.idChapter = idChapter;
    }

    public static int getIdLesson()
    {
        return idLesson;
    }

    public static void setIdLesson(int idLesson)
    {
        Controller.idLesson = idLesson;
    }

    public static int getPageChapter()
    {
        return pageChapter;
    }
    public static int getPageLesson()
    {
        return pageLesson;
    }
    public static int getPageUser()
    {
        return pageUser;
    }
    public static Function<Integer,Integer> funcPageChapter()
    {
        return (x) -> (pageChapter+=x);
    }
    public static Function<Integer,Integer> funcPageLesson()
    {
        return (x) -> (pageLesson+=x);
    }
    public static Function<Integer,Integer> funcPageUser()
    {
        return (x) -> (pageUser+=x);
    }
    private static Object client;
    public static void setClient(Object user)
    {
        client = user;
    }
    public static Object getClient()
    {
        return client;
    }
    private static Map<Integer, Chapter> chapters;
    public static Map<Integer, Chapter> getChapters()
    {
        return chapters;
    }
    public static void goBackDashboard()
    {
        if(client instanceof Admin)
            Controller.ShowAdminUI();
        else
            Controller.ShowUserUI();
    }

    public static void ShowAddChapterUI()
    {
        new AddChapterUI();
    }
    public static void ShowAddKeyWordUI()
    {
        new AddKeyWordUI();
    }
    public static void ShowAddLessonUI()
    {
        new AddLessonUI();
    }
    public static void ShowAddTestUI()
    {
        new AddTestUI();
    }
    public static void ShowAddUserUI()
    {
        new AddUserUI();
    }
    public static void ShowUsersUI()
    {
        AdminUsersUI aux = new AdminUsersUI();
        setCurrentFrame(aux.getFrame());
    }
    public static void ShowChaptersAdminUI()
    {
        ChaptersAdminUI aux = new ChaptersAdminUI();
        setCurrentFrame(aux.getFrame());
    }
    public static void ShowChaptersUI()
    {
        new ChapterUI();
    }
    public static void ShowEditChapterUI()
    {
        new EditChapterUI();
    }
    public static void ShowEditSubjectUI(Object parent,int idSubject)
    {
        new EditSubjectUI(parent,idSubject);
    }
    public static void ShowEditUserUI(User client)
    {
        new EditUserUI(client);
    }
    public static void ShowLessonAdminUI()
    {
        LessonAdminUI aux = new LessonAdminUI();
        setCurrentFrame(aux.getFrame());
    }
    public static void ShowLessonListAdminUI()
    {
        LessonListAdminUI aux = new LessonListAdminUI();
        setCurrentFrame(aux.getFrame());
    }
    public static void ShowLessonListUserUI()
    {
        new LessonListUserUI();
    }

    public static void ShowLessonUserUI()
    {
        new LessonUserUI();
    }
    public static void ShowLoginUI()
    {
        new LoginUI();
        chapters = new LinkedHashMap<>();
        Util.Director("data"); // create if it does not exist
        FileHandler file = new FileHandler("data/chapters.txt");
        file.read(new FileHandler.GetReadDataCallback()
        {
            public boolean getHash(Map<String, String> hash)
            {
                int sz = chapters.size()+1;
                chapters.put(sz,new Chapter(hash.get("name")));
                chapters.get(sz).init();
                return true;
            }
        });
    }
    public static void ShowAdminUI()
    {
        new MainAdminUI((Admin)client);
    }
    public static void ShowUserUI()
    {
        new MainUserUI((User)client);
    }
    public static void ShowProfileUI()
    {
        new ProfileUI();
    }
    public static void ShowSearchSubjUI()
    {
        new SearchSubjUI();
    }
    public static void ShowTestUserUI()
    {
        new TestUserUI();
    }
}