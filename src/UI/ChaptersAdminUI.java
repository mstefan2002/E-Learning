package UI;

import Controller.Controller;
import Learn.Chapter;
import Lang.Lang;
import Util.Pagination;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Function;


public class ChaptersAdminUI
{
    private JFrame frame;
    private LinkedList<JButton> myList;
    private int currPage;

    public ChaptersAdminUI()
    {
        startUI( 0);
    }

    public ChaptersAdminUI(int curPage)
    {
        startUI(curPage);
    }

    private void startUI(int curPage)
    {
        currPage=curPage;
        frame = new JFrame(Lang.ManagerChaptersTitle);
        frame.setSize(1000, 750);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        myList = new LinkedList<>();

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
        addButton.addActionListener(e -> Controller.ShowAddChapterUI(frame));
        addButton.setBounds(825, 0, 150, 50);

        JButton backButton = new JButton(Lang.Back);
        backButton.addActionListener(e -> Controller.goBackDashboard(frame));
        backButton.setBounds(825, 50, 150, 50);

        frame.add(backButton);
        frame.add(addButton);

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
        Object source = e.getSource();
        for (JButton element : myList)
        {
            if (source != element)
                continue;
            frame.dispose();
            Controller.ShowLessonListAdminUI(Integer.parseInt(element.getName()),currPage);
            return;
        }
    }
}
