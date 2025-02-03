package com.piece.memo.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.piece.memo.R;
import com.piece.memo.database.Database;
import com.piece.memo.database.Describable;
import com.piece.memo.database.Nameable;
import com.piece.memo.database.Node;

import java.util.Objects;

public class MoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_more);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_more), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        int id = Objects.requireNonNull(getIntent().getExtras()).getInt("ID", -1);
        if (id == -1) {
            finish();
            return;
        }
        Node node = Database.getInstance().getNode(id);
        if (node == null || (node.getType() != Node.Type.Folder && node.getType() != Node.Type.Text)) {
            finish();
            return;
        }

        final TextView titleView = findViewById(R.id.more_text_title);
        if (node instanceof Nameable) {
            titleView.setText(((Nameable) node).getName());
        }

        final EditText nameText = findViewById(R.id.more_text_name), descriptionText = findViewById(R.id.more_text_description);
        if (node instanceof Nameable) {
            nameText.setText(((Nameable) node).getName());
        } else {
            nameText.setEnabled(false);
        }
        nameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                titleView.setText(s);
                if (node instanceof Nameable) {
                    ((Nameable) node).setName(s.toString());
                    node.update();
                }
            }
        });
        if (node instanceof Describable) {
            descriptionText.setText(((Describable) node).getDescription());
        } else {
            descriptionText.setEnabled(false);
        }
        descriptionText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (node instanceof Describable) {
                    ((Describable) node).setDescription(s.toString());
                    node.update();
                }
            }
        });

        final Button removeButton = findViewById(R.id.more_button_remove);
        removeButton.setOnClickListener(view -> {
            node.delete();
            finish();
        });
    }
}