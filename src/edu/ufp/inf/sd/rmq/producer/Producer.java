package edu.ufp.inf.sd.rmq.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import edu.ufp.inf.sd.rmi.util.tabusearch.TabuSearchJSSP;
import edu.ufp.inf.sd.rmq.util.RabbitUtils;
import edu.ufp.inf.sd.rmq.util.geneticalgorithm.CrossoverStrategies;
import edu.ufp.inf.sd.rmq.util.geneticalgorithm.GeneticAlgorithmJSSP;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * RabbitMQ speaks multiple protocols. This tutorial uses AMQP 0-9-1, which is
 * an open, general-purpose protocol for messaging. There are a number of
 * clients for RabbitMQ in many different languages. We'll use the Java client
 * provided by RabbitMQ.
 * <p>
 * Download client library (amqp-client-4.0.2.jar) and its dependencies (SLF4J
 * API and SLF4J Simple) and copy them into lib directory.
 * <p>
 * Jargon terms:
 * RabbitMQ is a message broker, i.e., a server that accepts and forwards messages.
 * Producer is a program that sends messages (Producing means sending).
 * Queue is a post box which lives inside a RabbitMQ broker (large message buffer).
 * Consumer is a program that waits to receive messages (Consuming means receiving).
 * The server, client and broker do not have to reside on the same host
 *
 * @author rui
 */
public class Producer {

    /*+ name of the queue */
    //public final static String QUEUE_NAME="hello_queue";

    /**
     * Run publisher Send several times from terminal (will send msg "hello world" to Recv):
     * $ ./runproducer
     *
     * Challenge: concatenate several words from command line args (args[3].. args[n]):
     * $ ./runcnsumer hello world again (will concatenate msg "hello world again" to send)
     *
     * @param args
     */
    public static void main(String[] args) {
        RabbitUtils.printArgs(args);

        //Read args passed via shell command
        String host=args[0];
        int port=Integer.parseInt(args[1]);
        //String queueName=args[2];
        String exchangeName=args[2];

        /* try-with-resources will close resources automatically in reverse order... avoids finally */
        try (Connection connection= RabbitUtils.newConnection2Server(host, port, "guest", "guest");
             Channel channel= RabbitUtils.createChannel2Server(connection)) {
            // Declare a queue where to send msg (idempotent, i.e., it will only be created if it doesn't exist);
            //channel.queueDeclare(queueName, false, false, false, null);
            //channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            //String message="Hello World!";

            //

            TabuSearchJSSP tabuSearchJSSP = new TabuSearchJSSP()
            GeneticAlgorithmJSSP geneticAlgorithmJSSP = new GeneticAlgorithmJSSP();
            System.out.println(" [x] Declare exchange: '" + exchangeName + "' of type" + BuiltinExchangeType.FANOUT.toString());
            // Publish a message to the queue (content is byte array encoded with UTF-8)
            channel.exchangeDeclare(exchangeName,BuiltinExchangeType.FANOUT);
            String message = RabbitUtils.getMessage(args,3);

            String routingKey="";
            channel.basicPublish(exchangeName,routingKey,null,message.getBytes("UTF-8"));
            System.out.println(" [x] Sent: '" + message + "'");

        } catch (IOException | TimeoutException e) {
            Logger.getLogger(Producer.class.getName()).log(Level.INFO, e.toString());
        }
        /* try-with-resources will close resources automatically in reverse order, thus avoiding finally clause.
          finally {
            try {
                // Lastly, we close the channel and the connection
                if (channel != null) { channel.close(); }
                if (connection != null) { connection.close(); }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        */
    }
}
