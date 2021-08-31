package com.giftingnetwork.util;

import java.util.*;

public class MyClass {
	public static void main(String args[]) {

String a =   "it is a AND new AND ";

System.out.println(  a.lastIndexOf("AND")  ) ;


    System.out.println(  a.substring( 0 , a.lastIndexOf("AND")) );
 


		List<Employee> employees = EmployeeDAO.getAllEmployees();
		for (Employee emp : employees) {
			for (String conList : emp.getContactNumbers()) {
				System.out.println(conList);
			}
		}
	}


	


}

class EmployeeDAO {
	public static List<Employee> getAllEmployees() {




		List<Employee> employees = new ArrayList<>();

		employees.add(new Employee(111, "Ramesh", "IT", 600000, Arrays.asList("111-000-0000", "111-000-0001")));
		employees.add(new Employee(222, "John", "FINANCE", 500000, Arrays.asList("222-000-0000", "222-000-0001")));
		employees.add(new Employee(333, "Mike", "HR", 300000, Arrays.asList("333-000-0000", "333-000-0001")));

		return employees;
	}

}

class Employee {
	private int id;
	private String name;
	private String department;
	private int salary;
	private List<String> contactNumbers;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	public List<String> getContactNumbers() {
		return contactNumbers;
	}

	public void setContactNumbers(List<String> contactNumbers) {
		this.contactNumbers = contactNumbers;
	}

	public Employee(int id, String name, String department, int salary, List<String> contactNumbers) {
		this.id = id;
		this.name = name;
		this.department = department;
		this.salary = salary;
		this.contactNumbers = contactNumbers;
	}

	@Override
	public String toString() {
		return "Employee [contactNumbers=" + contactNumbers + ", department=" + department + ", id=" + id + ", name="
				+ name + ", salary=" + salary + "]";
	}
}
