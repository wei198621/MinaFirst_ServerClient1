package com.leo.Mina.MinaTimeServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import org.apache.mina.core.service.*;
import org.apache.mina.transport.socket.nio.*;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;

//import org.apache.mina.filter.logging.*;
import org.apache.mina.filter.codec.*;
import org.apache.mina.filter.codec.textline.*;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.core.session.*;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Main {
	//   http://blog.csdn.net/yoara/article/details/37324821
	private static final String HOSTNAME="192.168.1.44";
	private static final int PORT=9128;
	private static Logger logger = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args)throws IOException{
		logger.info("**********************************************");
		String date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
        //System.out.println("转换后的时间为：" + date);
		logger.info("MinaTimeServer  Main Start  时间为："+date);	
		logger.info("**********************************************");
		
		//首先，我们为服务器端创建IoAcceptor ,NicoSocketAcceptor 是基于noi的服务端监听器		
		//org.apache.mina.core.service.IoAcceptor
		IoAcceptor acceptor =new NioSocketAcceptor();
		//接着，如结构图示，在Acceptor和IoHandler之间将设置一些列的Filter		
		//包括记录过滤器
		//编码过滤器，TextLinecodeFactory是mina自带的文本编码解码器		
		acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
		//配置事务处理Handler,将请求转悠TimeServerHandler处理
		acceptor.setHandler(new TimeServerHandler());
		 //配置Buffer的缓冲区大小
		acceptor.getSessionConfig().setReadBufferSize(2048);
		//设置等待时间，每隔IdleTime将调用一次handler.sessionIdle()方法 
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		//绑定端口
		acceptor.bind(new InetSocketAddress(HOSTNAME,PORT));
	
	}
	
	//org.apache.mina.core.service.IoHandlerAdapter
	//org.apache.mina.core.session.IoSession
	static class TimeServerHandler extends IoHandlerAdapter{
		public void exceptionCaught(IoSession session,Throwable cause)
			throws Exception{
				cause.printStackTrace();
			}
		
		public void messageReceived(IoSession session,Object message)
		throws Exception{
			String strFromClient=message.toString();
			//如果客户端传入quit，退出 session
			if(strFromClient.trim().equalsIgnoreCase("quit")){
				session.close(false);
				return;
			}			 
			String strToClient="Server Received:"+strFromClient;
			session.write(strToClient);
			logger.info("strFromClient: "+strFromClient);
			logger.info("***************************************");
		}
		
		public void sessionIdle(IoSession session,IdleStatus status)	 
			throws Exception{
			logger.info("IDLE "+session.getIdleCount(status)); 
			}
		 
	}
	
	/**
	 * 启动日志记录器
	 */
	private static void startLog() {
		try {
			ILoggerFactory loggerFactory = LoggerFactory.getILoggerFactory();
			LoggerContext loggerContext = (LoggerContext) loggerFactory;
			loggerContext.reset();
			JoranConfigurator configurator = new JoranConfigurator();
			configurator.setContext(loggerContext);
			configurator.doConfigure("logback.xml");
			logger.info("成功启动日志记录器");
		} catch (Exception e) {
			System.out.println("启动日志记录器时发生错误:");
			e.printStackTrace();
			System.exit(0);
		}
	}
		
		
	 
	  /**
     * 
     * @Title: getNonSign
     * @Description: 获取无符号的字节
     * @param Sign
     * @return 0-255的数字
     * @throws
     */
    public static int getNonSign(byte Sign) {
        return Sign & 0xFF;
    }
	
	
	
	
	
	
	
	
	
	
	

}
