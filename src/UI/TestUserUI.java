package UI;

import Client.User;
import Controller.Controller;
import Learn.Chapter;
import Learn.Subject;
import Learn.Test;
import Util.Output;
import Lang.Lang;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import Util.Frame;

public class TestUserUI implements CloseFrame
{
    private final JLabel totalpointLabel;
    private final JEditorPane testField;
    private final JButton nextButton;
    private final JEditorPane[] optionsField;
    private final JCheckBox[] checkBox;
    private final Map<Integer,List<String>> choiseField;
    private final Frame frame;
    private final List<Subject> subjects;
    private int currSubj = 0,modeTest = 0;
    private Subject subj;
    public TestUserUI()
    {
        choiseField = new HashMap<>();
        Chapter chapter = Controller.getChapters().get(Controller.getIdChapter());
        Test test;
        int idLesson = Controller.getIdLesson();
        if(idLesson == 0)
            test = chapter.getFinalTest();
        else
            test = chapter.getLessons().get(idLesson).getTest();

        subjects = test.getSubjects();
        frame = new Frame(Lang.TestTitle,1200, 720);
        frame.getContentPane().setBackground(Color.GRAY);

        JLabel testLabel = new JLabel(Lang.TestLabel);
        testLabel.setBounds(20, 20, 200, 30);

        subj = subjects.get(currSubj);
        testField = new JEditorPane("text/html",getEnunt());
        testField.setOpaque(false);
        testField.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(testField);
        scrollPane.setBounds(20, 50, 880, 100);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        optionsField = new JEditorPane[4];
        checkBox = new JCheckBox[4];
        int i=0;
        Map<String,Integer> options = subj.getOptions();
        for (HashMap.Entry<String, Integer> entry : options.entrySet())
        {
            String key = entry.getKey();
            int yPos = (130*i);
            JLabel aLabel = new JLabel(Lang.TestOptionLabel.replace("{{$id}}",String.valueOf(i+1)));
            aLabel.setBounds(20, 150+yPos, 200, 30);

            optionsField[i] = new JEditorPane("text/html", Lang.TestOptionContentLabel.replace("{{$var}}",replaceToHtml(key)));
            optionsField[i].setOpaque(false);
            optionsField[i].setEditable(false);

            JScrollPane scrollOptions = new JScrollPane(optionsField[i]);
            scrollOptions.setBounds(20, 180+yPos, 880, 70);
            scrollOptions.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            checkBox[i] = new JCheckBox();
            checkBox[i].setBounds(20, 250+yPos, 25, 30);
            checkBox[i].setName(key);
            checkBox[i].setBackground(null);

            frame.add(scrollOptions);
            frame.add(aLabel);
            frame.add(checkBox[i]);
            ++i;
        }
        int szSubject = subjects.size();
        totalpointLabel = new JLabel(Lang.CurrentSubjectLabel.replace("{{$totalSubj}}",String.valueOf(szSubject)).replace("{{$currSubj}}","1"));
        totalpointLabel.setBounds(975, 20, 150, 50);

        nextButton = new JButton(Lang.NextSubjectLabel);
        nextButton.setBounds(975, 70, 150, 50);
        if(szSubject == 1)
            nextButton.setText(Lang.SendTestLabel);

        nextButton.addActionListener(e -> nextSubj());

        frame.add(totalpointLabel);
        frame.add(testLabel);
        frame.add(scrollPane);
        frame.add(nextButton);

        frame.closeEvent(this);
    }
    public void closeFrame(boolean onlyFrame)
    {
        frame.dispose();
        if(Controller.getIdLesson() == 0)
            Controller.ShowLessonListUserUI();
        else
            Controller.ShowLessonUserUI();
    }
    private void nextSubj()
    {
        int szSubject = subjects.size();
        if (modeTest == 1)
        {
            ++currSubj;
            if (currSubj == szSubject)
            {
                closeFrame(false);
                return;
            }

            updateUI();
            updateModeUI();
            if (currSubj + 1 == szSubject)
                nextButton.setText(Lang.ExitLabel);

            return;
        }

        List<String> listOps = new ArrayList<>();
        for (int it = 0; it < 4; ++it)
            if (checkBox[it].isSelected())
                listOps.add(checkBox[it].getName());

        choiseField.put(currSubj, listOps);
        if (currSubj + 1 != szSubject)
        {
            ++currSubj;
            updateUI();
            return;
        }

        modeTest = 1;
        int totalPoints = 0, userPoints = 0;

        for (int it = 0; it < szSubject; ++it)
        {
            Map<String, Integer> hashMapOp = subjects.get(it).getOptions();
            int userCurrPoints = 0;

            boolean anyWrong = false;

            List<String> listCh = choiseField.get(it);
            for (HashMap.Entry<String, Integer> entry : hashMapOp.entrySet())
            {
                String key = entry.getKey();
                int val = entry.getValue();
                totalPoints += val;
                for (String aux : listCh)
                {
                    if (!aux.equals(key))
                        continue;

                    if (val == 0)
                        anyWrong = true;
                    else
                        userCurrPoints += val;
                    break;
                }
                if(anyWrong) // we already found that he has a wrong answer, so we skip the other options
                    break;
            }
            if (!anyWrong)
                userPoints += userCurrPoints;
        }

        User user = (User) Controller.getClient();
        if (!user.setTest(Controller.getChapters().get(Controller.getIdChapter()).getName(), Controller.getIdLesson(), userPoints))
        {
            Output.PopUpAlert(Lang.GenericError);
            closeFrame(false);
            return;
        }
        Output.PopUp(Lang.TestPointsMessage.replace("{{$userPoints}}", String.valueOf(userPoints)).replace("{{$totalPoints}}", String.valueOf(totalPoints)));
        currSubj = 0;
        updateUI();
        updateModeUI();
        nextButton.setText(Lang.NextSubjectLabel);
    }
    private String replaceToHtml(String str)
    {
        return str.replaceAll("<","&lt;").replaceAll("\n","<br>").replaceAll("\t","&nbsp;");
    }
    private String getEnunt()
    {
        return ("<html><font color='blue' size='6'><b>"+replaceToHtml(subj.getEnunt())+"</b></font></html>");
    }
    private void updateUI()
    {
        int szSubject = subjects.size();
        subj = subjects.get(currSubj);
        testField.setText(getEnunt());
        Map<String, Integer> options = subj.getOptions();
        int i = 0;
        for (HashMap.Entry<String, Integer> entry : options.entrySet())
        {
            String key = entry.getKey();
            optionsField[i].setText(Lang.TestOptionContentLabel.replace("{{$var}}",replaceToHtml(key)));
            checkBox[i].setSelected(false);
            checkBox[i].setName(key);
            checkBox[i].setBackground(null);
            i++;
        }

        totalpointLabel.setText(Lang.CurrentSubjectLabel.replace("{{$totalSubj}}",String.valueOf(szSubject)).replace("{{$currSubj}}",String.valueOf(currSubj+1)));
        if (currSubj + 1 == szSubject)
            nextButton.setText(Lang.SendTestLabel);
    }
    private void updateModeUI()
    {
        for (HashMap.Entry<String, Integer> entry : subj.getOptions().entrySet())
        {
            String key = entry.getKey();
            int value = entry.getValue();
            Color color = Color.GREEN;
            if(value == 0)
                color = Color.RED;

            for (int i = 0; i < 4; ++i)
            {
                if (!checkBox[i].getName().equals(key))
                    continue;

                for (String aux : choiseField.get(currSubj))
                {
                    if (!aux.equals(key))
                        continue;

                    checkBox[i].setBackground(color);
                    checkBox[i].setSelected(true);
                    break;
                }
                if(value > 0 && !checkBox[i].isSelected())
                    checkBox[i].setBackground(Color.GREEN);
                break;
            }
        }
    }
}
