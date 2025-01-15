package com.example.memo.database;

public class Text extends Node {
    public Text(String name) {
        super(name, Type.Text);
    }
    protected Text(int id, String name) {
        super(id, name, Type.Text);
    }
}
