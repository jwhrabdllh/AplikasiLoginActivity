package jawahiir.pnp.ac.id.Util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import jawahiir.pnp.ac.id.LoginActivity;
import jawahiir.pnp.ac.id.MainActivity;

public class SessionManager {
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";

    // bersumber dari response API //
    public static final String ID_USERNAME = "ID_USERNAME";
    public static final String NAMA = "NAMA";
    public static final String IMG = "IMG";
    public static final String API_KEY = "API_KEY";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String id_username, String nama, String img, String api_key) {
        editor.putBoolean(LOGIN, true);
        editor.putString(ID_USERNAME, id_username);
        editor.putString(NAMA, nama);
        editor.putString(IMG, img);
        editor.putString(API_KEY, api_key);
        editor.apply();
    }

    public boolean isLoggin() {
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    // cek apakah sudah login
    public void checkLogin() {
        if (!this.isLoggin()) {
            // jika belum login di direct ke login //
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);
            ((MainActivity) context).finish();
        }
    }

    // mengambil data yang sudah kita simpan //
    public HashMap<String, String> getUserDetail() {
        HashMap<String, String> user = new HashMap<>();
        user.put(ID_USERNAME, sharedPreferences.getString(ID_USERNAME, null));
        user.put(NAMA, sharedPreferences.getString(NAMA, null));
        user.put(IMG, sharedPreferences.getString(IMG, null));
        user.put(API_KEY, sharedPreferences.getString(API_KEY, null));
        return user;
    }
}
