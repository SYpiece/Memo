package com.piece.memo.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Node接口定义了所有节点的基本操作和属性。
 */
public interface Node {
    int NO_ID = -1;

    /**
     * 获取节点的唯一标识符。
     * @return 节点的ID，如果节点未创建则返回NO_ID。
     */
    int getID();

    /**
     * 检查节点是否已经创建。
     * @return 如果节点已经创建则返回true，否则返回false。
     */
    default boolean exists() {
        return getID() != NO_ID;
    }

    /**
     * 获取节点的类型。
     * @return 节点的类型。
     */
    @NonNull Type getType();

    /**
     * 获取节点所属的父容器ID。
     * @return 父容器的ID。
     */
    int getBelong();

    /**
     * 获取节点的父容器。
     * @return 父容器对象，如果节点没有父容器则返回null。
     */
    @Nullable
    default Container getParent() {
        return (Container) getDatabase().getNode(getBelong());
    }

    /**
     * 获取节点所属的数据库。
     * @return 数据库对象。
     */
    @NonNull Database getDatabase();

    /**
     * 创建节点并将其插入到数据库中。
     */
    void create();

    /**
     * 更新节点信息到数据库。
     */
    void update();

    /**
     * 将节点移动到指定的父容器。
     * @param parent 目标父容器。
     */
    void moveTo(@NonNull Container parent);

    /**
     * 将节点复制到指定的父容器。
     * @param parent 目标父容器。
     */
    void copyTo(@NonNull Container parent);

    /**
     * 从数据库中删除节点。
     */
    void delete();

    /**
     * 节点类型的枚举。
     */
    enum Type {
        Unknown, Folder, Text, Paragraph;

        /**
         * 将节点类型转换为整数。
         * @param type 节点类型。
         * @return 对应的整数值。
         */
        public static int toInteger(@NonNull Type type) {
            switch (type) {
                case Folder: return 1;
                case Text: return 2;
                case Paragraph: return 3;
                default: return 0;
            }
        }

        /**
         * 将整数转换为节点类型。
         * @param i 整数值。
         * @return 对应的节点类型。
         */
        @NonNull public static Type fromInteger(int i) {
            switch(i) {
                case 1: return Folder;
                case 2: return Text;
                case 3: return Paragraph;
                default: return Unknown;
            }
        }
    }
}

abstract class NodeBase implements Node{
    int id, belong;
    Type type;
    Database database;
    String name, description, text;

    @Override
    public int getID() {
        return id;
    }

    @NonNull
    @Override
    public Type getType() {
        return type;
    }

    @Override
    public int getBelong() {
        return belong;
    }

    @NonNull
    @Override
    public Database getDatabase() {
        return database;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
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
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    protected NodeBase(@NonNull Node parent, @NonNull Type type) {
        this(parent.getDatabase(), NO_ID, parent.getID(), type, "", "", "");
    }

    protected NodeBase(@NonNull Node parent, @NonNull Type type, @NonNull String text, @NonNull String name, @NonNull String description) {
        this(parent.getDatabase(), NO_ID, parent.getID(), type, text, name, description);
    }

    protected NodeBase(@NonNull Database database, int id, int belong, @NonNull Type type, @NonNull String text, @NonNull String name, @NonNull String description) {
        this.database = database;
        this.id = id;
        this.belong = belong;
        this.text = text;
        this.name = name;
        this.description = description;
        this.type = type;
    }

    @Override
    public void create() {
        if (id != NO_ID) {
            throw new RuntimeException();
        }
        id = database.insertNode(this);
    }

    @Override
    public void update() {
        if (getID() == NO_ID) {
            throw new RuntimeException();
        }
        database.updateNode(this);
    }

    @Override
    public void moveTo(@NonNull Container parent) {
        if (getID() == NO_ID) {
            throw new RuntimeException();
        }
        database.updateNode(this);
    }

    @Override
    public void copyTo(@NonNull Container parent) {
        if (getID() == NO_ID) {
            throw new RuntimeException();
        }
        database.copyNode(this, parent);
    }

    @Override
    public void delete() {
        if (getID() == NO_ID) {
            throw new RuntimeException();
        }
        database.deleteNode(this);
    }
}
