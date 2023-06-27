package UI;
import javax.swing.JButton;
import javax.swing.JLabel;
import Client.User;
import Controller.*;
import Lang.Lang;
import Util.Frame;
public class MainUserUI
{
    public MainUserUI(User client)
    {
        Frame frame = new Frame(Lang.DashboardTitle,400, 220);

        JLabel welcomeLabel = new JLabel(Lang.WelcomeLabel.replace("{{$lastName}}",client.getLastName()).replace("{{$name}}",client.getName()));
        welcomeLabel.setBounds(0, 0, 400, 80);
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        welcomeLabel.setVerticalAlignment(JLabel.CENTER);

        JButton studentButton = new JButton(Lang.ManageProfileUserLabel);
        studentButton.setBounds(140, 80, 120, 30);
        studentButton.addActionListener(e ->
        {
            frame.dispose();
            Controller.ShowProfileUI();
        });

        JButton chaptersButton = new JButton(Lang.ChaptersLabel);
        chaptersButton.setBounds(150, 120, 100, 30);
        chaptersButton.addActionListener(e ->
        {
            frame.dispose();
            Controller.ShowChaptersUI();
        });
        frame.add(welcomeLabel);
        frame.add(studentButton);
        frame.add(chaptersButton);
    }
}
