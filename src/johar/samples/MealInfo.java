import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class MealInfo {
	private List<String> _mealInfoList;
	private int _duration;
	private Calendar _startTime;
	
	public MealInfo(List<String> mealInfoList, Calendar startTime, int duration) {
		_mealInfoList = mealInfoList;	
		_startTime = startTime;
		_duration = duration;
	}
	
	public int getDuration(){
		return _duration;
	}
	
	public int getFoodItemsCount(){
		return _mealInfoList.size();
	}
	
	public Calendar getStartTime(){
		return _startTime;		
	}
	
	public Calendar getEndTime(){		
		Calendar endTimeCal = (GregorianCalendar) _startTime.clone();
		endTimeCal.add(Calendar.MINUTE, _duration);
		
		return endTimeCal;		
	}
	
	public List<String> getFoodItems(){
		List<String> foodList = new ArrayList<String>();
		for (int i = 0; i < _mealInfoList.size(); i++){
			foodList.add(_mealInfoList.get(i).toString());
		}
		return foodList;
	}
	
	public boolean isEmpty(){
		if (_mealInfoList.size() == 0){
			return true;
		}
		return false;
	}

}
