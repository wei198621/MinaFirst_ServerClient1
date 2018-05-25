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
        //System.out.println("ת�����ʱ��Ϊ��" + date);
		logger.info("MinaTimeServer  Main Start  ʱ��Ϊ��"+date);	
		logger.info("**********************************************");
		
		//���ȣ�����Ϊ�������˴���IoAcceptor ,NicoSocketAcceptor �ǻ���noi�ķ���˼�����		
		//org.apache.mina.core.service.IoAcceptor
		IoAcceptor acceptor =new NioSocketAcceptor();
		//���ţ���ṹͼʾ����Acceptor��IoHandler֮�佫����һЩ�е�Filter		
		//������¼������
		//�����������TextLinecodeFactory��mina�Դ����ı����������		
		acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
		//����������Handler,������ת��TimeServerHandler����
		acceptor.setHandler(new TimeServerHandler());
		 //����Buffer�Ļ�������С
		acceptor.getSessionConfig().setReadBufferSize(2048);
		//���õȴ�ʱ�䣬ÿ��IdleTime������һ��handler.sessionIdle()���� 
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		//�󶨶˿�
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
			//����ͻ��˴���quit���˳� session
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
	 * ������־��¼��
	 */
	private static void startLog() {
		try {
			ILoggerFactory loggerFactory = LoggerFactory.getILoggerFactory();
			LoggerContext loggerContext = (LoggerContext) loggerFactory;
			loggerContext.reset();
			JoranConfigurator configurator = new JoranConfigurator();
			configurator.setContext(loggerContext);
			configurator.doConfigure("logback.xml");
			logger.info("�ɹ�������־��¼��");
		} catch (Exception e) {
			System.out.println("������־��¼��ʱ��������:");
			e.printStackTrace();
			System.exit(0);
		}
	}
		
		
	 
	  /**
     * 
     * @Title: getNonSign
     * @Description: ��ȡ�޷��ŵ��ֽ�
     * @param Sign
     * @return 0-255������
     * @throws
     */
    public static int getNonSign(byte Sign) {
        return Sign & 0xFF;
    }
	
	
	
	
	
	
	
	
	
	
	

}
