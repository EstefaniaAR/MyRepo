package com.ef;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class Options 
{
	public static final String STARTDATE="--startDate";
	public static final String DURATION = "--duration";
	public static final String THRESHOLD="--threshold";
	public static final String INVALID ="invalid";
	
	public Map<String,String> checkOptions(String [] args)
	{
		Map <String,String> map = new HashMap<String,String>();
		for(String arg: args)
		{
			String []res=arg.split("=");
			if(res.length==2)
			{
				if(res[0].trim().equals(Options.STARTDATE))
				{
					try 
					{
						new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").parse(res[1].trim());
						map.put(STARTDATE, res[1].trim());
					}
					catch (ParseException e) 
					{
						System.out.println("Invalid option: "+arg);
						e.printStackTrace();
						break;
					}
				}
				else if(res[0].trim().equals(Options.DURATION))
				{
					if(res[1].trim().equals("hourly") || res[1].trim().equals("daily"))
					{
						map.put(Options.DURATION,res[1].trim());
					}
					else
					{
						System.out.println("Invalid option: "+arg);
						break;
					}
				}
				else if(res[0].trim().equals(Options.THRESHOLD))
				{
					String pattern = "[0-9]+";
					if(res[1].trim().matches(pattern))
					{
						map.put(Options.THRESHOLD,res[1].trim());
					}
					else
					{
						System.out.println("Invalid option: "+arg);
						break;
					}
				}
				else
				{
					System.out.println("Invalid option: "+arg);
					break;
				}
			}
		}
		
		return map;
	}
	
	public int getDuration(String duration)
	{
		int res =0;
		if(duration.equals("hourly"))
		{
			res =1;
		}
		if(duration.equals("daily"))
		{
			res =24;
		}
		return res;
	}

}
