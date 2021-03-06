import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPooledSSLServer implements Runnable {
	
	protected int					 serverPort = 443;
	protected SSLServerSocketFactory serverSockerFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
	protected SSLServerSocket		 serverSocket = null;
	protected boolean 				 isStopped = false;
	protected Thread				 runningThread = null;
	protected ExecutorService		 threadPool = Executors.newFixedThreadPool(10);
	
	public ThreadPooledSSLServer(int port) {
		this.serverPort = port;
	}
	
    public void run(){
        synchronized(this) {
            this.runningThread = Thread.currentThread();
        }
        
        openServerSocket();
        while(! isStopped()){
            SSLSocket clientSocket = null;
            try {
                clientSocket = (SSLSocket) this.serverSocket.accept();
            } catch (IOException e) {
                if(isStopped()) {
                    System.out.println("Server Stopped.") ;
                    return;
                }
                throw new RuntimeException(
                    "Error accepting client connection", e);
            }
            this.threadPool.execute(
                new WorkerRunnable(clientSocket,
                    "Thread Pooled Server"));
        }
        System.out.println("Server Stopped.") ;
    }


    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop(){
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {
        try {
            this.serverSocket = (SSLServerSocket) serverSockerFactory.createServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + this.serverPort, e);
        }
    }
}
