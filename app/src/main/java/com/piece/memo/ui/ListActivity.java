package com.piece.memo.ui;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.piece.memo.R;
import com.piece.memo.database.Database;
import com.piece.memo.database.Folder;
import com.piece.memo.database.Text;

import java.util.Date;
import java.util.Locale;

public class ListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_list), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final RecyclerView recyclerView = findViewById(R.id.list_file);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final Database database = Database.getInstance();

        final TextView titleView = findViewById(R.id.text_listTitle);
        titleView.setText(database.getRoot().getTitle());

        final ListAdapter listAdapter = new ListAdapter(database.getRoot());
        recyclerView.setAdapter(listAdapter);
        listAdapter.setOnFolderVisitedListener(folder -> {
            listAdapter.visitFolder(folder);
            titleView.setText(folder.getTitle());
        });
        listAdapter.setOnTextVisitedListener(text -> {
            Intent intent = new Intent(ListActivity.this, TextActivity.class);
            intent.putExtra("ID", text.getID());
            startActivity(intent);
        });

        final ImageButton addButton = findViewById(R.id.button_add), returnButton = findViewById(R.id.button_return);
        addButton.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
            builder.setTitle("添加文件");
            String description = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            builder.setItems(new String[]{"文件夹", "笔记"}, (dialog, which) -> {
                switch (which) {
                    case 0: {
                        new Folder(listAdapter.getVisitingFolder(), "新的文件夹", description).create();
                        break;
                    }
                    case 1: {
                        new Text(listAdapter.getVisitingFolder(), "新的笔记", description).create();
                        break;
                    }
                }
                listAdapter.notifyItemInserted(listAdapter.getItemCount());
            });
            builder.show();
        });
        returnButton.setOnClickListener(view -> {
            listAdapter.leaveFolder();
            titleView.setText(listAdapter.getVisitingFolder().getTitle());
        });
    }
}

