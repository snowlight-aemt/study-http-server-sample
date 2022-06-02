package bong.lines.basic.comm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import bong.lines.basic.handler.getLoginParam.LoginDTO;

public class ParamUtils {
    private Map<String, String> paramMap;

    public ParamUtils(String url) {
        paramMap = parseToMap(url);
    }

    public <T> T map(Class<T> clazz) {
        assert paramMap == null : "맵핑 참조값이 NULL입니다.";

        T newInstance = createInstance(clazz);

        List<Method> methods = Arrays.stream(clazz.getDeclaredMethods()).filter(m -> m.getName().startsWith("set")).collect(Collectors.toList());
        for (Method method : methods) {
            String key = convertJavaBeanMethodToFieldName(method.getName());

            try {    
                // TODO 타입 형변환 현재는 문자열만 가능
                method.invoke(newInstance, paramMap.get(key));
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return newInstance;
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

    // TODO 정규식
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

    private String[] parserURLToField(String line) {
        return line.split("\\?")[1].split("\\&");
    }
}
