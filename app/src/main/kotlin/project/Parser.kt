package project

class Parser {
    fun exec(tokens: ArrayList<Token>): Node {
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
}

sealed interface Node {
    val _context: ArrayList<Node>
}

class Program(val body: ArrayList<Node>, override val _context: ArrayList<Node> = arrayListOf()) : Node

class NumberLiteral(val value: String, override val _context: ArrayList<Node> = arrayListOf()) : Node

class StringLiteral(val value: String, override val _context: ArrayList<Node> = arrayListOf()) : Node

class CallExpression(
    val name: String,
    val params: ArrayList<Node>,
    override var _context: ArrayList<Node> = arrayListOf()
) : Node