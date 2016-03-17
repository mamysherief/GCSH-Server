

	
	/*
	 * To change this license header, choose License Headers in Project Properties.
	 * To change this template file, choose Tools | Templates
	 * and open the template in the editor.
	 */
	

	/**
	 *
	 * @author G500
	 */
	//package bt;
//external JAR from BT folder; TURN ON BT in PC settings ; check somewhere with BT device not ur room!
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
	import javax.obex.ClientSession;
	import javax.obex.HeaderSet;
	import javax.obex.Operation;
	import javax.obex.ResponseCodes;
	
// BT on PC > on/off
	//import bt.BTListener.MyDeviceListenerFilter;
	
	// smaple output!! but npt on my PC!
	//.......... discovery listerner........
	//.......... local device........
	//BlueCove version 1.2.3
	//.......... discovery agent........
	//.......... lock........
	//device found: RNBT-640F
	//device found: ASUS-B�RBAR
	//device found: VT-PC
	//device found: BB6A
	//Device Inquiry Completed.
	//Service search finished.
	//Service search finished.
	//Service search finished.
	//Service search finished.
	//BUILD SUCCESSFUL (total time: 32 seconds)
	//netbeans

	public class MyDiscoveryListener  implements DiscoveryListener{
	    
	    private static Object lock=new Object();
	    public ArrayList<RemoteDevice> devices;
	    
	    //.....
	   public static byte data[];
	   
	    public static byte buffer[] = new byte[6];
	    
	    
	    
	    public MyDiscoveryListener() {
	        devices = new ArrayList<RemoteDevice>();
	    }
	    
	    public static void main(String[] args) {  // PC settings > bluetooth on
	        
	    	System.out.println(".......... discovery listerner........");
	        MyDiscoveryListener listener =  new MyDiscoveryListener();
	        
	        try{
	        	System.out.println(".......... local device........");
	            LocalDevice localDevice = LocalDevice.getLocalDevice();
	            System.out.println(".......... discovery agent........");
	            DiscoveryAgent agent = localDevice.getDiscoveryAgent();
	            agent.startInquiry(DiscoveryAgent.GIAC, listener);
	            
	            System.out.println(".......... lock........");
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
	            
	       
	            //UUID[] uuidSet = new UUID[1];
	            //....
	            UUID[] uuidSet = new UUID[1]; // if not the whole array set > crash > different service, on/off command
	            uuidSet[0]=new UUID(0x1105); //OBEX Object Push service
	            //uuidSet[0]=new UUID(0x1105);//00066608bb6a
	            
	           // uuidSet[1]=new UUID(0x1105);
	            
	            System.out.println();
	            //....................
	            System.out.printf("uuidSet.length %d\t",uuidSet.length);
	            System.out.println("uuidSet......\n");
	            for(int i=0;i<uuidSet.length;i++){
	            	System.out.println(uuidSet[i]); //0000110500001000800000805f9b34fb
	            }
	            
	            //.......
	            
	            int[] attrIDs =  new int[] {
	                    0x0100 // Service name
	            };
	            
	            for (RemoteDevice device : listener.devices) {
	                agent.searchServices(
	                        attrIDs,uuidSet,device,listener);
	                
	                
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
	        try {
	            name = btDevice.getFriendlyName(false);
	        } catch (Exception e) {
	            name = btDevice.getBluetoothAddress();
	        }
	        
	        devices.add(btDevice);
	        System.out.println("device found: " + name); //00066608bb6a
	        //.....
	        //if(name=="00066608bb6a"){
	        //System.out.println(name); //et
	        //if(name=="BB6A"){ //lamp name; debug > never comes inside here ... false.... idnt ; DES lab
	        if(name.equals("BB6A")){
	        	String command="h";
	        	data = command.getBytes();
	        	//data ="0xFF FE";
	        	//data = "h";
	        	//System.out.printf("..............data to be sent....%s  \n",data);
	        	System.out.printf("..............data to be sent....%s  \n",command);
	        	buffer[0]='1';
	        }
	        //....
	        //System.out.printf("..............data ...%s  \n",data); // loop for discovering each device, bytes
	    }//deviceDiscovered

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
	        for (int i = 0; i < servRecord.length; i++) {
	            String url = servRecord[i].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
	            if (url == null) {
	                continue;
	            }
	            DataElement serviceName = servRecord[i].getAttributeValue(0x0100);
	            if (serviceName != null) {
	                System.out.println("service " + serviceName.getValue() + " found " + url);
	                
	                if(serviceName.getValue().equals("OBEX Object Push")){
	                    sendMessageToDevice(url);                
	                }
	            } else {
	                System.out.println("service found " + url);
	            }
	            
	          
	        }
	    }
	    
	    private static void sendMessageToDevice(String serverURL){
	        try{
	            System.out.println("Connecting to " + serverURL);
	    
	            ClientSession clientSession = (ClientSession) Connector.open(serverURL);
	            HeaderSet hsConnectReply = clientSession.connect(null);
	            if (hsConnectReply.getResponseCode() != ResponseCodes.OBEX_HTTP_OK) {
	                System.out.println("Failed to connect");
	                return;
	            }
	    
	            HeaderSet hsOperation = clientSession.createHeaderSet();
	            hsOperation.setHeader(HeaderSet.NAME, "Hello.txt");
	            hsOperation.setHeader(HeaderSet.TYPE, "text");
	    
	            //Create PUT Operation
	            Operation putOperation = clientSession.put(hsOperation);
	    
	            // Send some text to server
	            
	            //.....
	        
	            
	            //.............................................................................................................................................uncommnet!
	          //  byte data[] = "Hello World !!!".getBytes("iso-8859-1");
	            
	            
	            OutputStream os = putOperation.openOutputStream();
	            os.write(data);  //....static
	            //....
	            os.write(buffer);
	            os.close();
	    
	            putOperation.close();
	    
	            clientSession.disconnect(null);
	    
	            clientSession.close();
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	}

	

