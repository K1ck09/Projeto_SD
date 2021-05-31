package edu.ufp.inf.sd.rmq.consumer;

/**
 * Consumer will keep running to listen for messages from queue and prInteger them out.
 * 
 * DefaultConsumer is a class implementing the Consumer interface, used to buffer 
 * the messages pushed to us by the server.
 * 
 * Compile with RabbitMQ java client on the classpath:
 *  javac -cp amqp-client-4.0.2.jar RPCServer.java RPCClient.java
 * 
 * Run with need rabbitmq-client.jar and its dependencies on the classpath.
 *  java -cp .:amqp-client-4.0.2.jar:slf4j-api-1.7.21.jar:slf4j-simple-1.7.22.jar Recv
 *  java -cp .:amqp-client-4.0.2.jar:slf4j-api-1.7.21.jar:slf4j-simple-1.7.22.jar Producer
 * 
 * OR
 * export CP=.:amqp-client-4.0.2.jar:slf4j-api-1.7.21.jar:slf4j-simple-1.7.22.jar
 * java -cp $CP Producer
 * java -cp %CP% Producer
 * 
 * The client will prInteger the message it gets from the publisher via RabbitMQ.
 * The client will keep running, waiting for messages (Use Ctrl-C to stop it).
 * Try running the publisher from another terminal.
 *
 * Check RabbitMQ Broker runtime info (credentials: guest/guest4rabbitmq):
 *  http://localhost:15672/
 * 
 * 
 * @author rui
 */

import com.rabbitmq.client.*;
import edu.ufp.inf.sd.rmq.producer.Producer;
import edu.ufp.inf.sd.rmq.util.RabbitUtils;

import java.util.logging.Level;
import java.util.logging.Logger;


public class Consumer {

    /**
     * Run consumer Recv that keeps running, waiting for messages from publisher Send (Use Ctrl-C to stop):
     * $ ./runconsumer
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            RabbitUtils.printArgs(args);

            //Read args passed via shell command
            String host=args[0];
            int port=Integer.parseInt(args[1]);
            //String queueName=args[2];
            String exchangeName=args[2];

            // Open a connection and a channel to rabbitmq broker/server
            Connection connection=RabbitUtils.newConnection2Server(host, port, "guest", "guest");
            Channel channel=RabbitUtils.createChannel2Server(connection);

            //Declare queue from which to consume (declare it also here, because consumer may start before publisher)
            //channel.queueDeclare(queueName, false, false, false, null);
            //channel.queueDeclare(Send.QUEUE_NAME, true, false, false, null);

            /* Use the Exchange FANOUT type: broadcasts all messages to all queues */

            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT);

            /*Create a non-durable, exclusive,autodelete queue with a generated name. */
            String queueName=channel.queueDeclare().getQueue();

            /*Create binding: tell exchange to send messages to a queue; */
            String routingKey="";
            channel.queueBind(queueName,exchangeName,routingKey);

            Logger.getAnonymousLogger().log(Level.INFO, Thread.currentThread().getName()+": Will create Deliver Callback...");
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            // Publisher pushes messages asynchronously, hence use DefaultConsumer callback
            //   that buffers messages until consumer is ready to handle them.
            /*Consumer client = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message=new String(body, "UTF-8");
                    System.out.println(" [x] Received '" + message + "'");
                }
            };
            channel.basicConsume(queueName, true, client);
            */


            boolean run = true;

            //DeliverCallback is an handler callback (lambda method) to consume messages pushed by the sender.
            //Create an handler callback to receive messages from queue
            DeliverCallback deliverCallback=(consumerTag, delivery) -> {
                String message=new String(delivery.getBody(), "UTF-8");
                Logger.getAnonymousLogger().log(Level.INFO, Thread.currentThread().getName()+": Message received " +message);
                System.out.println(" [x] Consumer Tag [ " + consumerTag + "] - Received '" + message + "'");
            };
            CancelCallback cancelCallback=(consumerTag) -> {
                System.out.println(" [x] Consumer Tag [ " + consumerTag + "] - Cancel Callback invoked'");
            };
            //Associate callback with channel queue
            channel.basicConsume(queueName, true, deliverCallback, cancelCallback);

        } catch (Exception e) {
            //Logger.getLogger(Recv.class.getName()).log(Level.INFO, e.toString());
            e.printStackTrace();
        }
    }
}
