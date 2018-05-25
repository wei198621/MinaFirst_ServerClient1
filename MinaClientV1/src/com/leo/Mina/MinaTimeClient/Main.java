package com.leo.Mina.MinaTimeClient;

import org.apache.mina.transport.socket.nio.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 

import org.apache.mina.core.service.*;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.textline.*;
import org.apache.mina.filter.codec.*;
import org.apache.mina.filter.logging.*;

import java.net.InetSocketAddress;

import org.apache.mina.core.*;
import org.apache.mina.core.future.ConnectFuture;

public class Main {
	//http://blog.csdn.net/yoara/article/details/37324821
	private static final String HOSTNAME="192.168.1.44";
	private static final int PORT=9128;
	private static final long CONNECT_TIMEOUT=30*1000L;   //30seconds
	

	private static Logger logger = LoggerFactory.getLogger(Main.class);
	
	//java.lang.Throwable
	public static void main(String[] args)throws Throwable{
		//http://blog.csdn.net/yoara/article/details/37324821
		//NioSocketConnector connector =new NioSocketConnector();                      //methord 1
		//org.apache.mina.core.service.IoConnector  connector2=new NioSocketConnector();  //methord 2
		

		logger.info("************************************");
		String date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
        //System.out.println("ת�����ʱ��Ϊ��" + date);
		logger.info("MinaTimeClient  Main Start  ʱ��Ϊ��"+date);	
		logger.info("************************************");
		
		//����Connector ������
		NioSocketConnector connector=new NioSocketConnector();
		//�������ӳ�ʱʱ��
		connector.setConnectTimeoutMillis(CONNECT_TIMEOUT);
		//��־�����������������
		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory())); 
		connector.getFilterChain().addLast("logger",new LoggingFilter());
		//�ǳ������Ĵ���handler�������������һ������
		connector.setHandler(new ClientSessionHandler(1));
		//�������������ͨ�ŵ�session  
		IoSession session=null;  
		try{
			//����Զ������������IP  port
			ConnectFuture future=connector.connect(new InetSocketAddress(HOSTNAME,PORT));
			//�ȴ���������
			future.awaitUninterruptibly();
			//���ӽ����󷵻ػỰsession
			session =future.getSession();
		}catch(RuntimeIoException  e){
			System.err.println("Failed to connect server.");
			e.printStackTrace();
			Thread.sleep(5000);
		}finally{
			if(session!=null)
			{
				//�ȴ���������ͨ�������������ն˵������ȴ�  
				session.getCloseFuture().awaitUninterruptibly();
			}
		}
		//�ر�����
		connector.dispose();
		
	}
	
	
	static class ClientSessionHandler extends IoHandlerAdapter{
		private final int value;
		//private int count = 0;
		int iTotal=100;
		
		public ClientSessionHandler(int value){
			this.value =value;
		}
		
		public void sessionOpened(IoSession session)
		{

			session.write(" current persent is 1/1:");
			
			/*for (int i=0 ;i< iTotal;i++)
			{
			session.write(" current persent is :"+i+"/"+iTotal);
			logger.info("�� " + i + " �� ��½address��: " + session.getRemoteAddress());
			logger.info("******************************************");
			*/
			/*try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
		    
			//}
		}
		
		public void messageReceived(IoSession session,Object message){
			logger.info("messageReceived is :"+message.toString());
			session.close(true);
		}
		// ����Ϣ�Ѿ����͸��ͻ��˺󴥷��˷���.
	    public void messageSent(IoSession session, Object message) {
	    	logger.info("messageSent is :"+message);	 
	    }
		
		
		public void exceptionCaught(IoSession session,Throwable cause){
			session.close(true);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
