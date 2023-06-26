package UI;

import Controller.Controller;
import Lang.Lang;
import Learn.Chapter;
import Learn.Lesson;
import Util.Output;
import Util.Pagination;

import javax.swing.*;
import java.awt.event.ActionEvent;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Function;

public class LessonListAdminUI
{
    private JFrame frame;
    private int currPage,chapterPage,idChapter;
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

        LinkedList<JButton> myList = new LinkedList<>();

        Map<Integer,Lesson> lessons = chapter.getLessons();
        for (int i = 1,szLesson = lessons.size(); i <= szLesson; ++i) {
            Lesson lesson = lessons.get(i);
            String aux = Lang.LessonAdminLabel.replace("{{$lessonName}}",lesson.getName());
            if(lesson.hasTest())
                aux = aux.replace("{{$hasTest}}",Lang.LessonAdminHasTest);
            else
                aux = aux.replace("{{$hasTest}}","");

            JButton lessonButton = new JButton(aux);
            lessonButton.setName(String.valueOf(i));
            lessonButton.setToolTipText(aux);
            lessonButton.addActionListener(this::pressLessonButton);

            frame.add(lessonButton);
            lessonButton.setVisible(false);
            myList.add(lessonButton);
        }


        JButton addButton = new JButton(Lang.AddLessonLabel_Two);
        addButton.addActionListener(e->pressAddButton());
        addButton.setBounds(825, 0, 150, 50);

        JButton editButton = new JButton(Lang.EditChapterLabel);
        editButton.addActionListener(e->Controller.ShowEditChapterUI(frame,idChapter,currPage,chapterPage));
        editButton.setBounds(825, 50, 150, 50);

        JButton testButton = new JButton(Lang.AddTestLabel_Two);
        testButton.addActionListener(e->pressTestButton());
        testButton.setBounds(825, 100, 150, 50);
        if(chapter.hasTest())
            testButton.setText(Lang.DeleteTestLabel);

        JButton backButton = new JButton(Lang.Back);
        backButton.addActionListener(e->closeFrame());
        backButton.setBounds(825, 150, 150, 50);

        frame.add(testButton);
        frame.add(backButton);
        frame.add(addButton);
        frame.add(editButton);

        Function<Integer,Integer> func = (x) -> (this.currPage+=x);
        Pagination.start(myList,currPage,frame, 12,func,0,0,800,50);

        frame.setLayout(null);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                closeFrame();
            }
        });
    }
    private void closeFrame()
    {
        frame.dispose();
        Controller.ShowChaptersAdminUI(chapterPage);
    }
    private void pressLessonButton(ActionEvent e)
    {
        JButton element = (JButton) e.getSource();
        frame.dispose();
        Controller.ShowLessonAdminUI(Integer.parseInt(element.getName()),idChapter,currPage,chapterPage);
    }
    private void pressAddButton()
    {
        frame.dispose();
        Controller.ShowAddLessonUI(idChapter,currPage,chapterPage);
    }
    private void pressTestButton()
    {
        Chapter chapter = Controller.getChapters().get(idChapter);
        if(!chapter.hasTest())
        {
            frame.dispose();
            Controller.ShowAddTestUI(0, idChapter, currPage, chapterPage);
            return;
        }
        if(!chapter.removeTest())
        {
            Output.PopUpAlert(Lang.GenericError);
            return;
        }
        frame.dispose();
        Controller.ShowLessonListAdminUI(idChapter, chapterPage, currPage);
    }
}
