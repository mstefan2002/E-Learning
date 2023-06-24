package UI;

import Controller.Controller;
import Util.Output;

import javax.swing.*;
import java.awt.*;
import Lang.Lang;

public class AddKeyWordUI
{
    private final JTextField nameField;
    private final JFrame frame;
    private final JTextArea infoField;
    public AddKeyWordUI(int idLesson,int idChapter, int lessonPage, int chapterPage, JFrame originalframe)
    {
        frame = new JFrame(Lang.AddKeyWordTitle);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setBackground(Color.GRAY);
        frame.setLayout(null);
        frame.setSize(800, 800);

        JLabel nameLabel = new JLabel(Lang.KeyWordName);
        nameLabel.setBounds(20, 20, 100, 30);

        nameField = new JTextField();
        nameField.setBounds(20, 50, 700, 30);

        JLabel infoLabel = new JLabel(Lang.Information);
        infoLabel.setBounds(20, 80, 200, 30);

        infoField = new JTextArea();
        infoField.setLineWrap(true);
        infoField.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(infoField);
        scrollPane.setBounds(20, 110, 700, 400);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JButton addButton = new JButton(Lang.Add);
        addButton.setBounds(400, 510, 150, 50);
        addButton.addActionListener(e -> addWord(idLesson,idChapter, lessonPage, chapterPage,originalframe));

        JButton backButton = new JButton(Lang.Back);
        backButton.setBounds(250, 510, 150, 50);
        backButton.addActionListener(e -> frame.dispose());

        frame.add(nameLabel);
        frame.add(nameField);
        frame.add(infoLabel);
        frame.add(scrollPane);
        frame.add(addButton);
        frame.add(backButton);

        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
    private void addWord(int idLesson,int idChapter, int lessonPage, int chapterPage, JFrame originalframe)
    {
        String name = nameField.getText();
        if(name.trim().isEmpty())
        {
            Output.PopUpAlert(Lang.KeyWordEmpty);
            return;
        }
        String infoText = infoField.getText();
        if(infoText.trim().isEmpty())
        {
            Output.PopUpAlert(Lang.KeyWordInfoEmpty);
            return;
        }
        if(!Controller.getChapters().get(idChapter).getLessons().get(idLesson).addWord(name,infoText))//we have PopUps message in the addWord method
            return;

        frame.dispose();
        originalframe.dispose();
        Controller.ShowLessonAdminUI(idLesson,idChapter,lessonPage,chapterPage);
    }
}
