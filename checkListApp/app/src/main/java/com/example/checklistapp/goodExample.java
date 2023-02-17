package com.example.checklistapp;

public class goodExample {
    private int id;
    private String name;
    private int count;

    public goodExample(int Id, String Name,int Count){
        id = Id;
        name = Name;
        count = Count;
    }


    public int getCount(){return count;}
    public int getId(){return id;}
    public String getName(){
        return  name;
    }
    public void setCount(int Count){count = Count;}
    public void setId(int Id){id = Id;}
    public void setName(String Name){name = Name;}

    public void increase(){count++;}
    public void decrease(){count--;}
}
