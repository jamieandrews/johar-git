


/* AppointmentTable.java
 * This source file is part of the Johar project.
 * @author Fatima Hussain
 * @author Jamie Andrews
 */

import java.util.*;

public class AppointmentTable  {
    static final Comparator<Appointment> appointmentTableComparator =
		new Comparator<Appointment>() {
	public int compare(Appointment appt1, Appointment appt2) {
	    return appt1.compare(appt2);
	}
    };

    private TreeSet<Appointment> appointments;

    public AppointmentTable() {
	appointments = new TreeSet<Appointment>(appointmentTableComparator);
    }

    public void addAppointment(GregorianCalendar calendar, String description) {
	// System.out.println("AT.addAppointment 01");
	Appointment appt =
	    new Appointment((GregorianCalendar) calendar.clone(), description);
	// System.out.println("AT.addAppointment 02");
	appointments.add(appt);
	// System.out.println("AT.addAppointment 03");
    }

    public void cancelAppointment(Appointment a) {
	appointments.remove(a);
    }

    public TreeSet<Appointment> allOn(GregorianCalendar day) {
	// Get a Calendar for the earliest appointment time in day
	// System.out.println("AT.allOn 01");
	GregorianCalendar earliest = (GregorianCalendar) day.clone();
	earliest.set(Calendar.HOUR_OF_DAY, 0);
	earliest.set(Calendar.MINUTE, 0);
	// System.out.println("AT.allOn 02");

	// Get a Calendar for the earliest appointment time in the next day
	GregorianCalendar earliestNextDay =
	    (GregorianCalendar) earliest.clone();
	earliestNextDay.add(Calendar.DAY_OF_MONTH, 1);
	// System.out.println("AT.allOn 03");

	// Create new empty TreeSet
	TreeSet<Appointment> retval =
	    new TreeSet<Appointment>(appointmentTableComparator);
	// System.out.println("AT.allOn 04");

	Iterator<Appointment> it = appointments.iterator();
	while (it.hasNext()) {
	    // System.out.println("AT.allOn 05");
	    // See if this appointment is on the required day
	    Appointment appt = it.next();
	    GregorianCalendar apptTime = appt.getCalendar();
	    // System.out.println("AT.allOn 06");
	    if (  earliest.before(apptTime)
	       && apptTime.before(earliestNextDay) ) {
		// Yep, it is
		// System.out.println("AT.allOn 06a");
		retval.add(appt);
		// System.out.println("AT.allOn 07");
	    }
	    // System.out.println("AT.allOn 08");
	}
	// System.out.println("AT.allOn 09");

	return retval;
    }
}
