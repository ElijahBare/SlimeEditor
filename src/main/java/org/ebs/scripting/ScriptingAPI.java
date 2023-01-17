package org.ebs.scripting;

import javax.swing.*;
import java.io.File;
import java.util.*;

public class ScriptingAPI {


    public static String result = "";
    public static Map<String, Object> variables = new HashMap<String, Object>();
    static ArrayList<Double> stack = new ArrayList<>();

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
                    String log = "";
                    if (line.contains("%")){
                        String varname = line.substring(line.indexOf("%") + 1 ,line.indexOf("$"));
                        if (variables.containsKey(varname)){
                            log = (String) variables.get(varname);
                        }
                    }else {
                        log = line.substring(line.indexOf("LOG ") + 4);
                    }
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
            stack.clear();

        } catch (Exception e) {
           JOptionPane.showMessageDialog(null,"Something wrong! " + Arrays.toString(e.getStackTrace()));
           System.out.println("Something wrong! " + Arrays.toString(e.getStackTrace()));
        }

    }


    private void push(String[] strings) throws Exception {
        for (int i = 0; i < strings.length; i++) {
            switch (strings[i]) {

                case ("PUSH"):
                    if (strings[i + 1] != null) {
                        try {
                            double a = Double.parseDouble(strings[i + 1]);
                            stack.add(a);
                        } catch (NumberFormatException exception) {
                            System.out.println(strings[i+1]);
                            if (variables.containsKey(strings[i+1])){
                                double a = Double.parseDouble((String) variables.get(strings[i + 1]));
                                stack.add(a);
                            }
                            else{
                                throw new Exception("Failed to find variable or parse value of defined var" + Arrays.toString(exception.getStackTrace()));
                            }
                        }
                    }
                    break;
                case ("ADD"):
                    if (stack.size() > 1) {
                        double total = 0;

                        for (Double d : stack){
                            total += d;
                        }

                        stack.clear();

                        stack.add(total);

                    }
                    break;
                case ("SUBTRACT"):
                    if (stack.size() > 1) {
                        double total = 0;

                        for (Double d : stack){
                            total -= d;
                        }
                        stack.clear();

                        stack.add(-total);
                    }
                    break;
                case ("CLEAR"):
                    stack.clear();
                    break;
                case ("MULTIPLY"):
                    if (stack.size() > 1) {
                        double total = stack.get(0);

                        System.out.println(total);

                        for (int stackIt = 1; stackIt < stack.size(); stackIt++){
                            total = total * stack.get(stackIt);
                        }

                        stack.clear();

                        stack.add(total);

                    }
                    break;
                case ("DIVIDE"):
                    if (stack.size() > 1) {
                        double total = stack.get(0);

                        System.out.println(total);

                        for (int stackIt = 1; stackIt < stack.size(); stackIt++){
                            total = total / stack.get(stackIt);
                        }

                        stack.clear();

                        stack.add(total);
                    }
                    break;
                case ("MOD"):
                    if (stack.size() > 1) {
                        double total = stack.get(0);

                        System.out.println(total);

                        for (int stackIt = 1; stackIt < stack.size(); stackIt++){
                            total = total % stack.get(stackIt);
                        }

                        stack.clear();

                        stack.add(total);
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