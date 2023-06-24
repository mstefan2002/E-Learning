package UI;

import Controller.Controller;
import Learn.Subject;
import Learn.Test;
import Util.Util;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import Util.Output;
import Util.FileHandler;
import Lang.Lang;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class EditSubjectUI
{
    public final JFrame frame;
    public final JLabel testLabel,totalpointLabel;
    public final JTextArea testField;
    public final JTextArea[] optionsField;
    public final JTextField[] pointField;
    public final JButton backButton,saveButton,delButton;
    public final int idChapter,idLesson,idSubject;
    public final Subject subj;
    public final Test test;
    public EditSubjectUI(int idChapter,int idLesson,int idSubject)
    {
        this.idChapter = idChapter;
        this.idLesson = idLesson;
        this.idSubject = idSubject;

        if(idLesson == 0)
            test = Controller.getChapters().get(idChapter).getFinalTest();
        else
            test = Controller.getChapters().get(idChapter).getLessons().get(idLesson).getTest();

        subj = test.getSubjects().get(idSubject);

        frame = new JFrame(Lang.EditSubjectTitle);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setBackground(Color.GRAY);
        frame.setLayout(null);
        frame.setSize(1200, 720);

        testLabel = new JLabel(Lang.TestLabel);
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
        for (Map.Entry<String, Integer> entry : subj.getOptions().entrySet()) {
            optionsField[it].setText(entry.getKey());
            pointField[it].setText(String.valueOf(entry.getValue()));
            ++it;
        }

        delButton = new JButton(Lang.DeleteSubjectLabel);
        delButton.setBounds(975, 120, 150, 50);

        backButton = new JButton(Lang.Cancel);
        backButton.setBounds(975, 170, 150, 50);

        ButtonClickListener listener = new ButtonClickListener(this);
        saveButton.addActionListener(listener);
        delButton.addActionListener(listener);
        backButton.addActionListener(listener);

        frame.add(totalpointLabel);
        frame.add(testLabel);
        frame.add(scrollPane);
        frame.add(saveButton);
        frame.add(backButton);
        frame.add(delButton);

        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                frame.dispose();
                Controller.ShowSearchSubjUI();
            }
        });
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
    static class ButtonClickListener implements ActionListener
    {
        private final EditSubjectUI self;
        public ButtonClickListener(EditSubjectUI self)
        {
            this.self = self;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            Object source = e.getSource();
            if (source == self.backButton) {
                self.frame.dispose();
                Controller.ShowSearchSubjUI();
            }
            else if(source == self.delButton)
            {
                FileHandler file = new FileHandler(self.test.getPath());
                if (!file.deleteLine("enunt", self.subj.getEnunt()))
                {
                    Output.PopUpAlert(Lang.GenericError);
                    return;
                }
                boolean deleteTest = false;
                if (self.idLesson == 0)
                {
                    Controller.getChapters().get(self.idChapter).getFinalTest().getSubjects().remove(self.idSubject);
                    if (Controller.getChapters().get(self.idChapter).getFinalTest().getSubjects().size() == 0)
                    {
                        Controller.getChapters().get(self.idChapter).setTest(null);
                        deleteTest = true;
                    }
                }
                else
                {
                    Controller.getChapters().get(self.idChapter).getLessons().get(self.idLesson).getTest().getSubjects().remove(self.idSubject);
                    if (Controller.getChapters().get(self.idChapter).getLessons().get(self.idLesson).getTest().getSubjects().size() == 0)
                    {
                        Controller.getChapters().get(self.idChapter).getLessons().get(self.idLesson).setTest(null);
                        deleteTest = true;
                    }
                }
                if(deleteTest)
                {
                    File fileTest = new File(self.test.getPath());
                    if (fileTest.delete())
                        Output.PopUp(Lang.EmptyTestDeleted);
                }
                Output.PopUp(Lang.SuccessDeletingSubject);
                self.frame.dispose();
                Controller.ShowSearchSubjUI();

            }
            else if(source == self.saveButton)
            {
                String testField = self.testField.getText();
                if (testField.trim().isEmpty())
                {
                    Output.PopUpAlert(Lang.EmptySubjectField);
                    return;
                }
                for (int i = 0; i < 4; i++)
                {
                    if (self.optionsField[i].getText().trim().isEmpty())
                    {
                        Output.PopUpAlert(Lang.EmptyOptionField.replace("{{$i}}",String.valueOf(i+1)));
                        return;
                    }
                }

                Map<String, String> hashMap = new HashMap<>();
                Map<String, Integer> hashOp = new HashMap<>();
                hashMap.put("enunt", testField);
                for (int it = 0; it < 4; ++it)
                {
                    int val;
                    String pointField = self.pointField[it].getText();
                    if (Util.isValidNumber(pointField) == 0)
                        val = Integer.parseInt(pointField);
                    else
                        val = 0;
                    String optionField = self.optionsField[it].getText();
                    hashMap.put(optionField, String.valueOf(val));
                    hashOp.put(optionField, val);
                }
                FileHandler file = new FileHandler(self.test.getPath());
                if (!file.replace("enunt", self.subj.getEnunt(), hashMap))
                {
                    Output.PopUpAlert(Lang.GenericError);
                    return;
                }
                if (self.idLesson == 0)
                {
                    Controller.getChapters().get(self.idChapter).getFinalTest().getSubjects().get(self.idSubject)
                            .setEnunt(testField);
                    Controller.getChapters().get(self.idChapter).getFinalTest().getSubjects().get(self.idSubject)
                            .setOptions(hashOp);
                }
                else
                {
                    Controller.getChapters().get(self.idChapter).getLessons().get(self.idLesson)
                            .getTest().getSubjects().get(self.idSubject)
                            .setEnunt(testField);
                    Controller.getChapters().get(self.idChapter).getLessons().get(self.idLesson)
                            .getTest().getSubjects().get(self.idSubject)
                            .setOptions(hashOp);
                }

                Output.PopUp(Lang.SuccessEditSubject);
                self.frame.dispose();
                Controller.ShowSearchSubjUI();
            }
        }
    }
}
