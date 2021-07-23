package com.centit.dde.java;

import cn.hutool.core.util.StrUtil;
import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@SuppressWarnings("all")
public class DynamicEngine {

	private static final Logger LOG = LoggerFactory.getLogger(DynamicEngine.class);

	private static DynamicEngine ourInstance;

	public static DynamicEngine getInstance() {
		return ourInstance;
	}

	public static void close() throws IOException {
		ourInstance.parentClassLoader.close();
	}

	private URLClassLoader parentClassLoader;

	private String classpath;

	private DynamicEngine(URLClassLoader classLoader) {
		this.parentClassLoader = classLoader;
		this.buildClassPath();
	}

	private void buildClassPath() {
		this.classpath = null;
		Set<String> classPathSet = new HashSet<>();

		for (URL url : this.parentClassLoader.getURLs()) {
			try {
				String p = new File(url.toURI()).getAbsolutePath();
				classPathSet.add(p);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}

		URLClassLoader classLoader = (URLClassLoader) this.getClass().getClassLoader();

		for (URL url : classLoader.getURLs()) {
			try {
				String p = new File(url.toURI()).getAbsolutePath();
				classPathSet.add(p);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}

		this.classpath = Joiner.on(File.pathSeparator).join(classPathSet);
	}

	public static void flush(URLClassLoader classLoader) throws Exception {
		ourInstance = new DynamicEngine(classLoader);
	}

	public Class<?> javaCodeToClass(String fullClassName, String javaCode) throws IOException, Exception {
		Class<?> clazz = null;
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		ClassFileManager fileManager = new ClassFileManager(compiler.getStandardFileManager(diagnostics, null, null));
		List<JavaFileObject> jfiles = new ArrayList<JavaFileObject>();
		jfiles.add(new CharSequenceJavaFileObject(fullClassName, javaCode));

		List<String> options = new ArrayList<String>();
		options.add("-encoding");
		options.add("UTF-8");
		options.add("-classpath");
		options.add(this.classpath);
		options.add("-parameters");

		JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, options, null, jfiles);
		boolean success = task.call();
		if (success) {
			JavaClassObject jco = fileManager.getMainJavaClassObject();
			DynamicClassLoader dynamicClassLoader = new DynamicClassLoader(this.parentClassLoader);
			try {
				List<JavaClassObject> innerClassJcos = fileManager.getInnerClassJavaClassObject();
				if (innerClassJcos != null && innerClassJcos.size() > 0) {
					for (JavaClassObject inner : innerClassJcos) {
						String name = inner.getName();
						name = name.substring(1, name.length() - 6).replace("/", ".");
						dynamicClassLoader.loadClass(name, inner);
					}
				}
				clazz = dynamicClassLoader.loadClass(fullClassName, jco);
			} catch (Exception e) {
				e.printStackTrace();
				LOG.error(e.getMessage(), e);
			} catch (Error e) {
				LOG.error(e.getMessage(), e);
				throw new Exception(e.toString());
			} finally {
				if (dynamicClassLoader != null) {
					dynamicClassLoader.close();
				}
			}
		} else {
			StringBuilder error = new StringBuilder();
			for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics()) {
				error.append(compilePrint(diagnostic));
			}
			throw new Exception(error.toString());
		}

		return clazz;
	}

	public byte[] javaCode2Bytes(String fullClassName, String javaCode) throws IOException, Exception {
		Class<?> clazz = null;
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		ClassFileManager fileManager = new ClassFileManager(compiler.getStandardFileManager(diagnostics, null, null));
		List<JavaFileObject> jfiles = new ArrayList<JavaFileObject>();
		jfiles.add(new CharSequenceJavaFileObject(fullClassName, javaCode));

		List<String> options = new ArrayList<String>();

		options.add("-source");
		options.add("1.6");
		options.add("-target");
		options.add("1.6");
		options.add("-encoding");
		options.add("UTF-8");
		options.add("-classpath");
		options.add(this.classpath);
		options.add("-parameters");

		JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, options, null, jfiles);
		boolean success = task.call();
		if (success) {
			JavaClassObject jco = fileManager.getMainJavaClassObject();
			DynamicClassLoader dynamicClassLoader = new DynamicClassLoader(this.parentClassLoader);
			try {
				List<JavaClassObject> innerClassJcos = fileManager.getInnerClassJavaClassObject();
				if (innerClassJcos != null && innerClassJcos.size() > 0) {
					for (JavaClassObject inner : innerClassJcos) {
						String name = inner.getName();
						name = name.substring(1, name.length() - 6).replace("/", ".");
						dynamicClassLoader.loadClass(name, inner);
					}
				}
				return jco.getBytes();
			} catch (Exception e) {
				e.printStackTrace();
				LOG.error(e.getMessage(), e);
			} catch (Error e) {
				LOG.error(e.getMessage(), e);
				throw new Exception(e.toString());
			} finally {
				if (dynamicClassLoader != null) {
					dynamicClassLoader.close();
				}
			}
		} else {
			StringBuilder error = new StringBuilder();
			for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics()) {
				error.append(compilePrint(diagnostic));
			}
			throw new Exception(error.toString());
		}

		return null;
	}

	public <T> T javaCodeToObject(String fullClassName, String javaCode) throws IllegalAccessException, InstantiationException, IOException, Exception {
		return (T) javaCodeToClass(fullClassName, javaCode).newInstance();
	}

	private StringBuilder compilePrint(Diagnostic<?> diagnostic) {
		StringBuilder res = new StringBuilder();
		res.append("Code:[" + diagnostic.getCode() + "]\n");
		res.append("Kind:[" + diagnostic.getKind() + "]\n");
		res.append("Position:[" + diagnostic.getPosition() + "]\n");
		res.append("Start Position:[" + diagnostic.getStartPosition() + "]\n");
		res.append("End Position:[" + diagnostic.getEndPosition() + "]\n");
		res.append("Source:[" + diagnostic.getSource() + "]\n");
		res.append("Message:[" + diagnostic.getMessage(null) + "]\n");
		res.append("LineNumber:[" + diagnostic.getLineNumber() + "]\n");
		res.append("ColumnNumber:[" + diagnostic.getColumnNumber() + "]\n");
		return res;
	}

	public <T> T javaCodeToObject(String content) throws IllegalAccessException, InstantiationException, IOException, Exception {
		String className = JavaSourceUtil.findFullName(content);
		if (StrUtil.isBlank(className)) {
			throw new ClassFormatError("can find class name ,please define it ! use javaCodeToObject(String fullClassName, String javaCode)");
		}
		return (T) this.javaCodeToObject(className, content);
	}

	public Class<?> javaCodeToClass(String content) throws IOException, Exception {
		String className = JavaSourceUtil.findFullName(content);
		if (StrUtil.isBlank(className)) {
			throw new ClassFormatError("can find class name ,please define it ! use javaCodeToObject(String fullClassName, String javaCode)");
		}
		return this.javaCodeToClass(className, content);
	}

	public URLClassLoader getParentClassLoader() {
		return parentClassLoader;
	}

}
