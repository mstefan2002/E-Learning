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
                if(hash.get("user").equals(getUsername())) // skip our account
                    return true;
                if (!hash.get("email").equals(email)) // skip if the email doesn't match
                    return true;

                Output.PopUpAlert(Lang.EmailTaken);
                return false; // we found an account that matches the new email
            }
            @Override
            public void onComplete() // we didn't find any account with that email or username, so we're good to go
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

                if(!file.edit("user",username,hashMap))
                {
                    Output.PopUpAlert(Lang.ErrorSaveData);
                    return;
                }
                Output.PopUp(Lang.SuccessSaveData);
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
