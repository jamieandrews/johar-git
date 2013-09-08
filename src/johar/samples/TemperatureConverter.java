/*
 * Application Engine of the Temperature Converter App 
 * 
 */

import java.text.DecimalFormat;
import johar.gem.Gem;

public class TemperatureConverter {
	
	//Initialization Method displays a Welcome message to the user
	public void initTemperatureConverter(Gem gem){
		String message = "Welcome to the Temperature Converter App.\n"
				+ "Conversion from Celsius to Fahrenheit, and vice-versa, "
				+ "just got easier!";
		gem.showText(message, 2000);
		gem.showText("Ready", 1000);
	}
	
	//Method to convert Celsius to Fahrenheit
	public void celsiusToFahrenheit(Gem gem){
		double celsiusTemp = gem.getFloatParameter("celsius");
		double fahrenheitTemp = convertToFahrenheit(celsiusTemp);
		fahrenheitTemp = Double.parseDouble(
				new DecimalFormat("0.0").format(fahrenheitTemp)
				);
		String outputMessage = "Temperature in Celsius: " 
				+ celsiusTemp + "\n"
				+ "Temperature in Fahrenheit: " + fahrenheitTemp;
		gem.showText(outputMessage, 2000);
	}
	
	//Method to convert Fahrenheit to Celsius
	public void fahrenheitToCelsius(Gem gem){
		double fahrenheitTemp = gem.getFloatParameter("fahrenheit");
		double celsiusTemp = convertToCelsius(fahrenheitTemp);
		celsiusTemp = Double.parseDouble(
				new DecimalFormat("0.0").format(celsiusTemp)
				);
		String outputMessage = "Temperature in Fahrenheit: " 
				+ fahrenheitTemp + "\n"
				+ "Temperature in Celsius: " + celsiusTemp;
		gem.showText(outputMessage, 2000);
	}
	
	//Actual Fahrenheit to Celsius conversion is done here
	private double convertToFahrenheit(double celsiusTemp){
		double fahrenheitTemp = (celsiusTemp * (9.0/5.0)) + 32.0;
		return fahrenheitTemp;
	}
	
	//Actual Fahrenheit to Celsius conversion is done here
	private double convertToCelsius(double fahrenheitTemp){
		double celsiusTemp = (fahrenheitTemp - 32.0) * (5.0/9.0);
		return celsiusTemp;
	}
	
	//Method is called when user expresses intent to exit the app
	public void exitApp(Gem gem){
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
