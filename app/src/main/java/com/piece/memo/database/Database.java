package com.piece.memo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Database {
    protected static Database instance;

    public static void createDatabase(@NonNull Context context) {
        if (instance != null) {
            throw new IllegalStateException();
        }
        instance = new Database(context);
    }

    @NonNull
    public static Database getInstance() {
        return instance;
    }

    static final String
            fileTable = "FILE_TABLE",
//            textTable = "TEXT_TABLE",
            idKey = "ID_KEY",
            textKey = "TEXT_KEY",
            nameKey = "NAME_KEY",
            descriptionKey = "DESCRIPTION_KEY",
            typeKey = "TYPE_KEY",
            belongKey = "BELONG_KEY";

    protected final SQLiteDatabase sqLiteDatabase;
    protected final Folder root = new FolderBase(this, 0, 0, "", "", "");

    @NonNull
    public Folder getRoot() {
        return root;
    }

    protected Database(@NonNull Context context) {
        sqLiteDatabase = context.openOrCreateDatabase("database.db", Context.MODE_PRIVATE, null);
        createDatabase();
    }

    private void createDatabase() {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + fileTable + ";");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + fileTable + "(" +
                idKey + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                textKey + " TEXT NOT NULL, " +
                nameKey + " TEXT NOT NULL, " +
                descriptionKey + " TEXT NOT NULL, " +
                typeKey + " INTEGER NOT NULL, " +
                belongKey + " INTEGER NOT NULL" +
                ");");
    }

    @Nullable
    public Node getNode(int id) {
        if (id == 0) {
            return root;
        }
        try (Cursor cursor = sqLiteDatabase.query(fileTable, null, idKey + " = ?", new String[] { String.valueOf(id) }, null, null, null)) {
            if (cursor != null && cursor.moveToNext()) {
                return new NodePack(
                        cursor.getInt(cursor.getColumnIndexOrThrow(idKey)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(belongKey)),
                        Node.Type.fromInteger(cursor.getInt(cursor.getColumnIndexOrThrow(typeKey))),
                        cursor.getString(cursor.getColumnIndexOrThrow(textKey)),
                        cursor.getString(cursor.getColumnIndexOrThrow(nameKey)),
                        cursor.getString(cursor.getColumnIndexOrThrow(descriptionKey))
                ).unpack();
            }
        }
        return null;
    }

    /**
     * 查询指定节点的所有子节点。
     *
     * @param node 父节点
     * @return 子节点列表
     */
    protected List<Node> queryChildren(Container node) {
        List<Node> nodes = new ArrayList<>();
        try (Cursor cursor = sqLiteDatabase.query(fileTable, null, belongKey + " = ?", new String[] { String.valueOf(node.getID()) }, null, null, null)) {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    nodes.add(new NodePack(
                            cursor.getInt(cursor.getColumnIndexOrThrow(idKey)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(belongKey)),
                            Node.Type.fromInteger(cursor.getInt(cursor.getColumnIndexOrThrow(typeKey))),
                            cursor.getString(cursor.getColumnIndexOrThrow(textKey)),
                            cursor.getString(cursor.getColumnIndexOrThrow(nameKey)),
                            cursor.getString(cursor.getColumnIndexOrThrow(descriptionKey))
                    ).unpack());
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
    int insertNode(@NonNull NodeBase node) {
        ContentValues values = new ContentValues();
        values.put(textKey, node.getText());
        values.put(nameKey, node.getName());
        values.put(descriptionKey, node.getDescription());
        values.put(typeKey, Node.Type.toInteger(node.getType()));
        values.put(belongKey, node.getBelong());
        int id = (int) sqLiteDatabase.insert(fileTable, null, values);
        if (id == -1) {
            throw new RuntimeException();
        }
        return id;
    }

    /**
     * 更新数据库中的节点信息。
     *
     * @param node 要更新的节点
     */
    void updateNode(@NonNull NodeBase node) {
        ContentValues values = new ContentValues();
        values.put(textKey, node.getText());
        values.put(nameKey, node.getName());
        values.put(descriptionKey, node.getDescription());
        sqLiteDatabase.update(fileTable, values, idKey + " = ?", new String[] { String.valueOf(node.getID()) });
    }

    /**
     * 将节点移动到新的父节点下。
     *
     * @param node   要移动的节点
     * @param parent 新的父节点
     */
    void moveNode(@NonNull NodeBase node, @NonNull Container parent) {
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
    void copyNode(@NonNull NodeBase node, @NonNull Container parent) {
        ContentValues values = new ContentValues();
        values.put(textKey, node.getText());
        values.put(nameKey, node.getName());
        values.put(descriptionKey, node.getDescription());
        values.put(typeKey, NodeBase.Type.toInteger(node.getType()));
        values.put(belongKey, parent.getID());
        sqLiteDatabase.insert(fileTable, null, values);
    }

    /**
     * 从数据库中删除指定的节点。
     *
     * @param node 要删除的节点
     */
    void deleteNode(@NonNull NodeBase node) {
        sqLiteDatabase.delete(fileTable, idKey + " = ?", new String[] { String.valueOf(node.getID()) });
    }

    private class NodePack {
        protected final int id, belong;
        protected final String text, title, description;
        protected final Node.Type type;

        public NodePack(int id, int belong, @NonNull Node.Type type, @NonNull String text, @NonNull String title, @NonNull String description) {
            this.id = id;
            this.belong = belong;
            this.type = type;
            this.text = text;
            this.title = title;
            this.description = description;
        }

        public Node unpack() {
            Node node;
            switch (type) {
                case Folder: {
                    node = new FolderBase(Database.this, id, belong, text, title, description);
                    break;
                }
                case Text: {
                    node = new TextBase(Database.this, id, belong, text, title, description);
                    break;
                }
                case Paragraph: {
                    node = new ParagraphBase(Database.this, id, belong, text, title, description);
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