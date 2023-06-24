package UI;

import Controller.Controller;
import Learn.Chapter;
import Learn.Lesson;
import Lang.Lang;
import Util.Output;
import Util.FileHandler;
import Util.Util;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class AddLessonUI
{
    private final JFrame frame;
    private final JTextField nameField;
    private final JTextArea lessonField;

    public AddLessonUI(int idChapter,int currPage,int chapterPage)
    {
        frame = new JFrame(Lang.AddLessonTitle);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setBackground(Color.GRAY);
        frame.setLayout(null);
        frame.setSize(1200, 720);

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
        addButton.addActionListener(e ->
        {
            if (e.getSource() == addButton)
            {
                addLesson(idChapter, currPage, chapterPage);
            }
        });
        JButton backButton = new JButton(Lang.Back);
        backButton.setBounds(975, 70, 150, 50);
        backButton.addActionListener(e ->
        {
            if (e.getSource() == backButton)
            {
                frame.dispose();
                Controller.ShowLessonListAdminUI(idChapter, currPage, chapterPage);
            }
        });
        frame.add(nameLabel);
        frame.add(nameField);
        frame.add(lessonLabel);
        frame.add(scrollPane);
        frame.add(addButton);
        frame.add(backButton);

        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
    public void addLesson(int idChapter,int currPage,int chapterPage)
    {
        String name = nameField.getText();
        String lessonText = lessonField.getText();
        if(name.trim().isEmpty())
        {
            Output.PopUpAlert(Lang.EmptyLessonNameField);
            return;
        }
        if(lessonText.trim().isEmpty())
        {
            Output.PopUpAlert(Lang.EmptyLessonField);
            return;
        }
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
        Controller.getChapters().get(idChapter).getLessons().put(szLesson,lesson);
        Controller.getChapters().get(idChapter).getLessons().get(szLesson).init();

        Output.PopUp(Lang.SuccessAddLesson);
        frame.dispose();
        Controller.ShowLessonListAdminUI(idChapter, currPage, chapterPage);
    }
}
