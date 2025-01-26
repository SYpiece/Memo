package com.piece.memo.database;

import androidx.annotation.NonNull;

public interface Description {
    @NonNull String getDescription();

    void setDescription(@NonNull String description);
}
