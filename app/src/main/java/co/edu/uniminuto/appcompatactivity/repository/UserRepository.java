package co.edu.uniminuto.appcompatactivity.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.sql.SQLDataException;
import java.util.ArrayList;

import co.edu.uniminuto.appcompatactivity.dataaccess.ManagerDataBase;
import co.edu.uniminuto.appcompatactivity.entities.User;

public class UserRepository {
    private ManagerDataBase dataBase;
    private Context context;
    private View view;
    private User user;

    public UserRepository(Context context, View view) {
        this.context = context;
        this.view = view;
        this.dataBase = new ManagerDataBase(context);

    }

    public void insertUser(User user) {
        try {
            SQLiteDatabase dataBaseSQL = dataBase.getWritableDatabase();
            if (dataBaseSQL != null) {
                ContentValues values = new ContentValues();
                values.put("use_document", user.getDocument());
                values.put("use_name", user.getName());
                values.put("use_lastname", user.getLastName());
                values.put("use_user", user.getUser());
                values.put("use_pass", user.getPass());
                values.put("use_status", "1");
                long response = dataBaseSQL.insert("users", null, values);
                String message = (response >= 1) ? "Se registró correctamente" : "No se pudo registrar";
                Snackbar.make(this.view, message, Snackbar.LENGTH_LONG).show();
            }
        } catch (SQLException e) {
            Log.i("Error en base de datos", "insertUser: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public ArrayList<User> getUserList() {
        SQLiteDatabase dataBaseSQL = dataBase.getWritableDatabase();
        String query = "SELECT * FROM users WHERE use_status = 1;";
        ArrayList<User> users = new ArrayList<>();
        Cursor cursor = dataBaseSQL.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setDocument(cursor.getInt(0));
                user.setName(cursor.getString(1));
                user.setLastName(cursor.getString(2));
                user.setUser(cursor.getString(3));
                user.setPass(cursor.getString(4));
                users.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        dataBaseSQL.close();
        return users;

    }
}