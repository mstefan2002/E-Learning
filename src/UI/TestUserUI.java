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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestUserUI
{
    private final JLabel totalpointLabel;
    private final JEditorPane testField;
    private final JButton nextButton;
    private final JEditorPane[] optionsField;
    private final JCheckBox[] checkBox;
    private final Map<Integer,List<String>> choiseField;
    private final int idLesson,idChapter,lessonPage,chapterPage;
    private final JFrame frame;
    private final Test test;
    private int currSubj = 0,modeTest = 0;
    private Subject subj;
    public TestUserUI(int idLesson,int idChapter, int lessonPage, int chapterPage)
    {
        this.idChapter = idChapter;
        this.idLesson = idLesson;
        this.lessonPage=lessonPage;
        this.chapterPage=chapterPage;

        choiseField = new HashMap<>();
        Chapter chapter = Controller.getChapters().get(idChapter);
        if(idLesson == 0)
            test = chapter.getFinalTest();
        else
            test = chapter.getLessons().get(idLesson).getTest();

        frame = new JFrame(Lang.TestTitle);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setBackground(Color.GRAY);
        frame.setLayout(null);
        frame.setSize(1200, 720);

        JLabel testLabel = new JLabel(Lang.TestLabel);
        testLabel.setBounds(20, 20, 200, 30);

        subj = test.getSubjects().get(currSubj);
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
            int yPos = (130*i);
            JLabel aLabel = new JLabel(Lang.TestOptionLabel.replace("{{$id}}",String.valueOf(i+1)));
            aLabel.setBounds(20, 150+yPos, 200, 30);

            optionsField[i] = new JEditorPane("text/html", Lang.TestOptionContentLabel.replace("{{$var}}",replaceToHtml(entry.getKey())));
            optionsField[i].setOpaque(false);
            optionsField[i].setEditable(false);

            JScrollPane scrollOptions = new JScrollPane(optionsField[i]);
            scrollOptions.setBounds(20, 180+yPos, 880, 70);
            scrollOptions.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            checkBox[i] = new JCheckBox();
            checkBox[i].setBounds(20, 250+yPos, 25, 30);
            checkBox[i].setName(entry.getKey());
            checkBox[i].setBackground(null);

            frame.add(scrollOptions);
            frame.add(aLabel);
            frame.add(checkBox[i]);
            ++i;
        }
        int szSubject = test.getSubjects().size();
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

        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                closeFrame();
            }
        });
    }
    protected void closeFrame()
    {
        frame.dispose();
        if(idLesson == 0)
            Controller.ShowLessonListUserUI(idChapter,lessonPage, chapterPage);
        else
            Controller.ShowLessonUserUI(idLesson,idChapter,lessonPage,chapterPage);
    }
    private void nextSubj()
    {
        int szSubject1 = test.getSubjects().size();
        if (modeTest == 1)
        {
            ++currSubj;
            if (currSubj == szSubject1)
            {
                closeFrame();
                return;
            }

            updateUI();
            updateModeUI();
            if (currSubj + 1 == szSubject1)
                nextButton.setText(Lang.ExitLabel);

            return;
        }

        List<String> listOps = new ArrayList<>();
        for (int it = 0; it < 4; ++it)
            if (checkBox[it].isSelected())
                listOps.add(checkBox[it].getName());

        choiseField.put(currSubj, listOps);
        if (currSubj + 1 != szSubject1)
        {
            ++currSubj;
            updateUI();
            return;
        }

        modeTest = 1;
        int totalPoints = 0, userPoints = 0;

        for (int it = 0; it < szSubject1; ++it)
        {
            Map<String, Integer> hashMapOp = test.getSubjects().get(it).getOptions();
            int userCurrPoints = 0;

            boolean anyWrong = false;

            List<String> listCh = choiseField.get(it);
            for (HashMap.Entry<String, Integer> entry : hashMapOp.entrySet())
            {
                int val = entry.getValue();
                totalPoints += val;
                for (String aux : listCh) {
                    if (!aux.equals(entry.getKey()))
                        continue;

                    if (val == 0)
                        anyWrong = true;
                    else
                        userCurrPoints += val;
                    break;
                }
            }
            if (!anyWrong)
                userPoints += userCurrPoints;
        }

        User user = (User) Controller.getClient();
        if (!user.setTest(Controller.getChapters().get(idChapter).getName(), idLesson, userPoints))
        {
            Output.PopUpAlert(Lang.GenericError);
            closeFrame();
            return;
        }
        Output.PopUp(Lang.TestPointsMessage.replace("{{$userPoints}}", String.valueOf(userPoints)).replace("{{$totalPoints}}", String.valueOf(totalPoints)));
        currSubj = 0;
        updateUI();
        updateModeUI();
        nextButton.setText(Lang.NextSubjectLabel);
    }
    protected String replaceToHtml(String str)
    {
        return str.replaceAll("<","&lt;").replaceAll("\n","<br>").replaceAll("\t","&nbsp;");
    }
    protected String getEnunt()
    {
        return ("<html><font color='blue' size='6'><b>"+replaceToHtml(subj.getEnunt())+"</b></font></html>");
    }
    protected void updateUI()
    {
        subj = test.getSubjects().get(currSubj);
        testField.setText(getEnunt());
        Map<String, Integer> options = subj.getOptions();
        int i = 0;
        for (HashMap.Entry<String, Integer> entry : options.entrySet())
        {
            optionsField[i].setText(Lang.TestOptionContentLabel.replace("{{$var}}",replaceToHtml(entry.getKey())));
            checkBox[i].setSelected(false);
            checkBox[i].setName(entry.getKey());
            checkBox[i].setBackground(null);
            i++;
        }

        totalpointLabel.setText(Lang.CurrentSubjectLabel.replace("{{$totalSubj}}",String.valueOf(test.getSubjects().size())).replace("{{$currSubj}}",String.valueOf(currSubj+1)));
        if (currSubj + 1 == test.getSubjects().size())
            nextButton.setText(Lang.SendTestLabel);
    }
    protected void updateModeUI()
    {
        for (HashMap.Entry<String, Integer> entry : subj.getOptions().entrySet())
        {
            for (String aux : choiseField.get(currSubj))
            {
                if (!aux.equals(entry.getKey()))
                    continue;

                if (entry.getValue() == 0)
                {
                    for (int i = 0; i < 4; ++i)
                    {
                        if (!checkBox[i].getName().equals(entry.getKey()))
                            continue;

                        checkBox[i].setBackground(Color.RED);
                        checkBox[i].setSelected(true);
                    }
                    continue;
                }
                for (int i = 0; i < 4; ++i)
                {
                    if (!checkBox[i].getName().equals(entry.getKey()))
                        continue;

                    checkBox[i].setBackground(Color.GREEN);
                    checkBox[i].setSelected(true);
                }
            }
            for(int i=0;i<4;++i)
                if(checkBox[i].getName().equals(entry.getKey()) &&
                        entry.getValue() > 0 && checkBox[i].getBackground() != Color.GREEN && checkBox[i].getBackground() != Color.RED)
                    checkBox[i].setBackground(Color.GREEN);
        }

    }
}
