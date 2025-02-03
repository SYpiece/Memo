package com.piece.memo.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.piece.memo.database.Node;
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
        titleView.setText(database.getRoot().getName());

        final ListAdapter listAdapter = new ListAdapter(database.getRoot());
        recyclerView.setAdapter(listAdapter);
        listAdapter.setOnFolderVisitedListener(folder -> titleView.setText(folder.getName()));
        listAdapter.setOnItemClickedListener(node -> {
            if (node instanceof Folder) {
                listAdapter.visitFolder((Folder) node);
            } else if (node instanceof Text) {
                Intent intent = new Intent(ListActivity.this, TextActivity.class);
                intent.putExtra("ID", node.getID());
                startActivity(intent);
            }
        });
        @SuppressLint("NotifyDataSetChanged") final ActivityResultLauncher<Intent>  activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> listAdapter.notifyDataSetChanged()
        );
        listAdapter.setOnItemLongClickedListener(node -> {
            Intent intent = new Intent(ListActivity.this, MoreActivity.class);
            intent.putExtra("ID", node.getID());
            activityResultLauncher.launch(intent);
        });

        final ImageButton returnButton = findViewById(R.id.button_return), addButton = findViewById(R.id.button_add);
        addButton.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
            builder.setTitle("添加文件");
            String description = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            builder.setItems(new String[]{"文件夹", "笔记"}, (dialog, which) -> {
                switch (which) {
                    case 0: {
                        Folder.from(listAdapter.getVisitingFolder(), "新的文件夹", description).create();
                        break;
                    }
                    case 1: {
                        Text.from(listAdapter.getVisitingFolder(), "新的笔记", description).create();
                        break;
                    }
                }
                listAdapter.notifyItemInserted(listAdapter.getItemCount());
            });
            builder.show();
        });
        returnButton.setOnClickListener(view -> listAdapter.leaveFolder());
    }
}

