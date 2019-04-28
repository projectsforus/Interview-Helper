package com.example.avatar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class JobSeekerApplicationsListAdapter extends ArrayAdapter<JobSeekerApplicationsState> {

    private int resourceLayout;
    private Context mContext;
    JobSeekerApplicationsState p;

    public JobSeekerApplicationsListAdapter(Context context, int resource, List<JobSeekerApplicationsState> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        p = getItem(position);

        if (p != null) {
            TextView tt1 = v.findViewById(R.id.job_seeker_application_list_item_job_name);

            if (tt1 != null) {
                tt1.setText(p.JobName);
            }

            ImageButton ib1 = v.findViewById(R.id.job_seeker_application_list_item_job_state);

            if (ib1 != null) {
                if (p.ImageID.equals("Y"))
                    ib1.setBackgroundResource( R.drawable.yes_icon);
                else if(p.ImageID.equals("N"))
                    ib1.setBackgroundResource( R.drawable.no_icon);
                /*else
                    ib1.setBackgroundResource( R.drawable.minus_icon);*/
            }
            TextView JobName = v.findViewById(R.id.job_seeker_application_list_item_job_name);
            JobName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!(getItem(position).ImageID.isEmpty()))
                        Toast.makeText(mContext, "Reason Is: " + (getItem(position).Reason),Toast.LENGTH_LONG).show();
                }
            });
        }

        return v;
    }
}
