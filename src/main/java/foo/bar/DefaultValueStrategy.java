package foo.bar;

import java.lang.reflect.Method;

public interface DefaultValueStrategy<T> {
	
	public T createValue(Method method);

}
