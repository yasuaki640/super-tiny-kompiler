package project.module

class CodeGenerator {
    fun exec(node: CNode): String = when (node) {
        is CProgram -> node.body.joinToString("\n") { exec(it) }
        is CExpressionStatement -> "${exec(node.expression)};"
        is CCallExpression -> exec(node.callee) +
                "(" +
                node.arguments.joinToString(", ") { exec(it) } +
                ")"

        is Identifier -> node.name
        is CNumberLiteral -> node.value
        is CStringLiteral -> "\"${node.value}\""
    }
}