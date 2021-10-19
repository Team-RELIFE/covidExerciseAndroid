package com.example.exercise_android1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoticeTitleAdapter extends RecyclerView.Adapter<NoticeTitleAdapter.Holder> {
    ArrayList<NoticeTitleItem> arrayList=new ArrayList<>();
    Context context;

    public NoticeTitleAdapter(Context context){
        this.context=context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.notice_title,parent,false);
        return new NoticeTitleAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.title.setText(arrayList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void addItem(NoticeTitleItem item){
        arrayList.add(item);
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView title;
        public Holder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.notice_title);
        }

    }

}
