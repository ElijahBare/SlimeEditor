package org.ebs.scripting;

import javax.swing.*;
import java.io.File;
import java.util.*;

public class ScriptingAPI {


    public static String result = "";
    public static Map<String, Object> variables = new HashMap<String, Object>();

    public static void runScript(String path) {
        result = "";
        File file = new File(path);
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNext()) {
                String line = sc.nextLine();
                String[] strings = line.split("\\W+");
                ScriptingAPI parser = new ScriptingAPI();
                parser.push(strings);


                if (line.startsWith("LOG")) {
                    String log = line.substring(line.indexOf("LOG ") + 4);
                    result += log + "\n";
                }


                if (line.startsWith("VAR")) {
                    //already set
                    if (variables.containsValue(strings[1])) {
                        variables.remove(strings[1]);
                        variables.put(strings[1], strings[2]);
                    } else {
                        variables.put(strings[1], strings[2]);
                    }
                    //result += (strings[1] + " has been set to " + strings[2]) + "\n";

                }

            }
            JOptionPane.showMessageDialog(null,"Program output: \n" + result + "\n");

        } catch (Exception e) {
           JOptionPane.showMessageDialog(null,"Something wrong! " + Arrays.toString(e.getStackTrace()));
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
                case ("SUBTRACT"):
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
                        result += (stack.get(stack.size() - 1)) + "\n";
                        stack.remove(stack.size() - 1);
                    }
                    break;
                case ("PICK"):
                    if (stack.size() > 0) {
                        result += (stack.get(stack.size() - 1)) + "\n";
                    }
                    break;
            }
        }


    }
}