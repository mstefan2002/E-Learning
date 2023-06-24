package Util;

import javax.swing.*;
import java.awt.*;
import java.awt.print.*;

public class PrinterUtil implements Printable
{
    private final JScrollPane jEditorPane;

    public PrinterUtil(JScrollPane jEditorPane)
    {
        this.jEditorPane = jEditorPane;
    }

    @Override
    public int print(Graphics g, PageFormat pf, int pageIndex)
    {
        if (pageIndex > 0)
            return NO_SUCH_PAGE;

        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());

        jEditorPane.printAll(g2d);

        return PAGE_EXISTS;
    }

    public void printJEditorPane()
    {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);

        if (!job.printDialog())
            return;

        try
        {
            job.print();
        }
        catch (PrinterException e)
        {
            e.printStackTrace();
        }
    }
}
