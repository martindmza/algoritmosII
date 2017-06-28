package main.domain;

import java.util.List;

import vendor.ann.Column;
import vendor.ann.Gui;
import vendor.ann.Id;
import vendor.ann.JoinColumn;
import vendor.ann.OneToMany;
import vendor.ann.Relation;
import vendor.ann.Table;

@Table(name="department", alias="dept")
public class Dept 
{
	@Id(strategy=Id.ASSIGNED)
	@Column(name="id")
	@Gui(editable=true)
	private Integer id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="loc")
	private String loc;
	
	@OneToMany(type=Emp.class, att="dept", fetchType=OneToMany.LAZY)
	private List<Emp> emps;
	
	public Integer getId()
	{ 
		return id;
	}

	public void setId(Integer id)
	{
		this.id=id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name=name;
	}

	public String getLoc()
	{
		return loc;
	}

	public void setLoc(String loc)
	{
		this.loc=loc;
	}

	@Override
	public String toString()
	{
		return "[" + getId() + " - " + getName() + " - " + getLoc() + "]";
	}

	public List<Emp> getEmps()
	{
		return emps;
	}

	public void setEmps(List<Emp> emps)
	{
		this.emps = emps;
	}	
	
	public boolean equals(Object o)
	{
		return ((Dept)o).getId()==getId();
				
	}
}
