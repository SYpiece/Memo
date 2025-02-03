package com.piece.memo.database;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Text接口定义了文本节点的基本操作和属性。
 */
public interface Text extends Container, Nameable, Describable, Node {
    /**
     * 获取当前文本节点下的所有段落。
     *
     * @return 不可修改的段落列表
     */
    @NonNull
    default List<Paragraph> getParagraphs() {
        List<Node> children = getChildren();
        List<Paragraph> paragraphs = new ArrayList<>(children.size());
        for (Node node : children) {
            if (node instanceof Paragraph) {
                paragraphs.add((Paragraph) node);
            }
        }
        return Collections.unmodifiableList(paragraphs);
    }

    /**
     * 根据给定的父文件夹、标题和描述创建一个新的文本节点。
     *
     * @param parent      父文件夹对象
     * @param title       文本节点的标题
     * @param description 文本节点的描述
     * @return 新创建的文本节点对象
     */
    @NonNull
    static Text from(@NonNull Folder parent, @NonNull String title, @NonNull String description) {
        return new TextBase(parent, title, description);
    }
}

class TextBase extends NodeBase implements Text {
    TextBase(@NonNull Folder folder, @NonNull String title, @NonNull String description) {
        super(folder, Node.Type.Text, "", title, description);
    }

    TextBase(@NonNull Database database, int id, int belong, @NonNull String text, @NonNull String title, @NonNull String description) {
        super(database, id, belong, Type.Text, text, title, description);
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
