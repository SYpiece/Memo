package com.piece.memo.database;

import org.jetbrains.annotations.NotNull;

public interface Description {
    @NotNull
    String getDescription();
    void setDescription(@NotNull String description);
}
