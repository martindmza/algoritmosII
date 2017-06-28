package main.domain;

import vendor.ann.Column;
import vendor.ann.Id;
import vendor.ann.Table;

@Table(name="tipo_ocupacion",alias="top")
public class TipoOcupacion
{
	@Id(strategy=Id.IDENTITY)
	@Column(name="id")
	private Integer idTipoOcupacion;

	@Column(name="descripcion")
	private String descripcion;
	
	public TipoOcupacion () {}
	
	public TipoOcupacion (String descripcion) {
		this.descripcion = descripcion;
	}
	
	public Integer getIdTipoOcupacion()
	{
		return idTipoOcupacion;
	}

	public void setIdTipoOcupacion(Integer idTipoOcupacion)
	{
		this.idTipoOcupacion=idTipoOcupacion;
	}

	public String getDescripcion()
	{
		return descripcion;
	}

	public void setDescripcion(String descripcion)
	{
		this.descripcion=descripcion;
	}

	@Override
	public String toString()
	{
		return getDescripcion();
	}

	@Override
	public boolean equals(Object obj)
	{
		TipoOcupacion other=(TipoOcupacion)obj;
		if(descripcion==null)
		{
			if(other.getDescripcion()!=null) return false;
		}
		else if(!descripcion.equals(other.getDescripcion())) return false;
		if(idTipoOcupacion!=other.getIdTipoOcupacion()) return false;
		return true;
	}
	
	
}
