package UI;
import Client.Admin;
import Client.User;
import Controller.Controller;
import Util.FileHandler;
import Util.Output;
import Lang.Lang;
import Util.Pagination;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Function;

public class AdminUsersUI
{
    protected int itemsPerPage = 7;
    protected JButton addButton,backButton;
    protected JFrame frame;
    protected LinkedList<JButton> myList;
    protected int currPage;
    public AdminUsersUI(Admin client)
    {
        startUI(client, 0);
    }
    public AdminUsersUI(Admin client, int curPage)
    {
        startUI(client, curPage);
    }
    private void startUI(Admin client, int curPage)
    {
        this.currPage = curPage;

        frame = new JFrame(Lang.ManagerUsersTitle);
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ButtonClickListener listener = new ButtonClickListener(this);

        myList = new LinkedList<>();

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
                profilButton.addActionListener(listener);
                frame.add(profilButton);
                profilButton.setVisible(false);
                myList.add(profilButton);
                return true;
            }
        });


        addButton = new JButton(Lang.AddStudentLabel);
        addButton.addActionListener(listener);
        addButton.setBounds(425, 0, 150, 50);

        backButton = new JButton(Lang.Back);
        backButton.addActionListener(listener);
        backButton.setBounds(425, 50, 150, 50);

        frame.add(backButton);
        frame.add(addButton);

        Function<Integer,Integer> func = (x) -> (currPage+=x);
        Pagination.start(myList,curPage,frame,itemsPerPage,func,0,0,400,50);

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
    static class ButtonClickListener implements ActionListener
    {
        AdminUsersUI self;
        public ButtonClickListener(AdminUsersUI self)
        {
            this.self = self;
        }
        @Override
        public void actionPerformed(ActionEvent e)
        {
            Object source = e.getSource();
            if(source == self.addButton)
                Controller.AddUserUI(self.frame);
            else if(source == self.backButton)
                Controller.goBackDashboard(self.frame);
            else
            {
                for (JButton element : self.myList)
                {
                    if (source != element)
                        continue;

                    String userName = element.getName();
                    FileHandler file = new FileHandler("data/clients.txt");
                    file.read(new FileHandler.GetReadDataCallback()
                    {
                        public boolean getHash(Map<String, String> hash)
                        {
                            if (!hash.get("user").equals(userName))
                                return true;

                            User client = new User(userName, hash.get("name"), hash.get("lastname"), hash.get("email"), Integer.parseInt(hash.get("age")));
                            Controller.ShowEditUserUI(self.frame, client, self.currPage);
                            return false; // we found the account, stop the reading
                        }
                        @Override
                        public void onComplete() // we didn't find the account
                        {
                            Output.PopUpAlert(Lang.UserDoesntExist);
                        }
                    });
                    return;
                }
            }
        }
    }
}
