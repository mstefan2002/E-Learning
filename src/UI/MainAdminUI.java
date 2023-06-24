package UI;

import Controller.*;
import Client.Admin;
import javax.swing.*;
import Lang.Lang;
public class MainAdminUI
{
    public MainAdminUI(Admin client)
    {
        JFrame frame = new JFrame(Lang.DashboardTitle);
        frame.setSize(400, 290);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel welcomeLabel = new JLabel(Lang.WelcomeLabel.replace("{{$lastName}}",client.getLastName()).replace("{{$name}}",client.getName()));
        welcomeLabel.setBounds(0, 0, 400, 80);
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        welcomeLabel.setVerticalAlignment(JLabel.CENTER);


        JButton profilButton = new JButton(Lang.ManageProfileAdminLabel);
        profilButton.setBounds(100, 80, 200, 30);
        profilButton.addActionListener(e ->
        {
            if (e.getSource() == profilButton)
            {
                frame.dispose();
                Controller.ShowProfileUI();
            }
        });

        JButton studentButton = new JButton(Lang.ManageUsersLabel);
        studentButton.setBounds(100, 120, 200, 30);
        studentButton.addActionListener(e ->
        {
            if (e.getSource() == studentButton)
            {
                frame.dispose();
                Controller.ShowUsersUI();
            }
        });

        JButton chaptersButton = new JButton(Lang.ManageChaptersLabel);
        chaptersButton.setBounds(100, 160, 200, 30);
        chaptersButton.addActionListener(e ->
        {
            if (e.getSource() == chaptersButton)
            {
                frame.dispose();
                Controller.ShowChaptersAdminUI();
            }
        });

        JButton subjButton = new JButton(Lang.ManageSubjectsLabel);
        subjButton.setBounds(100, 200, 200, 30);
        subjButton.addActionListener(e ->
        {
            if (e.getSource() == subjButton)
            {
                frame.dispose();
                Controller.ShowSearchSubjUI();
            }
        });
        frame.add(welcomeLabel);
        frame.add(profilButton);
        frame.add(studentButton);
        frame.add(chaptersButton);
        frame.add(subjButton);

        frame.setLayout(null);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
