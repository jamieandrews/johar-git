import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import johar.gem.Gem;

/*
 * The Meal Planner App Engine
 */
public class MealPlanner{
	private Meal meal;
	private Date mealToday;
	private String SAVE_PATH_DIR = "";
	private String SAVE_PATH = "";
	private String REPOSITORY = "";
	
	//Method for initializing the app engine
	public void initMealPlanner(Gem gem){
		meal = new Meal();
		
		gem.showTable("breakfastTable");
		gem.showTable("lunchTable");
		gem.showTable("dinnerTable");
		gem.showText("Have a great food experience! Plan your meals now.", 2000);
		
		//Set the path to the repository		
		String absolutePath = System.getProperty("user.dir");;		
		REPOSITORY = "Meals_Planner.jdat";
		SAVE_PATH_DIR = absolutePath + File.separator + "dataset";
		SAVE_PATH = SAVE_PATH_DIR + File.separator + REPOSITORY;
				
		//Load Meals for current day
		Calendar today = (GregorianCalendar) Calendar.getInstance();
		mealToday = getDateInstance(today);
		if (mealToday != null){
			viewMealsForAnyDay(mealToday, gem);
		}
		
		//Set top table
		setTopTable(gem);
	}
	
	private void setTopTable(Gem gem){
		Calendar currentDay = (GregorianCalendar) Calendar.getInstance();
		
		if (currentDay.get(Calendar.AM_PM) == 0)
			gem.setTopTable("breakfastTable");  
		else {
			if (currentDay.get(Calendar.AM_PM) == 1 && currentDay.get(Calendar.HOUR) < 6)
				gem.setTopTable("lunchTable");
			else{
				if (currentDay.get(Calendar.AM_PM) == 1 && currentDay.get(Calendar.HOUR) >= 6)
					gem.setTopTable("dinnerTable");
			}
		}
	}	

	//Method for the breakfast command
	public void breakfast(Gem gem){		
		Calendar dateCal = (GregorianCalendar) gem.getParameter("bfMealDate");
		Date mealDate = getDateInstance(dateCal);
		
		if (mealDate != null && mealDate.before(mealToday)){
			gem.showText("Meal date cannot be in the past. Please select current or future date.", 3000);
		}
		else{
			Calendar startTimeCal = (GregorianCalendar) gem.getParameter("bfMealStartTime");
			int duration = (int) gem.getIntParameter("bfMealDuration");
			
			dateCal.set(Calendar.HOUR, startTimeCal.get(Calendar.HOUR));
			dateCal.set(Calendar.MINUTE, startTimeCal.get(Calendar.MINUTE));
			dateCal.set(Calendar.SECOND, 0);
			dateCal.set(Calendar.MILLISECOND, 0);
			dateCal.set(Calendar.AM_PM, startTimeCal.get(Calendar.AM_PM));	
			
			//Store food items
			List<String> breakfastFoodList = new ArrayList<String>();
			for (int i = 0; i < gem.getParameterRepCount("bfMealFoodItems"); i++){
				breakfastFoodList.add(gem.getStringParameter("bfMealFoodItems", i));
			}
			
			//Store all meal information					
			meal.addInfo("breakfast", mealDate, startTimeCal, breakfastFoodList, duration);
			
			//Send status message
			gem.showText("Breakfast for " + getDateString(mealDate) + " has been added to your meal plan.", 2000);
				
			//Load table with meals for selected day
			viewMealsForAnyDay(mealDate, "breakfastTable", gem);
		}		
	}
	
