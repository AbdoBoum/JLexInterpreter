package com.demointerpreter.interpreter;

import java.util.*;

public class NativeFunctions {
      private static Map<String, LoxCallable> nativeFunctions = new HashMap<>();

      public NativeFunctions() {
          nativeFunctions.put("clock", new LoxCallable() {
              @Override
              public Object call(Interpreter interpreter, List<Object> args) {
                  return (double)System.currentTimeMillis()/1000;
              }

              @Override
              public int arity() {
                  return 0;
              }
          });
          nativeFunctions.put("Date", new LoxCallable() {
              @Override
              public Object call(Interpreter interpreter, List<Object> args) {
                  return new Date();
              }

              @Override
              public int arity() {
                  return 0;
              }
          });
          nativeFunctions.put("scan", new LoxCallable() {
              @Override
              public Object call(Interpreter interpreter, List<Object> args) {
                  return new Scanner(System.in).next();
              }

              @Override
              public int arity() {
                  return 0;
              }
          });
      }

    public Map<String, LoxCallable> getNativeFunctions() {
        return nativeFunctions;
    }
}
