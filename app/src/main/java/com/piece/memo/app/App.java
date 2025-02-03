package com.piece.memo.app;

import android.app.Application;

import com.piece.memo.database.Database;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Database.createDatabase(this);
    }
}
