import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
public class Connection implements Runnable {
	private Socket socket;
	ConcurrentLinkedQueue messageQueue;
	private volatile boolean running;
	public Vector<Message> messageRec = new Vector<Message>();
	private ObjectInputStream objInput;
	private ObjectOutputStream objOutput;
	public ConcurrentHashMap<String, Socket> sk;
	public ConcurrentHashMap<String, ObjectOutputStream> st;
	public ArrayList<LogicalTimeStamp> logMat;
	public ArrayList<VectorTimeStamp> vecMat;
	public boolean logicalTime;
	public String name;
	public boolean log;
	public Connection(String name,Socket slaveSocket, ObjectOutputStream out, ObjectInputStream objInput2, ConcurrentLinkedQueue mq, ConcurrentHashMap<String, Socket> sk, ConcurrentHashMap<String, ObjectOutputStream> st) throws IOException {
		// TODO Auto-generated constructor stub
		socket = slaveSocket;
		objOutput = out;
		this.name=name;
		objOutput.flush();
		objInput = objInput2;
		running=true;
		log=false;
		this.sk=sk;
		this.st=st;
		messageQueue=mq;
	}
	public Connection(String name,Socket slaveSocket, ObjectOutputStream out, ObjectInputStream objInput2, Vector<Message> messageRec,boolean logicalTime, ConcurrentHashMap<String, Socket> sk2, ConcurrentHashMap<String, ObjectOutputStream> st2) throws IOException 
	{
		sk=sk2;
		st=st2;
		this.name=name;
		socket = slaveSocket;
		objOutput = out;
		objOutput.flush();
		objInput = objInput2;
		running=true;
		this.messageRec=messageRec;
		this.logicalTime=logicalTime;
		log=true;
		
	}
	public void run() {
		try {
			Message mes;
			while (running) {
				try {
					mes = (Message) objInput.readObject();
					if(log)
					System.out.println("logger rec: "+mes.toString());
					if(log==false)
						messageQueue.offer(mes);
					else
					{
						messageRec.add(mes);
					}
					
				} catch (ClassNotFoundException e) {
					System.out.println("read disconnected message");
					return;
				}
				catch(EOFException e)
				{
					sk.remove(name);
		            st.remove(name);
					System.out.println("detect disconnected message remove "+name);
					return;
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("message error in handle");

		}
	}
	
}
