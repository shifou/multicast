import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;


public class Multicast {
	MessagePasser mp;
	HashMap<String, int[]> vectorMap = new HashMap<>();
	ArrayList<LinkedList<Message>> holdBackQueueList = new ArrayList<>();
	public Multicast(MessagePasser messagePasser){
		this.mp = messagePasser;
		for(int i=0; i<mp.groups.size(); i++){
				holdBackQueueList.add(new LinkedList<Message>());
				
			}
		for(String hold : mp.groups.keySet()){
			int len=mp.groups.get(hold).size();
			int[] groupVector = new int[len];
			for(int j=0; j<len; j++){
				groupVector[j] = 0;
			}
			vectorMap.put(hold, groupVector);
		}
	}
	public void send(Message message) throws FileNotFoundException {
		int length = vectorMap.get(message.groupName).length;
		int[] tmp = new int[length];
		for(int i=0; i<length; i++){
			tmp[i] =  (vectorMap.get(message.groupName))[i];
		}
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
		mp.messageRec.offer(mes);
	}
}
