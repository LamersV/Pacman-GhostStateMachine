package ghosts.messages;

import pacman.GhostPlayer;

public class Message{
    public GhostPlayer sender;
    public GhostPlayer receiver;
    private String msg;

    public Message(GhostPlayer sender, GhostPlayer receiver, String msg){
        this.sender = sender;
        this.receiver = receiver;
        this.msg = msg;
    }

    public String getMessage(){
        return msg;
    }
}