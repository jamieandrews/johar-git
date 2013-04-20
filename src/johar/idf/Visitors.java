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
	    String name = idfCommand.getCommandName();
	    if (_commandNames.contains(name)) {
		eh.error("Duplicate command name \"" + name + "\"");
	    } else {
		_commandNames.add(name);
	    }
	}
    }

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

}

