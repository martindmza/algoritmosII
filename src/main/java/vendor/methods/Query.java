package vendor.methods;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.cglib.proxy.Enhancer;
import vendor.JoinedElement;
import vendor.ann.Column;
import vendor.ann.ManyToOne;
import vendor.ann.OneToMany;
import vendor.ann.Relation;
import vendor.ann.Table;
import vendor.fieldstypes.AbstractField;
import vendor.fieldstypes.AbstractFieldsMapping;
import vendor.fieldstypes.FieldsTypesFactory;
import vendor.fieldstypes.ManyToOneField;
import vendor.fieldstypes.ObjectMappedRow;
import vendor.fieldstypes.OneToManyField;
import vendor.fieldstypes.PrimitiveField;

public class Query extends Utn {

	static boolean forceJoin = false;
	static List<String> prossesedClasses = new ArrayList<>();

	// Retorna: el SQL correspondiente a la clase dtoClass acotado por xql
	protected static <T> String _query(Class<T> dtoClass, String xql) {
		Table table = dtoClass.getAnnotation(Table.class);
		String tableName = table.name();
		prossesedClasses.add(dtoClass.getSimpleName());

		String tableFields = "";
		String joins = "";
		for (Field f : dtoClass.getDeclaredFields()) {
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
					String columnName = column.name();
					if (columnName.equals(Column.NONE)) {
						PrimitiveField primitiveField = (PrimitiveField) FieldsTypesFactory.buildfieldType(f, dtoClass);
						columnName = primitiveField.getColumnName();
					} 
					tableFields += getAlias(dtoClass) + "." + columnName + " AS '" + getAlias(dtoClass) + "."
							+ columnName + "' ,";
				} else {
					// join
					if (!forceJoin && column.fetchType() == ManyToOne.LAZY) {
						//continue;
					}
					
					String targetAlias = getAlias(f.getType());
					PrimitiveField targetId = FieldsTypesFactory.getIdAttribute(f.getType(), targetAlias);
					ManyToOneField manyToOneField = (ManyToOneField) FieldsTypesFactory.buildfieldType(f, dtoClass,
							getAlias(dtoClass));

					JoinedElement joinedElement = join(dtoClass, f.getType(), targetAlias,
							manyToOneField.getColumnName(), targetId.getColumnName(), manyToOneField, column.fetchType());
					if (joinedElement != null) {
						joins += " " + joinedElement.joinPart;
						tableFields += joinedElement.fieldsPart;
					}
				}			
			} else if (f.getAnnotation(Relation.class) != null) {
				Relation relationColumn = f.getAnnotation(Relation.class);
				if (relationColumn.fetchType() == OneToMany.LAZY) {
					continue;
				}
				
				Class<?> type = (Class<?>) (((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0]);
				
				AbstractField attField = FieldsTypesFactory.getAttribute(type, relationColumn.mappedBy());

				String targetAlias = getAlias(type);
				PrimitiveField idColumn = FieldsTypesFactory.getIdAttribute(dtoClass, targetAlias);
				OneToManyField oneToManyField = (OneToManyField) FieldsTypesFactory.buildfieldType(f, dtoClass,
						getAlias(dtoClass));

				JoinedElement joinedElement = join(dtoClass, type, targetAlias,
						idColumn.getColumnName(), attField.getColumnName(), oneToManyField, relationColumn.fetchType());
				
				if (joinedElement != null) {
					joins += " " + joinedElement.joinPart;
					tableFields += joinedElement.fieldsPart;
				}
			}
		}
		tableFields = tableFields.substring(0, tableFields.length() - 1);
		String where = "";
		if (!xql.trim().equals("") && xql != null) {
			where = buildWhere(xql, dtoClass);
		}

		PrimitiveField idField = FieldsTypesFactory.getIdAttribute(dtoClass);
		String sql = "SELECT " + tableFields + " FROM " + tableName + " AS " + getAlias(dtoClass) + " " + joins + " "
				+ where + " ORDER BY " + idField.getColumnName() + ";";
		//System.out.println(sql);
		prossesedClasses.clear();
		
