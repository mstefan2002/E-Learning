package UI;

import Controller.Controller;
import Learn.Chapter;
import Learn.Lesson;
import Learn.Test;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import Lang.Lang;
import Util.Util;
public class SearchSubjUI
{
    protected JButton backButton;
    protected final JFrame frame;
    private final LinkedList<JButton> myList;
    private final ButtonClickListener listener;
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
        listener = new ButtonClickListener(this);

        myList = new LinkedList<>();
        panel = new JPanel();

        for (int i = 1,szChapter = Controller.getChapters().size(); i <= szChapter; ++i)
        {
            Chapter chapter = Controller.getChapters().get(i);
            for(int j=1,szLesson=chapter.getLessonsSize();j<=szLesson;++j)
            {
                Lesson lesson = chapter.getLessons().get(j);
                if(lesson.hasTest())
                {
                    Test test = lesson.getTest();
                    for (int w=0,szSubject = test.getSubjects().size();w<szSubject;++w)
                        addSubj(i,j,w);
                }
            }
            if(chapter.hasTest())
            {
                for (int w=0,szSubject = chapter.getFinalTest().getSubjects().size();w<szSubject;w++)
                    addSubj(i,0,w);
            }
        }


        backButton = new JButton(Lang.Back);
        backButton.addActionListener(listener);
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
    private void addSubj(int idChapter,int idLesson,int idSubject)
    {
        Chapter chapter = Controller.getChapters().get(idChapter);
        String subjectName,parentName;
        if(idLesson == 0)
        {
            subjectName = chapter.getFinalTest().getSubjects().get(idSubject).getEnunt();
            parentName = chapter.getName();
        }
        else
        {
            Lesson lesson = chapter.getLessons().get(idLesson);
            subjectName = lesson.getTest().getSubjects().get(idSubject).getEnunt();
            parentName = lesson.getName();
        }
        String aux = Lang.SubjectLabel.replace("{{$subjectName}}",subjectName).replace("{{$parentName}}",parentName);

        JButton chapterButton = new JButton(aux);
        chapterButton.setName(idChapter+" "+idLesson+" "+idSubject);
        chapterButton.setToolTipText(aux);
        chapterButton.addActionListener(listener);
        panel.add(chapterButton);
        myList.add(chapterButton);
    }
    static class ButtonClickListener implements ActionListener
    {
        private final SearchSubjUI self;
        public ButtonClickListener(SearchSubjUI self)
        {
            this.self = self;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            Object source = e.getSource();
            if (source == self.backButton)
                Controller.goBackDashboard(self.frame);
            else
            {
                String[] aux = ((JButton) source).getName().split(" ");
                int idChapter = 0,idLesson =0,idSubject =0;
                if(Util.isValidNumber(aux[0]) == 0)
                    idChapter = Integer.parseInt(aux[0]);
                if(Util.isValidNumber(aux[1]) == 0)
                    idLesson = Integer.parseInt(aux[1]);
                if(Util.isValidNumber(aux[2]) == 0)
                    idSubject = Integer.parseInt(aux[2]);
                Controller.ShowEditSubjectUI(idChapter,idLesson,idSubject);
                self.frame.dispose();
            }
        }
    }
}
