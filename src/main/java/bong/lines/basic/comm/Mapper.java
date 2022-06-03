package bong.lines.basic.comm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Mapper {
    private Map<String, String> paramMap;

    public Mapper() {

    }

    public Mapper(String url) {
        paramMap = parseToMap(url);
    }

    // TODO map 객체를 인자로 받아서 처리하게 수정.
    public <T> T map(Class<T> clazz) {
        T newInstance = createInstance(clazz);

        List<Method> methods = Arrays.stream(clazz.getDeclaredMethods()).filter(m -> m.getName().startsWith("set")).collect(Collectors.toList());
        for (Method method : methods) {
            String key = convertJavaBeanMethodToFieldName(method.getName());

            try {    
                // TODO 타입 형변환 이슈 (현재는 문자열만 가능)
                method.invoke(newInstance, paramMap.get(key));
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return newInstance;
    }

    public <T> String mapToJSON(T t) {
        StringBuffer buff = new StringBuffer();
        buff.append("{");
        Arrays.stream(t.getClass().getDeclaredMethods()).filter(m -> m.getName().startsWith("get")).forEach(m -> {
            buff.append("\""+ convertJavaBeanMethodToFieldName(m.getName()) +"\"");
            buff.append(":");
            Object value = getValue(t, m);
            buff.append(",");
        });
        buff.deleteCharAt(buff.length()-1);
        buff.append("}");
        System.out.println(buff);

        return t.toString();
    }

    private <T> Object getValue(T t, Method m) {
        Object value = null;
        try {
            value = m.invoke(t);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        
        
        if (m.getReturnType().equals(String.class)) {
            value = "\""+ value +"\"";
        }
        // } else if (m.getReturnType().equals(Integer.class)) {

        // } else if (m.getReturnType().equals(Boolean.class)) {

        // } else if (m.getReturnType().equals(Float.class)) {

        // } else if (m.getReturnType().equals(Double.class)) {

        // } else if (m.getReturnType().equals(Byte.class)) {

        // } else if (m.getReturnType().equals(obj))

        return value.toString();
    }

    private <T> T createInstance(Class<T> clazz) {
        T newInstance = null;
        try {
            newInstance = clazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e1) {
            e1.printStackTrace();
        }
        return newInstance;
    }

    // TODO 정규식 적용
    private String convertJavaBeanMethodToFieldName(String beanMethodName) {
        String fixName = beanMethodName.substring(3, 4).toLowerCase();
        String name = beanMethodName.substring(4);
        return fixName + name;
    }

    private Map<String, String> parseToMap(String url) {
        Map<String, String> map = new HashMap<>();
        Arrays.stream(parserURLToField(url))
            .forEach(m -> {
                String[] valeu = m.split("=");
                String key = valeu[0];
                String value = valeu[1];
                map.put(key, value);
            });
        return map;
    }

    private String[] parserURLToField(String param) {
        return param.split("\\&");
    }
}
