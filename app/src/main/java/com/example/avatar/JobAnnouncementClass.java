package com.example.avatar;

import android.os.Parcel;
import android.os.Parcelable;

public class JobAnnouncementClass implements Parcelable {

    public String JobName, JobDepartment, JobDescription, ID, UID;

    public JobAnnouncementClass(String jobName, String jobDepartment, String jobDescription, String ID, String UID) {
        this.JobName = jobName;
        this.JobDepartment = jobDepartment;
        this.JobDescription = jobDescription;
        this.ID = ID;
        this.UID = UID;
    }
    public JobAnnouncementClass(Parcel parcel){
        this.JobName = parcel.readString();
        this.JobDepartment = parcel.readString();
        this.JobDescription = parcel.readString();
        this.ID = parcel.readString();
        this.UID = parcel.readString();
    }

    public static final Creator<JobAnnouncementClass> CREATOR = new Creator<JobAnnouncementClass>() {
        @Override
        public JobAnnouncementClass createFromParcel(Parcel in) {
            return new JobAnnouncementClass(in);
        }

        @Override
        public JobAnnouncementClass[] newArray(int size) {
            return new JobAnnouncementClass[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(JobName);
        dest.writeString(JobDepartment);
        dest.writeString(JobDescription);
        dest.writeString(ID);
        dest.writeString(UID);
    }
}
