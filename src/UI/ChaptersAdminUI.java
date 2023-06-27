package UI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.Map;

import Controller.Controller;
import Learn.Chapter;
import Lang.Lang;
import Util.Frame;
import Util.Pagination;

public class ChaptersAdminUI implements CallBack
{
    private final Frame frame;

    public ChaptersAdminUI()
    {
        frame = new Frame(Lang.ManagerChaptersTitle,1000, 750);

        LinkedList<JButton> myList = new LinkedList<>();

        Map<Integer,Chapter> chapters = Controller.getChapters();
        for (int i = 1,szChapter = chapters.size(); i <= szChapter; ++i)
        {
            Chapter chapter = chapters.get(i);
            String aux = Lang.ChapterAdminButton.replace("{{$countTests}}",String.valueOf(chapter.getTestsSize()))
                                         .replace("{{$countLessons}}",String.valueOf(chapter.getLessonsSize()))
                                         .replace("{{$chapterName}}",chapter.getName());
            JButton chapterButton = new JButton(aux);
            chapterButton.setName(String.valueOf(i));
            chapterButton.setToolTipText(aux);
            chapterButton.addActionListener(this::pressChapter);

            frame.add(chapterButton);
            chapterButton.setVisible(false);
            myList.add(chapterButton);
        }


        JButton addButton = new JButton(Lang.AddChapterField);
        addButton.addActionListener(e -> Controller.ShowAddChapterUI());
        addButton.setBounds(825, 0, 150, 50);

        JButton backButton = new JButton(Lang.Back);
        backButton.addActionListener(e -> closeFrame());
        backButton.setBounds(825, 50, 150, 50);

        frame.add(backButton);
        frame.add(addButton);

        Pagination.start(myList,Controller.getPageChapter(),frame, 12,Controller.funcPageChapter(),0,0,800,50);

        frame.closeEvent(this);
    }
    public Frame getFrame(){return frame;}

    public void goBack()
    {
        Controller.goBackDashboard();
    }
    private void pressChapter(ActionEvent e)
    {
        if(!frame.canSkip(true))
            return;
        JButton element = (JButton) e.getSource();
        Controller.setIdChapter(Integer.parseInt(element.getName()));
        Controller.ShowLessonListAdminUI();
    }
}
