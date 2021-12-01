package ghosts;

import pacman.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class StateGhostPlayer extends GhostPlayer {
    private static int periodLength = 40;
    private static int numPeriodTypes = 10;

    private Location target = null; // if null, then it's pacman himself
    private Random random = new Random();

    @Override
    public Move chooseMove(Game game, int ghostIndex) {
        return getStateMachine().ChooseMove(game, ghostIndex);
    }

    public Location getTarget(State s, int ghostIndex) {
        int step = s.getHistory().size()-1;
        int numGhosts = s.getGhostLocations().size();
        if (step%periodLength==(ghostIndex*(periodLength/numGhosts))) { // this makes ghosts change behavior at different points in the period
            // pick a new target
            int period = step/periodLength;
            int type = period%numPeriodTypes;
            if (type==numPeriodTypes-(ghostIndex*2)-1) { // one of the types
                // pick pacman as the target
                target = null;
            } else if (type==numPeriodTypes-(ghostIndex*2)-2) { // another of the types
                // pick one of the remaining dots as the target
                List<Location> dotList = new ArrayList<Location>(s.getDotLocations());
                target = dotList.get((step+ghostIndex)%dotList.size());
            } else { // the rest of the types
                // otherwise, pick any location to protect
                List<Location> dotList = new ArrayList<Location>(Game.getAllLocations());
                target = dotList.get((step+ghostIndex)%dotList.size());
            }
//      System.err.println("Ghost " + ghostIndex + " picked new target: " + target);
        }
        if (target!=null) return target;
        return s.getPacManLocation();
    }

    public boolean pinkyIsAlive(){
        List<GhostPlayer> ghosts = Game.getGame().getGhostPlayers();
        for (GhostPlayer ghost: ghosts) {
            if(ghost.getColor() == Color.pink){
                return true;
            }
        }
        return false;
    }

    public GhostPlayer getRandomGhost(Game g, GhostPlayer thisGhost){
        List<GhostPlayer> ghosts = g.getGhostPlayers();
        Collections.shuffle(ghosts);
        for (GhostPlayer ghost: ghosts) {
            if(ghost != thisGhost){
                return ghost;
            }
        }
        return null;
    }

    public Move getNewMove(List<Move> legalMoves, int index){
        Move m = legalMoves.get(index);
        int i = random.nextInt(legalMoves.size());
        if(legalMoves.get(i) == m){
            Collections.shuffle(legalMoves);
            return legalMoves.get(0);
        }else{
            m = legalMoves.get(i);
            return m;
        }
    }
}
