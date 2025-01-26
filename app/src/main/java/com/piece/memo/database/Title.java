package com.piece.memo.database;

import androidx.annotation.NonNull;

public interface Title {
    @NonNull
    String getTitle();

    void setTitle(@NonNull String title);
}
