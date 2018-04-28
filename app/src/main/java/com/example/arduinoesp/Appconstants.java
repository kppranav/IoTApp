package com.example.arduinoesp;

/**
 * Created by uvionics on 1/2/16.
 */
public interface Appconstants {
    //String IP = "192.168.4.1";
    String IP = "iotnodeapp.mybluemix.net:80"; //iotnodeapp.mybluemix.net:80 //192.168.0.132:8084
    String URL = "http://" + IP + "/switch_status";

    String FEEDBACK_URL = "http://" + IP + "/getswitch_status";

}
