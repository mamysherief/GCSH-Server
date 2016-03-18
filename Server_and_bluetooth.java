/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.bluetooth.DataElement;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

import java.lang.Thread;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server_and_bluetooth {
	
	
	public static void main(String[] args) {
		
		try {
			
			ServerSocket server = new ServerSocket(4444); // 4444
			
			
			
			while (true) {
				Socket client = server.accept();
				EchoHandler handler = new EchoHandler(client);
				handler.start();
			}
		} catch (Exception e) {
			System.err.println("Exception caught:" + e);
		}
		
	} // main
	
	
	


}//ThreadServer_ContextClient


class EchoHandler extends Thread implements Runnable, DiscoveryListener{


	
	String userInput = "hrj"; 
    Socket client;
	
	
    //String hostName="194.47.44.145"; //same cheng
	
	//int portNumber = 8080; // ......................................................uncoimmment
							
	
	
	Socket echoSocket = null; 
	PrintWriter outToContext = null; 
	BufferedReader in = null;
	BufferedReader stdIn = null;
	
	InetAddress ip = null;
        EchoHandler listener= this;
	
	EchoHandler(Socket  client) {
		this.client =  client;
		devices = new ArrayList<RemoteDevice>();
                
		
	}
	
	
    private static Object lock=new Object();
    public ArrayList<RemoteDevice> devices;
    LocalDevice localDevice;
    DiscoveryAgent agent;
    StreamConnection con  = null;
    OutputStream os=null;
	public void run(){
		
            try{
                
                localDevice= LocalDevice.getLocalDevice();
                agent = localDevice.getDiscoveryAgent();
                agent.startInquiry(DiscoveryAgent.GIAC, listener);
                
                try {
                    synchronized(lock){
                        lock.wait();
                    }
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
                
                
                System.out.println("Device Inquiry Completed. ");
                
                
                UUID[] uuidSet = new UUID[1];
                uuidSet[0]=new UUID(0x1105); //OBEX Object Push service MT alla bluetooth enheter lägger sina serienummer på denna plats(samma plats!!!) så att när den letarv efter bluetooth enheter i närheten kommer dessa att visas upp som representerar enheterna i närheten.
                System.out.print("this number:"+uuidSet[0]+" is uuidset[0]");
                int[] attrIDs =  new int[] {
                    0x0100 // Service name
                        
                };
                
                for (RemoteDevice device : listener.devices) {
                    agent.searchServices( attrIDs,uuidSet,device,listener);
                    //System.out.print(attrIDs.toString()+"***"+uuidSet+"*****"+device.toString()+"******"+listener.toString());
                    
                    try {
                        synchronized(lock){
                            lock.wait();
                        }
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                    
                    
                    System.out.println("Service search finished.");
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        client.getInputStream()));
                //PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
                while (true) {
                    String line = reader.readLine(); // enter
                    
                    
                    String REGEX_Android = "A"; // A: !!
                    String REGEX_Kinect = "K";
                    
                    Pattern pA = Pattern.compile(REGEX_Android);
                    Matcher mA = pA.matcher(line);
                    
                    Pattern pK = Pattern.compile(REGEX_Kinect);
                    Matcher mK = pK.matcher(line);
                    
                    String ToContextServer = "no gesture yet"; // hex/ binary value
                    
                    double[] android_accelerometer = new double[10]; // 100 > 10!!
                    int[] command = new int[5];
                    
                    if (mA.find()) {
                        System.out.println("Android discovered");// A:x y z# THEY NEED "\n" AT THE END, readline, doc
                        String[] token0 = line.split("A|:| |#");
                        System.out
                                .println("android data start......................");
                        for (int q = 0; q < token0.length; q++) {
                            System.out.println(token0[q]);
                        }
                        
                        
                        System.out.println("end of android data................................");
                        //sendMessageToDevice("btspp://00066608BB6A:1","2");
                        /*
                        android_accelerometer[3] = Double.parseDouble(token0[3]);
                        if (android_accelerometer[3] >= 6.1 && android_accelerometer[3] <= 7.1) {
                        System.out.println(android_accelerometer[3]);
                        System.out.println("mobile up");
                        //ToContextServer = "h";
                        ToContextServer = "1";
                        //sendMessageToDevice("btspp://00066608BB6A:1",ToContextServer);
                        }
                        if (android_accelerometer[3] <= -6.1 && android_accelerometer[3] >= -7.1) {
                        System.out.println(android_accelerometer[3]);
                        System.out.println("mobile down");
                        ToContextServer = "2";
                        //sendMessageToDevice("btspp://00066608BB6A:1",ToContextServer);
                        }
                        */
                        //2nd format
                        /*
                        command[1]=Integer.parseInt(token0[1]);
                        if(command[1]==1){
                        ToContextServer = "1";
                        }
                        if(command[1]==2){
                        ToContextServer = "2";
                        }
                        */
                        //3rd format ; 2016.03.17
                        
                        //..................... ROOM 1 .......................................
                        if(token0[2].equals("R1")){
                            if(token0[3].equals("FL")){
                                if(token0[4].equals("1")){
                                    System.out.println("Turn ON Floor lamp in Room 1");
                                    ToContextServer="1"; // ? other devices? .......................................
                                }//1 : on
                                else if(token0[4].equals("2")){
                                    System.out.println("Turn OFF Floor lamp in Room 1");
                                    ToContextServer="2";
                                }//2 : off
                                
                            }//FL
                            if(token0[3].equals("CL")){
                                if(token0[4].equals("1")){
                                    System.out.println("Turn ON Ceiling lamp in Room 1");
                                    ToContextServer="1";
                                }//1
                                else if(token0[4].equals("2")){
                                    System.out.println("Turn OFF Ceiling lamp in Room 1");
                                    ToContextServer="2";
                                }//2 : off
                                
                            }//CL
                            if(token0[3].equals("FN")){
                                if(token0[4].equals("1")){
                                    System.out.println("Turn ON Fan in Room 1");
                                    ToContextServer="1";
                                }//1
                                else if(token0[4].equals("2")){
                                    System.out.println("Turn OFF Fan in Room 1");
                                    ToContextServer="2";
                                }//2 : off
                                
                            }//FN
                            if(token0[3].equals("CR")){
                                if(token0[4].equals("1")){
                                    System.out.println("Scroll Up Curtain in Room 1");
                                    ToContextServer="1";
                                }//1
                                else if(token0[4].equals("2")){
                                    System.out.println("Scroll Down Curtain in Room 1");
                                    ToContextServer="2";
                                }//2 : off
                                
                            }//CR
                            
                            
                            // set variable ToContextServer/ BT
                        }//R1
                        
                        //..................... ROOM 2 .......................................
                        if(token0[2].equals("R2")){
                            if(token0[3].equals("FL")){
                                if(token0[4].equals("1")){
                                    System.out.println("Turn ON Floor lamp in Room 2");
                                    ToContextServer="1";
                                }//1 : on
                                else if(token0[4].equals("2")){
                                    System.out.println("Turn OFF Floor lamp in Room 2");
                                    ToContextServer="2";
                                }//2 : off
                                
                            }//FL
                            if(token0[3].equals("CL")){
                                if(token0[4].equals("1")){
                                    System.out.println("Turn ON Ceiling lamp in Room 2");
                                    ToContextServer="1";
                                }//1
                                else if(token0[4].equals("2")){
                                    System.out.println("Turn OFF Ceiling lamp in Room 2");
                                    ToContextServer="2";
                                }//2 : off
                                
                            }//CL
                            if(token0[3].equals("FN")){
                                if(token0[4].equals("1")){
                                    System.out.println("Turn ON Fan in Room 2");
                                    ToContextServer="2";
                                }//1
                                else if(token0[4].equals("2")){
                                    System.out.println("Turn OFF Fan in Room 2");
                                    ToContextServer="2";
                                }//2 : off
                                
                            }//FN
                            if(token0[3].equals("CR")){
                                if(token0[4].equals("1")){
                                    System.out.println("Scroll Up Curtain in Room 2");
                                    ToContextServer="1";
                                }//1
                                else if(token0[4].equals("2")){
                                    System.out.println("Scroll Down Curtain in Room 2");
                                    ToContextServer="2";
                                }//2 : off
                                
                            }//CR
                            
                            
                            // set variable ToContextServer/ BT
                        }//R2
                        //..................... ROOM 3 .......................................
                        if(token0[2].equals("R3")){
                            if(token0[3].equals("FL")){
                                if(token0[4].equals("1")){
                                    System.out.println("Turn ON Floor lamp in Room 3");
                                    ToContextServer="1";
                                }//1 : on
                                else if(token0[4].equals("2")){
                                    System.out.println("Turn OFF Floor lamp in Room 3");
                                    ToContextServer="2";
                                }//2 : off
                                
                            }//FL
                            if(token0[3].equals("CL")){
                                if(token0[4].equals("1")){
                                    System.out.println("Turn ON Ceiling lamp in Room 3");
                                    ToContextServer="1";
                                }//1
                                else if(token0[4].equals("2")){
                                    System.out.println("Turn OFF Ceiling lamp in Room 3");
                                    ToContextServer="2";
                                }//2 : off
                                
                            }//CL
                            if(token0[3].equals("FN")){
                                if(token0[4].equals("1")){
                                    System.out.println("Turn ON Fan in Room 3");
                                    ToContextServer="1";
                                }//1
                                else if(token0[4].equals("2")){
                                    System.out.println("Turn OFF Fan in Room 3");
                                    ToContextServer="2";
                                }//2 : off
                                
                            }//FN
                            if(token0[3].equals("CR")){
                                if(token0[4].equals("1")){
                                    System.out.println("Scroll Up Curtain in Room 3");
                                    ToContextServer="1";
                                }//1
                                else if(token0[4].equals("2")){
                                    System.out.println("Scroll Down Curtain in Room 3");
                                    ToContextServer="2";
                                }//2 : off
                                
                            }//CR
                            
                            // set variable ToContextServer/ BT
                        }//R3
                        
                        
                        
                        
                        System.out.println("end of x coordinates");
                        
                    }//if for android
                    
                    // to-do..................... right-left if
                    
                    double[] coordinate_y = new double[100];
                    
                    
                    if (mK.find()) {
                        System.out.println("Kinect discovered");
                        // K: s r x1 y1 z1: e r x2 y2 z2: w r x3 y3 z3:#
                        
                        String[] token1 = line.split("K| |:|#");
                        
                        
                        System.out
                                .println("kinect data start......................");
                        // print whole index
                        for (int k = 0; k < token1.length; k++) {
                            System.out.println(token1[k] + ";"); // don't count ;,
                            
                        }
                        //
                        System.out
                                .println("end of Kinect data.................................");
                        System.out.println("***********************");
                        System.out.println("x");
                        
                        for (int v = 6; v < token1.length; v += 6) {
                            System.out.println(token1[v]);
                            
                            coordinate_y[v + 1] = Double.parseDouble(token1[v + 1]);
                            System.out.println(token1[v + 1]);
                            
                            
                        }// for
                        
                        // 1st format
                        if ((coordinate_y[19] > coordinate_y[13])
                                && (coordinate_y[13] > coordinate_y[7])) {
                            System.out.println("right hand up");
                            
                            
                            ToContextServer = "h";
                            
                        }
                        
                        if ((coordinate_y[19] < coordinate_y[13])
                                && (coordinate_y[13] < coordinate_y[7])) {
                            System.out.println("right hand down");
                            
                            ToContextServer = "j";
                        }
                        
                        //2nd format ....... to be added here
                        
                        
                        
                    }// if for kinect
                    
                    
                    System.out.println("HHHHHH 1");
                    sendMessageToDevice("btspp://00066608BB6A:1","1", con, os); //ToContextServer, con, os);
                    /*char c = ToContextServer.charAt(0);
                    //char c = "2".charAt(0);
                    byte buffer[] = new byte[6];
                    //buffer[0] = '2';// MT if you want to turn on=1, if you want to turn off 2
                    buffer[0] = (byte) c;
                    System.out.println("Client Connection opened at " + con.toString());
                    dataOutputStream.flush();
                    os.write(buffer);*/
                    
                    
                    System.out.println("HHHHHH 3");
                    
                    
                    
                    
                    System.out.println("HHHHHH 4");
                    String[] token0 = line.split("A|K|:| |#"); // == parseInt ; Data
                    
                    
                    System.out.println("HHHHHH 5");
                    
                }
            } catch (Exception e) {
                System.err.println("Exception caught: client disconnected.");
                System.err.println(e.getMessage());
            } finally {
                try {
                    client.close();
                } catch (Exception e) {
                    ;
                }
            }
	}
	@Override
	public void deviceDiscovered(RemoteDevice btDevice, DeviceClass arg1) {
	   String name;
	   String deviceAdress;
	   try {
	       name = btDevice.getFriendlyName(false);
	       //System.out.println(name+":name = btDevice.getFriendlyName(false);");
	       deviceAdress = btDevice.getBluetoothAddress();
	      
	       System.out.println(deviceAdress+":name = btDevice.getBluetoothAddress();");
	      
	   } catch (Exception e) {
	          deviceAdress = btDevice.getBluetoothAddress();
	       System.out.println(deviceAdress+":name = btDevice.getBluetoothAddress();");
	   }
	  if(deviceAdress.equals("00066608BB6A")){
              System.out.println("********* DEVICE WITH ADRESS 00066608BB6A IS ADDED IN THE LIST ******************");
	   devices.add(btDevice);
          }if(deviceAdress.equals("00066608BB69")){
              System.out.println("********* DEVICE WITH ADRESS 00066608BB6A IS ADDED IN THE LIST ******************");
	   devices.add(btDevice);
          }
	   //System.out.println("device found: " + name);
	  
	   
	}

	@Override
	public void inquiryCompleted(int arg0) {
	   synchronized(lock){
	       lock.notify();
	   }
	}

	@Override
	public void serviceSearchCompleted(int arg0, int arg1) {
	   synchronized (lock) {
	       lock.notify();
	   }
	}

	@Override
	public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
	           System.out.println("Service discovered");
	  
	   for (int i = 0; i < servRecord.length; i++) {
	       String url = servRecord[i].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
               System.out.println("URL "+url);
	       if (url == null) {
	           continue;
	       }
	       System.out.println("Service discovered");
	       DataElement serviceName = servRecord[i].getAttributeValue(0x0100);
	       if (serviceName != null) {
	           System.out.println("SSSSSservice " + serviceName.getValue() + " found " + url);
	          
	           if(serviceName.getValue().equals("Object Push Service")){
	               System.out.println("LLLLLLLLLLLLLLLLLl");
	                         //sendMessageToDevice("btspp://00066608BB6A:1");          
	           }
	          
	       } else {
	           System.out.println("service found " + url);
	       }
	      
	     
	   }
	}
 
	private static void sendMessageToDevice(String serverURL, String command,StreamConnection con, OutputStream os){
	   try{
	   	char c = command.charAt(0);
	       System.out.println("Connecting to " + serverURL);
	      
	       //StreamConnection 
                       con = (StreamConnection) Connector.open(serverURL);
	       System.out.println("**** In RFcommclient run method **** Service URL = " + serverURL + ".");

	       // Send some text to server
	       byte buffer[] = new byte[6];
	      //buffer[0] = '2';// MT if you want to turn on=1, if you want to turn off 2
	       buffer[0] = (byte) c;
	       		
	       //OutputStream 
               os = con.openOutputStream();
	       DataOutputStream dataOutputStream = new DataOutputStream(os);
	       System.out.println("Client Connection opened at " + con.toString());
	                   
	       dataOutputStream.flush();

	       os.write(buffer);
	       System.out.println("********* CLOSING OUTPUT-STREAM*******");
	       os.close();
	      
	   }
	   catch (Exception e) {
	       e.printStackTrace();
               //os.close();

	   }
	}
}// EchoHandler
