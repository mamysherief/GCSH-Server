/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server_with_JSSC_23_march;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.Thread;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.*;

public class Server_with_JSSC_23_march implements Callbackhome {

    Socket contextSocket;
    PrintWriter outToContext;

    String hostName = "194.47.40.119"; // context server adress 194.47.32.117
    int portNumber = 2015;//4444;

    public Server_with_JSSC_23_march() {
        try {
            contextSocket = new Socket(hostName, portNumber);
            outToContext = new PrintWriter(contextSocket.getOutputStream(), true);
        } catch (IOException ex) {
            Logger.getLogger(Server_with_JSSC_23_march.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   // @Override
    synchronized public void prt_to_context(String value) {
        outToContext.println(value);
    }

    public static void main(String[] args) {
        // get computer serial ports names
        /*String[] portNames = SerialPortList.getPortNames();
                       for (String port : portNames) {
                        System.out.println(port);
                    }*/
        Callbackhome call = new Server_with_JSSC_23_march();

        SerialPort[] serialPorts = new SerialPort[7];
        serialPorts[0] = new SerialPort("COM41"); //floor lamp BB6A
        serialPorts[1] = new SerialPort("COM45"); //ceiling lamp BB69
        serialPorts[2] = new SerialPort("COM46"); // certain 
        serialPorts[3] = new SerialPort("COM47"); //fan

        SerialPort[] sensorer = new SerialPort[3];
        //sensorer[0] = new SerialPort("COM48"); //Christians sensor
        sensorer[1] = new SerialPort("COM42");// Sensor 1 Humidity+Temperature
        sensorer[2] = new SerialPort("COM43"); // Sensor 2 CO2+ temperature

//        String hostName = "194.47.32.117"; // context server adress
//        int portNumber = 4444;
//        Socket contextSocket;
//        PrintWriter outToContext = null;
        try {

            ServerSocket server = new ServerSocket(4444);

            //outToContext = new PrintWriter(contextSocket.getOutputStream(), true);
            try {
                // open port for communication
                System.out.println("Adding floor lamp");
                serialPorts[0].openPort();
                System.out.println("Adding Celing lamp");
                serialPorts[1].openPort();
                System.out.println("Adding curtain");
                serialPorts[2].openPort();
                System.out.println("Adding fan");
                serialPorts[3].openPort();
          //      System.out.println("Adding break point sensor");
            //    sensorer[0].openPort();
                System.out.println("Adding Humidity and Temperature sensor");
                sensorer[1].openPort();
                System.out.println("Adding CO2 sensor");
                sensorer[2].openPort();
                System.out.println("OPEN SERIAL PORT -PORT DONE");

                // baundRate, numberOfDataBits, numberOfStopBits, parity
                serialPorts[0].setParams(9600, 8, 1, 0);
                serialPorts[1].setParams(9600, 8, 1, 0);
                serialPorts[2].setParams(9600, 8, 1, 0);
                serialPorts[3].setParams(9600, 8, 1, 0);

              //  sensorer[0].setParams(9600, 8, 1, 0);
                sensorer[1].setParams(9600, 8, 1, 0);
                sensorer[2].setParams(9600, 8, 1, 0);

                SensorThread sensor = new SensorThread(sensorer, call);
                sensor.start();
                System.out.println("Waiting for action");

                // close port
                //serialPort.closePort();
            } catch (SerialPortException ex) {
                System.out.println(ex);
            }

            while (true) {
                Socket client = server.accept();
                System.out.println("Client attached");
                EchoHandler handler = new EchoHandler(client, serialPorts, call);
                handler.start();

            }
        } catch (Exception e) {
            System.err.println("Exception caught:" + e);
        }
    } // main

}//ThreadServer_ContextClient

class SensorThread extends Thread {

    ServerSocket server;
    SerialPort[] sensorsCOM_ports = null;
    int nbr_in_room_A = 0;
    int nbr_in_room_B = 0;
    Callbackhome call;

    Socket contextSocket;
    PrintWriter outToContext = null;

    public SensorThread(SerialPort[] sensorer, Callbackhome call) {
        this.call = call;
        try {
            this.server = new ServerSocket(9999);
        } catch (IOException ex) {
            Logger.getLogger(SensorThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        sensorsCOM_ports = sensorer;
    }

    @Override
    public void run() {

        /**
         * *********** Christian Sensor ***************************
         */
        byte[] buffer = null;

        /**
         * *********** Sensor 1 ***************************
         */
        String[] read_string_array_from_input_sensor_1 = null;
        String[] array_from_input_sensor_1 = new String[12];
        int counter_sensor_1 = 0;
        int num_sensor_1 = 0;

        /**
         * *********** Sensor 2 ***************************
         */
        String[] read_string_array_from_input_sensor_2 = null;
        String[] array_from_input_sensor_2 = new String[12];
        int counter_sensor_2 = 0;
        int num_sensor_2 = 0;

        /**
         * ************ end sensors **************************
         */
        while (true) {

            //System.out.println("** SENSOR THREAD ACCEPT, IN RUN():*************************************");
            try {
                /**
                 * **************** START CHRISTIAN SENSOR ********************
                 */
//                buffer = sensorsCOM_ports[0].readBytes(1);//1
//                if (buffer[0] == 'A') {
//                    System.out.println("A");
//                    ++nbr_in_room_A;
//                    System.out.println("NBR IN ROOM A:" + nbr_in_room_A);
//                    if (nbr_in_room_B > 0) {
//                        --nbr_in_room_B;
//                    }
//                }
//                if (buffer[0] == 'B') {
//                    System.out.println("B");
//                    ++nbr_in_room_B;
//                    System.out.println("NBR IN ROOM B:" + nbr_in_room_B);
//                    if (nbr_in_room_A > 0) {
//                        --nbr_in_room_A;
//                    }
//                }
//                call.prt_to_context("R1SENSORBP" + nbr_in_room_A + "#");
//                call.prt_to_context("R2SENSORBP" + nbr_in_room_B + "#");

                /**
                 * **************** END CHRISTIAN SENSOR ********************
                 */
                /**
                 * **************** START COMPORT 1 ********************
                 */
                int x_sensor_1 = 0;
                int y_sensor_1 = 0;
                int x_temp_sensor_1 = 0;
                int y_temp_sensor_1 = 0;
                read_string_array_from_input_sensor_1 = sensorsCOM_ports[1].readHexStringArray(1);//

                array_from_input_sensor_1[counter_sensor_1] = read_string_array_from_input_sensor_1[0];
                counter_sensor_1++;
                if (counter_sensor_1 == 12) {
                    for (num_sensor_1 = 0; num_sensor_1 < 10; num_sensor_1++) {
                        if (array_from_input_sensor_1[num_sensor_1].equals("52")) {
                            x_sensor_1 = Integer.valueOf(array_from_input_sensor_1[num_sensor_1 + 1], 16);
                            y_sensor_1 = Integer.valueOf(array_from_input_sensor_1[num_sensor_1 + 2], 16);
                        }
                        if (array_from_input_sensor_1[num_sensor_1].equals("54")) {
                            x_temp_sensor_1 = Integer.valueOf(array_from_input_sensor_1[num_sensor_1 + 1], 16);
                            y_temp_sensor_1 = Integer.valueOf(array_from_input_sensor_1[num_sensor_1 + 2], 16);
                        }
                    }
                    counter_sensor_1 = 0;

                    // System.out.println(Arrays.toString(array_from_input_sensor_2));
                    //System.out.print(x_sensor_1);
                    //System.out.print(y_sensor_1); 
                    /*int humidity1 =x_sensor_1*256+y_sensor_1;
                int temp_sensor_1 = x_temp_sensor_1*256+y_temp_sensor_1;*/
                    double humidityEquation = (x_sensor_1 * 256 + y_sensor_1) / 1024.0 * 100;
                    double temperatureEquation = ((x_temp_sensor_1 * 256 + y_temp_sensor_1) / 1024.0 * 108 - 32 - 9) / 1.8;

                    System.out.println("Humidity SENSOR 1: " + humidityEquation);
                    call.prt_to_context("Humidity SENSOR 1: " + humidityEquation);

                    System.out.println("Temperature SENSOR 1: " + temperatureEquation);
                    call.prt_to_context("Temperature SENSOR 1:" + temperatureEquation);

                }
                /**
                 * **************** END COMPORT 1 ********************
                 */
                /**
                 * **************** START COMPORT 2 ********************
                 */
                int x_sensor_2 = 0;
                int y_sensor_2 = 0;
                int x_temp_sensor_2 = 0;
                int y_temp_sensor_2 = 0;
                read_string_array_from_input_sensor_2 = sensorsCOM_ports[2].readHexStringArray(1);// Read CO2 and temperature for sensor_2

                array_from_input_sensor_2[counter_sensor_2] = read_string_array_from_input_sensor_2[0];
                counter_sensor_2++;
                if (counter_sensor_2 == 12) {
                    for (num_sensor_2 = 0; num_sensor_2 < 10; num_sensor_2++) {
                        if (array_from_input_sensor_2[num_sensor_2].equals("43")) {
                            x_sensor_2 = Integer.valueOf(array_from_input_sensor_2[num_sensor_2 + 1], 16);
                            y_sensor_2 = Integer.valueOf(array_from_input_sensor_2[num_sensor_2 + 2], 16);
                        }
                        if (array_from_input_sensor_2[num_sensor_2].equals("54")) {
                            x_temp_sensor_2 = Integer.valueOf(array_from_input_sensor_2[num_sensor_2 + 1], 16);
                            y_temp_sensor_2 = Integer.valueOf(array_from_input_sensor_2[num_sensor_2 + 2], 16);
                        }

                    }
                    counter_sensor_2 = 0;

                    /* int humidity_sensor_2 =x_sensor_2*256+y_sensor_2;
                int temp_sensor_2 = x_temp_sensor_2*256+y_temp_sensor_2; */
                    double CO2_Equation_sensor_2 = ((x_sensor_2 * 256 + y_sensor_2) / 1024.0 * 2000);
                    double temperatureEquation_sensor2 = ((x_temp_sensor_2 * 256 + y_temp_sensor_2) / 1024.0 * 108 - 32) / 1.8;

                    System.out.println("CO2_Equation:" + CO2_Equation_sensor_2);
                    call.prt_to_context("SENSOR0FC" + CO2_Equation_sensor_2);
                    System.out.println("Temperature SENSOR 2: " + temperatureEquation_sensor2);
                    call.prt_to_context("SENSOR0FT" + temperatureEquation_sensor2);

                }
                /**
                 * **************** END COMPORT 2 ********************
                 */

            } catch (SerialPortException ex) {

                Logger.getLogger(SensorThread.class.getName()).log(Level.SEVERE, null, ex);
            }

            //Closing the port
            //serialPort.closePort();
        }// 

        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /*@Override
    public void serialEvent(SerialPortEvent event) {
        if(event.isRXCHAR()&& event.getEventValue()>0){
        
            try {
                String received_data = sensorsCOM_ports[1].readString(event.getEventValue());
                System.out.println("Received response:"+received_data);
            } catch (SerialPortException ex) {
                Logger.getLogger(SensorThread.class.getName()).log(Level.SEVERE, null, ex);
            }
       
        }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/
    class PortReader implements SerialPortEventListener {

        @Override
        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    String received_data = sensorsCOM_ports[1].readString(event.getEventValue());
                    System.out.println("Received response:" + received_data);
                } catch (SerialPortException e) {
                    System.out.println("Error in receiving string");
                }
            }
        }
    }

}

class EchoHandler extends Thread {

    String userInput = "hrj";
    Socket client;
    Socket echoSocket = null;
    String[] portNames = null;
    SerialPort[] serialPorts = null;
    Callbackhome call;

    long startTimeH = System.nanoTime();

    Socket contextSocket;
    PrintWriter outToContext = null;

    EchoHandler(Socket client, SerialPort[] ports, Callbackhome call) {
        this.call = call;
        this.client = client;
        serialPorts = ports;
        System.out.println("** this.server = new ServerSocket(9999); *****");

    }// end constructor
    long stopTimeH = System.nanoTime();
    long durationH = stopTimeH - startTimeH;

    //ceiling lamp boolean values for 4 different lamps
    boolean lampA = false;
    boolean lampB = false;
    boolean lampC = false;
    boolean lampD = false;
    boolean rotate = false;
    boolean allLamps = false;

    @Override
    public void run() {
        try {
            System.out.println("durationH " + durationH);

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        client.getInputStream()));

                while (true) {

                    System.out.println("Android discovered");
                    String line = reader.readLine(); // enter
                    String REGEX_Android = "A"; // A: !!
                    String REGEX_Kinect = "K";

                    Pattern pA = Pattern.compile(REGEX_Android);
                    Matcher mA = pA.matcher(line);

                    Pattern pK = Pattern.compile(REGEX_Kinect);
                    Matcher mK = pK.matcher(line);

                    if (mA.find()) {
                        System.out.println("Android discovered");
                        String[] token0 = line.split("A|:| |#");
                        System.out.println("android data start......................");

                        for (String token01 : token0) {
                            System.out.println(token01);
                        }
                        System.out.println("end of android data................................");
                        //..................... ROOM 1 .......................................
//******************* WORKING ONLY WITH THIS RITGHT NOW 23/3 ********************************************************//
                        long startTimeC_Send = System.nanoTime();
                        if (token0[2].equals("R1")) {
                            if (token0[3].equals("FL")) {
                                if (token0[4].equals("1")) {
                                    System.out.println("Turn ON Floor lamp in Room 1");
                                    serialPorts[0].writeBytes("1".getBytes());
                                    call.prt_to_context("R1FLON");
                                    // outToContext.println("R1FLON");
                                }//1 : on
                                else if (token0[4].equals("2")) {
                                    System.out.println("Turn OFF Floor lamp in Room 1");
                                    serialPorts[0].writeBytes("2".getBytes());
//                                    outToContext.println("R1FLOFF");
                                    call.prt_to_context("R1FLOFF");

                                }//2 : off
                            }//FL
                            if (token0[3].equals("CL")) {
//                                
                                if (token0[4].equals("1")) {
                                    if (!lampA) {
                                        System.out.println("Turn ON Ceiling lampA in Room 1");
                                        serialPorts[1].writeBytes("1".getBytes());
                                        //outToContext.println("A1");
                                        call.prt_to_context("R1CLA1");
                                        lampA = true;
                                    } else {
                                        System.out.println("Turn OFF Ceiling lampA in Room 1");
                                        serialPorts[1].writeBytes("1".getBytes());
                                        //outToContext.println("A0");
                                        call.prt_to_context("R1CLA0");
                                        lampA = false;
                                    }
                                }//Lamp A
                                if (token0[4].equals("2")) {
                                    if (!lampB) {
                                        System.out.println("Turn ON Ceiling lampB in Room 1");
                                        serialPorts[1].writeBytes("2".getBytes());
                                        //   outToContext.println("B1");
                                        call.prt_to_context("R1CLB1");
                                        lampB = true;
                                    } else {
                                        System.out.println("Turn OFF Ceiling lampB in Room 1");
                                        serialPorts[1].writeBytes("2".getBytes());
                                        //   outToContext.println("B0");
                                        call.prt_to_context("R1CLB0");
                                        lampB = false;
                                    }
                                }//Lamp B 
                                if (token0[4].equals("3")) {
                                    if (!lampC) {
                                        System.out.println("Turn ON Ceiling lampC in Room 1");
                                        serialPorts[1].writeBytes("3".getBytes());
                                        //outToContext.println("C1");
                                        call.prt_to_context("R1CLC1");
                                        lampC = true;
                                    } else {
                                        System.out.println("Turn OFF Ceiling lampC in Room 1");
                                        serialPorts[1].writeBytes("3".getBytes());
                                        // outToContext.println("C0");
                                        call.prt_to_context("R1CLC0");
                                        lampC = false;
                                    }
                                }//Lamp C
                                if (token0[4].equals("4")) {
                                    if (!lampD) {
                                        System.out.println("Turn ON Ceiling lampD in Room 1");
                                        serialPorts[1].writeBytes("4".getBytes());
                                        //outToContext.println("D1");
                                        call.prt_to_context("R1CLD1");
                                        lampD = true;
                                    } else {
                                        System.out.println("Turn OFF Ceiling lampD in Room 1");
                                        serialPorts[1].writeBytes("4".getBytes());
                                        //outToContext.println("D0");
                                        call.prt_to_context("R1CLD0");
                                        lampD = false;
                                    }
                                }//Lamp D
                                if (token0[4].equals("5")) {
                                    if (!allLamps) {
                                        System.out.println("Turn ON Ceiling lamps in Room 1");
                                        serialPorts[1].writeBytes("5".getBytes());
                                        call.prt_to_context("R1CLON");
                                        allLamps = true;
                                    } else {
                                        System.out.println("Turn OFF ALL Ceiling lamps in Room 1");
                                        serialPorts[1].writeBytes("5".getBytes());
                                        call.prt_to_context("R1CLOFF");
                                        allLamps = false;
                                    }
                                }// All LAMPS
                            }//CL
//******************* WORKING ONLY WITH THIS RITGHT NOW 23/3********************************************************//

                            if (token0[3].equals("FN")) {
                                if (token0[4].equals("1")) {
                                    System.out.println("Turn ON Fan in Room 1");
                                    serialPorts[3].writeBytes("1".getBytes());
                                    call.prt_to_context("R1FNON");

                                }//1
                                else if (token0[4].equals("2")) {
                                    System.out.println("Turn OFF Fan in Room 1");
                                    serialPorts[3].writeBytes("2".getBytes());
                                    call.prt_to_context("R1FNOFF");

                                }//2 : off
                                if (token0[4].equals("3")) {
                                    System.out.println("Speed up Fan in Room 1");
                                    serialPorts[3].writeBytes("3".getBytes());
                                    call.prt_to_context("R1FNSPEEDUP");

                                }//1
                                if (token0[4].equals("4")) {
                                    System.out.println("Speed down Fan in Room 1");
                                    serialPorts[3].writeBytes("4".getBytes());
                                    call.prt_to_context("R1FNSPEEDDOWN");
                                }//1
                                if (token0[4].equals("5")) {
                                    if (!rotate) {
                                        System.out.println("Rotation ON Fan in Room 1");
                                        serialPorts[3].writeBytes("5".getBytes());
                                        rotate = true;
                                        call.prt_to_context("R1FNROTATEON");
                                    } else {
                                        System.out.println("Rotation OFF Fan in Room 1");
                                        serialPorts[3].writeBytes("5".getBytes());
                                        rotate = false;
                                        call.prt_to_context("R1FNROTATEOFF");
                                    }
                                }
                            }//FN
                            if (token0[3].equals("CR")) {
                                switch (token0[4]) {
                                //1
                                    case "1":
                                        System.out.println("Scroll Up Curtain in Room 1");
                                        serialPorts[2].writeBytes("1".getBytes()); //added 2016.03.30........................................
                                        //outToContext.println("CURTAINUP");
                                        call.prt_to_context("CURTAINDOWN");
                                        break;
                                    case "2":
                                        System.out.println("Stop Curtain in Room 1");
                                        serialPorts[2].writeBytes("2".getBytes()); //added 2016.03.30........................................   
                                        //outToContext.println("CURTAINSTOP");
                                        call.prt_to_context("CURTAINSTOP");
                                        break;
                                //2 : off
                                    case "3":
                                        //added 2016.03.30........................................
                                        System.out.println("Scroll Down Curtain in Room 1");
                                        serialPorts[2].writeBytes("3".getBytes());
                                        //outToContext.println("CURTAINDOWN");
                                        call.prt_to_context("CURTAINUP");
                                        break;
                                    default:
                                        break;
                                }
                            }//CR
                        }//R1
                        long stopTimeC_Send = System.nanoTime();
                        long durationC_Send = stopTimeC_Send - startTimeC_Send;
                        System.out.println("durationC_Send " + durationC_Send);

                        System.out.println("end of x coordinates");
                    }//if for android

                    double[] coordinate_y = new double[100];

                    if (mK.find()) {
                        System.out.println("Kinect discovered");
                        String[] token1 = line.split("K| |:|#");
                        System.out.println("kinect data start......................");
                        // print whole index
                        for (String token11 : token1) {
                            System.out.println(token11 + ";"); // don't count ;,
                        }

                        System.out.println("end of Kinect data.................................");
                        System.out.println("***********************");
                        System.out.println("x");

                        for (int v = 6; v < token1.length; v += 6) {
                            System.out.println(token1[v]);
                            coordinate_y[v + 1] = Double.parseDouble(token1[v + 1]);
                            System.out.println(token1[v + 1]);
                        }// for

                        //..................... ROOM 1 .......................................
                        if (token1[2].equals("R1")) {
                            if (token1[3].equals("FL")) {
                                if (token1[4].equals("1")) {
                                    System.out.println("Turn ON Floor lamp in Room 1");
                                    serialPorts[0].writeBytes("1".getBytes());
                                    call.prt_to_context("R1FLON");

                                }//1 : on
                                else if (token1[4].equals("2")) {
                                    System.out.println("Turn OFF Floor lamp in Room 1");
                                    serialPorts[0].writeBytes("2".getBytes());
                                    call.prt_to_context("R1FLOFF");
                                }//2 : off			
                            }//FL
                            if (token1[3].equals("CL")) {
                                if (token1[4].equals("1")) {
                                    if (!lampA) {
                                        System.out.println("Turn ON Ceiling lampA in Room 1");
                                        serialPorts[1].writeBytes("1".getBytes());
                                        //outToContext.println("A1");
                                        call.prt_to_context("R1CLA1");
                                        lampA = true;
                                    } else {
                                        System.out.println("Turn OFF Ceiling lampA in Room 1");
                                        serialPorts[1].writeBytes("1".getBytes());
                                        //  outToContext.println("A0");
                                        call.prt_to_context("R1CLA0");
                                        lampA = false;
                                    }
                                }//Lamp A
                                if (token1[4].equals("2")) {
                                    if (!lampB) {
                                        System.out.println("Turn ON Ceiling lampB in Room 1");
                                        serialPorts[1].writeBytes("2".getBytes());
                                        //outToContext.println("B1");
                                        call.prt_to_context("R1CLB1");
                                        lampB = true;
                                    } else {
                                        System.out.println("Turn OFF Ceiling lampB in Room 1");
                                        serialPorts[1].writeBytes("2".getBytes());
                                        //outToContext.println("B0");
                                        call.prt_to_context("R1CLB0");
                                        lampB = false;
                                    }
                                }//Lamp B 
                                if (token1[4].equals("3")) {
                                    if (!lampC) {
                                        System.out.println("Turn ON Ceiling lampC in Room 1");
                                        serialPorts[1].writeBytes("3".getBytes());
                                        //outToContext.println("C1");
                                        call.prt_to_context("R1CLC1");
                                        lampC = true;
                                    } else {
                                        System.out.println("Turn OFF Ceiling lampC in Room 1");
                                        serialPorts[1].writeBytes("3".getBytes());
                                        // outToContext.println("C0");
                                        call.prt_to_context("R1CLC0");
                                        lampC = false;
                                    }
                                }//Lamp C
                                if (token1[4].equals("4")) {
                                     if (!lampD) {
                                        System.out.println("Turn ON Ceiling lampD in Room 1");
                                        serialPorts[1].writeBytes("4".getBytes());
                                        //outToContext.println("D1");
                                        call.prt_to_context("R1CLD1");
                                        lampD = true;
                                    } else {
                                        System.out.println("Turn OFF Ceiling lampD in Room 1");
                                        serialPorts[1].writeBytes("4".getBytes());
                                        //outToContext.println("D0");
                                        call.prt_to_context("R1CLD0");
                                        lampD = false;
                                    }
                                }//Lamp D
                                if (token1[4].equals("5")) {
                                    if (!allLamps) {
                                        System.out.println("Turn ON Ceiling lamps in Room 1");
                                        serialPorts[1].writeBytes("5".getBytes());
                                        call.prt_to_context("R1CLON");
                                        allLamps = true;
                                    } else {
                                        System.out.println("Turn OFF ALL Ceiling lamps in Room 1");
                                        serialPorts[1].writeBytes("5".getBytes());
                                        call.prt_to_context("R1CLOFF");
                                        allLamps = false;
                                    }
                                }// All LAMPS
                            }//CL
                            if (token1[3].equals("FN")) {
                                if (token1[4].equals("1")) {
                                    System.out.println("Turn ON Fan in Room 1");
                                    serialPorts[3].writeBytes("1".getBytes());
                                    call.prt_to_context("R1FNON");

                                }//1
                                else if (token1[4].equals("2")) {
                                    System.out.println("Turn OFF Fan in Room 1");
                                    serialPorts[3].writeBytes("2".getBytes());
                                    call.prt_to_context("R1FNOFF");

                                }//2 : off
                                if (token1[4].equals("3")) {
                                    System.out.println("Speed up Fan in Room 1");
                                    serialPorts[3].writeBytes("3".getBytes());
                                    call.prt_to_context("R1FNSPEEDUP");

                                }//1
                                if (token1[4].equals("4")) {
                                    System.out.println("Speed down Fan in Room 1");
                                    serialPorts[3].writeBytes("4".getBytes());
                                    call.prt_to_context("R1FNSPEEDDOWN");

                                }//1
                                if (token1[4].equals("5")) {
                                    if (!rotate) {
                                        System.out.println("Rotation ON Fan in Room 1");
                                        serialPorts[3].writeBytes("5".getBytes());
                                        rotate = true;
                                        call.prt_to_context("R1FNROTATEON");
                                    } else {
                                        System.out.println("Rotation OFF Fan in Room 1");
                                        serialPorts[3].writeBytes("5".getBytes());
                                        rotate = false;
                                        call.prt_to_context("R1FNROTATEOFF");
                                    }
                                }//1
                            }//FN
                            if (token1[3].equals("CR")) {
                                switch (token1[4]) {
                                //1
                                    case "1":
                                        System.out.println("Scroll Up Curtain in Room 1");
                                        serialPorts[2].writeBytes("1".getBytes()); //added 2016.03.30........................................
                                        //outToContext.println("CURTAINUP");
                                        call.prt_to_context("CURTAINDOWN");
                                        break;
                                    case "2":
                                        System.out.println("Stop Curtain in Room 1");
                                        serialPorts[2].writeBytes("2".getBytes()); //added 2016.03.30........................................   
                                        //outToContext.println("CURTAINSTOP");
                                        call.prt_to_context("CURTAINSTOP");
                                        break;
                                //2 : off
                                    case "3":
                                        //added 2016.03.30........................................
                                        System.out.println("Scroll Down Curtain in Room 1");
                                        serialPorts[2].writeBytes("3".getBytes());
                                        //outToContext.println("CURTAINDOWN");
                                        call.prt_to_context("CURTAINUP");
                                        break;
                                    default:
                                        break;
                                }
                            }//CR
                        }//R1
                    }// if for kinect

                    System.out.println("HHHHHH 1");

                    String[] token0 = line.split("A|K|:| |#"); // == parseInt ; Data

                }//end while(true)

            } catch (IOException | SerialPortException | NumberFormatException e) {
                System.err.println("Exception caught: client disconnected.");
                System.err.println(e.getMessage());
            } finally {
                try {
                    client.close();
//                        os.close();
                    //                      os1.close();
                } catch (Exception e) {
                    
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(EchoHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//end run

}// EchoHandler

