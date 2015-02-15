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
	public void send(Message message) {
		// TODO Auto-generated method stub
		
	}
	public void receive(Message mes) {
		// TODO Auto-generated method stub
		mp.messageRec.offer(mes);
	}
}
