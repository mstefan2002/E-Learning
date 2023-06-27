package UI;

import Client.User;
import Controller.Controller;
import Lang.Lang;
import Learn.Chapter;
import Util.Pagination;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.Map;
import Util.Frame;
public class ChapterUI implements CloseFrame
{
    private final Frame frame;
    public ChapterUI()
    {
        frame = new Frame(Lang.ChaptersTitle,1000, 750);

        LinkedList<JButton> myList = new LinkedList<>();

        User user = (User)Controller.getClient();
        Map<Integer,Chapter> chapters = Controller.getChapters();
        for (int i = 1,szChapter=chapters.size(); i <= szChapter; ++i)
        {
            Chapter chapter = chapters.get(i);
            String chapterName = chapter.getName();
            String aux = Lang.ChapterUserButton.replace("{{$countTests}}",String.valueOf(chapter.getTestsSize()))
                    .replace("{{$userTests}}",String.valueOf(user.getTests(chapterName)))
                    .replace("{{$countLessons}}",String.valueOf(chapter.getLessonsSize()))
                    .replace("{{$userLessons}}",String.valueOf(user.getLessons(chapterName)))
                    .replace("{{$chapterName}}",chapterName);

            JButton chapterButton = new JButton(aux);
            chapterButton.setName(String.valueOf(i));
            chapterButton.setToolTipText(aux);
            chapterButton.addActionListener(this::pressChapter);

            frame.add(chapterButton);
            chapterButton.setVisible(false);
            myList.add(chapterButton);
        }

        JButton backButton = new JButton(Lang.Back);
        backButton.addActionListener(e->closeFrame(false));
        backButton.setBounds(825, 0, 150, 50);

        frame.add(backButton);
        Pagination.start(myList,Controller.getPageChapter(),frame, 12,Controller.funcPageChapter(),0,0,800,50);
        frame.closeEvent(this);
    }
    public void closeFrame(boolean onlyFrame)
    {
        frame.dispose();
        Controller.goBackDashboard();
    }
    private void pressChapter(ActionEvent e)
    {
        JButton element = (JButton) e.getSource();
        frame.dispose();
        Controller.setIdChapter(Integer.parseInt(element.getName()));
        Controller.ShowLessonListUserUI();
    }
}
