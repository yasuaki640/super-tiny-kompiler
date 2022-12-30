package project

class Visitor {
    fun visit(node: LispNode, parent: LispNode) {
        when (node) {
            is NumberLiteral -> {
                parent._context.add(NumberLiteral(node.value))
            }

            is StringLiteral -> {
                parent._context.add(StringLiteral(node.value))
            }

            is CallExpression -> {
                var expression = CCallExpression(Identifier(node.name), ArrayList())
                val lispArgs = expression.arguments.map { toNode(it) }
                node._context = ArrayList(lispArgs)
            }

            else -> {}
        }
    }

    private fun toNode(cNode: CNode): LispNode = when (cNode) {
        is CNumberLiteral -> NumberLiteral(cNode.value)
        is CStringLiteral -> StringLiteral(cNode.value)
        is CCallExpression -> {
            val lispArgs = cNode.arguments.map { toNode(it) }
            val params = ArrayList(lispArgs)
            CallExpression(cNode.callee.name, params)
        }

        else -> throw TypeCastException()
    }
}