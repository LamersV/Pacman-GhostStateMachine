package ghosts.states;

import ghosts.GhostState;
import pacman.*;
import util.Utils;

import java.util.Collections;
import java.util.List;

public class Desperate implements GhostState<GhostPlayer> {
    int ghostIndex;

    private static Desperate instance = null;

    public static Desperate getInstance(){
        if(instance == null){
            instance = new Desperate();
        }
        return instance;
    }

    @Override
    public void Execute(GhostPlayer g, int ghostIndex) {
        if(ghostIndex == 0){ this.ghostIndex = ghostIndex; }
        Game game = Game.getGame();
        List<Move> moves = game.getLegalGhostMoves(ghostIndex);
        if (moves.isEmpty()){
            g.getStateMachine().setMove(null);
        }else{
            Collections.shuffle(moves);
            g.getStateMachine().setMove(moves.get(0));
        }
    }

    @Override
    public void Enter(GhostPlayer g, int ghostIndex) {
        System.out.println(g.getName() + " is desperate!");
        this.ghostIndex = ghostIndex;
        Execute(g, ghostIndex);
    }

    @Override
    public void Exit(GhostPlayer g, int ghostIndex) {

    }

    @Override
    public boolean OnMessage(GhostPlayer g, ghosts.messages.Message msg) {
        if(msg.getMessage().equals("Stalk")){
            System.out.println("AHHHHHHHHHHHHHHHHH!");
            return true;
        }
        return false;
    }
}
