package com.example.laboratorio1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.laboratorio1.Models.Task;

import java.util.ArrayList;

public class TaskAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Task> list;


    public TaskAdapter(Context context, ArrayList<Task> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int i) {
        return this.list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return this.list.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.lyt_task_adapter, null);
        }
        TextView tvTaskName = view.findViewById(R.id.tvTaskName);
        TextView tvDescription = view.findViewById(R.id.tvDescription);
        TextView tvLimitDate = view.findViewById(R.id.tvLimitDate);
        TextView tvCategory = view.findViewById(R.id.tvCategory);
        Task task = this.list.get(i);
        tvTaskName.setText(task.getName());
        tvDescription.setText(task.getDescription());
        tvLimitDate.setText(task.getFormattedLimitDate());

        tvCategory.setText(task.getCategory().getName());
        return view;
    }
}
