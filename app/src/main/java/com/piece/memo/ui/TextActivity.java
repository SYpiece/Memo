package com.piece.memo.ui;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.piece.memo.R;
import com.piece.memo.database.Database;
import com.piece.memo.database.Node;
import com.piece.memo.database.Paragraph;
import com.piece.memo.database.Text;

import java.util.Objects;

public class TextActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_text);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_text), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final RecyclerView paragraphList = findViewById(R.id.list_paragraph);
        paragraphList.setLayoutManager(new LinearLayoutManager(this));
        paragraphList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        long id = Objects.requireNonNull(getIntent().getExtras()).getInt("ID", -1);
        if (id == -1) {
            finish();
            return;
        }
        Node node = Database.getInstance().getNode(id);
        if (node == null || node.getType() != Node.Type.Text) {
            finish();
            return;
        }
        TextAdapter textAdapter = new TextAdapter((Text) node);
        paragraphList.setAdapter(textAdapter);

        ImageButton addButton = findViewById(R.id.text_button_add), removeButton = findViewById(R.id.text_button_remove);
        addButton.setOnClickListener(v -> {
            new Paragraph(textAdapter.getText()).create();
            textAdapter.notifyItemInserted(textAdapter.getItemCount());
        });
        removeButton.setOnClickListener(v -> {

        });
    }
}