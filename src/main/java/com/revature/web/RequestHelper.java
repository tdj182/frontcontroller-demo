package com.revature.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.dao.EmployeeDao;
import com.revature.models.Employee;
import com.revature.service.EmployeeService;

public class RequestHelper {
	
	private static Logger log = Logger.getLogger(RequestHelper.class);
	private static EmployeeService eserv = new EmployeeService(new EmployeeDao());
	private static ObjectMapper om = new ObjectMapper();

	public static void processEmployees(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		// 1. set the content type to return text to the browser
		response.setContentType("text/html");
		
		// 2. get the list of all employees in the database
		List<Employee> allEmps = eserv.findAll(); 
		
		// 3. turn the list of java objects into JSON string (using Jackson Databind Object Mapper)
		String json = om.writeValueAsString(allEmps);
		
		// 4 use a print writer to write the objects to the response body seen in the browser
		PrintWriter out = response.getWriter();
		out.println(json);
	}
	
	public static void processLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		// we need to capture the user input
		BufferedReader reader = request.getReader();
		
		StringBuilder s = new StringBuilder();
		
		// transfer everything over to the string builder FROM the buffered reader
		String line = reader.readLine();
		
		while(line != null) {
			
			s.append(line);
			line = reader.readLine(); // req body looks like this username=bob&password=secret
			
		}
		
		String body = s.toString();
		String [] sepByAmp = body.split("&");
		
		List<String> values = new ArrayList<String>();
		
		for (String pair : sepByAmp) {  // each element in array looks like this
										// username=bob, password=pass
			values.add(pair.substring(pair.indexOf("=") + 1));
		}
		
		// captuer the actual username and password values
		String username = values.get(0);
		String password = values.get(1);
		
		log.info("User attempted to login with username " + username);
		
		// call the confirmLogin() method!
		Employee e = eserv.confirmLogin(username, password);
		
		
		// return found user and show the object in the browser
		if (e != null) {
			HttpSession session = request.getSession();
			session.setAttribute("user", e);
			
			//print logged in user to the screen
			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			
			//convert the object with the object mapper
			out.println(om.writeValueAsString(e));
			
			//log it
			log.info("The user " + username + " has logged in.");
		} else {
			response.setStatus(204); // No Content status (successful request, but no user)
		}
		
	}

	public static void processError(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		// if something goes wrong, redirect to custom 404 error page
		request.getRequestDispatcher("error.html").forward(request, response);
	}
	
	
	
	
	
}
