package com.example.memo.database;

import androidx.databinding.ObservableField;

public abstract class Node {
    protected int id;
    protected final ObservableField<String> name;
    protected final Type type;
    protected Node parent;
    public int getID() {
        return id;
    }
    protected void setID(int id) {
        this.id = id;
    }
    public String getName() {
        return name.get();
    }
    public void setName(String name) {
        this.name.set(name);
    }
    public Type getType() {
        return type;
    }
    public Node getParent() {
        return parent;
    }
    protected void setParent(Node parent) {
        this.parent = parent;
    }
    protected Node(Type type) {
        this(-1, "", type);
    }
    protected Node(String name, Type type) {
        this(-1, name, type);
    }
    protected Node(int id, String name, Type type) {
        this.id = id;
        this.name = new ObservableField<>(name);
        this.type = type;
    }
    public enum Type {
        Unknown, Folder, Text;
        public static int toInteger(Type type) {
            switch (type) {
                case Folder: return 1;
                case Text: return 2;
                default: return 0;
            }
        }
        public static Type fromInteger(int i) {
            switch (i) {
                case 1: return Folder;
                case 2: return Text;
                default: return Unknown;
            }
        }
    }
}
