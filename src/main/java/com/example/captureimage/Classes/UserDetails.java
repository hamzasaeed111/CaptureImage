package com.example.captureimage.Classes;

public class UserDetails
{
    private String name;
    private String email;
    private String message;

    public UserDetails()
    {
        // This is default constructor.
    }

    public void setName(String name)
    {
        this.name = name;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }
    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getName()
    {
        return name;
    }
    public String getEmail()
    {
        return email;
    }
    public String getMessage()
    {
        return message;
    }
}
