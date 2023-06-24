package Util;

import javax.swing.*;

public class Output
{
    public static void PopUpAlert(String message)
    {
        JOptionPane.showMessageDialog(null, message, "Alert", JOptionPane.ERROR_MESSAGE);
    }
    public static void PopUp(String message)
    {
        JOptionPane.showMessageDialog(null, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}
