package com.etz.loanCalculator.utility;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Random;
import java.util.ResourceBundle;

public class Utils
{
	protected static ClassLoader getCurrentClassLoader(Object defaultObject)
	{
		ClassLoader loader = Thread.currentThread().getContextClassLoader();

		if(loader == null)
		{
			loader = defaultObject.getClass().getClassLoader();
		}

		return loader;
	}

	public static String getMessageResourceString(String bundleName, String key, Object params[], 
		Locale locale)
	{
		String text = null;

		ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale, getCurrentClassLoader(params));

		try
		{
			text = bundle.getString(key);
		}
		catch(MissingResourceException e)
		{
			text = "?? key " + key + " not found ??";
		}

		if(params != null)
		{
			MessageFormat mf = new MessageFormat(text, locale);
			text = mf.format(params, new StringBuffer(), null).toString();
		}

		return text;
	}
	
	
	public String generateRandomNumber(int size) {
        String value = "";
        for (int t = 0; t < size; t++) {
            value = value + new Random().nextInt(9);
        }
        return value;
    }
}
