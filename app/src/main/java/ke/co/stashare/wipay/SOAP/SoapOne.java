package ke.co.stashare.wipay.SOAP;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import ke.co.stashare.wipay.R;
import ke.co.stashare.wipay.ui.TheMainClass;

/**
 * Created by Ken Wainaina on 10/04/2017.
 */

public class SoapOne extends AppCompatActivity {

    String TAG = "Response";
    Button bt;
    EditText celcius;
    AppCompatActivity mActivity;
    String getCel;
    SoapPrimitive resultString;
    ProgressDialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.soap_activityone);

        bt = (Button) findViewById(R.id.bt);
        celcius = (EditText) findViewById(R.id.cel);



        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog = new ProgressDialog(SoapOne.this);
                dialog.setMessage("Converting, please wait...");
                dialog.show();

                mActivity = SoapOne.this;


                getCel = celcius.getText().toString();
                AsyncCallWS task = new AsyncCallWS();
                task.execute();
            }
        });
    }

    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");


            calculate();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            dialog.dismiss();

            if(resultString!=null) {
                //dialog.dismiss();
                Toast.makeText(SoapOne.this, "Response " + resultString.toString(), Toast.LENGTH_LONG).show();

            }else {
                //dialog.dismiss();
                Toast.makeText(SoapOne.this, "Response " + "empty response", Toast.LENGTH_LONG).show();

            }
        }

    }

    public void calculate() {
        String SOAP_ACTION = "http://www.webserviceX.NET/ConvertTemp";
        String METHOD_NAME = "ConvertTemp";
        String NAMESPACE = "http://www.webserviceX.NET/";
        String URL = "http://www.webservicex.net/ConvertTemperature.asmx";
        String fD = "degreeCelsius";
        String toF = "degreeFahrenheit";

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            Log.i(TAG, "Temp Obtained: " + getCel);


            Request.addProperty("Temperature", getCel);
            Request.addProperty("degreeCelsius", fD);
            Request.addProperty("ToUnit", toF);


            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);
            resultString = (SoapPrimitive) soapEnvelope.getResponse();

            Log.i(TAG, "Result Celsius: " + resultString);
        } catch (Exception ex) {
            dialog.dismiss();

            Log.e(TAG, "Error: " + ex.getMessage());
        }
    }
}
