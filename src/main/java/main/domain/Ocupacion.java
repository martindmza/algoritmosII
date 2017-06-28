package main.domain;

import vendor.ann.Column;
import vendor.ann.Id;
import vendor.ann.ManyToOne;
import vendor.ann.Table;

@Table(name="ocupacion",alias="o")
public class Ocupacion
{
	@Id(strategy=Id.IDENTITY)
	@Column(name="id")
	private Integer idOcupacion;
	
	@Column(name="descripcion")
	private String descripcion;

	@ManyToOne(type=TipoOcupacion.class, name="id_tipo_ocupacion")
	public TipoOcupacion tipoOcupacion;
	
	public Integer getIdOcupacion()
	{
		return idOcupacion;
	}
	
	public Ocupacion () {}
	
	public Ocupacion (String descripcion, TipoOcupacion tipoOcupacion) {
		this.descripcion = descripcion;
		this.tipoOcupacion = tipoOcupacion;
	}

	public void setIdOcupacion(Integer idOcupacion)
	{
		this.idOcupacion=idOcupacion;
	}

	public String getDescripcion()
	{
		return descripcion;
	}

	public void setDescripcion(String descripcion)
	{
		this.descripcion=descripcion;
	}

	public TipoOcupacion getTipoOcupacion()
	{
		return tipoOcupacion;
	}

	public void setTipoOcupacion(TipoOcupacion tipoOcupacion)
	{
		this.tipoOcupacion=tipoOcupacion;
	}

	@Override
	public String toString()
	{
		return getDescripcion() + " (" + getTipoOcupacion().getDescripcion() + ")";
	}

	@Override
	public boolean equals(Object o)
	{
		Ocupacion other = (Ocupacion)o;
		boolean ok = true;
		ok = ok && idOcupacion==other.getIdOcupacion();
		ok = ok && descripcion.equals(other.getDescripcion());
		
		if( tipoOcupacion!=null )
		{
			ok = ok && tipoOcupacion.getIdTipoOcupacion()==other.getTipoOcupacion().getIdTipoOcupacion();
		}
		else
		{
			ok = ok && other.getTipoOcupacion()==null;
		}
		
		return ok;
	}	
}
