package com.application.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.R;
import com.application.adapter.TodoAdapter;
import com.application.listeners.OnTodoCompletedListener;
import com.application.model.Todo;
import com.application.viewmodel.TodoViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ViewTodosActivity extends AppCompatActivity {

    public static final String TAG = "TODOS";
    public static final int REQUEST_CODE = 1;
    private TextView viewCategoryTitle, viewCategoryTasks, noTodosContent, noTodosTitle;
    private ImageView viewCategoryImage, backArrow;

    private TodoViewModel todoViewModel;
    private RecyclerView todosRecyclerView;
    private TodoAdapter todoAdapter;

    private FloatingActionButton addTodoButton;

    private OnTodoCompletedListener mOnTodoCompletedListener = new OnTodoCompletedListener() {
        @Override
        public void todoCompleted(Todo todo) {
            setTodoCompleted(todo);
        }
    };

    public void setTodoCompleted(final Todo todo) {
        //remove the todo from the view
        todoViewModel.deleteTodo(todo);
        //add the snackbar and the undo button
        Snackbar.make(findViewById(R.id.viewTodosContainer), "Todo Completed.", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        todoViewModel.addTodo(todo);
                    }
                }).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_todos);

        setContentView(R.layout.activity_view_todos);

        todoViewModel = new ViewModelProvider(this).get(TodoViewModel.class);
        Log.d("TODOS", "onCreate: " + todoViewModel.hashCode());

        viewCategoryTitle = findViewById(R.id.viewCategoryTitle);
        viewCategoryTasks = findViewById(R.id.viewCategoryTasks);
        viewCategoryImage = findViewById(R.id.viewCategoryImage);
        backArrow = findViewById(R.id.backArrow);
        noTodosTitle = findViewById(R.id.noTodosTitle);
        noTodosContent = findViewById(R.id.noTodosContent);
        todosRecyclerView = findViewById(R.id.todosList);
        addTodoButton = findViewById(R.id.addTodoFab);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addTodoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addTodoIntent = new Intent(ViewTodosActivity.this, AddTodoActivity.class);
                addTodoIntent.putExtra("category", viewCategoryTitle.getText().toString().toLowerCase());

                startActivityForResult(addTodoIntent, REQUEST_CODE);
            }
        });

        Bundle extras = getIntent().getExtras();

        String title = extras.getString("title");
        int image = extras.getInt("image");

        if (title.toLowerCase().equals("all")) {
            todoViewModel.getTodos().observe(this, new Observer<List<Todo>>() {
                @Override
                public void onChanged(List<Todo> todos) {
                    if (todos.isEmpty()) {
                        noTodosTitle.setVisibility(View.VISIBLE);
                        noTodosContent.setVisibility(View.VISIBLE);
                        todosRecyclerView.setVisibility(View.INVISIBLE);

                        viewCategoryTasks.setText("0 tasks");
                    } else {
                        noTodosTitle.setVisibility(View.INVISIBLE);
                        noTodosContent.setVisibility(View.INVISIBLE);
                        todosRecyclerView.setVisibility(View.VISIBLE);

                        todoAdapter = new TodoAdapter(mOnTodoCompletedListener);
                        todosRecyclerView.setHasFixedSize(true);
                        todosRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                        todosRecyclerView.setAdapter(todoAdapter);

                        todoAdapter.setTodos(todos);
                        viewCategoryTasks.setText(todos.size() + " Task" + (todos.size() == 1 ? "" : "s"));
                    }
                }
            });
        } else {
            todoViewModel.getTodosByCategory(title.toLowerCase()).observe(this, new Observer<List<Todo>>() {
                @Override
                public void onChanged(List<Todo> todos) {
                    if (todos.isEmpty()) {
                        noTodosTitle.setVisibility(View.VISIBLE);
                        noTodosContent.setVisibility(View.VISIBLE);
                        todosRecyclerView.setVisibility(View.INVISIBLE);

                        viewCategoryTasks.setText("0 tasks");
                    } else {
                        noTodosTitle.setVisibility(View.INVISIBLE);
                        noTodosContent.setVisibility(View.INVISIBLE);
                        todosRecyclerView.setVisibility(View.VISIBLE);

                        todoAdapter = new TodoAdapter(mOnTodoCompletedListener);
                        todosRecyclerView.setHasFixedSize(true);
                        todosRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                        todosRecyclerView.setAdapter(todoAdapter);

                        todoAdapter.setTodos(todos);
                        viewCategoryTasks.setText(todos.size() + " Task" + (todos.size() == 1 ? "" : "s"));
                    }
                }
            });
        }

        viewCategoryTitle.setText(title);
        viewCategoryImage.setImageResource(image);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
                Todo addedTodo = (Todo) data.getSerializableExtra("addedTodo");
                todoViewModel.addTodo(addedTodo);
            }
        }
    }
}