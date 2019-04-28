package com.example.avatar;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;

public class AppliedPeopleListAdapter extends ArrayAdapter<String> {

    private int resourceLayout;
    private Context mContext;
    public static List<JobApplicationClass> Extras;

    public AppliedPeopleListAdapter(Context context, int resource, List<String> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        String p = getItem(position);

        if (p != null) {
            TextView tt1 = v.findViewById(R.id.list_item_applier_name);

            if (tt1 != null) {
                tt1.setText(p);
            }

            Button btn = v.findViewById(R.id.list_item_applier_btn);
            if(btn != null){
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, EmployerJobSeekerView.class);
                        intent.putExtra("CurrentJob", Extras.get(position));
                        mContext.startActivity(intent);
                    }
                });
            }
        }
        return v;
    }
}