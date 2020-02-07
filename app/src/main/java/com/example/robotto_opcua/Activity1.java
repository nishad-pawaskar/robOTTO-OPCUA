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
                //NodeId n = new NodeId(2, "Demo.Static.Scalar.Int32");
                NodeId n = new NodeId(3, "turtle1");
                browse.setNodeId(n);
                browse.setBrowseDirection(BrowseDirection.Forward);
                browse.setIncludeSubtypes(true);
                BrowseResponse res3 = mySession.Browse(null, null,
                        null, browse);
                //String result = res3.toString();
                System.out.println(res3);

                NodeId n1 = new NodeId(3, 2);


                // Read and write variables here
//                NodeId nodeId = new NodeId(3, "Random");
                // Read a variable
//                ReadValueId readValueId = new ReadValueId(n, Attributes.Value, null,
//                        null);
//                ReadResponse res = mySession.Read(null, 500.0,
//                        TimestampsToReturn.Source, readValueId);
//                DataValue[] dataValue = res.getResults();
//                String result = dataValue[0].getValue().toString();
//                System.out.println("\n\nRead Result:  " + result + "\n\n");


//                Variant x = new Variant(7.20f);
//                Variant y = new Variant(5.44f);
//                Variant theta = new Variant(0.0f);
                //Variant[] v = {x, y, theta};


                CallRequest callRequest = new CallRequest();
                CallMethodRequest methodRequest = new CallMethodRequest();
                callRequest.setMethodsToCall(new CallMethodRequest[] {methodRequest});
                methodRequest.setMethodId(n1);
                methodRequest.setObjectId(n);

                Float cords_x = 4.0f;
                Variant cords_x_InVariant = new Variant(cords_x);
                Float cords_y = 2.0f;
                Variant cords_y_InVariant = new Variant(cords_y);
                Float cords_ang = -45.0f;
                Variant cords_ang_InVariant = new Variant(cords_ang);
                methodRequest.setInputArguments( new Variant[] {cords_x_InVariant, cords_y_InVariant, cords_ang_InVariant});

                System.out.println("Methods to Call: ----- " + callRequest.getMethodsToCall()[0]);
                //System.out.println("Output Arguments: ----------" + methodRequest.getInputArguments()[0]);
                CallResponse res1 = mySession.Call(callRequest);
                System.out.println("Call Response:- " + res1);





                //methodRequest.setObjectId(n);
//                methodRequest.setInputArguments(new Variant[]{x, y, theta});
//                System.out.println("MethodID: " + methodRequest.getMethodId());
//                System.out.println("Lenght of InputArguments: " + methodRequest.getInputArguments().length);
//                System.out.println("ObjectID: " + methodRequest.getObjectId());


//                //CallRequest callRequest = new CallRequest();
//                CallMethodRequest methodRequest = new CallMethodRequest();
//                callRequest.setMethodsToCall(new CallMethodRequest[] {methodRequest});
//                methodRequest.setMethodId(MyServerExample.LIST_SOLVERS);
//                CallResponse res = myChannel.Call(callRequest);
//                System.out.println(res);
//                int l = calres.getInputArgumentResults().length;
//                System.out.println("InputArguments result: ");
//                for(int i =0; i < l; i++){
//                    System.out.println(calres.getInputArgumentResults()[i]);
//                }

                // Write a variable. In this case the same variable read is set to 0
//                WriteValue writeValue = new WriteValue(n, Attributes.Value, null,
//                        new DataValue(new Variant(v)));
//                //System.out.println("Write Value: " + value.toString());
//                WriteResponse wresponse =  mySession.Write(null, writeValue);
//                StatusCode[] wres = wresponse.getResults();
//                System.out.println(wresponse);
//                System.out.println(wres);
//
//
                // Close the session
                mySession.close();
                mySession.closeAsync();
//
                return res1.toString();
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
