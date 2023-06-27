package UI;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

import Client.Admin;
import Client.User;
import Controller.*;
import Lang.Lang;
import Util.Frame;
import Util.Output;
import Util.FileHandler;

public class LoginUI
{
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final Frame frame;
    public LoginUI()
    {
        frame = new Frame(Lang.LoginTitle,300, 200);
        frame.setLayout(new BorderLayout());

        JPanel loginPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel usernameLabel = new JLabel(Lang.LastNameLabel);
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel(Lang.PasswordLabel);
        passwordField = new JPasswordField();
        JButton loginButton = new JButton(Lang.LoginLabel);

        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(new JLabel());
        loginPanel.add(loginButton);

        frame.add(loginPanel, BorderLayout.CENTER);
        frame.setVisible(true); // to update Panel

        loginButton.addActionListener(e -> PressButton());
    }
    private void PressButton()
    {
        String username = usernameField.getText();
        if(username.trim().isEmpty())
        {
            Output.PopUpAlert(Lang.EmptyNameField);
            return;
        }
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);
        if(password.trim().isEmpty())
        {
            Output.PopUpAlert(Lang.EmptyPasswordField);
            return;
        }

        FileHandler file = new FileHandler("data/clients.txt");
        file.read(new FileHandler.GetReadDataCallback()
        {
            public boolean getHash(Map<String, String> hash)
            {
                if (!hash.get("user").equals(username))
                    return true; // we didn't find the user, so we keep reading

                if (!hash.get("password").equals(password))
                {
                    Output.PopUpAlert(Lang.LoginFailMessage);
                    return false; // we found the account, but the password doesn't match, so we stopped the reading
                }
                Object client;
                if(hash.containsKey("role") && hash.get("role").equals("admin"))
                    client = new Admin(username, hash.get("name"), hash.get("lastname"), hash.get("email"), Integer.parseInt(hash.get("age")));
                else
                    client = new User(username, hash.get("name"), hash.get("lastname"), hash.get("email"), Integer.parseInt(hash.get("age")));
                Controller.setClient(client);
                frame.dispose();
                Controller.goBackDashboard();
                return false; // we found the account and the password match
            }

            @Override
            public void onComplete() // we didn't find the user or the password doesn't match
            {
                Output.PopUpAlert(Lang.LoginFailMessage);
            }
        });
    }
}