	//Load the breakfast meal data into Gem
	private void resetBreakfastTable(Gem gem, Date mealDate){
		String beginTime = "";
		String endTime = "";
		MealInfo breakfastInfo = null;
		
		//Retrieve Meal Information for the specified date
		breakfastInfo = meal.getInfo("breakfast", mealDate);
		
		//Clear the Breakfast Table
		gem.clearTable("breakfastTable");
		
		//Fill Table with Meal Information
		if (breakfastInfo != null && !breakfastInfo.isEmpty()){
			for (int i = 0; i < breakfastInfo.getFoodItems().size(); i++){
				gem.fillRow("breakfastTable", i, breakfastInfo.getFoodItems().get(i));
			}		
			
			//Get Start Time
			Calendar startTimeCal = (GregorianCalendar) breakfastInfo.getStartTime();
			beginTime = getTimeString(startTimeCal); 

			//Get End Time
			Calendar endTimeCal = breakfastInfo.getEndTime();
			endTime = getTimeString(endTimeCal);
		}
		
		//Set Table Heading
		if (beginTime.equals("") && endTime.equals(""))
			gem.setTableHeading("breakfastTable", "Breakfast for " + getDateString(mealDate));
		else
			gem.setTableHeading("breakfastTable", "Breakfast for " + getDateString(mealDate) + " (" + beginTime + " - " + endTime + ")");
	}
	
	//Method for the lunch command
	public void lunch(Gem gem){
		Calendar dateCal = (GregorianCalendar) gem.getParameter("lunchMealDate");
		Date mealDate = getDateInstance(dateCal);
		
		if (mealDate != null && mealDate.before(mealToday)){
			gem.showText("Meal date cannot be in the past. Please select current or future date.", 3000);
		}
		else{
			Calendar startTimeCal = (GregorianCalendar) gem.getParameter("lunchMealStartTime");
			int duration = (int) gem.getIntParameter("lunchMealDuration");
			
			dateCal.set(Calendar.HOUR, startTimeCal.get(Calendar.HOUR));
			dateCal.set(Calendar.MINUTE, startTimeCal.get(Calendar.MINUTE));
			dateCal.set(Calendar.SECOND, 0);
			dateCal.set(Calendar.MILLISECOND, 0);
			dateCal.set(Calendar.AM_PM, startTimeCal.get(Calendar.AM_PM));		
			
			//Store food items
			List<String> lunchFoodList = new ArrayList<String>();
			for (int i = 0; i < gem.getParameterRepCount("lunchMealFoodItems"); i++){
				lunchFoodList.add(gem.getStringParameter("lunchMealFoodItems", i));
			}
			
			//Store all meal information			
			meal.addInfo("lunch", mealDate, startTimeCal, lunchFoodList, duration);
			
			//Send status message
			gem.showText("Lunch for " + getDateString(mealDate) + " has been added to your meal plan.", 2000);
				
			//Load table with meals for selected day
			viewMealsForAnyDay(mealDate, "lunchTable", gem);
		}
	}
	
	//Load the lunch meal data into Gem
	private void resetLunchTable(Gem gem, Date mealDate) {
		String beginTime = "";
		String endTime = "";
		MealInfo lunchInfo = null;
		
		//Retrieve Meal Information for the specified date
		lunchInfo = meal.getInfo("lunch", mealDate);
		
		//Clear the Lunch Table
		gem.clearTable("lunchTable");
		
		//Fill Table with Meal Information
		if (lunchInfo != null && !lunchInfo.isEmpty()){			
			for (int i = 0; i < lunchInfo.getFoodItems().size(); i++){
				gem.fillRow("lunchTable", i, lunchInfo.getFoodItems().get(i));
			}		
			
			//Get Start Time
			Calendar startTimeCal = (GregorianCalendar) lunchInfo.getStartTime();
			beginTime = getTimeString(startTimeCal);
			
			//Get End Time
			Calendar endTimeCal = lunchInfo.getEndTime();			
			endTime = getTimeString(endTimeCal);			
		}
		
		//Set Table Heading
		if (beginTime.equals("") && endTime.equals(""))
			gem.setTableHeading("lunchTable", "Lunch for " + getDateString(mealDate));
		else
			gem.setTableHeading("lunchTable", "Lunch for " + getDateString(mealDate) + " (" + beginTime + " - " + endTime + ")");
	}

