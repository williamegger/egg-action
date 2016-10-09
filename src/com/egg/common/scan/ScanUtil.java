package com.egg.common.scan;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.egg.common.log.LogKit;

public class ScanUtil {

	/**
	 * 按超类扫描，得到该超类的子类（公有、非抽象、非接口）
	 */
	public static List<Class<?>> scanBySuperClass(String packageName, final Class<?> superClass) throws IOException {
		return scan(packageName, new ClassFilter() {

			@Override
			public boolean filter(Class<?> clazz) {
				return (verifyModifier(clazz)
						&& !superClass.equals(clazz)
						&& superClass.isAssignableFrom(clazz));
			}
		});
	}

	/**
	 * 按接口扫描，得到该接口的实现类（公有、非抽象、非接口）
	 */
	public static List<Class<?>> scanByInterface(String packageName, final Class<?> interfaceClass) throws IOException {
		return scan(packageName, new ClassFilter() {

			@Override
			public boolean filter(Class<?> clazz) {
				return (verifyModifier(clazz)
						&& !interfaceClass.equals(clazz)
						&& interfaceClass.isAssignableFrom(clazz));
			}
		});
	}

	/**
	 * 按注解扫描，得到使用该注解的类（公有、非抽象、非接口）
	 */
	public static List<Class<?>> scanByAnnotation(String packageName, final Class<? extends Annotation> annoClass)
			throws IOException {
		return scan(packageName, new ClassFilter() {

			@Override
			public boolean filter(Class<?> clazz) {
				return (verifyModifier(clazz)
						&& !annoClass.equals(clazz)
						&& clazz.isAnnotationPresent(annoClass));
			}
		});
	}

	/**
	 * 扫描类
	 */
	public static List<Class<?>> scan(String packageName, ClassFilter filter) throws IOException {
		List<Class<?>> result = new ArrayList<Class<?>>();

		Enumeration<URL> urls = Thread.currentThread().getContextClassLoader()
				.getResources(packageName.replaceAll("\\.", "/"));
		if (urls != null) {
			URL url = null;
			while (urls.hasMoreElements()) {
				url = (URL) urls.nextElement();
				
				if ("file".equals(url.getProtocol())) {
					loadClass(result, new File(url.getPath()), packageName, filter);
				} else if ("jar".equals(url.getProtocol())) {
					JarURLConnection conn = (JarURLConnection) url.openConnection();
					JarFile jarFile = conn.getJarFile();
					loadClass(result, jarFile, packageName, filter);
				}
			}
		}

		return result;
	}

	// ----------------------
	// private method
	// ----------------------
	/**
	 * 加载类
	 */
	private static void loadClass(List<Class<?>> result, File file, String packageName, ClassFilter filter) {
		File[] files = file.listFiles(new FileFilter() {

			@Override
			public boolean accept(File f) {
				return (f.isDirectory() || (f.isFile() && f.getName().endsWith(".class")));
			}
		});
		if (files == null || files.length == 0) {
			return;
		}

		if (packageName.length() > 0) {
			packageName += ".";
		}

		Class<?> clazz = null;
		String name;
		for (File f : files) {
			if (f.isDirectory()) {
				loadClass(result, f, packageName + f.getName(), filter);
			} else {
				name = packageName + f.getName().substring(0, f.getName().lastIndexOf("."));
				try {
					clazz = Class.forName(name);
					if (filter != null && filter.filter(clazz)) {
						result.add(clazz);
					}
				} catch (Exception e) {
					LogKit.error("Load Class Error. ClassName = [" + name + "]", e);
				}
			}
		}
	}

	/**
	 * 加载Jar包中的类
	 */
	private static void loadClass(List<Class<?>> result, JarFile jarFile, String packageName, ClassFilter filter) {
		if (packageName.length() > 0) {
			packageName += ".";
		}

		Enumeration<JarEntry> entries = jarFile.entries();
		JarEntry jarEntry;
		String name;
		Class<?> clazz;
		while (entries.hasMoreElements()) {
			jarEntry = entries.nextElement();
			name = jarEntry.getName();
			if (jarEntry.isDirectory()) {
				continue;
			}
			if (!name.endsWith(".class")) {
				continue;
			}
			name = name.replaceAll("[\\\\/]", ".");
			if (!name.startsWith(packageName)) {
				continue;
			}
			name = name.substring(0, name.lastIndexOf("."));
			try {
				clazz = Class.forName(name);
				if (filter != null && filter.filter(clazz)) {
					result.add(clazz);
				}
			} catch (Exception e) {
				LogKit.error("Load Jar Class Error. ClassName = [" + name + "], Jar = [" + jarFile.getName() + "]", e);
			}
		}
	}

	/**
	 * 验证类的修饰符。公有、非抽象、非接口
	 */
	private static boolean verifyModifier(Class<?> clazz) {
		int mod = clazz.getModifiers();
		return Modifier.isPublic(mod) && !Modifier.isAbstract(mod) && !Modifier.isInterface(mod);
	}

}
