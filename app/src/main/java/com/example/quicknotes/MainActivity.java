package com.example.quicknotes;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private Button addButton;
    private ListView listView;
    private ArrayList<String> todoList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        addButton = findViewById(R.id.addButton);
        listView = findViewById(R.id.listView);

        todoList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, R.layout.todo_item, R.id.taskText, todoList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.todo_item, parent, false);
                }

                TextView taskText = convertView.findViewById(R.id.taskText);
                Button updateButton = convertView.findViewById(R.id.updateButton);
                Button deleteButton = convertView.findViewById(R.id.deleteButton);

                taskText.setText(getItem(position));

                // Update Task
                updateButton.setOnClickListener(v -> showUpdateDialog(position));

                // Delete Task
                deleteButton.setOnClickListener(v -> {
                    todoList.remove(position);
                    notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Task Deleted!", Toast.LENGTH_SHORT).show();
                });

                return convertView;
            }
        };

        listView.setAdapter(adapter);

        // Add Task
        addButton.setOnClickListener(v -> {
            String task = editText.getText().toString().trim();
            if (!task.isEmpty()) {
                todoList.add(task);
                adapter.notifyDataSetChanged();
                editText.setText("");
                Toast.makeText(MainActivity.this, "Task Added!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Enter a task!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showUpdateDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Task");

        final EditText input = new EditText(this);
        input.setText(todoList.get(position));
        builder.setView(input);

        builder.setPositiveButton("Update", (dialog, which) -> {
            String updatedTask = input.getText().toString().trim();
            if (!updatedTask.isEmpty()) {
                todoList.set(position, updatedTask);
                adapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Task Updated!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Task cannot be empty!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}
