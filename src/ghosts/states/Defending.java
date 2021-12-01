package ghosts.states;

import ghosts.GhostState;
import pacman.*;
import ghosts.messages.Message;
import util.Utils;

import java.awt.*;
import java.util.List;

public class Defending implements GhostState<GhostPlayer> {

    int ghostIndex;

    private static Defending instance = null;
    public static Defending getInstance(){
        if(instance == null){
            instance = new Defending();
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

            Location location = g.getBaseLocation(game, state);
            for (int i=0; i<distances.length; i++) {
                Location newLoc = Game.getNextLocation(state.getGhostLocations().get(ghostIndex), moves.get(i));
                distances[i] = Location.euclideanDistance(location, newLoc);
            }
            int moveIndex = Utils.argmin(distances); // the move that minimizes the distance to PacMan
            g.getStateMachine().setMove(moves.get(moveIndex));
        }
        if(!g.pinkyIsAlive()){
            Exit(g, ghostIndex);
        }
    }

    @Override
    public void Enter(GhostPlayer g, int ghostIndex) {
        System.out.println(g.getName() + " is defending base!");
        this.ghostIndex = ghostIndex;
        Execute(g, ghostIndex);
    }

    @Override
    public void Exit(GhostPlayer g, int ghostIndex) {
        g.getStateMachine().changeState(Revenging.getInstance(), ghostIndex);
    }

    @Override
    public boolean OnMessage(GhostPlayer g, Message msg) {
        if(msg.getMessage().equals("Stalk")){
            System.out.println("Not now, I'm defending the base!");
            return true;
        }
        return false;
    }
}
