package com.piece.memo.database;

import androidx.annotation.NonNull;

import java.util.List;

public interface Container extends Node {
    @NonNull
    default List<Node> getChildren() {
        return getDatabase().queryChildren(this);
    }
}
