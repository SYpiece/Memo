package com.piece.memo.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.piece.memo.R;
import com.piece.memo.database.Description;
import com.piece.memo.database.Folder;
import com.piece.memo.database.Node;
import com.piece.memo.database.Text;
import com.piece.memo.database.Title;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    protected OnFolderVisitedListener onFolderVisitedListener;
    protected OnFolderLeftListener onFolderLeftListener;
    protected OnTextVisitedListener onTextVisitedListener;
    protected OnTextLeftListener onTextLeftListener;
    protected final LinkedList<Folder> folders = new LinkedList<>();

    @Nullable
    public OnFolderVisitedListener getOnFolderVisitedListener() {
        return onFolderVisitedListener;
    }

    public void setOnFolderVisitedListener(@Nullable OnFolderVisitedListener onFolderVisitedListener) {
        this.onFolderVisitedListener = onFolderVisitedListener;
    }

    @Nullable
    public OnFolderLeftListener getOnFolderLeftListener() {
        return onFolderLeftListener;
    }

    public void setOnFolderLeftListener(@Nullable OnFolderLeftListener onFolderLeftListener) {
        this.onFolderLeftListener = onFolderLeftListener;
    }

    @Nullable
    public OnTextVisitedListener getOnTextVisitedListener() {
        return onTextVisitedListener;
    }

    public void setOnTextVisitedListener(@Nullable OnTextVisitedListener onTextVisitedListener) {
        this.onTextVisitedListener = onTextVisitedListener;
    }

    @Nullable
    public OnTextLeftListener getOnTextLeftListener() {
        return onTextLeftListener;
    }

    public void setOnTextLeftListener(@Nullable OnTextLeftListener onTextLeftListener) {
        this.onTextLeftListener = onTextLeftListener;
    }

    @NonNull
    public List<Folder> getFolderChain() {
        return Collections.unmodifiableList(folders);
    }

    @NonNull
    public Folder getVisitingFolder() {
        return folders.getLast();
    }

    public ListAdapter(@NonNull Folder folder) {
        folders.add(folder);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void visitFolder(@NonNull Folder folder) {
        folders.add(folder);
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void leaveFolder() {
        if (folders.size() <= 1) {
            return;
        }
        folders.removeLast();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setNode(folders.getLast().getChildren().get(position));
    }

    @Override
    public int getItemCount() {
        return folders.getLast().getChildren().size();
    }

    public interface OnFolderVisitedListener {
        void onFolderVisited(Folder folder);
    }

    public interface OnFolderLeftListener {
        void onFolderLeft(Folder folder);
    }

    public interface OnTextVisitedListener {
        void onTextVisited(Text text);
    }

    public interface OnTextLeftListener {
        void onTextLeft(Text text);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected Node node;
        protected final TextView title, description;

        public Node getNode() {
            return node;
        }

        public void setNode(Node node) {
            this.node = node;
            if (node instanceof Title) {
                title.setText(((Title) node).getTitle());
            }
            if (node instanceof Description) {
                description.setText(((Description) node).getDescription());
            }
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(view -> {
                if (node instanceof Folder) {
                    if (onFolderVisitedListener != null) {
                        onFolderVisitedListener.onFolderVisited((Folder) node);
                    }
                } else if (node instanceof Text) {
                    if (onTextVisitedListener != null) {
                        onTextVisitedListener.onTextVisited((Text) node);
                    }
                }
            });
            title = itemView.findViewById(R.id.text_fileTitle);
            description = itemView.findViewById(R.id.text_fileDescription);
        }
    }
}
