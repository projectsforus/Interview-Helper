package com.example.avatar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class JobSeekerAnnouncementDescription extends AppCompatActivity {

    TextView JobName, JobDept, JobDesc;
    Button Apply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_seeker_announcement_description);

        JobName = findViewById(R.id.job_seeker_announcement_description_job_name);
        JobDept = findViewById(R.id.job_seeker_announcement_description__job_department);
        JobDesc = findViewById(R.id.job_seeker_announcement_description__job_description);
        Apply = findViewById(R.id.job_seeker_announcement_description_btn_apply);


        final JobAnnouncementClass Job = getIntent().getParcelableExtra("CurrentJob");
        JobName.setText(Job.JobName);
        JobDept.setText(Job.JobDepartment);
        JobDesc.setText(Job.JobDescription);

        Apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JobSeekerAnnouncementDescription.this, JobSeekerApplicationForm.class);
                intent.putExtra("EmployerUID",Job.UID);
                intent.putExtra("AnnID",Job.ID);
                finish();
                startActivity(intent);
            }
        });
    }
}
