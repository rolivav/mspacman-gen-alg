package pacman.controllers.examples;

import com.fuzzylite.Engine;
import com.fuzzylite.defuzzifier.Centroid;
import com.fuzzylite.norm.s.Maximum;
import com.fuzzylite.norm.t.Minimum;
import com.fuzzylite.rule.Rule;
import com.fuzzylite.rule.RuleBlock;
import com.fuzzylite.term.Trapezoid;
import com.fuzzylite.variable.InputVariable;
import com.fuzzylite.variable.OutputVariable;
import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.Game;
import static pacman.game.Constants.DM;
import static pacman.game.Constants.MOVE;

/*
 * The Class NearestPillPacMan.
 */
public class GeneticPacMan extends Controller<MOVE>
{
	Engine engine;
	InputVariable closestGhost;
	OutputVariable danger;

	public void initFuzzy(double[][] decodedPhenotype)
	{
		engine = new Engine();
		engine.setName("FuzzyPacman");

		closestGhost = new InputVariable();
		closestGhost.setEnabled(true);
		closestGhost.setName("CLOSEST_GHOST");
		closestGhost.setRange(0.000, 1.000);

		double[] function = decodedPhenotype[0];
		closestGhost.addTerm(new Trapezoid("NEAR", function[0], function[1], function[2], function[3]));
		function = decodedPhenotype[1];
		closestGhost.addTerm(new Trapezoid("MEDIUM", function[0], function[1], function[2], function[3]));
		function = decodedPhenotype[2];
		closestGhost.addTerm(new Trapezoid("FAR", function[0], function[1], function[2], function[3]));
		engine.addInputVariable(closestGhost);

		danger = new OutputVariable();
		danger.setEnabled(true);
		danger.setName("DANGER");
		danger.setRange(0.000, 1.000);
		danger.fuzzyOutput().setAccumulation(new Maximum());
		danger.setDefuzzifier(new Centroid(200));
		danger.setDefaultValue(0.000);
		danger.setLockValidOutput(false);
		danger.setLockOutputRange(false);
		function = decodedPhenotype[3];
		danger.addTerm(new Trapezoid("LOW", function[0], function[1], function[2], function[3]));
		function = decodedPhenotype[4];
		danger.addTerm(new Trapezoid("MODERATE", function[0], function[1],function[2],function[3]));
		function = decodedPhenotype[5];
		danger.addTerm(new Trapezoid("HIGH", function[0],function[1], function[2], function[3]));

		engine.addOutputVariable(danger);

		RuleBlock ruleBlock = new RuleBlock();
		ruleBlock.setEnabled(true);
		ruleBlock.setName("");
		ruleBlock.setConjunction(new Minimum());
		ruleBlock.setDisjunction(new Maximum());
		ruleBlock.setActivation(new Minimum());
		ruleBlock.addRule(Rule.parse("if CLOSEST_GHOST is NEAR then DANGER is HIGH", engine));
		ruleBlock.addRule(Rule.parse("if CLOSEST_GHOST is MEDIUM then DANGER is MODERATE", engine));
		ruleBlock.addRule(Rule.parse("if CLOSEST_GHOST is FAR then DANGER is LOW", engine));
		engine.addRuleBlock(ruleBlock);

		StringBuilder status = new StringBuilder();
		if (!engine.isReady(status)) {
			throw new RuntimeException("Engine not ready. "
					+ "The following errors were encountered:\n" + status.toString());
		}
	}

	public void actions(){
		System.out.println ("Actions");
		System.out.println ("If DANGER is HIGH, Pacman is RunningAwayFromGhost");
		System.out.println ("If DANGER is MEDIUM, Pacman is GoingToClosestPowerpill");
		System.out.println ("If DANGER is LOW, Pacman is GoingToClosestPill");
	}
	/* (non-Javadoc)
	 * @see danger.controllers.Controller#getMove(danger.game.Game, long)
	 */

	public Constants.GHOST getClosestGhost(Game game, int blinky, int inky, int pinky, int sue) {
		Constants.GHOST result = null;
		int closestDist = 500;
		if(blinky < closestDist) {
			closestDist = blinky;
			result = Constants.GHOST.BLINKY;
		}
		if(inky < closestDist) {
			closestDist = inky;
			result = Constants.GHOST.INKY;
		}
		if(pinky < closestDist) {
			closestDist = pinky;
			result =Constants.GHOST.PINKY;
		}
		if(sue < closestDist) {
			result = Constants.GHOST.SUE;
		}
		return result;
	}

	public MOVE getMove(Game game,long timeDue)
	{
		int pacmanCurrentNode = game.getPacmanCurrentNodeIndex();
		int dBlinky = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(Constants.GHOST.BLINKY), pacmanCurrentNode);
		int dInky = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(Constants.GHOST.INKY), pacmanCurrentNode);
		int dPinky = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(Constants.GHOST.PINKY), pacmanCurrentNode);
		int dToSue = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(Constants.GHOST.SUE), pacmanCurrentNode);

		Constants.GHOST closestGhostEnum = getClosestGhost(game, dBlinky, dInky, dPinky, dToSue);
//		System.out.println(closestGhostEnum);
		double closestGhostDistToPacman = (double)game.getManhattanDistance(game.getGhostCurrentNodeIndex(closestGhostEnum), pacmanCurrentNode)/150;
//		System.out.println(closestGhostDistToPacman +" "+ game.getGhostCurrentNodeIndex(closestGhostEnum) + " " + pacmanCurrentNode);
		closestGhost.setInputValue(closestGhostDistToPacman);
		engine.process();
		double currentDanger =  danger.defuzzify();
		//DANGER LOW
		if(currentDanger < 0.3) {
			int[] pills = game.getActivePillsIndices();
			double nearestPillDistance = 1000;
			int closestPillPos = 0;
			for (int i = 0; i < pills.length; i++)
			{
				if(game.getManhattanDistance(pacmanCurrentNode, pills[i]) < nearestPillDistance) {
					nearestPillDistance = game.getManhattanDistance(pacmanCurrentNode, pills[i]);
					closestPillPos = pills[i];
				}
			}
			System.out.println("DANGER: LOW");
			return game.getNextMoveTowardsTarget(pacmanCurrentNode, closestPillPos,DM.PATH);
		//DANGER MEDIUM
		} else if (currentDanger >= 0.3 && currentDanger < 0.7) {
			int[] pills = game.getActivePowerPillsIndices();
			double nearestPillDistance = 1000;
			int closestPillPos = 0;
			for (int i = 0; i < pills.length; i++)
			{
				if(game.getManhattanDistance(pacmanCurrentNode, pills[i]) < nearestPillDistance) {
					nearestPillDistance = game.getManhattanDistance(pacmanCurrentNode, pills[i]);
					closestPillPos = pills[i];
				}
			}
			System.out.println("DANGER: MEDIUM");
			return game.getNextMoveTowardsTarget(pacmanCurrentNode, closestPillPos,DM.PATH);
		}
		//DANGER HIGH
		else {
			System.out.println("DANGER: HIGH");
			return game.getNextMoveAwayFromTarget(pacmanCurrentNode, game.getGhostCurrentNodeIndex(closestGhostEnum),DM.PATH);
		}
	}
}