package org.ebs.scripting;

import java.io.File;
import java.util.*;

public class ScriptingAPI {

    public static Map<String, Object> variables = new HashMap<String, Object>();

    public static void runScript(String path) {
        File file = new File(path);
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNext()) {
                String line = sc.nextLine();
                String[] strings = line.split("\\W+");
                ScriptingAPI parser = new ScriptingAPI();
                parser.push(strings);


                if (line.startsWith("LOG")) {
                    System.out.println(line.substring(line.indexOf("LOG ") + 4));
                }

                if (line.startsWith("CONST")) {
                    //already set
                    if (variables.containsValue(strings[1])) {
                        throw new Exception("Const variable " + strings[1] + " Defined More than once");
                    } else {
                        variables.put(strings[1], strings[2]);
                        System.out.println(strings[1] + " has been set to " + strings[2]);
                    }
                }

                if (line.startsWith("VAR")) {
                    //already set
                    if (variables.containsValue(strings[1])) {
                        variables.remove(strings[1]);
                        variables.put(strings[1], strings[2]);
                    } else {
                        variables.put(strings[1], strings[2]);
                    }
                    System.out.println(strings[1] + " has been set to " + strings[2]);

                }
            }
        } catch (Exception e) {
            System.out.println("Something wrong! " + Arrays.toString(e.getStackTrace()));
        }

    }

    static ArrayList<Double> stack = new ArrayList<>();

    private void push(String[] strings) {
        for (int i = 0; i < strings.length; i++) {
            switch (strings[i]) {
                case ("PUSH"):
                    if (strings[i + 1] != null) {
                        try {
                            double a = Double.parseDouble(strings[i + 1]);
                            stack.add(a);
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    break;
                case ("ADD"):
                    if (stack.size() > 1) {
                        double a = stack.get(stack.size() - 1);
                        double b = stack.get(stack.size() - 2);
                        stack.remove(stack.size() - 1);
                        stack.remove(stack.size() - 1);
                        stack.add(a + b);
                    }
                    break;
                case ("MINUS"):
                    if (stack.size() > 1) {
                        double a = stack.get(stack.size() - 1);
                        double b = stack.get(stack.size() - 2);
                        stack.remove(stack.size() - 1);
                        stack.remove(stack.size() - 1);
                        stack.add(a - b);
                    }
                    break;
                case ("CLEAR"):
                    stack.clear();
                    break;
                case ("MULTIPLY"):
                    if (stack.size() > 1) {
                        double a = stack.get(stack.size() - 1);
                        double b = stack.get(stack.size() - 2);
                        stack.remove(stack.size() - 1);
                        stack.remove(stack.size() - 1);
                        stack.add(a * b);
                    }
                    break;
                case ("DIVIDE"):
                    if (stack.size() > 1) {
                        double a = stack.get(stack.size() - 1);
                        double b = stack.get(stack.size() - 2);
                        stack.remove(stack.size() - 1);
                        stack.remove(stack.size() - 1);
                        stack.add(a / b);
                    }
                    break;
                case ("POP"):
                    if (stack.size() > 0) {
                        System.out.println(stack.get(stack.size() - 1));
                        stack.remove(stack.size() - 1);
                    }
                    break;
                case ("PICK"):
                    if (stack.size() > 0) {
                        System.out.println(stack.get(stack.size() - 1));
                    }
                    break;
            }
        }
    }
}