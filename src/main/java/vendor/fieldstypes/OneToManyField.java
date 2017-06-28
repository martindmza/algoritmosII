package vendor.fieldstypes;

import java.lang.reflect.Field;

public class OneToManyField extends AbstractField {
	
	protected Class<?> relatedClass;
	protected AbstractField mappedByIdField;

	public <T> OneToManyField(Field attribute, Class<T> dtoClass, String tableAlias, Class<?> relatedClass, AbstractField mappedByIdField) {
		super(attribute, "", dtoClass, tableAlias);
		this.relatedClass = relatedClass;
		this.mappedByIdField = mappedByIdField;
	}
}
