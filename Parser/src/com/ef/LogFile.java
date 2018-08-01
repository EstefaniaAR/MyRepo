package com.ef;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
public class LogFile 
{
	private int id;
	private String date;
	private String ip;
	private String request;
	private String status;
	private String userAgent;
	
	
	public List<LogFile>getFile()
	{
		List <LogFile> res = new ArrayList<LogFile>();
		File file = new File(getClass().getResource("access.log").getPath());
		BufferedReader b = null;
		try
		{
			b = new BufferedReader(new FileReader(file));
			String readLine = "";
			while ((readLine = b.readLine()) != null)
			{
				String [] line = readLine.split("\\|");
				if(line.length == 5)
				{
					LogFile f = new LogFile();
					f.setDate(line[0]);
					f.setIp(line[1]);
					f.setRequest(line[2]);
					f.setStatus(line[3]);
					f.setUserAgent(line[4]);
					res.add(f);
				}
	        }
			b.close();
		}
		catch (IOException e)
		{
            e.printStackTrace();
        }
		finally 
		{
			try 
			{
				if(b!=null)
					b.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		
		return res;
	}

	public List<LogFile>getLogFilesRange(String startDate, int duration,List<LogFile>file)
	{
		List<LogFile>res = new ArrayList<LogFile>();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss");
		DateTimeFormatter dtfLog = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		
		LocalDateTime localStartDate = LocalDateTime.parse(startDate,dtf);
		LocalDateTime localEndDate = localStartDate.plusHours(duration);
		for(LogFile f :file)
		{
			LocalDateTime fDate = LocalDateTime.parse(f.getDate(),dtfLog);
			if(fDate.isAfter(localStartDate)&& fDate.isBefore(localEndDate))
			{
				res.add(f);
			}
		}
		return res;
	}
	
	public Map<String,Long>getMostFrecuentIPs(List<LogFile>res,int threshold)
	{
		Map<String,Long>result=new HashMap<String,Long>();
		Map<String, Long> counting = res.stream().collect(
                Collectors.groupingBy(LogFile::getIp, Collectors.counting()));
		for (Map.Entry<String, Long> entry : counting.entrySet()) 
		{
	        if(entry.getValue()>=threshold)
	        {
	        	result.put(entry.getKey(), entry.getValue());
	        }
	    }
		return result;
	}

	public boolean insertLogFile(LogFile file)
	{
		boolean res=false;
		MyConnection con = new MyConnection();
		try 
		{
			con.getConnection();
			String query = " INSERT INTO users "
					+ "                 ("
					+ "                  	 date"
					+ "                  	, ip"
					+ "					 	, request"
					+ "                  	, status"
					+ "                 	, userAgent"
					+ "                  )"
			        + " values 			("
			        + "						, ?"
			        + "						, ?"
			        + "						, ?"
			        + "						, ?"
			        + "                     , ?"
			        + "					)";
			Connection conn = new MyConnection().getConnection();
			PreparedStatement preparedStmt = conn.prepareStatement(query);
		    preparedStmt.setString (1, file.getDate());
		    preparedStmt.setString (2, file.getIp());
		    preparedStmt.setString (3, file.getStatus());
		    preparedStmt.setString (4, file.getUserAgent());
		    res=preparedStmt.execute();
		    System.out.println(res);
		    conn.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return res;
	}
	public Map<String, List<LogFile>>getMostFrecuentLogFileByIP(Map<String,Long>frecIP,List<LogFile> file )
	{
		Map<String, List<LogFile>> groups = file.stream().collect(Collectors.groupingBy(LogFile::getIp));
		Map<String, List<LogFile>> res = new HashMap<String,List<LogFile>>();
		for (Map.Entry<String, Long> entry : frecIP.entrySet()) 
		{
	        res.put(entry.getKey(), groups.get(entry.getKey()));
	    }
		return res;
		
	}
	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public String getIp() {
		return ip;
	}


	public void setIp(String ip) {
		this.ip = ip;
	}


	public String getRequest() {
		return request;
	}


	public void setRequest(String request) {
		this.request = request;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getUserAgent() {
		return userAgent;
	}


	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	@Override
	public String toString() 
	{
		return "LogFile [date=" + date + ", ip=" + ip + ", request=" + request + ", status=" + status + ", userAgent="
				+ userAgent + "]";
	}

}
