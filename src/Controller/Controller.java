package Controller;
import Learn.Chapter;
import UI.*;
import Client.*;
import Util.FileHandler;
import Util.Util;


import javax.swing.JFrame;
import java.util.LinkedHashMap;
import java.util.Map;

public class Controller
{
    private static Object client;
    private static Map<Integer, Chapter> chapters;
    public static Map<Integer, Chapter> getChapters()
    {
        return chapters;
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
    public static void ShowSearchSubjUI()
    {
        new SearchSubjUI();
    }
    public static void ShowLessonListAdminUI(int idChapter,int lastPage)
    {
        new LessonListAdminUI(idChapter,lastPage);
    }
    public static void ShowEditSubjectUI(int idChapter,int idLesson,int idSubject)
    {
        new EditSubjectUI(idChapter,idLesson,idSubject);
    }
    public static void ShowAddLessonUI(int idChapter,int currPage,int chapterPage)
    {
        new AddLessonUI(idChapter,currPage,chapterPage);
    }
    public static void ShowLessonUserUI(int idLesson, int idChapter, int lessonPage, int chapterPage)
    {
        new LessonUserUI(idLesson, idChapter, lessonPage, chapterPage);
    }
    public static void ShowLessonAdminUI(int idLesson, int idChapter, int lessonPage, int chapterPage)
    {
        new LessonAdminUI(idLesson, idChapter, lessonPage, chapterPage);
    }
    public static void ShowAddKeyWordUI(int idLesson,int chapter, int lessonPage, int chapterPage, JFrame originalframe)
    {
        new AddKeyWordUI(idLesson,chapter, lessonPage, chapterPage, originalframe);
    }
    public static void ShowLessonListAdminUI(int idChapter,int lastPage,int curPage)
    {
        new LessonListAdminUI(idChapter,lastPage,curPage);
    }
    public static void ShowLessonListUserUI(int idChapter,int curPage)
    {
        new LessonListUserUI(idChapter,curPage);
    }
    public static void ShowTestUserUI(int idLesson, int idChapter, int lessonPage, int chapterPage)
    {
        new TestUserUI( idLesson, idChapter, lessonPage,chapterPage);
    }
    public static void ShowLessonListUserUI(int idChapter,int lastPage,int curPage)
    {
        new LessonListUserUI(idChapter,lastPage,curPage);
    }
    public static void ShowUserUI()
    {
        new MainUserUI((User)client);
    }
    public static void ShowAddTestUI(int idLesson,int idChapter, int lessonPage, int chapterPage)
    {
        new AddTestUI(idLesson,idChapter, lessonPage, chapterPage);
    }
    public static void ShowEditChapterUI(JFrame originFrame,int idChapter, int lessonPage, int chapterPage)
    {
        new EditChapterUI(originFrame,idChapter,lessonPage,chapterPage);
    }
    public static void ShowProfileUI()
    {
        new ProfileUI(client);
    }
    public static void ShowChaptersUI()
    {
        new ChapterUI();
    }
    public static void ShowChaptersUI(int lastPage)
    {
        new ChapterUI(lastPage);
    }
    public static void ShowAdminUI()
    {
        new MainAdminUI((Admin)client);
    }
    public static void ShowAddChapterUI(JFrame frame)
    {
        new AddChapterUI(frame);
    }
    public static void ShowChaptersAdminUI()
    {
        new ChaptersAdminUI();
    }
    public static void ShowChaptersAdminUI(int page)
    {
        new ChaptersAdminUI(page);
    }
    public static void ShowUsersUI()
    {
        new AdminUsersUI((Admin)client);
    }
    public static void ShowUsersUI(int curPage)
    {
        new AdminUsersUI((Admin)client,curPage);
    }
    public static void ShowEditUserUI(JFrame frame,User client,int curPage)
    {
        new EditUserUI(frame,client,curPage);
    }
    public static void setClient(Object user)
    {
        client = user;
    }
    public static void goBackDashboard(JFrame frame)
    {
        if(client instanceof Admin)
            Controller.ShowAdminUI();
        else
            Controller.ShowUserUI();
        frame.dispose();
    }
    public static void AddUserUI(JFrame frame)
    {
        new AddUserUI(frame);
    }
    public static Object getClient()
    {
        return client;
    }
}