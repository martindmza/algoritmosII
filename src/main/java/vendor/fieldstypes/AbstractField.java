package vendor.fieldstypes;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;

import vendor.ann.Column;
import vendor.ann.Id;
import vendor.ann.Table;

public abstract class AbstractField {

	protected Field attribute;
	protected String columnName;
	protected Method setter;
	protected Method getter;
	protected String tableName;
	protected String tableAlias;
	protected Class<?> dtoContainerClass;
	
	protected boolean isId;

	public <T> AbstractField(Field attribute, String columnName, Class<T> dtoClass, String tableAlias) {
		this.attribute = attribute;
		this.columnName = columnName;
		this.tableName = dtoClass.getAnnotation(Table.class).name();
		this.tableAlias = tableAlias;
		this.dtoContainerClass = dtoClass;
		
		if (columnName.equals(Column.NONE)) {
			this.columnName = attribute.getName();
		}

		Method methods[] = dtoClass.getDeclaredMethods();

		// obtengo el metodo setter de este atributo
		String setterName = "set" + attribute.getName().substring(0, 1).toUpperCase()
				+ attribute.getName().substring(1);
		for (Method method : methods) {
			if (method.getName().equals(setterName)) {
				this.setter = method;
				break;
			}
		}

		// obtengo el metodo getter de este atributo
		String getterName = "get" + attribute.getName().substring(0, 1).toUpperCase()
				+ attribute.getName().substring(1);
		for (Method method : methods) {
			if (method.getName().equals(getterName)) {
				this.getter = method;
				break;
			}
		}

		Id idAttribute = attribute.getAnnotation(Id.class);
		if (idAttribute == null) {
			this.isId = false;
		}
	}

	public boolean isId() {
		return isId;
	}

	public void setId(boolean isId) {
		this.isId = isId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableAlias() {
		return tableAlias;
	}

	public void setTableAlias(String tableAlias) {
		this.tableAlias = tableAlias;
	}

	public Field getAttribute() {
		return attribute;
	}

	public void setAttribute(Field attribute) {
		this.attribute = attribute;
	}

	public String getColumnName() {
		return this.columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Method getSetter() {
		return setter;
	}

	public void setSetter(Method setter) {
		this.setter = setter;
	}

	public Method getGetter() {
		return getter;
	}

	public void setGetter(Method getter) {
		this.getter = getter;
	}

	public Class<?> getDtoContainerClass() {
		return dtoContainerClass;
	}

	public Object getParamForSetter(ResultSet rs) throws SQLException {
		return null;
	}

}
