# [Parsing Expressions](https://craftinginterpreters.com/parsing-expressions.html)
## Ambiguity
### Precedence Rules
| Name     | Operators      | Associates             |
|----------|----------------|------------------------|
| Equality | == !=          | Left                   |
| Comparison | > >= < <=    | Left                   |
| Term      | - +           | Left                   |
| Factor    | / *           | Left                   |
| Unary     | ! -           | Right                  |

- For `Factor`s, we will try not to use left-recursive rules, so the structure will follow:

    - `higher_rule -> lower_rule (( intermediary ) lower_rule )* ;`

## Recursive Descent Parsing
### Top-Down Parsing
| Grammar notation | Code Representation        |
|------------------|----------------------------|
| Terminal         | Code to match and consume a token |
| Nonterminal      | Call to that rule's function|
| \|               | `if` or `switch` statement  |
| `*` or `+`       | `while` or `for` loop       |
| ?                | `if` statement              |
### The parser class
- Each grammar rule becomes a method inside this new class.

- Each grammar rule will attempt to call rules of a higher precedence to cover its precedence *or higher*.

## Syntax Errors
### Hard Requirements
- **Detect and report the error:** We don't want improper ASTs.

- **Avoid crashing or hanging:** Users *use* the parser to learn what syntax makes sense, after all.
### Better Requirements
- **Fast:** milliseconds per keystroke.

- **Report a lot:** users want to see them all.

- **Minimize cascading errors:** don't scare the user.
    - These last two points contradict each other.
    - Consider **error recovery**.
### Panic Mode
- If a parser detects an error, it attempts to **synchronize**-- get forthcoming tokens aligned to the rules.

- Some rule in the grammar is marked as the *synchronization point*.

    - Jump out of nested prods until getting back to this rule checkpoint.

    - Then, discard tokens until reaching one that *can* appear at that point in the rule.

- Traditionally done *between* statements, which we will implement but not synchronize right now.
### Synchronizing a Recursive Descent Parser
- A "best effort" synchronizer would want to *throw* exceptions upon wanting to synchronize, and then *catch* them on statement boundaries.

## Challenges
### Comma Operator
- Lowest precedence of any C operator. As such, we should handle it right between `expression` and `equality`.

- Grammar looks like: `expression "," expression`
### Ternary Operator
- Higher precedence than expressions but lower precedence than comparisons. Shove it between `comma` and `equality`.

- Grammar looks like: `equality ( "?" expression ":" expression )?`

- This is more complex because we also need a `:` in the line following whatever expression follows the ? mark. If there is not one, we throw a special error.
### Arithmetic Error Productions
- Successfully match the erroneous syntax. But, we'll report it as an error instead of producing a syntax tree.

- Detect a binary operator appearing at the beginning of an expression. We'll augment the grammar rule to be `factor -> unary (("/" | "*" ) unary)* | ("/" | "*") error ;` and we will follow the same structure for every other rule.

- Putting it in `primary()` is a solution.