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
    boolean Run;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);

//        text_view = findViewById(R.id.text_view);
        Enter = findViewById(R.id.button_enter);
        x_val = findViewById(R.id.x_cord);
        y_val = findViewById(R.id.y_cord);
        theta_val = findViewById(R.id.theta_cord);

        x_val.addTextChangedListener(valueswatcher);
        y_val.addTextChangedListener(valueswatcher);
        theta_val.addTextChangedListener(valueswatcher);


//        Intent intent = getIntent();
//        String text = intent.getStringExtra(MainActivity.SIM_RESULT);
//        text_view.setText(text);

//        Stop_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Run = false;
//            }
//        });
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
        Run = true;
        //finish();
        System.out.println("Read Clicked");
        new ConnectionAsyncTask().execute();
        System.out.println("ConnectionAsync Executed");
    }

    public class ConnectionAsyncTask extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... strings) {

            // Create Application Description
            ApplicationDescription appDescription = MainActivity.applicationDescription;
//            applicationDescription.setApplicationName(new LocalizedText("AndroidClient",
//                    Locale.ENGLISH));
//            applicationDescription.setApplicationUri(
//                    "urn:localhost:fd8d1dac-7224-4054-9592-1d944e1f4de6");
            //applicationDescription.setProductUri("urn:prosysopc.com:OPCUA:SimulationServer");
//            applicationDescription.setApplicationType(ApplicationType.Client);

            //Create Client Application Instance Certificate
            try {
//                System.out.println("xString: " + x_string + "\nyString: " + y_string + "\nthetaString: " + theta_string);
                KeyPair myClientApplicationInstanceCertificate = ExampleKeys.getCert(
                        getApplicationContext(), appDescription);

                //Create Client
                Client myClient = Client.createClientApplication(
                        myClientApplicationInstanceCertificate);

                // Discover endpoints
//                EndpointDescription[] endpoints = myClient.discoverEndpoints(endpturi);
//
                // Filter out all but opc.tcp protocol endpoints
//                endpoints = EndpointUtil.selectByProtocol(endpoints, "opc.tcp");
//
                // Filter out all but Signed & Encrypted endpoints
//                endpoints = EndpointUtil.selectByMessageSecurityMode(endpoints,
//                        MessageSecurityMode.None);

                // Filter out all but Basic256Sha256 encryption endpoints
//                endpoints = EndpointUtil.selectBySecurityPolicy(endpoints,
//                        SecurityPolicy.BASIC256SHA256);
                // Sort endpoints by security level. The lowest level at the beginning,
                // the highest at the end of the array
//                endpoints = EndpointUtil.sortBySecurityLevel(endpoints);
                // Choose one endpoint.
//                EndpointDescription endpoint = endpoints[0];
//
                // Choose one endpoint.
//                EndpointDescription endpoint = endpoints[endpoints.length - 1];
//                System.out.println("Endpoint Selected ");
//                endpoint.setEndpointUrl(endpturi);
//                System.out.println("new Endpoint url: " + endpoint.getEndpointUrl());
                EndpointDescription ept = MainActivity.endpoint;
//
//                System.out.println("Security Level " + endpoint.getSecurityPolicyUri());
//                System.out.println("Security Mode " + endpoint.getSecurityMode());
//
//
                //Create the session from the chosen endpoint
                SessionChannel mySession = myClient.createSessionChannel(ept);
//                System.out.println("Session Channel: " + mySession);
                // Activate the session.
                mySession.activate();

                //Browse
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

                //Input Arguments for Turtlesim teleop_absolute
//                Float cords_x = 8.0f;
                Float cords_x = Float.valueOf(x_string);
                Variant cords_x_InVariant = new Variant(cords_x);
//                Float cords_y = 2.0f;
                Float cords_y = Float.valueOf(y_string);
                Variant cords_y_InVariant = new Variant(cords_y);
                Float cords_ang = Float.valueOf(theta_string);
//                Float cords_ang = 180.0f;
                Variant cords_ang_InVariant = new Variant(cords_ang);
                methodRequest.setInputArguments( new Variant[] {cords_x_InVariant, cords_y_InVariant, cords_ang_InVariant});
//                System.out.println("Input Arguments: ----------" + methodRequest.getInputArguments()[0]);
                CallResponse res1 = mySession.Call(callRequest);
                System.out.println("Call Response:--------- " + res1);

                // Close the session
                mySession.close();
                mySession.closeAsync();

                return res1.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return "Connection Failed!!";
            }
        }

        @Override
        protected void onPostExecute(String result){
//            text_view.setText(result);
//            if (Run){
//                new ConnectionAsyncTask().execute();
//            }
        }
    }
}
