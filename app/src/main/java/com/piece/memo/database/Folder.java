package com.piece.memo.database;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Folder接口定义了文件夹的基本操作和属性。
 */
public interface Folder extends Container, Nameable, Describable, Node {
    /**
     * 获取当前文件夹下的所有子文件夹。
     *
     * @return 不可修改的子文件夹列表
     */
    @NonNull
    default List<Folder> getFolders() {
        List<Node> nodes = getChildren();
        List<Folder> folders = new ArrayList<>();
        for (Node node : nodes) {
            if (node instanceof Folder) {
                folders.add((Folder) node);
            }
        }
        return Collections.unmodifiableList(folders);
    }

    /**
     * 获取当前文件夹下的所有文本节点。
     *
     * @return 不可修改的文本节点列表
     */
    @NonNull
    default List<Text> getTexts() {
        List<Node> nodes = getChildren();
        List<Text> texts = new ArrayList<>();
        for (Node node : nodes) {
            if (node instanceof Text) {
                texts.add((Text) node);
            }
        }
        return Collections.unmodifiableList(texts);
    }

    /**
     * 根据给定的父文件夹、标题和描述创建一个新的文件夹。
     *
     * @param parent      父文件夹对象
     * @param title       文件夹的标题
     * @param description 文件夹的描述
     * @return 新创建的文件夹对象
     */
    @NonNull
    static Folder from(@NonNull Folder parent, @NonNull String title, @NonNull String description) {
        return new FolderBase(parent, title, description);
    }
}

class FolderBase extends NodeBase implements Folder {
    FolderBase(@NonNull Folder parent, @NonNull String title, @NonNull String description) {
        super(parent, Node.Type.Folder, "", title, description);
    }

    FolderBase(@NonNull Database database, int id, int belong, @NonNull String text, @NonNull String title, @NonNull String description) {
        super(database, id, belong, Type.Folder, text, title, description);
    }

    @Override
    public void moveTo(@NonNull Container parent) {
        if (!(parent instanceof Folder)) {
            throw new RuntimeException("目标父容器必须是Folder类型");
        }
        super.moveTo(parent);
    }

    @Override
    public void copyTo(@NonNull Container parent) {
        if (!(parent instanceof Folder)) {
            throw new RuntimeException("目标父容器必须是Folder类型");
        }
        super.copyTo(parent);
    }
}
