package foo.bar;

import java.util.Date;

public class Person {
	
	private String name;
	private Date birth;
	private Salary salary;

	public Salary getSalary() {
		return salary;
	}

	public void setSalary(Salary b) {
		this.salary = b;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	
	 

}
