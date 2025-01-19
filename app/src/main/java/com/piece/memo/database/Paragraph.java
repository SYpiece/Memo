package com.piece.memo.database;

public class Paragraph extends Node {
    public Paragraph(Text text) {
        super(text, "", Node.Type.Paragraph);
    }

    public Paragraph(Text text, String t) {
        super(text, t, Node.Type.Paragraph);
    }

    protected Paragraph(long id, Node parent, String text) {
        super(id, parent, text, Node.Type.Paragraph);
    }

    @Override
    public void copyTo(Node parent) {
        if (!(parent instanceof Text)) {
            throw new RuntimeException();
        }
        super.copyTo(parent);
    }

    @Override
    public void moveTo(Node parent) {
        if (!(parent instanceof Text)) {
            throw new RuntimeException();
        }
        super.moveTo(parent);
    }
}
