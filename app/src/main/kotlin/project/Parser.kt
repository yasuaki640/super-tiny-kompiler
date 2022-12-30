package project

class Parser {
    fun exec(tokens: ArrayList<Token>): LispNode {
        var current = 0

        fun walk(): LispNode {
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

        var ast = Program(ArrayList())

        while (current < tokens.size) {
            ast.body.add(walk())
        }

        return ast
    }
}

sealed interface LispNode {
    val _context: ArrayList<LispNode>
}

class Program(val body: ArrayList<LispNode>, override val _context: ArrayList<LispNode> = ArrayList()) : LispNode

class NumberLiteral(val value: String, override val _context: ArrayList<LispNode> = ArrayList()) : LispNode

class StringLiteral(val value: String, override val _context: ArrayList<LispNode> = ArrayList()) : LispNode

class CallExpression(
    val name: String,
    val params: ArrayList<LispNode>,
    override var _context: ArrayList<LispNode> = ArrayList()
) : LispNode