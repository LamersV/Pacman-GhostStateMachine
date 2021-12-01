package ghosts.states;

import ghosts.GhostState;
import messages.MessageDispatcher;
import pacman.*;
import util.Utils;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Drunk implements GhostState<GhostPlayer> {

    int ghostIndex;
    private boolean isStart = true;
    private Random random = new Random();

    private static Drunk instance = null;
    public static Drunk getInstance(){
        if(instance == null){
            instance = new Drunk();
        }
        return instance;
    }

    @Override
    public void Execute(GhostPlayer g, int ghostIndex) {
        setGhostIndex(ghostIndex);
        if(ghostIndex == 0){ this.ghostIndex = ghostIndex; }
        Game game = Game.getGame();
        State state = game.getCurrentState();
        List<Move> legalMoves = game.getLegalGhostMoves(ghostIndex);
        Move newMove;
        if (legalMoves.isEmpty()){
            g.getStateMachine().setMove(null);
        }else{
            double[] distances = new double[legalMoves.size()];
            Location pacManLoc = state.getPacManLocation();
            for (int i=0; i<distances.length; i++) {
                Location newLoc = Game.getNextLocation(state.getGhostLocations().get(ghostIndex), legalMoves.get(i));
                distances[i] = Location.euclideanDistance(pacManLoc, newLoc);
            }
            int moveIndex = Utils.argmin(distances);
            if(!isStart){
                int rd = random.nextInt(7);
                if(rd % 2 == 0){
                    newMove = g.getNewMove(legalMoves, moveIndex);
                    g.getStateMachine().setMove(newMove);
                }
            }
            isStart = false;
            g.getStateMachine().setMove(legalMoves.get(moveIndex));
        }
        if(game.getPoints() % 200 == 0){
            GhostPlayer ghost = g.getRandomGhost(game, g);
            MessageDispatcher.getInstance().DispatchMessage(g, ghost, "Stalk");
        }
        if (game.getPoints() >= 400){
            Exit(g, ghostIndex);
        }
    }

    @Override
    public void Enter(GhostPlayer g, int ghostIndex) {
        System.out.println(g.getName() + " is drunked!");
        setGhostIndex(ghostIndex);
        Execute(g, ghostIndex);
    }

    private void setGhostIndex(int ghostIndex){
        this.ghostIndex = ghostIndex;
    }

    @Override
    public void Exit(GhostPlayer g, int ghostIndex) {
        g.getStateMachine().changeState(Stalking.getInstance(), ghostIndex);
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
