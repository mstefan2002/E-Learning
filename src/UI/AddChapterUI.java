package UI;

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
import Util.Frame;
import Lang.Lang;

public class AddChapterUI implements CloseFrame
{
    private final Frame frame;
    private final JTextField nameField;

    public AddChapterUI()
    {
        Controller.addPreventGoingBack();

        frame = new Frame(Lang.AddChapterFrameTitle,300, 200);
        frame.setLayout(new GridLayout(0,1));

        JLabel nameLabel = new JLabel(Lang.NameChapterLabel);
        nameField = new JTextField();

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setSize(300, 150);

        JButton backButton = new JButton(Lang.Cancel);
        backButton.addActionListener(e -> closeFrame(true));

        JButton addButton = new JButton(Lang.Add);
        addButton.addActionListener(e -> addChapter());

        buttonPanel.add(backButton);
        buttonPanel.add(addButton);

        frame.add(nameLabel);
        frame.add(nameField);
        frame.add(buttonPanel);
        frame.closeEvent(this);
    }
    public void closeFrame(boolean onlyFrame)
    {
        frame.dispose();
        Controller.removePreventGoingBack();
        if(onlyFrame)
            return;
        Controller.getCurrentFrame().dispose();
        Controller.ShowChaptersAdminUI();
    }
    private void addChapter()
    {
        String name = nameField.getText();
        if(name.trim().isEmpty())
        {
            Output.PopUpAlert(Lang.EmptyChapterNameField);
            return;
        }
        Map<Integer,Chapter> chapters= Controller.getChapters();
        int szChapter = chapters.size();
        for (int i = 1; i <= szChapter; ++i)
        {
            if(chapters.get(i).getName().equals(name))
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
        chapters.put(sz,chapter);
        chapters.get(sz).init();

        Output.PopUp(Lang.SuccessAddChapter);
        closeFrame(false);
    }
}
