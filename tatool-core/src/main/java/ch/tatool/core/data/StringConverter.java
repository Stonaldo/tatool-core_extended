/*******************************************************************************
 * Copyright (c) 2011 Michael Ruflin, André Locher, Claudia von Bastian.
 * 
 * This file is part of Tatool.
 * 
 * Tatool is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published 
 * by the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * Tatool is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Tatool. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package ch.tatool.core.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains handler methods that convert a String into other object types
 * 
 * @author Michael Ruflin
 */
public class StringConverter {

	private static Map<Class<?>, Converter<?>> converters;
	
	static {
		converters = new HashMap<Class<?>, Converter<?>>();
		
		// initialize default implementations
		converters.put(String.class, new IdentityConverter());
		converters.put(Integer.class, new IntegerConverter());
		converters.put(Long.class, new LongConverter());
		converters.put(Float.class, new FloatConverter());
		converters.put(Double.class, new DoubleConverter());
	}
	
	private StringConverter() {
	}
	
	public static void registerConverter(Class<?> c, Converter<?> converter) {
		converters.put(c, converter);
	}
	
	public static boolean containsConverter(Class<?> type) {
		return converters.containsKey(type);
	}
	
	/** Converts a String into another object type.
	 * 
	 * @param s the string to convert
	 * @throws RuntimeException if conversion is not possible.
	 */
	public static Object convert(String s, Class<?> type) {
		Converter<?> converter = converters.get(type);
		if (converter == null) {
			throw new RuntimeException("No matching converter available");
		}
		return converter.convert(s);
	}
	
	public interface Converter<T> {
		/**
		 * Convert the passed in String to an object of type T.
		 * 
		 * @param s the String to convert, guaranteed to be non-null
		 * @return the converted object
		 * @throws RuntimeException whatever Exception might occur (e.g. NumberFormatException) 
		 */
		public T convert(String s);
	}
	
	static class IdentityConverter implements Converter<String> {
		public String convert(String s) {
			return s;
		}
	}
	
	static class IntegerConverter implements Converter<Integer> {
		public Integer convert(String s) {
			// Intentionally don't catch NumberFormatException
			return Integer.parseInt(s); 
		}
	}
	
	static class LongConverter implements Converter<Long> {
		public Long convert(String s) {
			// Intentionally don't catch NumberFormatException
			return Long.parseLong(s); 
		}
	}
	
	static class DoubleConverter implements Converter<Double> {
		public Double convert(String s) {
			// Intentionally don't catch NumberFormatException
			return Double.parseDouble(s);
		}
	}
	
	static class FloatConverter implements Converter<Float> {
		public Float convert(String s) {
			// Intentionally don't catch NumberFormatException
			return Float.parseFloat(s);
		}
	}
}
