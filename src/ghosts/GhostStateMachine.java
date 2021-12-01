package ghosts;

import pacman.Game;
import pacman.Move;
import pacman.State;
import ghosts.messages.Message;

public class GhostStateMachine<IntelliChar> {

    private IntelliChar ghost;
    private GhostState<IntelliChar> current;
    private GhostState<IntelliChar> previous;

    private Move move;

    public GhostStateMachine(IntelliChar g){
        ghost = g;
        current = previous = null;
    }

    public void setMove(Move m){
        move = m;
    }
    public Move getMove(){
        return move;
    }

    public void setCurrentState(GhostState<IntelliChar> current){
        this.current = current;
    }
    public void setPreviousState(GhostState<IntelliChar> previous){
        this.current = previous;
    }

    public Move ChooseMove(Game game, int ghostIndex){
        if(current != null){
            current.Execute(ghost, ghostIndex);
            return move;
        }
        return null;
    }

    public void changeState(GhostState<IntelliChar> newState, int ghostIndex){
        previous = current;
        current = newState;
        current.Enter(ghost, ghostIndex);
    }

    public boolean HandleMessage(Message msg){
        if(current.OnMessage(ghost, msg)){
            return true;
        }
        return false;
    }
}
