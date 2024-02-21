package lox;

/* 
    파서는 토큰이 어떤 어휘소(lexim)를 갖는지, 어떤 키워드인지 알아야 함
*/
public enum TokenType {
    // 단일 문자 토큰
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,
    COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH, STAR,

    //문자 1개 || 2개짜리 토큰
    BANG, BANG_EQUAL,
    EQUAL, EQUAL_EQUAL,
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL,

    // 리터럴
    IDENTIFIER, STRING, NUMBER,

    // 키워드
    AND, CLASS, ELSE, FALSE, FUN, FOR, IF, NIL, OR,
    PRINT, RETURN, SUPER, THIS, TRUE, VAR, WHILE,

    EOF
}
