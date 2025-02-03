package com.piece.memo.database;

import androidx.annotation.NonNull;

/**
 * Editable接口定义了可以编辑文本的对象的基本操作。
 */
public interface Editable {
    /**
     * 获取对象的文本内容。
     *
     * @return 对象的文本内容
     */
    @NonNull String getText();

    /**
     * 设置对象的文本内容。
     *
     * @param text 新的文本内容
     */
    void setText(@NonNull String text);
}
