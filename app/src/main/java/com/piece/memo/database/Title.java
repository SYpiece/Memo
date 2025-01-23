package com.piece.memo.database;

import org.jetbrains.annotations.NotNull;

public interface Title {
    @NotNull
    String getTitle();
    void setTitle(@NotNull String title);
}
