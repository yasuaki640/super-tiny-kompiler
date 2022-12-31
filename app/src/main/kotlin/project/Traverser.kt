package project

class Traverser(val visitor: (Node, Node) -> Unit) {
    fun exec(ast: Program) {
        traverseNode(ast, null)
    }

    private fun traverseArray(children: ArrayList<Node>, parent: Node) {
        children.forEach { traverseNode(it, parent) }
    }

    private fun traverseNode(node: Node, parent: Node?) {
        // If there is an `enter` method for this node type we'll call it with the
        // `node` and its `parent`.
        parent?.let { visitor(node, it) }

        when (node) {
            is Program -> traverseArray(node.body, node)
            is CallExpression -> traverseArray(node.params, node)
            is NumberLiteral -> {}
            is StringLiteral -> {}
        }


        // The exit process does not exist in this project.

    }
}

sealed interface CNode

class CProgram(val body: ArrayList<CNode>) : CNode

class CNumberLiteral(val value: String) : CNode

class CStringLiteral(val value: String) : CNode

class Identifier(val name: String) : CNode

class CCallExpression(
    val callee: Identifier,
    val arguments: ArrayList<CNode>
) : CNode

class CExpressionStatement(val expression: CCallExpression) : CNode