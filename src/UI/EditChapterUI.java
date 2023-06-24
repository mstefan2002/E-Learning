package UI;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.*;
import javax.swing.JPanel;
import javax.swing.JButton;
import Controller.*;
import Util.Output;
import Lang.Lang;

public class EditChapterUI
{
    private final JFrame frame,originFrame;
    private final JTextField nameField;

    public EditChapterUI(JFrame originFrame,int idChapter, int lessonPage, int chapterPage)
    {
        this.originFrame = originFrame;

        String chapterName= Controller.getChapters().get(idChapter).getName();

        frame = new JFrame(chapterName);
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridLayout(0,1));

        JLabel nameLabel = new JLabel(Lang.NameChapterLabel);

        nameField = new JTextField(chapterName);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setSize(300, 150);
        JButton backButton = new JButton(Lang.Cancel);
        backButton.addActionListener(e ->
        {
            if (e.getSource() == backButton)
                frame.dispose();
        });
        JButton saveButton = new JButton(Lang.Save);
        saveButton.addActionListener(e ->
        {
            if (e.getSource() == saveButton)
                saveChapter(idChapter,lessonPage,chapterPage);
        });

        buttonPanel.add(backButton);
        buttonPanel.add(saveButton);

        frame.add(nameLabel);
        frame.add(nameField);
        frame.add(buttonPanel);

        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
    public void saveChapter(int idChapter,int lessonPage,int chapterPage)
    {
        String name = nameField.getText();
        if(name.trim().isEmpty())
        {
            Output.PopUpAlert(Lang.EmptyChapterNameField);
            return;
        }

        for (int i = 1,szChapter = Controller.getChapters().size(); i <= szChapter; ++i)
        {
            if(i == idChapter   ||    !Controller.getChapters().get(i).getName().equals(name))
                continue;

            Output.PopUpAlert(Lang.ChapterAlreadyExists);
            return;
        }
        Controller.getChapters().get(idChapter).setName(name);
        frame.dispose();
        originFrame.dispose();
        Controller.ShowLessonListAdminUI(idChapter,chapterPage,lessonPage);
    }
}
