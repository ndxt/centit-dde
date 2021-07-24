package com.centit.dde.java;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaSourceUtil {

	private static final Pattern PAT = Pattern.compile("public\\s+?class\\s+?.*?");

	public static void main(String[] args) throws IOException {
		System.out.println(findClassName("public class Test implements Execute{"));
	}

	public static String findPackage(String sourceCode) throws IOException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new StringReader(sourceCode));
			String temp = null;
			while ((temp = br.readLine()) != null) {
				if (StringUtils.isBlank(temp)) {
					continue;
				}
				if (temp.trim().startsWith("package ")) {
					temp = temp.split("[\\s+?;]")[1];
					return temp;
				} else {
					return null;
				}
			}
			return null;
		} finally {
			if (br != null) {
				br.close();
			}
		}
	}

	public static String findFullName(String sourceCode) throws IOException {
		String pack = findPackage(sourceCode);
		String className = findClassName(sourceCode);
		if (className == null) {
			return null;
		}
		if (pack == null) {
			return className;
		}

		return pack + "." + className;
	}

	/**
	 * 根据一个类返回类名称
	 *
	 * @param sourceCode
	 * @return 类名称
	 * @throws IOException
	 */
	public static String findClassName(String sourceCode) {

		try (BufferedReader br = new BufferedReader(new StringReader(sourceCode))) {
			String temp = null;

			while ((temp = br.readLine()) != null) {
				if (StringUtils.isBlank(temp)) {
					continue;
				}
				Matcher matcher = PAT.matcher(temp);

				if (matcher.find()) {
					temp = temp.split("[\\s+?;]")[2];
					return temp;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static <T extends Annotation> T getAnnotationDeep(Method method, Class<T> annotationClass) {
        T t = method.getAnnotation(annotationClass);
        if (t != null)
            return t;
        Class klass = method.getDeclaringClass().getSuperclass();
        while (klass != null && klass != Object.class) {
            try {
                for (Method m : klass.getMethods()) {
                    if (m.getName().equals(method.getName())) {
                        Class[] mParameters = m.getParameterTypes();
                        Class[] methodParameters = method.getParameterTypes();
                        if (mParameters.length != methodParameters.length)
                            continue;
                        boolean match = true;
                        for (int i = 0; i < mParameters.length; i++) {
                            if (!mParameters[i].isAssignableFrom(methodParameters[i])) {
                                match = false;
                                break;
                            }
                        }
                        if (match) {
                            t = m.getAnnotation(annotationClass);
                            if (t != null)
                                return t;
                        }
                    }
                }
            }
            catch (Exception e) {
                break;
            }
            klass = klass.getSuperclass();
        }
        for (Class klass2 : method.getDeclaringClass().getInterfaces()) {
            try {
                Method tmp = klass2.getMethod(method.getName(), method.getParameterTypes());
                t = tmp.getAnnotation(annotationClass);
                if (t != null)
                    return t;
            }
            catch (Exception e) {}
        }
        return null;
    }

    public static <T extends Annotation> T getAnnotationDeep(Class<?> type, Class<T> annotationClass) {
        T t = type.getAnnotation(annotationClass);
        if (t != null)
            return t;
        Class<?> klass = type.getSuperclass();
        while (klass != null && klass != Object.class) {
            try {
                t = klass.getAnnotation(annotationClass);
                if (t != null)
                    return t;
            }
            catch (Exception e) {
                break;
            }
            klass = klass.getSuperclass();
        }
        for (Class<?> klass2 : type.getInterfaces()) {
            try {
                t = klass2.getAnnotation(annotationClass);
                if (t != null)
                    return t;
            }
            catch (Exception e) {}
        }
        return null;
    }
}

