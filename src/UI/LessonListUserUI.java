package UI;

import Client.User;
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

public class LessonListUserUI
{
    private JFrame frame;
    private int currPage,chapterPage,idChapter;
    public LessonListUserUI(int idChapter, int lastPage)
    {
        startUI(idChapter, lastPage, 0);
    }

    public LessonListUserUI(int idChapter, int lastPage, int currPage)
    {
        startUI(idChapter, lastPage, currPage);
    }

    private void startUI(int idChapter, int lastPage, int currPage)
    {
        this.idChapter=idChapter;
        this.chapterPage=lastPage;
        this.currPage=currPage;

        Chapter chapter = Controller.getChapters().get(idChapter);
        String chapterName = chapter.getName();

        frame = new JFrame(chapterName);
        frame.setSize(1000, 750);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        LinkedList<JButton> myList = new LinkedList<>();

        User user = (User)Controller.getClient();
        Map<Integer,Lesson> lessons = chapter.getLessons();
        for (int i = 1,szLesson=lessons.size(); i <= szLesson; ++i)
        {
            Lesson lesson = lessons.get(i);
            String aux = Lang.LessonUserLabel.replace("{{$lessonName}}",lesson.getName());
            if(lesson.hasTest())
            {
                int resultTest = user.getTest(chapterName, i);
                if (resultTest != -1)
                    aux = aux.replace("{{$hasTest}}",Lang.LessonUserHasTest_One
                            .replace("{{$resultTest}}",String.valueOf(resultTest))
                            .replace("{{$totalPoints}}",String.valueOf(lesson.getTest().getTotalPoints()))
                    );
                else
                    aux = aux.replace("{{$hasTest}}",Lang.LessonUserHasTest_Two);
            }
            else
                aux = aux.replace("{{$hasTest}}","");
            if(user.getLesson(chapterName,i))
                aux = aux.replace("{{$isRead}}",Lang.LessonUserRead);
            else
                aux = aux.replace("{{$isRead}}","");

            JButton lessonButton = new JButton(aux);
            lessonButton.setName(String.valueOf(i));
            lessonButton.setToolTipText(aux);
            lessonButton.addActionListener(this::pressLesson);
            frame.add(lessonButton);
            lessonButton.setVisible(false);
            myList.add(lessonButton);
        }
        JButton testButton = new JButton(Lang.FinalExam);
        testButton.addActionListener(e->pressTest());
        testButton.setBounds(825, 50, 150, 50);

        if(chapter.hasTest())
        {
            int resultTest = user.getTest(chapterName,0);
            if(resultTest > 0)
            {
                JLabel pointsLabel = new JLabel();
                pointsLabel.setText(Lang.ChapterResultTest
                        .replace("{{$resultTest}}", String.valueOf(resultTest))
                        .replace("{{$totalPoints}}", String.valueOf(chapter.getFinalTest().getTotalPoints()))
                );
                pointsLabel.setBounds(825, 0, 150, 50);
                frame.add(pointsLabel);
                testButton.setEnabled(false);
            }
        }
        else
            testButton.setEnabled(false);

        JButton backButton = new JButton(Lang.Back);
        backButton.addActionListener(e->closeFrame());
        backButton.setBounds(825, 100, 150, 50);


        frame.add(testButton);
        frame.add(backButton);

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
        Controller.ShowChaptersUI(chapterPage);
    }
    private void pressLesson(ActionEvent e)
    {
        JButton element = (JButton) e.getSource();
        frame.dispose();
        Controller.ShowLessonUserUI(Integer.parseInt(element.getName()),idChapter,currPage,chapterPage);
    }
    private void pressTest()
    {
        User user = (User)Controller.getClient();
        Chapter chapter = Controller.getChapters().get(idChapter);
        String chapterName = chapter.getName();
        int lessonsUser = user.getLessons(chapterName),lessonsSize = chapter.getLessonsSize()
                ,testsUser = user.getTests(chapterName), testsSize = chapter.getTestsSize()-1;
        if(lessonsUser < lessonsSize||testsUser < testsSize)
        {
            Output.PopUpAlert(Lang.FinalExamRequest.replace("{{$lessonsUser}}",String.valueOf(lessonsUser))
                    .replace("{{$lessonsSize}}",String.valueOf(lessonsSize))
                    .replace("{{$testsUser}}",String.valueOf(testsUser))
                    .replace("{{$testsSize}}",String.valueOf(testsSize)));
            return;
        }
        frame.dispose();
        Controller.ShowTestUserUI(0,idChapter,currPage,chapterPage);
    }
}
