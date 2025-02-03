package com.piece.memo.database;

import androidx.annotation.NonNull;

/**
 * Nameable接口定义了可以命名的对象的基本操作。
 */
public interface Nameable {
    /**
     * 获取对象的名称。
     *
     * @return 对象的名称
     */
    @NonNull
    String getName();

    /**
     * 设置对象的名称。
     *
     * @param name 新的名称
     */
    void setName(@NonNull String name);
}
