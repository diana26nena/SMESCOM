package mobileapps.ipn.mx.loginremote;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity implements OnClickListener {

    private EditText correo, contrasenya;
    private Button mSubmit, mRegister, mRestore;

    private ProgressDialog pDialog;

    // Clase JSONParser
    JSONParser jsonParser = new JSONParser();


    // si trabajan de manera local "localhost" :
    // En windows tienen que ir, run CMD > ipconfig
    // buscar su IP
    // y poner de la siguiente manera
    // "http://xxx.xxx.x.x:1234/cas/login.php";

    //Dominio de la base de datos remota.
    //private static final String LOGIN_URL = "http://sistmon.hol.es/login1.php";
    private static final String LOGIN_URL = "http://sistmon.hol.es/login2.php";

    // Respuesta del JSON
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // setup input fields
        correo = (EditText) findViewById(R.id.correo);
        contrasenya = (EditText) findViewById(R.id.contrasenya);

        // setup buttons
        mSubmit = (Button) findViewById(R.id.login);
        mRegister = (Button) findViewById(R.id.register);
        mRestore = (Button) findViewById(R.id.restore);

        // register listeners
        mSubmit.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        mRestore.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.login:
                new AttemptLogin().execute();
                break;
            case R.id.register:
                Intent regi = new Intent(this, Register.class);
                startActivity(regi);
                break;
            case R.id.restore:
                Intent rest = new Intent(this, Restore.class);
                startActivity(rest);
                break;
            default:
                break;
        }
    }

    class AttemptLogin extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Attempting login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            int success;
            String correo_ = correo.getText().toString();
            String contrasenya_ = contrasenya.getText().toString();
            try {
                // Building Parameters
                List params = new ArrayList();
                params.add(new BasicNameValuePair("correo", correo_));
                params.add(new BasicNameValuePair("contrasenya", contrasenya_));

                Log.d("request!", "starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST",
                        params);

                // check your log for json response
                Log.d("Login attempt", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Login Successful!", json.toString());
                    // save user data
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(Login.this);
                    Editor edit = sp.edit();
                    edit.putString("correo", correo_);
                    edit.commit();

                    Intent i = new Intent(Login.this, ReadComments.class);
                    finish();
                    startActivity(i);
                    return json.getString(TAG_MESSAGE);
                } else {
                    Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null) {
                Toast.makeText(Login.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }
}