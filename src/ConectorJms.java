//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package br.com.caelum;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ConectorJms {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS");
    private final InitialContext context;
    private final Connection conexao;
    private final Destination fila;

    public ConectorJms() {
        try {
            this.context = this.createInitialContext();
            ConnectionFactory cf = (ConnectionFactory)this.context.lookup("ConnectionFactory");
            this.conexao = cf.createConnection();
            this.fila = (Destination)this.context.lookup("financeiro");
        } catch (JMSException | NamingException var2) {
            throw new RuntimeException(var2);
        }
    }

    public void enviaMensagens(int max) {
        try {
            Session sessao = this.conexao.createSession(false, 1);
            MessageProducer producer = sessao.createProducer(this.fila);
            System.out.println("Enviando " + max + " menagem(ns)");

            for(int i = 0; i < max; ++i) {
                Message msg = sessao.createTextMessage("Mensagem " + (i + 1) + " (" + this.geraHora() + ")");
                producer.send(msg);
            }

            sessao.close();
        } catch (JMSException var6) {
            throw new RuntimeException(var6);
        }
    }

    private String geraHora() {
        return SDF.format(new Date());
    }

    public void close() {
        try {
            this.conexao.close();
            this.context.close();
        } catch (NamingException | JMSException var2) {
            throw new RuntimeException(var2);
        }
    }

    private InitialContext createInitialContext() throws NamingException {
        Properties props = new Properties();
        props.setProperty("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        props.setProperty("java.naming.provider.url", "tcp://localhost:61616");
        props.setProperty("queue.financeiro", "fila.financeiro");
        InitialContext ctx = new InitialContext(props);
        return ctx;
    }

    public void consomeMensagens() {
        try {
            Session sessao = this.conexao.createSession(false, 1);
            MessageConsumer consumer = sessao.createConsumer(this.fila);
            this.conexao.start();
            consumer.setMessageListener(new MessageListener() {
                public void onMessage(Message message) {
                    TextMessage textMessage = (TextMessage)message;

                    try {
                        System.out.println("Recebendo mensagem: " + textMessage.getText());
                    } catch (JMSException var4) {
                        throw new RuntimeException(var4);
                    }
                }
            });
            this.aguardeEnter();
            sessao.close();
        } catch (JMSException var3) {
            throw new RuntimeException(var3);
        }
    }

    private void aguardeEnter() {
        System.out.println("Aperte enter para finalizar");
        Scanner s = new Scanner(System.in);
        s.nextLine();
        s.close();
    }
}
