package com.example.robotto_opcua;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.opcfoundation.ua.application.Application;
import org.opcfoundation.ua.application.Client;
import org.opcfoundation.ua.application.SessionChannel;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.core.ApplicationType;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.EndpointDescription;
import org.opcfoundation.ua.core.MessageSecurityMode;
import org.opcfoundation.ua.core.ReadResponse;
import org.opcfoundation.ua.core.ReadValueId;
import org.opcfoundation.ua.core.TimestampsToReturn;
import org.opcfoundation.ua.core.WriteResponse;
import org.opcfoundation.ua.core.WriteValue;
import org.opcfoundation.ua.transport.SecureChannel;
import org.opcfoundation.ua.transport.ServiceChannel;
import org.opcfoundation.ua.transport.security.KeyPair;
import org.opcfoundation.ua.transport.security.SecurityPolicy;
import org.opcfoundation.ua.utils.CertificateUtils;
import org.opcfoundation.ua.utils.EndpointUtil;
import org.w3c.dom.Text;

import java.security.Security;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements
        AddConnectionDialog.AddConnectionDialogListener {

    TextView textView;
    ListView connection_list;
    LinearLayout AddConnectionbtn;
    boolean Run;
    String[] sName, sURI;
    Integer NumberOfConnections;
    DatabaseHelper opcRobotto_db;
    SQLiteDatabase robottodb;

    // Bouncy Castle encryption
    static { Security.insertProviderAt(new
            org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AddConnectionbtn = findViewById(R.id.AddConnectlayout);
        connection_list = findViewById(R.id.connections);
        textView = findViewById(R.id.textview);

        opcRobotto_db = new DatabaseHelper(this);
        robottodb = opcRobotto_db.getWritableDatabase();

        Cursor data = opcRobotto_db.getConnectionList();
        NumberOfConnections = data.getCount();
        ArrayList<String> alName = new ArrayList<>();
        ArrayList<String> alURI = new ArrayList<>();

        if(NumberOfConnections == 0){
            textView.setText("No Connections Available.");
        }
        else{
            while(data.moveToNext()){
                alName.add(data.getString(1));
                alURI.add(data.getString(2));
            }
            sName = alName.toArray(new String[alName.size()]);
            sURI = alURI.toArray(new String[alURI.size()]);
            listAdapter adapter = new listAdapter(this, sName, sURI);
            connection_list.setAdapter(adapter);
        }

        connection_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int i = 0; i < NumberOfConnections; i++){
                    if(position == i){
                        Toast.makeText(MainActivity.this, sName[position], Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        AddConnectionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

    }

    class listAdapter extends ArrayAdapter<String>{
        Context context;
        String[] rTitle;
        String[] rSubtitle;

        listAdapter (Context context, String[] title, String[] subtitle){
            super(context, R.layout.connections_list, R.id.srvName, title);
            this.context = context;
            this.rTitle = title;
            this.rSubtitle = subtitle;
        }

        @NonNull
        @Override
        public View getView(int Position, @Nullable View convertView, @NonNull ViewGroup parent){
            LayoutInflater layoutinflater = (LayoutInflater)getApplicationContext().getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View connection_list = layoutinflater.inflate(R.layout.connections_list, parent, false);
            TextView srvName = connection_list.findViewById(R.id.srvName);
            TextView srvURI = connection_list.findViewById(R.id.srvURI);

            srvName.setText(rTitle[Position]);
            srvURI.setText(rSubtitle[Position]);

            return connection_list;
        }
    }

    public void openDialog(){
        AddConnectionDialog addConnectionDialog = new AddConnectionDialog();
        addConnectionDialog.show(getSupportFragmentManager(), "Add New Connection");

    }

    @Override
    public void getData(String ServerName, String ServerURI) {
        boolean isInserted = opcRobotto_db.newconnectiondata(ServerName, ServerURI);
        if(isInserted)
            Toast.makeText(MainActivity.this, "New Connection added", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(MainActivity.this, "Can't add New Connection", Toast.LENGTH_LONG).show();

    }

    private class ConnectionAsyncTask extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... strings) {

            // Declare Entities
//            Application myClientApplication = new Application();
//            System.out.println("myClientApplication: " + myClientApplication);
//            KeyPair myClientApplicationInstanceCertificate;
//            String certificateCommonName = "Android_Client";
//            System.out.println("Generating new Certificate for Client using CN: " +
//                    certificateCommonName);
//            String applicationUri = myClientApplication.getApplicationUri();
//            System.out.println("Application URI: " + applicationUri);
//            String organisation = "Sample Organisation";
//            int validityTime = 3650;
//
//            // Create Client Application Instance Certificate
//            try {
//                myClientApplicationInstanceCertificate =
//                        CertificateUtils.createApplicationInstanceCertificate(certificateCommonName,
//                                organisation, applicationUri, validityTime);
//
//                //Create Client
//                Client myClient = new Client(myClientApplication);
//                myClientApplication.addApplicationInstanceCertificate(
//                            myClientApplicationInstanceCertificate);

            // Create Application Description
            ApplicationDescription applicationDescription = new ApplicationDescription();
            applicationDescription.setApplicationName(new LocalizedText("AndroidClient",
                    Locale.ENGLISH));
            applicationDescription.setApplicationUri(
                    "urn:localhost:fd8d1dac-7224-4054-9592-1d944e1f4de6");
            //applicationDescription.setProductUri("urn:prosysopc.com:OPCUA:SimulationServer");
            applicationDescription.setApplicationType(ApplicationType.Client);

            //Create Client Application Instance Certificate
            try {
                KeyPair myClientApplicationInstanceCertificate = ExampleKeys.getCert(
                        getApplicationContext(), applicationDescription);

                //Create Client
                Client myClient = Client.createClientApplication(
                        myClientApplicationInstanceCertificate);

                // Discover endpoints
                EndpointDescription[] endpoints = myClient.discoverEndpoints(
                        "opc.tcp://10.0.2.2:53530/OPCUA/SimulationServer");

                // Filter out all but opc.tcp protocol endpoints
                endpoints = EndpointUtil.selectByProtocol(endpoints, "opc.tcp");

                // Filter out all but Signed & Encrypted endpoints
                endpoints = EndpointUtil.selectByMessageSecurityMode(endpoints,
                        MessageSecurityMode.None);
//
                // Filter out all but Basic256Sha256 encryption endpoints
//                endpoints = EndpointUtil.selectBySecurityPolicy(endpoints,
//                        SecurityPolicy.BASIC256SHA256);
                // Sort endpoints by security level. The lowest level at the beginning,
                // the highest at the end of the array
                endpoints = EndpointUtil.sortBySecurityLevel(endpoints);
                // Choose one endpoint.
                //EndpointDescription endpoint = endpoints[0];

                // Choose one endpoint.
                EndpointDescription endpoint = endpoints[endpoints.length - 1];
                System.out.println("Endpoint Selected ");
                endpoint.setEndpointUrl("opc.tcp://10.0.2.2:53530/OPCUA/SimulationServer");
                //System.out.println("new Endpoint url: " + endpoint.getEndpointUrl());
//
//                System.out.println("Security Level " + endpoint.getSecurityPolicyUri());
//                System.out.println("Security Mode " + endpoint.getSecurityMode());
//
//
                //Create the session from the chosen endpoint
                SessionChannel mySession = myClient.createSessionChannel(endpoint);
//                System.out.println("Session Channel: " + mySession);
//                // Activate the session.
                mySession.activate("Android_Client", "pass@345");
//
                // Read and write variables here
                NodeId nodeId = new NodeId(3, "Counter");
                // Read a variable
                ReadValueId readValueId = new ReadValueId(nodeId, Attributes.Value, null,
                        null);
                ReadResponse res = mySession.Read(null, 500.0,
                        TimestampsToReturn.Source, readValueId);
                DataValue[] dataValue = res.getResults();
                String result = dataValue[0].getValue().toString();

//                // Write a variable. In this case the same variable read is set to 0
//                WriteValue writeValue = new WriteValue(nodeId, Attributes.Value, null,
//                        new DataValue(new Variant(0)));
//                WriteResponse wresponse =  mySession.Write(null, writeValue);
//                System.out.println(wresponse);


                // Close the session
                mySession.close();
                mySession.closeAsync();

                return result;
            } catch (Exception e){
                e.printStackTrace();
                return "Connection Failed!!";
            }
        }

        @Override
        protected void onPostExecute(String result){
            textView.setText(result);
            if (Run){
                new ConnectionAsyncTask().execute();
            }
        }
    }

}