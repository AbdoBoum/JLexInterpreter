package com.demointerpreter.tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

// generate Expression classes
// path for arg: src\com\demointerpreter\grammar
public class GenerateAst {
    public static final String BASE_CLASS_NAME = "Expression";

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: generate_ast <output directory>");
            System.exit(1);
        }
        var outputDir = args[0];
        defineAst(outputDir, BASE_CLASS_NAME, Arrays.asList(
                "Binary   : Expression left, Token operator, Expression right",
                "Grouping : Expression expression",
                "Literal  : Object value",
                "Unary    : Token operator, Expression right",
                "Logical  : Expression left, Token operator, Expression right",
                "Assign   : Token name, Expression value",
                "This     : Token keyword",
                "Conditional: Expression left, Expression thenBranch, Expression elseBranch"
        ));
    }

    private static void defineAst(String outputDir, String baseClassName, List<String> types) throws IOException {
        var path = outputDir + "/" + baseClassName + ".java";
        var writer = new PrintWriter(path, "UTF-8");
        writeBaseClassHeader(baseClassName, writer);
        writeBaseClassBody(baseClassName, types, writer);
        writeBaseClassFooter(writer);
    }

    private static void writeBaseClassHeader(String baseClassName, PrintWriter writer) {
        writer.println("package com.demointerpreter.grammar;");
        writer.println();
        writer.println("import com.demointerpreter.lexical_analyzer.Token;");
        writer.println();
        writer.println("public interface " + baseClassName + " {");
    }


    private static void writeBaseClassBody(String baseClassName, List<String> types, PrintWriter writer) throws IOException {
        writeAbstractAcceptMethod(writer);
        writVisitorInterface(writer, baseClassName, types);
        writeInnerClasses(writer, baseClassName, types);
    }

    private static void writeAbstractAcceptMethod(PrintWriter writer) throws IOException {
        writer.println();
        writer.println("    <R> R accept(Visitor<R> visitor);");
    }

    private static void writVisitorInterface(PrintWriter writer, String baseClassName, List<String> types) throws IOException {
        writer.println();
        writer.println("    interface Visitor<R> {");
        for (var type : types) {
            var typeName = type.split(":")[0].trim();
            writer.println("        R visit" + typeName + baseClassName + "(" + typeName + " " + baseClassName.toLowerCase() + ");");
        }
        writer.println("    }");
    }

    private static void writeInnerClasses(PrintWriter writer, String baseClassName, List<String> types) throws IOException {
        // The AST classes.
        for (var type : types) {
            var className = type.split(":")[0].trim();
            var fields = type.split(":")[1].trim();
            defineType(writer, baseClassName, className, fields);
        }

    }

    private static void defineType(PrintWriter writer, String baseClassName, String className, String fields) throws IOException {
        writeClassHeader(writer, baseClassName, className);
        writeFields(writer, fields);
        writeConstructor(writer, className, fields);
        writeAcceptMethodImplementation(writer, baseClassName, className);
        writeClassFooter(writer);
    }

    private static void writeClassHeader(PrintWriter writer, String baseClassName, String className) throws IOException {
        writer.println();
        writer.println("    class " + className + " implements " + baseClassName + " {");
    }

    private static void writeFields(PrintWriter writer, String fieldList) throws IOException {
        String[] fields = fieldList.split(", ");
        for (var field : fields) {
            writer.println("        public " + field + ";");
        }
        writer.println();
    }

    private static void writeConstructor(PrintWriter writer, String className, String fieldList) throws IOException {
        String[] fields = fieldList.split(", ");
        writer.println("        public " + className + "(" + fieldList + ") {");
        for (var field : fields) {
            var name = field.split(" ")[1];
            writer.println("                this." + name + " = " + name + ";");
        }
        writer.println("}");
    }

    private static void writeAcceptMethodImplementation(PrintWriter writer, String baseClassName, String className) {
        // Visitor pattern.
        writer.println();
        writer.println("        public <R> R accept(Visitor<R> visitor) {");
        writer.println("            return visitor.visit" + className + baseClassName + "(this);");
        writer.println("}");
    }


    private static void writeClassFooter(PrintWriter writer) throws IOException {
        writer.println(" }");
    }


    private static void writeBaseClassFooter(PrintWriter writer) throws IOException {
        writer.println(" }");
        writer.close();
    }
}
