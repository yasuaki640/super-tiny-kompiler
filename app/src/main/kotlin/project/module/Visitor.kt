package project.module

class Visitor {
    val enter = fun(node: Node, parent: Node) {
        when (node) {
            is NumberLiteral -> {
                parent._context.add(CNumberLiteral(node.value))
            }

            is StringLiteral -> {
                parent._context.add(CStringLiteral(node.value))
            }

            is CallExpression -> {
                var expression = CCallExpression(Identifier(node.name), ArrayList())
                node._context = expression.arguments

                if (parent !is CallExpression) {
                    val expressionStatement = CExpressionStatement(expression)
                    parent._context.add(expressionStatement)
                } else {
                    parent._context.add(expression)
                }
            }

            else -> {}
        }
    }
}