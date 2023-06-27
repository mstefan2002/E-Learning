package UI;

import Client.User;
import Controller.Controller;
import Learn.Chapter;
import Learn.Lesson;
import Util.Output;
import Util.PrinterUtil;
import Lang.Lang;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import Util.Frame;

public class LessonUserUI implements CloseFrame
{
    private final Frame frame;
    public LessonUserUI()
    {
        int idLesson = Controller.getIdLesson();
        User user = (User)Controller.getClient();
        Chapter chapter = Controller.getChapters().get(Controller.getIdChapter());
        Lesson lesson = chapter.getLessons().get(idLesson);
        String chapterName = chapter.getName();

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

        JButton testButton = new JButton(Lang.StartTestLabel);
        testButton.setBounds(975, 50, 150, 50);
        testButton.addActionListener(e ->
        {
            if(!user.getLesson(chapterName,idLesson))
            {
                Output.PopUpAlert(Lang.LessonTestRequest);
                return;
            }
            frame.dispose();
            Controller.ShowTestUserUI();
        });
        if(user.getTest(chapterName,idLesson)>0)
            testButton.setEnabled(false);

        JButton readButton = new JButton(Lang.MarkReadLessonLabel);
        readButton.setBounds(975, 100, 150, 50);
        readButton.addActionListener(e ->
        {
            if(!user.setLesson(chapterName,idLesson))
            {
                Output.PopUpAlert(Lang.GenericError);
                return;
            }
            readButton.setEnabled(false);
        });
        if(user.getLesson(chapterName,idLesson))
            readButton.setEnabled(false);

        JButton printButton = new JButton(Lang.PrintLessonLabel);
        printButton.setBounds(975, 150, 150, 50);
        printButton.addActionListener(e ->
        {
            PrinterUtil print = new PrinterUtil(scrollPane);
            print.printJEditorPane();
        });
        JButton backButton = new JButton(Lang.Back);
        backButton.setBounds(975, 200, 150, 50);
        backButton.addActionListener(e -> closeFrame(false));

        frame.add(printButton);
        frame.add(scrollPane);
        frame.add(readButton);
        frame.add(backButton);
        if(lesson.hasTest())
            frame.add(testButton);

        frame.closeEvent(this);
    }
    public void closeFrame(boolean onlyFrame)
    {
        frame.dispose();
        Controller.ShowLessonListUserUI();
    }
}
