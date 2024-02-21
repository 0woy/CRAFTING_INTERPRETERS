package lox;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Lox {
    static boolean hadERROR = false;    // 에러 식별

    public static void main(String[] args) throws IOException{
        if(args.length>1){
            System.out.println("Usage: jlox [script]");
            System.exit(64);
        }else if(args.length==1){
            runFile(args[0]);
        }else{
            runPrompt();
        }
    }
    
    // 명령줄에서 제이록스를 기동할 때 파일 경로를 지정하여 스크립트 파일을 실행
    public static void runFile(String path) throws  IOException{
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));

        if(hadERROR) System.exit(65);
    }
    
    // 대화형으로 실행하여, 인수 없이 제이록스 기동 -> 한 줄씩 코드를 입력해서 실행하는 프롬프트 표시
    public static void runPrompt() throws IOException{
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        for(;;){
            System.out.println(">");
            String line = reader.readLine();
            if(line == null) break;
            run(line);

            // 대화형 루프인 경우는 flag 리셋
            hadERROR=false;
        }
    }

    public static void run(String source){
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        for(Token token: tokens){
            System.out.println(token);
        }
    }

    // 에러 처리
    static void error(int line, String message) {
        report(line, "", message);
    }

    // 에러를 알리는 함수는 에러를 발생한 코드와 분리하는 것이 좋다.
    private static void report(int line, String where, String message){
        System.err.println(
                "[line "+ line+"] Error"+where+": "+message);
        hadERROR = true;
    }
}
