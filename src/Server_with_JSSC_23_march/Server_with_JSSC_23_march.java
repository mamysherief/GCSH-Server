/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_with_jssc_23_march;

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
import java.util.Arrays;
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
      //serialPorts[1] = new SerialPort("COM7"); //ceiling lamp BB69
      //serialPorts[2] = new SerialPort("COMX"); // certain 
      //serialPorts[3] = new SerialPort("COMX"); //ceiling lamp

       SerialPort [] sensorer = new SerialPort[3];
       //sensorer[0] = new SerialPort("COM40");
       sensorer[1] = new SerialPort("COM42");
       sensorer[2] = new SerialPort("COM43");

                try {
                    
                    ServerSocket server = new ServerSocket(4444);     
                        try {
                                // open port for communication
                                //System.out.println("OPEN PORT 0");
                                //serialPorts[0].openPort();
                                //System.out.println("OPEN PORT 1");
                                //serialPorts[1].openPort();
                                
                                //serialPorts[2].openPort();
                                //serialPorts[3].openPort();
                                //sensorer[0].openPort();
                                System.out.println("OPEN SENSOR-PORT DONE");
                                sensorer[1].openPort();
                                sensorer[2].openPort();
                                
                                // baundRate, numberOfDataBits, numberOfStopBits, parity
                                //serialPorts[0].setParams(9600, 8, 1, 0);
                                //serialPorts[1].setParams(9600, 8, 1, 0);
                                //serialPorts[2].setParams(9600, 8, 1, 0);
                                //serialPorts[3].setParams(9600, 8, 1, 0);
                                
                                //sensorer[0].setParams(9600, 8, 1, 0);
                                sensorer[1].setParams(9600, 8, 1, 0);
                                sensorer[2].setParams(9600, 8, 1, 0);
                                
                                SensorThread sensor = new SensorThread(sensorer);
                                  System.out.println("** SensorThread sensor = new SensorThread(sensorer);");
                                sensor.start();
                                  System.out.println("** sensor.start();");

                                
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

class SensorThread extends Thread {
ServerSocket server;
SerialPort [] sensorsCOM_ports= null;
int nbr_in_room_A = 0;
int nbr_in_room_B = 0; 

public SensorThread(SerialPort [] sensorer){
    try {
        this.server = new ServerSocket(9999);
        System.out.println("** this.server = new ServerSocket(9999); *****");
        
    } catch (IOException ex) {
        Logger.getLogger(SensorThread.class.getName()).log(Level.SEVERE, null, ex);
    }
    sensorsCOM_ports = sensorer;
}

@Override
    public void run() {
 
    /************* Christian Sensor ****************************/
     byte[] buffer=null;

     /************* Sensor 1 ****************************/ 
     String[] read_string_array_from_input_sensor_1 = null;
     String[] array_from_input_sensor_1 = new String [12];
     int counter_sensor_1 = 0;
     int num_sensor_1 = 0;
              
    /************* Sensor 2 ****************************/ 
     String[] read_string_array_from_input_sensor_2 = null;
     String[] array_from_input_sensor_2 = new String [12];
     int counter_sensor_2 = 0;
     int num_sensor_2 = 0;
          
    /************** end sensors ***************************/
        while(true){
           
            //System.out.println("** SENSOR THREAD ACCEPT, IN RUN():*************************************");
            try {
              /****************** START CHRISTIAN SENSOR *********************/ 
               //buffer = sensorsCOM_ports[0].readBytes(1);//1
                  /*if(buffer[0]=='A'){
                    System.out.println("A");
                    ++nbr_in_room_A;
                    System.out.println("NBR IN ROOM A:"+ nbr_in_room_A);
                    if(nbr_in_room_B>0){
                    --nbr_in_room_B;
                    }
                   //output message to context: A nbr of person, B nbr of persons
                }   
                if(buffer[0]=='B'){
                    System.out.println("B");
                    ++nbr_in_room_B;
                     System.out.println("NBR IN ROOM B:"+ nbr_in_room_B);
                    if(nbr_in_room_A>0){
                    --nbr_in_room_A;
                    }
                   //output message to context: A nbr of person, B nbr of persons
                }    */  
              /****************** END CHRISTIAN SENSOR *********************/  
                
               
                /****************** START COMPORT 1 *********************/
                int x_sensor_1 = 0;
                int y_sensor_1 = 0;
                int x_temp_sensor_1 = 0;
                int y_temp_sensor_1 = 0;
                read_string_array_from_input_sensor_1 = sensorsCOM_ports[1].readHexStringArray(1);//

                array_from_input_sensor_1[counter_sensor_1] =  read_string_array_from_input_sensor_1[0];
                counter_sensor_1++;
                if(counter_sensor_1 == 12)
                {
                   for( num_sensor_1 = 0;num_sensor_1 < 10; num_sensor_1++)
                    {
                        if(array_from_input_sensor_1[num_sensor_1].equals("52")){
                            x_sensor_1 = Integer.valueOf(array_from_input_sensor_1[num_sensor_1+1], 16);
                            y_sensor_1 = Integer.valueOf(array_from_input_sensor_1[num_sensor_1+2], 16);
                        }
                        if(array_from_input_sensor_1[num_sensor_1].equals("54")){                          
                            x_temp_sensor_1 = Integer.valueOf(array_from_input_sensor_1[num_sensor_1+1], 16);
                            y_temp_sensor_1 = Integer.valueOf(array_from_input_sensor_1[num_sensor_1+2], 16);
                        }                    
                    }
                    counter_sensor_1 = 0;                  
                
               // System.out.println(Arrays.toString(array_from_input_sensor_2));
                //System.out.print(x_sensor_1);
                //System.out.print(y_sensor_1); 
               /*int humidity1 =x_sensor_1*256+y_sensor_1;
                int temp_sensor_1 = x_temp_sensor_1*256+y_temp_sensor_1;*/
                
                double humidityEquation =(x_sensor_1*256+y_sensor_1)/1024.0*100;
                double temperatureEquation =((x_sensor_1*256+y_sensor_1)/1024.0*108-32)/1.8;
                
                System.out.println("Humidity SENSOR 1: "+ humidityEquation);
                System.out.println("Temperature SENSOR 1: "+ temperatureEquation);
                
                }
                /****************** END COMPORT 1 *********************/
                /****************** START COMPORT 2 *********************/
                int x_sensor_2 = 0;
                int y_sensor_2 = 0;
                int x_temp_sensor_2 = 0;
                int y_temp_sensor_2 = 0;
                read_string_array_from_input_sensor_2 = sensorsCOM_ports[2].readHexStringArray(1);// Read CO2 and temperature for sensor_2
          
                array_from_input_sensor_2[counter_sensor_2] =  read_string_array_from_input_sensor_2[0];
                counter_sensor_2++;
                if(counter_sensor_2 == 12)
                {
                   for( num_sensor_2 = 0;num_sensor_2 < 10; num_sensor_2++)
                    {
                        if(array_from_input_sensor_2[num_sensor_2].equals("43")){                     
                            x_sensor_2 = Integer.valueOf(array_from_input_sensor_2[num_sensor_2+1], 16);
                            y_sensor_2 = Integer.valueOf(array_from_input_sensor_2[num_sensor_2+2], 16);                         
                        }
                        if(array_from_input_sensor_2[num_sensor_2].equals("54")){
                            x_temp_sensor_2 = Integer.valueOf(array_from_input_sensor_2[num_sensor_2+1], 16);
                            y_temp_sensor_2 = Integer.valueOf(array_from_input_sensor_2[num_sensor_2+2], 16);                         
                        }  
                        
                    }
                    counter_sensor_2 = 0;
               
                /* int humidity_sensor_2 =x_sensor_2*256+y_sensor_2;
                int temp_sensor_2 = x_temp_sensor_2*256+y_temp_sensor_2; */
                
                double CO2_Equation_sensor_2 =((x_sensor_2*256+y_sensor_2)/1024.0*2000);
                double temperatureEquation_sensor2 =((x_sensor_2*256+y_sensor_2)/1024.0*90)/1.8;
                
                System.out.println("CO2_Equation:"+ CO2_Equation_sensor_2);
                System.out.println("Temperature SENSOR 2: "+ temperatureEquation_sensor2);
                }
                /****************** END COMPORT 2 *********************/
                                
          /**************** 6/4*************************/
            } catch (SerialPortException ex) {
                
            
                Logger.getLogger(SensorThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //System.out.println("** 1111 *****");
               
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

   class PortReader implements SerialPortEventListener{

    @Override
    public void serialEvent(SerialPortEvent event) {
        if(event.isRXCHAR()&& event.getEventValue()>0){
        try{
         String received_data = sensorsCOM_ports[1].readString(event.getEventValue());
         System.out.println("Received response:"+received_data);
        }catch(SerialPortException e){
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
    String[] portNames= null;
    SerialPort [] serialPorts = null;
 
    long startTimeH = System.nanoTime();
    EchoHandler(Socket  client, SerialPort [] ports){
            this.client =  client;   
            serialPorts = ports;
            
    }// end constructor
    long stopTimeH = System.nanoTime();
    long durationH = stopTimeH - startTimeH;
    
         public void run(){
            try{
                    System.out.println("durationH "+durationH);

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
                        long startTimeC_Send = System.nanoTime();
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
                                    //serialPorts[?].writeBytes("1".getBytes()); //added 2016.03.30........................................
                                }//1
                                else if(token0[4].equals("2")){
                                    System.out.println("Stop Curtain in Room 1");
                                 //serialPorts[?].writeBytes("2".getBytes()); //added 2016.03.30........................................   
                                }else if(token0[4].equals("3")) { //added 2016.03.30........................................
                                	System.out.println("Scroll Down Curtain in Room 1");
                                	//serialPorts[?].writeBytes("3".getBytes());      
                                }//2 : off
                            }//CR
                        }//R1
                        long stopTimeC_Send = System.nanoTime();
                        long durationC_Send =stopTimeC_Send - startTimeC_Send;
                                System.out.println("durationC_Send "+durationC_Send);

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