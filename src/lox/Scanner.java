package lox;

import java.util.ArrayList;
import java.util.List;
import static lox.TokenType.*;

public class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int current=0;  // 스캔 중인 렉심의 첫 번쨰 문자
    private int start=0;    // 현재 처리 중인 문자 
    private int line =1;    // curretn가 위치한 소스 줄 번호

    // 원시 소스코드를 단순 문자열로 저장
    Scanner(String source){
        this.source=source;
    }

    // 생성할 토큰을 하나씩 리스트에 채움
    List<Token> scanTokens(){
        while(!isAtEnd()){
            start = current;
            scanToken();
        }

        // EOF 토큰을 붙이는 이유 -> 파서가 깔끔해짐
        tokens.add(new Token(EOF, "",null,line));
        return tokens;
    }

    // 렉심 식별
    private void scanToken(){
        char c = advance();
        switch (c){
            case '(': addToken(LEFT_PAREN); break;
            case ')': addToken(RIGHT_PAREN); break;
            case '{': addToken(LEFT_BRACE); break;
            case '}': addToken(RIGHT_BRACE); break;
            case ',': addToken(COMMA); break;
            case '.': addToken(DOT); break;
            case '-': addToken(MINUS); break;
            case '+': addToken(PLUS); break;
            case ';': addToken(SEMICOLON); break;
            case '*': addToken(STAR); break;
            case '!':
                addToken(match('=')?BANG_EQUAL:BANG);
                break;
            case '=':
                addToken(match('=')?EQUAL_EQUAL:EQUAL);
                break;
            case '<':
                addToken(match('=')?LESS_EQUAL:LESS);
                break;
            case '>':
                addToken(match('=')?GREATER_EQUAL:GREATER);
                break;
            // 나눗셈 연산일 수도 있으나 주석일 수도 있음
            case '/':
                if (match('/')) {
                    while (peek()!='\n' && !isAtEnd()) advance();   // 주석인 경우 줄 끝까지 소비
                    // 파서는 주석을 처리하지 않으므로  addToken을 호출하지 않음
                }
                else{
                    addToken(SLASH);
                }
                break;

            case ' ':
            case '\t':
            case '\r':
                break;
            case '\n':
                line++;
                break;
            case '"': string(); break;

            // 안 쓰는 문자가 소스파일에 있는 경우
            default:
                // 숫자 리터럴
                if (isDigit(c)) {
                    number();
                }
                else {
                    Lox.error(line, "Unexpected character. ");
                }
                break;
        }
    }

    // 숫자 리터럴 처리
    private void number(){
        while (isDigit(peek())) advance();  // 정수부 소비

        // 소수부 소비 && . 바로 뒤에 숫자 존재 확인
        if(peek()=='.' && isDigit(peekNext())){
            advance();
            while (isDigit(peek())) advance();
        }
        addToken(NUMBER, Double.parseDouble(source.substring(start,current)));
    }
    // 문자 리터럴 처리
    private void string(){
        while (peek()!='"' && !isAtEnd()){
            if(peek()=='\n') line++;    // 여러 줄 문자열 지원
            advance();
        }
        // 닫는 따옴표가 나오기 전에 문자가 소진된 경우 -> 에러
        if(isAtEnd()){
            Lox.error(line, "Unterminated string.");
            return;
        }
        // 닫는 큰따옴표
        advance();

        // 앞 뒤 큰따옴표 제거
        String value = source.substring(start+1, current-1);
        addToken(STRING, value);
    }

    // 현재 문자가 찾으려는 문자와 매치되는 경우에만 소비
    private boolean match(char expected){
        if(isAtEnd()) return false;
        if(source.charAt(current)!=expected) return false;
        current++;
        return true;
    }

    // advance 함수와 유사하나 다음 문자를 소비하지 않음
    private char peek(){
        if(isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private char peekNext(){
        if(current+1 >= source.length()) return '\0';
        return source.charAt(current+1);
    }

    // 숫자 여부 체크
    private boolean isDigit(char c){
        return c>='0' && c<='9';
    }

    // 문자를 모두 소비했는지 체크하는 헬퍼
    private boolean isAtEnd(){
        return current>=source.length();
    }

    // 소스파일의 다음 문자 리턴 (입력)
    private char advance(){
        return source.charAt(current++);
    }

    // 현재 렉심의 텍스트를 가져와 그에 맞는 새 토큰 생성
    private void addToken(TokenType type){
        addToken(type,null);
    }
    private void addToken(TokenType type, Object literal){
        String text= source.substring(start, current);
        tokens.add(new Token(type,text,literal,line));
    }
}
