package com.example.memo.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

public class Database {
    protected final DatabaseOpenHelper databaseOpenHelper;
    protected final SQLiteDatabase sqLiteDatabase;
    protected Node root;
    public Database(Context context) {
        databaseOpenHelper = new DatabaseOpenHelper(context);
        sqLiteDatabase = databaseOpenHelper.getWritableDatabase();
        try (Cursor cursor = sqLiteDatabase.query(DatabaseOpenHelper.tableFileStructure, null, null, null, null, null, null)) {
            if (cursor != null) {
                List<NodePack> nodePacks = new LinkedList<>();
                List<Node> nodes = new LinkedList<>();
                while (cursor.moveToFirst()) {
                    NodePack nodePack = new NodePack(cursor.getInt(0), cursor.getString(1), Node.Type.fromInteger(cursor.getInt(2)), cursor.getInt(3));
                    if (nodePack.belong == 0) {
                        root = nodePack.unpack(null);
                        nodes.add(root);
                    } else {
                        Node n = null;
                        for (Node node : nodes) {
                            if (node.getID() == nodePack.belong) {
                                n = nodePack.unpack(node);
                                break;
                            }
                        }
                        if (n == null) {
                            nodePacks.add(nodePack);
                        } else {
                            nodes.add(n);
                        }
                    }
                }
                while (!nodePacks.isEmpty()) {
                    NodePack nodePack = nodePacks.get(0);
                    nodePacks.remove(0);
                    Node n = null;
                    for (Node node : nodes) {
                        if (node.getID() == nodePack.belong) {
                            n = nodePack.unpack(node);
                            break;
                        }
                    }
                    if (n == null) {
                        nodePacks.add(nodePack);
                    } else {
                        nodes.add(n);
                    }
                }
            }
        }
    }
    private static class NodePack {
        protected final int id, belong;
        protected final String name;
        protected final Node.Type type;
        public NodePack(int id, String name, Node.Type type, int belong) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.belong = belong;
        }
        public Node unpack(Node parent) {
            Node node;
            switch (type) {
                case Folder: {
                    node = new Folder(id, name);
                    break;
                }
                case Text: {
                    node = new Text(id, name);
                    break;
                }
                default: return null;
            }
            if (parent instanceof Folder) {
                ((Folder) parent).addNode(node);
            }
            return node;
        }
    }
}

class DatabaseOpenHelper extends SQLiteOpenHelper {
    static final String
            tableFileStructure = "FileStructure",
            tableTextStructure = "TextStructure";

    public DatabaseOpenHelper(Context context) {
        super(context, "database.db", null, 0);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + tableFileStructure + ";");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + tableTextStructure + ";");
        sqLiteDatabase.execSQL("CREATE TABLE " + tableFileStructure + "(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "NAME TEXT NOT NULL, " +
                "TYPE INTEGER NOT NULL, " +
                "BELONG INTEGER NOT NULL" +
                ");");
        sqLiteDatabase.execSQL("CREATE TABLE " + tableTextStructure + "(" +
                "ID INTEGER PRIMARY KEY, " +
                "FILE TEXT NOT NULL" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}