package UI;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import Controller.Controller;
import Learn.Chapter;
import Learn.Lesson;
import Lang.Lang;
import Util.Output;
import Util.FileHandler;
import Util.Util;
import Util.Frame;

public class AddLessonUI implements CloseFrame
{
    private final Frame frame;
    private final JTextField nameField;
    private final JTextArea lessonField;

    public AddLessonUI()
    {
        frame = new Frame(Lang.AddLessonTitle,1200, 720);
        frame.getContentPane().setBackground(Color.GRAY);

        JLabel nameLabel = new JLabel(Lang.LessonNameLabel);
        nameLabel.setBounds(20, 20, 100, 30);

        nameField = new JTextField();
        nameField.setBounds(20, 50, 400, 30);

        JLabel lessonLabel = new JLabel(Lang.LessonLabel);
        lessonLabel.setBounds(20, 80, 200, 30);

        lessonField = new JTextArea();
        lessonField.setLineWrap(true);
        lessonField.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(lessonField);
        scrollPane.setBounds(20, 110, 880, 530);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JButton addButton = new JButton(Lang.AddLessonLabel);
        addButton.setBounds(975, 20, 150, 50);
        addButton.addActionListener(e -> addLesson());

        JButton backButton = new JButton(Lang.Back);
        backButton.setBounds(975, 70, 150, 50);
        backButton.addActionListener(e -> closeFrame(false));

        frame.add(nameLabel);
        frame.add(nameField);
        frame.add(lessonLabel);
        frame.add(scrollPane);
        frame.add(addButton);
        frame.add(backButton);
        frame.closeEvent(this);
    }
    public void closeFrame(boolean onlyFrame)
    {
        frame.dispose();
        Controller.ShowLessonListAdminUI();
    }
    private void addLesson()
    {
        String name = nameField.getText();
        if(name.trim().isEmpty())
        {
            Output.PopUpAlert(Lang.EmptyLessonNameField);
            return;
        }
        String lessonText = lessonField.getText();
        if(lessonText.trim().isEmpty())
        {
            Output.PopUpAlert(Lang.EmptyLessonField);
            return;
        }
        int idChapter = Controller.getIdChapter();
        Chapter chapter = Controller.getChapters().get(idChapter);
        Map<Integer, Lesson> hashMapLessons= chapter.getLessons();
        int szLesson = hashMapLessons.size();
        for (int i = 1; i <= szLesson; ++i)
        {
            if(hashMapLessons.get(i).getName().equals(name))
            {
                Output.PopUpAlert(Lang.LessonAlreadyExists);
                return;
            }
        }

        ++szLesson;

        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("name",name);
        hashMap.put("lesson",lessonText);
        hashMap.put("index",String.valueOf(szLesson));

        String safename = Util.makeFileNameSafe(name)+".txt";
        String path = "data/"+chapter.getName() + "_lessons/" + safename;
        FileHandler file = new FileHandler(path);

        if(!file.write(hashMap))
        {
            Output.PopUpAlert(Lang.GenericError);
            return;
        }

        Lesson lesson = new Lesson(idChapter,path,safename);
        hashMapLessons.put(szLesson,lesson);
        hashMapLessons.get(szLesson).init();

        Output.PopUp(Lang.SuccessAddLesson);
        closeFrame(false);
    }
}
