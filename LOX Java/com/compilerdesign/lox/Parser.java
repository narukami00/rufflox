package com.compilerdesign.lox;

import java.util.ArrayList;
import java.util.List;

import static com.compilerdesign.lox.TokenType.*;

class Parser {
    private static class ParseError extends RuntimeException{

    }

    private final List<Token> tokens;
    private int current=0;

    Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    List<Stmt> parse(){
        List<Stmt> statements=new ArrayList<>();
        while(!isAtEnd()){
            statements.add(declaration());
        }
        return statements;
    }

    private Stmt statement(){
        if(match(PRINT)) return printStatement();

        return expressionStatement();
    }

    private Stmt printStatement(){
        Expr value=expression();
        consume(SEMICOLON,"Expect ';' after value.");
        return new Stmt.Print(value);
    }

    private Stmt varDeclaration(){
        Token name=consume(IDENTIFIER,"Expect variable name.");

        Expr initializer=null;
        if (match(EQUAL)) {
            initializer=expression();
        }

        consume(SEMICOLON,"Expect ';' after initializer.");

        return new Stmt.Var(name, initializer);
    }

    private Stmt expressionStatement(){
        Expr expr=expression();
        consume(SEMICOLON,"Expect ';' after expression.");
        return new Stmt.Expression(expr);
    }

    private Expr expression(){
        //return equality();

        //Challenge 5 - handling each binary operator (which cannot be unary)
        // without a left hand operand - but also parse and discard a right
        // hand operand with appropriate precedence

        //We use appropriate precedence level functions at each of the binary operators we find
        // because if we used expressions for everything, that would eat too much
        // of the following code, producing more errors

        // We are ensuring that the error correction only swallows exactly
        // what that specific operator would have taken if it were valid,
        // leaving the rest of the line intact for other corrections to do their job

        if(match(BANG_EQUAL,EQUAL_EQUAL)){
            error(peek(),"Binary operator at the start of expression.");
            equality();
            return null;
        }

        if(match(GREATER_EQUAL, GREATER, LESS, LESS_EQUAL)){
            error(peek(),"Binary operator at the start of expression.");
            comparison();
            return null;
        }

        if(match(PLUS)){
            error(peek(),"Binary operator at the start of expression.");
            term();
            return null;
        }

        if(match(SLASH,STAR)){
            error(peek(),"Binary operator at the start of expression.");
            factor();
            return null;
        }

        // Challenge 3: add support for C style comma seperated expressions
        return comma();
    }

    private Stmt declaration(){
        try{
            if(match(VAR))return varDeclaration();

            return statement();
        } catch(ParseError error){
            synchronize();
            return null;
        }
    }

    private Expr comma(){
        Expr expr = ternary();

        while(match(COMMA)){
            Token operator = previous();
            Expr right = ternary();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr ternary(){
        Expr expr = equality();

        if(match(QUESTION)){
            Token question = previous();
            Expr thenBranch=expression();
            consume(COLON, "Expect ':' after then branch of conditional expression.");
            Token colon = previous();
            Expr elseBranch=ternary();
            expr = new Expr.Ternary(expr, question, thenBranch, colon, elseBranch);
        }

        return expr;
    }

    private Expr equality(){
        Expr expr = comparison();
        while(match(BANG_EQUAL,EQUAL_EQUAL)){
            Token operator = previous();
            Expr right = comparison();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr comparison(){
        Expr expr = term();

        while(match(GREATER, GREATER_EQUAL, LESS. LESS_EQUAL)){
            Token operator = previous();
            Expr right = term();
            expr= new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr term(){
        Expr expr = factor();

        while(match(MINUS,PLUS)){
            Token operator = previous();
            Expr right = factor();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr factor(){
        Expr expr = unary();

        while(match(SLASH, STAR)){
            Token operator = previous();
            Expr right = unary();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr unary(){
        if(match(BANG, MINUS)){
            Token operator = previous();
            Expr right = unary();
            return new Expr.Unary(operator, right);
        }
        return primary();
    }

    private Expr primary(){
        if(match(FALSE)) return new Expr.Literal(false);
        if(match(TRUE)) return new Expr.Literal(true);
        if(match(NIL)) return new Expr.Literal(null);

        if(match(NUMBER, STRING)){
            return new Expr.Literal(previous().literal);
        }

        if(match(IDENTIFIER)){
            return new Expr.Variable(previous());
        }

        if(match(LEFT_PAREN)){
            Expr expr = expression();
            consume(RIGHT_PAREN, "Expect ')' after expression.");
            return new Expr.Grouping(expr);
        }

        throw error(peek(), "Expect expression.");
    }

    private boolean match(TokenType... types){
        for(TokenType type:types){
            if(check(type)){
                advance();
                return true;
            }
        }
        return false;
    }

    private Token consume(TokenType type, String message) {
        if(check(type)) return advance();

        throw error(peek(),message);
    }

    private boolean check(TokenType type){
        if(isAtEnd())return false;
        return peek().type==type;
    }

    private Token advance(){
        if(!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd(){
        return peek().type==EOF;
    }

    private Token peek(){
        return tokens.get(current);
    }

    private Token previous(){
        return tokens.get(current-1);
    }

    private ParseError error(Token token, String message){
        Lox.error(token, message);
        return new ParseError();
    }
    private void synchronize(){
        advance();

        while(!isAtEnd()){
            if(previous().type==SEMICOLON)return;

            switch(peek().type){
                case CLASS:
                case FUN:
                case VAR:
                case FOR:
                case IF:
                case WHILE:
                case PRINT:
                case RETURN:
                    return;
            }
            advance();
        }
    }

}
