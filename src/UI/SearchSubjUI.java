package UI;

import Controller.Controller;
import Learn.Chapter;
import Learn.Lesson;
import Learn.Subject;
import Learn.Test;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import Lang.Lang;

public class SearchSubjUI
{
    private final JFrame frame;
    private final LinkedList<JButton> myList;
    private final JTextField searchField;
    private final JPanel panel;
    public SearchSubjUI()
    {
        frame = new JFrame(Lang.ManagerSubjectTitle);
        frame.setSize(1000, 800);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel searchLabel = new JLabel(Lang.SearchLabel);
        searchLabel.setBounds(10,10,400,20);
        frame.add(searchLabel);
        searchField = new JTextField();
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                search();
            }
        });
        searchField.setBounds(10,30,400,30);
        frame.add(searchField);

        myList = new LinkedList<>();
        panel = new JPanel();

        Map<Integer,Chapter> chapters = Controller.getChapters();
        for (int i = 1,szChapter = chapters.size(); i <= szChapter; ++i)
        {
            Chapter chapter = chapters.get(i);
            Map<Integer,Lesson> lessons = chapter.getLessons();
            for(int j=1,szLesson=lessons.size();j<=szLesson;++j)
            {
                Lesson lesson = lessons.get(j);
                if(lesson.hasTest())
                    addSubj(lesson);
            }
            if(chapter.hasTest())
                addSubj(chapter);
        }

        JButton backButton = new JButton(Lang.Back);
        backButton.addActionListener(e->Controller.goBackDashboard(frame));
        backButton.setBounds(825, 10, 150, 30);

        frame.add(backButton);
        panel.setLayout(new GridLayout(0,1));
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(0, 100, 800, 640);
        frame.getContentPane().add(scrollPane);

        frame.setLayout(null);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                Controller.goBackDashboard(frame);
            }
        });
    }
    private void search()
    {
        panel.removeAll();
        String aux = searchField.getText().toLowerCase();
        for (JButton element : myList)
            if(element.getText().toLowerCase().contains(aux))
                panel.add(element);

        panel.updateUI();
    }
    private void addSubj(Object obj)
    {
        Test test;
        String parentName;
        if(obj instanceof Chapter chapter)
        {
            test = chapter.getFinalTest();
            parentName = chapter.getName();
        }
        else
        {
            Lesson lesson = (Lesson)obj;
            test = lesson.getTest();
            parentName = lesson.getName();
        }

        List<Subject> subjects = test.getSubjects();
        String subjectLabel = Lang.SubjectLabel.replace("{{$parentName}}", parentName);
        for (int i=0,szSubject = subjects.size();i<szSubject;++i)
        {
            String aux = subjectLabel.replace("{{$subjectName}}", subjects.get(i).getEnunt());

            JButton chapterButton = new JButton(aux);
            chapterButton.putClientProperty("parent",obj);
            chapterButton.putClientProperty("idSubject",i);
            chapterButton.setToolTipText(aux);
            chapterButton.addActionListener(this::pressSubject);
            panel.add(chapterButton);
            myList.add(chapterButton);
        }
    }
    private void pressSubject(ActionEvent e)
    {
        JButton button = (JButton) e.getSource();
        Controller.ShowEditSubjectUI(button.getClientProperty("parent"),(int)button.getClientProperty("idSubject"));
        frame.dispose();
    }
}
