package com.example.basicreminnder;

public class Timee {
    public String task;
    public String ctime;
    public String chour;

    public Timee() {
    }

    public Timee(String task, String ctime, String chour) {
        this.task = task;
        this.ctime = ctime;
        this.chour = chour;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getChour() {
        return chour;
    }

    public void setChour(String chour) {
        this.chour = chour;
    }
}