	//Method for the dinner command
	public void dinner(Gem gem){
		Calendar dateCal = (GregorianCalendar) gem.getParameter("dinMealDate");
		Date mealDate = getDateInstance(dateCal);
		
		if (mealDate != null && mealDate.before(mealToday)){
			gem.showText("Meal date cannot be in the past. Please select current or future date.", 3000);
		}
		else{		
			Calendar startTimeCal = (GregorianCalendar) gem.getParameter("dinMealStartTime");
			int duration = (int) gem.getIntParameter("dinMealDuration");
		
			dateCal.set(Calendar.HOUR, startTimeCal.get(Calendar.HOUR));
			dateCal.set(Calendar.MINUTE, startTimeCal.get(Calendar.MINUTE));
			dateCal.set(Calendar.SECOND, 0);
			dateCal.set(Calendar.MILLISECOND, 0);
			dateCal.set(Calendar.AM_PM, startTimeCal.get(Calendar.AM_PM));		
			
			//Store food items
			List<String> dinnerFoodList = new ArrayList<String>();
			for (int i = 0; i < gem.getParameterRepCount("dinMealFoodItems"); i++){
				dinnerFoodList.add(gem.getStringParameter("dinMealFoodItems", i));
			}
			
			//Store all meal information
			meal.addInfo("dinner", mealDate, startTimeCal, dinnerFoodList, duration);
			
			//Send status message
			gem.showText("Dinner for " + getDateString(mealDate) + " has been added to your meal plan.", 2000);
				
			//Load table with meals for selected day
			viewMealsForAnyDay(mealDate, "dinnerTable", gem);
		}
	}
	
	//Load the dinner meal data into Gem
	private void resetDinnerTable(Gem gem, Date mealDate) {
		String beginTime = "";
		String endTime = "";
		MealInfo dinnerInfo = null;
		
		//Retrieve Meal Information for the specified date
		dinnerInfo = meal.getInfo("dinner", mealDate);
		
		//Clear the Dinner Table
		gem.clearTable("dinnerTable");
		
		//Fill Table with Meal Information
		if (dinnerInfo != null && !dinnerInfo.isEmpty()){				
			for (int i = 0; i < dinnerInfo.getFoodItems().size(); i++){
				gem.fillRow("dinnerTable", i, dinnerInfo.getFoodItems().get(i));
			}		
			
			//Get Start Time
			Calendar startTimeCal = (GregorianCalendar) dinnerInfo.getStartTime();
			beginTime = getTimeString(startTimeCal);
			
			//Get End Time
			Calendar endTimeCal = dinnerInfo.getEndTime();			
			endTime = getTimeString(endTimeCal);		
		}
		
		//Set Table Heading
		if (beginTime.equals("") && endTime.equals(""))
			gem.setTableHeading("dinnerTable", "Dinner for " + getDateString(mealDate));
		else
			gem.setTableHeading("dinnerTable", "Dinner for " + getDateString(mealDate) + " (" + beginTime + " - " + endTime + ")");
	}
	
	//Method for the viewMeals command
	public void viewMeals(Gem gem){
		Calendar selectedDateCal = (Calendar) gem.getParameter("dateOfMeal");
		Date mealDate = getDateInstance(selectedDateCal);
		
		//Load table with meals for selected day
		viewMealsForAnyDay(mealDate, gem);
		
		setTopTable(gem);
	}

	//Display meals for any day
	private void viewMealsForAnyDay(Date mealDate, String mealTable, Gem gem){
		viewMealsForAnyDay(mealDate, gem);
		
		gem.setTopTable(mealTable);
	}
	
