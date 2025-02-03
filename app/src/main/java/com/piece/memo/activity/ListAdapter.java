package com.piece.memo.activity;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.piece.memo.R;
import com.piece.memo.database.Describable;
import com.piece.memo.database.Folder;
import com.piece.memo.database.Node;
import com.piece.memo.database.Nameable;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    protected OnFolderVisitedListener onFolderVisitedListener;
    protected OnFolderLeftListener onFolderLeftListener;
    protected OnItemClickedListener onItemClickedListener;
    protected OnItemLongClickedListener onItemLongClickedListener;
    protected final LinkedList<Folder> folders = new LinkedList<>();

    public List<Node> getVisitingItems() {
        return folders.getLast().getChildren();
    }

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
    public OnItemClickedListener getItemClickedListener() {
        return onItemClickedListener;
    }

    public void setOnItemClickedListener(@Nullable OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    @Nullable
    public OnItemLongClickedListener getOnItemLongClickedListener() {
        return onItemLongClickedListener;
    }

    public void setOnItemLongClickedListener(@Nullable OnItemLongClickedListener onItemLongClickedListener) {
        this.onItemLongClickedListener = onItemLongClickedListener;
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
        if (onFolderLeftListener != null) {
            onFolderLeftListener.onFolderLeft(folders.getLast());
        }
        folders.add(folder);
        if (onFolderVisitedListener != null) {
            onFolderVisitedListener.onFolderVisited(folder);
        }
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void leaveFolder() {
        if (folders.size() <= 1) {
            return;
        }
        if (onFolderLeftListener != null) {
            onFolderLeftListener.onFolderLeft(folders.getLast());
        }
        folders.removeLast();
        if (onFolderVisitedListener != null) {
            onFolderVisitedListener.onFolderVisited(folders.getLast());
        }
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
        holder.setNode(getVisitingItems().get(position));
    }

    @Override
    public int getItemCount() {
        return getVisitingItems().size();
    }

    public interface OnFolderVisitedListener {
        void onFolderVisited(Folder folder);
    }

    public interface OnFolderLeftListener {
        void onFolderLeft(Folder folder);
    }

    public interface OnItemClickedListener {
        void onItemClicked(Node node);
    }

    public interface OnItemLongClickedListener {
        void onItemLongClicked(Node node);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected Node node;
        protected final TextView title, description;

        public Node getNode() {
            return node;
        }

        public void setNode(Node node) {
            this.node = node;
            if (node instanceof Nameable) {
                title.setText(((Nameable) node).getName());
            }
            if (node instanceof Describable) {
                description.setText(((Describable) node).getDescription());
            }
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(view -> {
                if (onItemClickedListener != null) {
                    onItemClickedListener.onItemClicked(getNode());
                }
            });
            itemView.setOnLongClickListener(view -> {
                if (onItemLongClickedListener != null) {
                    onItemLongClickedListener.onItemLongClicked(getNode());
                }
                return true;
            });
            title = itemView.findViewById(R.id.text_fileTitle);
            description = itemView.findViewById(R.id.text_fileDescription);
        }
    }
}
