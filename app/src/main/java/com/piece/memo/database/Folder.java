package com.piece.memo.database;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Folder extends NodeBase {

    public Folder(@NonNull Folder parent, @NonNull String text) {
        super(parent, text, Type.Folder);
    }

    protected Folder(long id, @NonNull NodeBase parent, @NonNull String text) {
        super(id, parent, text, Type.Folder);
    }

    protected Folder(long id, @NonNull Database database, @NonNull String text) {
        super(id, database, text, Type.Folder);
    }

    /**
     * 获取当前文件夹节点下的所有文件夹节点。
     *
     * @return 文件夹节点列表
     */
    @NonNull
    public List<Folder> getFolders() {
        List<NodeBase> children = getChildren();
        List<Folder> folders = new ArrayList<>(children.size());
        for (NodeBase node : children) {
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
        List<NodeBase> children = getChildren();
        List<Text> texts = new ArrayList<>(children.size());
        for (NodeBase node : children) {
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
    public void moveTo(@NonNull NodeBase parent) {
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
    public void copyTo(@NonNull NodeBase parent) {
        if (!(parent instanceof Folder)) {
            throw new RuntimeException();
        }
        super.copyTo(parent);
    }
}
