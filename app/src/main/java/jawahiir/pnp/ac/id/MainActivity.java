package jawahiir.pnp.ac.id;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.HashMap;

import jawahiir.pnp.ac.id.Util.SessionManager;

public class MainActivity extends AppCompatActivity {
    SessionManager sessionManager;
    String id_username, nama, img, api_key;
    TextView tes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tes = findViewById(R.id.tes);

        // cek login //
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        // ambil data yang sudah disimpan didalam sharedpreferances //
        HashMap<String, String> user = sessionManager.getUserDetail();
        id_username = user.get(sessionManager.ID_USERNAME);
        nama = user.get(sessionManager.NAMA);
        img = user.get(sessionManager.IMG);
        api_key = user.get(sessionManager.API_KEY);
        // ----------------------------------------------------------

        tes.setText("id_username "+id_username+" nama"+nama+"img "+img+"api_key "+api_key);
    }
}