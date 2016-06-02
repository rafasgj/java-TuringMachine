# Turing Machine

A simple implementation of a Turing Machine in Java.

## Rules

* Programs run in an infinite loop.
* Programs may have code blocks. Each code block run in an infinite loop.
* Machine always start at the first tape position.
* Tape size is limited to 10 (ten) positions. (TODO: configurable)

### Commands:
* > : move to the next tape position.
* < : move to the previous tape position.
* + : add 1 to the current tape position.
* - : subtract 1 from the current tape position.
* { : start a new code block.
* } : end the current code block.
* ? : execute the next command or code block if the current tape position
is not equal to 0 (zero).
* x : break out of the current code block.

