package project

data class Token(val type: String, val value: String)

fun tokenizer(input: String): ArrayList<Token> {
    var current = 0
    val tokens = arrayListOf<Token>()

    while (current < input.length) {
        var char = input[current]

        if (char == '(') {
            tokens.add(Token("paren", "("))
            current++
            continue
        }

        if (char == ')') {
            tokens.add(Token("paren", ")"))
            current++
            continue
        }

        if (char.isWhitespace()) {
            current++;
            continue;
        }

        if (char.isDigit()) {
            var value = ""

            while (char.isDigit()) {
                value += char
                char = input[++current]
            }

            tokens.add(Token("number", value))
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

            tokens.add(Token("string", value))

            continue
        }

        if (char.isLetter()) {
            var value = ""

            while (char.isLetter()) {
                value += char
                char = input[++current]
            }

            tokens.add(Token("name", value))

            continue
        }

        throw TypeCastException("I dont know what this character is: $char")
    }

    return tokens
}

interface Node

class Program(val body: ArrayList<Node>) : Node

class NumberLiteral(val value: String) : Node

class StringLiteral(val value: String) : Node

class CallExpression(val name: String, val params: ArrayList<Node>) : Node

fun parser(tokens: ArrayList<Token>): Node {
    var current = 0

    fun walk(): Node {
        var token = tokens[current]

        if (token.type == "number") {
            current++

            return NumberLiteral(token.value)
        }

        if (token.type === "string") {
            current++

            return StringLiteral(token.value)
        }

        if (token.type == "paren" &&
            token.value == "("
        ) {
            token = tokens[++current]

            val node = CallExpression(token.value, ArrayList())

            token = tokens[++current]

            // Loop until closing brackets appear
            while (!(token.type == "paren" && token.value == ")")) {
                node.params.add(walk())
                token = tokens[current]
            }

            current++

            return node
        }

        throw TypeCastException(token.type)
    }

    var ast = Program(arrayListOf())

    while (current < tokens.size) {
        ast.body.add(walk())
    }

    return ast
}

