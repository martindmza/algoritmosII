package vendor.methods;

import java.sql.Connection;
import java.util.List;

public class FindAll extends Utn {

	// Retorna: una todasa las filas de la tabla representada por dtoClass
	// Invoca a: query
	public static <T> List<T> findAll(Connection con, Class<T> dtoClass)
	{
		return Query.query(con,dtoClass,"");
	}
}
