package com.piece.memo.database;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Text extends NodeBase {
    public Text(@NonNull Folder folder, @NonNull String name) {
        super(folder, name, Type.Text);
    }

    protected Text(long id, @NonNull NodeBase parent, @NonNull String name) {
        super(id, parent, name, Type.Text);
    }

    /**
     * 获取当前文本节点下的所有段落节点。
     *
     * @return 段落节点列表
     */
    @NonNull
    public List<Paragraph> getParagraphs() {
        List<NodeBase> children = getChildren();
        List<Paragraph> paragraphs = new ArrayList<>(children.size());
        for (NodeBase node : children) {
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
    public void moveTo(@NonNull NodeBase parent) {
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
    public void copyTo(@NonNull NodeBase parent) {
        if (!(parent instanceof Folder)) {
            throw new RuntimeException();
        }
        super.copyTo(parent);
    }
}
