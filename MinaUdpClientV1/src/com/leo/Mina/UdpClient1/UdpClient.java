package com.leo.Mina.UdpClient1;

import java.net.InetSocketAddress;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;

public class UdpClient {

	 private static IoSession session = null;  
	    public static void main(String[] args) {  
	        NioDatagramConnector connect = new NioDatagramConnector();  
	        connect.setHandler(new UDPClientHandler());  
	        try{  
	            ConnectFuture future = connect.connect(new InetSocketAddress("192.168.1.44",9234));  
	            future.awaitUninterruptibly();  
	            session = future.getSession();  
	            //�������ӽ�����ɺ�ļ�������  
	            //���ڽ�����ɺ����Ӽ�������������������ִ��  
	            future.addListener(new IoFutureListener<ConnectFuture>(){  
	                public void operationComplete(ConnectFuture future) {  
	                    if( future.isConnected() ){  
	                        try {  
	                            sendData();  
	                        } catch (InterruptedException e) {  
	                            e.printStackTrace();  
	                        }  
	                    } else {  
	                        System.out.println(("Not connected...exiting"));  
	                    }  
	                }  
	            });  
	        }catch(Exception e){  
	              
	        }finally{  
	            if(session!=null){  
	                session.getCloseFuture().awaitUninterruptibly();  
	            }  
	        }  
	        connect.dispose();  
	    }  
	  
	    private static void sendData() throws InterruptedException {  
	        long free = Runtime.getRuntime().freeMemory();  
	        IoBuffer buffer = IoBuffer.allocate(8);  
	        buffer.putLong(free);  
	        buffer.flip();  
	        session.write(buffer);  
	        //��Ϊ��UDP���ͻ����������ر�����  
	        session.close(false);  
	    }  
	    static class UDPClientHandler extends IoHandlerAdapter{}  
}
