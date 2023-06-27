package Util;

import Controller.Controller;
import Lang.Lang;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import UI.CallBack;
import UI.CloseFrame;
public class Frame extends JFrame
{
    private CallBack callBack;
    public Frame(String title,int width, int height)
    {
        super(title);
        this.setSize(width, height);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(null);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    public void closeEvent(CallBack callBack)
    {
        this.callBack = callBack;
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                if(!closeFrame())
                {
                    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    return;
                }
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }
        });
    }
    public void closeEvent(CloseFrame closeF)
    {
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                closeF.closeFrame(true);
            }
        });
    }
    public boolean canSkip(boolean dispose)
    {
        if(Controller.isPreventGoingBack() != 0)
        {
            Output.PopUpAlert(Lang.CloseTabsMessage);
            return false;
        }
        if(dispose)
            dispose();
        return true;
    }
    private boolean closeFrame()
    {
        if(!canSkip(true))
            return false;

        callBack.goBack();
        return true;
    }
}
