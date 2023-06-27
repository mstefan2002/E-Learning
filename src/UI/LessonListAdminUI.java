package UI;

import Controller.Controller;
import Lang.Lang;
import Learn.Chapter;
import Learn.Lesson;
import Util.Output;
import Util.Pagination;
import Util.Frame;
import javax.swing.*;
import java.awt.event.ActionEvent;

import java.util.LinkedList;
import java.util.Map;

public class LessonListAdminUI implements CallBack
{
    private final Frame frame;
    public LessonListAdminUI()
    {
        Controller.setIdLesson(0);

        Chapter chapter = Controller.getChapters().get(Controller.getIdChapter());

        frame = new Frame(chapter.getName(),1000, 750);

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
        editButton.addActionListener(e->Controller.ShowEditChapterUI());
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

        Pagination.start(myList,Controller.getPageLesson(),frame, 12,Controller.funcPageLesson(),0,0,800,50);
        frame.closeEvent(this);
    }
    public Frame getFrame(){return frame;}

    public void goBack()
    {
        Controller.ShowChaptersAdminUI();
    }
    private void pressLessonButton(ActionEvent e)
    {
        if(!frame.canSkip(true))
            return;
        JButton element = (JButton) e.getSource();
        Controller.setIdLesson(Integer.parseInt(element.getName()));
        Controller.ShowLessonAdminUI();
    }
    private void pressAddButton()
    {
        if(!frame.canSkip(true))
            return;
        Controller.ShowAddLessonUI();
    }
    private void pressTestButton()
    {
        if(!frame.canSkip(true))
            return;
        int idChapter = Controller.getIdChapter();
        Chapter chapter = Controller.getChapters().get(idChapter);
        if(!chapter.hasTest())
        {
            Controller.ShowAddTestUI();
            return;
        }
        if(!chapter.removeTest())
        {
            Output.PopUpAlert(Lang.GenericError);//stops the app
            return;
        }
        Controller.ShowLessonListAdminUI();
    }
}
