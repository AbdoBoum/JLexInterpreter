package com.demointerpreter.tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

// generate AST classes
public class GenerateAst {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: generate_ast <output directory>");
            System.exit(1);
        }
        String outputDir = args[0];
        defineAst(outputDir, "Expression", Arrays.asList(
                "Binary   : Expression left, Token operator, Expression right",
                "Grouping : Expression expression",
                "Literal  : Object value",
                "Unary    : Token operator, Expression right"
        ));
    }

    private static void defineAst(String outputDir, String baseClassName, List<String> types) throws IOException {
        String path = outputDir + "/" + baseClassName + ".java";
        PrintWriter writer = new PrintWriter(path, "UTF-8");
        writeBaseClassHeader(baseClassName, writer);
        writeBaseClassBody(baseClassName, types, writer);
        writeBaseClassFooter(writer);
    }

    private static void writeBaseClassHeader(String baseClassName, PrintWriter writer) {
        writer.println("package com.demointerpreter.grammar;");
        writer.println();
        writer.println("import com.demointerpreter.lexical_analyzer.Token;");
        writer.println();
        writer.println("abstract class " + baseClassName + " {");



    }

    private static void writeBaseClassBody(String baseClassName, List<String> types, PrintWriter writer) throws IOException {
        // The AST classes.
        for (String type : types) {
            String className = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();
            defineType(writer, baseClassName, className, fields);

        }
    }

    private static void defineType(PrintWriter writer, String baseClassName, String className, String fields) throws IOException {
        writeClassHeader(writer, baseClassName, className);
        writeFields(writer, fields);
        writeConstructor(writer, className, fields);
        writeClassFooter(writer);
    }

    private static void writeClassHeader(PrintWriter writer, String baseClassName, String className) throws IOException {
       writer.println();
        writer.println("static class " + className + " extends " + baseClassName + " {");
    }

    private static void writeFields(PrintWriter writer, String fieldList) throws IOException {
        String[] fields = fieldList.split(", ");
        for (String field: fields) {
            writer.println("public " + field + " ;");
        }
        writer.println();
    }

    private static void writeConstructor(PrintWriter writer, String className, String fieldList) throws IOException{
        String[] fields = fieldList.split(", ");
        writer.println("public " + className + " (" + fieldList + ") {");
        for (String field: fields) {
            String name = field.split(" ")[1];
            writer.println("this." + name + " = " + name + " ;");
        }
        writer.println("}");
        writer.println();
    }

    private static void writeClassFooter(PrintWriter writer) throws IOException {
        writer.println(" }");
    }


    private static void writeBaseClassFooter(PrintWriter writer) throws IOException {
        writer.println(" }");
        writer.close();
    }
}
