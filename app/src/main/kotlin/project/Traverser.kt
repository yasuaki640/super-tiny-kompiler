package project;

class Traverser {
    fun exec(ast: Program, visitor: (node: Node, parent: Node) -> Unit) {
        traverseNode(ast, visitor)
    }

    private fun traverseArray(children: ArrayList<Node>, parent: Node) {
        children.forEach { traverseNode(it, parent) }
    }

    private fun traverseNode(node: Node, parent: Any) {
        // If there is an `enter` method for this node type we'll call it with the
        // `node` and its `parent`.
        TODO("execute enter if exists")

        when (node) {
            is Program -> traverseArray(node.body, node)
            is CallExpression -> traverseArray(node.params, node)
            is NumberLiteral -> {}
            is StringLiteral -> {}
        } // sealed interfaceかつ、未確認のtokenは前処理で弾いてるからelseは不要


        // If there is an `exit` method for this node type we'll call it with the
        // `node` and its `parent`.
        TODO("execute exit if exists")
    }
}