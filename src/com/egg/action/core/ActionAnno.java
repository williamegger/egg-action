package com.egg.action.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class ActionAnno {

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface Action {

		/**
		 * 请求地址
		 */
		public String value() default "";

		/**
		 * 是否单例（默认单例）
		 */
		public boolean isSingle() default true;
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface POST {
	}

}
