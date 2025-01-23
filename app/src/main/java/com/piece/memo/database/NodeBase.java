package com.piece.memo.database;

import androidx.annotation.NonNull;

import java.util.List;

public abstract class NodeBase implements Node {
    protected long id;
    protected String title, description, text;
    protected final Type type;
    protected Node parent;
    protected final Database database;

    public long getID() {
        return id;
    }

    protected void setID(long id) {
        this.id = id;
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

    @NonNull
    public NodeBase getParent() {
        return parent;
    }

    protected void setParent(@NonNull NodeBase parent) {
        this.parent = parent;
    }

    @NonNull
    public Database getDatabase() {
        return database;
    }

    public NodeBase(@NonNull NodeBase parent, @NonNull String text, @NonNull Type type) {
        this(-1, parent, text, type);
    }

    protected NodeBase(long id, @NonNull NodeBase parent, @NonNull String text, @NonNull Type type) {
        this.id = id;
        this.text = text;
        this.parent = parent;
        this.type = type;
        this.database = parent.getDatabase();
    }

    protected NodeBase(long id, @NonNull Database database, @NonNull String text, @NonNull Type type) {
        this.id = id;
        this.text = text;
        this.type = type;
        this.database = database;
    }

    /**
     * 创建一个新的节点并插入到数据库中。
     *
     * @throws RuntimeException 如果节点已经存在于数据库中
     */
    public void create() {
        if (this.id != -1) {
            throw new RuntimeException();
        }
        database.insertNode(this);
    }

    /**
     * 更新当前节点的信息到数据库中。
     *
     * @throws RuntimeException 如果节点不存在于数据库中
     */
    public void update() {
        if (this.id == -1) {
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
    public void moveTo(@NonNull NodeBase parent) {
        if (this.id == -1) {
            throw new RuntimeException();
        }
        database.moveNode(this, parent);
    }

    /**
     * 将当前节点复制到新的父节点下。
     *
     * @param parent 新的父节点
     * @throws RuntimeException 如果节点不存在于数据库中
     */
    public void copyTo(@NonNull NodeBase parent) {
        if (this.id == -1) {
            throw new RuntimeException();
        }
        database.copyNode(this, parent);
    }

    /**
     * 从数据库中删除当前节点。
     *
     * @throws RuntimeException 如果节点不存在于数据库中
     */
    public void delete() {
        if (this.id == -1) {
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
    public List<NodeBase> getChildren() {
        return database.queryChildren(this);
    }
}
