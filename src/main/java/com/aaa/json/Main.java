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
        String result = "{";
        Field[] declaredFields = o.getClass().getDeclaredFields();
        for (int index = 0; index < declaredFields.length; index++) {
            Field i = declaredFields[index];
            i.setAccessible(true);

            if (!i.isAnnotationPresent(Transient.class)) {


                result += "\n" + tabulation + "\"" + i.getName() + "\" : ";
                if (i.getType().isPrimitive()) {
                    result += i.get(o);

                } else if (i.getType().getName().equals("java.lang.String")) {
                    result += "\"" + i.get(o) + "\"";
                } else if (Collection.class.isAssignableFrom(i.getType())) {
                    tabulation += "\t";
                    result += "[\n" + tabulation;
                    Collection collection = (Collection) i.get(o);
                    Iterator iterator = collection.iterator();
                    while (iterator.hasNext()) {
                        result += serialize(iterator.next());
                        if (iterator.hasNext()) {
                            result += ",\n" + tabulation;
                        } else {
                            result += "\n";
                        }
                    }
                    tabulation = tabulation.replaceFirst("\t", "");
                    result += tabulation + "]";

                } else {
                    result += serialize(i.get(o));
                }

                if (index != declaredFields.length - 1) {
                    result += ",";
                }
            }


            i.setAccessible(false);
        }
        tabulation = tabulation.replaceFirst("\t", "");
        result += "\n" + tabulation + "}";
        return result;

    }

}