	//Display meals for any day
	private void viewMealsForAnyDay(Date mealDate, Gem gem) {
		String temp = "";
		
		if (mealDate != null){
			resetBreakfastTable(gem, mealDate);
			resetLunchTable(gem, mealDate);
			resetDinnerTable(gem, mealDate);
			
			/* If the meal information for the specified data is not currently available, 
			 * then check the repository and load the data if found
			 */
			
			//Load breakfast data from the repository if available
			try {
				temp = gem.getRowText("breakfastTable", 0);
			} catch (Exception e) {
				if (!retrieveDataFromRepository("breakfast", mealDate, gem)){
					//gem.showText("Your breakfast for " + getDateString(mealDate)+ " could not be viewed at the moment. An error occurred while loading data.", 3000);
				}
			}
			
			//Load lunch data from the repository if available
			try {
				temp = gem.getRowText("lunchTable", 0);
			} catch (Exception e) {
				if (!retrieveDataFromRepository("lunch", mealDate, gem)){
					//gem.showText("Your lunch for " + getDateString(mealDate)+ " could not be viewed at the moment. An error occurred while loading data.", 3000);
				}
			}
			
			//Load dinner data from the repository if available
			try {
				temp = gem.getRowText("dinnerTable", 0);
			} catch (Exception e) {
				if (!retrieveDataFromRepository("dinner", mealDate, gem)){
					//gem.showText("Your dinner for " + getDateString(mealDate)+ " could not be viewed at the moment. An error occurred while loading data.", 3000);
				}
			}
		}
		else
			gem.showText("An error occurred while loading meal data.", 3000);
	}
	
	//Method for the save command
	public void save(Gem gem){
		File mainDataPath = new File(SAVE_PATH);

		if (mainDataPath.exists()){
			saveMealInfo(mainDataPath, gem);
		}
		else{
			try {
				File dataDir = new File(SAVE_PATH_DIR);
				
				if (!dataDir.exists())
					dataDir.mkdir();
				
				mainDataPath = new File(dataDir, REPOSITORY);
				mainDataPath.createNewFile();
				
				saveMealInfo(mainDataPath, gem);
			} catch (IOException e) {
				gem.showText("Your meal plan could not be saved at the moment. [Error Details: " + e.getMessage() + "]", 3000);
			}
		}
	}

	//Save the current meal plan into the repository
	private void saveMealInfo(File mainDataPath, Gem gem) {
		FileWriter writer = null;
		String resultSet = "";
		
		try {
			writer = new FileWriter(mainDataPath, true);
			resultSet = retrieveUniqueInfo(getMealInfo(), mainDataPath);
			writer.write(resultSet);
			
			gem.showText("Your meal plan has been successfully saved.", 3000);
			gem.showText("Your meal plan has been successfully saved.", 2000);
			
		} catch (Exception e) {
			gem.showText("Your meal plan could not be saved at the moment. [Error Details: " + e.getMessage() + "]", 3000);
		}
		finally{
			try {
				writer.close();
			} catch (IOException e) {}
		}
	}

	//Avoid saving duplicated information into the repository
	private String retrieveUniqueInfo(String resultSet, File repository){
		String dataElements[] = resultSet.split("\n");
		String newResultSet = "";
		String fileContent = "";
		Scanner fileScanner = null;
		
		try {
			fileScanner = new Scanner(repository);
			fileContent = fileScanner.useDelimiter("\\Z").next();
		} catch (FileNotFoundException e1) {
			System.out.println(e1.getMessage());
		} catch (NoSuchElementException e2){}
		finally{
			if (fileScanner != null)
				fileScanner.close();
		}
		
		if (fileContent == null || fileContent.equals("")){
			return resultSet;				
		}
		else{
			for (String element : dataElements){
				if (!element.trim().equals("")){
					int index = fileContent.indexOf(element);
					if (index < 0)
						newResultSet += element + "\n";				
				}
			}
		}
				
		return newResultSet;
	}
	
	//Get the current meal information
	private String getMealInfo(){
		HashMap<String, HashMap<Date, MealInfo>> allMeals = meal.getAllMeals();
		String mealsResultSet = "";
		
		for (String meal : allMeals.keySet()){			
			HashMap<Date, MealInfo> mealInstance = allMeals.get(meal);
			
			for (Date mealDate : mealInstance.keySet()){
				mealsResultSet += meal + "|";
				mealsResultSet += getDateString(mealDate) + "|";
				
				MealInfo mealInfo = mealInstance.get(mealDate);
				
				mealsResultSet += getTimeString(mealInfo.getStartTime()) + "|";
				mealsResultSet += mealInfo.getDuration() + "|";
				
				String foodItems = mealInfo.getFoodItems().toString();
				foodItems = foodItems.substring(1, foodItems.length()-1);
				
				mealsResultSet += foodItems + "\n";
			}
		}
		
		return mealsResultSet;
	}
	
