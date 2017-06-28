package vendor.interceptors;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import main.App;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import vendor.ann.Column;
import vendor.ann.ManyToOne;
import vendor.ann.OneToMany;
import vendor.ann.Relation;
import vendor.fieldstypes.AbstractField;
import vendor.fieldstypes.FieldsTypesFactory;
import vendor.fieldstypes.PrimitiveField;
import vendor.methods.Find;
import vendor.methods.Query;
import vendor.methods.Utn;

public class GenericMethodInterceptor implements MethodInterceptor
{
	private Object target;
	private static boolean locked = false;

	public GenericMethodInterceptor(Object target)
	{
		this.target=target;
	}
	

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable
	{
		if (locked) {
			return proxy.invoke(target,args);
		}
		locked = true;
		
		try {
			String methodName = method.getName();
			String prefix = methodName.substring(0, 3);
			if (prefix.equals("get")) {
				String suffix = methodName.substring(3, methodName.length());
				String attrName = suffix.substring(0, 1).toLowerCase() + suffix.substring(1);
				AbstractField field = FieldsTypesFactory.getAttribute(target.getClass(),attrName);			
				if (field != null && ! (field instanceof PrimitiveField)) {
					if (field.getAttribute().getAnnotation(Relation.class) != null) {
						Relation relationAnn = field.getAttribute().getAnnotation(Relation.class);
						Class<?> type = (Class<?>) (((ParameterizedType) field.getAttribute().getGenericType()).getActualTypeArguments()[0]);
						
						AbstractField attField = FieldsTypesFactory.getAttribute(type, relationAnn.mappedBy());

						PrimitiveField idField = FieldsTypesFactory.getIdAttribute(target.getClass());
						int id = (int) idField.getGetter().invoke(obj);
						String xql = "$" + attField.getAttribute().getName() + "." + idField.getAttribute().getName() + " = ? ";
						
						List<Object> result = (List<Object>) Query.query(App.getConection(), type, xql, id);
						return result;
						
					} else if (field.getAttribute().getAnnotation(Column.class) != null) {
						PrimitiveField idField = FieldsTypesFactory.getIdAttribute(target.getClass());
						if (idField != null && idField.getGetter().invoke(obj) != null ) {
							int objectId = (int) idField.getGetter().invoke(obj);
							String sql = 	"SELECT " + field.getColumnName() +
											" FROM " + field.getTableName() + " WHERE " + idField.getColumnName() + " = " + objectId + ";";
							PreparedStatement pstm = App.getConection().prepareStatement(sql); 
							ResultSet rs = pstm.executeQuery();
							while (rs.next()) {
								int fkId = rs.getInt(field.getColumnName());
								
								locked = false;
								Object zz = Find.find(App.getConection(), field.getAttribute().getType(), fkId);
								return zz;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			locked = false;
		}
		return proxy.invoke(target,args);
	}

}

