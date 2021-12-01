package ghosts.states;

import ghosts.GhostState;
import pacman.*;
import util.Utils;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Rotating implements GhostState<GhostPlayer> {

    int ghostIndex;

    private Random random = new Random();
    List<Location> locations = null;
    Location location = null;

    private static Rotating instance = null;
    public static Rotating getInstance(){
        if(instance == null){
            instance = new Rotating();
        }
        return instance;
    }

    public int getGhostIndex() {
        return ghostIndex;
    }

    public void setGhostIndex(int ghostIndex) {
        this.ghostIndex = ghostIndex;
    }

    @Override
    public void Execute(GhostPlayer g, int ghostIndex) {
        setLocation();
        if(ghostIndex == 0){ this.ghostIndex = ghostIndex; }
        Game game = Game.getGame();
        State state = game.getCurrentState();
        List<Move> moves = game.getLegalGhostMoves(ghostIndex);
        if (moves.isEmpty()){
            g.getStateMachine().setMove(null);
        }else{
            double[] distances = new double[moves.size()];
            for (int i=0; i<distances.length; i++) {
                Location newLoc = Game.getNextLocation(state.getGhostLocations().get(ghostIndex), moves.get(i));
                distances[i] = Location.euclideanDistance(location, newLoc);
            }
            int moveIndex = Utils.argmin(distances);
            g.getStateMachine().setMove(moves.get(moveIndex));
        }

        if(game.getTime() % 70 == 0){
            Exit(g, ghostIndex);
        }
    }

    @Override
    public void Enter(GhostPlayer g, int ghostIndex) {
        this.ghostIndex = ghostIndex;
        setLocation();
        System.out.println(g.getName() + " is rotating towards " + location);
        Execute(g, ghostIndex);
    }

    @Override
    public void Exit(GhostPlayer g, int ghostIndex) {
        g.getStateMachine().changeState(WorstMoving.getInstance(), ghostIndex);
    }

    private void setLocation(){
        if(location == null){
            location = new Location(2, 26);
        }
    }
    @Override
    public boolean OnMessage(GhostPlayer g, ghosts.messages.Message msg) {
        if(msg.getMessage().equals("Stalk")){
            System.out.println("Stop messaging me!");
            return true;
        }
        return false;
    }
}
