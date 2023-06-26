package UI;

import Client.User;
import Controller.Controller;
import Lang.Lang;
import Learn.Chapter;
import Util.Pagination;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Function;

public class ChapterUI
{
    private JFrame frame;
    private int currPage;
    public ChapterUI()
    {
        startUI(0);
    }

    public ChapterUI(int curPage)
    {
        startUI(curPage);
    }

    private void startUI(int curPage)
    {
        currPage=curPage;
        frame = new JFrame(Lang.ChaptersTitle);
        frame.setSize(1000, 750);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

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
        backButton.addActionListener(e->Controller.goBackDashboard(frame));
        backButton.setBounds(825, 0, 150, 50);

        frame.add(backButton);

        Function<Integer,Integer> func = (x) -> (currPage+=x);
        Pagination.start(myList,curPage,frame, 12,func,0,0,800,50);

        frame.setLayout(null);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                Controller.goBackDashboard(frame);
            }
        });
    }
    private void pressChapter(ActionEvent e)
    {
        JButton element = (JButton) e.getSource();
        frame.dispose();
        Controller.ShowLessonListUserUI(Integer.parseInt(element.getName()),currPage);
    }
}
