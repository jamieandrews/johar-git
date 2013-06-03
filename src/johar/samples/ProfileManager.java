import java.text.SimpleDateFormat;
import java.util.Date;

import johar.gem.Gem;

public class ProfileManager {

	public void initProfileManager(Gem gem) {
		gem.showText("Profile Manager 1.0", 1000);
		gem.showText("Profile Manager App started on " + new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a").format(new Date()) + ".\n\nThe Profile Manager helps you manage your personal information, such as biodata, contacts, resumes, and others.", 2000);
	}
	
	public boolean profilerActive(Gem gem) {
		return true;
	}
	
	public void createProfile(Gem gem) {
		String output = "\tYOUR PROFILE\n\n";
		String lastName = "";
		String firstName = "";
		String otherName = "";
		int nameCount = gem.getParameterRepCount("names");
		if (nameCount == 3) {
			lastName = gem.getStringParameter("names", 0);
			firstName = gem.getStringParameter("names", 1);
			otherName = gem.getStringParameter("names", 2);
			output += "Surname: " + lastName.toUpperCase() + "\n" + "First Name: " + firstName.toUpperCase() + 
					"\n" + "Other Name: " + otherName.toUpperCase() + "\n";
		}
		else{
			lastName = gem.getStringParameter("names", 0);
			firstName = gem.getStringParameter("names", 1);
			output += "Surname: " + lastName.toUpperCase() + "\n" + "First Name: " + firstName.toUpperCase() + "\n";
		}					
		
		String address = gem.getStringParameter("address");
		String city = gem.getStringParameter("city");
		String province = gem.getStringParameter("province");
		String country = gem.getStringParameter("country");		
		String phone = gem.getStringParameter("phone");
		String email = gem.getStringParameter("email");
		int age = (int) gem.getIntParameter("age");
		String gender = gem.getStringParameter("sex");
		String mStatus = gem.getStringParameter("maritalStatus");
		boolean employStatus = gem.getBooleanParameter("employed");
		
		output += "Address: " + address.toUpperCase() + "\nCity: " + city.toUpperCase() + "\nProvince: " + province.toUpperCase() + 
				"\nCountry: " + country.toUpperCase() + "\nPhone: " + phone.toUpperCase() + "\nEmail Address: " + 
				email.toUpperCase() + "\nAge: " + age + "\nGender: " + gender.toUpperCase() + "\nMarital Status: " + 
				mStatus.toUpperCase() + "\nEmployed? " + ((employStatus) ? "YES" : "NO") + "\n";
		
		int uploadCount = gem.getParameterRepCount("upload");
		
		for (int i = 0; i < uploadCount; i++){
			output += "Uploaded File " + (i + 1) + ": " + gem.getParameter("upload", i).toString() + "\n";
		}
		
		gem.showText(output, 2000);
	}
	
	public void editProfile(Gem gem) {
	}
	
	public String checkInputs(Gem gem) {
		return "";
	}
	
}
