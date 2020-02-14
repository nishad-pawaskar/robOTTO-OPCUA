package com.example.robotto_opcua;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.opcfoundation.ua.application.Client;
import org.opcfoundation.ua.application.SessionChannel;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.core.ApplicationType;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResponse;
import org.opcfoundation.ua.core.CallMethodRequest;
import org.opcfoundation.ua.core.CallMethodResult;
import org.opcfoundation.ua.core.CallRequest;
import org.opcfoundation.ua.core.CallResponse;
import org.opcfoundation.ua.core.EndpointDescription;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.MessageSecurityMode;
import org.opcfoundation.ua.core.ReadResponse;
import org.opcfoundation.ua.core.ReadValueId;
import org.opcfoundation.ua.core.TimestampsToReturn;
import org.opcfoundation.ua.core.WriteResponse;
import org.opcfoundation.ua.core.WriteValue;
import org.opcfoundation.ua.transport.security.KeyPair;
import org.opcfoundation.ua.utils.EndpointUtil;

import java.util.List;
import java.util.Locale;

public class Activity1 extends AppCompatActivity {
    EditText x_val, y_val, theta_val;
    Button Enter;
    String x_string, y_string, theta_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);

        Enter = findViewById(R.id.button_enter);
        x_val = findViewById(R.id.x_cord);
        y_val = findViewById(R.id.y_cord);
        theta_val = findViewById(R.id.theta_cord);

        x_val.addTextChangedListener(valueswatcher);
        y_val.addTextChangedListener(valueswatcher);
        theta_val.addTextChangedListener(valueswatcher);

    }

    private TextWatcher valueswatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            x_string = x_val.getText().toString().trim();
            y_string = y_val.getText().toString().trim();
            theta_string = theta_val.getText().toString().trim();

            Enter.setEnabled(!x_string.isEmpty() || !y_string.isEmpty() || !theta_string.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void connect(View view){
        System.out.println("Read Clicked");
        new ConnectionAsyncTask().execute();
        System.out.println("ConnectionAsync Executed");
    }

    public class ConnectionAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            // Create Application Description
            ApplicationDescription appDescription = MainActivity.applicationDescription;

            //Create Client Application Instance Certificate
            try {
                KeyPair myClientApplicationInstanceCertificate = ExampleKeys.getCert(
                        getApplicationContext(), appDescription);

                //Create Client
                Client myClient = Client.createClientApplication(
                        myClientApplicationInstanceCertificate);

                // Choose the endpoint.
                EndpointDescription ept = MainActivity.endpoint;

                //Create the session from the chosen endpoint
                SessionChannel mySession = myClient.createSessionChannel(ept);

                // Activate the session.
                mySession.activate();

                //Browse the address space
                BrowseDescription browse = new BrowseDescription();
//                NodeId n = Identifiers.ObjectsFolder;
                NodeId n = new NodeId(3, "turtle1");    //ObjectID
                NodeId n1 = new NodeId(3, 2);           //MethodID
                browse.setNodeId(n);
                browse.setBrowseDirection(BrowseDirection.Forward);
                browse.setIncludeSubtypes(true);
                BrowseResponse res3 = mySession.Browse(null, null,
                        null, browse);
                System.out.println(res3);

                //Call Method
                CallRequest callRequest = new CallRequest();
                CallMethodRequest methodRequest = new CallMethodRequest();
                callRequest.setMethodsToCall(new CallMethodRequest[] {methodRequest});
                methodRequest.setMethodId(n1);
                methodRequest.setObjectId(n);

                //Input Arguments for Turtlesim teleport_absolute
                Float cords_x = Float.valueOf(x_string);
                Variant cords_x_InVariant = new Variant(cords_x);
                Float cords_y = Float.valueOf(y_string);
                Variant cords_y_InVariant = new Variant(cords_y);
                Float cords_ang = Float.valueOf(theta_string);
                Variant cords_ang_InVariant = new Variant(cords_ang);
                methodRequest.setInputArguments( new Variant[] {cords_x_InVariant, cords_y_InVariant, cords_ang_InVariant});

                CallResponse res1 = mySession.Call(callRequest);
                System.out.println("Call Response:--------- " + res1);

                // Close the session
                mySession.close();
                mySession.closeAsync();

                return res1.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return "Error! Cannot enter data";
            }
        }

        @Override
        protected void onPostExecute(String result){

        }
    }
}
