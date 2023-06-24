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

public class AdminUsersUI
{
    protected int itemsPerPage = 7;
    protected JButton addButton,backButton,prevButton,nextPageButton;
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
        StringBuilder aux = new StringBuilder();
        FileHandler file = new FileHandler("data/clients.txt");
        file.read(new FileHandler.GetReadDataCallback()
        {
            public boolean getHash(Map<String, String> hash)
            {
                if (hash.get("user").equals(client.getUsername()))
                    return true; // we dont want to show our account(admin)

                aux.append("<html><b>").append(hash.get("name")).append(" ").append(hash.get("lastname")).append("</b>(<font color='red'>@").append(hash.get("user"))
                        .append("</font>)<br>").append(hash.get("email")).append("</html>");

                JButton profilButton = new JButton(aux.toString());
                profilButton.setName(hash.get("user"));
                profilButton.setToolTipText(aux.toString());
                profilButton.addActionListener(listener);
                frame.add(profilButton);
                profilButton.setVisible(false);
                myList.add(profilButton);
                aux.setLength(0);
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

        int i =0;
        for (JButton element : myList)
        {
            element.setBounds(0, (i%itemsPerPage)*50, 400, 50);
            ++i;
        }
        prevButton = new JButton(Lang.PrevButton);
        nextPageButton = new JButton(Lang.NextButton);
        Pagination.start(myList,prevButton, nextPageButton, curPage,frame,listener,itemsPerPage);

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
            else if(source == self.prevButton)
            {
                --self.currPage;
                Pagination.prev(self.myList,self.itemsPerPage,self.currPage,self.prevButton, self.nextPageButton);
            }
            else if(source == self.nextPageButton)
            {
                ++self.currPage;
                Pagination.next(self.myList,self.itemsPerPage,self.currPage,self.prevButton, self.nextPageButton);
            }
            else
            {
                for (JButton element : self.myList)
                {
                    if(source == element)
                    {
                        String buttonName = element.getName();
                        FileHandler file = new FileHandler("data/clients.txt");
                        file.read(new FileHandler.GetReadDataCallback()
                        {
                            public boolean getHash(Map<String, String> hash)
                            {
                                if (!hash.get("user").equals(buttonName))
                                    return true;

                                User client = new User(buttonName, hash.get("name"), hash.get("lastname"), hash.get("email"), Integer.parseInt(hash.get("age")));
                                Controller.ShowEditUserUI(self.frame,client,self.currPage);
                                return false; // we found the account, stop the reading
                            }

                            @Override
                            public void onComplete() // we didnt find the account
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
}
