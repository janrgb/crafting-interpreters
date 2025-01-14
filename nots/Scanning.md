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
### Number literals
- We look for a digit to start the process. We consider fractional parts as well.
- Defined pattern: **cannot** start with a "." or end with a ".".
- We add another character of lookahead because we need to make sure there is a digit following any peek of ".".

## Reserved Words and Identifiers
### Maximal Munch
- We can't just match keywords like `or` the same way we might match `<=`. Consider the word `orca`.
- We need a *rule*: whichever grammar rule matches the most characters wins.
- So we begin by first assuming any lexeme starting with a letter/underscore is an **identifier**.

## Challenges
### Regular Grammars
- Production rules of the form A -> aB or A -> Ba.
- Python and Haskell require **context** to govern their code, such as indentation. This complexity cannot be captured by regular grammar rules: you would at least need a CFG.
### Space Power
- In CoffeeScript, parentheses around function calls are actually optional. BUT, *spaces* are used instead to separate arguments from the function name.
    - `add 1, 2`
- In C, spaces and newlines affect how macros work.
    - `#define square(x) ((x) * (x))`
- In Ruby, spaces distinguish between method calls.
    - `Array.new(1,2)` passes two numbers.
    - `Array.new (1,2)` passes a tuple! Yikes!
### To Discard or Not to Discard
- One thing I can think of personally is the parser using comments to offer suggestions for how to proceed with a particular block. Like using A.I. to fill in code based on user comments.
- I believe TODOs are a good example as well. A language might be able to capture a listing of TODOs which it could print out at the beginning of every run to remind developers on what they need to target.
### C-Style Block Comment Support
- At first this seems difficult, but it's actually not that hard.
- The main tool here is going to be **recursion**. We'll keep consuming characters when inside a block comment until we see the start of another block comment, in which we will call `multicomment()` again.
- If we run out of characters to consume, we've failed at closing our comments in one way or another.
- We need to make sure we test whether we're at the end of the file after returning from a recursive `multicomment()` so that we don't accidentally advance one more than we should.
- You can see in the code that we advance *in pairs.* This is because two characters make up a block comment, a STAR and SLASH.
- Honestly, this challenge combines a lot of concepts: two-character lookahead, recursion, and selective consumption.