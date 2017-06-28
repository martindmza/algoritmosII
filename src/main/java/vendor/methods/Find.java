package vendor.methods;

import java.sql.Connection;
import java.util.List;

import vendor.exceptions.EntityNotFoundException;
import vendor.fieldstypes.FieldsTypesFactory;
import vendor.fieldstypes.PrimitiveField;

public class Find extends Utn {

	// Retorna: una fila identificada por id o null si no existe
	// Invoca a: query
	public static <T> T find(Connection con, Class<T> dtoClass, Object id) throws EntityNotFoundException {
		PrimitiveField idAttribute = FieldsTypesFactory.getIdAttribute(dtoClass, getAlias(dtoClass));
		String idAttrName = idAttribute.getAttribute().getName();

		List<T> result = Query.query(con, dtoClass, "$" + idAttrName + "= ? ", id);

		if (result.size() == 0) {
			throw new EntityNotFoundException();
		}
		return result.get(0);
	}

}
