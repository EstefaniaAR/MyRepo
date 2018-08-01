package com.ef;

import java.util.List;
import java.util.Map;

public class Parser 
{
	public static void main (String ...args)
	{
		args = new String[]{
	            "--startDate=2017-01-01.13:00:00", 
	            "--duration=daily",
	            "--threshold=250"};
	            
		Options opt = new Options();
		LogFile lf = new LogFile();
		Map<String, String>options = opt.checkOptions(args);
		List<LogFile>res =null;
		if(!options.containsKey("INVALID"))
		{
			res=lf.getFile();
			if(options.containsKey("--startDate") 
					&& options.containsKey("--threshold") 
					&& options.containsKey("--duration"))
			{
				int duration = opt.getDuration(options.get("--duration"));
				int threshold = Integer.parseInt(options.get("--threshold"));
				res = lf.getLogFilesRange(options.get("--startDate"), duration,res);
				Map<String,Long>ips=lf.getMostFrecuentIPs(res,threshold);
				Map<String,List<LogFile>>regs=lf.getMostFrecuentLogFileByIP(ips, res);
				System.out.println("The Most frecuent IP's that made a request:");
				for (Map.Entry<String, Long> entry : ips.entrySet()) 
				{
					System.out.println("IP:"+entry.getKey()+", Request Number:"+entry.getValue());
				}
				for (Map.Entry<String, List<LogFile>> entry : regs.entrySet()) 
				{
					for(LogFile f:entry.getValue())
					{
						//System.out.println("Reg: "+f);
					}
				}
				
			}
			
		}
		

	}

}

