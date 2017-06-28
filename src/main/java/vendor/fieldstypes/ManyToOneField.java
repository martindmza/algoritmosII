package vendor.fieldstypes;

import java.lang.reflect.Field;

import vendor.ann.Column;

public class ManyToOneField extends AbstractField {

	public <T> ManyToOneField(Field attribute, Column manyToOneColumn, Class<T> dtoClass, String tableAlias) {		
		super(attribute, manyToOneColumn.name(), dtoClass, tableAlias);
		
		Column column = attribute.getAnnotation(Column.class);
		if (column.name().equals(Column.NONE)) {
			this.columnName = "id_" + attribute.getName();
		}
	}
}
