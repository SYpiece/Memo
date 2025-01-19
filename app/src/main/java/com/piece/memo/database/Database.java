package com.piece.memo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

//    protected final DatabaseOpenHelper databaseOpenHelper;
    protected final SQLiteDatabase sqLiteDatabase;
    protected final Folder root = new Folder(0, null, "root");

    public Database(Context context) {
//        databaseOpenHelper = new DatabaseOpenHelper(context);
//        sqLiteDatabase = databaseOpenHelper.getWritableDatabase();
        sqLiteDatabase = context.openOrCreateDatabase("database.db", Context.MODE_PRIVATE, null);
        createDatabase();
        readDatabase();
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

    private void readDatabase() {
        try (Cursor cursor = sqLiteDatabase.query(DatabaseOpenHelper.tableFileStructure, null, belongKey + " = ?", new String[] { "0" }, null, null, null)) {

        }
    }

    public Folder getRoot() {
        return root;
    }

//    private void readDatabase(Context context) {
//        root.setExisted(true);
//        List<Node> nodes = new LinkedList<>();
//        try (Cursor cursor = sqLiteDatabase.query(DatabaseOpenHelper.tableFileStructure, null, null, null, null, null, null)) {
//            if (cursor != null) {
//                List<NodePack> nodePacks = new LinkedList<>();
//                nodes.add(root);
//                while (cursor.moveToFirst()) {
//                    NodePack nodePack = new NodePack(cursor.getInt(0), cursor.getString(1), Node.Type.fromInteger(cursor.getInt(2)), cursor.getInt(3));
//                    Node n = null;
//                    for (Node node : nodes) {
//                        if (node.getID() == nodePack.belong) {
//                            n = nodePack.unpack(node);
//                            break;
//                        }
//                    }
//                    if (n == null) {
//                        nodePacks.add(nodePack);
//                    } else {
//                        nodes.add(n);
//                    }
//                }
//                while (!nodePacks.isEmpty()) {
//                    NodePack nodePack = nodePacks.get(0);
//                    nodePacks.remove(0);
//                    Node n = null;
//                    for (Node node : nodes) {
//                        if (node.getID() == nodePack.belong) {
//                            n = nodePack.unpack(node);
//                            break;
//                        }
//                    }
//                    if (n == null) {
//                        nodePacks.add(nodePack);
//                    } else {
//                        nodes.add(n);
//                    }
//                }
//            } else {
//                throw new RuntimeException();
//            }
//        }
//        try (Cursor cursor = sqLiteDatabase.query(DatabaseOpenHelper.tableTextStructure, null, null, null, null, null, null)) {
//            if (cursor != null) {
//                while (cursor.moveToFirst()) {
//                    int id = cursor.getInt(0);
//                    String fileName = cursor.getString(1);
//                    for (Node node : nodes) {
//                        if (node instanceof Text && node.getID() == id) {
//                            ((Text) node).setFile(new File(context.getFilesDir(), fileName));
//                            break;
//                        }
//                    }
//                }
//            } else {
//                throw new RuntimeException();
//            }
//        }
//    }

    protected List<Node> queryChildren(Node node) {
        List<Node> nodes = new ArrayList<>();
        try (Cursor cursor = sqLiteDatabase.query(fileTable, null, belongKey + " = ?", new String[] { String.valueOf(node.getID()) }, null, null, null)) {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    nodes.add(new NodePack(
                            cursor.getLong(cursor.getColumnIndexOrThrow(idKey)),
                            cursor.getString(cursor.getColumnIndexOrThrow(textKey)),
                            Node.Type.fromInteger(cursor.getType(cursor.getColumnIndexOrThrow(typeKey))),
                            cursor.getLong(cursor.getColumnIndexOrThrow(belongKey))
                    ).unpack(node));
                }
            }
        }
        return Collections.unmodifiableList(nodes);
    }

    protected void insertNode(Node node) {
        ContentValues values = new ContentValues();
        values.put(textKey, node.getText());
        values.put(typeKey, Node.Type.toInteger(node.getType()));
        values.put(belongKey, node.getParent().getID());
        long id = sqLiteDatabase.insert(fileTable, null, values);
        if (id == -1) {
            throw new RuntimeException();
        }
        node.setID(id);
    }

    protected void updateNode(Node node) {
        ContentValues values = new ContentValues();
        values.put(textKey, node.getText());
        sqLiteDatabase.update(fileTable, values, idKey + " = ?", new String[] { String.valueOf(node.getID()) });
    }

    protected void moveNode(Node node, Node parent) {
        ContentValues values = new ContentValues();
        values.put(belongKey, parent.getID());
        sqLiteDatabase.update(fileTable, values, idKey + " = ?", new String[] { String.valueOf(node.getID()) });
    }

    protected void copyNode(Node node, Node parent) {
        ContentValues values = new ContentValues();
        values.put(textKey, node.getText());
        values.put(typeKey, Node.Type.toInteger(node.getType()));
        values.put(belongKey, parent.getID());
        sqLiteDatabase.insert(fileTable, null, values);
    }

    protected void deleteNode(Node node) {
        sqLiteDatabase.delete(fileTable, idKey + " = ?", new String[] { String.valueOf(node.getID()) });
    }

    private static class NodePack {
        protected final long id, belong;
        protected final String name;
        protected final Node.Type type;

        public NodePack(long id, String name, Node.Type type, long belong) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.belong = belong;
        }

        public Node unpack(Node parent) {
            if (parent.getID() != belong) {
                throw new RuntimeException();
            }
            Node node;
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