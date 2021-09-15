package com.revature.util;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

	private static SessionFactory sf = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
	
	private static Session ses; // Session gives more functionality such as save, get, load, delete
	
	public static Session getSession() {
		
		if (ses == null) {
			ses = sf.openSession(); // This returns a JDBC connection to our database
		}
		
		return ses;
	}
	
	public static void closeSession() {
		ses.close();
		sf.close();
	}
	
}
