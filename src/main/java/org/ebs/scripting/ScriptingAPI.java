package org.ebs.scripting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.Objects;

public class ScriptingAPI {
    public static void runScript(Reader script, Map<String, Object> variables) throws Exception {
        BufferedReader reader = new BufferedReader(script);
        String line;
        boolean result = false;

        while ((line = reader.readLine()) != null) {
            // Split the line into tokens separated by whitespace
            String[] tokens = line.split("\\s+");
            if (tokens[0].equals("if")) {
                String var1 = tokens[1];
                String operator = tokens[2];
                String var2 = tokens[3];
                String operator2 = null;
                String condition = null;

                try {
                    operator2 = tokens[4];
                    condition = tokens[5];
                } catch (Exception ignored) {

                }


                for (String varName : variables.keySet()) {
                    if (Objects.equals(varName, var1)) {
                        var1 = (String) variables.get(varName);
                    }
                }
                for (String varName : variables.keySet()) {
                    if (Objects.equals(varName, var2)) {
                        var2 = (String) variables.get(varName);
                    }
                }

                System.out.printf("%s %s %s %s %s \n", var1, operator, var2, operator2, condition);

                // perform the operation only if both var1 and var2 are numeric
                if (operator2 == null && condition == null) {
                    switch (operator) {
                        case ">":
                            result = Double.parseDouble(var1) > Double.parseDouble(var2);
                            break;
                        case "<":
                            result = Double.parseDouble(var1) == Double.parseDouble(var2);
                            break;
                        case "==":
                            result = Double.parseDouble(var1) == Double.parseDouble(var2);
                            break;
                        default:
                            throw new Exception("Second operator is invalid");


                    }
                    System.out.println(result);

                } else if (isNumeric(var1) && isNumeric(var2)) {
                    switch (operator) {
                        case "+":
                            switch (operator2) {
                                case ">":
                                    result = Double.parseDouble(var1) + Double.parseDouble(var2) > Double.parseDouble(condition);
                                    break;
                                case "<":
                                    result = Double.parseDouble(var1) + Double.parseDouble(var2) < Double.parseDouble(condition);
                                    break;
                                case "==":
                                    result = Double.parseDouble(var1) + Double.parseDouble(var2) == Double.parseDouble(condition);
                                    break;
                                default:
                                    throw new Exception("Second operator is invalid");
                            }
                            break;
                        case "-":
                            switch (operator2) {
                                case ">":
                                    result = Double.parseDouble(var1) - Double.parseDouble(var2) > Double.parseDouble(condition);
                                    break;
                                case "<":
                                    result = Double.parseDouble(var1) - Double.parseDouble(var2) < Double.parseDouble(condition);
                                    break;
                                case "==":
                                    result = Double.parseDouble(var1) - Double.parseDouble(var2) == Double.parseDouble(condition);
                                    break;
                                default:
                                    throw new Exception("Second operator is invalid");

                            }
                            break;
                        case "*":
                            switch (operator2) {
                                case ">":
                                    result = Double.parseDouble(var1) * Double.parseDouble(var2) > Double.parseDouble(condition);
                                    break;
                                case "<":
                                    result = Double.parseDouble(var1) * Double.parseDouble(var2) < Double.parseDouble(condition);
                                    break;
                                case "==":
                                    result = Double.parseDouble(var1) * Double.parseDouble(var2) == Double.parseDouble(condition);
                                    break;
                                default:
                                    throw new Exception("Second operator is invalid");

                            }
                            break;
                        case "/":
                            switch (operator2) {
                                case ">":
                                    result = Double.parseDouble(var1) / Double.parseDouble(var2) > Double.parseDouble(condition);
                                    break;
                                case "<":
                                    result = Double.parseDouble(var1) / Double.parseDouble(var2) < Double.parseDouble(condition);
                                    break;
                                case "==":
                                    result = Double.parseDouble(var1) / Double.parseDouble(var2) == Double.parseDouble(condition);
                                    break;
                                default:
                                    throw new Exception("Second operator is invalid");

                            }
                            break;
                        case "%":
                            switch (operator2) {
                                case ">":
                                    result = Double.parseDouble(var1) % Double.parseDouble(var2) > Double.parseDouble(condition);
                                    break;
                                case "<":
                                    result = Double.parseDouble(var1) % Double.parseDouble(var2) < Double.parseDouble(condition);
                                    break;
                                case "==":
                                    result = Double.parseDouble(var1) % Double.parseDouble(var2) == Double.parseDouble(condition);
                                    break;
                                default:
                                    throw new Exception("Second operator is invalid");

                            }
                            break;
                    }
                    System.out.println(result);

                } else {

                    throw new Exception("If statement variables are invalid");

                }


            } else if (tokens[0].equals("set")) {
                // Set a variable
                String varName = tokens[1];
                Object value = tokens[2];
                variables.put(varName, value);
            } else {
                // Process other statements
            }

        }
        reader.close();
    }

    public static boolean isNumeric(Object in) {
        try {
            Double.parseDouble((String) in);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
