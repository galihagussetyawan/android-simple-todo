package com.application.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.R;
import com.application.listeners.OnTodoCompletedListener;
import com.application.model.Todo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {

    private List<Todo> todos;
    private OnTodoCompletedListener mOnTodoCompletedListener;

    public TodoAdapter(OnTodoCompletedListener mOnTodoCompletedListener) {
        this.mOnTodoCompletedListener = mOnTodoCompletedListener;
    }

    @NonNull
    @Override
    public TodoAdapter.TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View todoView = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item, parent, false);
        return new TodoViewHolder(todoView);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoAdapter.TodoViewHolder holder, int position) {
        Todo todo = todos.get(position);
        holder.setData(todo);
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    public void setTodos(List<Todo> todos) {
        this.todos = todos;
        notifyDataSetChanged();
    }

    public class TodoViewHolder extends RecyclerView.ViewHolder {

        TextView title, datetime;
        CheckBox completed;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.todoTitle);
            datetime = itemView.findViewById(R.id.todoDateTime);
            completed = itemView.findViewById(R.id.todoCompleted);
        }

        public void setData(Todo todo) {
            this.title.setText(todo.getTitle());

            String pattern = "hh:mm - MMM dd";

            this.datetime.setText(new SimpleDateFormat(pattern).format(new Date(todo.getCreatedAt())));
            completed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        Todo completedTodo = todos.get(getAdapterPosition());
                        mOnTodoCompletedListener.todoCompleted(completedTodo);
                    }
                }
            });
        }
    }
}
