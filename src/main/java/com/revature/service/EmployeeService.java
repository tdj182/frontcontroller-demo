package com.revature.service;

import java.util.List;
import java.util.Optional;

import com.revature.dao.EmployeeDao;
import com.revature.models.Employee;

public class EmployeeService {

	private EmployeeDao edao;
	
	// Introduce Dependency Injection via Constructor injection
	public EmployeeService(EmployeeDao dao) {
		super();
		this.edao = dao;
	}
	
	// method: confirmLogin -> let's call the findAll() from the DAO, and use a stream to confirm username & password
	public Employee confirmLogin(String username, String password) {
		
		// Optional replaces list in this case because Optional can assigned as null;
		Optional<Employee> emp = edao.findAll() 
									.stream()
									.filter(e -> (e.getUsername().equals(username) && e.getPassword().equals(password)))
									.findFirst(); 
		
		
		return (emp.isPresent() ? emp.get() : null);
	}
	
	public List<Employee> findAll() {
		return edao.findAll();
	}
	
	public int insert(Employee e) {
		return edao.insert(e);
	}
	
	public boolean update(Employee e) {
		return edao.update(e);
	}
	
	public boolean delete(Employee e) {
		return edao.delete(e);
	}
}
