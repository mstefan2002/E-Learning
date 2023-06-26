package UI;

import Client.User;
import Controller.Controller;
import Lang.Lang;
import Learn.Chapter;
import Util.Pagination;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Function;

public class ChapterUI
{
    protected final int itemsPerPage = 12;
    protected JButton backButton;
    protected JFrame frame;
    protected LinkedList<JButton> myList;
    protected int currPage;

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

        ButtonClickListener listener = new ButtonClickListener(this);

        myList = new LinkedList<>();

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
            chapterButton.addActionListener(listener);

            frame.add(chapterButton);
            chapterButton.setVisible(false);
            myList.add(chapterButton);
        }

        backButton = new JButton(Lang.Back);
        backButton.addActionListener(listener);
        backButton.setBounds(825, 0, 150, 50);

        frame.add(backButton);

        Function<Integer,Integer> func = (x) -> (currPage+=x);
        Pagination.start(myList,curPage,frame,itemsPerPage,func,0,0,800,50);

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

    static class ButtonClickListener implements ActionListener
    {
        ChapterUI self;
        public ButtonClickListener(ChapterUI self)
        {
            this.self = self;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            Object source = e.getSource();
            if (source == self.backButton)
                Controller.goBackDashboard(self.frame);
            else
            {
                for (JButton element : self.myList)
                {
                    if (source != element)
                        continue;
                    self.frame.dispose();
                    Controller.ShowLessonListUserUI(Integer.parseInt(element.getName()),self.currPage);
                    return;
                }
            }
        }
    }
}
