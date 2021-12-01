package ghosts.states;

import ghosts.GhostState;
import pacman.*;
import util.Utils;

import java.util.List;

public class Escaping implements GhostState<GhostPlayer> {

    int ghostIndex;
    private static Escaping instance = null;

    public static Escaping getInstance(){
        if(instance == null){
            instance = new Escaping();
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
            int moveIndex = Utils.argmax(distances);
            g.getStateMachine().setMove(moves.get(moveIndex));
        }
        if(game.getLives() < 2){
            Exit(g, ghostIndex);
        }
    }

    @Override
    public void Enter(GhostPlayer g, int ghostIndex) {
        System.out.println(g.getName() + " is escaping!");
        this.ghostIndex = ghostIndex;
        Execute(g, ghostIndex);
    }

    @Override
    public void Exit(GhostPlayer g, int ghostIndex) {
        g.getStateMachine().changeState(Desperate.getInstance(), ghostIndex);
    }

    @Override
    public boolean OnMessage(GhostPlayer g, ghosts.messages.Message msg) {
        if(msg.getMessage().equals("Stalk")){
            System.out.println("I'm feared! Not now!");
            return true;
        }
        return false;
    }
}
