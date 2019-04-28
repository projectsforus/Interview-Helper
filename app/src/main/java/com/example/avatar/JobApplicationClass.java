package com.example.avatar;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class JobApplicationClass implements Parcelable {

    public String AnnouncementID, Status, ApplierID, Comment, ApplicationID;
    public List<String> Answers;

    public JobApplicationClass(String AnnouncementID, String Status,
                               String ApplierID, String Comment, String ApplicationID, List<String> Answers) {
        this.AnnouncementID = AnnouncementID;
        this.Status = Status;
        this.ApplierID = ApplierID;
        this.Comment = Comment;
        this.ApplicationID = ApplicationID;
        this.Answers = Answers;
    }

    public JobApplicationClass(Parcel parcel){
        this.AnnouncementID = parcel.readString();
        this.Status = parcel.readString();
        this.ApplierID = parcel.readString();
        this.Comment = parcel.readString();
        this.ApplicationID = parcel.readString();
        this.Answers = new ArrayList<>();
        parcel.readStringList(this.Answers);
    }

    public static final Creator<JobApplicationClass> CREATOR = new Creator<JobApplicationClass>() {
        @Override
        public JobApplicationClass createFromParcel(Parcel in) {
            return new JobApplicationClass(in);
        }

        @Override
        public JobApplicationClass[] newArray(int size) {
            return new JobApplicationClass[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(AnnouncementID);
        dest.writeString(Status);
        dest.writeString(ApplierID);
        dest.writeString(Comment);
        dest.writeString(ApplicationID);
        dest.writeList(Answers);
    }
}
