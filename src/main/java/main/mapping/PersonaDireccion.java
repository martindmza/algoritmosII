package main.mapping;

import vendor.ann.Column;
import vendor.ann.Id;
import vendor.ann.Table;

@Table(name="persona_direccion")
public class PersonaDireccion
{
	@Id(strategy=Id.IDENTITY)
	@Column(name="id_persona_direccion")
	private Integer idPersonaDireccion;
	
	@Column(name="id_persona")
	private Persona persona;

	@Column(name="id_direccion")
	private Direccion direccion;
	
	@Column(name="id_tipo_direccion")
	private TipoDireccion tipoDireccion;
	
	public TipoDireccion getTipoDireccion()
	{
		return tipoDireccion;
	}

	public void setTipoDireccion(TipoDireccion tipoDireccion)
	{
		this.tipoDireccion=tipoDireccion;
	}

	public Integer getIdPersonaDireccion()
	{
		return idPersonaDireccion;
	}

	public void setIdPersonaDireccion(Integer idPersonaDireccion)
	{
		this.idPersonaDireccion=idPersonaDireccion;
	}

	public Persona getPersona()
	{
		return persona;
	}

	public void setPersona(Persona persona)
	{
		this.persona=persona;
	}

	public Direccion getDireccion()
	{
		return direccion;
	}

	public void setDireccion(Direccion direccion)
	{
		this.direccion=direccion;
	}

	@Override
	public boolean equals(Object o)
	{
		PersonaDireccion other = (PersonaDireccion)o;		
		return other.getIdPersonaDireccion().equals(getIdPersonaDireccion())
			&& other.getPersona().equals(getPersona())
			&& other.getDireccion().equals(getDireccion());
	}	
}
