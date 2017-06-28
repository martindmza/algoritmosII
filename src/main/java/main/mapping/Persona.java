package main.mapping;

import java.util.List;
import vendor.ann.Column;
import vendor.ann.Id;
import vendor.ann.Relation;
import vendor.ann.Table;

@Table(name="persona")
public class Persona
{
	@Id(strategy=Id.IDENTITY)
	@Column(name="id_persona")
	private Integer idPersona;
	
	@Column
	public String nombre;
		
	@Column(fetchType=Column.LAZY)
	public Ocupacion ocupacion;

	@Relation(mappedBy="persona",fetchType=Column.LAZY)
	public List<PersonaDireccion> direcciones;
	
	public List<PersonaDireccion> getDirecciones()
	{
		return direcciones;
	}

	public void setDirecciones(List<PersonaDireccion> direcciones)
	{
		this.direcciones=direcciones;
	}

	public Integer getIdPersona()
	{
		return idPersona;
	}

	public void setIdPersona(Integer idPersona)
	{
		this.idPersona=idPersona;
	}

	public String getNombre()
	{
		return nombre;
	}

	public void setNombre(String nombre)
	{
		this.nombre=nombre;
	}

	public Ocupacion getOcupacion()
	{
		return ocupacion;
	}

	public void setOcupacion(Ocupacion ocupacion)
	{
		this.ocupacion=ocupacion;
	}

	@Override
	public boolean equals(Object o)
	{
		Persona other = (Persona)o;
		return other.getIdPersona().equals(getIdPersona())
			&& other.getNombre().equals(getNombre())
			&& other.getOcupacion().equals(getOcupacion());
	}
	
	public String toString()
	{
		String ocupacionName = null;
		if (ocupacion != null) {
			ocupacionName = ocupacion.toString();
		}
		
		return getNombre() + " - ocupacion : " + ocupacionName ;
	}
}
