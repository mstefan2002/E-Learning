package UI;

import javax.swing.*;
import java.awt.*;

import Controller.Controller;
import Util.Output;
import Util.Frame;
import Lang.Lang;

public class AddKeyWordUI implements CloseFrame
{
    private final JTextField nameField;
    private final Frame frame;
    private final JTextArea infoField;
    public AddKeyWordUI()
    {
        Controller.addPreventGoingBack();
        frame = new Frame(Lang.AddKeyWordTitle,800, 800);
        frame.getContentPane().setBackground(Color.GRAY);

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
        addButton.addActionListener(e -> addWord());

        JButton backButton = new JButton(Lang.Back);
        backButton.setBounds(250, 510, 150, 50);
        backButton.addActionListener(e -> closeFrame(true));

        frame.add(nameLabel);
        frame.add(nameField);
        frame.add(infoLabel);
        frame.add(scrollPane);
        frame.add(addButton);
        frame.add(backButton);

        frame.closeEvent(this);
    }
    public void closeFrame(boolean onlyFrame)
    {
        frame.dispose();
        Controller.removePreventGoingBack();
        if(onlyFrame)
            return;
        Controller.getCurrentFrame().dispose();
        Controller.ShowLessonAdminUI();
    }
    private void addWord()
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
        if(!Controller.getChapters().get(Controller.getIdChapter()).getLessons().get(Controller.getIdLesson()).addWord(name,infoText))//we have PopUps message in the addWord method
            return;

        closeFrame(false);
    }
}
