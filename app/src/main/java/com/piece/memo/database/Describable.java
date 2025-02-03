package com.piece.memo.database;

import androidx.annotation.NonNull;

/**
 * Describable接口定义了可以描述的对象的基本操作。
 */
public interface Describable {
    /**
     * 获取对象的描述。
     *
     * @return 对象的描述
     */
    @NonNull String getDescription();

    /**
     * 设置对象的描述。
     *
     * @param description 新的描述
     */
    void setDescription(@NonNull String description);
}
