package main.domain;

import vendor.ann.Column;
import vendor.ann.Id;
import vendor.ann.ManyToOne;
import vendor.ann.Table;

@Table(name = "persona", alias = "p")
public class Persona {
	@Id(strategy = Id.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "nombre")
	private String nombre;

	@ManyToOne(type = Direccion.class, name = "id_direccion")
	public Direccion direccion;

	@ManyToOne(type = Ocupacion.class, name = "id_ocupacion", fetchType=ManyToOne.LAZY)
	public Ocupacion ocupacion;
	
	public Persona(){}
	
	public Persona (String nombre, Direccion direccion, Ocupacion ocupacion) {
		this.nombre = nombre;
		this.direccion = direccion;
		this.ocupacion = ocupacion;
	}

	public Persona(String nombre) {
		this.nombre = nombre;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Direccion getDireccion() {
		return direccion;
	}

	public void setDireccion(Direccion direccion) {
		this.direccion = direccion;
	}

	public Ocupacion getOcupacion() {
		return ocupacion;
	}

	public void setOcupacion(Ocupacion ocupacion) {
		this.ocupacion = ocupacion;
	}

	@Override
	public String toString() {
		return "Persona [idPersona=" + id + ", nombre=" + nombre + ", direccion=" + direccion + ", ocupacion="
				+ ocupacion + "]";
	}

	@Override
	public boolean equals(Object o) {
		Persona other = (Persona) o;
		boolean ok = true;
		ok = ok && id == other.getId();
		ok = ok && nombre.equals(other.getNombre());

		if (direccion != null) {
			ok = ok && direccion.getIdDireccion() == other.getDireccion().getIdDireccion();
		} else {
			ok = ok && other.getDireccion() == null;
		}

		if (ocupacion != null) {
			ok = ok && ocupacion.getIdOcupacion() == other.getOcupacion().getIdOcupacion();
		} else {
			ok = ok && other.getOcupacion() == null;
		}

		return ok;
	}

}