		return sql;
	}
	
	// Invoca a: _query para obtener el SQL que se debe ejecutar
		// Retorna: una lista de objetos de tipo T
		public static <T> List<T> query(Connection con, Class<T> dtoClass, String xql, Object... args) {
			String sql = _query(dtoClass, xql);
			addAbstractFields(dtoClass);

			HashMap<Integer, T> result = new HashMap<Integer, T>();
			PreparedStatement pstm = null;
			ResultSet rs = null;
			try {
				pstm = con.prepareStatement(sql);
				int count = 1;
				for (Object arg : args) {
					if (arg instanceof Integer) {
						pstm.setInt(count, (int) arg);
					} else if (arg instanceof Boolean) {
						pstm.setBoolean(count, (boolean) arg);
					} else if (arg instanceof Date) {
						pstm.setDate(count, (Date) arg);
					} else {
						pstm.setString(count, arg.toString());
					}
					count++;
				}

				rs = pstm.executeQuery();
				// System.out.println(((JDBC4PreparedStatement) pstm).asSql());

				PrimitiveField idAttribute = FieldsTypesFactory.getIdAttribute(dtoClass, getAlias(dtoClass));
				Constructor<T> ctor = dtoClass.getConstructor();
				while (rs.next()) {
					T mainEntity = (T) ctor.newInstance();
					int id = (int) idAttribute.getParamForSetter(rs);
					HashMap<String, ObjectMappedRow<?>> entities = new HashMap<>();
					for (AbstractFieldsMapping<?, ?> mapping : mappedFields) {
						if (mapping.getClassType().getSimpleName().equals(dtoClass.getSimpleName())) {
							if (result.containsKey(id)) {
								continue;
							}

							for (AbstractField myField : mapping.getPrimitiveFields()) {
								if (myField.getSetter() != null) {
									myField.getSetter().invoke(mainEntity, myField.getParamForSetter(rs));
								}
							}
							entities.put(dtoClass.getSimpleName(), new ObjectMappedRow<Object>(mainEntity, mapping));
							continue;
						}

						if (mapping.getFetchType() != Column.LAZY) {						
							Constructor<?> joinCtor = mapping.getClassType().getConstructor();
							Object joinedEntity = joinCtor.newInstance();
							
							ObjectMappedRow mappedRow = new ObjectMappedRow<Object>(joinedEntity, mapping);
							
							for (AbstractField myField : mapping.getPrimitiveFields()) {
								if (myField.getSetter() != null) {
									myField.getSetter().invoke(joinedEntity, myField.getParamForSetter(rs));
								}
							}
							entities.put(mapping.getClassType().getSimpleName(),mappedRow);	
						}
					}

					for (HashMap.Entry<String, ObjectMappedRow<?>> entity : entities.entrySet()) {
						if (entity.getValue().getAbstractField().getContainerClass() != null) {
							ObjectMappedRow<?> containerEntity = entities.get(entity.getValue().getAbstractField().getContainerClass().getSimpleName());
							if (containerEntity != null) {
								Method setter = entity.getValue().getAbstractField().getContainerClassField().getSetter();
		 						setter.invoke(containerEntity.getEntity(), entity.getValue().getEntity());
							}
						}
					}
					result.put(id, mainEntity);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} finally {
				mappedFields.clear();
				prossesedClasses.clear();
			}

			List<T> resultList = new ArrayList<T>();
			for (HashMap.Entry<Integer, T> resultEntity : result.entrySet()) {
				@SuppressWarnings("unchecked")
				T o = (T) Enhancer.create(dtoClass,getMethodInterceptor(resultEntity.getValue()));
				resultList.add(o);
			}
			return resultList;
		}
		
		
		protected static <T, R> JoinedElement join(Class<T> dtoClass, Class<R> dtoTargetClass, String targetTableAlias,
				String originId, String targetId, AbstractField containerClassField, int fetchType) {
			String alias = getAlias(dtoClass);
			addAbstractFields(dtoTargetClass, dtoClass, alias, containerClassField, fetchType);
			
			if (prossesedClasses.contains(dtoTargetClass.getSimpleName())) {
				return null;
			}
			prossesedClasses.add(dtoTargetClass.getSimpleName());

			Table targetTable = dtoTargetClass.getAnnotation(Table.class);
			String targetTableName = targetTable.name();

			String join = " LEFT JOIN " + targetTableName + " AS " + targetTableAlias + " ON " + alias + "." + originId + " = "
					+ targetTableAlias + "." + targetId + " ";

			String tableFields = "";
			for (Field f : dtoTargetClass.getDeclaredFields()) {
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
						String columnName = column.name();
						if (columnName.equals(Column.NONE)) {
							PrimitiveField primitiveField = (PrimitiveField) FieldsTypesFactory.buildfieldType(f, dtoClass);
							columnName = primitiveField.getColumnName();
						} 
						tableFields += getAlias(dtoTargetClass) + "." + columnName + " AS '" + getAlias(dtoTargetClass) + "."
								+ columnName + "' ,";
					} else {
						if (! Query.forceJoin && column.fetchType() == ManyToOne.LAZY) {
							continue;
						}
						PrimitiveField subJoinTargetId = FieldsTypesFactory.getIdAttribute(f.getType(), alias);
						String targetSubJoinAlias = getAlias(f.getType());
						ManyToOneField manyToOneField = (ManyToOneField) FieldsTypesFactory.buildfieldType(f, dtoTargetClass,
								getAlias(dtoClass));

						JoinedElement joinedElement = join(dtoTargetClass, f.getType(), targetSubJoinAlias,
								manyToOneField.getColumnName(), subJoinTargetId.getColumnName(), manyToOneField, column.fetchType());
						if (joinedElement != null) {
							join += " " + joinedElement.joinPart;
							tableFields += joinedElement.fieldsPart;
						}
					}
				}
			}

			return new JoinedElement(join, tableFields);
		}
}
