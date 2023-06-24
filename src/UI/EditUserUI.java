package UI;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import Controller.*;
import Learn.Chapter;
import Learn.Lesson;
import Util.Output;
import Util.FileHandler;
import Util.Util;
import Client.User;
import Lang.Lang;

public class EditUserUI
{
    private final JFrame frame,originFrame;
    private final User client;
    private final int curPage;
    private final JTextField nameField,lastnameField,emailField,ageField,userField,passwordField;

    public EditUserUI(JFrame originFrame,User userclient,int curPage)
    {
        this.client = userclient;
        this.curPage=curPage;
        this.originFrame=originFrame;

        String userName = userclient.getUsername();
        frame = new JFrame(Lang.EditUserTitle.replace("{{$userName}}",userName));
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridLayout(0,2));

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0,1));
        panel.setSize(300,400);

        JLabel userLabel = new JLabel(Lang.UserNameLabel);
        JLabel passwordLabel = new JLabel(Lang.EditPasswordLabel);
        JLabel nameLabel = new JLabel(Lang.NameLabel);
        JLabel lastnameLabel = new JLabel(Lang.LastNameLabel);
        JLabel emailLabel = new JLabel(Lang.EmailLabel);
        JLabel ageLabel = new JLabel(Lang.AgeLabel);

        userField = new JTextField(userName);
        passwordField = new JTextField();
        nameField = new JTextField(userclient.getName());
        lastnameField = new JTextField(userclient.getLastName());
        emailField = new JTextField(userclient.getEmail());
        ageField = new JTextField(String.valueOf(userclient.getAge()));

        JButton backButton = new JButton(Lang.Cancel);
        backButton.addActionListener(e -> frame.dispose());

        JButton deleteButton = new JButton(Lang.DeleteUserLabel);
        deleteButton.addActionListener(e ->
        {
            FileHandler file = new FileHandler("data/clients.txt");
            if(!file.deleteLine("user",userName))
            {
                Output.PopUpAlert(Lang.GenericError);
                return;
            }
            Output.PopUp(Lang.SuccessDeletingUser);
            closeFrame();
        });

        JButton saveButton = new JButton(Lang.Save);
        saveButton.addActionListener(e -> EditUser());

        JPanel panelTests = new JPanel();
        panelTests.setLayout(new GridLayout(0,1));
        panelTests.setSize(300,400);

        for(int it=1,szChapter=Controller.getChapters().size();it<=szChapter;++it)
        {
            Chapter chapter = Controller.getChapters().get(it);
            String chapterName = chapter.getName();
            for (int i = 1,szLesson=chapter.getLessonsSize(); i <= szLesson; ++i)
            {
                Lesson lesson = chapter.getLessons().get(i);
                if (lesson.hasTest())
                    panelTests.add(addTest(chapterName,i,lesson.getTest().getTotalPoints(),lesson.getName()));
            }
            if (chapter.hasTest())
                panelTests.add(addTest(chapterName,0,chapter.getFinalTest().getTotalPoints(),chapterName));
        }
        JScrollPane scrollPane = new JScrollPane(panelTests);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(0, 100, 800, 640);

        panel.add(userLabel);
        panel.add(userField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(lastnameLabel);
        panel.add(lastnameField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(ageLabel);
        panel.add(ageField);
        panel.add(backButton);
        panel.add(deleteButton);
        panel.add(saveButton);
        frame.add(panel);
        frame.getContentPane().add(scrollPane);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
    public void closeFrame()
    {
        frame.dispose();
        originFrame.dispose();
        Controller.ShowUsersUI(curPage);
    }
    public JButton addTest(String chapterName, int idLesson,int totalPoints,String testName)
    {
        String resultTest;
        JButton buttTest = new JButton();
        int resultTestNumber = client.getTest(chapterName, idLesson);
        if (resultTestNumber != -1)
            resultTest = String.valueOf(resultTestNumber);
        else
            resultTest = Lang.NotAttemptTest;

        buttTest.setText(Lang.ResultTestLabel
                .replace("{{$totalPoints}}",String.valueOf(totalPoints))
                .replace("{{$resultTest}}",resultTest)
                .replace("{{$testName}}",testName)
        );
        return buttTest;
    }
    public void EditUser()
    {
        String user = userField.getText();
        switch(Util.isValidUserName(user))
        {
            case 1 -> {
                Output.PopUpAlert(Lang.EmptyUserNameField);
                return;
            }
            case 2 -> {
                Output.PopUpAlert(Lang.UserNameContainSpace);
                return;
            }
            case 3 -> {
                Output.PopUpAlert(Lang.UserNameMinChar);
                return;
            }
        }
        String password = passwordField.getText();
        if (Util.isValidPassword(password) == 2)
        {
            Output.PopUpAlert(Lang.PasswordMinMaxChar);
            return;
        }
        String name = nameField.getText();
        switch (Util.isValidName(name))
        {
            case 1 -> {
                Output.PopUpAlert(Lang.EmptyNameField);
                return;
            }
            case 2 -> {
                Output.PopUpAlert(Lang.NameInvalidChar);
                return;
            }
            case 3 -> {
                Output.PopUpAlert(Lang.NameMinChar);
                return;
            }
        }
        String lastname = lastnameField.getText();
        switch (Util.isValidName(lastname))
        {
            case 1 -> {
                Output.PopUpAlert(Lang.EmptyLastNameField);
                return;
            }
            case 2 -> {
                Output.PopUpAlert(Lang.LastNameInvalidChar);
                return;
            }
            case 3 -> {
                Output.PopUpAlert(Lang.LastNameMinChar);
                return;
            }
        }
        String email = emailField.getText();
        switch (Util.isValidEmail(email))
        {
            case 1 -> {
                Output.PopUpAlert(Lang.EmptyEmailField);
                return;
            }
            case 2 -> {
                Output.PopUpAlert(Lang.EmailInvalid);
                return;
            }
        }
        String strAge = ageField.getText();
        switch (Util.isValidNumber(strAge))
        {
            case 1 -> {
                Output.PopUpAlert(Lang.EmptyAgeField);
                return;
            }
            case 2 -> {
                Output.PopUpAlert(Lang.AgeInvalid);
                return;
            }
        }

        int age = Integer.parseInt(strAge);
        if(age < 16)
        {
            Output.PopUpAlert(Lang.MinAgeField);
            return;
        }
        if(age > 100)
        {
            Output.PopUpAlert(Lang.OverAgeField.replace("{{$age}}",strAge));
            return;
        }
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("name",name);
        hashMap.put("lastname",lastname);
        hashMap.put("email",email);
        hashMap.put("age",strAge);
        hashMap.put("user",user);
        if(!password.isEmpty())
            hashMap.put("password",password);

        String clientUserName = client.getUsername();
        FileHandler file = new FileHandler("data/clients.txt");
        file.read(new FileHandler.GetReadDataCallback()
        {
            public boolean getHash(Map<String, String> hash)
            {
                if (hash.get("user").equals(clientUserName)) // we skip current account
                    return true;

                if (hash.get("user").equals(user)) // we found an account that have the new username
                {
                    Output.PopUpAlert(Lang.UserNameAlreadyExists);
                    return false;
                }
                if (hash.get("email").equals(email)) // we found an account that have the new email
                {
                    Output.PopUpAlert(Lang.EmailTaken);
                    return false;
                }
                return true;
            }
            @Override
            public void onComplete()
            {
                if(!file.edit("user",clientUserName,hashMap))
                {
                    Output.PopUpAlert(Lang.GenericError);
                    return;
                }
                Output.PopUp(Lang.SuccessEditUser);
                closeFrame();
            }
        });

    }
}
