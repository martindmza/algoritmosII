package vendor.fieldstypes;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import vendor.ann.Relation;

public class RelationField extends AbstractField {

	public <T> RelationField(Field attribute, Relation an, Class<T> dtoClass, String tableAlias) {
		super(attribute, "", dtoClass, tableAlias);
		
		Type type = ((ParameterizedType) attribute.getGenericType()).getActualTypeArguments()[0];
	}

}
