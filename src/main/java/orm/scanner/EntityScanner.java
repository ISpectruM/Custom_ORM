package orm.scanner;

import annotations.Entity;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class EntityScanner {
    public static List<Class> getAllEntities(String startPath) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        List<Class> result = new ArrayList<>();

        File dir = new File(startPath);
        File[] fileList = dir.listFiles();

        for (File file : fileList) {
            if (file.isFile()) {
                String fileName = file.getName();
                if (!fileName.endsWith(".class")) {
                    continue;
                }
                try {
                    Class myClass = Class.forName(
                            fileName.substring(0, fileName.length() - 6));
                    myClass.newInstance();

                    if (!myClass.isAnnotationPresent(Entity.class)) continue;

                    result.add(myClass);
                    System.out.println(myClass.getName());
                } catch (ClassNotFoundException e) {
//                    System.out.println("x" + fileName);
                }
            } else {
                List<Class> allEnt = getAllEntities(
                        startPath + File.separator + file.getName());
                result.addAll(allEnt);
            }
        }
        return result;
    }
}
