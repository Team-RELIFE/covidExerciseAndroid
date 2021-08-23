package com.example.exercise_android1;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PointAdapter extends RecyclerView.Adapter<PointAdapter.Holder> {
    ArrayList<PointItem> arrayList = new ArrayList<>();
    Context context;

    public PointAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.pointlist,parent,false);
        return new PointAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.onBind(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void addItem(PointItem item){
        arrayList.add(item);
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView textView1;
        TextView textView2;

        public Holder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.add_text);
            textView2 = itemView.findViewById(R.id.add_secondary_text);
        }

        void onBind(PointItem item){
            textView1.setText(item.getPoint_title());
            textView2.setText(item.getPoint_content());
        }
    }
}
