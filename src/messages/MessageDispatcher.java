package messages;

import pacman.GhostPlayer;
import ghosts.messages.Message;

public class MessageDispatcher{
    private static MessageDispatcher instance = null;

    public static MessageDispatcher getInstance(){
        if(instance == null){
            instance = new MessageDispatcher();
        }
        return instance;
    }

    public void DispatchMessage(GhostPlayer sender, GhostPlayer receiver, String msg){
        Message message = new Message(sender, receiver, msg);

        DeliverMessage(receiver, message);
    }

    private void DeliverMessage(GhostPlayer receiver, Message msg){
        if(!receiver.HandleMessage(msg)){
            System.out.println("Error 404");
        }
    }
    
}