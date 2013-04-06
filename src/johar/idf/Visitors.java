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
		eh.error("Error: duplicate command name \"" + name + "\"");
	    } else {
		_commandNames.add(name);
	    }
	}
    }

}
