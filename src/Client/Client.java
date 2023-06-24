package Client;

import Util.FileHandler;
import Util.Output;
import java.util.HashMap;
import java.util.Map;
import Lang.Lang;

public class Client
{
    protected String name,lastname,email,username;
    protected int age;

    public Client(String username, String name,String lastname,String email,int age)
    {
        this.username = username;
        this.name=name;
        this.lastname=lastname;
        this.email=email;
        this.age=age;

    }
    public void editClient(String name,String lastname,String email,int age)
    {
        FileHandler file = new FileHandler("data/clients.txt");
        file.read(new FileHandler.GetReadDataCallback()
        {
            public boolean getHash(Map<String, String> hash)
            {
                if (!hash.get("email").equals(email))
                    return true;
                if(!hash.get("user").equals(getUsername()))
                    Output.PopUpAlert(Lang.EmailTaken);
                return true;
            }
            @Override
            public void onComplete() // we didnt find any account with that email or username so we good to go
            {
                setName(name);
                setLastName(lastname);
                setEmail(email);
                setAge(age);
                Map<String, String> hashMap = new HashMap<>();
                hashMap.put("name",name);
                hashMap.put("lastname",lastname);
                hashMap.put("email",email);
                hashMap.put("age",String.valueOf(age));

                if(file.edit("user",username,hashMap))
                    Output.PopUp(Lang.SuccessSaveData);
                else
                    Output.PopUpAlert(Lang.ErrorSaveData);
            }
        });
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public void setLastName(String name)
    {
        this.lastname = name;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }
    public void setAge(int age)
    {
        this.age = age;
    }
    public String getUsername()
    {
        return username;
    }
    public String getLastName()
    {
        return lastname.isEmpty() ? "-" : lastname;
    }

    public String getName()
    {
        return name.isEmpty() ? "-" : name;
    }

    public int getAge()
    {
        return age;
    }

    public String getEmail()
    {
        return email.isEmpty() ? "-" : email;
    }
}
