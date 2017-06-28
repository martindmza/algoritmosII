package vendor.fieldstypes;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import vendor.ann.Column;
import vendor.ann.Id;
import vendor.ann.Relation;

public class FieldsTypesFactory {
	
	public static <T> AbstractField buildfieldType(Field f, Class<T> dtoClass) {
		return buildfieldType(f,dtoClass,"");
	}

	public static <T> AbstractField buildfieldType(Field f, Class<T> dtoClass, String tableAlias) {
		if (f.getAnnotation(Column.class) != null) {
			Column column = f.getAnnotation(Column.class);
			String type = f.getType().getSimpleName();		
			if (	type.equals("Integer") 	|| 
					type.equals("int") 		|| 
					type.equals("String") 	||
					type.equals("Boolean") 	|| 
					type.equals("boolean")	|| 
					type.equals("Date") 
			) {
				return new PrimitiveField(f, column.name(), dtoClass, tableAlias);
			} else {
				return new ManyToOneField(f, column, dtoClass, tableAlias);
			}
		} else if (f.getAnnotation(Relation.class) != null) {
			Class<?> type = (Class<?>) (((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0]);
			Relation relationAttribute = f.getAnnotation(Relation.class);
			String mappedBy = relationAttribute.mappedBy();
			AbstractField mappedByIdField = getAttribute(type,mappedBy);
			return new OneToManyField(f, dtoClass, tableAlias, type, mappedByIdField);
		}
		return null;
	}

	public static <T> PrimitiveField getIdAttribute(Class<T> dtoClass, String tableAlias) {
		for (Field f : dtoClass.getDeclaredFields()) {
			Id idAttribute = f.getAnnotation(Id.class);
			if (idAttribute != null) {
				Column column = f.getAnnotation(Column.class);
				return new PrimitiveField(f, column.name(), dtoClass, tableAlias);
			}
		}
		return null;
	}
	
	public static <T> PrimitiveField getIdAttribute(Class<T> dtoClass) {
		for (Field f : dtoClass.getDeclaredFields()) {
			Id idAttribute = f.getAnnotation(Id.class);
			if (idAttribute != null) {
				Column column = f.getAnnotation(Column.class);
				return new PrimitiveField(f, column.name(), dtoClass);
			}
		}
		return null;
	}
	
	
	public static <T> AbstractField getAttribute (Class<T> dtoClass, String attributeName) {
		for (Field f : dtoClass.getDeclaredFields()) {
			if (f.getName().equals(attributeName)) {
				return buildfieldType(f, dtoClass, null);
			}
		}
		return null;
	}

	public static <T> PrimitiveField buildPrimitiveField(Field f, Class<T> dtoClass, String tableAlias) {
		if (f.getAnnotation(Column.class) != null) {
			Column column = f.getAnnotation(Column.class);
			return new PrimitiveField(f, column.name(), dtoClass, tableAlias);
		}
		return null;
	}
}