	//Get meal data from the repository for the specified date
	private boolean retrieveDataFromRepository(String mealDesc, Date mealDate, Gem gem){	
		String dataElements[];
		String fileContent = "";
		Scanner fileScanner = null;
		String dateStr = getDateString(mealDate);
		
		try {
			fileScanner = new Scanner(new File(SAVE_PATH));
			fileContent = fileScanner.useDelimiter("\\Z").next();
		} catch (FileNotFoundException e1) {
			return false;
		} catch (NoSuchElementException e2){}
		finally{
			if (fileScanner != null)
				fileScanner.close();
		}
		
		if (fileContent != null && !fileContent.trim().equals("")){
			dataElements = fileContent.split("\n");
			
			for (int i = dataElements.length - 1; i >= 0; i--){
				int index = dataElements[i].indexOf(dateStr);
				if (index >= 0 && dataElements[i].startsWith(mealDesc)){
					loadData(dataElements[i]);
					break;
				}
			}
		}
		else{
			return false;
		}
		
		if (mealDesc.equals("breakfast"))
			resetBreakfastTable(gem, mealDate);
		else if (mealDesc.equals("lunch"))
			resetLunchTable(gem, mealDate);
		else{
			if (mealDesc.equals("dinner"))
				resetDinnerTable(gem, mealDate);
		}
			
		return true;
	}
	
	//Store meal information
	private void loadData(String mealData) {
		String mealInfoList[] = mealData.split("\\|");
				
		Date mealDate = this.getDateInstance(mealInfoList[1]);
		Date startTime = this.getTimeInstance(mealInfoList[2]);
		
		Calendar startTimeCal = new GregorianCalendar();
		startTimeCal.setTime(startTime);
		
		int duration = Integer.parseInt(mealInfoList[3]);
		String foodItems[] = mealInfoList[4].split(",");
		
		List<String> foodList = new ArrayList<String>();
		for (String item : foodItems){
			foodList.add(item.trim());
		}
		
		//Store all meal information
		if (mealDate != null){
			meal.addInfo(mealInfoList[0], mealDate, startTimeCal, foodList, duration);
		}
	}	

	//Method for the quit command
	public void quit(Gem gem){		
	}
	
	//Get the specified date string as a date object
	private Date getDateInstance(String dateStr) {
		Date date;
		try {
			date = new SimpleDateFormat("dd-MMM-yyyy").parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
		return date;
	}
	
	//Get the specified time string as a date object
	private Date getTimeInstance(String timeStr) {
		Date time;
		try {
			time = new SimpleDateFormat("h:mm a").parse(timeStr);
		} catch (ParseException e) {
			return null;
		}
		return time;
	}
	
	//Get the specified calendar as a date object
	private Date getDateInstance(Calendar mealDate){
		try {
			String dateString = (new SimpleDateFormat("dd-MMM-yyyy").format(mealDate.getTime()));
			Date date = new SimpleDateFormat("dd-MMM-yyyy").parse(dateString);
			return date;
		} catch (ParseException e) {
			return null;
		}
	}
	
	//Get the specified date as a string
	private String getDateString(Date mealDate){
		String dateString = (new SimpleDateFormat("dd-MMM-yyyy").format(mealDate));
		return dateString;
	}
	
	//Get the specified time as a string
	private String getTimeString(Calendar mealStartTime){
		String dateString = (new SimpleDateFormat("h:mm a").format(mealStartTime.getTime()));
		return dateString;
	}
	
	//Method to determine whether to ask the user to confirm his/her
	//intent to exit the app
	public boolean confirmAppExit(Gem gem){
		return true;
	}
		
	//Method to confirm whether to exit the app or not
	public boolean appShouldQuit(Gem gem){
		boolean questionResponse = gem.getBooleanParameter("confirmExit");
			
		if (questionResponse)
			return true;
		else
			return false;
	}
}