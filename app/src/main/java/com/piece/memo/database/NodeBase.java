package com.piece.memo.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public abstract class NodeBase implements Node {
    protected int id, belong;
    protected String title = "", description = "", text;
    protected final Type type;
    protected final Database database;

    public int getID() {
        return id;
    }

    protected void setID(int id) {
        this.id = id;
    }

    @NonNull
    protected String getTitle() {
        return title;
    }

    protected void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    protected String getDescription() {
        return description;
    }

    protected void setDescription(@NonNull String description) {
        this.description = description;
    }

    @NonNull
    public String getText() {
        return text;
    }

    public void setText(@NonNull String text) {
        this.text = text;
    }

    @NonNull
    public Type getType() {
        return type;
    }

    public int getParentID() {
        return belong;
    }

    @Nullable
    public Node getParent() {
        return database.getNode(belong);
    }

    @NonNull
    public Database getDatabase() {
        return database;
    }

    public NodeBase(@NonNull Node parent, @NonNull String text, @NonNull Type type) {
        this(-1, parent.getID(), text, "", "", type, parent.getDatabase());
    }

    public NodeBase(@NonNull Node parent, @NonNull String title, @NonNull String description, @NonNull Type type) {
        this(-1, parent.getID(), "", title, description, type, parent.getDatabase());
    }

    protected NodeBase(int id, int belong, @NonNull String text, @NonNull String title, @NonNull String description, @NonNull Type type, @NonNull Database database) {
        this.id = id;
        this.text = text;
        this.title = title;
        this.description = description;
        this.belong = belong;
        this.type = type;
        this.database = database;
    }

    protected NodeBase(int id, @NonNull Database database, @NonNull Type type) {
        this.id = id;
        this.type = type;
        this.database = database;
    }

    /**
     * 创建一个新的节点并插入到数据库中。
     *
     * @throws RuntimeException 如果节点已经存在于数据库中
     */
    public void create() {
        if (id != -1) {
            throw new RuntimeException();
        }
        id = database.insertNode(this);
    }

    /**
     * 更新当前节点的信息到数据库中。
     *
     * @throws RuntimeException 如果节点不存在于数据库中
     */
    public void update() {
        if (id == -1) {
            throw new RuntimeException();
        }
        database.updateNode(this);
    }

    /**
     * 将当前节点移动到新的父节点下。
     *
     * @param parent 新的父节点
     * @throws RuntimeException 如果节点不存在于数据库中
     */
    public void moveTo(@NonNull Node parent) {
        if (id == -1 || !(parent instanceof Container)) {
            throw new RuntimeException();
        }
        database.moveNode(this, (NodeBase) parent);
    }

    /**
     * 将当前节点复制到新的父节点下。
     *
     * @param parent 新的父节点
     * @throws RuntimeException 如果节点不存在于数据库中
     */
    public void copyTo(@NonNull Node parent) {
        if (id == -1 || !(parent instanceof Container)) {
            throw new RuntimeException();
        }
        database.copyNode(this, (NodeBase) parent);
    }

    /**
     * 从数据库中删除当前节点。
     *
     * @throws RuntimeException 如果节点不存在于数据库中
     */
    public void delete() {
        if (id == -1) {
            throw new RuntimeException();
        }
        database.deleteNode(this);
    }

    /**
     * 获取当前节点的所有子节点。
     *
     * @return 子节点列表
     */
    @NonNull
    public List<Node> getChildren() {
        return database.queryChildren(this);
    }
}
