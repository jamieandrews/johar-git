/* TextInputValidator.java
 * This source file is part of the Johar project.
 * @author Fatima Hussain
 * @author Jamie Andrews
 */
package johar.utilities;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*
 * A class with some basic functions that are
 * facilitate text input, and general text
 * validation.
 */
public class TextInputValidator {
    /*
     * Checks if the string str is a lower case identifier,
     * @return returns true if the string str starts from a lower case
     * letter, otherwise false
     */
    public static boolean isLowerCase(String str) {
	if (str.length() > 0) {
	    char ch = str.charAt(0);
	    if ( Character.isLowerCase(ch) ) {
		return true;
	    }
	}
	return false;
    }

    /*
     * Checks if the string str is a upper case identifier,
     * @return returns true if the string str starts from a upper case
     * letter, otherwise false
     */
    public static boolean isUpperCase(String str) {
	if (str.length() > 0) {
	    char ch = str.charAt(0);
	    if ( Character.isUpperCase(ch) ) {
		return true;
	    }
	}
	return false;
    }

    /*
     * Converts the string str to Camel case, as specified in the
     * IDF design document.
     * Examples:
     * add --> Add
     * saveAs --> Save as
     * findInThisPase --> Find in this page
     * iputXMLFile --> Input x m l file
     * iputXmlFile --> Input xml file
     * iputJavaFile --> Input java file
     *
     */
    public static String camelCaseTranslation(String str) {
	int index = 0;
	String tmpWord = "";
	String result = "";

	// breaks the string into words,
	// depending on the capital letters in
	// the string.
	while ( index < str.length() ) {
	    char ch = str.charAt(index);
	    if ( Character.isLowerCase(ch) ) {
		tmpWord += ch;
	    } else if ( Character.isUpperCase(ch) ) {
		result += tmpWord + " ";
		tmpWord = Character.toLowerCase(ch) + "";
	    }
	    index++;
	}
	result += tmpWord + " ";
	// Capitalize the first letter of the final string.
	if ( isLowerCase(result) ) {
	    result = Character.toUpperCase( result.charAt(0) ) +
	             result.substring(1);
	}
	if (result.charAt(result.length() - 1) == ' ') {
	    result = result.substring(0, result.length() - 1);
	}
	return result;
    } /* camelCaseTranslation */

    /*
     * Convers the string str to Title Case.
     * Examples:
     * add --> Add
     * saveAs --> Save As
     * findInThisPase --> Find In This page
     * iputXMLFile --> Input X M L File
     * iputXmlFile --> Input Xml File
     * iputJavaFile --> Input Java File
     *
     */
    public static String titleCaseTranslation(String str) {
	int index = 0;
	String tmpWord = "";
	String result = "";

	// breaks the string into words,
	// depending on the capital letters in
	// the string.
	while ( index < str.length() ) {
	    char ch = str.charAt(index);
	    if ( Character.isLowerCase(ch) ) {
		tmpWord += ch;
	    } else if ( Character.isUpperCase(ch) ) {
		result += tmpWord + " ";
		tmpWord = ch + "";
	    }
	    index++;
	}
	result += tmpWord + " ";
	if ( isLowerCase(result) ) {
	    result = Character.toUpperCase( result.charAt(0) ) +
	             result.substring(1);
	}
	if (result.charAt(result.length() - 1) == ' ') {
	    result = result.substring(0, result.length() - 1);
	}
	return result;
    } /* titleCaseTranslation */

    /*
     * The method removes the specified char from the string s.
     */
    public static String remove(String s, char toBeRemoved) {
	String str = "";

	for (int i = 0; i < s.length(); i++) {
	    if ( !(s.charAt(i) == toBeRemoved) ) {
		str += s.charAt(i);
	    }
	}
	return str;
    }

    /*
     * Assumption: The string is of form "--------".
     * @return returns true if the string is enclosed in
     * double-quotes.
     */
    public static boolean isDoubleQuoted(String s) {
	if (s != null) {
	    if ( (s.charAt(0) == '"') &&
	        (s.charAt(s.length() - 1) == '"') ) {
		return true;
	    }
	}
	return false;
    }

    /*
     * Assumption: The string is of form "--------".
     * @return returns the string without the double-quotes.
     */
    public static String removeDoubleQuotes(String s) {
	String str = s.substring(1, s.length() - 1);

	return str;
    }

    /*
     * Checks if a String is a valid digit.
     * @return returns true if the string comprises of a valid
     * digit
     */
    public static boolean isDigit(String token) {
	// String expression =
	// "[\\s]*[\\w]+[\\s]*[={1,1}][\\s]*[-+]?[\\d]*\\.?[\\d]+" ;
	// String expression = "[-+]?[\\d]*\\.?[\\d]+" ;
	String expression = "^[-+]?[0-9]*\\.?[0-9]+$";
	Pattern p = Pattern.compile(expression);
	Matcher m = p.matcher(token);

	return m.matches();
    }

    /*
     * @return returns true if the string is not null or
     * empty.
     */
    public static boolean NotNullOrEmpty(String str) {
	if ( (str != null) && (str.length() != 0) ) {
	    return true;
	}
	return false;
    }

    /**
     * @return returns true if the string has length less than or equal
     *   to maxLength, and contains a carriage return iff crPermitted.
     */
    public static boolean respectsConstraints(String s, int maxLength,
	     boolean crPermitted) {
	if (!crPermitted && s.contains("\n")) {
	    return false;
	}
	if (s.length() > maxLength) {
	    return true;
	} else {
	    return false;
	}
    }

}
