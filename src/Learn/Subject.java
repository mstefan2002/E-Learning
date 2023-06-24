package Learn;

import java.util.Map;

public class Subject
{
    private String enunt;
    private Map<String,Integer> options;
    public String getEnunt()
    {
        return enunt;
    }
    public Map<String,Integer> getOptions()
    {
        return options;
    }
    public void setEnunt(String aux)
    {
        enunt = aux;
    }
    public void setOptions(Map<String,Integer> aux)
    {
        options = aux;
    }
}
