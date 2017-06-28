package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import main.domain.Dept;
import main.domain.Direccion;
import main.domain.Emp;
import main.domain.Ocupacion;
import main.domain.TipoOcupacion;
import main.mapping.Persona;
import vendor.methods.Delete;
import vendor.methods.Find;
import vendor.methods.FindAll;
import vendor.methods.Insert;
import vendor.methods.Query;

/**
 * Hello world!
 *
 */
public class App {

	private static Connection con;

	public static void main(String[] args) throws Exception {

		try {
			con = getConection();

			boolean findall = true;
			boolean find = true;
			boolean query = true;
			boolean insert = false;
			boolean delete = false;

			if (findall) {
				System.out.println("======= FindAll ===============================================");
				System.out.println("Personas:");
				for (Persona p : FindAll.findAll(con, Persona.class)) {
					System.out.println(p);
				}
				System.out.println("======= FindAll ===============================================");
			}

			if (find) {
				System.out.println("======= Find    ===============================================");
				Persona p2 = Find.find(con, Persona.class, 12);
				System.out.println(p2);
				System.out.println("ocupacion: " + p2.getOcupacion());
				System.out.println("direcciones: " + p2.getDirecciones());
				System.out.println("======= Find    ===============================================");
			}

			if (query) {
				System.out.println("======= Query   ===============================================");
				for (Persona p : Query.query(con, Persona.class, "$ocupacion.tipoOcupacion.descripcion = ?",
						"Empleado")) {
					System.out.println(p);
				}

				for (Persona p : Query.query(con, Persona.class, "$nombre = ?", "Jose")) {
					System.out.println(p);
				}

				for (Persona p : Query.query(con, Persona.class,
						"$ocupacion.descripcion = ? AND $ocupacion.tipoOcupacion.descripcion = ?", "Ingeniero",
						"Profesional")) {
					System.out.println(p);
				}
				System.out.println("======= Query   ===============================================");
			}

			Persona p1 = new Persona();
			p1.setNombre("Bob");
			if (insert) {
				Direccion d1 = new Direccion("Rivadavia", 700);
				Insert.insert(con, d1);
				System.out.println("Objeto creado : " + d1);

				TipoOcupacion to1 = new TipoOcupacion("Docente");
				Ocupacion o1 = new Ocupacion("Profesor", to1);

				//p1.setDireccion(d1);
				//p1.setOcupacion(o1);

				Insert.insert(con, p1);
				System.out.println("Objeto creado : " + p1);
			}

			if (delete) {
				int resultado = Delete.delete(con, Persona.class, p1.getIdPersona());
				if (resultado == 1) {
					System.out.println("Objeto Eliminado : " + p1);
				} else {
					System.out.println("Objeto No Eliminado : " + p1);
				}
			}

			// int direccionBorrada = Delete.delete(con, Direccion.class,
			// direccionId);
			// if (direccionBorrada == 0) {
			// System.out.println("Error: Objeto " + nueva + " no eliminado!!");
			// } else {
			// System.out.println("Objeto " + nueva + " eliminado");
			// }
			//
			// //deletes por xql---
			// Insert.insert(con, new Direccion("Juan b Justo", 700));
			// Insert.insert(con, new Direccion("Juan b Justo", 1000));
			// Insert.insert(con, new Direccion("Juan b Justo", 1004));
			//
			// int direccionBorrada2 = Delete.delete(con, Direccion.class,
			// "calle=?", "Juan b Justo");
			// if (direccionBorrada == 0) {
			// System.out.println("Error: Objetos " + nueva + " no
			// eliminados!!");
			// } else {
			// System.out.println(direccionBorrada2 + " Objetos con xql
			// eliminados");
			// }

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
		}
		System.out.println("");
		System.out.println("have a nice day :)");
	}

	public static Connection getConection() {
		String usr = "root";
		String pwd = "root";
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/algoritmos";

		if (con != null) {
			return con;
		}

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, usr, pwd);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return con;
	}
}
