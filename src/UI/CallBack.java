package UI;
import Util.Frame;

import javax.swing.*;

public interface CallBack
{
    void goBack();
    Frame getFrame();
    default void closeFrame()
    {
        if(!getFrame().canSkip(true))
            return;
        goBack();
    }
}
