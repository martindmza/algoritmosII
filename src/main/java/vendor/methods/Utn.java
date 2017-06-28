package vendor.methods;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ProgressMonitorInputStream;

import net.sf.cglib.proxy.MethodInterceptor;
import vendor.JoinedElement;
import vendor.ann.Column;
import vendor.ann.ManyToOne;
import vendor.ann.Table;
import vendor.exceptions.EntityNotFoundException;
import vendor.fieldstypes.AbstractField;
import vendor.fieldstypes.AbstractFieldsMapping;
import vendor.fieldstypes.FieldsTypesFactory;
import vendor.fieldstypes.ManyToOneField;
import vendor.fieldstypes.PrimitiveField;
import vendor.interceptors.GenericMethodInterceptor;

public class Utn {
	
	protected static List<AbstractFieldsMapping<?, ?>> mappedFields = new ArrayList<AbstractFieldsMapping<?, ?>>();

	protected static <T> String buildWhere(String xql, Class<T> dtoClass) {
		List<String> vars = new ArrayList<String>();
		boolean write = false;
		String currentString = "";
		for (char ch: xql.toCharArray()) {
			if (ch == '$') {
				write = true;
				continue;
			}
			if ((ch == ' ' || ch == '=') && write) {
				write = false;
				vars.add(currentString);
				currentString = "";
			}
			
			if (write) {
				currentString += ch; 
			}
		}

		for (String var : vars) {
			String[] splited = var.split("\\.");
			if (splited.length == 1) {
				AbstractField field = FieldsTypesFactory.getAttribute(dtoClass, var);
				xql = xql.replace("$" + var, getAlias(dtoClass) + "." + field.getColumnName());
				continue;
			}
			
			Class<?> dtoFilterClass = dtoClass;
			for (int i = 0; i < splited.length ;i++) {
				AbstractField field = FieldsTypesFactory.getAttribute(dtoFilterClass, splited[i]);
				dtoFilterClass = field.getAttribute().getType();
				if (i + 1 == splited.length) {
					xql = xql.replace("$" + var, getAlias(field.getDtoContainerClass()) + "." + field.getColumnName());
				}
			}
		}
		
		return " WHERE " + xql;
	}

	protected static <T> String getAlias(Class<T> dtoClass) {
		Table table = dtoClass.getAnnotation(Table.class);
		String tableName = table.name();
		String alias = "";
		if (table.alias() == null || alias.trim().equals("")) {
			alias = tableName;
		}

		return alias;
	}

	

	protected static <T> void addAbstractFields(Class<T> dtoClass) {
		addAbstractFields(dtoClass, null, null, null, Column.EAGER);
	}

	protected static <T, C> void addAbstractFields(Class<T> dtoClass, Class<C> containerClass, String containerAlias,
			AbstractField containerClassField, int fetchType) {
		if (mappedFields.contains(dtoClass)) {
			return;
		}
		String alias = getAlias(dtoClass);
		List<PrimitiveField> primitiveFields = new ArrayList<PrimitiveField>();
		for (Field f : dtoClass.getDeclaredFields()) {
			AbstractField field = FieldsTypesFactory.buildfieldType(f, dtoClass, alias);
			if (field != null && field instanceof PrimitiveField) {
				primitiveFields.add((PrimitiveField) field);
			}
		}
		
		mappedFields.add(new AbstractFieldsMapping<T, C>(dtoClass, containerClass, primitiveFields, containerClassField,
				containerAlias, fetchType));
	}
	
	protected static MethodInterceptor getMethodInterceptor(Object obj)
	{
		return new GenericMethodInterceptor(obj);
	}
	
	
	
	
	public static <T> List<T> findAll(Connection con, Class<T> dtoClass)
	{
		return FindAll.findAll(con, dtoClass);
	}
	
	public static <T> T find(Connection con, Class<T> dtoClass, Object id) throws EntityNotFoundException {
		return Find.find(con, dtoClass, id);
	}
	
	public static <T> List<T> query(Connection con, Class<T> dtoClass, String xql, Object... args) {
		return Query.query(con, dtoClass, xql, args);
	}
}
