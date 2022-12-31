package project.module

class Parser {
    fun exec(tokens: ArrayList<Token>): Program {
        var current = 0

        fun walk(): Node {
            var token = tokens[current]

            if (token is NumberToken) {
                current++

                return NumberLiteral(token.value)
            }

            if (token is StringToken) {
                current++

                return StringLiteral(token.value)
            }

            if (token is ParenToken &&
                token.value == "("
            ) {
                token = tokens[++current]

                val node = CallExpression(token.value, ArrayList())

                token = tokens[++current]

                // Loop until closing brackets appear
                while (!(token is ParenToken && token.value == ")")) {
                    node.params.add(walk())
                    token = tokens[current]
                }

                current++

                return node
            }

            throw TypeCastException(token.value)
        }

        val ast = Program(arrayListOf())

        while (current < tokens.size) {
            ast.body.add(walk())
        }

        return ast
    }
}

sealed interface Node {
    var _context: ArrayList<CNode>
}

data class Program(val body: ArrayList<Node>, override var _context: ArrayList<CNode> = arrayListOf()) : Node

data class NumberLiteral(val value: String, override var _context: ArrayList<CNode> = arrayListOf()) : Node

data class StringLiteral(val value: String, override var _context: ArrayList<CNode> = arrayListOf()) : Node

data class CallExpression(
    val name: String,
    val params: ArrayList<Node>,
    override var _context: ArrayList<CNode> = arrayListOf()
) : Node