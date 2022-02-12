package ru.otus.appcontainer;

import lombok.extern.log4j.Log4j2;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        // You code here...
        var classMethods = configClass.getMethods();
        var sortedMethodsList = Arrays.stream(classMethods).
                filter(classMethod -> classMethod.isAnnotationPresent(AppComponent.class)).
                collect(Collectors.toMap(classMethod -> classMethod.getAnnotation(AppComponent.class), classMethod -> classMethod)).entrySet().stream().sorted(Comparator.comparing(entry -> entry.getKey().order())).collect(Collectors.toList());

        if (sortedMethodsList.stream().map(Map.Entry::getKey).count() != sortedMethodsList.stream().map(Map.Entry::getKey).map(e -> e.name()).collect(Collectors.toList()).stream().distinct().count()) {
            log.info("found duplicate AppComponents");
            throw new RuntimeException();
        }


        sortedMethodsList.forEach(method -> {
            var methodToInvoke = method.getValue();
            var parameters = methodToInvoke.getParameters();
            Object[] args = new Object[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                args[i] = getAppComponent(parameters[i].getType());
            }
            methodToInvoke.setAccessible(true);
            try {
                Object configInstance = null;
                try {
                    configInstance = configClass.getDeclaredConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                    log.error("error creating configInstance" + e.getMessage());
                    throw new RuntimeException();
                }
                Object object = methodToInvoke.invoke(configInstance, args);
                appComponents.add(object);
                appComponentsByName.put(method.getKey().name(), object);
            } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                log.error("error invoking method" + methodToInvoke.getName() + e.getMessage());
                throw new RuntimeException();
            }
        });

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
