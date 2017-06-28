package vendor.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Relation
{
	int LAZY=1;
	int EAGER=2;
	
	String mappedBy();
	int fetchType() default EAGER; 
	//Class<?> type();
	//String name();
}
