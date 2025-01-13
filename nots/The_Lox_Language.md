# [The Lox Language](https://craftinginterpreters.com/the-lox-language.html#top)
This is a reference for everything we need to know about implementing our flavor of Lox. Lox is a C-style language, so much of the creativity is derived from that.

## Data Types

- Booleans
    - true
    - false
- Numbers
    - Double precision floating point
- Strings
- Nil
    - Equivalent to "null"

## Expressions
- Arithmetic
    - Addition
        - Concatenation
    - Subtraction
        - Prefix via negation
    - Multiplication
    - Division
- Comparison and Equality
    - <, <=, >, >=, ==, !=
    - Differing types are *never* equal
- Logical Operators
    - !, and, or
    - Short-Circuit
- Precedence and Grouping
    - C-like
- <p style="color:cyan">Bitwise, Shift, Modulo, Conditional (BONUS)</p>

## Statements
- `print "Hello, world!";`
- An expression followed by a ";" makes an **expression statement.**
- Pack statements with curly braces.
    ``` 
    {
        print "One statement.";
        print "Two statements.";
    }
    ```

## Variables
- Declare them using `var` statements.
- No initialization -> `nil`.

## Control Flow
- `if` statements
- `while` loops
- `for` loops

## Functions
- Looks the same as it does in C.
- `fun` defines functions
- Body is always a block. Inside, `return` a value
- Implicit `nil` return

## Closures
- `print identity(addPair)(1, 2)` will print "3"
- Can declare local functions inside another function.
    ```
    fun outerFunction() {
        fun localFunction() {
            print "I'm local!";
        }

        localFunction();
    }
    ```

## Classes
- The body of a class contains its methods.
    - Don't require `fun` keyword.
- Call it like a function to make an instance.
    - `var breakfast = Breakfast();`
- Free to add properties onto objects.
- Use `this` to access a field/method from within a method.
- Use `init(x1, x2, ...)` as a constructor/initializer.
- Specify a class that another inherits from using `<`.
    - The relationship consists of a **derived class** and **superclass.**
    - Every method defined in the superclass is also available to its subclass.
    - Use `super` to reference a parent constructor.

## Standard Library
- We've already seen `print`.
- We'll need a `clock()` to test optimizations.
- <p>That's it! <span style="color:cyan">We may want to improve on this later.</span></p>

## Challenges

### Missing Features
- The lack of operator overloading is a bit of a disappointment. But maybe it's for the best if Lox is just trying to be a tiny language.
- There is only single inheritance, not multiple inheritance.
- Making your own types might be useful, but that's only because I'm biased towards Typescript.
- No try? No catch? These are immensely useful for error handling.

### Qs About Syntax and Semantics
- Does every statement need to end with a semicolon?
- Is indentation strict? (I'd like it to be tbh)
- Are variables passed by reference or value? Since it's C-like I'd assume by value.
- What are the stipulations of naming? I'm guessing special characters are not allowed and vars cannot start with a number...
- What are the reserved phrases or keywords? Can "print" be a variable? I'd assume not.
- Could we have a local variable with the same name as a global variable? And if so, how do we differentiate between the two?

### Sample Lox Programs
