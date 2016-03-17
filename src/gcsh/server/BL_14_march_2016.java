/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gcsh.server;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

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
import javax.obex.ClientSession;
import javax.obex.HeaderSet;
import javax.obex.Operation;
import javax.obex.ResponseCodes;



public class BL_14_march_2016 implements DiscoveryListener{
    
    private static Object lock=new Object();
    public ArrayList<RemoteDevice> devices;
    //private OutputStream os;
  
    
    public BL_14_march_2016() {
        devices = new ArrayList<RemoteDevice>();
    }
    
    public static void main(String[] args) {
        
    	BL_14_march_2016 listener =  new BL_14_march_2016();
        
        try{
            LocalDevice localDevice = LocalDevice.getLocalDevice();
            DiscoveryAgent agent = localDevice.getDiscoveryAgent();
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
                System.out.print(attrIDs.toString()+"***"+uuidSet+"*****"+device.toString()+"******"+listener.toString());
                
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
    }

  

    @Override
    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass arg1) {
        String name;
        String deviceAdress;
        try {
            name = btDevice.getFriendlyName(false);
            System.out.println(name+":name = btDevice.getFriendlyName(false);");
            deviceAdress = btDevice.getBluetoothAddress();
            
            System.out.println(deviceAdress+":name = btDevice.getBluetoothAddress();");
            
        } catch (Exception e) {
        	deviceAdress = btDevice.getBluetoothAddress();
            //System.out.println(deviceAdress+":name = btDevice.getBluetoothAddress();");
        }
        
        devices.add(btDevice);
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
            if (url == null) {
                continue;
            }
            System.out.println("URL "+url);
            System.out.println("Service discovered");
            DataElement serviceName = servRecord[i].getAttributeValue(0x0100);
            if (serviceName != null) {
                System.out.println("SSSSSservice " + serviceName.getValue() + " found " + url);
                
                if(serviceName.getValue().equals("Object Push Service")){
                    System.out.println("LLLLLLLLLLLLLLLLLl");
                	sendMessageToDevice("btspp://00066608BB6A:1");  
                        //sendMessageToDevice("btspp://00066608BB69:1");
                }
                
            } else {
                System.out.println("service found " + url);
            }
            
          
        }
    }
   
 
    private static void sendMessageToDevice(String serverURL){
        try{
            System.out.println("Connecting to " + serverURL);
            
            StreamConnection con = (StreamConnection) Connector.open(serverURL);
            System.out.println("**** In RFcommclient run method **** Service URL = " + serverURL + ".");
    
            // Send some text to server
            byte buffer[] = new byte[6];
            buffer[0] = '1';// MT if you want to turn on=1, if you want to turn off 2
            
            OutputStream os = con.openOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(os);
            System.out.println("Client Connection opened at " + con.toString());
                        
            dataOutputStream.flush();

            os.write(buffer);
            System.out.println("********* CLOSING OUTPUT-STREAM*******");
            os.close();
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
