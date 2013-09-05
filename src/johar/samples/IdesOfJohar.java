


/* IdesOfJohar.java
 * This source file is part of the Johar project.
 * @author Fatima Hussain
 * @author Jamie Andrews
 */

import java.text.*;
import java.util.*;

import johar.gem.Gem;

public class IdesOfJohar {
	private AppointmentTable appointments;
	GregorianCalendar currentDay;

	public void initIdesOfJohar(Gem gem) {
		gem.showText("Ides of Johar v1.0", 1000);
		gem.showTable("weeks");
		gem.showTable("days");
		gem.showTable("appointments");
		
		appointments = new AppointmentTable();

		// Start with current day = today's date and time
		currentDay = new GregorianCalendar();
		refreshTables(gem, "weeks");
		
		StringBuilder welcome = new StringBuilder();
		welcome.append("Welcome to... IDES of JOHAR");
		welcome.append("\nAn accessible appointment calendar");
		welcome.append("\nToday's date:  ");
		Formatter fmt = new Formatter(welcome, Locale.getDefault());
		fmt.format("%1$tA %1$te %1$tB %1$tY", currentDay);
		gem.showText(welcome.toString(), 2000);
	}

	// Command addAppointment
	public void addAppointment(Gem gem) {
		Calendar timeCal = (Calendar) (gem.getParameter("time"));		
		String description = gem.getStringParameter("description");
		int numReps = (int) gem.getIntParameter("numReps");
		String repeatEvery = gem.getStringParameter("repeatEvery");
		
		// Set up a calendar with the correct date and time
		GregorianCalendar cal = (GregorianCalendar) currentDay.clone();
		cal.set(Calendar.HOUR, timeCal.get(Calendar.HOUR));
		cal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.AM_PM, timeCal.get(Calendar.AM_PM));
		
		//Add appointment
		int numAdded = 0;
		while (numAdded < numReps) {			
			appointments.addAppointment(cal, description);
			
			if (repeatEvery.equals("day"))
				cal.add(Calendar.DAY_OF_MONTH, 1);
			else if (repeatEvery.equals("week"))
				cal.add(Calendar.WEEK_OF_YEAR, 1);
			else
				cal.add(Calendar.MONTH, 1);
			
			numAdded++;
		}

		//Refresh the Appointments table
		refreshTables(gem, "appointments");		
		
