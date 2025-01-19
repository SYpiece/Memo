package com.piece.memo.database;

import java.util.List;

public abstract class Node {
    protected long id;
    protected String text;
    protected final Type type;
    protected Node parent;
    protected final Database database;

    public long getID() {
        return id;
    }

    protected void setID(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public Database getDatabase() {
        return database;
    }

    public Node(Node parent, String text, Type type) {
        this(-1, parent, text, type);
    }

    protected Node(long id, Node parent, String text, Type type) {
        this.id = id;
        this.parent = parent;
        this.text = text;
        this.type = type;
        this.database = parent.getDatabase();
    }

    public void create() {
        if (this.id != -1) {
            throw new RuntimeException();
        }
        database.insertNode(this);
    }

    public void update() {
        if (this.id == -1) {
            throw new RuntimeException();
        }
        database.updateNode(this);
    }

    public void moveTo(Node parent) {
        if (this.id == -1) {
            throw new RuntimeException();
        }
        database.moveNode(this, parent);
    }

    public void copyTo(Node parent) {
        if (this.id == -1) {
            throw new RuntimeException();
        }
        database.copyNode(this, parent);
    }

    public void delete() {
        if (this.id == -1) {
            throw new RuntimeException();
        }
        database.deleteNode(this);
    }

    public List<Node> getChildren() {
        return database.queryChildren(this);
    }

    public enum Type {
        Unknown, Folder, Text, Paragraph;

        public static int toInteger(Type type) {
            switch (type) {
                case Folder: return 1;
                case Text: return 2;
                case Paragraph: return 3;
                default: return 0;
            }
        }

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
