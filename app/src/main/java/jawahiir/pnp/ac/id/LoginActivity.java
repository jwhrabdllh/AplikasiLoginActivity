package jawahiir.pnp.ac.id;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import jawahiir.pnp.ac.id.Util.ServerAPI;
import jawahiir.pnp.ac.id.Util.SessionManager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    EditText et_username, et_password;
    Button btn_login, btn_daftar;
    ProgressDialog progressBar;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    int success;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_username=findViewById(R.id.et_username);
        et_password=findViewById(R.id.et_password);
        btn_login=findViewById(R.id.btn_login);
        btn_daftar=findViewById(R.id.btn_daftar);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        progressBar = new ProgressDialog(this);

        sessionManager = new SessionManager(this);
        
        proses_daftar();

        proses_login();
    }

    private void proses_login() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_username.getText().toString().length()==0){
                    et_username.setError("Tidak Boleh Kosong!");
                }else if(et_password.getText().toString().length()==0){
                    et_password.setError("Tidak Boleh Kosong!");
                }else {
                    //jika user dan password telah diinput
                    api_login(et_username.getText().toString().trim(),et_password.getText().toString().trim());
                }
            }
        });
    }

    private void proses_daftar() {

        btn_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,DaftarActivity.class));
            }
        });
    }

    private void api_login(final String username, final String password){
        progressBar.setMessage("Please Wait..");
        progressBar.show();
        progressBar.setCancelable(false);

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("username", username)
                .add("pass", password)
                .build();

        final Request request = new Request.Builder()
                .url(ServerAPI.URL_LOGIN)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.dismiss();
                        Log.d("Login", "Failed "+e);
                        Toast.makeText(LoginActivity.this, "Server Sedang Maintanance",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if(response.code()==200){
                                JSONObject jObj = new JSONObject(response.body().string());
                                success = jObj.getInt(TAG_SUCCESS);

                                if (success == 1){
                                    // mengambil data response login
                                    JSONArray jsonArray = jObj.getJSONArray("login");

                                    for (int i=0; i<jsonArray.length();i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        String id_user = object.getString("id_user").trim();
                                        String nama = object.getString("nama").trim();
                                        String img = object.getString("img").trim();
                                        String api_key = object.getString("api_key").trim();

                                        // create session //
                                        sessionManager.createSession(id_user,nama,img,api_key);

                                        // proses intent direct ke MainActivity
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                }else {
                                    Toast.makeText(LoginActivity.this,jObj.getString(TAG_MESSAGE),Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "Server Sedang Maintanance",Toast.LENGTH_LONG).show();
                            }
                            progressBar.dismiss();

                        } catch (Exception e){
                            progressBar.dismiss();
                            Log.d("Login", "Failed "+e);
                            Toast.makeText(LoginActivity.this, "Server Sedang Maintanance",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}

