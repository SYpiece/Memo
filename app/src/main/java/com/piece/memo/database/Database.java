package com.piece.memo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Database {
    static final String
            fileTable = "FILE_TABLE",
            textTable = "TEXT_TABLE",
            idKey = "ID_KEY",
            textKey = "TEXT_KEY",
            typeKey = "TYPE_KEY",
            belongKey = "BELONG_KEY";

    protected final SQLiteDatabase sqLiteDatabase;
    protected final Folder root = new Folder(0, this, "root");

    public Folder getRoot() {
        return root;
    }

    public Database(Context context) {
        sqLiteDatabase = context.openOrCreateDatabase("database.db", Context.MODE_PRIVATE, null);
        createDatabase();
    }

    private void createDatabase() {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + fileTable + ";");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + textTable + ";");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + fileTable + "(" +
                idKey + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                textKey + " TEXT NOT NULL, " +
                typeKey + " INTEGER NOT NULL, " +
                belongKey + " INTEGER NOT NULL" +
                ");");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + textTable + "(" +
                "ID INTEGER PRIMARY KEY, " +
                "FILE TEXT NOT NULL" +
                ");");
    }

    /**
     * 查询指定节点的所有子节点。
     *
     * @param node 父节点
     * @return 子节点列表
     */
    protected List<NodeBase> queryChildren(NodeBase node) {
        List<NodeBase> nodes = new ArrayList<>();
        try (Cursor cursor = sqLiteDatabase.query(fileTable, null, belongKey + " = ?", new String[] { String.valueOf(node.getID()) }, null, null, null)) {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    nodes.add(new NodePack(
                            cursor.getLong(cursor.getColumnIndexOrThrow(idKey)),
                            cursor.getString(cursor.getColumnIndexOrThrow(textKey)),
                            NodeBase.Type.fromInteger(cursor.getType(cursor.getColumnIndexOrThrow(typeKey))),
                            cursor.getLong(cursor.getColumnIndexOrThrow(belongKey))
                    ).unpack(node));
                }
            }
        }
        return Collections.unmodifiableList(nodes);
    }

    /**
     * 插入一个新的节点到数据库中。
     *
     * @param node 要插入的节点
     */
    protected void insertNode(@NonNull NodeBase node) {
        ContentValues values = new ContentValues();
        values.put(textKey, node.getText());
        values.put(typeKey, NodeBase.Type.toInteger(node.getType()));
        values.put(belongKey, node.getParent().getID());
        long id = sqLiteDatabase.insert(fileTable, null, values);
        if (id == -1) {
            throw new RuntimeException();
        }
        node.setID(id);
    }

    /**
     * 更新数据库中的节点信息。
     *
     * @param node 要更新的节点
     */
    protected void updateNode(@NonNull NodeBase node) {
        ContentValues values = new ContentValues();
        values.put(textKey, node.getText());
        sqLiteDatabase.update(fileTable, values, idKey + " = ?", new String[] { String.valueOf(node.getID()) });
    }

    /**
     * 将节点移动到新的父节点下。
     *
     * @param node   要移动的节点
     * @param parent 新的父节点
     */
    protected void moveNode(@NonNull NodeBase node, @NonNull NodeBase parent) {
        ContentValues values = new ContentValues();
        values.put(belongKey, parent.getID());
        sqLiteDatabase.update(fileTable, values, idKey + " = ?", new String[] { String.valueOf(node.getID()) });
    }

    /**
     * 复制节点到新的父节点下。
     *
     * @param node   要复制的节点
     * @param parent 新的父节点
     */
    protected void copyNode(@NonNull NodeBase node, @NonNull NodeBase parent) {
        ContentValues values = new ContentValues();
        values.put(textKey, node.getText());
        values.put(typeKey, NodeBase.Type.toInteger(node.getType()));
        values.put(belongKey, parent.getID());
        sqLiteDatabase.insert(fileTable, null, values);
    }

    /**
     * 从数据库中删除指定的节点。
     *
     * @param node 要删除的节点
     */
    protected void deleteNode(@NonNull NodeBase node) {
        sqLiteDatabase.delete(fileTable, idKey + " = ?", new String[] { String.valueOf(node.getID()) });
    }

    private static class NodePack {
        protected final long id, belong;
        protected final String name;
        protected final NodeBase.Type type;

        public NodePack(long id, @NonNull String name, @NonNull NodeBase.Type type, long belong) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.belong = belong;
        }

        public NodeBase unpack(@NonNull NodeBase parent) {
            if (parent.getID() != belong) {
                throw new RuntimeException();
            }
            NodeBase node;
            switch (type) {
                case Folder: {
                    node = new Folder(id, parent, name);
                    break;
                }
                case Text: {
                    node = new Text(id, parent, name);
                    break;
                }
                case Paragraph: {
                    node = new Paragraph(id, parent, name);
                    break;
                }
                default: {
                    throw new RuntimeException();
                }
            }
            return node;
        }
    }
}