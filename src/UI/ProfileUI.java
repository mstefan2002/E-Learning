package UI;

import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import javax.swing.JPanel;
import javax.swing.JButton;

import Client.Client;
import Client.Admin;
import Controller.*;
import Util.Output;
import Util.Util;
import Util.Frame;
import Lang.Lang;

public class ProfileUI implements CloseFrame
{
    private final JTextField nameField,lastnameField,emailField,ageField;
    private final Object cl;
    private final Frame frame;
    public ProfileUI()
    {
        this.cl = Controller.getClient();
        frame = new Frame(Lang.ProfileTitle,300, 350);
        frame.setLayout(new GridLayout(0,1));

        JLabel nameLabel = new JLabel(Lang.NameLabel);
        JLabel lastnameLabel = new JLabel(Lang.LastNameLabel);
        JLabel emailLabel = new JLabel(Lang.EmailLabel);
        JLabel ageLabel = new JLabel(Lang.AgeLabel);

        Client client = (Client)cl;
        nameField = new JTextField(client.getName());
        lastnameField = new JTextField(client.getLastName());
        emailField = new JTextField(client.getEmail());
        ageField = new JTextField(String.valueOf(client.getAge()));

        String role;
        if(cl instanceof Admin)
            role = Lang.ProfileRoleAdminLabel;
        else
            role = Lang.ProfileRoleUserLabel;
        JLabel roleLabel = new JLabel(role);
        JLabel usernameLabel = new JLabel(Lang.ProfileUserNameLabel.replace("{{$userName}}",client.getUsername()));

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton backButton = new JButton(Lang.Back);
        backButton.addActionListener(e -> closeFrame(false));

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

        frame.closeEvent(this);
    }
    public void closeFrame(boolean onlyFrame)
    {
        frame.dispose();
        Controller.goBackDashboard();
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
