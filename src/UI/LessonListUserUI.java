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
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Function;

public class LessonListUserUI
{
    protected final int itemsPerPage = 12;
    protected JButton backButton, testButton;
    protected JFrame frame;
    protected LinkedList<JButton> myList;
    protected int currPage,chapterPage,idChapter;

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

        ButtonClickListener listener = new ButtonClickListener(this);

        myList = new LinkedList<>();

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
            lessonButton.addActionListener(listener);
            frame.add(lessonButton);
            lessonButton.setVisible(false);
            myList.add(lessonButton);
        }
        testButton = new JButton(Lang.FinalExam);
        testButton.addActionListener(listener);
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

        backButton = new JButton(Lang.Back);
        backButton.addActionListener(listener);
        backButton.setBounds(825, 100, 150, 50);


        frame.add(testButton);
        frame.add(backButton);

        Function<Integer,Integer> func = (x) -> (this.currPage+=x);
        Pagination.start(myList,currPage,frame,itemsPerPage,func,0,0,800,50);

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
    protected void closeFrame()
    {
        frame.dispose();
        Controller.ShowChaptersUI(chapterPage);
    }
    static class ButtonClickListener implements ActionListener
    {
        LessonListUserUI self;
        public ButtonClickListener(LessonListUserUI self)
        {
            this.self = self;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            Object source = e.getSource();
            if (source == self.backButton)
                self.closeFrame();
            else if(source == self.testButton)
            {
                User user = (User)Controller.getClient();
                Chapter chapter = Controller.getChapters().get(self.idChapter);
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
                self.frame.dispose();
                Controller.ShowTestUserUI(0,self.idChapter,self.currPage,self.chapterPage);
            }
            else
            {
                for (JButton element : self.myList)
                {
                    if (source != element)
                        continue;

                    self.frame.dispose();
                    Controller.ShowLessonUserUI(Integer.parseInt(element.getName()),self.idChapter,self.currPage,self.chapterPage);
                    return;
                }
            }
        }
    }
}
