package UI;

import Controller.Controller;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

import Lang.Lang;
import Learn.Chapter;
import Learn.Lesson;
import Learn.Subject;
import Learn.Test;
import Util.Output;
import Util.FileHandler;
import Util.Util;
import Util.Frame;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class AddTestUI implements CloseFrame
{
    private final JTextField[] pointField;
    private final List<Subject> subjectList;
    private final JLabel totalpointLabel;
    private final JTextArea testField;
    private final JButton addButton,addSubjButton;
    private final JTextArea[] optionsField;
    private final Frame frame;
    public AddTestUI()
    {
        subjectList = new ArrayList<>();
        frame = new Frame(Lang.AddTestTitle,1200, 720);
        frame.getContentPane().setBackground(Color.GRAY);


        JLabel testLabel = new JLabel(Lang.TestLabel);
        testLabel.setBounds(20, 20, 200, 30);

        testField = new JTextArea();
        testField.setLineWrap(true);
        testField.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(testField);
        scrollPane.setBounds(20, 50, 880, 100);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        optionsField = new JTextArea[4];
        pointField = new JTextField[4];
        for(int i=0;i<4;++i)
        {
            int currY = (130*i);
            JLabel aLabel = new JLabel(Lang.TestOptionLabel.replace("{{$id}}",String.valueOf(i+1)));
            aLabel.setBounds(20, 150+currY, 200, 30);

            optionsField[i] = new JTextArea();
            optionsField[i].setLineWrap(true);
            optionsField[i].setWrapStyleWord(true);

            JScrollPane scrollOptions = new JScrollPane(optionsField[i]);
            scrollOptions.setBounds(20, 180+currY, 880, 70);
            scrollOptions.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            int yPos = 250+currY;
            JLabel pointLabel = new JLabel(Lang.PointsLabel);
            pointLabel.setBounds(20, yPos, 50, 30);

            pointField[i] = new JTextField("0");
            pointField[i].setBounds(70, yPos, 150, 30);
            final int index=i;
            pointField[i].getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    checkPoints(index);
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    checkPoints(index);
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    checkPoints(index);
                }
            });

            frame.add(scrollOptions);
            frame.add(aLabel);
            frame.add(pointLabel);
            frame.add(pointField[i]);
        }
        totalpointLabel = new JLabel(Lang.TotalPointsLabel.replace("{{$points}}","0").replace("{{$subjects}}","0"));
        totalpointLabel.setBounds(975, 20, 150, 50);

        addButton = new JButton(Lang.AddTestLabel);
        addButton.setBounds(975, 70, 150, 50);
        addButton.setEnabled(false);

        addSubjButton = new JButton(Lang.AddAnotherSubject);
        addSubjButton.setBounds(975, 120, 150, 50);
        addSubjButton.setEnabled(false);

        JButton backButton = new JButton(Lang.Cancel);
        backButton.setBounds(975, 170, 150, 50);

        addSubjButton.addActionListener(e -> {
            if (addSubj())
                checkPoints(0);
        });
        addButton.addActionListener(e -> addTest());
        backButton.addActionListener(e -> closeFrame(false));

        frame.add(totalpointLabel);
        frame.add(testLabel);
        frame.add(scrollPane);
        frame.add(addButton);
        frame.add(backButton);
        frame.add(addSubjButton);

        frame.closeEvent(this);
    }
    public void closeFrame(boolean onlyFrame)
    {
        frame.dispose();
        if(Controller.getIdLesson() == 0)
            Controller.ShowLessonListAdminUI();
        else
            Controller.ShowLessonAdminUI();
    }
    private void addTest()
    {
        if(!addSubj())
            return;

        int idChapter = Controller.getIdChapter(),idLesson = Controller.getIdLesson();
        Chapter chapter = Controller.getChapters().get(idChapter);
        Lesson lesson = null;
        String safename;
        if(idLesson == 0)
            safename = "finaltest.txt";
        else
        {
            lesson = chapter.getLessons().get(idLesson);
            safename = lesson.getFileName();
        }
        FileHandler file = new FileHandler("data/"+chapter.getName() + "_tests/" + safename);

        for (Subject obj : subjectList)
        {
            Map<String, String> hashMapTest = new HashMap<>();
            Map<String,Integer> hashMap = obj.getOptions();
            for (Map.Entry<String,Integer> entry : hashMap.entrySet())
                hashMapTest.put(entry.getKey(),String.valueOf(entry.getValue()));

            hashMapTest.put("enunt",obj.getEnunt());
            if(!file.write(hashMapTest))
            {
                Output.PopUpAlert(Lang.GenericError);
                return;
            }
        }

        frame.dispose();
        if(idLesson == 0)
        {
            Test test = new Test(idChapter, -1);
            chapter.setTest(test);
            Controller.ShowLessonListAdminUI();
        }
        else
        {
            Test test = new Test(idChapter,idLesson);
            lesson.setTest(test);
            Controller.ShowLessonAdminUI();
        }

        Output.PopUp(Lang.SuccessAddTest);
    }
    private void checkPoints(int idOption)
    {
        String text = pointField[idOption].getText();
        if (Util.isValidNumber(text) == 2)
        {
            SwingUtilities.invokeLater(() ->
            {
                pointField[idOption].setText("");
                Output.PopUpAlert(Lang.OnlyDigits);
            });
            return;
        }
        int totalPoint = 0,currPoint=0;
        for (Subject obj : subjectList)
            for (Map.Entry<String,Integer> entry : obj.getOptions().entrySet())
                totalPoint+=entry.getValue();

        for(int i=0;i<4;++i)
        {
            String textOp = pointField[i].getText();
            if (Util.isValidNumber(textOp) == 0)
                currPoint += Integer.parseInt(textOp);
        }
        totalPoint+=currPoint;

        int limitPoints = 100;
        if (totalPoint > limitPoints)
        {
            totalPoint-=Integer.parseInt(text);
            addButton.setEnabled(false);
            addSubjButton.setEnabled(currPoint > 0);
            SwingUtilities.invokeLater(() ->
            {
                pointField[idOption].setText("");
                Output.PopUpAlert(Lang.PointsLimit.replace("{{$limit}}",String.valueOf(limitPoints)));
            });
        }
        else if(totalPoint == limitPoints)
        {
            addButton.setEnabled(true);
            addSubjButton.setEnabled(false);
        }
        else
        {
            addSubjButton.setEnabled(currPoint > 0);
            addButton.setEnabled(false);
        }

        totalpointLabel.setText(Lang.TotalPointsLabel.replace("{{$points}}",String.valueOf(totalPoint)).replace("{{$subjects}}",String.valueOf(subjectList.size())));
    }
    private boolean checkEmpty()
    {
        if(testField.getText().trim().isEmpty())
        {
            Output.PopUpAlert(Lang.EmptySubjectField);
            return true;
        }
        for(int i=0;i<4;i++)
        {
            if(!optionsField[i].getText().trim().isEmpty())
                continue;

            Output.PopUpAlert(Lang.EmptyOptionField.replace("{{$i}}",String.valueOf(i+1)));
            return true;
        }
        return false;
    }
    private boolean addSubj()
    {
        if(checkEmpty())
            return false;

        Subject subj = new Subject();
        subj.setEnunt(testField.getText());
        testField.setText("");
        Map<String,Integer> aux = new HashMap<>();
        for(int it=0;it<4;++it)
        {
            int val;
            String pointsFromField=pointField[it].getText();
            if (Util.isValidNumber(pointsFromField) == 0)
                val = Integer.parseInt(pointsFromField);
            else
                val = 0;
            aux.put(optionsField[it].getText(),val);
            optionsField[it].setText("");
            pointField[it].setText("");
        }
        subj.setOptions(aux);
        addSubject(subj);

        return true;
    }
    private void addSubject(Subject subj)
    {
        subjectList.add(subj);
    }
}
