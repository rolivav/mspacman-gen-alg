package pacman.controllers.examples;

import pacman.controllers.Controller;
import pacman.game.Game;
import com.fuzzylite.*;
import com.fuzzylite.defuzzifier.*;
import com.fuzzylite.factory.*;
import com.fuzzylite.hedge.*;
import com.fuzzylite.imex.*;
import com.fuzzylite.norm.*;
import com.fuzzylite.norm.s.*;
import com.fuzzylite.norm.t.*;
import com.fuzzylite.rule.*;
import com.fuzzylite.term.*;
import com.fuzzylite.variable.*;

import static pacman.game.Constants.*;

/*
 * The Class NearestPillPacMan.
 */
public class RodrigoController extends Controller<MOVE>
{

    /* (non-Javadoc)
     * @see danger.controllers.Controller#getMove(danger.game.Game, long)
     */

    Engine engine = new Engine();
    InputVariable blinky = new InputVariable();
    InputVariable inky = new InputVariable();
    InputVariable pinky = new InputVariable();
    InputVariable sue = new InputVariable();

    public void initFuzzyness () {
        engine.setName("ghosts");

        blinky.setEnabled(true);
        blinky.setName("BLINKY");
        blinky.setRange(0.000, 1.000);
        blinky.addTerm(new Triangle("CLOSE", 0.500, -20.000));

//        inputVariable.setName("INKY");
//        inputVariable.setName("PINKY");
//        inputVariable.setName("SUE");

    }

    public MOVE getMove(Game game,long timeDue)
    {
        int currentNodeIndex=game.getPacmanCurrentNodeIndex();

        //get all active pills
        int[] activePills=game.getActivePillsIndices();

        //get all active power pills
        int[] activePowerPills=game.getActivePowerPillsIndices();

        //create a target array that includes all ACTIVE pills and power pills
        int[] targetNodeIndices=new int[activePills.length+activePowerPills.length];

        for(int i=0;i<activePills.length;i++)
            targetNodeIndices[i]=activePills[i];

        for(int i=0;i<activePowerPills.length;i++)
            targetNodeIndices[activePills.length+i]=activePowerPills[i];

        //return the next direction once the closest target has been identified
        return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(),game.getClosestNodeIndexFromNodeIndex(currentNodeIndex,targetNodeIndices,DM.PATH),DM.PATH);
    }
}