package UI;

import Controller.Controller;
import Learn.*;
import Util.Util;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import Util.Output;
import Util.FileHandler;
import Lang.Lang;
import Util.Frame;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditSubjectUI implements CloseFrame
{
    private final Frame frame;
    private final JLabel totalpointLabel;
    private final JTextArea testField;
    private final JTextArea[] optionsField;
    private final JTextField[] pointField;
    private final JButton saveButton;
    private final int idSubject;
    private final Subject subj;
    private final Test test;
    private Chapter chapter=null;
    private Lesson lesson=null;
    public EditSubjectUI(Object parent,int idSubject)
    {
        this.idSubject = idSubject;
        if(parent instanceof Chapter)
        {
            chapter = (Chapter) parent;
            test = chapter.getFinalTest();
        }
        else
        {
            lesson = (Lesson)parent;
            test = lesson.getTest();
        }
        subj = test.getSubjects().get(idSubject);

        frame = new Frame(Lang.EditSubjectTitle,1200, 720);
        frame.getContentPane().setBackground(Color.GRAY);

        JLabel testLabel = new JLabel(Lang.TestLabel);
        testLabel.setBounds(20, 20, 200, 30);

        testField = new JTextArea();
        testField.setLineWrap(true);
        testField.setWrapStyleWord(true);
        testField.setText(subj.getEnunt());

        saveButton = new JButton(Lang.SaveSubjectLabel);
        saveButton.setBounds(975, 70, 150, 50);
        saveButton.setEnabled(false);

        totalpointLabel = new JLabel(Lang.TotalPoints_SubjLabel.replace("{{$points}}","0"));
        totalpointLabel.setBounds(975, 20, 150, 50);

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
        int it=0;
        for (Map.Entry<String, Integer> entry : subj.getOptions().entrySet())
        {
            optionsField[it].setText(entry.getKey());
            pointField[it].setText(String.valueOf(entry.getValue()));
            ++it;
        }

        JButton delButton = new JButton(Lang.DeleteSubjectLabel);
        delButton.setBounds(975, 120, 150, 50);

        JButton backButton = new JButton(Lang.Cancel);
        backButton.setBounds(975, 170, 150, 50);

        saveButton.addActionListener(e -> saveSubject());
        delButton.addActionListener(e -> deleteSubject());
        backButton.addActionListener(e -> closeFrame(false));

        frame.add(totalpointLabel);
        frame.add(testLabel);
        frame.add(scrollPane);
        frame.add(saveButton);
        frame.add(backButton);
        frame.add(delButton);
        frame.closeEvent(this);
    }
    public void closeFrame(boolean onlyFrame)
    {
        frame.dispose();
        Controller.ShowSearchSubjUI();
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
        int currPoint=0;
        for(int i=0;i<4;++i)
        {
            String textOp = pointField[i].getText();
            if (Util.isValidNumber(textOp) == 0)
                currPoint += Integer.parseInt(textOp);
        }

        saveButton.setEnabled(currPoint > 0);
        totalpointLabel.setText(Lang.TotalPoints_SubjLabel.replace("{{$points}}",String.valueOf(currPoint)));
    }
    private void deleteSubject()
    {
        FileHandler file = new FileHandler(test.getPath());
        if (!file.deleteLine("enunt", subj.getEnunt()))
        {
            Output.PopUpAlert(Lang.GenericError);
            return;
        }
        List<Subject> subjects = test.getSubjects();
        subjects.remove(idSubject);
        if (subjects.isEmpty())
        {
            if (chapter != null)
                chapter.setTest(null);
            else
                lesson.setTest(null);

            File fileTest = new File(test.getPath());
            if (fileTest.delete())
                Output.PopUp(Lang.EmptyTestDeleted);
        }

        Output.PopUp(Lang.SuccessDeletingSubject);
        closeFrame(false);
    }
    private void saveSubject()
    {
        String testFieldText = testField.getText();
        if (testFieldText.trim().isEmpty())
        {
            Output.PopUpAlert(Lang.EmptySubjectField);
            return;
        }
        for (int i = 0; i < 4; i++)
        {
            if (!optionsField[i].getText().trim().isEmpty())
                continue;

            Output.PopUpAlert(Lang.EmptyOptionField.replace("{{$i}}",String.valueOf(i+1)));
            return;
        }

        Map<String, String> hashMap = new HashMap<>();
        Map<String, Integer> hashOp = new HashMap<>();
        hashMap.put("enunt", testFieldText);
        for (int it = 0; it < 4; ++it)
        {
            int val;
            String pointFieldText = pointField[it].getText();
            if (Util.isValidNumber(pointFieldText) == 0)
                val = Integer.parseInt(pointFieldText);
            else
                val = 0;
            String optionField = optionsField[it].getText();
            hashMap.put(optionField, String.valueOf(val));
            hashOp.put(optionField, val);
        }
        FileHandler file = new FileHandler(test.getPath());
        if (!file.replace("enunt", subj.getEnunt(), hashMap))
        {
            Output.PopUpAlert(Lang.GenericError);
            return;
        }

        subj.setEnunt(testFieldText);
        subj.setOptions(hashOp);

        Output.PopUp(Lang.SuccessEditSubject);
        closeFrame(false);
    }
}
