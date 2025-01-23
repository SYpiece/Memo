package com.piece.memo.database;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

public interface Node {
    long getID();

    @NotNull
    Node getParent();

    @NotNull
    Database getDatabase();

    void create();

    void update();

    void moveTo(@NonNull Node parent);

    void copyTo(@NonNull Node parent);

    void delete();

    enum Type {
        Unknown, Folder, Text, Paragraph;

        public static int toInteger(@NonNull Type type) {
            switch (type) {
                case Folder: return 1;
                case Text: return 2;
                case Paragraph: return 3;
                default: return 0;
            }
        }

        @NonNull
        public static Type fromInteger(int i) {
            switch (i) {
                case 1: return Folder;
                case 2: return Text;
                case 3: return Paragraph;
                default: return Unknown;
            }
        }
    }
}
