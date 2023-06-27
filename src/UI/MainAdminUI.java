package UI;

import Controller.*;
import Client.Admin;
import javax.swing.*;
import Lang.Lang;
import Util.Frame;
public class MainAdminUI
{
    public MainAdminUI(Admin client)
    {
        Frame frame = new Frame(Lang.DashboardTitle,400, 290);

        JLabel welcomeLabel = new JLabel(Lang.WelcomeLabel.replace("{{$lastName}}",client.getLastName()).replace("{{$name}}",client.getName()));
        welcomeLabel.setBounds(0, 0, 400, 80);
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        welcomeLabel.setVerticalAlignment(JLabel.CENTER);

        JButton profilButton = new JButton(Lang.ManageProfileAdminLabel);
        profilButton.setBounds(100, 80, 200, 30);
        profilButton.addActionListener(e ->
        {
            frame.dispose();
            Controller.ShowProfileUI();
        });

        JButton studentButton = new JButton(Lang.ManageUsersLabel);
        studentButton.setBounds(100, 120, 200, 30);
        studentButton.addActionListener(e ->
        {
            frame.dispose();
            Controller.ShowUsersUI();
        });

        JButton chaptersButton = new JButton(Lang.ManageChaptersLabel);
        chaptersButton.setBounds(100, 160, 200, 30);
        chaptersButton.addActionListener(e ->
        {
            frame.dispose();
            Controller.ShowChaptersAdminUI();
        });

        JButton subjButton = new JButton(Lang.ManageSubjectsLabel);
        subjButton.setBounds(100, 200, 200, 30);
        subjButton.addActionListener(e ->
        {
            frame.dispose();
            Controller.ShowSearchSubjUI();
        });
        frame.add(welcomeLabel);
        frame.add(profilButton);
        frame.add(studentButton);
        frame.add(chaptersButton);
        frame.add(subjButton);
    }
}
