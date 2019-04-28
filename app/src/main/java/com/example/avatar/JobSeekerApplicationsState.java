package com.example.avatar;

public class JobSeekerApplicationsState {

    public String JobName, ImageID, Reason;

    public JobSeekerApplicationsState(){

    }

    public JobSeekerApplicationsState(String JobName, String ImageID, String Reason){
        this.JobName = JobName;
        this.ImageID = ImageID;
        this.Reason = Reason;
    }
}
