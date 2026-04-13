//package com.compilerdesign.lox;
//
//// Challenge 2: Define a visitor class for our syntax tree classes that takes an expression, converts it
////to RPN, and returns the resulting string
//
//class RpnPrinter implements Expr.Visitor<String>{
//    String print(Expr expr){
//        return expr.accept(this);
//    }
//
//    @Override
//    public String visitLiteralExpr(Expr.Literal expr){
//        if(expr.value==null)return "nil";
//        return expr.value.toString();
//    }
//
//    @Override
//    public String visitGroupingExpr(Expr.Grouping expr){
//        return expr.accept(this);
//    }
//
//    @Override
//    public String visitUnaryExpr(Expr.Unary expr){
//        String right=expr.right.accept(this);
//        return right+" "+expr.operator.lexeme;
//    }
//
//    @Override
//    public String visitBinaryExpr(Expr.Binary expr){
//        String left=expr.left.accept(this);
//        String right=expr.right.accept(this);
//        return left+" "+right+" "+expr.operator.lexeme;
//    }
//
//    @Override
//    public String visitTernaryExpr(Expr.Ternary expr){
//        String condition = expr.condition.accept(this);
//        String thenBranch = expr.thenBranch.accept(this);
//        String elseBranch = expr.elseBranch.accept(this);
//        return condition+" "+thenBranch+" "+elseBranch+"?";
//    }
//
//
//    // Testing it out
//
////    public static void main(String[] args){
////        Expr expression=new Expr.Binary(
////                new Expr.Binary(
////                        new Expr.Literal(1),
////                        new Token(TokenType.PLUS,"+",null,1),
////                        new Expr.Literal(2)
////                ),
////                new Token(TokenType.STAR,"*",null,1),
////                new Expr.Binary(
////                        new Expr.Literal(4),
////                        new Token(TokenType.MINUS,"-",null,1),
////                        new Expr.Literal(3)
////                )
////        );
////
////        System.out.println(new RpnPrinter().print(expression));
////    }
//}
