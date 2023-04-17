package com.zakiis.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsvUtil {
	
	Logger log = LoggerFactory.getLogger(CsvUtil.class);

	public static void write(OutputStream os, List<?> rows, String charset){
		if (rows == null || rows.size() == 0) {
			return;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Class<?> clazz = rows.get(0).getClass();
			Field[] declaredFields = clazz.getDeclaredFields();
			String[] columnNames = new String[declaredFields.length];
			int i = 0;
			for (Field field : declaredFields) {
				columnNames[i++] = field.getName();
				ClassUtil.makeAccessible(field, rows.get(0));
			}
			StringBuilder builder = new StringBuilder();
			builder.append(StringUtils.join(columnNames, ",")).append(System.lineSeparator());
			for (Object row : rows) {
				String[] rowColumns = new String[declaredFields.length];
				i = 0;
				for (Field field : declaredFields) {
					Object fieldValue = field.get(row);
					if (fieldValue == null) {
						rowColumns[i++] = "";
					} else {
						if (ClassUtil.isNumber(field.getType()) || ClassUtil.isBoolean(field.getType())) {
							rowColumns[i++] = fieldValue.toString();
						} else if (Date.class.isAssignableFrom(field.getType())) {
							rowColumns[i++] = String.format("\"%s\"", sdf.format((Date)fieldValue).replaceAll("\"", "\"\""));
						} else {
							rowColumns[i++] = String.format("\"%s\"", fieldValue.toString().replaceAll("\"", "\"\""));
						}
					}
				}
				builder.append(StringUtils.join(rowColumns, ",")).append(System.lineSeparator());
			}
			os.write(builder.toString().getBytes(Charset.forName(charset)));
		} catch (Exception e) {
			throw new RuntimeException("csv write method got an excpeiton", e);
		}
	}
	
	public static <T> List<T> read(InputStream is, String charset, Class<T> rowClazz) {
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is))) {
			String line = bufferedReader.readLine();
			if (line == null) {
				return null;	
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String[] columnNames = parseLine(line, ',');
			Field[] rowFields = new Field[columnNames.length];
			Constructor<?>[] fieldConstructors = new Constructor[columnNames.length];
			T rowModel = rowClazz.getConstructor().newInstance();
			for (int i = 0; i < columnNames.length; i++) {
				Field field = rowClazz.getDeclaredField(columnNames[i]);
				ClassUtil.makeAccessible(field, rowModel);
				rowFields[i] = field;
				if (!Date.class.isAssignableFrom(field.getType())) {
					Constructor<?> fieldConstructor = field.getType().getConstructor(String.class);
					fieldConstructors[i] = fieldConstructor;
				}
			}
			List<T> results = new ArrayList<T>();
			while ((line = bufferedReader.readLine()) != null) {
				if (StringUtils.isEmpty(line)) {
					continue;
				}
				String[] columnValues =parseLine(line, ',');
				T row = rowClazz.getDeclaredConstructor().newInstance();
				for (int i = 0; i < rowFields.length; i++) {
					if (columnValues[i] != null && columnValues[i].length() > 0) {
						if (fieldConstructors[i] != null) {
							rowFields[i].set(row, fieldConstructors[i].newInstance(columnValues[i]));
						} else {
							rowFields[i].set(row, sdf.parse(columnValues[i]));
						}
					}
				}
				results.add(row);
			}
			return results;
		} catch (Exception e) {
			throw new RuntimeException("csv read method got an excpeiton", e);
		}
	}
	
	private static String[] parseLine(String csvLine, char separater) throws IOException {
		int marker = 0;
		int quoteStart = -1;
		int quoteEnd = -1;
		List<String> columns = new ArrayList<String>();
		for (int i = 0; i < csvLine.length(); i++) {
			if (csvLine.charAt(i) == '"') {
				if (i + 1 < csvLine.length() && csvLine.charAt(i + 1) == '"') {
					i++;
				} else if (quoteStart == -1) {
					quoteStart = i;
				} else {
					quoteEnd = i;
					columns.add(csvLine.substring(quoteStart + 1, quoteEnd).replaceAll("\"\"", "\""));
					marker = i;
					quoteStart = -1;
					quoteEnd = -1;
				}
				continue;
			} else if (csvLine.charAt(i) == separater) {
				if (csvLine.charAt(marker) != '"') {
					if (marker != 0) {
						marker++;
					}
					String columnValue = csvLine.substring(marker, i);
					columns.add(columnValue);
				}
				marker = i;
			}
		}
		if (marker < csvLine.length() - 1) {
			columns.add(csvLine.substring(marker + 1));
		}
		return columns.toArray(new String[columns.size()]);
	}

}
