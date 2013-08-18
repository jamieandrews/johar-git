import java.util.*;
import johar.gem.Gem;

public class Mugwump {

    private static final int numMugwumps = 4;

    private int _mugwumpX[];
    private int _mugwumpY[];
    private boolean _mugwumpFound[];

    private int _guessesLeftInGame;
    private int _mugwumpsFoundInGame;
    private int _totalGuesses = 0;
    private int _totalMugwumpsFound = 0;

    private Random _random;

    //public Mugwump(Gem gem) {
	// Don't need to do anything here -- applicationEngineInitialize
	// will do everything.
    //}

    public void applicationEngineInitialize(Gem gem) {
	gem.showText("Hunt the Mugwumps!", 2000);
	gem.showText(" ", 2000);
	gem.showText("Four mugwumps are hiding on the points of a grid.", 2000);
	gem.showText("You get seven guesses to find all four.", 2000);
	gem.showText("Each guess consists of a value of x and y.", 2000);
	gem.showText("The values of x and y must be between 1 and 10.", 2000);

	_mugwumpX = new int[numMugwumps];
	_mugwumpY = new int[numMugwumps];
	_mugwumpFound = new boolean[numMugwumps];
	_random = new Random(System.currentTimeMillis());
	startNewGame();
	gem.showText("You have " + _guessesLeftInGame + " guesses left.", 2000);
    }

    public void guess(Gem gem) {
	_guessesLeftInGame--;
	_totalGuesses++;
	long x = gem.getIntParameter("x");
	long y = gem.getIntParameter("y");

	gem.showText("Your guess: x = " + x + ", y = " + y, 2000);

	for (int i = 0 ; i<numMugwumps; i++) {
	    if (!_mugwumpFound[i]) {
		double distance = Math.sqrt(
		    (x - _mugwumpX[i]) * (x - _mugwumpX[i]) +
		    (y - _mugwumpY[i]) * (y - _mugwumpY[i])
		);
		if (distance == 0.0) {
		    _mugwumpFound[i] = true;
		    _mugwumpsFoundInGame++;
		    _totalMugwumpsFound++;
		    gem.showText("You have found mugwump " + i + "!", 2000);
		} else {
		    String distanceString = Double.toString(distance + 0.005);
		    int n1 = distanceString.indexOf('.');
		    gem.showText("You are " + 
				distanceString.substring(0, n1+3) +
			 	" away from mugwump " + i, 2000);
		}
	    }
	}

	if (_guessesLeftInGame==0 || _mugwumpsFoundInGame==numMugwumps) {
	    if (_mugwumpsFoundInGame==numMugwumps) {
		gem.showText("You win!!!", 2000);
	    } else {
		gem.showText("You lose!!!", 2000);
	    }
	    gem.showText("This is where the mugwumps were:", 2000);
	    for (int i=0; i<numMugwumps; i++) {
		gem.showText("Mugwump " + i + ": " +
		    _mugwumpX[i] + ", " +
		    _mugwumpY[i], 2000);
	    }
	    gem.showText("Starting a new game.", 2000);
	    startNewGame();
	}
	gem.showText("You have " + _guessesLeftInGame + " guesses left.", 2000);
    }

    public void newGame(Gem gem) {
	boolean resignGame = gem.getBooleanParameter("resignGame");
	if (resignGame) {
	    gem.showText("Starting a new game.", 2000);
	    startNewGame();
	} else {
	    gem.showText("OK, staying with the current game.", 2000);
	}
    }

    public boolean activeGame(Gem gem) {
	return true;
    }

    public void quit(Gem gem) {
	gem.showText("In total you found " + _totalMugwumpsFound +
		     " in " + _totalGuesses + " guesses.", 2000);
	gem.showText("Thanks for playing Mugwump!", 2000);
    }

    private void startNewGame() {
	for (int i = 0 ; i<numMugwumps; i++) {
	    _mugwumpX[i] = _random.nextInt(10) + 1;
	    _mugwumpY[i] = _random.nextInt(10) + 1;
	    _mugwumpFound[i] = false;
	}
	_guessesLeftInGame = 7;
	_mugwumpsFoundInGame = 0;
    }

}
