package com.example.exercise_android1.trainer_list;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import com.example.exercise_android1.R;


public class TrainerListAdapter extends BaseAdapter {
    private Context context;
    private List<Trainer> trainerList;

    public TrainerListAdapter(Context context, List<Trainer> trainerList){
        this.context = context;
        this.trainerList = trainerList;
    }

    @Override
    public int getCount() {
        return trainerList.size();
    }

    @Override
    public Object getItem(int i) {
        return trainerList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.trainer_item, null);

        TextView userName = (TextView)v.findViewById(R.id.userName);
        TextView userAge = (TextView)v.findViewById(R.id.userAge);

        userName.setText(trainerList.get(i).getUserName());
        userAge.setText(trainerList.get(i).getUserAge());

        return v;
    }
}