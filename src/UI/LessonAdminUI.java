package UI;

import Controller.Controller;
import Learn.Chapter;
import Learn.Lesson;
import Util.Output;
import Lang.Lang;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LessonAdminUI
{
    public LessonAdminUI(int idLesson, int idChapter, int lessonPage, int chapterPage)
    {
        Chapter chapter = Controller.getChapters().get(idChapter);
        Lesson lesson = chapter.getLessons().get(idLesson);

        JFrame frame = new JFrame(lesson.getName());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(null);
        frame.setSize(1200, 720);

        JEditorPane lessonField = new JEditorPane("text/html", lesson.getLessonText().replaceAll("\n","<br>").replaceAll("\t","&nbsp;"));
        lessonField.setEditable(false);
        lessonField.setOpaque(false);
        lessonField.addHyperlinkListener(hle -> {
            if (HyperlinkEvent.EventType.ACTIVATED.equals(hle.getEventType())) {
                Output.PopUp(lesson.getWords().get(hle.getDescription()));
            }
        });
        lessonField.setCaretPosition(0);

        JScrollPane scrollPane = new JScrollPane(lessonField);
        scrollPane.setBounds(20, 20, 880, 650);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JButton addButton = new JButton(Lang.AddKeyLabel);
        addButton.setBounds(975, 20, 150, 80);
        addButton.addActionListener(e -> Controller.ShowAddKeyWordUI(idLesson,idChapter, lessonPage, chapterPage, frame));

        JButton testButton = new JButton(Lang.AddTestLabel_Two);
        testButton.setBounds(975, 100, 150, 50);
        testButton.addActionListener(e ->
        {
            frame.dispose();
            Controller.ShowAddTestUI(idLesson,idChapter, lessonPage, chapterPage);
        });

        JButton deltestButton = new JButton(Lang.DeleteTestLabel);
        deltestButton.setBounds(975, 100, 150, 50);
        deltestButton.addActionListener(e ->
        {
            if(!lesson.removeTest())
            {
                Output.PopUpAlert(Lang.GenericError);
                return;
            }
            Output.PopUp(Lang.SuccessDeletingTest);
            frame.dispose();
            Controller.ShowLessonAdminUI(idLesson,idChapter,lessonPage,chapterPage);
        });

        JButton backButton = new JButton(Lang.Back);
        backButton.setBounds(975, 150, 150, 50);
        backButton.addActionListener(e -> closeFrame(frame,idChapter, lessonPage, chapterPage));

        frame.add(scrollPane);
        frame.add(addButton);
        frame.add(backButton);
        if(!lesson.hasTest())
            frame.add(testButton);
        else
            frame.add(deltestButton);

        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                closeFrame(frame,idChapter, lessonPage, chapterPage);
            }
        });
    }
    private void closeFrame(JFrame frame, int idChapter, int lessonPage, int chapterPage)
    {
        frame.dispose();
        Controller.ShowLessonListAdminUI(idChapter, lessonPage, chapterPage);
    }
}
