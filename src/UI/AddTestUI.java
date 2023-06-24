package UI;

import Controller.Controller;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

import Lang.Lang;
import Learn.Chapter;
import Learn.Subject;
import Learn.Test;
import Util.Output;
import Util.FileHandler;
import Util.Util;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class AddTestUI
{
    private final JTextField[] pointField;
    private final List<Subject> subjectList;
    private final JLabel totalpointLabel;
    private final JTextArea testField;
    private final JButton addButton,addSubjButton;
    private final JTextArea[] optionsField;
    private final int idLesson,idChapter,lessonPage,chapterPage;
    private final JFrame frame;
    public AddTestUI(int idLesson,int idChapter, int lessonPage, int chapterPage)
    {
        this.idChapter = idChapter;
        this.idLesson = idLesson;
        this.lessonPage=lessonPage;
        this.chapterPage=chapterPage;

        subjectList = new ArrayList<>();
        frame = new JFrame(Lang.AddTestTitle);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setBackground(Color.GRAY);
        frame.setLayout(null);
        frame.setSize(1200, 720);

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
        backButton.addActionListener(e -> closeFrame());

        frame.add(totalpointLabel);
        frame.add(testLabel);
        frame.add(scrollPane);
        frame.add(addButton);
        frame.add(backButton);
        frame.add(addSubjButton);

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
    private void closeFrame()
    {
        frame.dispose();
        if(idLesson == 0)
            Controller.ShowLessonListAdminUI(idChapter, lessonPage, chapterPage);
        else
            Controller.ShowLessonAdminUI(idLesson, idChapter,lessonPage,chapterPage);
    }
    private void addTest()
    {
        if(!addSubj())
            return;

        Map<String, String> hashMapTest = new HashMap<>();
        Chapter chapter = Controller.getChapters().get(idChapter);
        String safename;
        if(idLesson == 0)
            safename = "finaltest.txt";
        else
            safename = chapter.getLessons().get(idLesson).getFileName();

        FileHandler file = new FileHandler("data/"+chapter.getName() + "_tests/" + safename);

        for (Subject obj : subjectList)
        {
            hashMapTest.clear();
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
            Controller.getChapters().get(idChapter).setTest(test);
            Controller.ShowLessonListAdminUI(idChapter, lessonPage, chapterPage);
        }
        else
        {
            Test test = new Test(idChapter,idLesson);
            Controller.getChapters().get(idChapter).getLessons().get(idLesson).setTest(test);
            Controller.ShowLessonAdminUI(idLesson, idChapter,lessonPage,chapterPage);
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
            if(optionsField[i].getText().trim().isEmpty())
            {
                Output.PopUpAlert(Lang.EmptyOptionField.replace("{{$i}}",String.valueOf(i+1)));
                return true;
            }
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
