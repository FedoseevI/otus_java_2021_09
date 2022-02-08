package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.config.AppConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();
    private final List<AppComponent> listAppComponentAnnotation = new ArrayList<>();
    private final AppConfig appConfig = new AppConfig();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        // You code here...
        Method[] classMethods = configClass.getMethods();
        Map<String, Method> classMethodsMap = new HashMap<>();

        for (Method classMethod : classMethods) {
            if (classMethod.isAnnotationPresent(AppComponent.class)) {
                listAppComponentAnnotation.add(classMethod.getAnnotation(AppComponent.class));
                classMethodsMap.put(classMethod.getAnnotation(AppComponent.class).name(), classMethod);
            }
        }

        listAppComponentAnnotation.sort(Comparator.comparing(AppComponent::order));

        for (AppComponent appComponent : listAppComponentAnnotation) {
            var methodToInvoke = classMethodsMap.get(appComponent.name());
            var parameters = methodToInvoke.getParameters();
            Object[] args = new Object[parameters.length];
            var i = 0;
            for (Parameter parameter : parameters) {
                args[i] = getAppComponent(parameter.getType());
                i++;
            }
            methodToInvoke.setAccessible(true);
            try {
                Object object = methodToInvoke.invoke(appConfig, args);
                appComponents.add(object);
                appComponentsByName.put(appComponent.name(), object);
            } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                throw new RuntimeException("error invoking method" + methodToInvoke.getName());
            }
        }
    }


    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        for (Object component : appComponents) {
            if (componentClass.isAssignableFrom(component.getClass()) || componentClass.isInstance(component))
                return (C) component;
        }
        return null;
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }
}
