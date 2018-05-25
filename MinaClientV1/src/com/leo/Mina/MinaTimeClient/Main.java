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
        //System.out.println("转换后的时间为：" + date);
		logger.info("MinaTimeClient  Main Start  时间为："+date);	
		logger.info("************************************");
		
		//创建Connector 连接器
		NioSocketConnector connector=new NioSocketConnector();
		//设置连接超时时间
		connector.setConnectTimeoutMillis(CONNECT_TIMEOUT);
		//日志过滤器，编码过滤器
		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory())); 
		connector.getFilterChain().addLast("logger",new LoggingFilter());
		//非常建档的处理handler，向服务器发送一个数字
		connector.setHandler(new ClientSessionHandler(1));
		//与服务器间连接通信的session  
		IoSession session=null;  
		try{
			//连接远程主机，设置IP  port
			ConnectFuture future=connector.connect(new InetSocketAddress(HOSTNAME,PORT));
			//等待建立连接
			future.awaitUninterruptibly();
			//连接建立后返回会话session
			session =future.getSession();
		}catch(RuntimeIoException  e){
			System.err.println("Failed to connect server.");
			e.printStackTrace();
			Thread.sleep(5000);
		}finally{
			if(session!=null)
			{
				//等待本次连接通话结束，不可终端的阻塞等待  
				session.getCloseFuture().awaitUninterruptibly();
			}
		}
		//关闭连接
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
			logger.info("第 " + i + " 个 登陆address：: " + session.getRemoteAddress());
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
		// 当信息已经传送给客户端后触发此方法.
	    public void messageSent(IoSession session, Object message) {
	    	logger.info("messageSent is :"+message);	 
	    }
		
		
		public void exceptionCaught(IoSession session,Throwable cause){
			session.close(true);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
