package project.module

class Tokenizer {
    fun exec(input: String): ArrayList<Token> {
        var current = 0
        val tokens = arrayListOf<Token>()

        while (current < input.length) {
            var char = input[current]

            if (char == '(') {
                tokens.add(ParenToken("("))
                current++
                continue
            }

            if (char == ')') {
                tokens.add(ParenToken(")"))
                current++
                continue
            }

            if (char.isWhitespace()) {
                current++
                continue
            }

            if (char.isDigit()) {
                var value = ""

                while (char.isDigit()) {
                    value += char
                    char = input[++current]
                }

                tokens.add(NumberToken(value))
                continue
            }

            if (char == '"') {
                var value = ""

                char = input[++current]

                while (char != '"') {
                    value += char
                    char = input[++current]
                }
                char = input[++current]

                tokens.add(StringToken(value))

                continue
            }

            if (char.isLetter()) {
                var value = ""

                while (char.isLetter()) {
                    value += char
                    char = input[++current]
                }

                tokens.add(NameToken(value))

                continue
            }

            throw TypeCastException("I dont know what this character is: $char")
        }

        return tokens
    }
}

interface Token {
    val value: String
}

data class StringToken(override val value: String) : Token
data class NumberToken(override val value: String) : Token
data class ParenToken(override val value: String) : Token
data class NameToken(override val value: String) : Token

