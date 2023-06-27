package UI;
import Util.Frame;

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
