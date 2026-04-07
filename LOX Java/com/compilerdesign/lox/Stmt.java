package com.compilerdesign.lox;

import java.util.List;

abstract class Stmt {
    interface Visitor <R> {
        R visitExpressionStmt(Expression stmt);
        R visitPrintStmt(Print stmt);
        R visitVarStmt(Var stmt);
    }
 static class Expression extends Stmt {
    Expression(Expr expression ) {
      this.expression = expression;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitExpressionStmt(this);
    }

    final Expr expression;
}
 static class Print extends Stmt {
    Print(Expr expression ) {
      this.expression = expression;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitPrintStmt(this);
    }

    final Expr expression;
}
 static class Var extends Stmt {
    Var(Token name, Expr initializer ) {
      this.name = name;
      this.initializer = initializer;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitVarStmt(this);
    }

    final Token name;
    final Expr initializer;
}

    abstract <R> R accept(Visitor<R> visitor);
}
