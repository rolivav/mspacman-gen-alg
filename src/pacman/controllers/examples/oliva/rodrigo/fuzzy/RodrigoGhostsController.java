package pacman.controllers.examples.oliva.rodrigo.fuzzy;

import java.util.EnumMap;
import java.util.Random;

import com.fuzzylite.defuzzifier.Centroid;
import com.fuzzylite.norm.s.Maximum;
import com.fuzzylite.norm.t.Minimum;
import com.fuzzylite.rule.Rule;
import com.fuzzylite.rule.RuleBlock;
import com.fuzzylite.term.*;
import com.fuzzylite.variable.InputVariable;
import com.fuzzylite.variable.OutputVariable;
import pacman.controllers.Controller;
import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import com.fuzzylite.*;
import pacman.game.internal.PacMan;

import static pacman.game.Constants.*;

/*
 * The Class AggressiveGhosts.
 */
public final class RodrigoGhostsController extends Controller<EnumMap<GHOST,MOVE>>
{
    private EnumMap<GHOST,MOVE> myMoves=new EnumMap<GHOST,MOVE>(GHOST.class);
    private Random rnd=new Random();
    private MOVE[] moves=MOVE.values();

    Engine engine;
    InputVariable pacman;
    OutputVariable danger;

    public void initFuzzy()
    {
        engine = new Engine();
        engine.setName("OH NO, HE'S GONNA EAT THAT POWERPIU");

        pacman = new InputVariable();
        pacman.setEnabled(true);
        pacman.setName("PACMAN");
        pacman.setRange(0.000, 1.000);
        pacman.addTerm(new Trapezoid("OnPowerPill", 0.000, 0.000, 0.100, 0.200));
        pacman.addTerm(new Rectangle("CloseToPowerPill", 0.200,0.450));
        pacman.addTerm(new Trapezoid("FarFromPowerPill", 0.400, 0.500, 1.000, 1.000));
        engine.addInputVariable(pacman);

        danger = new OutputVariable();
        danger.setEnabled(true);
        danger.setName("DANGER");
        danger.setRange(0.000, 1.000);
        danger.fuzzyOutput().setAccumulation(new Maximum());
        danger.setDefuzzifier(new Centroid(200));
        danger.setDefaultValue(0.000);
        danger.setLockValidOutput(false);
        danger.setLockOutputRange(false);
        danger.addTerm(new Trapezoid("LOW", 0.000,0.000, 0.200, 0.500));
        danger.addTerm(new Trapezoid("MODERATE", 0.400, 0.500,0.800,0.900));
        danger.addTerm(new Trapezoid("HIGH", 0.800, 0.900, 1, 1));
        engine.addOutputVariable(danger);

        RuleBlock ruleBlock = new RuleBlock();
        ruleBlock.setEnabled(true);
        ruleBlock.setName("");
        ruleBlock.setConjunction(new Minimum());
        ruleBlock.setDisjunction(new Maximum());
        ruleBlock.setActivation(new Minimum());
        ruleBlock.addRule(Rule.parse("if PACMAN is FarFromPowerPill then DANGER is LOW", engine));
        ruleBlock.addRule(Rule.parse("if PACMAN is CloseToPowerPill then DANGER is MODERATE", engine));
        ruleBlock.addRule(Rule.parse("if PACMAN is OnPowerPill then DANGER is HIGH", engine));
        engine.addRuleBlock(ruleBlock);

        StringBuilder status = new StringBuilder();
        if (!engine.isReady(status)) {
            throw new RuntimeException("Engine not ready. "
                    + "The following errors were encountered:\n" + status.toString());
        }
    }

    /* (non-Javadoc)
     * @see danger.controllers.Controller#getMove(danger.game.Game, long)
     */
    public EnumMap<GHOST,MOVE> getMove(Game game,long timeDue)
    {
        initFuzzy();
        GHOST blinky = GHOST.BLINKY;

        int pacmanPos = game.getPacmanCurrentNodeIndex();

        int[] pills = game.getActivePowerPillsIndices();

        double nearestPillDistance = 1000;

        for (int i = 0; i < pills.length; i++)
        {
            if(game.getManhattanDistance(pacmanPos, pills[i]) < nearestPillDistance) {
                nearestPillDistance = game.getManhattanDistance(pacmanPos, pills[i]);
            }
        }

        pacman.setInputValue(nearestPillDistance/90);
        engine.process();

        if(danger.defuzzify() < 0.5)
        {
            myMoves.put(blinky,game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(blinky),
                    game.getPacmanCurrentNodeIndex(),game.getGhostLastMoveMade(blinky),DM.PATH));
        }
        else if (danger.defuzzify() >= 0.8) {
            myMoves.put(blinky,game.getApproximateNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(blinky),
                    game.getPacmanCurrentNodeIndex(),game.getGhostLastMoveMade(blinky),DM.PATH));
        }
        else									//else take a random action
             myMoves.put(blinky,moves[rnd.nextInt(moves.length)]);
        return myMoves;
    }
}