package com.piece.memo.database;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Folder extends NodeBase implements Container, Title, Description {

    public Folder(@NonNull Folder parent, @NonNull String title, @NonNull String description) {
        super(parent, title, description, Type.Folder);
    }

    protected Folder(int id, int belong, @NonNull String text, @NonNull String title, @NonNull String description, @NonNull Database database) {
        super(id, belong, text, title, description, Type.Folder, database);
    }

    protected Folder(int id, @NonNull Database database) {
        super(id, database, Type.Folder);
    }

    /**
     * 获取当前文件夹节点下的所有文件夹节点。
     *
     * @return 文件夹节点列表
     */
    @NonNull
    public List<Folder> getFolders() {
        List<Node> children = getChildren();
        List<Folder> folders = new ArrayList<>(children.size());
        for (Node node : children) {
            if (node instanceof Folder) {
                folders.add((Folder) node);
            }
        }
        return Collections.unmodifiableList(folders);
    }

    /**
     * 获取当前文件夹节点下的所有文本节点。
     *
     * @return 文本节点列表
     */
    @NonNull
    public List<Text> getTexts() {
        List<Node> children = getChildren();
        List<Text> texts = new ArrayList<>(children.size());
        for (Node node : children) {
            if (node instanceof Text) {
                texts.add((Text) node);
            }
        }
        return Collections.unmodifiableList(texts);
    }

    /**
     * 将当前文件夹节点移动到新的父节点下。
     *
     * @param parent 新的父节点
     * @throws RuntimeException 如果新的父节点不是文件夹类型
     */
    @Override
    public void moveTo(@NonNull Node parent) {
        if (!(parent instanceof Folder)) {
            throw new RuntimeException();
        }
        super.moveTo(parent);
    }

    /**
     * 将当前文件夹节点复制到新的父节点下。
     *
     * @param parent 新的父节点
     * @throws RuntimeException 如果新的父节点不是文件夹类型
     */
    @Override
    public void copyTo(@NonNull Node parent) {
        if (!(parent instanceof Folder)) {
            throw new RuntimeException();
        }
        super.copyTo(parent);
    }

    @NonNull
    @Override
    public String getTitle() {
        return super.getTitle();
    }

    @Override
    public void setTitle(@NonNull String title) {
        super.setTitle(title);
    }

    @NonNull
    @Override
    public String getDescription() {
        return super.getDescription();
    }

    @Override
    public void setDescription(@NonNull String description) {
        super.setDescription(description);
    }
}
