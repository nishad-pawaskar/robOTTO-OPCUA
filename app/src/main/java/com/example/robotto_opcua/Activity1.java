package com.example.robotto_opcua;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.opcfoundation.ua.application.Client;
import org.opcfoundation.ua.application.SessionChannel;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.core.ApplicationType;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResponse;
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

import java.util.Locale;

public class Activity1 extends AppCompatActivity {
    TextView text_view;
    Button Stop_btn;
    boolean Run;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1);

        text_view = findViewById(R.id.text_view);
        Stop_btn = findViewById(R.id.button_stop);

        Intent intent = getIntent();
        String text = intent.getStringExtra(MainActivity.SIM_RESULT);
        text_view.setText(text);

        Stop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Run = false;
            }
        });
    }

    public void connect(View view){
        Run = true;
        //finish();
        System.out.println("Read Clicked");
        new ConnectionAsyncTask().execute();
        System.out.println("ConnectionAsync Executed");
    }

    public class ConnectionAsyncTask extends AsyncTask<String, String, String> {
        //
//
        @Override
        protected String doInBackground(String... strings) {

//            // Create Application Description
            ApplicationDescription appDescription = MainActivity.applicationDescription;
//            applicationDescription.setApplicationName(new LocalizedText("AndroidClient",
//                    Locale.ENGLISH));
//            applicationDescription.setApplicationUri(
//                    "urn:localhost:fd8d1dac-7224-4054-9592-1d944e1f4de6");
//            //applicationDescription.setProductUri("urn:prosysopc.com:OPCUA:SimulationServer");
//            applicationDescription.setApplicationType(ApplicationType.Client);
//
//            //Create Client Application Instance Certificate
            try {
                KeyPair myClientApplicationInstanceCertificate = ExampleKeys.getCert(
                        getApplicationContext(), appDescription);
//
//                //Create Client
                Client myClient = Client.createClientApplication(
                        myClientApplicationInstanceCertificate);
//
//                // Discover endpoints
//                EndpointDescription[] endpoints = myClient.discoverEndpoints(endpturi);
//
//                // Filter out all but opc.tcp protocol endpoints
//                endpoints = EndpointUtil.selectByProtocol(endpoints, "opc.tcp");
//
//                // Filter out all but Signed & Encrypted endpoints
//                endpoints = EndpointUtil.selectByMessageSecurityMode(endpoints,
//                        MessageSecurityMode.None);
////
//                // Filter out all but Basic256Sha256 encryption endpoints
////                endpoints = EndpointUtil.selectBySecurityPolicy(endpoints,
////                        SecurityPolicy.BASIC256SHA256);
//                // Sort endpoints by security level. The lowest level at the beginning,
//                // the highest at the end of the array
//                endpoints = EndpointUtil.sortBySecurityLevel(endpoints);
//                // Choose one endpoint.
//                //EndpointDescription endpoint = endpoints[0];
//
//                // Choose one endpoint.
//                EndpointDescription endpoint = endpoints[endpoints.length - 1];
//                System.out.println("Endpoint Selected ");
//                endpoint.setEndpointUrl(endpturi);
//                //System.out.println("new Endpoint url: " + endpoint.getEndpointUrl());
                EndpointDescription ept = MainActivity.endpoint;
////
////                System.out.println("Security Level " + endpoint.getSecurityPolicyUri());
////                System.out.println("Security Mode " + endpoint.getSecurityMode());
////
////
//                //Create the session from the chosen endpoint
                    SessionChannel mySession = myClient.createSessionChannel(ept);
////                System.out.println("Session Channel: " + mySession);
////                // Activate the session.
                mySession.activate();



                //Browse
                BrowseDescription browse = new BrowseDescription();
                //NodeId n = Identifiers.ObjectsFolder;
                NodeId n = new NodeId(3, 24);
                browse.setNodeId(n);
                browse.setBrowseDirection(BrowseDirection.Forward);
                browse.setIncludeSubtypes(true);
                BrowseResponse res3 = mySession.Browse(null, null,
                        null, browse);
                //String result = res3.toString();
                System.out.println("Browse Response:  " + res3);


                // Read and write variables here
//                NodeId nodeId = new NodeId(3, "Random");
                // Read a variable
                ReadValueId readValueId = new ReadValueId(n, Attributes.Value, null,
                        null);
                ReadResponse res = mySession.Read(null, 500.0,
                        TimestampsToReturn.Source, readValueId);
                DataValue[] dataValue = res.getResults();
                String result = dataValue[0].getValue().toString();

//
//                double[] value = {7.20, 5.44, 0.00};
                Object[] value = new Object[3];
                value[0] = 7.20;
                value[1] = 5.44;
                value[2] = 0.00;
                // Write a variable. In this case the same variable read is set to 0
                WriteValue writeValue = new WriteValue(n, Attributes.Value, null,
                        new DataValue((new Variant(value))));
                System.out.println("Write Value: " + value.toString());
                WriteResponse wresponse =  mySession.Write(null, writeValue);
                System.out.println("Write Response: " + wresponse);
//
//
//                // Close the session
                mySession.close();
                mySession.closeAsync();
//
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return "Connection Failed!!";
            }
        }
//
        @Override
        protected void onPostExecute(String result){
            text_view.setText(result);
//            if (Run){
//                new ConnectionAsyncTask().execute();
//            }
        }
    }
}
