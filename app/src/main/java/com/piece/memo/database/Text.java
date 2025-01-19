package com.piece.memo.database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Text extends Node {
    public Text(Folder folder, String name) {
        super(folder, name, Type.Text);
    }

    protected Text(long id, Node parent, String name) {
        super(id, parent, name, Type.Text);
    }

    public List<Paragraph> getParagraphs() {
        List<Node> children = getChildren();
        List<Paragraph> paragraphs = new ArrayList<>(children.size());
        for (Node node : children) {
            if (node instanceof Paragraph) {
                paragraphs.add((Paragraph) node);
            }
        }
        return Collections.unmodifiableList(paragraphs);
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
