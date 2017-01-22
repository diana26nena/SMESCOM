package mobileapps.ipn.mx.loginremote;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity implements OnClickListener{
    private EditText nombre, apellido_pat, apellido_mat, fecha_nac, sexo, correo, celular, contrasenya;
    private Button  mRegister;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    //si lo trabajan de manera local en xxx.xxx.x.x va su ip local
    // private static final String REGISTER_URL = "http://xxx.xxx.x.x:1234/cas/register.php";

    //testing on Emulator:
    private static final String REGISTER_URL = "http://sistmon.hol.es/register2.php";

    //ids
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        nombre = (EditText)findViewById(R.id.nombre);
        apellido_pat = (EditText)findViewById(R.id.apellido_pat);
        apellido_mat = (EditText)findViewById(R.id.apellido_mat);
        fecha_nac = (EditText)findViewById(R.id.fecha_nac);
        sexo = (EditText)findViewById(R.id.sexo);
        correo = (EditText)findViewById(R.id.correo);
        celular = (EditText)findViewById(R.id.celular);
        contrasenya = (EditText)findViewById(R.id.contrasenya);



        mRegister = (Button)findViewById(R.id.register);
        mRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        new CreateUser().execute();

    }

    class CreateUser extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Register.this);
            pDialog.setMessage("Creating User...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;
            String nombre_ = nombre.getText().toString();
            String apellido_pat_ = apellido_pat.getText().toString();
            String apellido_mat_ = apellido_mat.getText().toString();
            String fecha_nac_ = fecha_nac.getText().toString();
            String sexo_ = sexo.getText().toString();
            String correo_ = correo.getText().toString();
            String celular_ = celular.getText().toString();
            String contrasenya_ = contrasenya.getText().toString();
            try {
                // Building Parameters
                List params = new ArrayList();
                params.add(new BasicNameValuePair("nombre", nombre_));
                params.add(new BasicNameValuePair("apellido_pat", apellido_pat_));
                params.add(new BasicNameValuePair("apellido_mat", apellido_mat_));
                params.add(new BasicNameValuePair("fecha_nac", fecha_nac_));
                params.add(new BasicNameValuePair("sexo", sexo_));
                params.add(new BasicNameValuePair("correo", correo_));
                params.add(new BasicNameValuePair("celular", celular_));
                params.add(new BasicNameValuePair("contrasenya", contrasenya_));

                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(
                        REGISTER_URL, "POST", params);

                // full json response
                Log.d("Registering attempt", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("User Created!", json.toString());
                    finish();
                    return json.getString(TAG_MESSAGE);
                }else{
                    Log.d("Registering Failure!", json.getString(TAG_MESSAGE));
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
            if (file_url != null){
                Toast.makeText(Register.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }
}