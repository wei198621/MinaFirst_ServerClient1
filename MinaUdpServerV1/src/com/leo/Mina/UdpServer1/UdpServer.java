package com.leo.Mina.UdpServer1;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;

public class UdpServer {

	private final static int PORT = 9234;  
    public static void main(String[] args) throws IOException {  
        //����UDPAcceptor  
        NioDatagramAcceptor acceptor = new NioDatagramAcceptor(null);  
        //��β������ַ��������filter����Ϣֱ����Buffer�ֽ�������  
        acceptor.getFilterChain().addLast("logger", new LoggingFilter());  
        acceptor.setHandler(new UDPHandler());  
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);  
        acceptor.getSessionConfig().setReuseAddress(true);//<span style="font-family: Arial, Helvetica, sans-serif;">NioDatagramAcceptor ����Ϊnull�������ظ�ʹ�ö˿�</span>  
        acceptor.bind(new InetSocketAddress(PORT));  
    }  
    static class UDPHandler extends IoHandlerAdapter{  
        public void messageReceived(IoSession session, Object message)  
                throws Exception {  
            IoBuffer buffer = (IoBuffer)message;  
            System.out.println(buffer.getLong());  
            session.close(false);  
        }  
        public void sessionIdle(IoSession session, IdleStatus status)  
                throws Exception {  
            System.out.println("IDLE " + session.getIdleCount(status));  
        }  
    } 
}
