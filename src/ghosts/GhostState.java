package ghosts;

import pacman.Game;
import ghosts.messages.Message;

public interface GhostState<IntelliChar> {

    public void Execute(IntelliChar g, int ghostIndex);
    public void Enter(IntelliChar g, int ghostIndex);
    public void Exit(IntelliChar g, int ghostIndex);
    public boolean OnMessage(IntelliChar g, Message msg);

}
