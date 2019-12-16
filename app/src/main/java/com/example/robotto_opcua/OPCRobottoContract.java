package com.example.robotto_opcua;

import android.provider.BaseColumns;

public class OPCRobottoContract {
    public static final class Connections implements BaseColumns {
        public static final String CONNECTIONS_TABLE = "connections_table";
        public static final String SERVER_NAME = "server_name";
        public static final String SERVER_URI = "server_uri";
//        public static final String SECURITY = "security";
//        public static final String SECURITY_POLICIES = "seecurity_policies";
    }
}
