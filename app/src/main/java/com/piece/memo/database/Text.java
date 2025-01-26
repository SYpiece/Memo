package com.piece.memo.database;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Text extends NodeBase implements Container, Title, Description {
    public Text(@NonNull Folder folder, @NonNull String title, @NonNull String description) {
        super(folder, title, description, Type.Text);
    }

    protected Text(int id, int belong, @NonNull String text, @NonNull String title, @NonNull String description, @NonNull Database database) {
        super(id, belong, text, title, description, Type.Text, database);
    }

    /**
     * 获取当前文本节点下的所有段落节点。
     *
     * @return 段落节点列表
     */
    @NonNull
    public List<Paragraph> getParagraphs() {
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
     * 将当前文本节点移动到新的父节点下。
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
     * 将当前文本节点复制到新的父节点下。
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
