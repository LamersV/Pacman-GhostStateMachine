package ghosts.states;

import ghosts.GhostState;
import ghosts.messages.Message;
import pacman.*;
import util.Utils;

import java.awt.*;
import java.util.List;

public class Colorchanger implements GhostState<GhostPlayer> {

    private static Colorchanger instance = null;
    public static Colorchanger getInstance(){
        if(instance == null){
            instance = new Colorchanger();
        }
        return instance;
    }

    @Override
    public void Execute(GhostPlayer g, int ghostIndex) {
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
        if (game.getTime() % 15 == 0){
            g.setColor(Color.WHITE);
        }
        if(game.getTime() % 31 == 0){
            g.setColor(Color.pink);
        }
    }

    @Override
    public void Enter(GhostPlayer g, int ghostIndex) {
        System.out.println("Mudou de cor");
        Execute(g, ghostIndex);
    }

    @Override
    public void Exit(GhostPlayer g, int ghostIndex) {

    }

    @Override
    public boolean OnMessage(GhostPlayer g, Message msg) {
        return false;
    }
}
