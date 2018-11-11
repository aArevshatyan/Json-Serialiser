package com.aaa.json;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;

public class Main {

    private static String tabulation = "";

    public static void main(String[] args) throws IllegalAccessException {

        System.out.println("\nBook in Json format \n ");
        System.out.println(serialize(new Book("Sleeping Beauty", 20)));
        System.out.println("\nUser in Json format \n ");
        System.out.println(serialize(new User("Kate", 12, new Book("Little Red Riding Hood", 20))));
        System.out.println("\n Library in Json format \n ");
        System.out.println(serialize(new Library(300)));

    }

    private static String serialize(Object o) throws IllegalAccessException {

        tabulation += "\t";
        StringBuilder jsonString = new StringBuilder();
        jsonString.append("{");
        Field[] declaredFields = o.getClass().getDeclaredFields();
        for (int index = 0; index < declaredFields.length; index++) {
            Field field = declaredFields[index];
            field.setAccessible(true);

            if (!field.isAnnotationPresent(Transient.class)) {


                jsonString.append("\n").append(tabulation).append("\"").append(field.getName()).append("\" : ");
                if (field.getType().isPrimitive()) {
                    jsonString.append(field.get(o));

                } else if (field.getType().getName().equals("java.lang.String")) {
                    jsonString.append("\"").append(field.get(o)).append("\"");
                } else if (Collection.class.isAssignableFrom(field.getType())) {
                    tabulation += "\t";
                    jsonString.append("[\n").append(tabulation);
                    Collection collection = (Collection) field.get(o);
                    Iterator iterator = collection.iterator();
                    while (iterator.hasNext()) {
                        jsonString.append(serialize(iterator.next()));
                        if (iterator.hasNext()) {
                            jsonString.append(",\n").append(tabulation);
                        } else {
                            jsonString.append("\n");
                        }
                    }
                    tabulation = tabulation.replaceFirst("\t", "");
                    jsonString.append(tabulation).append("]");

                } else {
                    jsonString.append(serialize(field.get(o)));
                }

                if (index != declaredFields.length - 1) {
                    jsonString.append(",");
                }
            }


            field.setAccessible(false);
        }
        tabulation = tabulation.replaceFirst("\t", "");
        jsonString.append("\n").append(tabulation).append("}");
        return jsonString.toString();

    }

}

