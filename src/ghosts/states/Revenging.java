package ghosts.states;

import ghosts.GhostState;
import ghosts.StateGhostPlayer;
import pacman.*;
import util.Utils;

import java.util.List;

public class Revenging implements GhostState<GhostPlayer> {

    int ghostIndex;

    private static Revenging instance = null;

    public static Revenging getInstance(){
        if(instance == null){
            instance = new Revenging();
        }
        return instance;
    }

    @Override
    public void Execute(GhostPlayer g, int ghostIndex) {
        if(ghostIndex == 0){ this.ghostIndex = ghostIndex; }
        Game game = Game.getGame();
        State state = game.getCurrentState();
        List<Move> moves = game.getLegalGhostMoves(ghostIndex);
        if (moves.isEmpty()){
            g.getStateMachine().setMove(null);
        }else{
            double[] distances = new double[moves.size()];
            Location pacManLoc = state.getPacManLocation();
            for (int i=0; i<distances.length; i++) {
                Location newLoc = Game.getNextLocation(state.getGhostLocations().get(ghostIndex), moves.get(i));
                distances[i] = Location.euclideanDistance(pacManLoc, newLoc);
            }
            int moveIndex = Utils.argmin(distances); // the move that minimizes the distance to PacMan
            g.getStateMachine().setMove(moves.get(moveIndex));
        }
        if(g.pinkyIsAlive()){
            Exit(g, ghostIndex);
        }
    }

    @Override
    public void Enter(GhostPlayer g, int ghostIndex) {
        System.out.println(g.getName() + " is revenging Pink!");
        this.ghostIndex = ghostIndex;
        Execute(g, ghostIndex);
    }

    @Override
    public void Exit(GhostPlayer g, int ghostIndex) {
        g.getStateMachine().changeState(Defending.getInstance(), ghostIndex);
    }

    @Override
    public boolean OnMessage(GhostPlayer g, ghosts.messages.Message msg) {
        if(msg.getMessage().equals("Stalk")){
            System.out.println("I will revenge pink, f..k your message!");
            return true;
        }
        return false;
    }
}

