import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Manager {
	public static void main(String[] args) throws IOException, InterruptedException{
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		MessagePasser messagePasser = new MessagePasser(args[0], args[1],false);
		int seq=0;
		while(true){
			System.out.println("Enter the command : send or rec");
			String cm = in.readLine();
			String[] hold = cm.split("#");
			messagePasser.log=false;
			switch(hold[0]){
				case "send":
					if(hold.length!=6)
					{
						System.err.println("wrong send command!\n");
						System.out.println("usage: send#bob#Action#kind#what is your name#y");
						break;
					}
					Message message = new Message(args[1],hold[1],hold[2], hold[3],hold[4]);
					message.set_seqNum(seq++);
					if(hold[5].equals("y"))
						messagePasser.log=true;
					messagePasser.send(message);
					break;
				case "rec":
					System.out.println(messagePasser.receive().toString());
					break;
				case "lrec":
					messagePasser.log=true;
					//System.out.println(messagePasser.log);
					System.out.println(messagePasser.receive().toString());
					break;
				case "print":
					System.out.println(messagePasser.printTimestamp());
					break;
				default:
					System.err.println("Illegal input format! Please enter again!");
				}
		}
	}
}
