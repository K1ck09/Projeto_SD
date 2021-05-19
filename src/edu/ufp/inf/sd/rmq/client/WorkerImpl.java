package edu.ufp.inf.sd.rmq.client;

import com.rabbitmq.client.*;
import edu.ufp.inf.sd.rmq.server.JobGroupRI;
import edu.ufp.inf.sd.rmq.server.State;
import edu.ufp.inf.sd.rmq.server.User;
import edu.ufp.inf.sd.rmq.util.RabbitUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorkerImpl extends UnicastRemoteObject implements WorkerRI {
    Integer id;
    private final JobShopClient client;
    private State state;
    private final User owner;
    private final String jobGroupName;
    private int bestMakespan= Integer.MAX_VALUE;
    private int totalShares=0;
    private int currentMakespan;
    private int totalRewarded=0;
    private final JobGroupRI JobGroupRI;
    private static final String PATH_FILE="C:\\Users\\danie\\Documents\\GitHub\\Projeto_SD\\src\\edu\\ufp\\inf\\sd\\rmq\\client\\temp\\";
    private File file;
    private static final String HOST="localhost";
    private static final int PORT=5672;
    private static final String ROUTING_KEY="";
    private Channel channel;


    protected WorkerImpl( JobShopClient client,Integer id,User jobOwner, State state,String jobGroupName) throws RemoteException {
        this.id=id;
        this.owner=jobOwner;
        this.client=client;
        this.state=state;
        this.jobGroupName = jobGroupName;
        this.JobGroupRI=client.userSessionRI.getJobList().get(jobGroupName);
    }

    protected WorkerImpl(JobShopClient client,Integer id,User jobOwner, State state,String jobGroupName,boolean var) throws RemoteException {
        this.id=id;
        this.owner=jobOwner;
        this.client=client;
        this.state=state;
        this.jobGroupName = jobGroupName;
        this.JobGroupRI=client.userSessionRI.getJobList().get(jobGroupName);
        Connection connection = null;
        try {
            connection = RabbitUtils.newConnection2Server(HOST, PORT, "guest", "guest");
            assert connection != null;
            this.channel = RabbitUtils.createChannel2Server(connection);
            String exchangeName = String.valueOf(id)+owner+JobGroupRI.getJobName();
            channel.exchangeDeclare(exchangeName, "");

            String queueName=channel.queueDeclare().getQueue();

            String routingKey="";
            channel.queueBind(queueName,exchangeName,routingKey);

            DeliverCallback deliverCallback=(consumerTag, delivery) -> {
                String message=new String(delivery.getBody(), StandardCharsets.UTF_8);
                String[] args= message.split(",");

                Logger.getAnonymousLogger().log(Level.INFO, Thread.currentThread().getName()+": Message received " +message);
                System.out.println(" [x] Received '" + args[1] + "'");
            };
            CancelCallback cancelCallback=(consumerTag) ->{
                System.out.println(" [0] Consumer Tag [" + consumerTag + "] - Cancel Callback invoked");
            };
            channel.basicConsume(queueName, true, deliverCallback, cancelCallback);

        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setOperation() {
        Operations op = new Operations(file.getAbsolutePath(), this);
        Thread t=new Thread(op);
        t.start();
    }

    @Override
    public void changeState(String state) {
        this.state.setCurrentState(state);
    }

    @Override
    public void setState(String state) throws IOException {
        this.state.setCurrentState(state);
        JobGroupRI.updateTotalShares(this);
    }

    @Override
    public synchronized void updateMakeSpan(int makespan) throws IOException {
            this.currentMakespan=makespan;
            //System.out.println("["+id+"] -> "+currentMakespan);
            if(this.bestMakespan>this.currentMakespan){
                this. bestMakespan=this.currentMakespan;
            }
            JobGroupRI.updateTotalShares(this);
    }

    @Override
    public void setFile(String filePath)throws IOException {
        downloadFile(filePath);
        JobGroupRI.updateTotalShares(this);
    }

    private void downloadFile( String filepath) throws IOException {
        byte [] data = JobGroupRI.downloadFileFromServer(filepath);
        this.file=new File(PATH_FILE+this.id+"_"+owner.getUsername()+"_"+jobGroupName);
        FileOutputStream out = new FileOutputStream(this.file);
        out.write(data);
        out.flush();
        out.close();
    }

    public int getCurrentMakespan() {
        return currentMakespan;
    }
    public int getBestMakespan() {
        return bestMakespan;
    }
    public int getTotalShares() {
        return totalShares;
    }
    public void setTotalShares(int totalShares) {
        this.totalShares = totalShares;
    }

    @Override
    public Integer getId() {
        return id;
    }
    @Override
    public State getState() {
        return state;
    }
    @Override
    public User getOwner() {
        return owner;
    }
    @Override
    public String getJobGroupName() {
        return jobGroupName;
    }
    @Override
    public JobShopClient getClient() {
        return client;
    }

    @Override
    public int getTotalRewarded() {
        return totalRewarded;
    }

    public void setTotalRewarded(int totalRewarded) {
        this.totalRewarded = totalRewarded;
    }
}
