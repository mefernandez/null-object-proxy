package foo.bar;

import java.lang.reflect.Method;

public class MethodNameValueStrategy implements DefaultValueStrategy<String> {

	public String createValue(Method method) {
		return method.getName();
	}

}
