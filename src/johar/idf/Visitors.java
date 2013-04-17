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

	    // Check BriefHelp.
	    if (!johar.utilities.TextInputValidator.respectsConstraints(
		    idfCommand.getBriefHelp(),
		    briefHelpMaxLength, false)) {
		String commandName = idfCommand.getCommandName();
		eh.error("Command " + commandName +
		    ": BriefHelp must be no more than 30 characters, " +
		    "and contain no carriage returns.");
	    }

	    // Check OneLineHelp.
	    if (!johar.utilities.TextInputValidator.respectsConstraints(
		    idfCommand.getOneLineHelp(),
		    oneLineHelpMaxLength, false)) {
		String commandName = idfCommand.getCommandName();
		eh.error("Command " + commandName +
		    ": OneLineHelp must be no more than 80 characters, " +
		    "and contain no carriage returns.");
	    }

	}

	public void beforeChildren(IdfParameter idfParameter, ErrorHandler eh) {

	    // Check BriefHelp.
	    if (!johar.utilities.TextInputValidator.respectsConstraints(
		    idfParameter.getBriefHelp(),
		    briefHelpMaxLength, false)) {
		String parameterName = idfParameter.getParameterName();
		eh.error("Parameter " + parameterName +
		    ": BriefHelp must be no more than 30 characters, " +
		    "and contain no carriage returns.");
	    }

	    // Check OneLineHelp.
	    if (!johar.utilities.TextInputValidator.respectsConstraints(
		    idfParameter.getOneLineHelp(),
		     oneLineHelpMaxLength, false)) {
		String parameterName = idfParameter.getParameterName();
		eh.error("Parameter " + parameterName +
		    ": OneLineHelp must be no more than 80 characters, " +
		    "and contain no carriage returns.");
	    }

	}

    }

}

