package UI;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JButton;
import Controller.*;
import Learn.Chapter;
import Util.Output;
import Util.FileHandler;
import Lang.Lang;

public class AddChapterUI
{
    private final JFrame frame,originFrame;
    private final JTextField nameField;

    public AddChapterUI(JFrame originFrame)
    {
        this.originFrame = originFrame;

        frame = new JFrame(Lang.AddChapterFrameTitle);
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridLayout(0,1));

        JLabel nameLabel = new JLabel(Lang.NameChapterLabel);

        nameField = new JTextField();

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setSize(300, 150);

        JButton backButton = new JButton(Lang.Cancel);
        backButton.addActionListener(e ->
        {
            if (e.getSource() == backButton)
                frame.dispose();
        });

        JButton addButton = new JButton(Lang.Add);
        addButton.addActionListener(e ->
        {
            if (e.getSource() == addButton)
                addChapter();
        });

        buttonPanel.add(backButton);
        buttonPanel.add(addButton);

        frame.add(nameLabel);
        frame.add(nameField);
        frame.add(buttonPanel);

        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
    public void addChapter()
    {
        String name = nameField.getText();
        if(name.trim().isEmpty())
        {
            Output.PopUpAlert(Lang.EmptyChapterNameField);
            return;
        }
        int szChapter = Controller.getChapters().size();
        for (int i = 1; i <= szChapter; ++i)
        {
            if(Controller.getChapters().get(i).getName().equals(name))
            {
                Output.PopUpAlert(Lang.ChapterAlreadyExists);
                return;
            }
        }
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("name",name);

        FileHandler file = new FileHandler("data/chapters.txt");
        if(!file.write(hashMap))
        {
            Output.PopUpAlert(Lang.GenericError);
            return;
        }

        Chapter chapter = new Chapter(name);
        int sz = szChapter+1;
        Controller.getChapters().put(sz,chapter);
        Controller.getChapters().get(sz).init();

        Output.PopUp(Lang.SuccessAddChapter);
        frame.dispose();
        originFrame.dispose();
        Controller.ShowChaptersAdminUI();

    }
}
