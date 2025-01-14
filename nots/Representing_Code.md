# [Representing Code](https://craftinginterpreters.com/representing-code.html)
## Overview
### Contents
- Theory of formal grammars
- Functional and OOP
- Design patterns
- Metaprogramming

## Context-Free Grammars
### Formal Grammars
| Terminology | Lexical Grammar | Syntactic Grammar |
|-------------|-----------------|-------------------|
| The "alphabet" is...    | Characters      | Tokens            |
| A "string" is...      | Lexeme or token | Expression        |
| Implemented by... | Scanner         | Parser            |

- Specify which strings are *valid or invalid*.
### Lox's Grammar
- **Literals**: Numbers, strings, Booleans, and `nil`.
- **Unary expressions**: A prefix ! for not and - to negate.
- **Binary expressions**: Infix arithmetic and logic ops.
- **Parentheses**: A pair wrapped around an expression.

```
expression  -> literal 
            | unary 
            | binary 
            | grouping ;
literal     -> NUMBER | STRING | "true" | "false" 
              | "nil" ;
grouping    -> "(" expressions ")" ;
unary       -> ( "-" | "!" ) expression ;
binary      -> expression operator expression ;
operator    -> "==" | "!=" | "<" | "<=" | ">" | ">="
               | "+" | "-" | "*" | "/" ;
```

## Implementing ASTs
### Script for Generation
- We'll write a script in a separate package to help generate `Expr.java`, which implements Lox's grammar.

## Working with Trees
### The Expression Problem
- Having a long chain of `if`s is inefficient.
- For an OOL like Java, maybe try making an abstract `interpret()` method on `Expr` which each subclass then implements? Well, this scales poorly.
- Well, it's still difficult to add a new operation. Conversely, functional programming makes it easy to add new operations, but hard to add new types.
- Each style has a "grain", and notice that neither makes it easy to add *both* types and operations.
### [The Visitor Pattern](https://en.wikipedia.org/wiki/Visitor_pattern)
- Add a layer of indirection to help define operations.
- We have an interface which classes implement to consolidate all operations in one place. This is the *visitor interface*.
- To route the correct method to a visitor based on its type, we use an *accept* method which is overriden for each subclass. The accept method just calls the appropriate visitor.
- We can use this *accept* method for as many visitors as we want.
### Visitors for Expressions
- We first define the visitor interface, `defineVisitor(writer, baseName, types)`, and put it in the base class. It will generate the visitor interface.
    - We iterate through every subclass and declare a visit method for each one.
- We also define an abstract `accept()` method in the base class, which is overridden in `defineType()` as a method following the constructor.

## A (Not Very) Pretty Printer
### AstPrinter Implementation
- 