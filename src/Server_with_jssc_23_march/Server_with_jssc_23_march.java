/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server_with_jssc_23_march;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.Thread;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.*;
 

public class Server_with_JSSC_23_march {

              public static void main(String[] args) {
                    // get computer serial ports names
                    /*String[] portNames = SerialPortList.getPortNames();
                       for (String port : portNames) {
                        System.out.println(port);
                    }*/
      SerialPort [] serialPorts = new SerialPort[7];
      serialPorts[0] = new SerialPort("COM13"); //floor lamp BB6A
      serialPorts[1] = new SerialPort("COM7"); //ceiling lamp BB69
      //serialPorts[2] = new SerialPort("COMX"); // certain 
      //serialPorts[3] = new SerialPort("COMX"); //ceiling lamp

       SerialPort [] sensorer = new SerialPort[3];
       sensorer[0] = new SerialPort("COM_S1");
       sensorer[1] = new SerialPort("COM_S2");
       sensorer[2] = new SerialPort("COM_S3");

                try {
                    
                    ServerSocket server = new ServerSocket(4444);     
                        try {
                                // open port for communication
                                System.out.println("OPEN PORT 0");
                                serialPorts[0].openPort();
                                System.out.println("OPEN PORT 1");
                                serialPorts[1].openPort();
                                System.out.println("OPEN PORT DONE");
                                //serialPorts[2].openPort();
                                //serialPorts[3].openPort();
                                sensorer[0].openPort();
                                sensorer[1].openPort();
                                sensorer[2].openPort();
                                
                                // baundRate, numberOfDataBits, numberOfStopBits, parity
                                serialPorts[0].setParams(9600, 8, 1, 0);
                                serialPorts[1].setParams(9600, 8, 1, 0);
                                //serialPorts[2].setParams(9600, 8, 1, 0);
                                //serialPorts[3].setParams(9600, 8, 1, 0);
                                
                                sensorer[0].setParams(9600, 8, 1, 0);
                                sensorer[1].setParams(9600, 8, 1, 0);
                                sensorer[2].setParams(9600, 8, 1, 0);
                                
                                SensorThread sensor = new SensorThread(sensorer);
                                sensor.start();
                                
                                // close port
                                //serialPort.closePort();
                             } catch (SerialPortException ex) {
                                System.out.println(ex);
                             }

                         while (true) {
                                         Socket client = server.accept();
                                         EchoHandler handler = new EchoHandler(client,serialPorts);
                                         handler.start();
                                         
                                                }
                                } catch (Exception e) {
                                                System.err.println("Exception caught:" + e);
                                }
                } // main

}//ThreadServer_ContextClient
class SensorThread extends Thread{
ServerSocket server;

 
SerialPort [] sensorsCOM_ports= null;
public SensorThread(SerialPort [] sensorer){
    try {
        this.server = new ServerSocket(9999);
    } catch (IOException ex) {
        Logger.getLogger(SensorThread.class.getName()).log(Level.SEVERE, null, ex);
    }
    sensorsCOM_ports = sensorer;
}
  




@Override
    public void run() {
        while(true){
            try {
                server.accept();
            } catch (IOException ex) {
                Logger.getLogger(SensorThread.class.getName()).log(Level.SEVERE, null, ex);
            }
                //Writes data to port
                //serialPort.writeBytes("Test");
            try {
                //Read the data of 10 bytes. Be careful with the method readBytes(), if the number of bytes in the input buffer
                //is less than you need, then the method will wait for the right amount. Better to use it in conjunction with the
                //interface SerialPortEventListener.

                byte[] buffer = sensorsCOM_ports[0].readBytes(1);//1
            
            } catch (SerialPortException ex) {
                Logger.getLogger(SensorThread.class.getName()).log(Level.SEVERE, null, ex);
            }

                    //output till konsolen pga simulering
                    System.out.println("** SENSOR DATA:"+ sensorsCOM_ports[0].toString());
                    //Closing the port

                //serialPort.closePort();
        }
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}








class EchoHandler extends Thread {
    String userInput = "hrj"; 
    Socket client;
    Socket echoSocket = null; 
    String[] portNames= null;
    SerialPort [] serialPorts = null;
 
    EchoHandler(Socket  client, SerialPort [] ports){
            this.client =  client;   
            serialPorts = ports;
            
    }// end constructor

