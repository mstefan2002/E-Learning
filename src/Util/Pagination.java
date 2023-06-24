package Util;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class Pagination
{
    public static void start(LinkedList<JButton> myList,JButton prevButton, JButton nextPageButton, int curPage,JFrame frame,Object listener,int itemsPerPage)
    {
        int szList = myList.size();
        if(szList > itemsPerPage)
        {
            prevButton.addActionListener((ActionListener)listener);
            prevButton.setBounds(50, 375, 150, 50);
            prevButton.setEnabled(false);

            nextPageButton.addActionListener((ActionListener)listener);
            nextPageButton.setBounds(200, 375, 150, 50);

            int startIdx = itemsPerPage * curPage;
            int endIdx = Math.min(itemsPerPage * (curPage + 1), szList);

            for (int i = 0; i < szList; ++i)
            {
                JButton element = myList.get(i);
                boolean isVisible = (i >= startIdx && i < endIdx);
                element.setVisible(isVisible);
            }
            nextPageButton.setEnabled(endIdx < szList);
            prevButton.setEnabled(curPage > 0);

            frame.add(prevButton);
            frame.add(nextPageButton);
        }
        else
        {
            for (JButton element : myList)
                element.setVisible(true);
        }
    }
    public static void prev(LinkedList<JButton> myList,int itemsPerPage,int currPage,JButton prevButton, JButton nextPageButton)
    {
        int szList = myList.size();
        int startIdx = itemsPerPage * currPage;
        int endIdx = Math.min(itemsPerPage * (currPage + 1), szList);

        for (int i = 0; i < szList; ++i)
        {
            JButton element = myList.get(i);
            boolean isVisible = (i >= startIdx && i < endIdx);
            element.setVisible(isVisible);
        }

        nextPageButton.setEnabled(endIdx < szList);
        prevButton.setEnabled(currPage > 0);
    }
    public static void next(LinkedList<JButton> myList,int itemsPerPage,int currPage,JButton prevButton, JButton nextPageButton)
    {
        int szList = myList.size();
        int startIdx = itemsPerPage * currPage;
        int endIdx = Math.min(itemsPerPage * (currPage + 1), szList);

        for (int i = 0; i < szList; ++i)
        {
            JButton element = myList.get(i);
            boolean isVisible = (i >= startIdx && i < endIdx);
            element.setVisible(isVisible);
        }

        nextPageButton.setEnabled(endIdx < szList);
        prevButton.setEnabled(true);
    }
}
