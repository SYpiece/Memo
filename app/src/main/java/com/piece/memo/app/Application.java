package com.piece.memo.app;

import com.piece.memo.database.Database;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Database.createDatabase(this);
    }
}