         public void run(){
            try{
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

                                for (int q = 0; q < token0.length; q++) {
                                    System.out.println(token0[q]);
                                }
                                System.out.println("end of android data................................");
                      //..................... ROOM 1 .......................................
//******************* WORKING ONLY WITH THIS RITGHT NOW 23/3 ********************************************************//
                        if(token0[2].equals("R1")){
                            if(token0[3].equals("FL")){
                                if(token0[4].equals("1")){
                                    System.out.println("Turn ON Floor lamp in Room 1");
                                    serialPorts[0].writeBytes("1".getBytes());
                                }//1 : on
                                else if(token0[4].equals("2")){
                                    System.out.println("Turn OFF Floor lamp in Room 1");
                                    serialPorts[0].writeBytes("2".getBytes());
                                }//2 : off
                           }//FL
                            if(token0[3].equals("CL")){
                                if(token0[4].equals("1")){
                                    System.out.println("Turn ON Ceiling lamp in Room 1");
                                    serialPorts[1].writeBytes("1".getBytes());
                                }//1
                                else if(token0[4].equals("2")){
                                    System.out.println("Turn OFF Ceiling lamp in Room 1");
                                    serialPorts[1].writeBytes("2".getBytes());
                                }//2 : off                            
                            }//CL
//******************* WORKING ONLY WITH THIS RITGHT NOW 23/3********************************************************//

                            if(token0[3].equals("FN")){
                                if(token0[4].equals("1")){
                                    System.out.println("Turn ON Fan in Room 1");
                                    
                                }//1
                                else if(token0[4].equals("2")){
                                    System.out.println("Turn OFF Fan in Room 1");
                                    
                                }//2 : off
                            }//FN
                            if(token0[3].equals("CR")){
                                if(token0[4].equals("1")){
                                    System.out.println("Scroll Up Curtain in Room 1");
                                    
                                }//1
                                else if(token0[4].equals("2")){
                                    System.out.println("Scroll Down Curtain in Room 1");
                                    
                                }//2 : off
                            }//CR
                        }//R1

     System.out.println("end of x coordinates");
                 }//if for android

                              double[] coordinate_y = new double[100];

                            if (mK.find()) {
                                System.out.println("Kinect discovered");
                                String[] token1 = line.split("K| |:|#");
                                System.out.println("kinect data start......................");
                                // print whole index

                                for (int k = 0; k < token1.length; k++) {
                                    System.out.println(token1[k] + ";"); // don't count ;,
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

		if(token1[2].equals("R1")){
			if(token1[3].equals("FL")){
				if(token1[4].equals("1")){
					 System.out.println("Turn ON Floor lamp in Room 1");
					 serialPorts[0].writeBytes("1".getBytes());
                                         
				}//1 : on
				else if(token1[4].equals("2")){
					System.out.println("Turn OFF Floor lamp in Room 1");
					serialPorts[0].writeBytes("2".getBytes());
				}//2 : off			
			}//FL
			if(token1[3].equals("CL")){
				if(token1[4].equals("1")){
					 System.out.println("Turn ON Ceiling lamp in Room 1");
					 serialPorts[1].writeBytes("1".getBytes());
				}//1
				else if(token1[4].equals("2")){
					System.out.println("Turn OFF Ceiling lamp in Room 1");
					serialPorts[1].writeBytes("2".getBytes());
				}//2 : off				
			}//CL
			if(token1[3].equals("FN")){
				if(token1[4].equals("1")){
					 System.out.println("Turn ON Fan in Room 1");
					 
				}//1
				else if(token1[4].equals("2")){
					System.out.println("Turn OFF Fan in Room 1");
					
				}//2 : off			
			}//FN
			if(token1[3].equals("CR")){
				if(token1[4].equals("1")){
					 System.out.println("Scroll Up Curtain in Room 1");
					 
				}//1
				else if(token1[4].equals("2")){
					System.out.println("Scroll Down Curtain in Room 1");
					
				}//2 : off
		        }//CR
	            }//R1
                 }// if for kinect
                 
                System.out.println("HHHHHH 1");

                String[] token0 = line.split("A|K|:| |#"); // == parseInt ; Data

                  }//end while(true)

                } catch (Exception e) {
                    System.err.println("Exception caught: client disconnected.");
                    System.err.println(e.getMessage());
                } finally {
                    try {
                        client.close();
//                        os.close();
  //                      os1.close();
                    } catch (Exception e) {
                        ;
                    }
                }
             }catch (Exception ex) {
                Logger.getLogger(EchoHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

  }//end run

   

}// EchoHandler