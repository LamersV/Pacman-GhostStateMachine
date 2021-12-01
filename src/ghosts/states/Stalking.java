package ghosts.states;

import ghosts.GhostState;
import pacman.*;
import util.Utils;

import java.util.List;

public class Stalking implements GhostState<GhostPlayer> {

    int ghostIndex;

    private static Stalking instance = null;

    public static Stalking getInstance(){
        if(instance == null){
            instance = new Stalking();
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
                Location lo = state.getGhostLocations().get(0);
                distances[i] = Location.euclideanDistance(lo, newLoc);
            }
            int moveIndex = Utils.argmin(distances); // the move that minimizes the distance to PacMan
            g.getStateMachine().setMove(moves.get(moveIndex));
        }
    }

    @Override
    public void Enter(GhostPlayer g, int ghostIndex) {
        System.out.println(g.getName() + " is stalking!");
        this.ghostIndex = ghostIndex;
        Execute(g, ghostIndex);
    }

    @Override
    public void Exit(GhostPlayer g, int ghostIndex) {

    }

    @Override
    public boolean OnMessage(GhostPlayer g, ghosts.messages.Message msg) {
        if(msg.getMessage().equals("Stalk")){
            g.getStateMachine().changeState(Stalking.getInstance(), ghostIndex);
            return true;
        }
        return false;
    }
}
