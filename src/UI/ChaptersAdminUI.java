package UI;

import Controller.Controller;
import Learn.Chapter;
import Lang.Lang;
import Util.Pagination;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;


public class ChaptersAdminUI
{
    protected final int itemsPerPage = 12;
    protected JButton addButton,backButton,prevButton,nextPageButton;
    protected JFrame frame;
    protected LinkedList<JButton> myList;
    protected int currPage;

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
        frame = new JFrame(Lang.ManagerChaptersTitle);
        frame.setSize(1000, 750);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ButtonClickListener listener = new ButtonClickListener(this);

        myList = new LinkedList<>();
        String aux;

        for (int i = 1,szChapter = Controller.getChapters().size(); i <= szChapter; ++i)
        {
            Chapter chapter = Controller.getChapters().get(i);
            aux = Lang.ChapterAdminButton.replace("{{$countTests}}",String.valueOf(chapter.getTestsSize()))
                                         .replace("{{$countLessons}}",String.valueOf(chapter.getLessonsSize()))
                                         .replace("{{$chapterName}}",chapter.getName());
            JButton chapterButton = new JButton(aux);
            chapterButton.setName(String.valueOf(i));
            chapterButton.setToolTipText(aux);
            chapterButton.addActionListener(listener);

            frame.add(chapterButton);
            chapterButton.setVisible(false);
            myList.add(chapterButton);
        }


        addButton = new JButton(Lang.AddChapterField);
        addButton.addActionListener(listener);
        addButton.setBounds(825, 0, 150, 50);

        backButton = new JButton(Lang.Back);
        backButton.addActionListener(listener);
        backButton.setBounds(825, 50, 150, 50);

        frame.add(backButton);
        frame.add(addButton);

        int i = 0;
        for (JButton element : myList)
        {
            element.setBounds(0, (i % itemsPerPage) * 50, 800, 50);
            ++i;
        }
        prevButton = new JButton(Lang.PrevButton);
        nextPageButton = new JButton(Lang.NextButton);
        Pagination.start(myList,prevButton, nextPageButton, curPage,frame,listener,itemsPerPage);

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
        private final ChaptersAdminUI self;

        public ButtonClickListener(ChaptersAdminUI self)
        {
            this.self = self;
        }


        @Override
        public void actionPerformed(ActionEvent e)
        {
            Object source = e.getSource();
            if (source == self.addButton) {
                Controller.ShowAddChapterUI(self.frame);
            } else if (source == self.backButton) {
                Controller.goBackDashboard(self.frame);
            } else if(source == self.prevButton)
            {
                --self.currPage;
                Pagination.prev(self.myList,self.itemsPerPage,self.currPage,self.prevButton, self.nextPageButton);
            }
            else if(source == self.nextPageButton)
            {
                ++self.currPage;
                Pagination.next(self.myList,self.itemsPerPage,self.currPage,self.prevButton, self.nextPageButton);
            }
            else
            {
                for (JButton element : self.myList)
                {
                    if (source == element)
                    {
                        self.frame.dispose();
                        Controller.ShowLessonListAdminUI(Integer.parseInt(element.getName()),self.currPage);
                        return;
                    }
                }
            }
        }
    }
}
