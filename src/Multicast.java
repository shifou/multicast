import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;


public class Multicast {
	MessagePasser mp;
	HashMap<String, int[]> vectorMap = new HashMap<>();
	HashMap<String,LinkedList<Message>> holdBackQueueList = new HashMap<String,LinkedList<Message>>();
	public Multicast(MessagePasser messagePasser){
		this.mp = messagePasser;
		for(String hold : mp.groups.keySet()){
			int len=mp.groups.get(hold).size();
			int[] groupVector = new int[len];
			for(int j=0; j<len; j++){
				groupVector[j] = 0;
			}
			vectorMap.put(hold, groupVector);
			holdBackQueueList.put(hold,new LinkedList<Message>());
			
		}
	}
	public void send(Message message) throws FileNotFoundException {
		int length = vectorMap.get(message.groupName).length;
		int[] tmp = new int[length];
		for(int i=0; i<length; i++){
			tmp[i] =  (vectorMap.get(message.groupName))[i];
		}
		tmp[mp.u2i.get(message.src)]+=1;
		message.setMulticastVector(tmp);
		for(String dest : mp.groups.get(message.groupName)){
			if(!dest.equalsIgnoreCase(message.src)){
				message.des = dest;
				Message hold = message.clone(message);
				mp.send(hold);
			}
		}
	}
	public void receive(Message mes) {
		// TODO Auto-generated method stub
		int[] recVec = mes.multicastVector;
		int length = mes.groupSize;
		int[] curVec = new int[length];
		for(int i=0; i<length; i++){
			curVec[i] =  (vectorMap.get(mes.groupName))[i];
		}
		String check = judge(curVec, recVec);
		switch(check){
		case "rec":
			System.out.println("receive multicast");
			mp.messageRec.offer(mes);
			break;
		case "drop":
			System.out.println("drop multicast");
			break;
		case "hold":
			System.out.println("holdback multicast");
			insert(this.holdBackQueueList.get(mes.groupName),mes);
			break;
		}
	}
	private void insert(LinkedList<Message> linkedList, Message mes) {
		// TODO Auto-generated method stub
		
	}
	private String judge(int[] curVec, int[] recVec) {
		// TODO Auto-generated method stub
		return null;
	}
}
