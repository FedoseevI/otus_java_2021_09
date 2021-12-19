package ru.otus.homework.proxy;

import ru.otus.homework.proxy.annotation.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class LogProxyCreator {

    private LogProxyCreator() {
    }

    public static <T> T getLogProxy(Class<T> targetInterface, Object targetInstance) {

        var invocationHandler = new LogInvocationHandler(targetInstance);
        var interfaces = new Class<?>[]{targetInterface};
        Object proxy = Proxy.newProxyInstance(targetInterface.getClassLoader(), interfaces, invocationHandler);

        return targetInterface.cast(proxy);
    }

    static class LogInvocationHandler implements InvocationHandler {

        private final Object target;
        private final List<Method> targetMethods;

        public LogInvocationHandler(Object target) {
            this.target = target;
            this.targetMethods = Arrays.stream(target.getClass().getMethods())
                    .filter(m -> m.isAnnotationPresent(Log.class))
                    .toList();
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            Optional<Method> targetMethod = targetMethods.stream()
                    .filter(m -> m.getName().equals(method.getName()))
                    .filter(m -> Arrays.equals(m.getParameterTypes(), method.getParameterTypes()))
                    .findFirst();

            if (targetMethod.isPresent()) {
                System.out.printf("LogProxy logging: invoke '%s' args%s%n", method.getName(), Arrays.toString(args) == "null" ? "[WITHOUT args]" : Arrays.toString(args));
            }

            return method.invoke(target, args);
        }
    }
}
