package UI;

import Controller.Controller;
import Lang.Lang;
import Learn.Chapter;
import Learn.Lesson;
import Util.Output;
import Util.Pagination;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;

public class LessonListAdminUI
{
    protected final int itemsPerPage = 12;
    protected JButton addButton, backButton, prevButton, nextPageButton,editButton,testButton;
    protected JFrame frame;
    protected LinkedList<JButton> myList;
    protected int currPage,chapterPage,idChapter;

    public LessonListAdminUI(int idChapter, int lastPage)
    {
        startUI(idChapter, lastPage, 0);
    }

    public LessonListAdminUI(int idChapter, int lastPage, int currPage)
    {
        startUI(idChapter, lastPage, currPage);
    }

    private void startUI(int idChapter, int lastPage, int currPage)
    {
        this.idChapter = idChapter;
        this.currPage=currPage;
        this.chapterPage = lastPage;
        Chapter chapter = Controller.getChapters().get(idChapter);

        frame = new JFrame(chapter.getName());
        frame.setSize(1000, 750);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        ButtonClickListener listener = new ButtonClickListener(this);

        myList = new LinkedList<>();

        for (int i = 1,szLesson = chapter.getLessonsSize(); i <= szLesson; ++i) {
            Lesson lesson = chapter.getLessons().get(i);
            String aux = Lang.LessonAdminLabel.replace("{{$lessonName}}",lesson.getName());
            if(lesson.hasTest())
                aux = aux.replace("{{$hasTest}}",Lang.LessonAdminHasTest);
            else
                aux = aux.replace("{{$hasTest}}","");

            JButton lessonButton = new JButton(aux);
            lessonButton.setName(String.valueOf(i));
            lessonButton.setToolTipText(aux);
            lessonButton.addActionListener(listener);
            frame.add(lessonButton);
            lessonButton.setVisible(false);
            myList.add(lessonButton);
        }


        addButton = new JButton(Lang.AddLessonLabel_Two);
        addButton.addActionListener(listener);
        addButton.setBounds(825, 0, 150, 50);

        editButton = new JButton(Lang.EditChapterLabel);
        editButton.addActionListener(listener);
        editButton.setBounds(825, 50, 150, 50);

        testButton = new JButton(Lang.AddTestLabel_Two);
        testButton.addActionListener(listener);
        testButton.setBounds(825, 100, 150, 50);
        if(chapter.hasTest())
            testButton.setText(Lang.DeleteTestLabel);

        backButton = new JButton(Lang.Back);
        backButton.addActionListener(listener);
        backButton.setBounds(825, 150, 150, 50);

        frame.add(testButton);
        frame.add(backButton);
        frame.add(addButton);
        frame.add(editButton);

        int i = 0;
        for (JButton element : myList) {
            element.setBounds(0, (i % itemsPerPage) * 50, 800, 50);
            ++i;
        }
        prevButton = new JButton(Lang.PrevButton);
        nextPageButton = new JButton(Lang.NextButton);
        Pagination.start(myList,prevButton, nextPageButton, currPage,frame,listener,itemsPerPage);

        frame.setLayout(null);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                frame.dispose();
                Controller.ShowChaptersAdminUI(lastPage);
            }
        });
    }

    static class ButtonClickListener implements ActionListener
    {
        LessonListAdminUI self;
        public ButtonClickListener(LessonListAdminUI self)
        {
            this.self = self;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            Object source = e.getSource();
            if (source == self.addButton)
            {
                self.frame.dispose();
                Controller.ShowAddLessonUI(self.idChapter,self.currPage,self.chapterPage);
            }
            else if(source==self.editButton)
            {
                Controller.ShowEditChapterUI(self.frame,self.idChapter,self.currPage,self.chapterPage);
            }
            else if (source == self.backButton)
            {
                self.frame.dispose();
                Controller.ShowChaptersAdminUI(self.chapterPage);
            }
            else if(source == self.prevButton)
            {
                --self.currPage;
                Pagination.prev(self.myList,self.itemsPerPage,self.currPage,self.prevButton, self.nextPageButton);
            }
            else if(source == self.nextPageButton)
            {
                ++self.currPage;
                Pagination.next(self.myList,self.itemsPerPage,self.currPage,self.prevButton, self.nextPageButton);
            }
            else if(source == self.testButton)
            {
                if(!Controller.getChapters().get(self.idChapter).hasTest())
                {
                    self.frame.dispose();
                    Controller.ShowAddTestUI(0, self.idChapter, self.currPage, self.chapterPage);
                    return;
                }
                if(!Controller.getChapters().get(self.idChapter).removeTest())
                {
                    Output.PopUpAlert(Lang.GenericError);
                    return;
                }
                self.frame.dispose();
                Controller.ShowLessonListAdminUI(self.idChapter, self.chapterPage, self.currPage);
            }
            else
            {
                for (JButton element : self.myList)
                {
                    if (source == element)
                    {
                        self.frame.dispose();
                        Controller.ShowLessonAdminUI(Integer.parseInt(element.getName()),self.idChapter,self.currPage,self.chapterPage);
                        return;
                    }
                }
            }
        }
    }
}