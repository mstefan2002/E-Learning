package UI;

import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.*;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JButton;

import Controller.*;
import Learn.Chapter;
import Lang.Lang;
import Util.Output;
import Util.Frame;

public class EditChapterUI implements CloseFrame
{
    private final Frame frame;
    private final JTextField nameField;

    public EditChapterUI()
    {
        Controller.addPreventGoingBack();

        String chapterName= Controller.getChapters().get(Controller.getIdChapter()).getName();

        frame = new Frame(chapterName,300, 200);
        frame.setLayout(new GridLayout(0,1));

        JLabel nameLabel = new JLabel(Lang.NameChapterLabel);

        nameField = new JTextField(chapterName);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setSize(300, 150);

        JButton backButton = new JButton(Lang.Cancel);
        backButton.addActionListener(e -> closeFrame(true));

        JButton saveButton = new JButton(Lang.Save);
        saveButton.addActionListener(e -> saveChapter());

        buttonPanel.add(backButton);
        buttonPanel.add(saveButton);

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
        Controller.ShowLessonListAdminUI();
    }
    private void saveChapter()
    {
        String name = nameField.getText();
        if(name.trim().isEmpty())
        {
            Output.PopUpAlert(Lang.EmptyChapterNameField);
            return;
        }
        int idChapter = Controller.getIdChapter();
        Map<Integer, Chapter> chapters = Controller.getChapters();
        for (int i = 1,szChapter = chapters.size(); i <= szChapter; ++i)
        {
            if(i == idChapter   ||    !chapters.get(i).getName().equals(name))
                continue;

            Output.PopUpAlert(Lang.ChapterAlreadyExists);
            return;
        }
        chapters.get(idChapter).setName(name);
        closeFrame(false);
    }
}
