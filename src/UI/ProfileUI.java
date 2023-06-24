package UI;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JPanel;
import javax.swing.JButton;
import Client.Client;
import Client.Admin;
import Controller.*;
import Util.Output;
import Util.Util;
import Lang.Lang;
public class ProfileUI
{
    private final JTextField nameField,lastnameField,emailField,ageField;
    private final Object cl;
    public ProfileUI(Object ObClient)
    {
        this.cl = ObClient;
        JFrame frame = new JFrame(Lang.ProfileTitle);
        frame.setSize(300, 350);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridLayout(0,1));

        JLabel nameLabel = new JLabel(Lang.NameLabel);
        JLabel lastnameLabel = new JLabel(Lang.LastNameLabel);
        JLabel emailLabel = new JLabel(Lang.EmailLabel);
        JLabel ageLabel = new JLabel(Lang.AgeLabel);

        Client client = (Client)ObClient;
        nameField = new JTextField(client.getName());
        lastnameField = new JTextField(client.getLastName());
        emailField = new JTextField(client.getEmail());
        ageField = new JTextField(String.valueOf(client.getAge()));

        String role;
        if(ObClient instanceof Admin)
            role = Lang.ProfileRoleAdminLabel;
        else
            role = Lang.ProfileRoleUserLabel;
        JLabel roleLabel = new JLabel(role);
        JLabel usernameLabel = new JLabel(Lang.ProfileUserNameLabel.replace("{{$userName}}",client.getUsername()));

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton backButton = new JButton(Lang.Back);
        backButton.addActionListener(e -> Controller.goBackDashboard(frame));

        JButton saveButton = new JButton(Lang.Save);
        saveButton.addActionListener(e -> saveEdit());

        buttonPanel.add(backButton);
        buttonPanel.add(saveButton);

        frame.add(nameLabel);
        frame.add(nameField);
        frame.add(lastnameLabel);
        frame.add(lastnameField);
        frame.add(emailLabel);
        frame.add(emailField);
        frame.add(ageLabel);
        frame.add(ageField);
        frame.add(roleLabel);
        frame.add(usernameLabel);
        frame.add(buttonPanel);

        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
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
    private void saveEdit()
    {
        String name = nameField.getText();
        switch (Util.isValidName(name)) {
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
        switch (Util.isValidName(lastname)) {
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
        switch (Util.isValidEmail(email)) {
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
        switch (Util.isValidNumber(strAge)) {
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

        Client client = (Client)cl;
        client.editClient(name,lastname,email,age);
    }
}