		gem.showText("Appointment added.", 2000);
	}

	// Command cancel
	public void cancel(Gem gem) throws ParseException {
		int apptTableRow = (int) gem.getIntParameter("appt");
		String selectedRow = gem.getRowText("appointments", apptTableRow);
		String rowEntries[] = selectedRow.split("\\|");
		Date time = new SimpleDateFormat("hh:mm a").parse(rowEntries[0]);
		String desc = rowEntries[1];
				
		// Set up a calendar with the correct date and time
		GregorianCalendar timeCal = new GregorianCalendar();
		timeCal.setTime(time);
		GregorianCalendar cal = (GregorianCalendar) currentDay.clone();
		
		cal.set(Calendar.HOUR, timeCal.get(Calendar.HOUR));
		cal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.AM_PM, timeCal.get(Calendar.AM_PM));
		
		//Cancel appointment
		Appointment appt = new Appointment(cal, desc);
		appointments.cancelAppointment(appt);
		
		// Refresh the Appointments table
		refreshTables(gem, "appointments");
		
		gem.showText("Appointment cancelled.", 2000);
	}

	// Command reminders
	public void reminders(Gem gem) {
		String reminderType = gem.getStringParameter("kind");
		if (reminderType.equals("popup")) {
			gem.showText("Reminders currently not implemented.", 2000);
			gem.showText("When they are, you will be reminded by popup!", 2000);
		} else if (reminderType.equals("email")) {
			gem.showText("Reminders currently not implemented.", 2000);
			gem.showText("When they are, you will be reminded by email!", 2000);
		} else {
			gem.showText("Reminders turned off.", 2000);
		}
	}

	// Command previousMonth
	public void previousMonth(Gem gem) {
		currentDay.set(Calendar.DAY_OF_MONTH, 1);
		currentDay.add(Calendar.MONTH, -1);
		refreshTables(gem, "weeks");
	}

	// Command nextMonth
	public void nextMonth(Gem gem) {
		currentDay.set(Calendar.DAY_OF_MONTH, 1);
		currentDay.add(Calendar.MONTH, 1);
		refreshTables(gem, "weeks");
	}

	// Command previousWeek
	public void previousWeek(Gem gem) {
		currentDay.add(Calendar.WEEK_OF_YEAR, -1);
		refreshTables(gem, "days");
	}

	// Command nextWeek
	public void nextWeek(Gem gem) {
		currentDay.add(Calendar.WEEK_OF_YEAR, 1);
		refreshTables(gem, "days");
	}

	// Command previousDay
	public void previousDay(Gem gem) {
		currentDay.add(Calendar.DAY_OF_MONTH, -1);
		refreshTables(gem, "appointments");
	}

	// Command nextDay
	public void nextDay(Gem gem) {
		currentDay.add(Calendar.DAY_OF_MONTH, 1);
		refreshTables(gem, "appointments");
	}

	// Command goToSelectedWeek
	public void goToSelectedWeek(Gem gem) {
		int weeksTableRow = (int) gem.getIntParameter("week");
		currentDay.set(Calendar.WEEK_OF_MONTH, weeksTableRow + 1);
		refreshTables(gem, "days");
	}

	// Command goToSelectedDay
	public void goToSelectedDay(Gem gem) throws ParseException {
		int daysTableRow = (int) gem.getIntParameter("day");
		String selectedRow = gem.getRowText("days", daysTableRow);
		String rowEntries[] = selectedRow.split("\\|");
		
		// Set up a calendar with the selected day
		Date dateTime = new SimpleDateFormat("E d MMM").parse(rowEntries[0]);
		GregorianCalendar dateCal = new GregorianCalendar();
		dateCal.setTime(dateTime);
		
		// Go to the selected day
		currentDay.set(Calendar.DAY_OF_MONTH, dateCal.get(Calendar.DAY_OF_MONTH));
		currentDay.set(Calendar.MONTH, dateCal.get(Calendar.MONTH));
		
		// Refresh the Appointments table
		refreshTables(gem, "appointments");
	}

	// Command goToDate
	public void goToDate(Gem gem) {
		Calendar dateToGoTo = (Calendar) gem.getParameter("dateToGoTo");
				
		// Go to the selected date
		currentDay = (GregorianCalendar) dateToGoTo;
				
		// Refresh the Appointments table
		refreshTables(gem, "appointments");
	}
	
	/*
	 * refreshTables: Refresh the data in the tables.
	 * 
	 * @param gem Gem.
	 * 
	 * @param topTableName The table which should become the top table after the
	 * refresh.
	 */
	private void refreshTables(Gem gem, String topTableName) {
		/* refreshTables */
		refreshWeeksTable(gem);
		refreshDaysTable(gem);
		refreshAppointmentsTable(gem);
		gem.setTopTable(topTableName);
	} 

	private void refreshWeeksTable(Gem gem) {
		gem.clearTable("weeks");

		StringBuilder header = new StringBuilder();
		Formatter fmt = new Formatter(header, Locale.getDefault());
		fmt.format("%1$tB %1$tY", currentDay);
		gem.setTableHeading("weeks", header.toString());

		GregorianCalendar firstDayThisMonth = (GregorianCalendar) currentDay
				.clone();
		firstDayThisMonth.add(Calendar.DAY_OF_MONTH,
				1 - currentDay.get(Calendar.DAY_OF_MONTH));
		GregorianCalendar sunday = (GregorianCalendar) firstDayThisMonth
				.clone();
		sunday.set(Calendar.DAY_OF_MONTH, 1);
		sunday.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		GregorianCalendar firstDayNextMonth = (GregorianCalendar) firstDayThisMonth
				.clone();
		firstDayNextMonth.add(Calendar.MONTH, 1);

		int row = 0;
		while (firstDayNextMonth.after(sunday)) {
			// Fill in a row, corresponding to a week
			StringBuilder contents = new StringBuilder();
			GregorianCalendar day = (GregorianCalendar) sunday.clone();
			for (int col = 0; col <= 6; col++) {
				// Fill in a day in this week's row
				if (day.before(firstDayThisMonth)) {
					contents.append("      ");
				} else if (day.before(firstDayNextMonth)) {
					Formatter fmt2 = new Formatter(contents,
							Locale.getDefault());

					TreeSet<Appointment> todaysAppointments = appointments
							.allOn(day);
					// System.out.println("refreshWeeksTable 08");
					if (todaysAppointments.size() == 0) {
						fmt2.format("  %1$te  ", day);
					} else {
						fmt2.format(" *%1$te* ", day);
					}
				} else { // first day of next month or later
					contents.append("      ");
				}
				if (col < 6)
					contents.append("|");
				day.add(Calendar.DAY_OF_WEEK, 1);
			}
			gem.fillRow("weeks", row, contents.toString());

			sunday.add(Calendar.WEEK_OF_YEAR, 1);
			row++;
		}
	}

	private void refreshDaysTable(Gem gem) {
		gem.clearTable("days");
		GregorianCalendar day = (GregorianCalendar) currentDay.clone();
		day.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

		StringBuilder header = new StringBuilder();
		Formatter fmt = new Formatter(header, Locale.getDefault());
		fmt.format("Week of %1$te %1$tB %1$tY", day);

		GregorianCalendar sundayOfNextWeek = (GregorianCalendar) day.clone();
		sundayOfNextWeek.add(Calendar.WEEK_OF_YEAR, 1);

		int row = 0;
		while (day.before(sundayOfNextWeek)) {
			// Fill a row with one day's appointments
			TreeSet<Appointment> todaysAppointments = appointments.allOn(day);
			StringBuilder contents = new StringBuilder();
			Formatter fmt2 = new Formatter(contents, Locale.getDefault());
			fmt2.format("%1$ta %1$te %1$tb", day);
			contents.append("|" + todaysAppointments.size());
			gem.fillRow("days", row, contents.toString());
			
			day.add(Calendar.DAY_OF_WEEK, 1);
			row++;
		}
	}

	private void refreshAppointmentsTable(Gem gem) {
		gem.clearTable("appointments");
		StringBuilder header = new StringBuilder();

		Formatter fmt = new Formatter(header, Locale.getDefault());
		fmt.format("Appointments for %1$tA %1$te %1$tB %1$tY", currentDay);
		gem.setTableHeading("appointments", header.toString());		
		gem.setColumnNames("appointments", "Time|Description");

		TreeSet<Appointment> todaysAppointments = appointments
				.allOn(currentDay);
		Iterator<Appointment> it = todaysAppointments.iterator();
		int row = 0;
		while (it.hasNext()) {
			// Fill a row with an appointment
			Appointment appt = it.next();
			GregorianCalendar time = appt.getCalendar();
			StringBuilder contents = new StringBuilder();
			Formatter fmt2 = new Formatter(contents, Locale.getDefault());
			fmt2.format("%1$tI:%1$tM %1$Tp|", time);
			contents.append(appt.getDescription());
			gem.fillRow("appointments", row, contents.toString());
			row++;
		}
	}

	//Ask Reminder question
    public boolean askReminderQuestion(Gem gem){		
		return true;
    }	

	/*
	 * exit: Exit the system. 
	 * @param gem Gem.
	 */
	public void exit(Gem gem) {
		gem.showText("Ides of Johar ended", 2000);
	}
}
