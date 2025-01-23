package com.piece.memo.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memo.R;
import com.piece.memo.database.Folder;
import com.piece.memo.database.NodeBase;

import org.jetbrains.annotations.NotNull;

class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    protected Folder folder;

    public ListAdapter(@NotNull Folder folder) {
        this.folder = folder;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NodeBase node = folder.getChildren().get(position);
        holder.setTitle(node.getText());
    }

    @Override
    public int getItemCount() {
        return folder.getChildren().size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView title, text;

        public String getTitle() {
            return title.getText().toString();
        }

        public void setTitle(String title) {
            this.title.setText(title);
        }

        public String getText() {
            return text.getText().toString();
        }

        public void setText(String text) {
            this.text.setText(text);
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            text = itemView.findViewById(R.id.text);
        }
    }
}
