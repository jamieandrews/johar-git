import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Meal {
	private HashMap<String, HashMap<Date, MealInfo>> mealMap;
	private HashMap<Date, MealInfo> mealDateMap;
	
	public Meal() {
		mealMap = new HashMap<String, HashMap<Date, MealInfo>>();
	}
	
	public void addInfo(String mealName, Date mealDate, Calendar startTime, List<String> mealInfoList, int duration){
		MealInfo mealInfo = new MealInfo(mealInfoList, startTime, duration);
		
		if (mealMap.containsKey(mealName)){
			mealMap.get(mealName).put(mealDate, mealInfo);
		}
		else{
			mealDateMap = new HashMap<Date, MealInfo>();
			mealDateMap.put(mealDate, mealInfo);
			mealMap.put(mealName, mealDateMap);
		}			
	}
	
	public MealInfo getInfo(String mealName, Date mealDate){
		if (mealMap.containsKey(mealName)){
			if (mealMap.get(mealName).containsKey(mealDate))
				return mealMap.get(mealName).get(mealDate);
			else
				return null;
		}
		else
			return null;		
	}
	
	public HashMap<String, HashMap<Date, MealInfo>> getAllMeals(){
		return mealMap;
	}
}
