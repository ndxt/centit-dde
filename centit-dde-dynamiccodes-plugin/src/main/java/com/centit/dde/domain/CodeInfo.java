package com.centit.dde.domain;

import com.centit.dde.java.DynamicEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;

public class CodeInfo {

	private static final Logger LOG = LoggerFactory.getLogger(CodeInfo.class);

	private Class<?> classz;

	private Object JavaObject;

	private Map<String, ExecuteMethod> executeMethods = new HashMap<>();

	private ExecuteMethod defaultMethod;

	private ClassLoader classLoader;

	private boolean single = true;


	public boolean classLoaderChanged() {
		return this.classLoader != DynamicEngine.getInstance().getParentClassLoader();
	}


	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	public Class<?> getClassz() {
		return classz;
	}

	public void setClassz(Class<?> classz) {
		this.classz = classz;
	}

	public Object getJavaObject() {
		return JavaObject;
	}

	public void setJavaObject(Object javaObject) {
		JavaObject = javaObject;
	}

	public Collection<ExecuteMethod> getExecuteMethods() {
		return executeMethods.values();
	}

	public void setDefaultMethod(Method method, Set<String> methodTypeSet, boolean rpc, boolean restful) {
		if (defaultMethod == null) {
			this.defaultMethod = new ExecuteMethod(method, methodTypeSet, rpc, restful);
		} else {
			LOG.warn(classz.getName() + " has being more than one @DefaultExecute annotation method : " + defaultMethod.getName() + " so skip method : " + method.getName());
		}
		addMethod(defaultMethod);
	}

	public boolean isSingle() {
		return single;
	}

	public void setSingle(boolean single) {
		this.single = single;
	}

	public void addMethod(ExecuteMethod method) {
		this.executeMethods.put(method.getName(), method);
	}

	public void addMethod(Method method, Set<String> methodTypeSet, boolean rpc, boolean restful) {
		addMethod(new ExecuteMethod(method, methodTypeSet, rpc, restful));
	}

	public ExecuteMethod getDefaultMethod() {
		if (defaultMethod == null) {
			if (executeMethods.size() == 0) {
				throw new RuntimeException(classz.getName() + " not have any @Execute or @DefaultExecute annotation you must set one ");
			}
			defaultMethod = executeMethods.entrySet().iterator().next().getValue();
			LOG.warn(defaultMethod.getMethod().getDeclaringClass().getName() + " none @DefaultExecute annotation method : so set the firest method to DefaultExecute function:"
					+ defaultMethod.getName());
		}
		return defaultMethod;
	}

	/**
	 * 閫氳繃鏂规硶鍚嶇О鑾峰緱鏂规硶
	 *
	 * @param methodName
	 * @return
	 */
	public ExecuteMethod getExecuteMethod(String methodName) {
		return executeMethods.get(methodName);
	}

	/**
	 * 鎵ц鏂规硶鐨勬娊璞＄被
	 *
	 * @author Ansj
	 *
	 */
	public class ExecuteMethod {
		private Method method;
		private Set<String> methodTypeSet = new HashSet<>();
		private boolean rpc;
		private boolean restful;

		public ExecuteMethod(Method method, Set<String> methodTypeSet, boolean rpc, boolean restful) {
			super();
			this.method = method;
			this.methodTypeSet = methodTypeSet;
			this.rpc = rpc;
			this.restful = restful;
		}

		public Method getMethod() {
			return method;
		}

		public void setMethod(Method method) {
			this.method = method;
		}

		public Set<String> getMethodTypeSet() {
			return methodTypeSet;
		}

		public void setMethodTypeSet(Set<String> methodTypeSet) {
			this.methodTypeSet = methodTypeSet;
		}

		public boolean isRpc() {
			return rpc;
		}

		public void setRpc(boolean rpc) {
			this.rpc = rpc;
		}

		public boolean isRestful() {
			return restful;
		}

		public void setRestful(boolean restful) {
			this.restful = restful;
		}

		public String getName() {
			return this.method.getName();
		}

	}

}

