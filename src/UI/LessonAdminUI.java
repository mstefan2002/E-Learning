package UI;

import Controller.Controller;
import Learn.Chapter;
import Learn.Lesson;
import Util.Output;
import Util.Frame;
import Lang.Lang;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;

public class LessonAdminUI implements CallBack
{
    private final Frame frame;
    public LessonAdminUI()
    {
        Chapter chapter = Controller.getChapters().get(Controller.getIdChapter());
        Lesson lesson = chapter.getLessons().get(Controller.getIdLesson());

        frame = new Frame(lesson.getName(),1200, 720);

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
        addButton.addActionListener(e -> Controller.ShowAddKeyWordUI());

        JButton testButton = new JButton(Lang.AddTestLabel_Two);
        testButton.setBounds(975, 100, 150, 50);
        testButton.addActionListener(e ->
        {
            if(!frame.canSkip(true))
                return;
            frame.dispose();
            Controller.ShowAddTestUI();
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
            Controller.ShowLessonAdminUI();
        });

        JButton backButton = new JButton(Lang.Back);
        backButton.setBounds(975, 150, 150, 50);
        backButton.addActionListener(e -> closeFrame());

        frame.add(scrollPane);
        frame.add(addButton);
        frame.add(backButton);
        if(!lesson.hasTest())
            frame.add(testButton);
        else
            frame.add(deltestButton);

        frame.closeEvent(this);
    }
    public Frame getFrame(){return frame;}

    public void goBack()
    {
        Controller.ShowLessonListAdminUI();
    }
}
