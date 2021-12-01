package pacman;

import java.awt.Color;
import java.util.List;
import java.util.ArrayList;

import ghosts.GhostState;
import ghosts.GhostStateMachine;
import util.Pair;
import ghosts.messages.Message;

/**
 * An abstract class for class that can choose a ghost's moves in the Pac-Man game.
 * @author grenager
 *
 */
public abstract class GhostPlayer {

  private Color color = Color.PINK;
  private String name =  "";
  private GhostStateMachine<GhostPlayer> stateMachine = new GhostStateMachine<>(this);

  public abstract Move chooseMove(Game game, int ghostIndex);

  /* Default implementation for deterministic ghosts...will be overridden
   * in nondeterministic ghosts...
   */
  public List<Pair<Move, Double>> getMoveDistribution(Game game, State state, int ghostIndex) {
  		List<Pair<Move, Double>> distribution = new ArrayList<Pair<Move, Double>>();
  		Move choice = chooseMove(game, ghostIndex);
  		for(Move move : Game.getLegalGhostMoves(state, ghostIndex)) {
  			if(move.equals(choice)) {
  				distribution.add(new Pair<Move, Double>(move, 1.0));
  			} else {
  				distribution.add(new Pair<Move, Double>(move, 0.0));
  			}
  		}
  		return distribution;
  }

  public Color getColor() {
	  return color;
  }

  public void setColor(Color color) {
	  this.color = color;
  }

  public String getName() {
	  return name;
  }

  public void setName(String name) {
	  this.name = name;
  }

  public GhostStateMachine<GhostPlayer> getStateMachine(){
        return stateMachine;
    }

  public void setState(GhostState<GhostPlayer> state){
        stateMachine.setCurrentState(state);
  }

  public Location getBaseLocation(Game game, State s){
      List<GhostPlayer> ghosts = game.getGhostPlayers();
      GhostPlayer pinky = null;
      for (GhostPlayer ghost: ghosts) {
          if(ghost.getColor() == Color.PINK){
              pinky = ghost;
              break;
          }
      }
      if(pinky != null) {
          return new Location(game.getGhostPen().getX() + 1, game.getGhostPen().getY());
      }else{
          return s.getPacManLocation();
      }
  }

  public boolean HandleMessage(Message msg) {
      return stateMachine.HandleMessage(msg);
  }

    public abstract boolean pinkyIsAlive();
    public abstract GhostPlayer getRandomGhost(Game game, GhostPlayer g);
    public abstract Move getNewMove(List<Move> legalMoves, int moveIndex);
}
