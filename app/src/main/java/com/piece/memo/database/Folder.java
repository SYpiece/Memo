package com.piece.memo.database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Folder extends Node {

    public Folder(Folder parent, String text) {
        super(parent, text, Type.Folder);
    }

    protected Folder(long id, Node parent, String text) {
        super(id, parent, text, Type.Folder);
    }

    public List<Folder> getFolders() {
        List<Node> children = getChildren();
        List<Folder> folders = new ArrayList<>(children.size());
        for (Node node : children) {
            if (node instanceof Folder) {
                folders.add((Folder) node);
            }
        }
        return Collections.unmodifiableList(folders);
    }

    public List<Text> getTexts() {
        List<Node> children = getChildren();
        List<Text> texts = new ArrayList<>(children.size());
        for (Node node : children) {
            if (node instanceof Text) {
                texts.add((Text) node);
            }
        }
        return Collections.unmodifiableList(texts);
    }

    @Override
    public void moveTo(Node parent) {
        if (!(parent instanceof Folder)) {
            throw new RuntimeException();
        }
        super.moveTo(parent);
    }

    @Override
    public void copyTo(Node parent) {
        if (!(parent instanceof Folder)) {
            throw new RuntimeException();
        }
        super.copyTo(parent);
    }
}
