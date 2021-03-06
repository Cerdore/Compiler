import ASMPackage.ASMGenerater;
import ASMPackage.ASMSentence;
import MiddleDataUtilly.QT;
import OptimizePackage.Optimizer;
import TranslatorPackage.MiddleLangTranslator;
import TranslatorPackage.SymbolTable.SymbolTableManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Compile {
    public static StringBuilder msg;
    public static StringBuilder asms;
    private static Parser parser;
    private static StringBuilder input;
    private static Optimizer optimizer;
    private static ASMGenerater asmGenerater;
    public static void main(String[] args) throws Exception{
        msg = new StringBuilder("");
        asms = new StringBuilder("");

        parser = new Parser();

        input = new StringBuilder("");
        String filename =
                (args.length == 0) ? "/media/troublor/OS/JavaProject/Compiler/test1" : args[0];
        boolean debug = false;
        if (args.length == 1) {
            if (!args[0].startsWith("-")) {
                filename = args[0];
            } else {
                debug = args[0].equals("-d");
            }
        } else if (args.length == 2) {
            debug = args[0].equals("-d");
            filename = args[1];
        }
        File file = new File(filename);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                input.append(tempString).append("$");//$用来表示行尾
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        parser.setSourceCode(input.toString());

        try {
            parser.setDebug(debug);
            parser.LL1Analyze();
            MiddleLangTranslator translator = parser.getTranslator();
            if (debug) {
                String all_qts = translator.printAllQTs();
                System.out.println(all_qts);
                msg.append(all_qts).append("\n");
            }
            optimizer = new Optimizer(parser.getAllQTs());
            ArrayList<QT> qts = optimizer.optimize();
            if (debug) {
                System.out
                        .println("\n\n优化后的所有四元式:\n" + parser.getAllQTs().size() + " => " + qts.size());
                msg.append("\n\nQTs after optimization:\n").append(parser.getAllQTs().size()).append(" => ").append(qts.size()).append("\n");
                System.out.println(String.format("%-11s%-25s%-25s%-25s", "oprt:", "left_oprd:", "right_oprd:", "result_target:"));
                msg.append(String.format("%-11s%-25s%-25s%-25s", "oprt:", "left_oprd:", "right_oprd:", "result_target:")).append("\n");
                for (QT qt : qts) {
                    System.out.println(qt);
                    msg.append(qt).append("\n");
                }

                SymbolTableManager symbolTableManager = parser.getSymbolTableManager();
                String variable_table_output = symbolTableManager.printAllVariable();
                System.out.println(variable_table_output);
                msg.append("\n").append(variable_table_output).append("\n");
            }
            asmGenerater = new ASMGenerater(qts, parser.getSymbolTableManager());
            List<ASMSentence> asmSentences = asmGenerater.generate();
            System.out.println("\n\n以下是生成的汇编源码: ");
            msg.append("\n\nASM codes are as follows: ").append("\n");
            for (ASMSentence asm : asmSentences) {
                System.out.println(asm);
                msg.append(asm).append("\n");
                asms.append(asm).append("\n");
            }
            System.out.println("编译成功！");
            msg.append("Compile Success！").append("\n");
        } catch (Exception e) {
            msg.append("Compile Failed！\n");
            System.out.println(e.getMessage());
            msg.append(e.getMessage());
            e.printStackTrace();
            throw e;
         }
    }


}
