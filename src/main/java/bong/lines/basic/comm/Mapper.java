package bong.lines.basic.comm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Mapper {

    public Map<String, String> mapUrlToMap(String urlParameter) {
        Map<String, String> map = new HashMap<>();
        Arrays.stream(parserURLToField(urlParameter))
            .forEach(m -> {
                String[] valeu = m.split("=");
                map.put(valeu[0], valeu[1]);
            });
        return map;
    }

    public <T> T mapMapToObject(Map<String, String> source, Class<T> clazz) {
        T newInstance = createInstance(clazz);

        List<Method> methods = Arrays.stream(clazz.getDeclaredMethods()).filter(m -> m.getName().startsWith("set")).collect(Collectors.toList());
        for (Method method : methods) {
            String key = convertJavaBeanMethodToFieldName(method.getName());

            try {
                Object obj = null;
                Class<?> arguType = method.getParameterTypes()[0];
                if (arguType.equals(String.class)) {
                    obj = source.get(key);
                } else if (arguType.equals(Integer.class) || arguType.equals(int.class)) {
                    obj = Integer.parseInt(source.get(key));
                } else {
                    throw new IllegalArgumentException(key +"에 타입은 "+ arguType +"입니다.");
                }

                method.invoke(newInstance, obj);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return newInstance;
    }

    // TODO 컬렉션, 객체 지원하지 않음.
    public <T> String mapObjectToJSON(T t) {
        StringBuffer buff = new StringBuffer();
        buff.append("{");
        Arrays.stream(t.getClass().getDeclaredMethods()).filter(m -> m.getName().startsWith("get") || m.getName().startsWith("is")).forEach(m -> {
            buff.append("\""+ convertJavaBeanMethodToFieldName(m.getName()) +"\"");
            buff.append(":");
            buff.append(getValue(t, m));
            buff.append(",");
        });
        buff.deleteCharAt(buff.length()-1);
        buff.append("}");

        return buff.toString();
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
        if (beanMethodName.startsWith("get") || beanMethodName.startsWith("set")) {
            String fixName = beanMethodName.substring(3, 4).toLowerCase();
            String name = beanMethodName.substring(4);
            return fixName + name;
        } else if (beanMethodName.startsWith("is")) {
            String fixName = beanMethodName.substring(2, 3).toLowerCase();
            String name = beanMethodName.substring(3);
            return fixName + name;
        } else {
            throw new IllegalArgumentException("자바 빈 규약을 위반했습니다.");
        }
    }

    private String[] parserURLToField(String param) {
        String decode = URLDecoder.decode(param, Charset.defaultCharset());
        return decode.split("\\&");
    }
}
