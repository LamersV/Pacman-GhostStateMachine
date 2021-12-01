package ghosts.states;

import ghosts.GhostState;
import ghosts.StateGhostPlayer;
import pacman.*;

import java.awt.*;
import java.util.List;

public class WorstMoving implements GhostState<GhostPlayer> {

    int ghostIndex;

    private Move lastMove = null;

    private static WorstMoving instance = null;

    public static WorstMoving getInstance(){
        if(instance == null){
            instance = new WorstMoving();
        }
        return instance;
    }

    @Override
    public void Execute(GhostPlayer g, int ghostIndex) {
        if(ghostIndex == 0){ this.ghostIndex = ghostIndex; }
        Game game = Game.getGame();
        State s = game.getCurrentState();
        Location target = new StateGhostPlayer().getTarget(s, ghostIndex);
        Location oldLoc = s.getGhostLocations().get(ghostIndex);
        List<Move> legalMoves = game.getLegalGhostMoves(ghostIndex);
        Move worstMove = null;
        double maxDistance = Double.NEGATIVE_INFINITY;
        for (Move m : legalMoves) {
            Location newLoc = Game.getNextLocation(oldLoc, m);
            double distance = Location.euclideanDistance(newLoc, target);
            if (distance>maxDistance) {
                maxDistance = distance;
                worstMove = m;
            }
        }
        if (worstMove ==null) throw new RuntimeException("Legal moves for ghost " +ghostIndex+": " + legalMoves);
        lastMove = worstMove;
        g.getStateMachine().setMove(worstMove);

        if(game.getTime() % 15 == 0){
            g.setColor(Color.gray);
        }else if(game.getTime() % 31 == 0){
            g.setColor(Color.pink);
        }

        if(game.getTime() % 69 == 0){
            Exit(g, ghostIndex);
        }
    }

    @Override
    public void Enter(GhostPlayer g, int ghostIndex) {
        System.out.println(g.getName() + " is doing the worst move!");
        this.ghostIndex = ghostIndex;
        Execute(g, ghostIndex);
    }

    @Override
    public void Exit(GhostPlayer g, int ghostIndex) {
        g.getStateMachine().changeState(Rotating.getInstance(), ghostIndex);
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
