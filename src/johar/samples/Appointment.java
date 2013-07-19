

/*
 * Appointment is a class that stores the information
 * of an appointment. This class is used by the
 * AppointmentCalendar class to represent various
 * appointments.
 *
 */

import java.util.*;

public class Appointment {

    private GregorianCalendar _calendar;
    private String _description;

    public Appointment(GregorianCalendar calendar, String description) {
	_calendar = calendar;
	_description = description;
    }

    public GregorianCalendar getCalendar() {
	return _calendar;
    }

    public String getDescription() {
	return _description;
    }

    public int compare(Appointment other) {
	GregorianCalendar otherCalendar = other.getCalendar();
	if (_calendar.before(otherCalendar))
	    return -1;
	else if (otherCalendar.before(_calendar))
	    return 1;
	else
	    return 0;
    }
}
