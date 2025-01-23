package com.piece.memo.database;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Container extends Node {
    @NotNull
    List<Node> getChildren();
}
