package com.piece.memo.database;

import androidx.annotation.NonNull;

/**
 * Paragraph接口定义了段落的基本操作和属性。
 */
public interface Paragraph extends Editable, Node {
    /**
     * 根据给定的父文本和文本内容创建一个新的段落。
     *
     * @param parent 父文本对象
     * @param text   段落的文本内容
     * @return 新创建的段落对象
     */
    @NonNull
    static Paragraph from(@NonNull Text parent, @NonNull String text) {
        return new ParagraphBase(parent, text);
    }
}

class ParagraphBase extends NodeBase implements Paragraph {
    ParagraphBase(@NonNull Text parent, @NonNull String text) {
        super(parent, Type.Paragraph, text, "", "");
    }

    ParagraphBase(@NonNull Database database, int id, int belong, @NonNull String text, @NonNull String title, @NonNull String description) {
        super(database, id, belong, Type.Paragraph, text, title, description);
    }

    @Override
    public void moveTo(@NonNull Container parent) {
        if (!(parent instanceof Text)) {
            throw new RuntimeException("目标父容器必须是Text类型");
        }
        super.moveTo(parent);
    }

    @Override
    public void copyTo(@NonNull Container parent) {
        if (!(parent instanceof Text)) {
            throw new RuntimeException("目标父容器必须是Text类型");
        }
        super.copyTo(parent);
    }
}