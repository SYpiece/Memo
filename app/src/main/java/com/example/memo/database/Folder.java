package com.example.memo.database;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import java.util.Collections;
import java.util.List;

public class Folder extends Node {
    protected final ObservableList<Node> children = new ObservableArrayList<>();
    public List<Node> getChildren() {
        return Collections.unmodifiableList(children);
    }
    public void addNode(Node node) {
        if (node.getParent() != null) {
            throw new RuntimeException();
        }
        node.setParent(this);
        children.add(node);
    }
    public void removeNode(Node node) {
        if (node.getParent() != this) {
            throw new RuntimeException();
        }
        node.setParent(null);
        children.remove(node);
    }
    public Folder(String name) {
        super(name, Type.Folder);
    }
    protected Folder(int id, String name) {
        super(id, name, Type.Folder);
    }
}
