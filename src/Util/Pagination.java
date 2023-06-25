package Util;

import Lang.Lang;

import javax.swing.*;
import java.util.LinkedList;
import java.util.function.Function;

public class Pagination
{
    public static void start(LinkedList<JButton> myList, int curPage, JFrame frame, int itemsPerPage, Function<Integer,Integer> func, int xPos,int yPos,int width,int height)
    {
        int szList = myList.size();
        if (szList <= itemsPerPage)
        {
            for (int i = 0; i < szList; ++i)
            {
                JButton element = myList.get(i);
                element.setBounds(xPos, (i%itemsPerPage)*height+yPos, width, height);
                element.setVisible(true);
            }
            return;
        }
        JButton prevButton = new JButton(Lang.PrevButton);
        JButton nextPageButton = new JButton(Lang.NextButton);

        nextPageButton.addActionListener(e -> Pagination.next(myList,itemsPerPage,func.apply(1),prevButton, nextPageButton));
        nextPageButton.setBounds((width-300)/2+150, itemsPerPage*height+25, 150, 50);

        prevButton.addActionListener(e -> Pagination.prev(myList,itemsPerPage,func.apply(-1),prevButton, nextPageButton));
        prevButton.setBounds((width-300)/2, itemsPerPage*height+25, 150, 50);
        prevButton.setEnabled(false);

        int startIdx = itemsPerPage * curPage;
        int endIdx = Math.min(itemsPerPage * (curPage + 1), szList);

        for (int i = 0; i < szList; ++i)
        {
            JButton element = myList.get(i);
            boolean isVisible = (i >= startIdx && i < endIdx);
            element.setBounds(xPos, (i%itemsPerPage)*height+yPos, width, height);
            element.setVisible(isVisible);
        }
        nextPageButton.setEnabled(endIdx < szList);
        prevButton.setEnabled(curPage > 0);

        frame.add(prevButton);
        frame.add(nextPageButton);
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
