package UI;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JButton;
import Controller.*;
import Util.Output;
import Util.Util;
import Util.FileHandler;
import Lang.Lang;
public class AddUserUI
{
    private final JFrame frame,originFrame;
    private final JTextField nameField,lastnameField,emailField,ageField,userField,passwordField;

    public AddUserUI(JFrame originFrame)
    {
        this.originFrame = originFrame;
        frame = new JFrame(Lang.AddUserTitle);
        frame.setSize(300, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridLayout(0,1));

        JLabel userLabel = new JLabel(Lang.UserNameLabel);
        JLabel passwordLabel = new JLabel(Lang.PasswordLabel);
        JLabel nameLabel = new JLabel(Lang.NameLabel);
        JLabel lastnameLabel = new JLabel(Lang.LastNameLabel);
        JLabel emailLabel = new JLabel(Lang.EmailLabel);
        JLabel ageLabel = new JLabel(Lang.AgeLabel);

        userField = new JTextField();
        passwordField = new JTextField();
        nameField = new JTextField();
        lastnameField = new JTextField();
        emailField = new JTextField();
        ageField = new JTextField();

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setSize(300, 150);

        JButton backButton = new JButton(Lang.Cancel);
        backButton.addActionListener(e -> frame.dispose());

        JButton addButton = new JButton(Lang.Add);
        addButton.addActionListener(e -> addUser());

        buttonPanel.add(backButton);
        buttonPanel.add(addButton);

        frame.add(userLabel);
        frame.add(userField);
        frame.add(passwordLabel);
        frame.add(passwordField);
        frame.add(nameLabel);
        frame.add(nameField);
        frame.add(lastnameLabel);
        frame.add(lastnameField);
        frame.add(emailLabel);
        frame.add(emailField);
        frame.add(ageLabel);
        frame.add(ageField);
        frame.add(buttonPanel);

        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
    private void addUser()
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
        switch (Util.isValidPassword(password))
        {
            case 1 -> {
                Output.PopUpAlert(Lang.EmptyPasswordField);
                return;
            }
            case 2 -> {
                Output.PopUpAlert(Lang.PasswordMinMaxChar);
                return;
            }
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
        hashMap.put("password",password);

        FileHandler file = new FileHandler("data/clients.txt");
        file.read(new FileHandler.GetReadDataCallback()
        {
            public boolean getHash(Map<String, String> hash)
            {
                if (hash.get("user").equals(user))
                {
                    Output.PopUpAlert(Lang.UserNameAlreadyExists);
                    return false;
                }
                if (hash.get("email").equals(email))
                {
                    Output.PopUpAlert(Lang.EmailTaken);
                    return false;
                }
                return true;
            }
            @Override
            public void onComplete()
            {
                if(!file.write(hashMap)) {
                    Output.PopUpAlert(Lang.GenericError);
                    return;
                }
                Output.PopUp(Lang.SuccessAddUser);
                frame.dispose();
                originFrame.dispose();
                Controller.ShowUsersUI();
            }
        });

    }
}
