package UI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.Map;

import Client.Admin;
import Client.User;
import Controller.Controller;
import Util.FileHandler;
import Util.Output;
import Util.Pagination;
import Util.Frame;
import Lang.Lang;

public class AdminUsersUI implements CallBack
{
    private final Frame frame;
    public AdminUsersUI()
    {
        Admin client = (Admin)Controller.getClient();
        frame = new Frame(Lang.ManagerUsersTitle,600, 500);

        LinkedList<JButton> myList = new LinkedList<>();

        FileHandler file = new FileHandler("data/clients.txt");
        file.read(new FileHandler.GetReadDataCallback()
        {
            public boolean getHash(Map<String, String> hash)
            {
                String currentUserName = hash.get("user");
                if (currentUserName.equals(client.getUsername()))
                    return true; // we don't want to display our admin account

                StringBuilder aux = new StringBuilder();
                aux.append("<html><b>").append(hash.get("name")).append(" ").append(hash.get("lastname")).append("</b>(<font color='red'>@").append(currentUserName)
                        .append("</font>)<br>").append(hash.get("email")).append("</html>");

                JButton profilButton = new JButton(aux.toString());
                profilButton.setName(currentUserName);
                profilButton.setToolTipText(aux.toString());
                profilButton.addActionListener(e->pressButton(e));
                frame.add(profilButton);
                profilButton.setVisible(false);
                myList.add(profilButton);
                return true;
            }
        });


        JButton addButton = new JButton(Lang.AddStudentLabel);
        addButton.addActionListener(e->Controller.ShowAddUserUI());
        addButton.setBounds(425, 0, 150, 50);

        JButton backButton = new JButton(Lang.Back);
        backButton.addActionListener(e->closeFrame());
        backButton.setBounds(425, 50, 150, 50);

        frame.add(backButton);
        frame.add(addButton);

        Pagination.start(myList,Controller.getPageUser(),frame, 7,Controller.funcPageUser(),0,0,400,50);

        frame.closeEvent(this);
    }
    public Frame getFrame(){return frame;}
    public void goBack()
    {
        Controller.goBackDashboard();
    }
    private void pressButton(ActionEvent e)
    {
        JButton element = (JButton) e.getSource();
        String userName = element.getName();
        FileHandler file = new FileHandler("data/clients.txt");
        file.read(new FileHandler.GetReadDataCallback()
        {
            public boolean getHash(Map<String, String> hash)
            {
                if (!hash.get("user").equals(userName))
                    return true;

                if(!frame.canSkip(false))
                    return false;// we found the account but he has some second frame opened, stop the reading

                User client = new User(userName, hash.get("name"), hash.get("lastname"), hash.get("email"), Integer.parseInt(hash.get("age")));
                Controller.ShowEditUserUI(client);
                return false; // we found the account, stop the reading
            }
            @Override
            public void onComplete() // we didn't find the account
            {
                Output.PopUpAlert(Lang.UserDoesntExist);
            }
        });
    }
}
