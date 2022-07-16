package jawahiir.pnp.ac.id;

import androidx.appcompat.app.AppCompatActivity;

import android.accessibilityservice.GestureDescription;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import jawahiir.pnp.ac.id.Util.ServerAPI;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DaftarActivity extends AppCompatActivity {
    EditText et_username,et_password,et_email,et_nama,et_no_tlpn,et_re_password;
    Button btn_login,btn_daftar;
    ProgressDialog progressBar;
    Spinner sp_jekel;

    private static final String TAG_SUCCESS="success";
    private static final String TAG_MESSAGE="message";
    int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        sp_jekel=findViewById(R.id.sp_jakel);
        btn_daftar=findViewById(R.id.btn_daftar);
        btn_login=findViewById(R.id.btn_login);
        et_username=findViewById(R.id.et_username);
        et_password=findViewById(R.id.et_password);
        et_re_password=findViewById(R.id.et_re_password);
        et_email=findViewById(R.id.et_email);
        et_nama=findViewById(R.id.et_nama);
        et_no_tlpn=findViewById(R.id.et_no_tlpn);

        progressBar = new ProgressDialog(this);

        String[] jekel = getResources().getStringArray(R.array.jenis_kelamin);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, jekel);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        sp_jekel.setAdapter(adapter);

        proses_daftar();
        halaman_login();
    }

    private void halaman_login() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DaftarActivity.this,LoginActivity.class));
            }
        });
    }

    private void proses_daftar() {
        btn_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_username.getText().toString().length() == 0) {
                    et_username.setError("Tidak Boleh Kosong!");
                } else if (et_password.getText().toString().length() == 0) {
                    et_password.setError("Tidak Boleh Kosong!");
                } else if (et_email.getText().toString().length() == 0) {
                    et_email.setError("Tidak Boleh Kosong!");
                } else if (et_no_tlpn.getText().toString().length() == 0) {
                    et_no_tlpn.setError("Tidak Boleh Kosong!");
                } else if (et_re_password.getText().toString().length() == 0) {
                    et_re_password.setError("Tidak Boleh Kosong!");
                } else {
                    if ((!et_password.getText().toString().equals(et_re_password.getText().toString()))) {
                        // jika password tidak sama dengan re password
                        Toast.makeText(DaftarActivity.this, "Password Tidak Sama",Toast.LENGTH_LONG).show();
                    } else {
                        // jika isian data sudah benar //
                        api_daftar(et_username.getText().toString(),
                                et_email.getText().toString(),
                                et_nama.getText().toString(),
                                sp_jekel.getSelectedItem().toString(),
                                et_no_tlpn.getText().toString(),
                                et_password.getText().toString());
                    }
                }
            }
        });
    }

    private void api_daftar(final String username, final String email,
                            final String nama, final String jenis_kelamin, final String no_tlpn, final String password) {
        progressBar.setMessage("Please wait...");
        progressBar.show();
        progressBar.setCancelable(false);

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("username", username)
                .add("email", email)
                .add("nama", nama)
                .add("jenis_kelamin", jenis_kelamin)
                .add("no_tlpn", no_tlpn)
                .add("pass", password)
                .build();

        final Request request = new Request.Builder()
                .url(ServerAPI.URL_DAFTAR)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // jika terjadi error saat request ke API //
                progressBar.dismiss();
                Log.d("Daftar",  "Failed "+e);
                Toast.makeText(DaftarActivity.this, "Server Sedang Maintanance",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if(response.code()==200){
                                // terhubung ke API //
                                JSONObject jObj = new JSONObject(response.body().string());
                                success = jObj.getInt(TAG_SUCCESS);
                                if (success == 1) {
                                    Toast.makeText(DaftarActivity.this,jObj.getString(TAG_MESSAGE),Toast.LENGTH_LONG).show();
                                }else {
                                    Toast.makeText(DaftarActivity.this,jObj.getString(TAG_MESSAGE),Toast.LENGTH_LONG).show();
                                }
                                // Toast.makeText(DaftarActivity.this, "Terhubung ke API",Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(DaftarActivity.this, "Server Sedang Maintanance", Toast.LENGTH_LONG).show();
                            }
                            progressBar.dismiss();
                        }catch (Exception e){
                                progressBar.dismiss();
                                Log.d("Daftar", "Failed "+e);
                                Toast.makeText(DaftarActivity.this, "Server Sedang Maintanance",Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }
        });
    }
}