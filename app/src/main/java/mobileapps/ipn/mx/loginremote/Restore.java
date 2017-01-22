package mobileapps.ipn.mx.loginremote;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Restore extends Activity implements View.OnClickListener {
    private EditText correo;
    private Button mRestore;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class, se utiliza para el envio de peticiones y respuestas
    JSONParser jsonParser = new JSONParser();

    //link de la parte funcional de php webservices
    private static final String RESTORE_URL = "http://sistmon.hol.es/restore2.php";

    //Banderas para mensajes de error o éxito
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restore);
        correo = (EditText)findViewById(R.id.correo);
        mRestore = (Button)findViewById(R.id.restore);
        mRestore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        new Restore.SendEmail().execute();

    }


    class SendEmail extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Restore.this);
            pDialog.setMessage("Send email...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            int success;
            String correo_ = correo.getText().toString();
            try {
                // Building Parameters
                List params = new ArrayList();
                params.add(new BasicNameValuePair("correo", correo_));

                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(
                        RESTORE_URL, "POST", params);

                // full json response
                Log.d("Restore email attempt", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Email Send!", json.toString());
                    finish();
                    return json.getString(TAG_MESSAGE);
                }else{
                    Log.d("Not Email Send!", json.getString(TAG_MESSAGE));
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
                Toast.makeText(Restore.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }

}
