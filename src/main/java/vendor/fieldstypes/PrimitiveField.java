package vendor.fieldstypes;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

import vendor.ann.Column;

public class PrimitiveField extends AbstractField {

	public <T> PrimitiveField(Field attribute, String columnName, Class<T> dtoClass, String tableAlias) {
		super(attribute, columnName, dtoClass, tableAlias);	
		Column column = attribute.getAnnotation(Column.class);
		if (column.name().equals(Column.NONE)) {
			this.columnName = attribute.getName();
		}
	}
	
	public <T> PrimitiveField(Field attribute, String columnName, Class<T> dtoClass) {
		super(attribute, columnName, dtoClass,"");
		Column column = attribute.getAnnotation(Column.class);
		if (column.name().equals(Column.NONE)) {
			this.columnName = attribute.getName();
		}
	}

	@Override
	public Object getParamForSetter(ResultSet rs) throws SQLException {
		String type = this.attribute.getType().getSimpleName();		
		if (type.equals("Integer") || type.equals("int")) {
			return rs.getInt(this.getColumnAliasName());
		} else if (type.equals("String")) {
			return rs.getString(this.getColumnAliasName());
		} else if (type.equals("Boolean") || type.equals("boolean")) {
			return rs.getBoolean(this.getColumnAliasName());
		} else if (type.equals("Date")) {
			return rs.getDate(this.getColumnAliasName());
		} else {
			return null;
		}
	}

	private String getColumnAliasName() {
		return this.tableAlias + "." + this.columnName;
	}

}
