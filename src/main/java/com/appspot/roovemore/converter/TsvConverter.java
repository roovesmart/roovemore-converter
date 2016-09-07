package com.appspot.roovemore.converter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * To convert the Tsv's String to <T> Object. and convert the <T> Object to Tsv's String.
 *
 * @author roove
 *
 * @param <T> convert Object Class.
 */
public class TsvConverter<T> {

	/**
	 * tsv's line separator
	 */
	public String lineSeparator = System.getProperty("line.separator");

	/**
	 * tsv's string separator
	 */
	public String stringSeparator = "\t";

	/**
	 * Character for the Empty String or Null String
	 *
	 * 空文字専用文字列
	 * 空文字、nullの場合にtsv化した場合、
	 * 復元すると要素が詰まるため（要素数が少なくなるため）の考慮
	 */
	public String tsvEmptyStr = "$$null$$";

	/**
	 * To convert the <T> Object to tsv's Array.
	 *
	 * @param srcObject
	 * 			to convert Tsv Array
	 *
	 * @return TsvFormat Array.
	 *
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public String[] convertTsvArray(T srcObject)
			throws NullPointerException, IllegalArgumentException, IllegalAccessException {

		if (srcObject == null) {
			throw new NullPointerException(
					"The object parameter must not be null.");
		}

		Class<?> c = srcObject.getClass();
		System.out.println(c);

		HashMap<Integer, String> map = new HashMap<Integer, String>();

		Field[] fs = null;
		try {
			fs = c.getFields();
			for (Field f : fs) {
				Tsv tsvAnnotation = f.getAnnotation(Tsv.class);
				if (tsvAnnotation == null) {
					//TODO modify exception class
					throw new NullPointerException(
							"The field does not have Tsv.Annotation. fieidName = " + f.getName());
				}

				Object value = f.get(srcObject);
				String stringValue = null;
				if (value == null) {
					stringValue = tsvEmptyStr;
				} else {
					if (value instanceof String) {
					} else if (value instanceof Long) {
					} else if (value instanceof Integer) {
					} else {
						//TODO modify exception class
						throw new NullPointerException(
								"Type is required to be String or Long or Integer. fieidName = " + f.getName());
					}
					stringValue = value.toString();
				}

				map.put(tsvAnnotation.no(), stringValue);
			}

		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(
					"field reflection error :  the object parameter class exception. " + e);

		} catch (IllegalAccessException e) {
			throw new IllegalAccessException(
					"field reflection error :  the object parameter class exception. " + e);
		}

		List<Entry<Integer, String>> entries = new ArrayList<Entry<Integer, String>>(map.entrySet());

		Collections.sort(entries, new Comparator<Entry<Integer, String>>() {
			@Override
			public int compare(Entry<Integer, String> o1, Entry<Integer, String> o2) {
				return o1.getKey().compareTo(o2.getKey());
			}
		});

		String[] array = new String[entries.size()];

		for (int i = 0; i < entries.size(); i++) {
			array[i] = entries.get(i).getValue();
		}

		return array;

	}

	/**
	 * To convert the tsv's Array to <T> Object.
	 *
	 * @param srcArray
	 * @param destClazz
	 * @return <T> Object
	 *
	 * @throws NullPointerException
	 * @throws InstantiationException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public T convertEntity(String[] srcArray, Class<T> destClazz)
		throws  NullPointerException, InstantiationException, IllegalArgumentException, IllegalAccessException {

		T entity = null;
		try {
			entity = destClazz.newInstance();
		} catch (InstantiationException e) {
			throw new InstantiationException("param destClazz newInstance error e = " + e);
		}

		try {
			Class<?> c = entity.getClass();

			Field[] fs = null;
			fs = c.getFields();
			for (Field f : fs) {
				Tsv tsvAnnotation = f.getAnnotation(Tsv.class);
				if (tsvAnnotation == null) {
					//TODO modify exception class
					throw new NullPointerException(
							"The field does not have Tsv.Annotation. fieidName = " + f.getName());
				}

				Class<?> type = f.getType();

				String value = srcArray[tsvAnnotation.no() - 1];

				//TODO add "double", "float"
				if( tsvEmptyStr.equals(value)){
					f.set(entity, null);
				}else if (type == String.class){
					f.set(entity, value);
				}else if(type == Long.class){
					f.set(entity, Long.parseLong(value));
				}else if(type == long.class){
					long l = Long.parseLong(value);
					f.set(entity, l);
				}else if (type == Integer.class){
					f.set(entity, Integer.parseInt(value));
				}else if (type == int.class){
					int i = Integer.parseInt(value);
					f.set(entity, i);
				}else{
					//TODO modify exception class
					throw new NullPointerException(
							"Type is required to be String or Long or Integer. fieidName = " + f.getName());
				}

			}

		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(
					"field reflection error :  the object parameter class exception. " + e);

		} catch (IllegalAccessException e) {
			throw new IllegalAccessException(
					"field reflection error :  the object parameter class exception. " + e);
		}

		return entity;
	}

	/**
	 * To convert the <T> Object to Tsv's String.
	 *
	 * 例：以下のような文字列にします。
	 * key [tab] qThemeId [tab] …みたいな文字列にします。
	 *
	 * @param srcObject
	 * @return tsv String
	 *
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public String convertTsvString(T srcObject)
			throws NullPointerException, IllegalArgumentException, IllegalAccessException {

		String[] array = convertTsvArray(srcObject);
		StringBuilder sb = new StringBuilder();
		for (String s : array) {
			if (sb.length() != 0) {
				sb.append(stringSeparator);
			}
			sb.append(s);
		}
		return sb.toString();
	}

	/**
	 * To convert the <T> ObjectList to Tsv's String.
	 *
	 * @param srcObjectList
	 *
	 * @return tsv's String
	 *
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public String convertTsvString(List<T> srcObjectList)
			throws NullPointerException, IllegalArgumentException, IllegalAccessException {

		StringBuilder sb = new StringBuilder();
		for (T srcObject : srcObjectList) {
			String s = convertTsvString(srcObject);
			if (sb.length() != 0) {
				sb.append(lineSeparator);
			}
			sb.append(s);
		}
		return sb.toString();
	}

	/**
	 * To convert the Tsv's String  to <T> ObjectList.
	 *
	 * @param srcTsvString
	 * @param destClazz
	 *
	 * @return <T> ObjectList
	 *
	 * @throws NullPointerException
	 * @throws InstantiationException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public List<T> convertEntityList(String srcTsvString, Class<T> destClazz)
		throws  NullPointerException, InstantiationException, IllegalArgumentException, IllegalAccessException {

		String[] listArray = srcTsvString.split(lineSeparator);

		List<T> list = new ArrayList<T>();

		for (String str : listArray) {

			String[] array = str.split(stringSeparator);
			if (array.length == 1) {
				// 想定外のデータの場合
				return null;
			}

			T t = convertEntity(array, destClazz);
			list.add(t);
		}
		return list;
	}

	/**
	 * To convert the Tsv's String  to <T> Object.
	 *
	 * @param srcTsvString
	 * @param destclazz
	 *
	 * @return <T> ObjectList
	 *
	 * @throws NullPointerException
	 * @throws InstantiationException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public T convertEntity(String srcTsvString, Class<T> destclazz)
			throws  NullPointerException, InstantiationException, IllegalArgumentException, IllegalAccessException {

		if (isEmpty(srcTsvString)){
			throw new NullPointerException(
					"The srcTsvStrin parameter must not be null.");
		}

		String[] array = srcTsvString.split(stringSeparator);
		if (array.length == 1) {
			// 想定外のデータの場合
			return null;
		}
		return convertEntity(array, destclazz);

	}

	private boolean isEmpty(String text) {
		return text == null || text.length() == 0;
	}
}
