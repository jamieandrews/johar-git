package johar.idf;

import java.util.*;

public class Visitors {

    // Checks that all command names are unique.
    public static class CommandNamesUnique extends VisitorOfIdfElement {
	Set<String> _commandNames;

	public void beforeChildren(Idf idf, ErrorHandler eh) {
	    _commandNames = new HashSet<String>();
	}

	public void beforeChildren(IdfCommand idfCommand, ErrorHandler eh) {
	    // System.out.println("CNU.beforeChildren 1");
	    String name = idfCommand.getCommandName();
	    // System.out.println("CNU.beforeChildren 2");
	    if (_commandNames.contains(name)) {
		// System.out.println("CNU.beforeChildren 3a");
		eh.error("Duplicate command name \"" + name + "\"");
		// System.out.println("CNU.beforeChildren 3b");
	    } else {
		// System.out.println("CNU.beforeChildren 4");
		_commandNames.add(name);
	    }
	}
    }

    // Checks that the help messages BriefHelp and OneLineHelp have no
    // newlines, and are the right number of characters, for both
    // Commands and Parameters.
    public static class HelpMessageConstraints extends VisitorOfIdfElement {
	public static final int briefHelpMaxLength = 30;
	public static final int oneLineHelpMaxLength = 80;

	public void beforeChildren(IdfCommand idfCommand, ErrorHandler eh) {
	    String elementDesc =
		"Command " + idfCommand.getCommandName();

	    // Check BriefHelp.
	    checkHelpMessage(elementDesc,
		"BriefHelp", idfCommand.getBriefHelp(),
		briefHelpMaxLength, eh);

	    // Check OneLineHelp.
	    checkHelpMessage(elementDesc,
		"OneLineHelp", idfCommand.getOneLineHelp(),
		oneLineHelpMaxLength, eh);
	}

	public void beforeChildren(IdfParameter idfParameter, ErrorHandler eh) {
	    String elementDesc =
		"Parameter " + idfParameter.getParameterName();

	    // Check BriefHelp.
	    checkHelpMessage(elementDesc,
		"BriefHelp", idfParameter.getBriefHelp(),
		briefHelpMaxLength, eh);

	    // Check OneLineHelp.
	    checkHelpMessage(elementDesc,
		"OneLineHelp", idfParameter.getOneLineHelp(),
		oneLineHelpMaxLength, eh);
	}

	public void checkHelpMessage(String elementDesc, String attrName,
		String helpMessage, int maxLength, ErrorHandler eh) {
	    // System.out.println("cHM a elementDesc=\"" + elementDesc +
		// "\", attrName=\"" + attrName +
		// "\", helpMessage=\"" + helpMessage + "\"");
	    boolean respectsConstraints =
		johar.utilities.TextInputValidator.respectsConstraints(
		    helpMessage, maxLength, false
		);

	    if (respectsConstraints)
		return;

	    eh.error(elementDesc + ": " + attrName +
		" must be no more than " + maxLength +
		" characters, and contain no carriage returns.");
	}

    }

    // Checks that all parameters in all stages of a given command
    // have names that are unique to that command.
    public static class ParameterNamesUniqueInCommand extends VisitorOfIdfElement {
	String _currentCommandName;
	Set<String> _parameterNamesSoFar;

	public void beforeChildren(IdfCommand idfCommand, ErrorHandler eh) {
	    _currentCommandName = idfCommand.getCommandName();
	    _parameterNamesSoFar = new HashSet<String>();
	}

	public void beforeChildren(IdfParameter idfParameter, ErrorHandler eh) {
	    String parameterName = idfParameter.getParameterName();
	    if (_parameterNamesSoFar.contains(parameterName)) {
		eh.error("Command " + _currentCommandName + 
		    ": duplicate parameter " + parameterName +
		    " detected"
		);
	    } else {
		_parameterNamesSoFar.add(parameterName);
	    }
	}
    }

    // Checks that the value for MinNumberOfReps is <= the value for
    // MaxNumberOfReps for a parameter.
    public static class MinRepsLeqMaxReps extends VisitorOfIdfElement {
	String _currentCommandName;

	public void beforeChildren(IdfCommand idfCommand, ErrorHandler eh) {
	    _currentCommandName = idfCommand.getCommandName();
	}

	public void beforeChildren(IdfParameter idfParameter, ErrorHandler eh) {
	    boolean ok = (
		idfParameter.getMinNumberOfReps()
		<= idfParameter.getMaxNumberOfReps()
	    );

	    if (!ok) {
		eh.error("Command " + _currentCommandName +
		    ", parameter " + idfParameter.getParameterName() +
		    ": MinNumberOfReps should be less than or equal " +
		    "to MaxNumberOfReps"
		);
	    }
	}
    }

    // Checks that the value for MinValue is <= the value for
    // MaxValue for a parameter.
    public static class MinValueLeqMaxValue extends VisitorOfIdfElement {
	String _currentCommandName;

	public void beforeChildren(IdfCommand idfCommand, ErrorHandler eh) {
	    _currentCommandName = idfCommand.getCommandName();
	}

	public void beforeChildren(IdfParameter idfParameter, ErrorHandler eh) {
	    boolean ok = (
		( idfParameter.getMinIntValue()
		  <= idfParameter.getMaxIntValue())
	    &&  ( idfParameter.getMinFloatValue()
		  <= idfParameter.getMaxFloatValue())
	    );

	    if (!ok) {
		eh.error("Command " + _currentCommandName +
		    ", parameter " + idfParameter.getParameterName() +
		    ": MinValue should be less than or equal to MaxValue"
		);
	    }
	}
    }

    // Checks that the value for MinNumberOfChars is <= the value for
    // MaxNumberOfChars for a "text" parameter.
    public static class MinCharsLeqMaxChars extends VisitorOfIdfElement {
	String _currentCommandName;

	public void beforeChildren(IdfCommand idfCommand, ErrorHandler eh) {
	    _currentCommandName = idfCommand.getCommandName();
	}

	public void beforeChildren(IdfParameter idfParameter, ErrorHandler eh) {
	    if (!idfParameter.getType().equals("text"))
		return;

	    boolean ok = (
		idfParameter.getMinNumberOfChars()
		<= idfParameter.getMaxNumberOfChars()
	    );

	    if (!ok) {
		eh.error("Command " + _currentCommandName +
		    ", parameter " + idfParameter.getParameterName() +
		    ": MinNumberOfChars should be less than or equal to MaxNumberOfChars"
		);
	    }
	}
    }

}

