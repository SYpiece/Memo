package com.piece.memo.database;

import androidx.annotation.NonNull;

public class Paragraph extends NodeBase {
    public Paragraph(@NonNull Text text) {
        super(text, "", NodeBase.Type.Paragraph);
    }

    public Paragraph(@NonNull Text text, @NonNull String t) {
        super(text, t, NodeBase.Type.Paragraph);
    }

    protected Paragraph(long id, @NonNull NodeBase parent, @NonNull String text) {
        super(id, parent, text, NodeBase.Type.Paragraph);
    }

    /**
     * 将当前段落节点复制到新的父节点下。
     *
     * @param parent 新的父节点
     * @throws RuntimeException 如果新的父节点不是文本类型
     */
    @Override
    public void copyTo(@NonNull NodeBase parent) {
        if (!(parent instanceof Text)) {
            throw new RuntimeException();
        }
        super.copyTo(parent);
    }

    /**
     * 将当前段落节点移动到新的父节点下。
     *
     * @param parent 新的父节点
     * @throws RuntimeException 如果新的父节点不是文本类型
     */
    @Override
    public void moveTo(@NonNull NodeBase parent) {
        if (!(parent instanceof Text)) {
            throw new RuntimeException();
        }
        super.moveTo(parent);
    }
}
