# [Scanning](https://craftinginterpreters.com/scanning.html)

## The Framework
### Two ways of running code
- REPL: `runPrompt()`
- File: `runFile(String path)`
### Error handling
- Good practice to separate code *generating* errors and *reporting* them.
- No actual abstraction. Instead, just gives line no.

## Lexemes and Tokens
### Token type
- Use an enum to represent all the different flavors.
- **Literal values** can be converted to the living runtime object that'll be used by the interpreter later.

## Regular Languages and Expressions
### The Scanner Class
- Appends one final EOF token for cleanliness.
- `start` points to frst char in lexeme being scanned while `current` points at char being considered.
- `line` useful for errors.
- helper function for telling us whether we consumed every char.

## Recognizing Lexemes
### Single-Character Lexemes
- All we need to do is consume the next character and pick a token for it.
- `advance()` is consumption, it is for input.
- `addToken()` is for output, grabbing the text of the current lexeme and creating a new token for it.
### Lexical Errors
- We can use the `Lox.error` method we wrote to handle the `default` case.
- Since `hadError` is set, we will exit the system instead of executing code.
### Operators
- A little more complicated, will need to set up **lookahead** and **match** functionality for operators that may have an extra character of meaning. Like a conditional `advance()`.
- Skip over meaningless chars.
### String literals
- We need to look for an opening and closing quote.
- If we don't find the closing quote by the end of the file, we need to report an error.
- We also need the actual string *value*, which we procure using `substring` to chop off the quotes.
- <p style="color:cyan">We will add escape sequences at a later point! :red_circle: </p>
